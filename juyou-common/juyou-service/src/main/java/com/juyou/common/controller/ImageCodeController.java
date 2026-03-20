package com.juyou.common.controller;

import com.juyou.common.constant.CommonConstant;
import com.juyou.common.kaptcha.CodeDto;
import com.juyou.common.kaptcha.KaptchaUtils;
import com.juyou.common.service.LoginUtilsService;
import com.juyou.common.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/common")
@CrossOrigin
@Tag(name = "公共接口")
public class ImageCodeController {

    @Autowired
    KaptchaUtils kaptchaUtils;
    @Autowired
    LoginUtilsService loginUtilsService;

    @PostMapping(value = "/getImageCode")
    @Operation(summary = "公共接口-图片验证码-需登录", tags = { "公共接口" })
    public Result<CodeDto> getImageCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String key= CommonConstant.SYS_CACHE_IMGCODE+"::" +loginUtilsService.getLoginUserInfo().getUserId();
        return new Result<>(kaptchaUtils.createCode(key));
    }
}
