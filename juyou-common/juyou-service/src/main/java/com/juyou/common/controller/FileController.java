//package com.juyou.common.controller;
//
//import java.io.IOException;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.alibaba.fastjson.JSONObject;
//import com.juyou.common.utils.ResourceUtils;
//import com.juyou.common.utils.Result;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//
//@RestController
//@RequestMapping("/cc/user/")
//@CrossOrigin
//@Tag(name = "公共接口")
//public class FileController {
//
//	@Autowired
//	private ResourceUtils resourceUtils;
//
//	@PostMapping(value = "/upload")
//	@Operation(summary = "公共接口-文件上传", tags = { "公共接口" })
//	public Result<JSONObject> upload(HttpServletRequest request, HttpServletResponse response) {
//		Result<JSONObject> result = new Result<JSONObject>();
//		try {
//			result = this.resourceUtils.upload(request, response);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
//
//	@GetMapping(value = "/view/**")
//	@Operation(summary = "公共接口-文件预览", tags = { "公共接口" })
//	public void view(HttpServletRequest request, HttpServletResponse response) {
//		try {
//			this.resourceUtils.view(request, response);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//}
