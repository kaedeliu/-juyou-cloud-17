package com.juyou.storage.controller;

import com.juyou.common.constant.CommonConstant;
import com.juyou.common.log.AutoLog;
import com.juyou.common.utils.Result;
import com.juyou.common.utils.SpringContextUtils;
import com.juyou.storage.dto.ReturnFileDto;
import com.juyou.storage.utils.StorageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Tag(name = "文件处理")
@RequestMapping("/storage")
public class StorageController {

    @Autowired
    StorageUtils storageUtils;

    @Operation(summary = "文件处理-上传", tags = { "文件处理" },description = "上传控件名称:file")
    @AutoLog(operateType = CommonConstant.OPERATE_更新_3, value = "文件处理-上传", db = false)
    @PostMapping("/upload")
    public Result<ReturnFileDto> upload() throws IOException {
        String filePath=storageUtils.upload(SpringContextUtils.getHttpServletRequest());
        return new Result<>(new ReturnFileDto().setKey(filePath));
    }

    @Operation(summary = "文件处理-本地文件展示", tags = { "文件处理" },description = "上传文件返回的key")
    @AutoLog(operateType = CommonConstant.OPERATE_更新_3, value = "文件处理-上传", db = false)
    @GetMapping("/view")
    public void view(@Schema(description = "filePath",required = true) String filePath, HttpServletResponse response){
        storageUtils.localView(filePath,response);
    }



}
