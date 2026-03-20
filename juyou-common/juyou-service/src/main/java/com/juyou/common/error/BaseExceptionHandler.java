package com.juyou.common.error;


import com.alibaba.fastjson.JSONObject;
import com.juyou.common.constant.CommonConstant;
import com.juyou.common.env.EnvKey;
import com.juyou.common.env.EnvUtils;
import com.juyou.common.exception.BaseException;
import com.juyou.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.connection.PoolException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.naming.AuthenticationException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLSyntaxErrorException;
import java.util.List;
import java.util.Set;

/**
 * 异常处理器
 * 
 * @Author kaedeliu
 * @Date 2019
 */
@RestControllerAdvice
@Slf4j
public class BaseExceptionHandler {

	
	@Autowired
	ErrorMsgUtil errorMsgUtil;

	@Autowired
	EnvUtils envUtils;
	
	/**
	 * 未知异常
	 */
//	private static final String NOERORR="emsg.common.A9900";
//	
//	private static final String NOERORR_CODE="A9900";
	
	
	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(BaseException.class)
//	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Result<Object> handleRRException(BaseException e){
//		log.error(e.getMessage(),e);
		String key=e.getCode();
		String message=e.getMessage();

		Result<Object> result = errorMsgUtil.result(key, message);
		log(e,result);
		return result;
	}
	
	/**
	 * 空指针
	 */
	@ExceptionHandler(NullPointerException.class)
//	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Result<Object> handleNullPointerException(NullPointerException e){
//		log.error(e.getMessage(),e);
		Result<Object> result=  errorMsgUtil.result(ErrorCode.A9205,"未知数据未找到");
		log(e,result);
		return result;
//		return errorMsgUtil.result(key, message);
	}
	
	

	//begin 错误异常处理
	/**
	 * 处理Violation验证错误 @NotBlank @NotNull @NotNull
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = ConstraintViolationException.class)
	public Result<Object> handlerConstraintViolationException(ConstraintViolationException e){
//		log.error(e.getMessage());
		Result<Object> result =null;
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
    	String errMsg=null;
    	String code=null;
        if (!violations.isEmpty()) {
            // 设置验证结果状态码
            for (ConstraintViolation<?> item : violations) {
            	String message=item.getMessage();
            	if(!StringUtils.isEmpty(message)) {
            		if(message.startsWith("{")) {
            			message=message.replace("{", "").replace("}", "");
            			code=message.substring(message.lastIndexOf(".")+1,message.length());
            			if(code.indexOf(":")>0) {
            				errMsg=code.split(":")[1];
            				code=code.split(":")[0];
            			}
						result = errorMsgUtil.result(code, errMsg);
            		}else {
            			result =  errorMsgUtil.result(ErrorCode.A9200,errMsg);
            		}
            	}
            	//只取一个
            	break;
            }
        }else {
        	result= errorMsgUtil.result(ErrorCode.A9200,"请求参数异常");
    	}
//		Result<Object> result = errorMsgUtil.result(code, errMsg);
		log(e,result);
		return result;
	 }
	
	/**
	 * 处理在验证 @Validated @Valid仅对于表单提交有效，对于以json格式提交将会失效
	 * @param e
	 * @return
	 */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Object> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
//		log.error(e.getMessage());
    	List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
    	String code=null;
    	String errMsg=null;
		Result<Object> result=null;
    	if(!fieldErrors.isEmpty()) {
    		FieldError item=fieldErrors.get(0);
    		String message=item.getDefaultMessage();
        	if(!StringUtils.isEmpty(message)) {
        		if(message.startsWith("{")) {
        			message=message.replace("{", "").replace("}", "");
					code=message.substring(message.lastIndexOf(".")+1,message.length());
					if(code.indexOf(":")>0) {
						errMsg=code.split(":")[1];
						code=code.split(":")[0];
					}
					result =  errorMsgUtil.result(code,errMsg);
        		}else{
					result =  errorMsgUtil.result(ErrorCode.A9200,item.getField()+":"+message);
				}

        	}else {
				result =  errorMsgUtil.result(ErrorCode.A9200,"JSON格式异常");
        	}
    	}else{
			result = errorMsgUtil.result(code, errMsg);
		}

		log(e,result);
		return result;
    }
    

    /**
     * 处理验证 @Validated @Valid仅对于表单提交有效，对于以json格式提交将会失效
     * @param e
     * @return
     */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public Result<Object> handlerBindException(BindException e) {
//		log.error(e.getMessage());
    	BindingResult bindingResult = e.getBindingResult();
    	FieldError objectError = bindingResult.getFieldError();
        String code=null;
    	String errMsg=null;
        String message = objectError.getDefaultMessage();
		Result<Object> result=null;
		if(!StringUtils.isEmpty(message)) {
        	if(message.startsWith("{")) {
    			message=message.replace("{", "").replace("}", "");
    			code=message.substring(message.lastIndexOf(".")+1,message.length());
    			if(code.indexOf(":")>0) {
    				errMsg=code.split(":")[1];
    				code=code.split(":")[0];
    			}
				result = Result.error(code,errMsg);
    		}else {
    			result=errorMsgUtil.result(ErrorCode.A9200, objectError.getField() +" " + message);
    		}
    		
    	}
		log(e,result);
		return result;
    }
    
    //end 错误异常处理
    
    
 
	//end 错误异常处理结束
	
//    @ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NoHandlerFoundException.class)
	public Result<Object> handlerNoFoundException(Exception e) {
//		log.error(e.getMessage(), e);
		Result<Object> result =  errorMsgUtil.result(ErrorCode.B0202,"请求路径错误");
		log(e,result);
		return result;
//		return Result.error(ErrorCode.B0202, "路径不存在，请检查路径是否正确");
	}

//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(DuplicateKeyException.class)
	public Result<Object> handleDuplicateKeyException(DuplicateKeyException e){
//		log.error(e.getMessage(), e);
		String msg=null;
		if(e.getCause()!=null && e.getCause().getMessage()!=null) {
			msg=e.getCause().getMessage();
			if(msg.split("'").length==4) {
				msg=msg.split("'")[3]+msg.split("'")[1];
			}
			msg="数据重复:"+msg;
		}

		Result<Object> result =   errorMsgUtil.result(ErrorCode.B0301,msg);
//		return Result.error("数据库中已存在该记录");
		log(e,result);
		return result;
	}

//	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(SQLSyntaxErrorException.class)
	public Result<Object> handleSQLSyntaxErrorException(SQLSyntaxErrorException e){
//		log.error(e.getMessage(), e);
		Result<Object> result =  errorMsgUtil.result(ErrorCode.B0302,"数据查询错误:"+e.getMessage());
		log(e,result);
		return result;

	}


//	@ExceptionHandler({UnauthorizedException.class, AuthorizationException.class})
////	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
//	public Result<Object> handleAuthorizationException(AuthorizationException e){
////		log.error(e.getMessage(), e);
//		String message=e.getMessage();
//		Result<Object> result = null;
//		if(StringUtils.hasLength(message) && message.startsWith(ErrorMsgUtil.CODE_START_BUF)) {
//			result =  errorMsgUtil.result(message);
//		}else if(StringUtils.hasLength(message) && message.indexOf(CommonConstant.SYSTEM_ROLE_ADMIN)>=0) {
//			result =  errorMsgUtil.result(CommonConstant.COMMON_ERROR_CODE,"敏感数据，修改可能造成系统异常，该模块需要超级管理员权限才能设置");
//		} else{
//			result =errorMsgUtil.result(CommonConstant.COMMON_ERROR_CODE,"您没有权限，请联系管理员");
//		}
//		log(e,result);
//		return result;
////		return Result.error("没有权限，请联系管理员授权");
//	}
	@ExceptionHandler({AuthenticationException.class})
//	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	public Result<Object> handleAuthenticationException(AuthenticationException e){
//		log.error(e.getMessage(), e);
		Result<Object> result = errorMsgUtil.result(ErrorCode.A9102,"您没有权限或者登录超时");
//		return Result.error("您没有权限或者登录超时");
		log(e,result);
		return result;
	}

//	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public Result<Object> handleException(Exception e){
//		log.error(e.getMessage(), e);
		Result<Object> result =  errorMsgUtil.result(CommonConstant.COMMON_ERROR_CODE,"系统错误");
		log(e,result);
		return result;
//		return Result.error("操作失败，"+e.getMessage());
	}
	
	/**
	 * @Author kaedeliu
	 * @param e
	 * @return
	 */
//	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public Result<Object> HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
		StringBuffer sb = new StringBuffer();
		sb.append("不支持");
		sb.append(e.getMethod());
		sb.append("请求方法，");
		sb.append("支持以下");
		String [] methods = e.getSupportedMethods();
		if(methods!=null){
			for(String str:methods){
				sb.append(str);
				sb.append("、");
			}
		}
//		log.error(sb.toString(), e);
		//return Result.error("没有权限，请联系管理员授权");
		Result<Object> result = Result.error(ErrorCode.B0104,sb.toString());
		log(e,result);
		return result;
	}
	
	 /** 
	  * spring默认上传大小100MB 超出大小捕获异常MaxUploadSizeExceededException 
	  */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
//    	log.error(e.getMessage(), e);
		Result<Object> result =  errorMsgUtil.result(ErrorCode.B0203,"文件大小超过限制");
		log(e,result);
		return result;
//        return Result.error("文件大小超出10MB限制, 请压缩或降低文件质量! ");
    }

	@ExceptionHandler(BindingException.class)
	public Result<Object> handleBindingException(BindingException e) {
//    	log.error(e.getMessage(), e);
		Result<Object> result =  errorMsgUtil.result(ErrorCode.B0302,"系统错误，查询异常");
		log(e,result);
		return result;
//        return Result.error("字段太长,超出数据库字段的长度");
	}


    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result<Object> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
//    	log.error(e.getMessage(), e);
		Result<Object> result =  errorMsgUtil.result(ErrorCode.B0302,"数据类型异常");
		log(e,result);
		return result;
//        return Result.error("字段太长,超出数据库字段的长度");
    }

    @ExceptionHandler(PoolException.class)
    public Result<Object> handlePoolException(PoolException e) {
//    	log.error(e.getMessage(), e);
		Result<Object> result =  errorMsgUtil.result(ErrorCode.B0401,"系统错误，缓存异常");
		log(e,result);
		return result;
//        return Result.error("Redis 连接异常!");
    }

    /**
     * 数据转换异常
     * @param e
     * @return
     */
    @ExceptionHandler(ConversionFailedException.class)
    public Result<Object> handleConversionFailedException(ConversionFailedException e) {
//    	log.error(e.getMessage(), e);
		Result<Object> result =errorMsgUtil.result(ErrorCode.B0102,"数据类型错误");
//        return Result.error("Redis 连接异常!");
		log(e,result);
		return result;
    }
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
    	//log.error(e.getMessage());
		Result<Object> result=  errorMsgUtil.result(ErrorCode.B0106,"请求错误");
		log(e,result);
		return result;
    }

	private void log(Exception e,Result<Object> error){
		String msg=e.getMessage();
		if(error!=null){
			msg+="   " + JSONObject.toJSONString(error);
		}
		Integer type=envUtils.value(EnvKey.日志_异常日志类型,Integer.class);
		if(type==1)
			log.error(msg);
		else if(type==2)
			log.error(msg,e);

	}
}
