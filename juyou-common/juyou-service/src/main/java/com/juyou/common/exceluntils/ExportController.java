package com.juyou.common.exceluntils;

import com.juyou.common.constant.CommonConstant;
import com.juyou.common.env.EnvKey;
import com.juyou.common.env.EnvUtils;
import com.juyou.common.error.ErrorCode;
import com.juyou.common.exception.BaseException;
import com.juyou.common.log.AutoLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RestController
@RequestMapping("/exoprt")
@Slf4j
@Tag(name = "公共接口")
public class ExportController {

    @Autowired
    EnvUtils envUtils;
    @Operation(summary = "导出-导出下载")
    @RequestMapping(value = "/down", method = RequestMethod.GET)
    @AutoLog(operateType = CommonConstant.OPERATE_查询_1,value = "下载或者打开文件" )
    public void down(HttpServletRequest request, HttpServletResponse response){
        String fileName=request.getParameter("fileName");
        String open=request.getParameter("open");
        if(StringUtils.isEmpty(fileName))
            throw  new BaseException(ErrorCode.A9201,"文件名不为空");
        String filePath=envUtils.value(EnvKey.导出文件存放地址);
        File file=new File(filePath,fileName);
        if(!file.isFile())
            throw  new BaseException(ErrorCode.A9201,"文件不存在");
        if(StringUtils.isNotEmpty(open) && open.equals("1")){ //尝试打开
            try {
                response.addHeader("Content-Disposition", "inline;fileName=" + new String(fileName.getBytes("UTF-8"),"iso-8859-1"));
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        }else{
            response.setContentType("application/force-download");// 设置强制下载不打开
            try {
                response.addHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("UTF-8"),"iso-8859-1"));
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        }
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(file);
            outputStream = response.getOutputStream();

            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            response.flushBuffer();
        } catch (IOException e) {
            response.setStatus(404);
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
//					log.error(e.getMessage(), e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
//					log.error(e.getMessage(), e);
                }
            }
        }
    }
}
