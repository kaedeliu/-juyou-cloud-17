package com.juyou.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.juyou.common.env.EnvKey;
import com.juyou.common.env.EnvUtils;
import com.juyou.common.error.ErrorCode;
import com.juyou.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.Map.Entry;

/**
 * HTTP请求工具类
 *
 * @author Administrator
 */
@Slf4j
public class HttpUtils {

    final static String CONTENT_TYPE_TEXT_JSON = "application/json";

    static EnvUtils envUtils=null;

    //	/**
//	 * get请求
//	 * @param url url地址
//	 * @param params 参数
//	 * @param header header
//	 * @return
//	 * @throws Exception
//	 */
//	public static String get(String url,Map<String,String> params,Map<String,String> header) throws Exception {
//		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//		StringBuffer sendUrl=new StringBuffer();
//		sendUrl.append(url);
//		if(params!=null && !params.isEmpty()) {
//			if(sendUrl.indexOf("?")!=-1) {
//				sendUrl.append("?");
//			}
//			Iterator<Entry<String, String>> item=params.entrySet().iterator();
//			while (item.hasNext()) {
//				Entry<String, String> entry=item.next();
//				String value=entry.getValue();
//				String key=entry.getKey();
//				if(!StringUtils.hasLength(value))
//					value="";
//				sendUrl.append(key+"="+value).append("&");
//			}
//			sendUrl.deleteCharAt(sendUrl.length());
//		}
//
//		HttpGet httpGet=new HttpGet(sendUrl.toString());
//		//处理header
//		if(header!=null && !header.isEmpty()) {
//			Iterator<Entry<String, String>> item=header.entrySet().iterator();
//			while (item.hasNext()) {
//				Entry<String, String> entry=item.next();
//				httpGet.addHeader(entry.getKey(), entry.getValue());
//			}
//		}
//		 CloseableHttpResponse response = null;
//		 try {
//			 response = httpClient.execute(httpGet);
//			 String result=null;
//	         if(response != null && response.getStatusLine().getStatusCode() == 200)
//	         {
//	             HttpEntity entity = response.getEntity();
//	             result = EntityUtils.toString(entity);
//	         }
//	         return result;
//		} catch (Exception e) {
//			throw e;
//		}
//
//	}
//	/**
//	 * get请求
//	 * @param url url地址
//	 * @param params 参数
//	 * @param header header
//	 * @return
//	 * @throws Exception
//	 */
//	public static CloseableHttpResponse getRep(String url,Map<String,String> params,Map<String,String> header) throws Exception {
//		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//		StringBuffer sendUrl=new StringBuffer();
//		sendUrl.append(url);
//		if(params!=null && !params.isEmpty()) {
//			if(sendUrl.indexOf("?")!=-1) {
//				sendUrl.append("?");
//			}
//			Iterator<Entry<String, String>> item=params.entrySet().iterator();
//			while (item.hasNext()) {
//				Entry<String, String> entry=item.next();
//				String value=entry.getValue();
//				String key=entry.getKey();
//				if(!StringUtils.hasLength(value))
//					value="";
//				sendUrl.append(key+"="+value).append("&");
//			}
//			sendUrl.deleteCharAt(sendUrl.length());
//		}
//
//		HttpGet httpGet=new HttpGet(sendUrl.toString());
//		//处理header
//		if(header!=null && !header.isEmpty()) {
//			Iterator<Entry<String, String>> item=header.entrySet().iterator();
//			while (item.hasNext()) {
//				Entry<String, String> entry=item.next();
//				httpGet.addHeader(entry.getKey(), entry.getValue());
//			}
//		}
//		 CloseableHttpResponse response = null;
//		 try {
//			 response = httpClient.execute(httpGet);
//			 String result=null;
//	         if(response != null && response.getStatusLine().getStatusCode() == 200)
//	         {
//	             HttpEntity entity = response.getEntity();
//	             result = EntityUtils.toString(entity);
//	         }
//		} catch (Exception e) {
//			throw e;
//		}
//		return response;
//	}
//
//	/**
//	 * post请求
//	 * @param url url地址
//	 * @param params 参数
//	 * @param header header
//	 * @return
//	 * @throws Exception
//	 */
//	public static String post(String url,Map<String,String> params,Map<String,String> header) throws Exception {
//
//		CloseableHttpResponse response = postRep(url, params, header);
//		String result=null;
//        if(response != null && response.getStatusLine().getStatusCode() == 200)
//        {
//            HttpEntity entity = response.getEntity();
//            result = EntityUtils.toString(entity);
//        }
//        return result;
//	}

	/**
	 * post请求
	 * @param url url地址
	 * @param params 参数String or Map<String,Object>
	 * @param header header
	 * @return
	 * @throws Exception
	 */
	public static HttpRep doPostJson(String url, Object params, Map<String,String> header, boolean zf) throws Exception {
        String res = null;
		CloseableHttpClient httpClient = noVfSslHttpClietn();
        if (!zf) {
            Object[] obj = addCommonHeader(header, url);
            url = (String) obj[0];
            header = (Map<String, String>) obj[1];
        }
        HttpPost httpPost=new HttpPost(url);
        if(params!=null){
            if(params.getClass().equals(String.class)){ //json
                StringEntity entity = new StringEntity(params.toString(),"UTF-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType(CONTENT_TYPE_TEXT_JSON);
                httpPost.setEntity(entity);
            }else{
                Map<String, Object> paramMap= (Map<String, Object>) params;
                Iterator<Entry<String, Object>> iterator = paramMap.entrySet().iterator();
                List<BasicNameValuePair> content = new ArrayList<>();
                while (iterator.hasNext()) {
                    Entry<String, Object> entry=iterator.next();
                    String value="";
                    if(entry.getValue()!=null)
                        value=entry.getValue()+value;
                    content.add(new BasicNameValuePair(entry.getKey(),value));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(content,"UTF-8");
                httpPost.setEntity(entity);
            }
        }
        if(header==null || !header.containsKey("Content-Type")) {
            if(header==null) header=new HashMap<>();
            header.put("Content-Type", "application/json;charset=UTF-8");
        }
		//处理header
		if(header!=null && !header.isEmpty()) {
			Iterator<Entry<String, String>> item=header.entrySet().iterator();
			while (item.hasNext()) {
				Entry<String, String> entry=item.next();
				httpPost.addHeader(entry.getKey(), entry.getValue());
			}
		}


		CloseableHttpResponse response = null;

        Exception exception=null;
        HttpRep httpRep=new HttpRep();
        int code=-1;
        try {
            response = httpClient.execute(httpPost);
            code=response.getStatusLine().getStatusCode();
            if (code == 307) {
                Header h = response.getFirstHeader("location"); // 跳转的目标地址是在 HTTP-HEAD上
                String newuri = h.getValue(); // 这就是跳转后的地址，再向这个地址发出新申请
                return doPostJson(newuri, params, header, true);
            }
            res=getRepBody(response);
            httpRep.setCode(code);
            httpRep.setRes(res);
        } catch (IOException e) {
            res=getRepBody(response);
            httpRep.setCode(code);
            exception=e;
            throw e;
        } finally {
            printLog(url,params,header,code,exception,res);
            httpPost.releaseConnection();
        }
        return httpRep;
	}

    //绕过证书
    public static CloseableHttpClient noVfSslHttpClietn() {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] arg0,
                                               String arg1) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] arg0,
                                               String arg1) throws CertificateException {
                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(
                    ctx, NoopHostnameVerifier.INSTANCE);
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(ssf).build();
            return httpclient;
        } catch (Exception e) {
            return HttpClients.createDefault();
        }
    }


    private static String getRepBody(CloseableHttpResponse response){
        String res=null;
        try {
            HttpEntity entity = response.getEntity();
            res = EntityUtils.toString(entity,"UTF-8");
        }catch (Exception e){}
        return res;
    }


    public static String doPost(String url, Map<String, Object> paramMap, Map<String, String> header) throws Exception {
        return doPostJson(url, paramMap, header, false).getRes();
    }


    /**
     * post请求
     * @param url url地址
     * @param header header
     * @return
     * @throws Exception
     */
    public static HttpRep doGet(String url, Map<String,String> header, boolean zf) throws Exception {
        String res = null;
        CloseableHttpClient httpClient = noVfSslHttpClietn();
        if (!zf) {
            Object[] obj = addCommonHeader(header, url);
            url = (String) obj[0];
            header = (Map<String, String>) obj[1];
        }
        HttpGet httpGet=new HttpGet(url);

        //处理header
        if(header!=null && !header.isEmpty()) {
            Iterator<Entry<String, String>> item=header.entrySet().iterator();
            while (item.hasNext()) {
                Entry<String, String> entry=item.next();
                httpGet.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if(header==null || !header.containsKey("Content-Type")) {
            httpGet.addHeader("Content-Type", "application/json;charset=UTF-8");
        }

        CloseableHttpResponse response = null;

        Exception exception=null;
        HttpRep httpRep=new HttpRep();
        int code=-1;
        try {
            response = httpClient.execute(httpGet);
            code=response.getStatusLine().getStatusCode();
            if (code == 307) {
                Header h = response.getFirstHeader("location"); // 跳转的目标地址是在 HTTP-HEAD上
                String newuri = h.getValue(); // 这就是跳转后的地址，再向这个地址发出新申请
                return doGet(newuri, header, true);
            }
            res=getRepBody(response);
            httpRep.setCode(code);
            httpRep.setRes(res);
        } catch (IOException e) {
            res=getRepBody(response);
            httpRep.setCode(code);
            exception=e;
            e.printStackTrace();
            throw e;
        } finally {
            printLog(url,null,header,code,exception,res);
            httpGet.releaseConnection();
        }
        return httpRep;
    }

//    /**
//     * get请求,一定需要自己关闭 GetMethod
//     *
//     * @param url 请求地址
//     * @param header 信息头
//     * @param zf 是否307转发
//     * @return
//     */
//    @SuppressWarnings("unchecked")
//    public static HttpRep doGet(String url, Map<String, String> header, boolean zf) throws Exception {
////	    String res = null;
//        HttpClient client = new HttpClient();
//        if (!zf) {
//            Object[] obj = addCommonHeader(header, url, false);
//            url = (String) obj[0];
//            header = (Map<String, String>) obj[1];
//        }
//        GetMethod getMethod = new GetMethod(url);
//        getMethod.getParams().setContentCharset("UTF-8");
//
//        if (header != null && !header.isEmpty()) {
//            Iterator<Entry<String, String>> item = header.entrySet().iterator();
//            while (item.hasNext()) {
//                Entry<String, String> entry = item.next();
//                getMethod.setRequestHeader(entry.getKey(), entry.getValue());
//            }
//        }
//        Exception exception=null;
//        HttpRep httpRep=new HttpRep();
//        int code=-1;
//        try {
//             code = client.executeMethod(getMethod);
//            if (code == 307) {
//                org.apache.commons.httpclient.Header h = getMethod.getResponseHeader("location"); // 跳转的目标地址是在 HTTP-HEAD上
//                String newuri = h.getValue(); // 这就是跳转后的地址，再向这个地址发出新申请
//                httpRep = doGet(newuri, header, true);
//            }else{
//                httpRep.setRes(getMethod.getResponseBodyAsString());
//            }
//        } catch (IOException e) {
//            httpRep.setCode(code);
//            exception=e;
//            throw e;
//        } finally {
//            printLog(url,null,header,code,exception,httpRep.getRes());
//            getMethod.releaseConnection();
//        }
//        return httpRep;
//    }


    public static String doPostJson(String url, String json, Map<String, String> header) throws Exception {
        return doPostJson(url, json, header, false).getRes();
    }

//    /**
//     * @功能 发送HTTPPOST请求
//     * @Author kaedeliu
//     * @创建时间 2026/3/18 10:49
//     * @修改人 kaedeliu
//     * @修改时间 2026/3/18 10:49
//     * @Param
//     * @param url: 请求地址
//     * @param params:  请求参数,可以是json,也可以是Map<String,Object>
//     * @param header: 请求头
//     * @param zf: 307转发标志,主动调用请传fasle
//     * @return
//    **/
//    @SuppressWarnings("unchecked")
//    private static HttpRep doPostJson(String url, Object params, Map<String, String> header, boolean zf) throws Exception {
//        String res = null;
//        HttpClient client = new HttpClient();
//        if (!zf) {
//            Object[] obj = addCommonHeader(header, url);
//            url = (String) obj[0];
//            header = (Map<String, String>) obj[1];
//        }
//        PostMethod postMethod = new PostMethod(url);
//        postMethod.getParams().setContentCharset("UTF-8");
//        if(params.getClass().equals(String.class)) {
//            RequestEntity se = new StringRequestEntity(params.toString(), "application/json", "UTF-8");
//            postMethod.setRequestEntity(se);
//        }else if(params!=null && params.getClass().equals(Map.class)){
//            Map<String, Object> paramMap= (Map<String, Object>) params;
//            Iterator<Map.Entry<String, Object>> iterator = paramMap.entrySet().iterator();
//            while (iterator.hasNext()) {
//                Map.Entry<String, Object> next = iterator.next();
//                postMethod.addParameter(next.getKey(), next.getValue().toString());
//            }
//        }
//        if (header != null && !header.isEmpty()) {
//            Iterator<Entry<String, String>> item = header.entrySet().iterator();
//            while (item.hasNext()) {
//                Entry<String, String> entry = item.next();
//                postMethod.setRequestHeader(entry.getKey(), entry.getValue());
//            }
//        } else {
//            postMethod.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
//        }
//        Exception exception=null;
//        HttpRep httpRep=new HttpRep();
//        int code=-1;
//        try {
//            code = client.executeMethod(postMethod);
//            if (code == 307) {
//                org.apache.commons.httpclient.Header h = postMethod.getResponseHeader("location"); // 跳转的目标地址是在 HTTP-HEAD上
//                String newuri = h.getValue(); // 这就是跳转后的地址，再向这个地址发出新申请
//                return doPostJson(newuri, params, header, true);
//            }
//            res = postMethod.getResponseBodyAsString();
//            httpRep.setRes(res);
//        } catch (IOException e) {
//            httpRep.setRes(postMethod.getResponseBodyAsString());
//            httpRep.setCode(code);
//            exception=e;
//            throw e;
//        } finally {
//            printLog(url,params,header,code,exception,res);
//            postMethod.releaseConnection();
//        }
//        return httpRep;
//    }

    public static Object[] addCommonHeader(Map<String, String> header, String url) {
        return addCommonHeader(header, url, true);
    }

    public static Object[] addCommonHeader(Map<String, String> header, String url, boolean json) {

        String reqUrl=url;
        Boolean frontComputer = envUtils.value(EnvKey.是否使用前置机转发, Boolean.class);

        if (frontComputer) {
            if (header == null) {
                header = new HashMap<String, String>();
                if (json)
                    header.put("Content-Type", "application/json;charset=UTF-8");
            }
            String frontComputerForward = envUtils.value(EnvKey.前置机转发地址);
            if (StringUtils.isEmpty(frontComputerForward)) {
                throw new BaseException(ErrorCode.B0100, "前置机地址配置错误");
            }
            header.put("frontTarget", reqUrl);
            reqUrl = frontComputerForward;
        }

        return new Object[]{reqUrl, header};
    }

    public static String resolve(String baseUri, String url) {
        if (!baseUri.endsWith("/"))
            baseUri = baseUri + "/";
        if (url.startsWith("/"))
            url = url.substring(1);
        URI base = URI.create(baseUri);
        URI uri = URIUtils.resolve(base, url);
        return uri.toString();
    }

   /**
    * @功能 输出请求日志
    * @Author kaedeliu
    * @创建时间 2026/3/18 10:49
    * @修改人 kaedeliu
    * @修改时间 2026/3/18 10:49
    * @Param
    * @param url: 请求地址
    * @param params: 请求数据
    * @param header: 请求头信息
    * @param code: 返回状态
    * @param e: 异常
    * @param res: 返回数据
    * @return
   **/
    public static void printLog(String url, Object params, Map<String, String> header, int code, Exception e, String res){
        if(envUtils==null){
            envUtils=SpringContextUtils.getBean(EnvUtils.class);
        }
        Integer type=envUtils.value(EnvKey.日志_请求日志类型,Integer.class);
        String json=null;
        if(params!=null &&params.getClass().equals(String.class)){
            json=(String) params;
        }else if(params!=null){
            json=JSONObject.toJSONString(params);
        }
        String req=json;
        String rep=res;
        if(type==1){
            if (json!=null  && json.length() > 500) {
                req=json.substring(0,200)+"..."+json.substring(json.length()-200);
            }
            if (res!=null  && res.length() > 500) {
                rep=res.substring(0,200)+"..."+res.substring(res.length()-200);
            }
        }else if(type==0){
            req=null;
            rep=null;
        }
        String uuid=UUID.randomUUID().toString();
        if(type!=0)
            log.info("\nrequestId:"+uuid+"\nurl:" + url + "\nreq:\n" + req + "\nheader:\n" + JSONObject.toJSONString(header)  +"\ncode:"+code+"\nrep:"+rep);
//        if(e!=null)
//            log.error("uuid:"+uuid,e);
    }

}
