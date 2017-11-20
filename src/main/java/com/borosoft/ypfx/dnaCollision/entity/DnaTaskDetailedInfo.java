package com.borosoft.ypfx.dnaCollision.entity;

import java.util.Date;

import com.borosoft.middleware.dirEntry.DirDataEntity;

/**
 * 	DNA任务详细记录
 * @author yepeicong
 *
 */
public class DnaTaskDetailedInfo extends DirDataEntity{

	private static final long serialVersionUID = -969993451661233736L;
	
	public static String tableName= "YW_DCD_DGSGAJ_DNARWX";
	
	public static String configCode = "YW_DCD_DGSGAJ_DNARWX";
	
	//主任务id
	private String dcd_zrwid;
	
	//任务详细状态
	private String dcd_rwxxzt;
	
	//任务执行时间
	private Date dcd_rwzxsj;
	
	//唯一编号
	private String dcd_wybh;
	
	//变更内容
	private String dcd_bgnr;
	
	//操作人id
	private String dcd_czr;
	
	//操作人
	private String dcd_czrid;
	
	//碰撞编号
	private String dcd_pzbh;

	public String getDcd_zrwid() {
		return dcd_zrwid;
	}

	public void setDcd_zrwid(String dcd_zrwid) {
		this.dcd_zrwid = dcd_zrwid;
	}

	public String getDcd_rwxxzt() {
		return dcd_rwxxzt;
	}

	public void setDcd_rwxxzt(String dcd_rwxxzt) {
		this.dcd_rwxxzt = dcd_rwxxzt;
	}

	public Date getDcd_rwzxsj() {
		return dcd_rwzxsj;
	}

	public void setDcd_rwzxsj(Date dcd_rwzxsj) {
		this.dcd_rwzxsj = dcd_rwzxsj;
	}

	public String getDcd_wybh() {
		return dcd_wybh;
	}

	public void setDcd_wybh(String dcd_wybh) {
		this.dcd_wybh = dcd_wybh;
	}

	public String getDcd_bgnr() {
		return dcd_bgnr;
	}

	public void setDcd_bgnr(String dcd_bgnr) {
		this.dcd_bgnr = dcd_bgnr;
	}

	public String getDcd_czr() {
		return dcd_czr;
	}

	public void setDcd_czr(String dcd_czr) {
		this.dcd_czr = dcd_czr;
	}

	public String getDcd_czrid() {
		return dcd_czrid;
	}

	public void setDcd_czrid(String dcd_czrid) {
		this.dcd_czrid = dcd_czrid;
	}

	public String getDcd_pzbh() {
		return dcd_pzbh;
	}

	public void setDcd_pzbh(String dcd_pzbh) {
		this.dcd_pzbh = dcd_pzbh;
	}
	
	
	
	
}
