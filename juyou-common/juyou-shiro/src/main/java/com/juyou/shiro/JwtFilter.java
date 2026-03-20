//package com.juyou.shiro;
//
//import com.juyou.common.constant.CommonConstant;
//import com.juyou.common.error.ErrorCode;
//import com.juyou.common.error.ErrorMsgUtil;
//import com.juyou.common.utils.SpringContextUtils;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.apache.shiro.authc.AuthenticationToken;
//import org.apache.shiro.authz.AuthorizationException;
//import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//
//
///**
// * @Description: 鉴权登录拦截器
// * @Author: lj
// * @Date: 2018/10/7
// **/
//public class JwtFilter extends AuthenticatingFilter {
//
//    private boolean allowOrigin = true;
//
//    static final Log log=LogFactory.getLog(JwtFilter.class);
//
//    public JwtFilter(){
//
//    }
//
//    @Autowired
//    ErrorMsgUtil errorMsgUtil;
//
//    public JwtFilter(boolean allowOrigin){
//        this.allowOrigin = allowOrigin;
//    }
//
//    /**
//     * 执行登录认证
//     *
//     * @param request
//     * @param response
//     * @param mappedValue
//     * @return
//     */
//    @Override
//    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
//        try {
//        	return executeLogin(request, response);
//        } catch (Exception e) {
//           throw new AuthorizationException(e.getMessage(), e);
////        	return false;
//        }
//    }
//
//    /**
//     *
//     */
////    @Override
////    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
////        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
////        String token = httpServletRequest.getHeader(CommonConstant.X_ACCESS_TOKEN);
////        JwtToken jwtToken = new JwtToken(token);
////    	 // 提交给realm进行登入，如果错误他会抛出异常并被捕获
////        getSubject(request, response).login(jwtToken);
////        return true;
////    }
//
//    /**
//     * 对跨域提供支持
//     */
//    @Override
//    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//        if(allowOrigin){
//            httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
//            httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
//            httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
//            //update-begin-author:scott date:20200907 for:issues/I1TAAP 前后端分离，shiro过滤器配置引起的跨域问题
//            // 是否允许发送Cookie，默认Cookie不包括在CORS请求之中。设为true时，表示服务器允许Cookie包含在请求中。
//            httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
//            //update-end-author:scott date:20200907 for:issues/I1TAAP 前后端分离，shiro过滤器配置引起的跨域问题
//        }
//        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
//        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
//            httpServletResponse.setStatus(HttpStatus.OK.value());
//            return false;
//        }
//        return super.preHandle(request, response);
//    }
//	@Override
//	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
//		// TODO Auto-generated method stub
//		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//	    String token = httpServletRequest.getHeader(CommonConstant.X_ACCESS_TOKEN);
//	    JwtToken jwtToken = new JwtToken(token);
//		return jwtToken;
//	}
//
//	@Override
//	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
//		// TODO Auto-generated method stub
//		((HttpServletResponse)response).setStatus(500);
//		if(errorMsgUtil==null)
//			errorMsgUtil= SpringContextUtils.getBean(ErrorMsgUtil.class);
//		response.setCharacterEncoding("UTF-8");
//        		response.setContentType("text/html;charset=UTF-8");
//        String code=(String) request.getAttribute(ErrorMsgUtil.CODE);
//        String msg=(String) request.getAttribute(ErrorMsgUtil.MSG);
//        String result=null;
//        if(StringUtils.hasLength(code)) {
//            result=errorMsgUtil.result(code,msg).toJsonString();
//        }else {
//            result=errorMsgUtil.result(ErrorCode.A9102).toJsonString();
//        }
//        response.getOutputStream().write(result.getBytes("UTF-8"));
//        log.error("url:"+((HttpServletRequest) request).getRequestURL()+"  " +result);
//        return false;
//	}
//
//
//}
