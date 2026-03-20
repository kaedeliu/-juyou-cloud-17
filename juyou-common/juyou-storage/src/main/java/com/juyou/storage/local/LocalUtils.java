package com.juyou.storage.local;

import com.juyou.common.exception.BaseException;
import com.juyou.storage.config.LocalConfig;
import com.juyou.storage.utils.StorageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 本地存储工具类
 */
@Component
@Slf4j
public class LocalUtils {

    @Autowired
    LocalConfig localConfig;


    /**
     * 文件上传
     * @param mf
     * @param filePath 上传文件路径，不带盘符,盘符从配置取
     * @return
     */
    public String upLocal(MultipartFile mf,String filePath){
        if(!StringUtils.hasLength(localConfig.getRootPath()))
            throw new BaseException("存储路径错误");
        File file=new File(new File(localConfig.getRootPath()),filePath);
        uploadLocal(mf,file);
        return returnPath(filePath);
    }


    /**
     * 获取返回的数据
     * @param filePaht
     * @return
     */
    private String returnPath(String filePaht){
        if(0==localConfig.getReturnType()){
            return filePaht;
        }
        Map<String,String> args=new HashMap<>();
        args.put("filePath",filePaht);
        return StorageUtils.stringFormat(localConfig.getReturnPath(),args);
    }


    /**
     * 本地文件上传
     * @param mf 文件
     * @param file  输入文件
     * @return
     */
    private void uploadLocal(MultipartFile mf, File file){
        if (!file.getParentFile().exists()) {
            file.mkdirs();// 创建文件根目录
        }
        FileOutputStream fos=null;
        try {
            fos=new FileOutputStream(file);
            FileCopyUtils.copy(mf.getInputStream(), fos);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }finally {
                try {
                    if(fos!=null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

        }
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

            filePath = localConfig + File.separator + filePath;
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

    public static void main(String[] args) {
        System.out.println(new File("d:\\test\\","/2023/22.jpg").getAbsoluteFile());
    }
}
