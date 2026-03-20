package com.juyou.storage.utils;

import com.juyou.common.exception.BaseException;
import com.juyou.common.utils.DateUtils;
import com.juyou.storage.alioss.AliOssUtils;
import com.juyou.storage.config.StorageConfig;
import com.juyou.storage.dto.StorageFileNameTypeEnum;
import com.juyou.storage.dto.StorageTypeEnum;
import com.juyou.storage.local.LocalUtils;
import com.juyou.storage.txcos.TxCosUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * 存储工具类
 */
@Component
@Slf4j
public class StorageUtils {

    @Autowired
    StorageConfig storageConfig;

    @Autowired
    LocalUtils localUtils;

    @Autowired
    AliOssUtils aliOssUtils;

    @Autowired
    TxCosUtils txCosUtils;


    /**
     * @功能 上传文件，暂时只处理图片，不考虑视频，其它类型文件
     * @Author kaedeliu
     * @创建时间 2026/3/18 10:49
     * @修改人 kaedeliu
     * @修改时间 2026/3/18 10:49
     * @Param
     * @return
     * @throws IOException
     **/
    public String upload(HttpServletRequest request) throws IOException {
        // 文件名称
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");// 获取上传文件对象
        String fileType = file.getContentType(); // 文件类型
        String maxLen=storageConfig.getMaxLen();
        if(storageConfig.getTypeMaxLen()!=null && storageConfig.getTypeMaxLen().containsKey(fileType)){
            String maxLenTemp=storageConfig.getTypeMaxLen().get(fileType);
            if(StringUtils.hasLength(maxLenTemp)) maxLen=maxLenTemp;

        }
        if (!checkFileSize(file.getSize(),  maxLen)) {
            throw BaseException.defaultCode(fileType + " 大小限制:" + maxLen);
        }
        String fileName =  getFileName(file);
        if (!checkFileSize(file.getSize(), maxLen)) {
            throw BaseException.defaultCode(fileType + " 大小限制:" + maxLen);
        }
        String returnPath=null;
        if (StorageTypeEnum.本地存储.val.equals(storageConfig.getStorageType())) { //本地存储
            returnPath =  localUtils.upLocal(file,fileName);
        } else if (StorageTypeEnum.阿里云OSS.val.equals(storageConfig.getStorageType())) {
            returnPath = aliOssUtils.upload(fileName,file.getInputStream());
        } else if (StorageTypeEnum.腾讯COS.val.equals(storageConfig.getStorageType())) {
            returnPath = txCosUtils.update(fileName,file.getInputStream(),file.getSize());
        }else{
            throw BaseException.defaultCode("存储方式错误:"+storageConfig.getStorageType());
        }
        return returnPath;
    }




    /**
     * 获取文件命名
     * @param file
     * @return
     */
    private String getFileName(MultipartFile file){
        String fileName = file.getOriginalFilename();
        String suffix="";
        Random random=new Random();
        if(fileName.contains(".")){
            suffix=suffix.substring(fileName.lastIndexOf("."));
        }
        if(StorageFileNameTypeEnum.原文件名.val.equals(storageConfig.getFileNameType())){
            return fileName;
        }else if(StorageFileNameTypeEnum.时间戳.val.equals(storageConfig.getFileNameType())){
            return System.currentTimeMillis()+random.nextInt(99)+suffix;
        }else if(StorageFileNameTypeEnum.日期_随机数.val.equals(storageConfig.getFileNameType())){
            return DateUtils.formatDate("yyyyMMddHHmmssSSS")+random.nextInt(99)+suffix;
        }else
            return  DateUtils.formatDate("yyyyMMdd") + File.separator+ DateUtils.formatDate("yyyyMMddHHmmssSSS")+random.nextInt(99)+suffix;
    }

    /**
     * 判断文件大小
     *
     * @param len
     *            文件长度
     * @param unit
     *            限制大小单位（B,KB,MB,G）
     * @return
     */
    public static boolean checkFileSize(Long len, String unit) {
        // long len = file.length();
        System.out.println(len + "  " + unit);
        double fileSize = 0;
        unit = unit.toUpperCase();
        String sizeStr = "";
        if (unit.endsWith("KB")) {
            fileSize = (double) len / 1024;
            sizeStr = unit.replace("KB", "");
        } else if (unit.endsWith("MB")) {
            fileSize = (double) len / 1048576;
            sizeStr = unit.replace("MB", "");
        } else if (unit.endsWith("G")) {
            fileSize = (double) len / 1073741824;
            sizeStr = unit.replace("G", "");
        } else {
            return false;
        }

        return Double.parseDouble(sizeStr) >= fileSize;
    }

    public static String stringFormat(String format, Map<String,String> args){
        if(!StringUtils.hasLength(format)) return format;
        if(args==null || args.isEmpty()) return format;
        Iterator<Map.Entry<String, String>> iterator = args.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> obj=iterator.next();
            String key=obj.getKey();
            String val=obj.getValue();
            format=format.replace("["+key+"]",val);
        }
        return format;
    }


    /**
     * 文件预览
     * @param filePath
     * @param response
     */
    public void localView(String filePath, HttpServletResponse response){
        if (StorageTypeEnum.本地存储.val.equals(storageConfig.getStorageType())) { //本地存储
             localUtils.localView(filePath,response);
        }else{
            throw BaseException.defaultCode("暂不支持此方式预览");
        }
    }
}
