package com.juyou.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.juyou.common.constant.DataBaseConstant;
import com.juyou.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;

@Slf4j
public class CommonUtils {


	static final String DBERROR="B0300";
    /**
     * 判断文件名是否带盘符，重新处理
     * @param fileName
     * @return
     */
    public static String getFileName(String fileName){
        //判断是否带有盘符信息
        // Check for Unix-style path
        int unixSep = fileName.lastIndexOf('/');
        // Check for Windows-style path
        int winSep = fileName.lastIndexOf('\\');
        // Cut off at latest possible point
        int pos = (winSep > unixSep ? winSep : unixSep);
        if (pos != -1)  {
            // Any sort of path separator found...
            fileName = fileName.substring(pos + 1);
        }
        //替换上传文件名字的特殊字符
        fileName = fileName.replace("=","").replace(",","").replace("&","").replace("#", "");
        return fileName;
    }

   
//    /** 当前系统数据库类型 */
    private static String DB_TYPE = "";
//    public static String getDatabaseType() {
//        if(oConvertUtils.isNotNull(DB_TYPE)){
//            return DB_TYPE;
//        }
//        DataSource dataSource = SpringContextUtils.getApplicationContext().getBean(DataSource.class);
//        try {
//            return getDatabaseTypeByDataSource(dataSource);
//        } catch (SQLException e) {
//            //e.printStackTrace();
//            log.warn(e.getMessage());
//            return "";
//        }
//    }

    /**
     * 获取数据库类型
     * @param dataSource
     * @return
     * @throws SQLException
     */
    private static String getDatabaseTypeByDataSource(DataSource dataSource) throws SQLException{
        if("".equals(DB_TYPE)) {
            Connection connection = dataSource.getConnection();
            try {
                DatabaseMetaData md = connection.getMetaData();
                String dbType = md.getDatabaseProductName().toLowerCase();
                if(dbType.indexOf("mysql")>=0) {
                    DB_TYPE = DataBaseConstant.DB_TYPE_MYSQL;
                }else if(dbType.indexOf("oracle")>=0) {
                    DB_TYPE = DataBaseConstant.DB_TYPE_ORACLE;
                }else if(dbType.indexOf("sqlserver")>=0||dbType.indexOf("sql server")>=0) {
                    DB_TYPE = DataBaseConstant.DB_TYPE_SQLSERVER;
                }else if(dbType.indexOf("postgresql")>=0) {
                    DB_TYPE = DataBaseConstant.DB_TYPE_POSTGRESQL;
                }else {
                    throw new BaseException(DBERROR,"数据库类型:["+dbType+"]不识别!");
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }finally {
                connection.close();
            }
        }
        return DB_TYPE;

    }
    
    /**
	 *    对MD5文件进行加密处理
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String md5File(File file) throws FileNotFoundException, IOException {
		return DigestUtils.md5Hex(new FileInputStream(file));
	}

	/**
	 *    对MD5文件进行加密处理
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String md5Bytes(byte[] bytes) throws FileNotFoundException, IOException {
		return DigestUtils.md5Hex(bytes);
	}

	/**
	 * 	将List转换为HashMap
	 * @param <T>
	 * @param list
	 * @param keyName
	 * @param clazz
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static <T> HashMap<Object,T> toHashMap(List<T> list,String keyName,Class<T> clazz) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		HashMap<Object, T> map=new HashMap<Object, T>();
		if(list!=null) {
			Field f=null;
			Class<?> c=clazz;
			while (c!=null && f==null) {
				try {
					f=c.getDeclaredField(keyName);
				} catch (NoSuchFieldException e) {
				}
				if(f==null) c=c.getSuperclass();
			}
			if(f==null) throw new NoSuchFieldException(clazz.getTypeName() + ":" +keyName);
			f.setAccessible(true);
			for (T t : list) {
				Object key=f.get(t);
				map.put(key, t);
			}
		}
		return map;
	}

	/**
	 * 通过class获取field，查找父类
	 * @param clazz
	 * @param name
	 * @param accessible
	 * @return
	 */
	public static Field getField(Class<?> clazz,String name,boolean accessible) {
		Field f=null;
		while (clazz!=null) {
			try {
				f=clazz.getDeclaredField(name);
			} catch (Exception e) {
			}
			if(f!=null) {
				f.setAccessible(true);
				return f;
			}else {
				clazz=clazz.getSuperclass();
			}
		}
		return f;
	}

	/**
	 *
	 * @param day
	 * @param date
	 * @param isClaer 是否清空时分秒
	 * @return
	 */
	public static Date addDate(int day,Date date,boolean isClaer) {
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, day);
		if(isClaer) {
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
		}
		return calendar.getTime();
	}

	/**
	 * 从JSON中判断参数是否为空
	 * @param json
	 * @param keys
	 * @return
	 */
	public static String jsonNullValue(JSONObject json,String... keys) {
		if(keys==null)
			return null;
		for (String key : keys) {
			if(!json.containsKey(key))
				return key +" is null";
			Object value=json.get(key);
			if(StringUtils.isEmpty(value)) {
				return key + " value is null";
			}
		}
		return null;
	}

	/**
	 * 从对象中判断是否有值为空
	 * @param obj
	 * @param fields
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public static String objNullValue(Object obj,String... fields) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Class<?>  clazz= obj.getClass();
		for (String field : fields) {
			Field f = clazz.getDeclaredField(field);
			f.setAccessible(true);
			Object val=f.get(obj);
			if(StringUtils.isEmpty(val))
				return field + " value is null";
		}
		return null;
	}

	/**
	 * 从对象中判断是否有值为空
	 * @param obj
	 * @param fields
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public static String mapNullValue(Map<String,Object> obj,String... fields) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		for (String key : fields) {
			if(!obj.containsKey(key))
				return key +" is null";
			Object value=obj.get(key);
			if(StringUtils.isEmpty(value)) {
				return key + " value is null";
			}
		}
		return null;
	}

	/**
	 * 从request中获取出map参数
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getRequestData(HttpServletRequest request) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String, String[]> mapData = request.getParameterMap();
		Iterator<Entry<String, String[]>> it = mapData.entrySet().iterator();
		while (it.hasNext()){
			Entry<String, String[]> entry = (Entry<String, String[]>) it.next();
			for (String v: entry.getValue())
				map.put(entry.getKey(), StringUtils.isEmpty(v)?null:v);
		}
		return map;
	}

	/**
	 * 获取request的内容体
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static String getRequestBody(HttpServletRequest request) throws IOException {
		String notifyData = "";
		InputStream is = request.getInputStream();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			notifyData=sb.toString();
		} finally {
			is.close();
		}
		return notifyData;
	}


	/**
	 * 判断两个对象是否相等
	 * @param one
	 * @param two
	 * @return
	 */
	public static boolean equals(Object one,Object two) {
		if(one==null) return two==null;
		return one.equals(two);
	}

	/**
	 * bytes转base64
	 * @param bytes
	 * @return
	 */
	public static String byte2Base64(byte[] bytes) {
		if(bytes==null)
			return null;
		return Base64.encodeBase64String(bytes);
	}

	public static byte[] base642Byte(String base64String) {
		if(StringUtils.isEmpty(base64String))
			return null;
		return Base64.decodeBase64(base64String);
	}

	public static void writeDate(String filePath,String data) {
		try{
			File file = new File(filePath);

			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			//一次写一行
			bw.write(data);
			//关闭流
			bw.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static void writeDate(String filePath,byte[] data) {
		try{
			File file = new File(filePath);

			FileOutputStream fos=new FileOutputStream(filePath);

			//一次写一行
			fos.write(data);
			//关闭流
			fos.close();
			fos.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static String readTxt(String txtPath){
		return readTxt(txtPath,"\n");
	}
	public static String readTxt(String txtPath,String lineBuff) {
		File file = new File(txtPath);
		if(file.isFile() && file.exists()){
			FileInputStream fileInputStream = null;
			try {
				fileInputStream = new FileInputStream(file);
				InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

				StringBuffer sb = new StringBuffer();
				String text = null;
				while((text = bufferedReader.readLine()) != null){
					sb.append(lineBuff);
					sb.append(text);
				}
				return sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if(fileInputStream!=null){
					try {
						fileInputStream.close();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return null;
	}

	public static <T> List<T>  copyProperties(List<?> sources,Class<T> clazz, String... ignoreProperties) throws Exception{
		List<T> list=new ArrayList<>();
		if(sources==null) return list;
		for(Object item:sources){
			T news= clazz.getDeclaredConstructor().newInstance();
			BeanUtils.copyProperties(item,news);
			list.add(news);
		}
		return list;
	}

}