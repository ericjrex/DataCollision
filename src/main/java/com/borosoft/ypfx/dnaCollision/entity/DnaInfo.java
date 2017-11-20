package com.borosoft.ypfx.dnaCollision.entity;

import java.util.Date;

import com.borosoft.middleware.dirEntry.DirDataEntity;

/**
 * 刑技平台DNA
 * @author yepeicong
 *
 */
public class DnaInfo  extends DirDataEntity{

	private static final long serialVersionUID = -7551615584107128889L;
	
	public static String tableName = "HJWZ_DYBZ_4419";
	
	public static String configCode = "HJWZ_DYBZ_4419_LIST";
	
	//入库时间
	private Date rksj;
	
	//更新时间
	private Date gxsj;
	
	//比中类型（1：指纹；2：DNA）
	private String bzlx;
	
	//姓名
	private String xm;
	
	//身份证号
	private String sfzh;
	
	//户籍地
	private String hjd;

	//人员样本编号
	private String ryybbh;
	
	//警综人员编号
	private String jzrybh;
	
	//比中时间
	private Date bzsj;
	
	//比中单位代码
	private String bzdwdm;
	
	//比中单位名称
	private String bzdwmc;
	
	//物证编号
	private String wzbh;
		
	//物证案件编号
	private String wzajbh;
		
	//物证类型
	private String wzlx;
	
	//发案日期
	private String farq;

	// 发案地点
	private String fadd;

	// 简要案情
	private String jyaq;

	// 案件类别代码
	private String ajlbdm;

	// 案件类别中文
	private String ajlbmc;
	
	//提取部位
	private String tqbw;

	// 提取单位代码
	private String tqdwdm;

	// 提取单位名称
	private String tqdwmc;

	// 警综案件编号
	private String jzajbh;

	// 警综案件名称
	private String jzajmc;

	//警综案发日期
	private Date jzafrq;

	// 警综案发地点
	private String jzafdd;

	// 警综简要案情
	private String jzjyaq;
	
	// 警综办案单位代码
	private String jzbadwdm;

	// 警综办案单位名称
	private String jzbadwmc;

	// 警综案件类别代码
	private String jzajlbdm;

	// 警综案件类别中文
	private String jzajlbmc;

	// 警综案件状态
	private String jzajzt;

	// 主键ID
	private String dcd_zjid;

	public Date getRksj() {
		return rksj;
	}

	public void setRksj(Date rksj) {
		this.rksj = rksj;
	}

	public Date getGxsj() {
		return gxsj;
	}

	public void setGxsj(Date gxsj) {
		this.gxsj = gxsj;
	}

	public String getBzlx() {
		return bzlx;
	}

	public void setBzlx(String bzlx) {
		this.bzlx = bzlx;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getSfzh() {
		return sfzh;
	}

	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}

	public String getHjd() {
		return hjd;
	}

	public void setHjd(String hjd) {
		this.hjd = hjd;
	}

	public String getRyybbh() {
		return ryybbh;
	}

	public void setRyybbh(String ryybbh) {
		this.ryybbh = ryybbh;
	}

	public String getJzrybh() {
		return jzrybh;
	}

	public void setJzrybh(String jzrybh) {
		this.jzrybh = jzrybh;
	}

	public Date getBzsj() {
		return bzsj;
	}

	public void setBzsj(Date bzsj) {
		this.bzsj = bzsj;
	}

	public String getBzdwdm() {
		return bzdwdm;
	}

	public void setBzdwdm(String bzdwdm) {
		this.bzdwdm = bzdwdm;
	}

	public String getBzdwmc() {
		return bzdwmc;
	}

	public void setBzdwmc(String bzdwmc) {
		this.bzdwmc = bzdwmc;
	}

	public String getWzbh() {
		return wzbh;
	}

	public void setWzbh(String wzbh) {
		this.wzbh = wzbh;
	}

	public String getWzajbh() {
		return wzajbh;
	}

	public void setWzajbh(String wzajbh) {
		this.wzajbh = wzajbh;
	}

	public String getWzlx() {
		return wzlx;
	}

	public void setWzlx(String wzlx) {
		this.wzlx = wzlx;
	}

	public String getFarq() {
		return farq;
	}

	public void setFarq(String farq) {
		this.farq = farq;
	}

	public String getFadd() {
		return fadd;
	}

	public void setFadd(String fadd) {
		this.fadd = fadd;
	}

	public String getJyaq() {
		return jyaq;
	}

	public void setJyaq(String jyaq) {
		this.jyaq = jyaq;
	}

	public String getAjlbdm() {
		return ajlbdm;
	}

	public void setAjlbdm(String ajlbdm) {
		this.ajlbdm = ajlbdm;
	}

	public String getAjlbmc() {
		return ajlbmc;
	}

	public void setAjlbmc(String ajlbmc) {
		this.ajlbmc = ajlbmc;
	}

	public String getTqbw() {
		return tqbw;
	}

	public void setTqbw(String tqbw) {
		this.tqbw = tqbw;
	}

	public String getTqdwdm() {
		return tqdwdm;
	}

	public void setTqdwdm(String tqdwdm) {
		this.tqdwdm = tqdwdm;
	}

	public String getTqdwmc() {
		return tqdwmc;
	}

	public void setTqdwmc(String tqdwmc) {
		this.tqdwmc = tqdwmc;
	}

	public String getJzajbh() {
		return jzajbh;
	}

	public void setJzajbh(String jzajbh) {
		this.jzajbh = jzajbh;
	}

	public String getJzajmc() {
		return jzajmc;
	}

	public void setJzajmc(String jzajmc) {
		this.jzajmc = jzajmc;
	}

	public Date getJzafrq() {
		return jzafrq;
	}

	public void setJzafrq(Date jzafrq) {
		this.jzafrq = jzafrq;
	}

	public String getJzafdd() {
		return jzafdd;
	}

	public void setJzafdd(String jzafdd) {
		this.jzafdd = jzafdd;
	}

	public String getJzjyaq() {
		return jzjyaq;
	}

	public void setJzjyaq(String jzjyaq) {
		this.jzjyaq = jzjyaq;
	}

	public String getJzbadwdm() {
		return jzbadwdm;
	}

	public void setJzbadwdm(String jzbadwdm) {
		this.jzbadwdm = jzbadwdm;
	}

	public String getJzbadwmc() {
		return jzbadwmc;
	}

	public void setJzbadwmc(String jzbadwmc) {
		this.jzbadwmc = jzbadwmc;
	}

	public String getJzajlbdm() {
		return jzajlbdm;
	}

	public void setJzajlbdm(String jzajlbdm) {
		this.jzajlbdm = jzajlbdm;
	}

	public String getJzajlbmc() {
		return jzajlbmc;
	}

	public void setJzajlbmc(String jzajlbmc) {
		this.jzajlbmc = jzajlbmc;
	}

	public String getJzajzt() {
		return jzajzt;
	}

	public void setJzajzt(String jzajzt) {
		this.jzajzt = jzajzt;
	}

	public String getDcd_zjid() {
		return dcd_zjid;
	}

	public void setDcd_zjid(String dcd_zjid) {
		this.dcd_zjid = dcd_zjid;
	}
	
	


}
