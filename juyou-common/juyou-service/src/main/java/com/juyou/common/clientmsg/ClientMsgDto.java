package com.juyou.common.clientmsg;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

/**
 * 异步给浏览器返回的消息对象,主要是为了长时间任务显示提示
 * @author 27174
 *
 */
@Data
@Schema(title = "前端异步消息对象")
public class ClientMsgDto {

	/*1~*/@Schema(title ="0-成功，1-普通消息，2-错误消息,3-不重要的，10完成")
	int type;
	
	/*1~*/@Schema(title ="具体消息类容")
	String content;

	/*1~*/@Schema(title ="消息得添加时间，后面根据此时间清除数据")
	long time=0;
}
