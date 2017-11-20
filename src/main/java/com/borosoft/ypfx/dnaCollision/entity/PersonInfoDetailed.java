package com.borosoft.ypfx.dnaCollision.entity;

import java.util.Date;

import com.borosoft.middleware.dirEntry.DirDataEntity;

/**
 * 人员详细信息子表
 * 
 * @author yepeicong
 *
 */
public class PersonInfoDetailed extends DirDataEntity {

	private static final long serialVersionUID = -811073792665782425L;
	
	public static String tableName= "YW_DCD_DGSGAJ_RYXXXX";
	
	public static String configCode = "YW_DCD_DGSGAJ_RYXXXX_LIST";

	// 主表数据编号
	private String dcd_zbsjbh;

	// 数据类型
	private String dcd_sjlx;

	// 数据值
	private String dcd_sjz;

	// 数据来源
	private String dcd_sjly;

	// 案件编号
	private String dcd_ajbh;

	// 新增时间
	private Date dcd_xzsj;

	// 唯一编号(主表数据编号 + 数据类型 + 数据值 + 案件编号)
	private String dcd_wybh;
	
	//人员编号
	private String dcd_rybh;

	public String getDcd_zbsjbh() {
		return dcd_zbsjbh;
	}

	public void setDcd_zbsjbh(String dcd_zbsjbh) {
		this.dcd_zbsjbh = dcd_zbsjbh;
	}

	public String getDcd_sjlx() {
		return dcd_sjlx;
	}

	public void setDcd_sjlx(String dcd_sjlx) {
		this.dcd_sjlx = dcd_sjlx;
	}

	public String getDcd_sjz() {
		return dcd_sjz;
	}

	public void setDcd_sjz(String dcd_sjz) {
		this.dcd_sjz = dcd_sjz;
	}

	public String getDcd_sjly() {
		return dcd_sjly;
	}

	public void setDcd_sjly(String dcd_sjly) {
		this.dcd_sjly = dcd_sjly;
	}

	public String getDcd_ajbh() {
		return dcd_ajbh;
	}

	public void setDcd_ajbh(String dcd_ajbh) {
		this.dcd_ajbh = dcd_ajbh;
	}

	public Date getDcd_xzsj() {
		return dcd_xzsj;
	}

	public void setDcd_xzsj(Date dcd_xzsj) {
		this.dcd_xzsj = dcd_xzsj;
	}

	public String getDcd_wybh() {
		return dcd_wybh;
	}

	public void setDcd_wybh(String dcd_wybh) {
		this.dcd_wybh = dcd_wybh;
	}

	public String getDcd_rybh() {
		return dcd_rybh;
	}

	public void setDcd_rybh(String dcd_rybh) {
		this.dcd_rybh = dcd_rybh;
	}

	
	
	

}
