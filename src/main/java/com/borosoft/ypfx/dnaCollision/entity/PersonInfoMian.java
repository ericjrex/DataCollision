package com.borosoft.ypfx.dnaCollision.entity;

import com.borosoft.middleware.dirEntry.DirDataEntity;

/**
 * 人员信息主表
 * 
 * @author yepeicong
 *
 */
public class PersonInfoMian extends DirDataEntity {

	private static final long serialVersionUID = -6395037152561087582L;
	
	public static String tableName = "YW_DCD_DGSGAJ_RYXXZB";

	public static String configCode = "YW_DCD_DGSGAJ_RYXXZB_LIST";
	
	// 数据编号  生成规则：MD5（身份证号+姓名+案件编号）
	private String dcd_sjbh ;

	// 姓名
	private String dcd_xm ;

	// 身份证
	private String dcd_sfz;
	
	//案件编号
	private String xzd_ajbh;

	public String getDcd_sjbh() {
		return dcd_sjbh;
	}

	public void setDcd_sjbh(String dcd_sjbh) {
		this.dcd_sjbh = dcd_sjbh;
	}

	public String getDcd_xm() {
		return dcd_xm;
	}

	public void setDcd_xm(String dcd_xm) {
		this.dcd_xm = dcd_xm;
	}

	public String getDcd_sfz() {
		return dcd_sfz;
	}

	public void setDcd_sfz(String dcd_sfz) {
		this.dcd_sfz = dcd_sfz;
	}

	public String getXzd_ajbh() {
		return xzd_ajbh;
	}

	public void setXzd_ajbh(String xzd_ajbh) {
		this.xzd_ajbh = xzd_ajbh;
	}

	

}
