package com.juyou.common.query;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.juyou.common.constant.CommonConstant;
import com.juyou.common.dto.LoginUserBasicDto;
import com.juyou.common.permissiondata.PermissionDataRule;
import com.juyou.common.permissiondata.PermissionDataRuleUtils;
import com.juyou.common.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class QueryGenerator {

	public static final String SQL_RULES_COLUMN = "SQL_RULES_COLUMN";


	private static final String BEGIN = "_begin";
	private static final String END = "_end";
	/**
	 * 数字类型字段，拼接此后缀 接受多值参数
	 */
	private static final String MULTI = "_MultiString";
	private static final String STAR = "*";
	private static final String COMMA = ",";
	private static final String NOT_EQUAL = "!";
	/**页面带有规则值查询，空格作为分隔符*/
	private static final String QUERY_SEPARATE_KEYWORD = " ";
	/**高级查询前端传来的参数名*/
	private static final String SUPER_QUERY_PARAMS = "superQueryParams";
	/** 高级查询前端传来的拼接方式参数名 */
	private static final String SUPER_QUERY_MATCH_TYPE = "superQueryMatchType";
	/** 单引号 */
	public static final String SQL_SQ = "'";
	/**排序列*/
	private static final String ORDER_COLUMN = "column";
	/**排序方式*/
	private static final String ORDER_TYPE = "order";
	private static final String ORDER_TYPE_ASC = "ASC";


	/**
	 * 获取查询条件构造器QueryWrapper实例 通用查询条件已被封装完成
	 * @param reqeustObject 查询实体
	 * @param clazz 实体类型
	 * @return QueryWrapper实例
	 */
	public static <T> QueryWrapper<T> initQueryWrapper(Object reqeustObject,Class<T> clazz) throws Exception {
		long start = System.currentTimeMillis();
		T searchObj = clazz.getDeclaredConstructor().newInstance();
		BeanUtils.copyProperties(reqeustObject,searchObj);

		QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
		installMplus(queryWrapper, searchObj,reqeustObject, SpringContextUtils.getHttpServletRequest().getParameterMap());
		log.debug("---查询条件构造器初始化完成,耗时:"+(System.currentTimeMillis()-start)+"毫秒----");
		return queryWrapper;
	}

	/**
	 * 获取查询条件构造器QueryWrapper实例 通用查询条件已被封装完成
	 * @param searchObj 查询实体
	 * @param parameterMap request.getParameterMap()
	 * @return QueryWrapper实例
	 */
	public static <T> QueryWrapper<T> initQueryWrapper(T searchObj,Object reqeustObject,Map<String, String[]> parameterMap){
		long start = System.currentTimeMillis();
		QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
		installMplus(queryWrapper, searchObj,reqeustObject, parameterMap);
		log.debug("---查询条件构造器初始化完成,耗时:"+(System.currentTimeMillis()-start)+"毫秒----");
		return queryWrapper;
	}

	/**
	 * 组装Mybatis Plus 查询条件
	 * <p>使用此方法 需要有如下几点注意:
	 * <br>1.使用QueryWrapper 而非LambdaQueryWrapper;
	 * <br>2.实例化QueryWrapper时不可将实体传入参数
	 * <br>错误示例:如QueryWrapper<JeecgDemo> queryWrapper = new QueryWrapper<JeecgDemo>(jeecgDemo);
	 * <br>正确示例:QueryWrapper<JeecgDemo> queryWrapper = new QueryWrapper<JeecgDemo>();
	 * <br>3.也可以不使用这个方法直接调用 {@link #initQueryWrapper}直接获取实例
	 */
	public static void installMplus(QueryWrapper<?> queryWrapper,Object searchObj,Object reqeustObject,Map<String, String[]> parameterMap) {

        /*
		 * 注意:权限查询由前端配置数据规则 当一个人有多个所属部门时候 可以在规则配置包含条件 orgCode 包含 #{sys_org_code}
		但是不支持在自定义SQL中写orgCode in #{sys_org_code} 
		当一个人只有一个部门 就直接配置等于条件: orgCode 等于 #{sys_org_code} 或者配置自定义SQL: orgCode = '#{sys_org_code}'
		*/
		//区间条件组装 模糊查询 高级查询组装 简单排序 权限查询
		PropertyDescriptor origDescriptors[] = PropertyUtils.getPropertyDescriptors(searchObj);
		Map<String,PermissionDataRule> ruleMap = getRuleMap();

		//权限规则自定义SQL表达式
		for (String c : ruleMap.keySet()) {
			if(StringUtils.hasLength(c) && c.startsWith(SQL_RULES_COLUMN)){
				queryWrapper.and(i ->i.apply(getSqlRuleValue(ruleMap.get(c).getRuleValue())));
			}
		}

		Map<String, String> aliasNames = aliasName(parameterMap);
		Map<String, String> aliasNamesRule = aliasNameRule(ruleMap);
		String name, type;
		for (int i = 0; i < origDescriptors.length; i++) {
			//多表查询有别名情况
			//aliasName = origDescriptors[i].getName();  mybatis  不存在实体属性 不用处理别名的情况
			name = origDescriptors[i].getName();
			type = origDescriptors[i].getPropertyType().toString();
			try {
				if (judgedIsUselessField(name)|| !PropertyUtils.isReadable(searchObj, name)) {
					continue;
				}

				Object value = PropertyUtils.getSimpleProperty(searchObj, name);
				// update-begin--Author:taoyan  Date:20200910 for：issues/1671 如果字段加注解了@TableField(exist = false),不走DB查询-------
				//如果字段加注解了@TableField(exist = false),不走DB查询
				//TODO 存在缺陷，这个写法 clazz.getDeclaredField(name) 获取不到继承的父实体字段
				try {
					if (!StringUtils.isEmpty(value)) {
						Field field = CommonUtils.getField(searchObj.getClass(), name, false);
//						Field field = searchObj.getClass().getDeclaredField(name);
						if (field != null) {
							TableField tableField = field.getAnnotation(TableField.class);
							if (tableField != null && tableField.exist() == false) {
								continue;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// update-end--Author:taoyan  Date:20200910 for：issues/1671 如果字段加注解了@TableField(exist = false),不走DB查询 -------

				String anName=null;
				//对新增的注解进行处理
				Field f= CommonUtils.getField(searchObj.getClass(), name, false);
				if(f!=null ) {
					QueryField  afile=f.getDeclaredAnnotation(QueryField.class);
					if(afile!=null) {
						anName=afile.value();
					}
				}

				//存在别名的查询
				if(aliasNames.containsKey(name) ) {
					name=aliasNames.get(name);
					if(parameterMap.get(name) !=null)
						value=parameterMap.get(name)[0];
				}

				if(StringUtils.hasLength(anName)) {
					name=anName;
				}

				//数据权限查询
				if(ruleMap.containsKey(name) || aliasNamesRule.containsKey(name)) {
					String asName=name;
					if(aliasNamesRule.containsKey(name))
						asName=aliasNamesRule.get(name);
					addRuleToQueryWrapper(ruleMap.get(asName), asName, origDescriptors[i].getPropertyType(), queryWrapper);
				}

				// 添加 判断是否有区间值
				String endValue = null,beginValue = null;
				if (parameterMap != null && parameterMap.containsKey(name + BEGIN)) {
					beginValue = parameterMap.get(name+BEGIN)[0].trim();
					addQueryByRule(queryWrapper, name, type, beginValue, QueryRuleEnum.GE);

				}
				if (parameterMap != null && parameterMap.containsKey(name + END)) {
					endValue = parameterMap.get(name + END)[0].trim();
					addQueryByRule(queryWrapper, name, type, endValue, QueryRuleEnum.LE);
				}
				//多值查询
				if (parameterMap != null && parameterMap.containsKey(name + MULTI)) {
					endValue = parameterMap.get(name + MULTI)[0].trim();
					addQueryByRule(queryWrapper, name.replace(MULTI,""), type, endValue, QueryRuleEnum.IN);
				}


				//判断单值  参数带不同标识字符串 走不同的查询
				//TODO 这种前后带逗号的支持分割后模糊查询需要否 使多选字段的查询生效
				if (null != value && value.toString().startsWith(COMMA) && value.toString().endsWith(COMMA)) {
					String multiLikeval = value.toString().replace(",,", COMMA);
					String[] vals = multiLikeval.substring(1).split(COMMA);
					final String field = oConvertUtils.camelToUnderline(name);
					if(vals.length>1) {
						queryWrapper.and(j -> {
							j = j.like(field,vals[0]);
							for (int k=1;k<vals.length;k++) {
								j = j.or().like(field,vals[k]);
							}
							//return j;
						});
					}else {
						queryWrapper.and(j -> j.like(field,vals[0]));
					}
				}else {
					//根据参数值带什么关键字符串判断走什么类型的查询
					QueryRuleEnum rule = convert2Rule(value);
					value = replaceValue(rule,value);
					// add -begin 添加判断为字符串时设为全模糊查询
					//if( (rule==null || QueryRuleEnum.EQ.equals(rule)) && "class java.lang.String".equals(type)) {
					// 可以设置左右模糊或全模糊，因人而异
					//rule = QueryRuleEnum.LIKE;
					//}
					// add -end 添加判断为字符串时设为全模糊查询
					addEasyQuery(queryWrapper, name, rule, value);
				}


			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		// 排序逻辑 处理
//		doMultiFieldsOrder(queryWrapper, parameterMap);

		doMultiFieldsOrder(queryWrapper,reqeustObject);
		//高级查询
		doSuperQuery(queryWrapper, parameterMap);

	}


	/**
	 * 获取请求对应的数据权限规则
	 * @return
	 */
	public static Map<String, PermissionDataRule> getRuleMap() {
		Map<String, PermissionDataRule> ruleMap = new HashMap<String, PermissionDataRule>();
		List<PermissionDataRule> list =PermissionDataRuleUtils.getMenuDataRules();
		if(list != null&&list.size()>0){
			if(list.get(0)==null){
				return ruleMap;
			}
			for (PermissionDataRule rule : list) {
				String column = rule.getRuleColumn();
				if(QueryRuleEnum.SQL_RULES.getValue().equals(rule.getRuleConditions())) {
					column = SQL_RULES_COLUMN+rule.getDataRuleId();
				}
				ruleMap.put(column, rule);
			}
		}
		return ruleMap;
	}


	/**
	 * 整理别名
	 * @param parameterMap
	 * @return
	 */
	public static Map<String, String> aliasName(Map<String, String[]> parameterMap) {
		Map<String, String> aliasNames=new HashMap<String, String>();
		Iterator<String>  iterator= parameterMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			if(key.indexOf(".")>0) {
				String name=key.substring(key.lastIndexOf(".")+1);
				if(name.endsWith(BEGIN) || name.endsWith(END) || name.endsWith(MULTI)) {
					name=name.substring(0,name.lastIndexOf("_"));
					key=key.substring(0,key.lastIndexOf("_"));
				}
				aliasNames.put(name, key);
			}
		}
		return aliasNames;
	}

	/**
	 * 整理别名
	 * @param parameterMap
	 * @return
	 */
	public static Map<String, String> aliasNameRule(Map<String,PermissionDataRule> parameterMap) {
		Map<String, String> aliasNames=new HashMap<String, String>();
		Iterator<String>  iterator= parameterMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			if(key.indexOf(".")>0) {
				String name=key.substring(key.lastIndexOf(".")+1);
				if(name.endsWith(BEGIN) || name.endsWith(END) || name.endsWith(MULTI)) {
					name=name.substring(0,name.lastIndexOf("_"));
					key=key.substring(0,key.lastIndexOf("_"));
				}
				aliasNames.put(name, key);
			}
		}
		return aliasNames;
	}


	public static String getSqlRuleValue(String sqlRule){
		try {
			Set<String> varParams = getSqlRuleParams(sqlRule);
			for(String var:varParams){
				String tempValue = converRuleValue(var);
				sqlRule = sqlRule.replace("#{"+var+"}",tempValue);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return sqlRule;
	}

	/**
	 * 获取sql中的#{key} 这个key组成的set
	 */
	public static Set<String> getSqlRuleParams(String sql) {
		if(!StringUtils.hasLength(sql)){
			return null;
		}
		Set<String> varParams = new HashSet<String>();
		String regex = "\\#\\{\\w+\\}";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(sql);
		while(m.find()){
			String var = m.group();
			varParams.add(var.substring(var.indexOf("{")+1,var.indexOf("}")));
		}
		return varParams;
	}

	public static String converRuleValue(String ruleValue) {
		String value = getUserInfoValue(ruleValue,null);
		return value!= null ? value : ruleValue;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void addRuleToQueryWrapper(PermissionDataRule dataRule, String name, Class propertyType, QueryWrapper<?> queryWrapper) {
		QueryRuleEnum rule = QueryRuleEnum.getByValue(dataRule.getRuleConditions());
		if(rule!=null && rule.equals(QueryRuleEnum.IN) && ! propertyType.equals(String.class)) {
			String[] values = dataRule.getRuleValue().split(",");
			Object[] objs = new Object[values.length];
			for (int i = 0; i < values.length; i++) {
				objs[i] = NumberUtils.parseNumber(values[i], propertyType);
			}
			addEasyQuery(queryWrapper, name, rule, objs);
		}else {
			if (propertyType.equals(String.class)) {
				addEasyQuery(queryWrapper, name, rule, converRuleValue(dataRule.getRuleValue()));
			}else if (propertyType.equals(Date.class)) {
				String dateStr =converRuleValue(dataRule.getRuleValue());
				if(dateStr.length()==10){
					addEasyQuery(queryWrapper, name, rule, DateUtils.str2Date(dateStr,DateUtils.date_sdf.get()));
				}else{
					addEasyQuery(queryWrapper, name, rule, DateUtils.str2Date(dateStr,DateUtils.datetimeFormat.get()));
				}
			}else {
				addEasyQuery(queryWrapper, name, rule, NumberUtils.parseNumber(dataRule.getRuleValue(), propertyType));
			}
		}
	}

	private static void addQueryByRule(QueryWrapper<?> queryWrapper,String name,String type,String value,QueryRuleEnum rule) throws ParseException {
		if(oConvertUtils.isNotNull(value)) {
			Object temp;
			// 针对数字类型字段，多值查询
			if(value.contains(COMMA)){
				temp = value;
				addEasyQuery(queryWrapper, name, rule, temp);
				return;
			}

			switch (type) {
				case "class java.lang.Integer":
					temp =  Integer.parseInt(value);
					break;
				case "class java.math.BigDecimal":
					temp =  new BigDecimal(value);
					break;
				case "class java.lang.Short":
					temp =  Short.parseShort(value);
					break;
				case "class java.lang.Long":
					temp =  Long.parseLong(value);
					break;
				case "class java.lang.Float":
					temp =   Float.parseFloat(value);
					break;
				case "class java.lang.Double":
					temp =  Double.parseDouble(value);
					break;
				case "class java.util.Date":
					temp = getDateQueryByRule(value, rule);
					break;
				default:
					temp = value;
					break;
			}
			addEasyQuery(queryWrapper, name, rule, temp);
		}
	}

	/**
	 * json排序
	 * @param queryWrapper
	 * @param object
	 */
	public  static void doMultiFieldsOrder(QueryWrapper<?> queryWrapper,Object object){
		if(object==null) return;
		try {
			Field field=object.getClass().getDeclaredField(ORDER_COLUMN);
			field.setAccessible(true);
			String column = (String) field.get(object);
			if(!StringUtils.hasLength(column)) return;
			String order = ORDER_TYPE_ASC;
			try {
				Field forderType=object.getClass().getDeclaredField(ORDER_TYPE);
				forderType.setAccessible(true);
				String o= (String) forderType.get(object);
				if(StringUtils.hasLength(o)) order=o;
			}catch (Exception e2){}

			SqlInjectionUtil.filterContent(column);
			if (order.toUpperCase().contains(ORDER_TYPE_ASC)) {
				queryWrapper.orderByAsc(oConvertUtils.camelToUnderline(column));
			} else {
				queryWrapper.orderByDesc(oConvertUtils.camelToUnderline(column));
			}
		}catch (Exception e){
//			log.error("query order error",e);

		}

		}

		//多字段排序 TODO 需要修改前端
		public static void doMultiFieldsOrder(QueryWrapper<?> queryWrapper,Map<String, String[]> parameterMap) {
			String column=null,order=null;
			if(parameterMap!=null&& parameterMap.containsKey(ORDER_COLUMN)) {
				column = parameterMap.get(ORDER_COLUMN)[0];
			}
			if(parameterMap!=null&& parameterMap.containsKey(ORDER_TYPE)) {
				order = parameterMap.get(ORDER_TYPE)[0];
			}
			log.debug("排序规则>>列:"+column+",排序方式:"+order);
			if (oConvertUtils.isNotNull(column) && oConvertUtils.isNotNull(order)) {
				//字典字段，去掉字典翻译文本后缀
				if(column.endsWith(CommonConstant.DICT_TEXT_SUFFIX)) {
					column = column.substring(0, column.lastIndexOf(CommonConstant.DICT_TEXT_SUFFIX));
				}
				//SQL注入check
				SqlInjectionUtil.filterContent(column);

				if (order.toUpperCase().contains(ORDER_TYPE_ASC)) {
					queryWrapper.orderByAsc(oConvertUtils.camelToUnderline(column));
				} else {
					queryWrapper.orderByDesc(oConvertUtils.camelToUnderline(column));
				}
			}
		}

		/**
		 * 高级查询
		 * @param queryWrapper
		 * @param parameterMap
		 */
		public static void doSuperQuery(QueryWrapper<?> queryWrapper,Map<String, String[]> parameterMap) {
			if(parameterMap!=null&& parameterMap.containsKey(SUPER_QUERY_PARAMS)){
				String superQueryParams = parameterMap.get(SUPER_QUERY_PARAMS)[0];
				String superQueryMatchType = parameterMap.get(SUPER_QUERY_MATCH_TYPE) != null ? parameterMap.get(SUPER_QUERY_MATCH_TYPE)[0] : MatchTypeEnum.AND.getValue();
            MatchTypeEnum matchType = MatchTypeEnum.getByValue(superQueryMatchType);
            // update-begin--Author:sunjianlei  Date:20200325 for：高级查询的条件要用括号括起来，防止和用户的其他条件冲突 -------
            try {
                superQueryParams = URLDecoder.decode(superQueryParams, "UTF-8");
                List<QueryCondition> conditions = JSON.parseArray(superQueryParams, QueryCondition.class);
                if (conditions == null || conditions.size() == 0) {
                    return;
                }
                log.info("---高级查询参数-->" + conditions.toString());
                queryWrapper.and(andWrapper -> {
                    for (int i = 0; i < conditions.size(); i++) {
                        QueryCondition rule = conditions.get(i);
                        if (oConvertUtils.isNotNull(rule.getField())
                                && oConvertUtils.isNotNull(rule.getRule())
                                && oConvertUtils.isNotNull(rule.getVal())) {

                            log.debug("SuperQuery ==> " + rule.toString());
                            addEasyQuery(andWrapper, rule.getField(), QueryRuleEnum.getByValue(rule.getRule()), rule.getVal());

                            // 如果拼接方式是OR，就拼接OR
                            if (MatchTypeEnum.OR == matchType && i < (conditions.size() - 1)) {
                                andWrapper.or();
                            }
                        }
                    }
                    //return andWrapper;
                });
            } catch (UnsupportedEncodingException e) {
                log.error("--高级查询参数转码失败：" + superQueryParams, e);
            } catch (Exception e) {
                log.error("--高级查询拼接失败：" + e.getMessage());
                e.printStackTrace();
            }
            // update-end--Author:sunjianlei  Date:20200325 for：高级查询的条件要用括号括起来，防止和用户的其他条件冲突 -------
			}
			//log.info(" superQuery getCustomSqlSegment: "+ queryWrapper.getCustomSqlSegment());
		}

		/**
		 *
		 * @param name
		 * @return
		 */
		private static boolean judgedIsUselessField(String name) {
			return "class".equals(name) || "ids".equals(name)
					|| "page".equals(name) || "rows".equals(name)
					|| "sort".equals(name) || "order".equals(name);
		}

		/**
		 * 根据规则走不同的查询
		 * @param queryWrapper QueryWrapper
		 * @param name         字段名字
		 * @param rule         查询规则
		 * @param value        查询条件值
		 */
		private static void addEasyQuery(QueryWrapper<?> queryWrapper, String name, QueryRuleEnum rule, Object value) {
			if (value == null || rule == null || oConvertUtils.isEmpty(value)) {
				return;
			}
			name = oConvertUtils.camelToUnderline(name);
			log.info("--查询规则-->"+name+" "+rule.getValue()+" "+value);
			switch (rule) {
				case GT:
					queryWrapper.gt(name, value);
					break;
				case GE:
					queryWrapper.ge(name, value);
					break;
				case LT:
					queryWrapper.lt(name, value);
					break;
				case LE:
					queryWrapper.le(name, value);
					break;
				case EQ:
					queryWrapper.eq(name, value);
					break;
				case NE:
					queryWrapper.ne(name, value);
					break;
				case IN:
					if(value instanceof String) {
						queryWrapper.in(name, (Object[])value.toString().split(","));
					}else if(value instanceof String[]) {
						queryWrapper.in(name, (Object[]) value);
					}
					//update-begin-author:taoyan date:20200909 for:【bug】in 类型多值查询 不适配postgresql #1671
					else if(value.getClass().isArray()) {
						queryWrapper.in(name, (Object[])value);
					}else {
						queryWrapper.in(name, value);
					}
					//update-end-author:taoyan date:20200909 for:【bug】in 类型多值查询 不适配postgresql #1671
					break;
				case LIKE:
					queryWrapper.like(name, value);
					break;
				case LEFT_LIKE:
					queryWrapper.likeLeft(name, value);
					break;
				case RIGHT_LIKE:
					queryWrapper.likeRight(name, value);
					break;
				default:
					log.info("--查询规则未匹配到---");
					break;
			}
		}

		/**
		 * 根据所传的值 转化成对应的比较方式
		 * 支持><= like in !
		 * @param value
		 * @return
		 */
		private static QueryRuleEnum convert2Rule(Object value) {
			// 避免空数据
			if (value == null) {
				return null;
			}
			String val = (value + "").trim();
			if (val.length() == 0) {
				return null;
			}
			QueryRuleEnum rule =null;

			//update-begin--Author:scott  Date:20190724 for：initQueryWrapper组装sql查询条件错误 #284-------------------
			//TODO 此处规则，只适用于 le lt ge gt
			// step 2 .>= =<
			if (rule == null && val.length() >= 3) {
				if(QUERY_SEPARATE_KEYWORD.equals(val.substring(2, 3))){
					rule = QueryRuleEnum.getByValue(val.substring(0, 2));
				}
			}
			// step 1 .> <
			if (rule == null && val.length() >= 2) {
				if(QUERY_SEPARATE_KEYWORD.equals(val.substring(1, 2))){
					rule = QueryRuleEnum.getByValue(val.substring(0, 1));
				}
			}
			//update-end--Author:scott  Date:20190724 for：initQueryWrapper组装sql查询条件错误 #284---------------------

			// step 3 like
			if (rule == null && val.contains(STAR)) {
				if (val.startsWith(STAR) && val.endsWith(STAR)) {
					rule = QueryRuleEnum.LIKE;
				} else if (val.startsWith(STAR)) {
					rule = QueryRuleEnum.LEFT_LIKE;
				} else if(val.endsWith(STAR)){
					rule = QueryRuleEnum.RIGHT_LIKE;
				}
			}
			// step 4 in
			if (rule == null && val.contains(COMMA)) {
				//TODO in 查询这里应该有个bug  如果一字段本身就是多选 此时用in查询 未必能查询出来
				rule = QueryRuleEnum.IN;
			}
			// step 5 !=
			if(rule == null && val.startsWith(NOT_EQUAL)){
				rule = QueryRuleEnum.NE;
			}
			return rule != null ? rule : QueryRuleEnum.EQ;
		}

		/**
		 * 替换掉关键字字符
		 *
		 * @param rule
		 * @param value
		 * @return
		 */
		private static Object replaceValue(QueryRuleEnum rule, Object value) {
			if (rule == null) {
				return null;
			}
			if (! (value instanceof String)){
				return value;
			}
			String val = (value + "").toString().trim();
			if (rule == QueryRuleEnum.LIKE) {
				value = val.substring(1, val.length() - 1);
			} else if (rule == QueryRuleEnum.LEFT_LIKE || rule == QueryRuleEnum.NE) {
				value = val.substring(1);
			} else if (rule == QueryRuleEnum.RIGHT_LIKE) {
				value = val.substring(0, val.length() - 1);
			} else if (rule == QueryRuleEnum.IN) {
				value = val.split(",");
			} else {
				//update-begin--Author:scott  Date:20190724 for：initQueryWrapper组装sql查询条件错误 #284-------------------
				if(val.startsWith(rule.getValue())){
					//TODO 此处逻辑应该注释掉-> 如果查询内容中带有查询匹配规则符号，就会被截取的（比如：>=您好）
					value = val.replaceFirst(rule.getValue(),"");
				}else if(val.startsWith(rule.getCondition()+QUERY_SEPARATE_KEYWORD)){
					value = val.replaceFirst(rule.getCondition()+QUERY_SEPARATE_KEYWORD,"").trim();
				}
				//update-end--Author:scott  Date:20190724 for：initQueryWrapper组装sql查询条件错误 #284-------------------
			}
			return value;
		}

		/**
		 * 待实现
		 * 获取登录用户的数据，比如userid，部门id等
		 * @return
		 */
		public static String getUserInfoValue(String ruleValue,Object user) {
			LoginUserBasicDto loginUser=PermissionDataRuleUtils.getLoginUser();
			if(loginUser!=null) {
				String filedName=null;
				if(ruleValue.contains("#")) {
					filedName=ruleValue.split("\\}")[0].split("\\{")[1];
				}else {
					filedName = ruleValue;
				}
				try {
					Field filed=loginUser.getClass().getDeclaredField(filedName);
					filed.setAccessible(true);
					String value=filed.get(loginUser)+"";
					if(ruleValue.contains("#")) {
						value =  ruleValue.replace("#{"+filedName+"}", value);
						return value;
					}else {
						return value;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			return null;
		}


//	@SuppressWarnings("rawtypes")
//	private static SFunction toSFunction(Class clazz) {
//		SFunction func = null;
//		final MethodHandles.Lookup lookup = MethodHandles.lookup();
//		
//        MethodType methodType = MethodType.methodType(Integer.class, clazz);
//        final CallSite site;
//        try {
//            //方法名叫做:getSecretLevel  转换为 SFunction function interface对象
//            site = LambdaMetafactory.altMetafactory(lookup,
//                    "invoke",
//                    MethodType.methodType(SFunction.class),
//                    methodType,
//                    lookup.findVirtual(clazz, "getSecretLevel", MethodType.methodType(Integer.class)),
//                    methodType,FLAG_SERIALIZABLE);
//            func = (SFunction) site.getTarget().invokeExact();
//            //数据小于这个级别的都查出来
//        } catch (Throwable e) {
//            log.error("获取getSecretLevel方法错误",e);
//            throw e;
//        }
//	}
		/**
		 * 获取日期类型的值
		 * @param value
		 * @param rule
		 * @return
		 * @throws ParseException
		 */
		private static Date getDateQueryByRule(String value,QueryRuleEnum rule) throws ParseException {
			Date date = null;
			if(value.length()==10) {
				if(rule==QueryRuleEnum.GE) {
					//比较大于
					date = getTime().parse(value + " 00:00:00");
				}else if(rule==QueryRuleEnum.LE) {
					//比较小于
					date = getTime().parse(value + " 23:59:59");
				}
				//TODO 日期类型比较特殊 可能oracle下不一定好使
			}
			if(date==null) {
				date = getTime().parse(value);
			}
			return date;
		}

		/**时间格式化 */
		private static final ThreadLocal<SimpleDateFormat> local = new ThreadLocal<SimpleDateFormat>();

		private static SimpleDateFormat getTime(){
			SimpleDateFormat time = local.get();
			if(time == null){
				time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				local.set(time);
			}
			return time;
		}

	}
