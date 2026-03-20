package com.juyou.common.error;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.yaml.snakeyaml.Yaml;

/**
 * 将配置文件生成常量使用
 * @return
 */
public class ErrorCreateClass {

	
	public static String loadYml(InputStream input) {
		Yaml yaml=new Yaml();
		Map<String, Object> map =yaml.load(input);
		Iterator<Entry<String, Object>>  iterator = map.entrySet().iterator();
		StringBuffer sb=new StringBuffer();
		while (iterator.hasNext()) {
			Entry<String, Object> entry=iterator.next();
			String key=entry.getKey();
			recursion(key, entry.getValue(), sb);
//			String k=key.substring(key.lastIndexOf(".")+1,key.length());
//			String val=entry.getValue()+"";
//			sb.append("\t /** \r\n");
//			sb.append("\t /** "+val+" " + val+" \r\n");
//			sb.append("\t **/ \r\n");
//			sb.append("\tpublic static final String "+key + " = "+key+";\r\n");
//			sb.append("\r\n");
		}
		return sb.toString();
	}
	
	private static void recursion(String key,Object value,StringBuffer sb) {
		if(value instanceof LinkedHashMap) {
			@SuppressWarnings("unchecked")
			LinkedHashMap<String,Object> map= (LinkedHashMap<String,Object>) value;
			Iterator<Entry<String, Object>>  iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> entry=iterator.next();
				String k=entry.getKey();
				Object v=entry.getValue();
				recursion(key+"."+k, v, sb);
			}
		}else {
			String k=key.substring(key.lastIndexOf(".")+1,key.length());
			String val=value+"";
			sb.append("\t /** \r\n");
			sb.append("\t ** "+val+" " + key+" \r\n");
			sb.append("\t **/ \r\n");
			sb.append("\tpublic static final String "+k + " = \""+key+"\";\r\n");
			sb.append("\r\n");
		}
	}
	
	public static void create(String ymlPath,String packageStr,String javaFileName,String outFile) throws IOException {
		FileInputStream fileInput=new FileInputStream(new File(ymlPath));
		StringBuffer sb=new StringBuffer();
		sb.append("package "+packageStr+";\r\n");
		sb.append("\r\n");
		sb.append("/**\r\n 异常常量定义 \r\n**/\r\n");
		sb.append("public class "+javaFileName+"{\r\n");
		sb.append("\r\n");
		String str=loadYml(fileInput);
		sb.append(str);
		sb.append("\r\n}");
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFile, javaFileName+".java")));
       //一次写一行
        bw.write(sb.toString());
           //关闭流
       bw.close();
       System.out.println("写入成功");
	}
	
	
	public static void main(String[] args) throws IOException {
		create("D:\\dev\\work\\chongqing\\ticket\\server\\ticket-server\\ticket_server\\config\\src\\main\\resources\\dev\\error-dev.yml"
				, "com.juyou.ticket.common.error","ErrorCode", "D:\\test");
	}
	
}
