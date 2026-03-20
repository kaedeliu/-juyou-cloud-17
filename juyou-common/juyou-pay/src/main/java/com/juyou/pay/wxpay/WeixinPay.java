package com.juyou.pay.wxpay;

import com.alibaba.fastjson.JSONObject;
import com.juyou.pay.config.WxConfig;
import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.github.binarywang.wxpay.util.SignUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @功能 完成微信支付功能
 * @Author kaedeliu
 * @创建时间 2026/3/18 10:49
 * @修改人 kaedeliu
 * @修改时间 2026/3/18 10:49
 * @Param
 * @return
**/
@Component
public class WeixinPay {

    @Autowired
    WxConfig wxConfig;

    /**
     * 配置支付需要的连接
     * @return
     */
    private  WxPayService getWxPayService() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(wxConfig.getAppId());
        payConfig.setMchId(wxConfig.getMchId());
        payConfig.setMchKey(wxConfig.getMchKey());
        payConfig.setKeyPath(wxConfig.getKeyPath());
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

    /**
     *
     * @param openId V3小程序、公众号版本支付需要，V2版本可不传
     * @param orderNum 订单号
     * @param money 支付金额，单位分
     * @param notifyUrl 回调地址，为空通过配置取
     * @param body 支付显示body
     * @param h5Type:iOS, Android, Wap,H5支付必填
     * @param createIp 客户端地址
     * @param attach 附加数据
     * @param tradeType 1-JSAPI,2-APP,3-H5,4-NATIVE
     * @return
     * @throws WxPayException
     */
    public  String wxPay(String openId,String orderNum,Integer money,String notifyUrl, String body,String h5Type, String createIp,String attach,Integer tradeType) throws WxPayException {
        if("V3".equals(wxConfig.getVersion())){
            return wxAppPayV3(openId,orderNum,money,notifyUrl,body,h5Type,createIp,attach,getTradeTypeEnum(tradeType));
        }
//        else if("V2".equals(wxConfig.getVersion())){
//            if(tradeType==2){
//                return wxAppPay(orderNum,money,notifyUrl,body,createIp,"APP");
//            }else if(tradeType==1){
//                return wxAppPay(orderNum,money,notifyUrl,body,createIp,"JSAPI");
//            }else if(tradeType==3){
//                return wxAppPay(orderNum,money,notifyUrl,body,createIp,"JSAPI");
//            }
//            throw new WxPayException("tradeType 异常:"+tradeType);
//        }
        else{
            throw new WxPayException("Config version配置异常:"+wxConfig.getVersion());
        }

    }

    private  TradeTypeEnum getTradeTypeEnum(Integer tradeType) throws WxPayException {
        switch (tradeType){
            case 1:
                return TradeTypeEnum.JSAPI;
            case 2:
                return TradeTypeEnum.APP;
            case 3:
                return TradeTypeEnum.H5;
            case 4:
                return TradeTypeEnum.NATIVE;
            default:
                throw new WxPayException("tradeType错误:"+tradeType);
        }
    }



    /**
     * V3版本APP支付
     * @param openId
     * @param orderNum
     * @param money
     * @param notifyUrl
     * @param body
     * @param payer_client_ip
     * @param h5Type:iOS, Android, Wap
     * @param attach:附加数据
     * @param tradeTypeEnum
     * @return
     * @throws WxPayException
     */
    private String wxAppPayV3(String openId, String orderNum, Integer money, String notifyUrl, String body,String payer_client_ip,String h5Type,String attach, TradeTypeEnum tradeTypeEnum) throws WxPayException {
        WxPayUnifiedOrderV3Request orderRequest = new WxPayUnifiedOrderV3Request();
        orderRequest.setNotifyUrl(notifyUrl == null ? wxConfig.getNotifyUrl() : notifyUrl);
        orderRequest.setMchid(wxConfig.getMchId());
        orderRequest.setOutTradeNo(orderNum);
        orderRequest.setDescription(body);
        orderRequest.setAppid(wxConfig.getAppId());
        orderRequest.setAmount(new WxPayUnifiedOrderV3Request.Amount().setTotal(money));//直接分
        orderRequest.setAttach(attach);
        if(tradeTypeEnum.equals(TradeTypeEnum.JSAPI)) {
            if(!StringUtils.hasLength(openId))
                throw new WxPayException("openid is null");
            orderRequest.setPayer(new WxPayUnifiedOrderV3Request.Payer().setOpenid(openId));
        }else if(tradeTypeEnum.equals(TradeTypeEnum.H5)){
            if(!StringUtils.hasLength(payer_client_ip)){
                throw new WxPayException("payer_client_ip is null");
            }
            if(!StringUtils.hasLength(h5Type)){
                throw new WxPayException("h5Type is null");
            }
            WxPayUnifiedOrderV3Request.SceneInfo sceneInfo=new WxPayUnifiedOrderV3Request.SceneInfo();
            sceneInfo.setPayerClientIp(payer_client_ip);
            sceneInfo.setH5Info(new WxPayUnifiedOrderV3Request.H5Info().setType(h5Type));
            orderRequest.setSceneInfo(sceneInfo);
        }
        //APP没有附加数据
        WxPayService wxPayService = getWxPayService();
        WxPayUnifiedOrderV3Result o = wxPayService.createOrderV3(tradeTypeEnum, orderRequest);
        return o.getPrepayId();
    }


    /**
     * 将结果提取为需要前端支付需要的字符串
     * @param o
     * @return
     */
    public  String wxPayXml(WxPayAppOrderResult o) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String date = df.format(new Date()).replace("-","").replace(":","").replace(" ","");
        Map<String, String> params = new HashMap<>();
        params.put("appid", wxConfig.getAppId());  //上面的appid，注意大小写
        params.put("partnerid", wxConfig.getMchId()); //商户id
        params.put("prepayid", o.getPrepayId());
        params.put("noncestr", o.getNonceStr());  //32位随机数
        params.put("package", "Sign=WXPay"); //商品描述
        params.put("timestamp",(System.currentTimeMillis()/1000)+"");
        try {
            String sign=SignUtils.createSign(params,wxConfig.getMchKey());
            params.put("sign",sign);
            return JSONObject.toJSONString(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询订单是否支付
     * @param orderId
     * @return
     * @throws WxPayException
     */
    public WxPayOrderQueryV3Result queryOrdersV3(String orderId) throws WxPayException {
        WxPayOrderQueryV3Request orderRequest = new WxPayOrderQueryV3Request();
        orderRequest.setOutTradeNo(orderId);
        WxPayService wxPayService = getWxPayService();
        WxPayOrderQueryV3Result wxPayOrderQueryResult = wxPayService.queryOrderV3(orderRequest);
        System.out.println(JSONObject.toJSONString(wxPayOrderQueryResult));
        return wxPayOrderQueryResult;
    }


    public WxPayRefundV3Result refund(String orderNum,String refundOrder,Integer totalMoney,Integer refundMoney,String refundDesc) throws WxPayException{
        if("V3".equals(wxConfig.getVersion())){
            return refundV3( orderNum,  refundOrder,  refundMoney,  totalMoney);
        }
//        else if("V2".equals(wxConfig.getVersion())){
//           return refundV2( orderNum, refundOrder, totalMoney, refundMoney, refundDesc);
//        }
        else{
            throw new WxPayException("Config version配置异常:"+wxConfig.getVersion());
        }
    }



    /**
     * @param outTradeNo 原支付交易对应的微信订单号
     * @param outRefundNo   商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
     * @param refundPrice   退款金额，单位为分，只能为整数，不能超过原订单支付金额。
     * @param totalPrice    原支付交易的订单总金额，单位为分，只能为整数
     */
    public WxPayRefundV3Result refundV3(String outTradeNo, String outRefundNo, Integer refundPrice, Integer totalPrice) throws WxPayException {
        WxPayRefundV3Request request = new WxPayRefundV3Request();
        request.setOutRefundNo(outRefundNo);
        request.setOutRefundNo(outTradeNo);
        request.setAmount(new WxPayRefundV3Request.Amount().setRefund(refundPrice).setTotal(totalPrice).setCurrency("CNY"));
        WxPayService wxPayService = getWxPayService();
        WxPayRefundV3Result res = wxPayService.refundV3(request);
        return res;

    }

    //    /**
//     * @功能 微信支付
//     * @Author kaedeliu
//     * @创建时间 2026/3/18 10:49
//     * @修改人 kaedeliu
//     * @修改时间 2026/3/18 10:49
//     * @Param
//     * @param orderNum:订单号
//     * @param money:支付金额，单位分
//     * @param notifyUrl:回调地址，为空通过配置取
//     * @param body: 支付显示body
//     * @param createIp:可用户端Ip地址，H5支付必填
//     * @param tradeType:APP,NATIVE,MWEB,JSAPI
//     * @return
//    **/
//    public  String wxAppPay(String orderNum,Integer money,String notifyUrl, String body, String createIp,String tradeType) throws WxPayException {
//        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
//        orderRequest.setNotifyUrl(notifyUrl==null?wxConfig.getNotifyUrl():notifyUrl);
//        orderRequest.setTradeType(tradeType);
//        orderRequest.setSignType("MD5");
//        orderRequest.setBody(body);
//        orderRequest.setOutTradeNo(orderNum); //自己生成order_No
//        orderRequest.setTotalFee(money);//直接分
//        orderRequest.setSpbillCreateIp(createIp);
//        WxPayService wxPayService = getWxPayService();
//        WxPayAppOrderResult o = wxPayService.createOrder(orderRequest);
//        String refString = wxPayXml(o);
//        System.out.println(refString);
//        return refString;
//    }

    //
//    /**
//     * 查询订单是否支付
//     * @param orderId
//     * @return
//     * @throws WxPayException
//     */
//    public WxPayOrderQueryResult queryOrders(String orderId) throws WxPayException {
//        WxPayOrderQueryRequest orderRequest = new WxPayOrderQueryRequest();
//        orderRequest.setOutTradeNo(orderId);
//        WxPayService wxPayService = getWxPayService();
//        WxPayOrderQueryResult wxPayOrderQueryResult = wxPayService.queryOrder(orderRequest);
//        System.out.println(JSONObject.toJSONString(wxPayOrderQueryResult));
//        return wxPayOrderQueryResult;
////        if(wxPayOrderQueryResult.getResultCode().equals("SUCCESS")){
////            return wxPayOrderQueryResult;
////        }else{
////            return null;
////        }
//    }

    //    /**
//     * @功能 微信退款
//     * @Author kaedeliu
//     * @创建时间 2026/3/18 10:49
//     * @修改人 kaedeliu
//     * @修改时间 2026/3/18 10:49
//     * @Param
//     * @param orderNum: 系统订单号
//     * @param refundOrder: 退款单号
//     * @param totalMoney: 订单金额，单位分
//     * @param refundMoney: 退款金额，单位分
//     * @param refundDesc: 退款缘由
//     * @return
//    **/
//    public WxPayRefundResult refundV2(String orderNum,String refundOrder,Integer totalMoney,Integer refundMoney,String refundDesc) throws WxPayException {
//        WxPayRefundRequest wxPayRefundRequest=new WxPayRefundRequest();
//        wxPayRefundRequest.setOutTradeNo(orderNum);
//        wxPayRefundRequest.setOutRefundNo(refundOrder);
//        wxPayRefundRequest.setRefundFee(refundMoney);
//        wxPayRefundRequest.setTotalFee(totalMoney);
//        wxPayRefundRequest.setRefundDesc(refundDesc);
//        WxPayService wxPayService = getWxPayService();
//
//        return wxPayService.refund(wxPayRefundRequest);
//    }

}
