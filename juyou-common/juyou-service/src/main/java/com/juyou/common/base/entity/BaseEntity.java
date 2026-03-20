package com.juyou.common.base.entity;

import java.util.Date;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 基类
 * @author 27174
 *
 */
@Data
public class BaseEntity implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*1~*/@Schema(title = "创建时间")
    private Date createTime;

    /*1~*/@Schema(title = "创建人")
    private String createName;

    /*1~*/@Schema(title = "最后修改时间")
    private Date lastupdateTime;

    /*1~*/@Schema(title = "最后修改人")
    private String lastupdateName;

//    /*1~*/@Schema(title = "数据归属机构编码")
//    private String ascriptionMacCode;

}
