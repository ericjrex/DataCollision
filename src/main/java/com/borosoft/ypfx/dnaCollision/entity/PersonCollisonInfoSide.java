package com.borosoft.ypfx.dnaCollision.entity;

import java.util.Date;

import com.borosoft.middleware.dirEntry.DirDataEntity;

/**
 * 人员信息副表
 * @author yepeicong
 *
 */
public class PersonCollisonInfoSide extends DirDataEntity{
	
	private static final long serialVersionUID = 5449605093757213376L;
	
	public static String tableName = "YW_XZD_DGSGAJ_RYXXFB";
	
	//案件编号
	public String xzd_ajbh;
	
	//姓名
	public String xzd_xm;
	
	//身份证
	public String xzd_sfz;
	
	//数据状态
	public String xzd_sjzt;
	
	//受理单位
	public String xzd_sldw;
	
	//受理单位名称
	public String xzd_sldwmc;
	
	//优先分配单位
	public String xzd_yxfpdw;
	
	//优先分配单位名称
	public String xzd_yxfpdwmc;
	
	//唯一编号
	public String xzd_wybh;
	
	//生成时间
	public Date xzd_scsj;
	
	//优先碰撞数据ID
	public String xzd_yxpzsjid;

	public String getXzd_ajbh() {
		return xzd_ajbh;
	}

	public void setXzd_ajbh(String xzd_ajbh) {
		this.xzd_ajbh = xzd_ajbh;
	}

	public String getXzd_xm() {
		return xzd_xm;
	}

	public void setXzd_xm(String xzd_xm) {
		this.xzd_xm = xzd_xm;
	}

	public String getXzd_sfz() {
		return xzd_sfz;
	}

	public void setXzd_sfz(String xzd_sfz) {
		this.xzd_sfz = xzd_sfz;
	}

	public String getXzd_sjzt() {
		return xzd_sjzt;
	}

	public void setXzd_sjzt(String xzd_sjzt) {
		this.xzd_sjzt = xzd_sjzt;
	}

	public String getXzd_sldw() {
		return xzd_sldw;
	}

	public void setXzd_sldw(String xzd_sldw) {
		this.xzd_sldw = xzd_sldw;
	}

	public String getXzd_sldwmc() {
		return xzd_sldwmc;
	}

	public void setXzd_sldwmc(String xzd_sldwmc) {
		this.xzd_sldwmc = xzd_sldwmc;
	}

	public String getXzd_yxfpdw() {
		return xzd_yxfpdw;
	}

	public void setXzd_yxfpdw(String xzd_yxfpdw) {
		this.xzd_yxfpdw = xzd_yxfpdw;
	}

	public String getXzd_yxfpdwmc() {
		return xzd_yxfpdwmc;
	}

	public void setXzd_yxfpdwmc(String xzd_yxfpdwmc) {
		this.xzd_yxfpdwmc = xzd_yxfpdwmc;
	}

	public String getXzd_wybh() {
		return xzd_wybh;
	}

	public void setXzd_wybh(String xzd_wybh) {
		this.xzd_wybh = xzd_wybh;
	}

	public Date getXzd_scsj() {
		return xzd_scsj;
	}

	public void setXzd_scsj(Date xzd_scsj) {
		this.xzd_scsj = xzd_scsj;
	}

	public String getXzd_yxpzsjid() {
		return xzd_yxpzsjid;
	}

	public void setXzd_yxpzsjid(String xzd_yxpzsjid) {
		this.xzd_yxpzsjid = xzd_yxpzsjid;
	}

}
