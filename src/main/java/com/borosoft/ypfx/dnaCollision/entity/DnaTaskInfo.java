package com.borosoft.ypfx.dnaCollision.entity;

import java.util.Date;

import com.borosoft.middleware.dirEntry.DirDataEntity;

/**
 * DNA碰撞任务实体
 * @author yepeicong
 *
 */
public class DnaTaskInfo extends DirDataEntity {

	private static final long serialVersionUID = 2595850175369539649L;
	
	public static String tableName =  "YW_DCD_DGSGAJ_DNAPZR";
	
	public static String configCode = "YW_DCD_DGSGAJ_DNAPZR";

	// 任务编号 生成规则 ：MD5（姓名+案件编号）
	private String dcd_rwbh;

	// 任务状态
	private String dcd_rwzt;

	// 任务开始时间
	private Date dcd_rwkssj;

	// 任务执行时间
	private Date dcd_rwzxsj;

	// DNAID
	private String dcd_dnaid;

	//案件编号
	private String dcd_ajbh;

	// 人员编号
	private String dcd_rysjbh;

	public String getDcd_rwbh() {
		return dcd_rwbh;
	}

	public void setDcd_rwbh(String dcd_rwbh) {
		this.dcd_rwbh = dcd_rwbh;
	}

	public String getDcd_rwzt() {
		return dcd_rwzt;
	}

	public void setDcd_rwzt(String dcd_rwzt) {
		this.dcd_rwzt = dcd_rwzt;
	}

	public Date getDcd_rwkssj() {
		return dcd_rwkssj;
	}

	public void setDcd_rwkssj(Date dcd_rwkssj) {
		this.dcd_rwkssj = dcd_rwkssj;
	}

	public Date getDcd_rwzxsj() {
		return dcd_rwzxsj;
	}

	public void setDcd_rwzxsj(Date dcd_rwzxsj) {
		this.dcd_rwzxsj = dcd_rwzxsj;
	}

	public String getDcd_dnaid() {
		return dcd_dnaid;
	}

	public void setDcd_dnaid(String dcd_dnaid) {
		this.dcd_dnaid = dcd_dnaid;
	}

	public String getDcd_ajbh() {
		return dcd_ajbh;
	}

	public void setDcd_ajbh(String dcd_ajbh) {
		this.dcd_ajbh = dcd_ajbh;
	}

	public String getDcd_rysjbh() {
		return dcd_rysjbh;
	}

	public void setDcd_rysjbh(String dcd_rysjbh) {
		this.dcd_rysjbh = dcd_rysjbh;
	}
	
	
	

}
