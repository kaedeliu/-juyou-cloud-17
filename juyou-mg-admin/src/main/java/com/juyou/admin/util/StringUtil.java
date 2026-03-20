package com.juyou.admin.util;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;

/**
 * 字符串 工具类
 * 
 * @author Melody
 *
 */
public class StringUtil {
	/**
	 * Redis服务名称
	 */
	public static final String REDIS_SERVICE = "redisService";
	/**
	 * request
	 */
	public static final String REQUEST = "request";
	/**
	 * request
	 */
	public static final String RESPONSE = "response";
	/**
	 * SDO
	 */
	public static final String SDO = "sdo";
	/**
	 * 用户
	 */
	public static final String USER = "user";
	/**
	 * 版本号
	 */
	public static final String VERSION = "version";
	/**
	 * 信息
	 */
	public static final String INFO = "info";
	/**
	 * 系统
	 */
	public static final String SYSTEM = "system";
	/**
	 * 参数
	 */
	public static final String PARAMETER = "parameter";
	/**
	 * 菜单
	 */
	public static final String MENU = "menu";
	/**
	 * 角色
	 */
	public static final String ROLE = "role";
	/**
	 * 字典
	 */
	public static final String DICTIONARY = "dictionary";
	/**
	 * Redis 所有key都包含的字符（:）
	 */
	public static final String REDIS_PREFIX = ":";
	/**
	 * 空格（ ）
	 */
	public static final String BLANK_SPACE = " ";
	/**
	 * 星号（*）
	 */
	public static final String ASTERISK = "*";
	/**
	 * 减号（-）
	 */
	public static final String MINUS_SIGN = "-";
	/**
	 * 下划线（_）
	 */
	public static final String UNDERLINE = "_";
	/**
	 * 斜杠（/）
	 */
	public static final String BIAS = "/";
	/**
	 * 文号（?）
	 */
	public static final String QUESTION_MARK = "?";
	/**
	 * 等号（=）
	 */
	public static final String EQUALITY_SIGN = "=";
	/**
	 * 斜杠（\）
	 */
	public static final String BACK_SLANT = "\\";
	/**
	 * 点（.）
	 */
	public static final String SPOT = ".";
	/**
	 * 逗号（,）
	 */
	public static final String COMMA = ",";
	/**
	 * 空字符串（""）
	 */
	public static final String NULL = "";
	/**
	 * 替换（"%s"）
	 */
	public static final String REPLACE = "%s";
	/**
	 * 时间格式 yyyy-MM-dd HH:mm:ss
	 */
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 修改
	 */
	public static final String UPDATE = "update";
	/**
	 * 修改
	 */
	public static final String TABLE_TOOL = "tableTool";
	/**
	 * 菜单名称
	 */
	public static final String[] MENU_NAME = { "新增", "修改", "删除", "启用", "禁用" };
	/**
	 * 菜单编码
	 */
	public static final String[] MENU_CODE = { "add", "update", "delete", "enable", "disabled" };
	/**
	 * 菜单图标
	 */
	public static final String[] MENU_ICON = { "layui-icon-add-1", "layui-icon-file-b", "layui-icon-delete", "layui-icon-ok", "layui-icon-close" };
	/**
	 * 菜单类型
	 */
	public static final Long MENU_TYPE = 3L;
	/**
	 * 菜单URL
	 */
	public static final String MENU_URL = "javascript:;";
	/**
	 * 树根节点ID
	 */
	public static final String TREE_ROOT_ID = "0";
	/**
	 * 文件大小单位（MB）
	 */
	public static final String SIZE_MB = "MB";
	/**
	 * 文件大小单位（KB）
	 */
	public static final String SIZE_KB = "KB";
	/**
	 * 文件大小单位（B）
	 */
	public static final String SIZE_B = "B";
	/**
	 * 附件根目录文件夹名称
	 */
	public static final String ATTACHMENT_PATH = "attachment";
	/**
	 * 单个附件上传专用bizId
	 */
	public static final String ATTACHMENT_BIZ = "biz";
	/**
	 * 人员初始化密码
	 */
	public static final String INITIALIZATION_PASSWORD = "initializationPassword";
	/**
	 * 附件数量
	 */
	public static final String ATTACHMENTS_NUMBER = "attachmentsNumber";
	/**
	 * contentType
	 */
	public static final String CONTENT_TYPE = "text/json; charset=utf-8";
	/**
	 * 编码
	 */
	public static final String UTF8 = "utf-8";
	/**
	 * limit 1
	 */
	public static final String LIMIT_1 = "limit 1";

	/**
	 * 判断字符串是否为空
	 * 
	 * @param string 字符串
	 * @return true/false
	 */
	public static boolean isBlank(String string) {
		if ((null == string) || (StringUtil.NULL.equals(string.trim()))) {
			return true;
		}
		return false;
	}

	/**
	 * 生成32位UUID
	 * 
	 * @return UUID
	 */
	public static String getUuid() {
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replaceAll(StringUtil.MINUS_SIGN, StringUtil.NULL);
		return uuid;
	}

	/**
	 * 字符集合转成字符串，以（,）隔开
	 * 
	 * @param arr
	 * @return
	 */
	public static String arrayToString(Collection<String> arr) {
		String str = StringUtil.NULL;
		if (arr != null && arr.size() > 0) {
			int count = 0;
			for (String string : arr) {
				if (count == 0) {
					str += string;
				} else {
					str += StringUtil.COMMA + string;
				}
				count++;
			}
		}
		return str;
	}

	/**
	 * 随机数
	 * 
	 * @param place 定义随机数的位数
	 */
	public static String randomGen(int place) {
		String base = "qwertyuioplkjhgfdsazxcvbnmQAZWSXEDCRFVTGBYHNUJMIKLOP0123456789";
		StringBuffer sb = new StringBuffer();
		Random rd = new Random();
		for (int i = 0; i < place; i++) {
			sb.append(base.charAt(rd.nextInt(base.length())));
		}
		return sb.toString();
	}
}
