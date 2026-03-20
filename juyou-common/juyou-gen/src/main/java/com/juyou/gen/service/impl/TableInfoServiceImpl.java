package com.juyou.gen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juyou.common.utils.CommonUtils;
import com.juyou.gen.dto.tableinfo.*;
import com.juyou.gen.entity.TableInfo;
import com.juyou.gen.mapper.TableInfoMapper;
import com.juyou.gen.service.TableInfoService;
import com.juyou.gen.utils.Generator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TableInfoServiceImpl extends ServiceImpl<TableInfoMapper, TableInfo> implements TableInfoService {


    @Autowired
    Generator generator;

    @Override
    public IPage<DbTableDto> pageList(Page<DbTableDto> page, QueryWrapper<DbTableDto> queryWrapper){
        queryWrapper.apply("table_schema=(SELECT DATABASE())");
        queryWrapper.select("table_name","table_schema","table_type","create_time","update_time");
        queryWrapper.orderByAsc("create_time");
        return baseMapper.pageList(page,queryWrapper);
    }

    @Override
    public List<TableInfoColumnDto> getByIdInfo(String tableName) {
        QueryWrapper<TableInfoColumnDto> dbWrapper=new QueryWrapper<>();
        dbWrapper.apply("table_schema=(SELECT DATABASE())").eq("table_name",tableName);
        dbWrapper.select("table_name","column_name","column_type","column_comment");
//        dbWrapper.orderByAsc("create_time");
        List<TableInfoColumnDto> list=baseMapper.dbTableInfo(dbWrapper);
        if(list.isEmpty()) return list;

        //组装自定义数据
        LambdaQueryWrapper<TableInfo> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(TableInfo::getTableName,tableName).orderByAsc(TableInfo::getColumnName);
        List<TableInfo> tableInfos=baseMapper.selectList(queryWrapper);
        if(!tableInfos.isEmpty()) {
            Map<String,TableInfo> map=tableInfos.stream().collect(Collectors.toMap(TableInfo::getColumnName,entity->entity));
            list.forEach(item->{
                TableInfo temp=map.get(item.getColumnName());
                if(temp!=null){
                    BeanUtils.copyProperties(temp,item);
                }
            });
        }
        list.forEach(item->{
            if(StringUtils.hasLength(item.getColumnComment()) && !StringUtils.hasLength(item.getFieldDesc()))
                item.setFieldDesc(item.getColumnComment());
        });
        return list;
    }

    @Override
    public String createAndZip(CreateDto createDto) throws Exception {
        return generator.zipCreateAuto(createDto.getModuleName(),createDto.getTableNames());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TableInfoSaveDto tableInfoSaveDto) {
        //删除表字段配置
        LambdaUpdateWrapper<TableInfo> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.eq(TableInfo::getTableName,tableInfoSaveDto.getTableName());
        remove(updateWrapper);

        List<TableInfoColumnInfoDto> columnDtos=tableInfoSaveDto.getTableInfoColumnInfoDto();
        List<TableInfo> tableInfos=new ArrayList<>();
        columnDtos.forEach(item->{
            TableInfo tableInfo=new TableInfo();
            BeanUtils.copyProperties(item,tableInfo);
            tableInfo.setTableName(tableInfoSaveDto.getTableName());
            tableInfos.add(tableInfo);
        });
        saveBatch(tableInfos);
    }

    @Override
    public List<ShowTableFilesDto> showTableFiles(String tableName) throws Exception {
        LambdaQueryWrapper<TableInfo> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.select(TableInfo::getModuleName).eq(TableInfo::getTableName,tableName).isNotNull(TableInfo::getModuleName).last("limit 1");
        TableInfo tableInfo = baseMapper.selectOne(queryWrapper);

        String moduleName="temp";
        if(tableInfo!=null && StringUtils.hasLength(tableInfo.getModuleName())) moduleName=tableInfo.getModuleName();

        generator.createAuto(moduleName,new String[]{tableName});

        List<ShowTableFilesDto> showTableFilesDtos=new ArrayList<>();

        GenConfig genConfig=generator.getGenConfig();
        String javaPath=genConfig.getOutputDir()+ File.separator+genConfig.getParent().replace(".","\\")+File.separator+genConfig.getModuleName()+File.separator;
        String entity=generator.tableNameToEntityName(tableName,genConfig);

        Map<String,String> javaMap=new LinkedHashMap<>();
        javaMap.put("controller","controller"+File.separator+entity+"Controller.java");
        javaMap.put("entity","entity"+File.separator+entity+".java");
        javaMap.put("mapper","mapper"+File.separator+entity+"Mapper.java");
        javaMap.put("service","service"+File.separator+entity+"Service.java");
        javaMap.put("serviceImpl","service"+File.separator+"impl"+File.separator+entity+"ServiceImpl.java");
        javaMap.put("pageDto","dto"+File.separator+entity.toLowerCase()+File.separator+entity+"PageDto.java");
        javaMap.put("addDto","dto"+File.separator+entity.toLowerCase()+File.separator+entity+"AddDto.java");
        javaMap.put("editDto","dto"+File.separator+entity.toLowerCase()+File.separator+entity+"EditDto.java");
        javaMap.put("listDto","dto"+File.separator+entity.toLowerCase()+File.separator+entity+"ListDto.java");
        javaMap.put("index.vue","vue"+File.separator+entity.toLowerCase()+File.separator+"index.vue");
        javaMap.put("popup.vue","vue"+File.separator+entity.toLowerCase()+File.separator+"popup.vue");

        readFiles(showTableFilesDtos,javaPath,javaMap);

        String xmlPath=genConfig.getOutputDir()+ File.separator+"resources"+File.separator+"mapper"+File.separator+moduleName+File.separator;
        Map<String,String> xmlMap=new LinkedHashMap<>();
        xmlMap.put("mapperXml",entity+"Mapper.xml");
        readFiles(showTableFilesDtos,xmlPath,xmlMap);

        return showTableFilesDtos;
    }

    @Override
    public void down(String filePath, HttpServletResponse response) {
        String zipPath=generator.getGenConfig().getZipPath();
        localView(new File(zipPath,filePath).getAbsolutePath(),response);
    }


    /**
     * 显示，下载文件
     * @param filePath
     * @param response
     */
    public void localView(String filePath, HttpServletResponse response){
        // 其余处理略
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            File file = new File(filePath);
            if(!file.exists()){
                response.setStatus(404);
                throw new RuntimeException(file.getAbsolutePath()+":文件不存在..");
            }
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=" + new String(file.getName().getBytes("UTF-8"),"iso-8859-1"));
            inputStream = new BufferedInputStream(new FileInputStream(filePath));
            outputStream = response.getOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            response.flushBuffer();
        } catch (IOException e) {
            log.error("预览文件失败" + e.getMessage());
            response.setStatus(404);
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 读取文件类容
     * @param showTableFilesDtos
     * @param parentPath
     * @param filePaths
     */
    private void  readFiles(List<ShowTableFilesDto> showTableFilesDtos,String parentPath,Map<String,String> filePaths){
        Iterator<Map.Entry<String, String>> iterator = filePaths.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String key =  entry.getKey();
            String value = entry.getValue();
            ShowTableFilesDto showTableFilesDto=new ShowTableFilesDto();
            showTableFilesDto.setFileName(key);
            try {
                String text= CommonUtils.readTxt(new File(parentPath,value).getAbsolutePath());
                if(StringUtils.hasLength(text))
                    showTableFilesDto.setFileContent(text);
                else
                    showTableFilesDto.setFileContent("读取失败，请尝试重新生成");
            }catch (Exception e){
                showTableFilesDto.setFileContent("读取失败，请尝试重新生成");
            }
            showTableFilesDtos.add(showTableFilesDto);
        }


    }

}