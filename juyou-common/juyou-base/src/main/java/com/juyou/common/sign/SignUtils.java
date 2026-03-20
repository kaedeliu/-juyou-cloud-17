package com.juyou.common.sign;


import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.SM2;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;

@Slf4j
public class SignUtils {
	
	/**
	 * 验签
	 * @param body 验签的json,需排除signature
	 * @param privateKey 私钥
	 * @param publicKeyX 公钥X
	 * @param publicKeyY 公钥Y
	 * @return
	 * @throws Exception 
	 */
	public static boolean vertifyBody(JSONObject body,String privateKey,String publicKeyX,String publicKeyY,String sign,String id) throws Exception {
		String input=JSONObject.toJSONString(body,SerializerFeature.MapSortField);
//		log.info(input);
//		log.info(privateKey);
		return SM2Helper.verifySign(input, sign, publicKeyX, publicKeyY, id);
	}
	
	public static boolean vertifyMap(Map<String,Object> paramsData,String privateKey,String publicKeyX,String publicKeyY,String sign,String id) throws Exception {
		String input=mapToString(paramsData);
//		System.out.println(input);
		return SM2Helper.verifySign(input, sign, publicKeyX, publicKeyY, id);
	}
	
	/**
	 * 
	 * @param paramsData
	 * @param privateKey
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public static String sign(Map<String,Object> paramsData,String privateKey,String id) throws Exception {
		String input=mapToString(paramsData);
		return SM2Helper.sign(input, privateKey, id);
	}
	
	/**
	 * 加签
	 * @param body 参与加签的json
	 * @param privateKey 私钥
	 * @param id 身份标识
	 * @return
	 * @throws Exception
	 */
	public static String sign(JSONObject body,String privateKey,String id) throws Exception {
		String input=JSONObject.toJSONString(body,SerializerFeature.MapSortField);
//		log.info(input);
		return SM2Helper.sign(input, privateKey, id);
	}
	
	/**
	 * 加签，不返回数据，直接将sign加入到JSON里
	 * @param body 参与加签的参数
	 * @param privateKey 私钥
	 * @param id 身份证id
	 * @param key 附加签名的key:当前为signature
	 * @throws Exception
	 */
	public static void sign(JSONObject body,String privateKey,String id,String key) throws Exception {
		String sign=sign(body, privateKey, id);
		body.put(key, sign);
	}
	
	/**
	 * 签名改为SM2
	 */
//	/**
//	 * 验签
//	 * @param body jsonBody
//	 * @param signKey 加签KEY
//	 * @return
//	 */
//	public static boolean vertifyBody(JSONObject body,String signKey,String openKey) {
//		String sign=body.remove(signKey).toString();
//		Map<String, Object> params = jsonToMap(body);
//		String srcStr=toJsonString(sortMap(params))+openKey;
//		return vertify(srcStr, sign);
//	}
//	

//	
	private static String mapToString(Map<String,Object> paramsData) {
		List<Entry<String, Object>> list=sortMap(paramsData);
		StringBuffer sb=new StringBuffer();
		for (Entry<String, Object> entry : list) {
			String val=null;
			Object v=entry.getValue();
			if(v==null) val="";
			else
				val=v+"";
			sb.append(entry.getKey()+"="+val).append("&");
		}
	
		if(sb.length()>0) return sb.substring(0, sb.length()-1);
		
		return sb.toString();
	}
//	
//	/**
//	 * 验签
//	 * @param srcStr
//	 * @param sign
//	 * @return
//	 */
//	public static boolean vertify(String srcStr,String sign){
//		return SM.sm3Vertify(srcStr, sign);
//	}
//
//	/**
//	 * 加签JSONObject
//	 * @param data
//	 * @return
//	 */
//	public static String sign(JSONObject data,String key) {
//		Map<String, Object> params = jsonToMap(data);
//		String srcStr=toJsonString(sortMap(params));
//		String sign=SM.sm3Encrypt(srcStr+key);
//		return sign;
//	}
//	
//	/**
//	 * 加签HashMap
//	 * @param params
//	 * @return
//	 */
//	public static String sign(Map<String, Object> params,String key) {
////		List<Map.Entry<String, Object>> temps=sortMap(params);
//		String signStr=mapToString(params);
//		String sign=SM.sm3Encrypt(signStr+key);
//		return sign;
//	}
//	
	/**
	 * 	将hashMap排序
	 * @param params
	 * @return
	 */
	public static List<Entry<String, Object>> sortMap(Map<String, Object> params){
		List<Entry<String, Object>> list = new ArrayList<Entry<String, Object>>(
				params.entrySet());
		Collections.sort(list, new Comparator<Entry<String, Object>>() {
            public int compare(Entry<String, Object> o1,
                    Entry<String, Object> o2) {
                return o1.getKey().toString().compareTo(o2.getKey());
            }
        });
		return list;
	}
//	
//	/**
//	 * 将参数转换为JSON串，有序
//	 * @param params
//	 * @return
//	 */
//	private static String toJsonString(List<Map.Entry<String, Object>> params) {
//		JSONObject json=new JSONObject(true);
//		for (Entry<String, Object> entry : params) {
//			json.put(entry.getKey(), entry.getValue());
//		}
//		return json.toJSONString();
//	}
//	
//	/**
//	 * 将json转换为Map
//	 * @param json
//	 * @return
//	 */
//	private static Map<String,Object> jsonToMap(JSONObject json){
//		Map<String, Object> params = new HashMap<String, Object>();
//		for (@SuppressWarnings("rawtypes") Map.Entry entry : json.entrySet()) {
//			params.put(entry.getKey().toString(), entry.getValue());
//        }
//		return params;
//	}
	
	public static void main(String[] args) throws Exception {
		JSONObject jsonObject=new JSONObject();
////		jsonObject.put("a", "11");
////		jsonObject.put("ts",1626922680);
////		jsonObject.put("signature", "xxxxxxxx");
////		jsonObject.put("b", "22");
//		System.out.println(jsonObject.toJSONString());
//		
////		String str="{\"a\":\"11\",\"ss\":{\"d\":1,\"a\":2},\"ts\":1626922680,\"b\":\"22\",\"openid\":\"testopenid\"}";
//		jsonObject jsonObject= JSONObject.parseObject(str);
////		System.out.println(jsonObject.toJSONString());
////		String sign=sign(jsonObject, "testopenkey");
////		jsonObject.put("sign", sign);
////		System.out.println(jsonObject.toJSONString());
//		
		Charset charset = Charset.forName("utf-8");
		
		String openid="self1001";
		String privateKey="5c8b56f35f30fd3a1f87c1d1c2071715c071023b783f5d29a99cbe90025440bd";
		String publicKeyX="6ef06db355f44dfd0fd175db5eb67f84b40ab88a0964540bbd6f039483a7ba56";
		String publicKeyY="5db4aaf5f0dcb6a799b3d7917252c2b0361b1b4ef015d473b94a3364969f5d25";
//		String openid="selfc001";
//		String privateKey="00e8b2a7e120e5e7e492489bddce0eba63df13497c7594250f44aa76996781816d";
//		String publicKeyX="3b141c5cdb36f7dfb531633befef0165dcd38554123e5d07749194bb4616332b";
//		String publicKeyY="40e4e4ce3e99b8654acb83ee0d71d5070fb7394bc1a5ee419aca22dc3808d70c";
////		Map<String,Object> params=new HashMap<String, Object>();
////		jsonObject.put("ts", System.currentTimeMillis()/1000);
//		jsonObject.put("openid", openid);
//		jsonObject.put("ifRemote", "0");
//		String openid="center001";
//		String privateKey="00986d5725c330cd98a21e13fc5c4a76ac7253352ca951366daa743fe8530e923c";
//		String publicKeyX="5c41d46813aff167125a2a488d1829062396190dcc640d147d94f23077b0d399";
//		String publicKeyY="00c0ffa35718c88efa9502a3a4d9561f6b30082fc4f2c0707442ab45ffb48287";
//		jsonObject.put("openid", openid);
//		//愉客行加签，公共包验签
//		String sign=null;
//		try {
			String sign = sign(jsonObject, privateKey,openid);
			jsonObject.put("signature", sign);
			String js="";
////			System.out.println(sign);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//下单锁位
//		String js="{\"arrivalStationCode\":\"440105005000\",\"channelId\":\"center001\",\"departureStationCode\":\"500056000001\",\"freeNum\":0,\"fullNum\":2,\"halfNum\":0,\"insuranceNum\":0,\"mainMacSeq\":\"8001\",\"openid\":\"center001\",\"otherOrderNum\":\"\",\"passengers\":[{\"certificateCode\":\"01\",\"certificateId\":\"450104197812232025\",\"certificateName\":\"01\",\"children\":\"0\",\"freeChildrenName\":\"\",\"name\":\"刘万里\",\"phone\":\"13719156825\",\"ticketDiscountCode\":\"0\"},{\"certificateCode\":\"01\",\"certificateId\":\"450104197812232216\",\"certificateName\":\"01\",\"children\":\"0\",\"freeChildrenName\":\"\",\"name\":\"刘万里2\",\"phone\":\"13719156826\",\"ticketDiscountCode\":\"0\"}],\"returnPassengers\":1,\"returnSchedule\":1,\"returnScheduleTicket\":0,\"scheduleNumber\":\"C1001\",\"scheduleday\":\"2021-12-13\",\"signature\":\"3046022100e3695fb21d0ceb3bccbbd6e2794f2acf40bbc94672069a06356359bc2b686be6022100a46d9e6036ce1b7f118e401fa02d6d8f2beb925c9cb4a412fdff78bdb0b5a146\",\"source\":10,\"tkSpPsAdd\":{},\"transactionId\":\"2021121310000002\",\"usesCode\":\"0000\"}";
		//@RequestMapping("
//		String js="{\"openid\":\"center001\",\"payTime\":\"2021-12-13 15:11\",\"payType\":\"1\",\"returnPassengers\":1,\"returnSchedule\":1,\"returnScheduleTicket\":0,\"signature\":\"3045022100f9366fe9a9dc5dc2a69cf7735df0afee780aaef0912b7f311d8fd200fd66c38002205e9f499ab2d04460e240dcfe18f14c54716266ff34b1bba69089423fcd011110\",\"source\":10,\"transactionId\":\"2021121310000002\"}";
		//退票查询
//		String js="{\"tickets\":[{\"transactionId\": \"2021121310000002\"}],\"source\":\"10\"}";
		//订单退票
//		String js="{"refundType":0,"returnRate":100,"tickets":[{"transactionId":"2021121310000002"}],"signature":"3046022100a38ae83b015fe5810f515731a3b4166863c3f8785fcbfa6d4e115f3539ff3bbf022100bccadfa2e94b94a9dcd5472d74330ef96f5a082d6a67ce45b68ab19f56a63719","openid":"center001","retunProcedureFee":0,"refundMode":0,"source":10}";
//		String js="{\r\n"
//				+ "  \"payTime\": \"\",\r\n"
//				+ "  \"refundMode\": 0,\r\n"
//				+ "  \"refundType\": 0,\r\n"
//				+ "  \"retunProcedureFee\": 0,\r\n"
//				+ "  \"returnRate\": 0,\r\n"
//				+ "  \"source\": 10,\r\n"
//				+ "  \"tickets\": [\r\n"
//				+ "    {\r\n"
//				+ "      \"insuranceSeq\": \"\",\r\n"
//				+ "      \"invoiceSeq\": \"\",\r\n"
//				+ "      \"passengersSeq\": \"\",\r\n"
//				+ "      \"transactionId\": \"2021121310000002\"\r\n"
//				+ "    }\r\n"
//				+ "  ],\r\n"
//				+ "  \"transTime\": \"\",\r\n"
//				+ "  \"userCode\": \"\",\r\n"
//				+ "  \"userName\": \"\"\r\n"
//				+ "}";
//		String js="{\"ifRemote\":0,\"source\":5}";
//		String js="{\r\n"
//				+ "  \"endStationCode\": \"440105005000\",\r\n"
//				+ "  \"pageNo\": 1,\r\n"
//				+ "  \"pageSize\": 5,\r\n"
//				+ "  \"scheduleDay\": \"2021-10-21\",\r\n"
//				+ "  \"source\":5\r\n"
//				+ "}";
//		String js="{\"departureStationCode\":\"500056000001\",\"departureTime\":\"00:01\",\"endStationCode\":\"8001020001066\",\"openid\":\"self001\",\"pageSize\":\"100\",\"scheduleDay\":\"2021-12-16\",\"source\":\"5\",\"status\":\"0\",\"signature\":\"3045022100a716b7fd5008eddf7b01b80ae80b656ec85589e73a81d97892aa7cae043f8f7902201efdae256fab827439bf6d89a8b32fbcdad19e0abf5a0167360f103c6f105dae\"}";
//		String js="{\"arrivalStationCode\":\"440105005000\",\"departureStationCode\":\"500056000001\",\"freeNum\":0,\"fullNum\":1,\"halfNum\":0,\"insuranceCurrentNumber\":\"1\",\"insuranceNum\":0,\"invoiceCurrentNumber\":\"53\",\"openid\":\"self001\",\"passengers\":[{\"certificateId\":\"500222199101025935\",\"certificateName\":\"身份证\",\"children\":\"0\",\"freeChildrenName\":\"\",\"name\":\"啊啊\",\"phone\":\"\"}],\"returnPassengers\":\"1\",\"returnSchedule\":\"1\",\"returnScheduleTicket\":\"1\",\"scheduleday\":\"2021-10-16\",\"scheduleNumber\":\"C1004\",\"source\":\"5\",\"startMachinerySeq\":\"001\",\"transactionId\":\"2110150001010100000687\"}";
//		String js="{\"departureTime\":\"19:51\",\"machineryCode\":\"8001\",\"signature\":\"304502203e38fb13e7f565e7e14b63a30d5f0a0a60382a77781d822f706309437c3627d60221009adb3aff169bca1075e34676e291580178eba0306a1c2cf0cb421a354f19c0f2\",\"stype\":1,\"openid\":\"ticket001\",\"pageSize\":10,\"source\":4,\"pageNo\":1,\"scheduleDay\":\"2022-01-06\",\"endStationCode\":\"001020003064\"}";
//		 js="{\"machineryCode\":\"8001\",\"endStationCode\":\"8001020001535\",\"scheduleDay\":\"2022-02-01\",\"stype\":1,\"source\":5,\"pageSize\":10,\"pageNo\":1,\"departureTime\":\"18:25\"}";
		js="{\"openid\":\"center001\",\"orgCode\":\"8001\",\"signature\":\"3044022007760e8bf610620ff7c900014408936252780a0672199831324546512ffe897202201b65d43680971e01aaa2770cd54c4c6397eaf8fe3d0b43707aff39dceca7becd\",\"source\":\"10\",\"tickets\":[{\"passengersSeq\":\"22028001010200210387\"}]}";
		jsonObject = JSONObject.parseObject(js);
		jsonObject.put("openid", openid);
		jsonObject.remove("signature");
		sign = sign(jsonObject, privateKey,openid);
//		sign="3046022100d4e0a5d6077b499bbcd6c8da1c458469fd6b08b71f8ead860888f4ad4ac6806a02210099bf73fff3ac55b21e85d7f11a9e61f3a27f3cbb2d1953f8045e87dd2684f5a5";
		boolean b=false;
//		String sign="3045022100f0de86c71c1b3e0b8044854620ab21530642fdf71da83120161b83e94b635c64022050660e2b414d0ca5118fed69f7a90280be628b940076b1a8e72f9e5e03d05cb9";
		b=vertifyBody(jsonObject, privateKey, publicKeyX, publicKeyY, sign, openid);
		System.out.println("B1:"+b);
		ECPublicKeyParameters publicKey=SM2Helper.buildECPublicKeyParameters(publicKeyX, publicKeyY);
		ECPrivateKeyParameters privateKeyp=SM2Helper.buildECPrivateKeyParameters(privateKey);
		SM2 sm2=SmUtil.sm2(privateKeyp, publicKey);
		 byte[] signByte = new BigInteger(sign, 16).toByteArray();
		b=sm2.verify(JSONObject.toJSONString(jsonObject,SerializerFeature.MapSortField).getBytes(charset), signByte, openid.getBytes(charset));
		System.out.println("B2:"+b);
//		
//		//公共包加签愉客行验签
//		sign=Hex.toHexString(sm2.sign(jsonObject.toJSONString().getBytes(charset), openid.getBytes(charset)));
//		b=vertifyBody(jsonObject, privateKey, publicKeyX, publicKeyY, sign, openid);
//		System.out.println("B3:"+b);
		jsonObject.put("signature", sign);
		
//		
		log.info(jsonObject.toJSONString());
		
		//联网中心加签
//		String openid="center001";
//		String privateKey="00d2b9a7aa39e5b69b770a011dc89a76fff79c33679bac0f3ee9705a202c0b7335";
//		JSONObject json=new JSONObject();
//		json.put("openid", openid);
////		json.put("province", "重庆");
////		json.put("city", "重庆");
////		json.put("timeStamp", "");
//		String sign=sign(json, privateKey, openid);
//		
//		String publicKeyX="227ccc23299dad159e01e27b3ffab21bd53fdd445b3dddb46b50dc7a619b99bd";
//		String publicKeyY="bc4e89556b034f2604a046a7f5c422e8578c2ce2e30858d41025aedac59e5958";
//		boolean b=vertifyBody(json, privateKey, publicKeyX, publicKeyY, sign, openid);
//		System.out.println(b);
//		json.put("signature", sign);
//		System.out.println(JSONObject.toJSONString(json, SerializerFeature.MapSortField));
		
	}
	
	
}
