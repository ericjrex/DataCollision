package com.borosoft.ypfx.dnaCollision.entity;

import java.util.Date;

import com.borosoft.middleware.dirEntry.DirDataEntity;

/**
 * 碰撞人员信息
 * 
 * @author yepeicong
 *
 */
public class PersonCollisonInfo extends DirDataEntity {

	private static final long serialVersionUID = 4166065005051225088L;
	
	public static String tableName = "YW_DCD_DGSGAJ_PZRYXX";
	
	public static String configCode = "YW_DCD_DGSGAJ_PZRYXX_LIST";

	// 姓名
	private String dcd_xm;

	// 身份证号
	private String dcd_sfzh;

	// 手机号
	private String dcd_sjh;

	// QQ
	private String dcd_qq;

	// 微信
	private String dcd_wx;

	//IMEI
	private String dcd_imei;

	// IMSI
	private String dcd_imsi;

	// MAC
	private String dcd_mac;
	
	// 京东
	private String dcd_jd;

	// 淘宝
	private String dcd_tb;

	// 经度
	private String dcd_jd0;

	// 纬度
	private String dcd_wd;
	
	// 地址
	private String dcd_dz;

	// 案件编号
	private String dcd_ajbh;
	
	// 数据状态
	private String dcd_sjzt;
	
	//碰撞编号(主任务编号)
	private String dcd_pzbh;
	
	//受理单位
	private String dcd_sldw;
	
	//受理单位编号
	private String dcd_sldwbh;
	
	//处理描述
	private String dcd_clms;

	//下发时间
	private Date dcd_xfsj;
	
	//颜色标志
	private String dcd_ysbz;
	
	//离期限剩余天数
	private Integer dcd_lqxsyts;
	
	//最后捕获时间
	private Date dcd_zhbhsj;
	
	//分配时间
	private Date dcd_fpsj;
	
	//处理状态编号
	private String dcd_clztbh;
	
	//车牌号
	private String xzd_cph;
	
	public String getDcd_xm() {
		return dcd_xm;
	}

	public void setDcd_xm(String dcd_xm) {
		this.dcd_xm = dcd_xm;
	}

	public String getDcd_sfzh() {
		return dcd_sfzh;
	}

	public void setDcd_sfzh(String dcd_sfzh) {
		this.dcd_sfzh = dcd_sfzh;
	}

	public String getDcd_sjh() {
		return dcd_sjh;
	}

	public void setDcd_sjh(String dcd_sjh) {
		this.dcd_sjh = dcd_sjh;
	}

	public String getDcd_qq() {
		return dcd_qq;
	}

	public void setDcd_qq(String dcd_qq) {
		this.dcd_qq = dcd_qq;
	}

	public String getDcd_wx() {
		return dcd_wx;
	}

	public void setDcd_wx(String dcd_wx) {
		this.dcd_wx = dcd_wx;
	}

	public String getDcd_imei() {
		return dcd_imei;
	}

	public void setDcd_imei(String dcd_imei) {
		this.dcd_imei = dcd_imei;
	}

	public String getDcd_imsi() {
		return dcd_imsi;
	}

	public void setDcd_imsi(String dcd_imsi) {
		this.dcd_imsi = dcd_imsi;
	}

	public String getDcd_mac() {
		return dcd_mac;
	}

	public void setDcd_mac(String dcd_mac) {
		this.dcd_mac = dcd_mac;
	}

	public String getDcd_dz() {
		return dcd_dz;
	}

	public void setDcd_dz(String dcd_dz) {
		this.dcd_dz = dcd_dz;
	}

	public String getDcd_ajbh() {
		return dcd_ajbh;
	}

	public void setDcd_ajbh(String dcd_ajbh) {
		this.dcd_ajbh = dcd_ajbh;
	}

	public String getDcd_sjzt() {
		return dcd_sjzt;
	}

	public void setDcd_sjzt(String dcd_sjzt) {
		this.dcd_sjzt = dcd_sjzt;
	}

	public String getDcd_pzbh() {
		return dcd_pzbh;
	}

	public void setDcd_pzbh(String dcd_pzbh) {
		this.dcd_pzbh = dcd_pzbh;
	}

	public String getDcd_sldw() {
		return dcd_sldw;
	}

	public void setDcd_sldw(String dcd_sldw) {
		this.dcd_sldw = dcd_sldw;
	}

	public String getDcd_sldwbh() {
		return dcd_sldwbh;
	}

	public void setDcd_sldwbh(String dcd_sldwbh) {
		this.dcd_sldwbh = dcd_sldwbh;
	}

	public String getDcd_clms() {
		return dcd_clms;
	}

	public void setDcd_clms(String dcd_clms) {
		this.dcd_clms = dcd_clms;
	}

	public Date getDcd_xfsj() {
		return dcd_xfsj;
	}

	public void setDcd_xfsj(Date dcd_xfsj) {
		this.dcd_xfsj = dcd_xfsj;
	}

	public String getDcd_ysbz() {
		return dcd_ysbz;
	}

	public void setDcd_ysbz(String dcd_ysbz) {
		this.dcd_ysbz = dcd_ysbz;
	}

	public Integer getDcd_lqxsyts() {
		return dcd_lqxsyts;
	}

	public void setDcd_lqxsyts(Integer dcd_lqxsyts) {
		this.dcd_lqxsyts = dcd_lqxsyts;
	}

	public String getDcd_jd() {
		return dcd_jd;
	}

	public void setDcd_jd(String dcd_jd) {
		this.dcd_jd = dcd_jd;
	}

	public String getDcd_tb() {
		return dcd_tb;
	}

	public void setDcd_tb(String dcd_tb) {
		this.dcd_tb = dcd_tb;
	}

	public String getDcd_jd0() {
		return dcd_jd0;
	}

	public void setDcd_jd0(String dcd_jd0) {
		this.dcd_jd0 = dcd_jd0;
	}

	public String getDcd_wd() {
		return dcd_wd;
	}

	public void setDcd_wd(String dcd_wd) {
		this.dcd_wd = dcd_wd;
	}

	public Date getDcd_zhbhsj() {
		return dcd_zhbhsj;
	}

	public void setDcd_zhbhsj(Date dcd_zhbhsj) {
		this.dcd_zhbhsj = dcd_zhbhsj;
	}

	public Date getDcd_fpsj() {
		return dcd_fpsj;
	}

	public void setDcd_fpsj(Date dcd_fpsj) {
		this.dcd_fpsj = dcd_fpsj;
	}

	public String getDcd_clztbh() {
		return dcd_clztbh;
	}

	public void setDcd_clztbh(String dcd_clztbh) {
		this.dcd_clztbh = dcd_clztbh;
	}

	public String getXzd_cph() {
		return xzd_cph;
	}

	public void setXzd_cph(String xzd_cph) {
		this.xzd_cph = xzd_cph;
	}

	

}
