package com.juyou.common.pk;


import com.juyou.common.base.service.BaseService;
import com.juyou.common.constant.CommonConstant;
import com.juyou.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 主键工具类
 *
 * @author 27174
 */
@Component
@Slf4j
public class PkUtils {


    @Autowired
    RedisUtil redisUtils;

    @Autowired
    BaseService baseService;

    static ConcurrentHashMap<String, Integer> pks = new ConcurrentHashMap<String, Integer>();

    static SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");

    static Thread thread = null;

    static Object lock = new Object();

    /**
     * 通过模块编码获取一个唯一主键
     *
     * @param moduleCode 模块编码
     * @return
     */
    public String createOne(String moduleCode) {
        return createOne(moduleCode, 4, true, 6,2);
    }



    public String createOne(String moduleCode, int codebuwei, boolean hasDate, int pkbuwei) {
        return createOne(moduleCode, codebuwei, hasDate, pkbuwei, 2);
    }
    /**
     * 生成主键
     *
     * @param moduleCode 业务码
     * @param codebuwei  业务码补位数
     * @param hasDate    是否含日期结构
     * @param pkbuwei    自增数值补位数
     * @return
     */
    public String createOne(String moduleCode, int codebuwei, boolean hasDate, int pkbuwei, int suiji) {
        StringBuffer sb = new StringBuffer();
        if (hasDate) {
            String date = sdf.format(new Date());
            sb.append(date);
        }
//        String macCode = macService.selfMac().getMacSeq();
       //直接补位，不适用传输的
        //直接补位，不使用传输的
        moduleCode=complement(moduleCode, 4);
        sb.append(moduleCode);
        long increasingVal = redisKey(sb.toString());
        String increasing = complement(increasingVal + "", pkbuwei);
        sb.append(increasing);
        if (suiji>0){
            String random = getRandom(suiji);
            return sb.toString()+random;
        }
        return sb.toString();
    }

    public List<String> create(String moduleCode, int codebuwei, boolean hasDate, int pkbuwei, int pknum) {
        return create(moduleCode, codebuwei, hasDate, pkbuwei, pknum, 2);
    }

    /**
     * 生成主键
     *
     * @param moduleCode 业务码
     * @param codebuwei  业务码补位数
     * @param hasDate    是否含日期结构
     * @param pkbuwei    自增数值补位数
     * @param pknum      生成主键数
     * @return
     */
    public List<String> create(String moduleCode, int codebuwei, boolean hasDate, int pkbuwei, int pknum, int suij) {
        StringBuffer sb = new StringBuffer();
        if (hasDate) {
            String date = sdf.format(new Date());
            sb.append(date);
        }
//        String macCode = macService.selfMac().getMacSeq();

        //直接补位，不使用传输的
        moduleCode=complement(moduleCode, 4);

        sb.append(moduleCode);
        //主键最后值
        String key = sb.toString();
        long increasingVal = redisKey(key, pknum);
        List<String> pks = new ArrayList<String>();
        for (int i = 0; i < pknum; i++) {
            long pk = increasingVal - i;
            String pkstr = complement(pk + "", pkbuwei);
            if (suij>0){
				String random = getRandom(suij);
				pkstr=pkstr+random;
			}
            pks.add(key + pkstr);
        }
        return pks;
    }

    /**
     * 补位，string.format效率低
     */
    public static String complement(String val, int len) {
        int l = 0;
        if (!StringUtils.isEmpty(val))
            l = val.length();
        else if (val.length() >= len)
            return val;

        StringBuffer buf = new StringBuffer();
        for (int i = l; i < len; i++) {
            buf.append("0");
        }
        if (StringUtils.hasLength(val))
            buf.append(val);
        return buf.toString();
    }

    /**
     * 从redis中获取数据
     *
     * @param key
     * @return
     */
    private long redisKey(String key) {
        String cacheKey = CommonConstant.SYS_CACHE_PK + "::" + key;
        if (!redisUtils.hasKey(cacheKey)) {
            Integer val = baseService.selectPkByType(key);
            if (val != null) {
                redisUtils.set(cacheKey, val, 86400L);
            }
        }
        Long val = redisUtils.incr(cacheKey, 1);
        addDb(key, val.intValue());
        return val;
    }

    /**
     * 从redis中获取多个主键的最后数值
     *
     * @param key
     * @param pkNum
     * @return
     */
    private long redisKey(String key, int pkNum) {
        String cacheKey = CommonConstant.SYS_CACHE_PK + "::" + key;
        if (!redisUtils.hasKey(cacheKey)) {
            Integer val = baseService.selectPkByType(key);
            if (val != null) {
                redisUtils.set(cacheKey, val, 86400L);
            }
        }
        Long val = redisUtils.incr(cacheKey, pkNum);
        addDb(key, val.intValue());
        return val;
    }

    private void addDb(String key, int val) {
        init();
        pks.put(key, val);
        synchronized (lock) {
            lock.notify();
        }
    }

    private void init() {
        if (thread == null) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            if (!pks.isEmpty()) {
                                Iterator<Entry<String, Integer>> iterator = pks.entrySet().iterator();
                                while (iterator.hasNext()) {
                                    Entry<String, Integer> entry = iterator.next();
                                    String key = entry.getKey();
                                    Integer val = entry.getValue();
                                    if(key.indexOf("-")>0) {
                                    	baseService.updatePkNum(key.split("-")[0], val);
                                    }
                                    
                                    pks.remove(key);
                                }
                            }
                            if (pks.isEmpty()) {
                                synchronized (lock) {
                                    lock.wait();
                                }
                            }
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            log.error("up pk error", e);
                        }
                    }
                }
            });
            thread.setName("pk-update-task");
            thread.start();
        }
    }


	private String getRandom(int len) {
		Random r = new Random();
		StringBuilder rs = new StringBuilder();
		for (int i = 0; i < len; i++) {
			rs.append(r.nextInt(10));
		}
		return rs.toString();
	}


//	public static void main(String[] args) {
////		String date = sdf.format(new Date());
////		System.out.println(date);
//	}
    }
