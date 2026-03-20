package com.juyou.common.utils;



import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class XmlToMap {

    //xml解析
    public static Map doXMLParse(String strxml) throws JDOMException, IOException {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");

        if(null == strxml || "".equals(strxml)) {
            return null;
        }

        Map m = new HashMap();

        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while(it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if(children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }

            m.put(k, v);
        }

        //关闭流
        in.close();

        return m;
    }


    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if(!children.isEmpty()) {
            Iterator it = children.iterator();
            while(it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if(!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) throws JDOMException, IOException {
        String xmlString = "<xml><appid><![CDATA[wx38f6be4059a162db]]></appid><body><![CDATA[善行测试商品]]></body><mch_id><![CDATA[1594366931]]></mch_id><nonce_str><![CDATA[86476306728130041557814283766755]]></nonce_str><notify_url><![CDATA[/buddha/payResultController/wexinPayResult]]></notify_url><out_trade_no><![CDATA[1202005221245261000001]]></out_trade_no><package><![CDATA[Sign=WXPay]]></package><spbill_create_ip><![CDATA[192.168.0.118]]></spbill_create_ip><total_fee><![CDATA[1]]></total_fee><trade_type><![CDATA[APP]]></trade_type><sign>125B30FF3D833C1BBE14B6391AEE09B9</sign></xml>";
        Map<String,String> map = doXMLParse(xmlString);
        System.out.println(map);
    }
}
