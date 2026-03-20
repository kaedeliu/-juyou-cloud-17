package com.juyou.gen.utils;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.juyou.common.utils.ZipUtils;
import com.juyou.gen.dto.tableinfo.CustomTableColumn;
import com.juyou.gen.dto.tableinfo.GenConfig;
import com.google.common.base.CaseFormat;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

/**
 * 代码生成
 *
 * @author kaedeliu
 */
@Component
public class Generator {

	final static String  COLUMN_NAME="column_name";

	final static String  TABLE_NAME="table_name";

	@Autowired
	GenConfigUtils genConfigUtils;

	GenConfig genConfig;

	public  String zipCreateAuto(String moduleName,String[] tables) throws Exception {
		return zipCreateAuto(genConfigUtils.getConfig(moduleName,tables));
	}

	/**
	 * 生成代码并压缩ZIP文件
	 * @param genConfig
	 * @return
	 */
	public  String zipCreateAuto(GenConfig genConfig) throws Exception {

		FileUtils.deleteDirectory(new File(genConfig.getZipPath()));
		if(!new File(genConfig.getZipPath()).exists()){
			new File(genConfig.getZipPath()).mkdirs();
		}

		createAuto(genConfig);
		String zipName=System.currentTimeMillis()+ new Random().nextInt(100)+".zip";
		String zipPath=genConfig.getZipPath() + File.separator + zipName;
		ZipUtils.toZip(new String[]{genConfig.getOutputDir()},zipPath,true);
		return zipName;
	}

	public void createAuto(String moduleName,String[] tables) throws IOException {
		GenConfig genConfig1=genConfigUtils.getConfig(moduleName,tables);
		//删除目录下文件
		FileUtils.deleteDirectory(new File(genConfig1.getOutputDir()));
		if(!new File(genConfig1.getOutputDir()).exists()){
			new File(genConfig1.getOutputDir()).mkdirs();
		}
		createAuto(genConfig1);
	}
	/**
	 * 生成代码
	 * @param genConfig
	 */
	public void createAuto(GenConfig genConfig){
		this.genConfig=genConfig;

		DataSourceConfig dataSourceConfig=new DataSourceConfig.Builder(genConfig.getUrl(), genConfig.getUsername(), genConfig.getPassword())
				.typeConvertHandler((globalConfig, typeRegistry, metaInfo)->{
					int typeCode = metaInfo.getJdbcType().TYPE_CODE;
					if (typeCode == Types.TINYINT) {
						// 自定义类型转换
						return DbColumnType.INTEGER;
					}
					return typeRegistry.getColumnType(metaInfo);
				}).build();
		AutoGenerator gen=new AutoGenerator(dataSourceConfig);
		GlobalConfig globalConfig=new GlobalConfig.Builder().author("yx") // 设置作者
				.enableSwagger() // 开启swagger模式
				.fileOverride() // 覆盖已生成文件
				.dateType(DateType.ONLY_DATE) // 配置时间类型策略
				.commentDate("yyyy-MM-dd HH:mm:ss") // 设置时间格式
				.disableOpenDir()// 文件生成完成不打开资源目录
//					.outputDir("D:\\test\\number"); // 指定输出目录
				.outputDir(genConfig.getOutputDir())

				.build();
		gen.global(globalConfig);

		Map<OutputFile,String> pathInfo=new HashMap<>();

		PackageConfig.Builder packageBuilder=new PackageConfig.Builder();
		packageBuilder.parent(genConfig.getParent()) // 设置父包名
				.moduleName(genConfig.getModuleName()) // 设置父包模块名
//					.moduleName("admin") // 设置父包模块名
				.entity("entity")
				.pathInfo(Collections.singletonMap(OutputFile.xml,genConfig.getOutputDir()+ File.separator + "resources/mapper/"+genConfig.getModuleName()+"/"));  //XML路径
		gen.packageInfo(packageBuilder.build());

		StrategyConfig.Builder strategyBulder=new StrategyConfig.Builder();
		strategyBulder.entityBuilder() // 实体策略

				.enableTableFieldAnnotation()// 开启生成实体时生成字段注解
				// .superClass(BaseModel.class)// 设置父类
				.enableLombok() // 开启lombok模型
				.controllerBuilder()// controller策略配置
				// .superClass(BaseController.class).enableRestStyle() //
				// 开启生成@RestController控制器
				.serviceBuilder()// Service策略配置
				// .superServiceImplClass(BaseServiceImpl.class) //
				// 设置service实现类父类
				.formatServiceFileName("%sService")// 格式化命名
				.mapperBuilder() // Mapper策略配置
				.enableMapperAnnotation();
		if(StringUtils.hasLength(genConfig.getSuperClass()))
			strategyBulder.entityBuilder().superClass(genConfig.getSuperClass());
		if(StringUtils.hasLength(genConfig.getSuperEntityColumns()))
			strategyBulder.entityBuilder().addSuperEntityColumns(genConfig.getSuperEntityColumns().split(","));

		if(genConfig.getTablePrefixs()!=null && genConfig.getTablePrefixs().length>0)
			strategyBulder.addTablePrefix(genConfig.getTablePrefixs());
		if(genConfig.getTables()!=null && genConfig.getTables().length>0)
			strategyBulder.addInclude(genConfig.getTables());

		StrategyConfig strategyConfig=strategyBulder.build();
		gen.strategy(strategyConfig);


		TemplateConfig.Builder templateBuilder=new TemplateConfig.Builder();
		templateBuilder.entity(genConfig.getEntityTemplate());
		templateBuilder.service(genConfig.getServiceTemplate());
		templateBuilder.serviceImpl(genConfig.getServiceImplTemplate());
		if(StringUtils.hasLength(genConfig.getControllerTemplate()))
			templateBuilder.controller(genConfig.getControllerTemplate());
		if(StringUtils.hasLength(genConfig.getMapperTemplate()))
			templateBuilder.mapper(genConfig.getMapperTemplate());
		TemplateConfig templateConfig=templateBuilder.build();
		gen.template(templateConfig);

		Map<String,Map<String,CustomTableColumn>> customTable=getConsumerData(gen,genConfig);
		Map<String,Object> customData= new HashMap<>();
		customData.put("customTable", customTable);
		customData.put("genConfig",genConfig);

		InjectionConfig.Builder injectionBuilder =new InjectionConfig.Builder();

		//自定义表数据
		injectionBuilder.customMap(customData);
		if(genConfig.getPathInfo()!=null){
			injectionBuilder.customFile(genConfig.getPathInfo());
		}
		gen.injection(injectionBuilder.build());

		gen.execute(new EnhanceFreemarkerTemplateEngine());
	}

	/**
	 * 获取自定义表数据
	 * @param gen
	 * @param genConfig
	 * @return 表名,字段名,字段属性
	 */
	private static Map<String, Map<String, CustomTableColumn>> getConsumerData(AutoGenerator gen, GenConfig genConfig){
		Map<String, Map<String,CustomTableColumn>> map=new HashMap<>();
		Connection conn=gen.getDataSource().getConn();
		try {
			ResultSet resultSet = conn.prepareCall("select * from juyou_sys_table_info")
					.executeQuery();
			ResultSetMetaData metaData=resultSet.getMetaData();
			int count=metaData.getColumnCount();

			while (resultSet.next()){
				String tableName=resultSet.getString(TABLE_NAME);
				String columnName=resultSet.getString(COLUMN_NAME);
				Map<String,CustomTableColumn> tmap=null;
				if(map.containsKey(tableName)){
					tmap=map.get(tableName);
				}else{
					tmap=new HashMap<>();
					map.put(tableName,tmap);
				}
				CustomTableColumn customTableColumn=null;
				if(tmap.containsKey(columnName))
					customTableColumn=tmap.get(columnName);
				else{
					customTableColumn=new CustomTableColumn();
					tmap.put(columnName,customTableColumn);
				}
				Class<CustomTableColumn> clazz=CustomTableColumn.class;
				for (int i=1;i<=count;i++){
					String cName=metaData.getColumnName(i);
					String value=resultSet.getString(cName);
					String type=metaData.getColumnTypeName(i);
					Object val=null;
					if((type.equals("TINYINT") || type.equals("INT")) && StringUtils.hasLength(value))
						val=Integer.parseInt(value);
					else
						val=value;
					cName= CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,cName);
					try {
						if(val!=null) {
							Field field = clazz.getDeclaredField(cName);
							field.setAccessible(true);
							field.set(customTableColumn, val);
						}
					}catch (Exception e1){
						e1.printStackTrace();
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return map;
	}

	/**
	 * 将tableName转换为EntityName
	 * @param tableName
	 * @param genConfig
	 * @return
	 */
	public String tableNameToEntityName(String tableName,GenConfig genConfig){
		tableName=tablePrefixs(tableName,genConfig);
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL,tableName);
	}

	/**
	 * 去掉前缀
	 * @param tableName
	 * @param genConfig
	 * @return
	 */
	private String tablePrefixs(String tableName,GenConfig genConfig){
		if(genConfig.getTablePrefixs()!=null && genConfig.getTablePrefixs().length>0){
			for (String tablePrefix: genConfig.getTablePrefixs()){
				if(tableName.startsWith(tablePrefix)){
					tableName=tableName.replaceFirst(tablePrefix,"");
					break;
				}
			}
		}
		return tableName;
	}

	public GenConfig getGenConfig(){
		return genConfig;
	}

	public static void main(String[] args) {
		GenConfig genConfig=new GenConfig();
		genConfig.setUrl("jdbc:mysql://106.52.96.249:3306/chat_test?characterEncoding=utf-8&useSSL=false&serverTimezone=GMT");
		genConfig.setUsername("root");
		genConfig.setPassword("iD):R6wT0+RP");
		genConfig.setParent("com.juyou").setModuleName("mapper/gen").
				setOutputDir("d:\\test\\test2\\")
				.setEntityTemplate("/templates/entity.java")
				.setServiceTemplate("/templates/service.java")
				.setServiceImplTemplate("/templates/serviceImpl.java")
				.setControllerTemplate("/templates/controller.java");
		genConfig.setTables(new String[]{});
		genConfig.setTablePrefixs(new String[]{"cc_","yx_"});

		Map<String,String> pathInfo=new HashMap<>();
		pathInfo.put("dto/PageDto.java", "/templates/page.dto.java.ftl");
		pathInfo.put("dto/AddDto.java", "/templates/add.dto.java.ftl");
		pathInfo.put("dto/EditDto.java", "/templates/edit.dto.java.ftl");
		genConfig.setPathInfo(pathInfo);

		new Generator().createAuto(genConfig);
	}

	/**
	 * 代码生成器支持自定义[DTO\VO等]模版
	 */
	public static class EnhanceFreemarkerTemplateEngine extends FreemarkerTemplateEngine {
		@Override
		protected void outputCustomFile(@NotNull List<CustomFile> customFiles, @NotNull TableInfo tableInfo, @NotNull Map<String, Object> objectMap) {
			String entityName = tableInfo.getEntityName();
			String parentPaht = this.getPathInfo(OutputFile.parent);

			customFiles.forEach((customFile) -> {
						String paht="";
						String key=customFile.getFileName();
						if(key.contains("/")) {
							paht = key.substring(0, key.lastIndexOf("/"));
							key = key.substring(key.lastIndexOf("/")+1);
						}
						key=key.replace(".ftl","");
						String name=entityName;
						if(key.contains("vue")) name="";

						String fileName = String.format(parentPaht+File.separator + paht + File.separator +entityName.toLowerCase()  + File.separator +  name + "%s", key);
						this.outputFile(new File(fileName), objectMap, customFile.getTemplatePath(),true);

			});
		}

	}

}

