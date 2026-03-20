package com.juyou.common.clientmsg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 前端消息
 * @author 27174
 *
 */
public class CmsgUtils {
	
    private static ConcurrentHashMap<String,List<ClientMsgDto>> creareMsgs=new ConcurrentHashMap<String,List<ClientMsgDto>>();

    private static ConcurrentHashMap<String,Lock> locks=new ConcurrentHashMap<String,Lock>();
	static Object lock=new Object();
	static Thread thread=null;
    /**
     * 获取锁，如果能获取到
     * @param macSeq
     * @return
     */
    public static  boolean tryLock(String macSeq,int type) {
    	String key=macSeq+"__"+type;
    	if(!locks.containsKey(key)) {
    		locks.put(key, new ReentrantLock());
    	}
    	return locks.get(key).tryLock();
    }
    
    /**
     * 释放锁
     * @param macSeq
     */
    public static  void unLock(String macSeq,int type) {
    	String key=macSeq+"__"+type;
    	if(locks.containsKey(key)) {
    		Lock lock=locks.remove(key);
    		lock.unlock();
    	}
    }
    
    /**
     * 获取消息
     * @param macSeq
     * @return
     */
    public static List<ClientMsgDto> getMsg(String macSeq,int type){
    	String key=macSeq+"__"+type;
    	List<ClientMsgDto> list=creareMsgs.get(key);
		if(list==null)
			return null;
		List<ClientMsgDto> items=new ArrayList<>();
		items.addAll(list);
		list.clear();
		return items;
    }
    
    /**
     * 添加消息
     * @param macSeq
     * @param type
     * @param content
     */
    public static void addMsg(String macSeq,int type,int msgType,String content) {
    	try {
			init();
    		String key=macSeq+"__"+type;
        	ClientMsgDto clientMsgDto=new ClientMsgDto();
        	clientMsgDto.setContent(content);
        	clientMsgDto.setType(msgType);
			clientMsgDto.setTime(System.currentTimeMillis());

//			System.out.println(JSONObject.toJSONString(clientMsgDto));
        	if(!creareMsgs.containsKey(key)) {
        		creareMsgs.put(key, new ArrayList<ClientMsgDto>());
        	}
        	creareMsgs.get(key).add(clientMsgDto);
		} catch (Exception e) {
			e.getMessage();
		}
    	
    }
    
    public static void clearMsg(String macSeq,int type) {
    	String key=macSeq+"__"+type;
    	creareMsgs.remove(key);
    }

	public static void init(){
		synchronized (lock){
			if(thread!=null) return;
			thread=new Thread(new Runnable() {
				@Override
				public void run() {
					while (true){
						try {
							List<String> keys=new ArrayList<>();
							Iterator<Map.Entry<String,List<ClientMsgDto>>> iterator = creareMsgs.entrySet().iterator();
							while (iterator.hasNext()){
								Map.Entry<String,List<ClientMsgDto>> entry=iterator.next();
								List<ClientMsgDto> list=entry.getValue();
								List<ClientMsgDto> removes=new ArrayList<>();
								for (ClientMsgDto c:list){
									//10分钟以前得消息直接删了
									if(c.getTime()+60000*10<System.currentTimeMillis()){
										removes.add(c);
									}
								}
								list.remove(removes);
								if(list.isEmpty()){
									keys.add(entry.getKey());
								}
							}
							creareMsgs.remove(keys);
						}catch (Exception e){

						}
						try {
							Thread.sleep(60000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				}
			});
			thread.setName("clear-client-msg");
			thread.start();
		}

	}
}
