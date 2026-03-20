package com.juyou.pay.aipay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.juyou.pay.config.AliyunConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * @功能 阿里云支付
 * @Author kaedeliu
 * @创建时间 2026/3/18 10:49
 * @修改人 kaedeliu
 * @修改时间 2026/3/18 10:49
 * @Param
 * @return
 **/
@Slf4j
@Component
public class AliyunPay {

	@Autowired
    AliyunConfig aliyunConfig;

	public AlipayClient getAlipayClient() {
		// 支付宝网关
		String url = aliyunConfig.getUrl();
		// 商户的APPID
		String appid = aliyunConfig.getAppId();
		// 商户的私钥
		String appPrivateKey = aliyunConfig.getRsaPrivatekey();
		// 参数返回格式
		String format = aliyunConfig.getFormat() == null ? "json" : aliyunConfig.getFormat();
		// 字符编码
		String charset = aliyunConfig.getCharset() == null ? "UTF-8" : aliyunConfig.getCharset();
		// 支付宝公钥
		String alipayPublicKey = aliyunConfig.getAlipayPublicKey();
		// 商户生成签名的算法RSA2
		String signtype = aliyunConfig.getSignType() == null ? "RSA2" : aliyunConfig.getSignType();
		// 实例化客户端
		AlipayClient alipayClient = new DefaultAlipayClient(url, appid, appPrivateKey, format, charset, alipayPublicKey, signtype);
		return alipayClient;
	}

	/**
	 * @功能 支付宝支付
	 * @Author kaedeliu
	 * @创建时间 2026/3/18 10:49
	 * @修改人 kaedeliu
	 * @修改时间 2026/3/18 10:49
	 * @Param
	 * @param orderNum:
	 *            订单号
	 * @param money:
	 *            支付金额,单位分
	 * @param subject:
	 *            商品标题
	 * @param body:
	 *            支付内容
	 * @param notifyUrl:
	 *            支付回调，如果不填写，使用系统配置
	 * @param retrunUrl:
	 *            支付成功返回，如果不填写，使用系统配置
	 * @param driverType:
	 *            1安卓，2苹果，3h5
	 * @return
	 **/
	public String alipay(String orderNum, Integer money, String subject, String body, String notifyUrl, String retrunUrl, Integer driverType) {
		if (driverType == 1 || driverType == 2) {
			return alipay(orderNum, money, subject, body, notifyUrl, retrunUrl);
		} else if (driverType == 3) {
			return alipayH5(orderNum, money, subject, body, notifyUrl, retrunUrl);
		}
		return null;
	}

	/**
	 * @功能 支付宝支付
	 * @Author kaedeliu
	 * @创建时间 2026/3/18 10:49
	 * @修改人 kaedeliu
	 * @修改时间 2026/3/18 10:49
	 * @Param
	 * @param orderNum:
	 *            订单号
	 * @param money:
	 *            支付金额,单位分
	 * @param subject:
	 *            商品标题
	 * @param body:
	 *            支付内容
	 * @param notifyUrl:
	 *            支付回调，如果不填写，使用系统配置
	 * @param retrunUrl:
	 *            支付成功返回，如果不填写，使用系统配置
	 * @return
	 **/
	public String alipay(String orderNum, Integer money, String subject, String body, String notifyUrl, String retrunUrl) {
		// 实例化客户端
		AlipayClient alipayClient = getAlipayClient();
		// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setSubject(subject); // 商品标题
		model.setBody(body);
		model.setOutTradeNo(orderNum); // 商家订单的唯一编号
		model.setTimeoutExpress("30m"); // 超时关闭该订单时间
		Double money2 = Double.valueOf(money / 100.00);

		model.setTotalAmount(money2.toString()); // 订单总金额
		// model.setTotalAmount("0.01"); //订单总金额
		model.setProductCode("QUICK_MSECURITY_PAY"); // 销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
		model.setSellerId(aliyunConfig.getSellerId());
		request.setBizModel(model);
		request.setNotifyUrl(StringUtils.hasLength(notifyUrl) ? notifyUrl : aliyunConfig.getNotifyUrl()); // 回调地址
		request.setReturnUrl(StringUtils.hasLength(retrunUrl) ? retrunUrl : aliyunConfig.getReturnUrl());
		String orderString = "";
		log.info("支付宝APP请求参数:" + JSONObject.toJSONString(request));
		try {
			// 这里和普通的接口调用不同，使用的是sdkExecute
			AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
			// orderString 可以直接给客户端请求，无需再做处理。
			orderString = response.getBody();
			System.out.println("调取成功");
			System.out.println(orderString);
		} catch (AlipayApiException e) {
			System.out.println("异常处理");
			e.printStackTrace();
		}
		return orderString;
	}

	public String alipayH5(String orderNum, Integer money, String subject, String body, String notifyUrl, String retrunUrl) {
		// 实例化客户端
		AlipayClient alipayClient = getAlipayClient();
		// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
		// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeWapPayModel wapPayModel = new AlipayTradeWapPayModel();
		wapPayModel.setSubject(subject);
		// h5PayModel.setAuth_token(orderNum);
		wapPayModel.setOutTradeNo(orderNum);
		Double money2 = Double.valueOf(money / 100.00);
		wapPayModel.setTotalAmount(money2.toString());
		wapPayModel.setSellerId(aliyunConfig.getSellerId());
		// h5PayModel.setQuit_url(returnUrl);
		wapPayModel.setProductCode("QUICK_MSECURITY_PAY");
		wapPayModel.setBody(body);
		// h5PayModel.setGoods_type(goodsType);
		// String model = JSONObject.toJSONString(h5PayModel);
		request.setBizModel(wapPayModel);
		request.setNotifyUrl(StringUtils.hasLength(notifyUrl) ? notifyUrl : aliyunConfig.getNotifyUrl()); // 回调地址
		request.setReturnUrl(StringUtils.hasLength(retrunUrl) ? retrunUrl : aliyunConfig.getReturnUrl());
		// request.setReturnUrl(AliyunPayModel.return_url);
		log.info("支付宝H5请求参数:" + JSONObject.toJSONString(request));
		String orderString = "";
		try {
			// 这里和普通的接口调用不同，使用的是sdkExecute
			// AlipayTradeAppPayResponse response =
			// alipayClient.sdkExecute(request);
			AlipayTradeWapPayResponse response = alipayClient.pageExecute(request);
			// orderString 可以直接给客户端请求，无需再做处理。
			orderString = response.getBody();
			System.out.println("调取成功");
			System.out.println(orderString);
		} catch (AlipayApiException e) {
			System.out.println("异常处理");
			e.printStackTrace();
		}
		return orderString;
	}

	/**
	 * @功能 查询订单支付结果
	 * @Author kaedeliu
	 * @创建时间 2026/3/18 10:49
	 * @修改人 kaedeliu
	 * @修改时间 2026/3/18 10:49
	 * @Param
	 * @param orderSn:
	 * @return
	 **/
	public AlipayTradeQueryResponse selectOrder(String orderSn) throws AlipayApiException {
		// 实例化客户端
		AlipayClient alipayClient = getAlipayClient();
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		AlipayTradeQueryModel queryModel = new AlipayTradeQueryModel();
		queryModel.setOutTradeNo(orderSn);
		//log.info(JSONObject.toJSONString(h5PayModel));
		request.setBizModel(queryModel);
		AlipayTradeQueryResponse response = alipayClient.execute(request);
		if (response.isSuccess()) {
			System.out.println("调用成功");
			return response;
		} else {
			System.out.println("调用失败");
			return null;
		}
	}

	/**
	 * @功能 阿里退款
	 * @Author kaedeliu
	 * @创建时间 2026/3/18 10:49
	 * @修改人 kaedeliu
	 * @修改时间 2026/3/18 10:49
	 * @Param
	 * @param orderNum:
	 *            系统订单号
	 * @param refundOrderNum:
	 *            退款订单号
	 * @param refundAmount:
	 *            退款金额
	 * @param refundReason:
	 *            退款说明
	 * @return
	 **/

	public AlipayTradeRefundResponse refund(String orderNum, String refundOrderNum, Integer refundAmount, String refundReason) throws AlipayApiException {
		// 实例化客户端
		AlipayClient alipayClient = getAlipayClient();
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		AlipayTradeRefundModel refundModel = new AlipayTradeRefundModel();
		refundModel.setOutTradeNo(orderNum);
		refundModel.setOutRequestNo(refundOrderNum);
		refundModel.setRefundAmount(BigDecimal.valueOf(Long.valueOf(refundAmount.toString())).divide(new BigDecimal(100)).toString());
		refundModel.setRefundReason(refundReason);
		//
		request.setBizModel(refundModel);
		AlipayTradeRefundResponse response = alipayClient.execute(request);
		return response;
		// if(response.isSuccess()){
		// System.out.println("调用成功");
		// return response;
		// } else {
		// System.out.println("调用失败");
		// return null;
		// }
	}

	public static void main(String[] args) throws AlipayApiException {
		String orderSn = "1202005201703151000021";
		// selectOrder(orderSn);
		/*
		 * String returnUrl =
		 * "http://192.168.8.55:8080/order-pay/a1b999c7a34aa2086fcfa2de4df6cb5f";
		 * BfPay pay = new BfPay(); pay.setOrderData("231313212321");
		 * pay.setUserId("2131312"); String orderSn =
		 * UtilString.getOrderSn("1"); System.out.println(orderSn);
		 * pay.setPayOrderNumber(orderSn); String goodsName = "测试产品"; String
		 * goodsType = "0"; String userToken = "23213123131231231323132123213";
		 * alipay(pay,goodsName,returnUrl);
		 */
		// alipay("1202005131550021000009");
	}
}
