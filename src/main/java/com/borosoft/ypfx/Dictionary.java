package com.borosoft.ypfx;

/**
 * 字典定义
 * 
 * @author Administrator
 *
 */
	
public class Dictionary {
		// ~~~~~~~~~~~~~~~~~~~~~卡口信息状态~~~~~~~~~~~~~~~~~~~~~~
		public final static String 卡口信息状态 = "007000028";

		public final static String 卡口信息状态_草稿 = "草稿";

		public final static String 卡口信息状态_已发布 = "已发布";

		public final static String 卡口信息状态_已撤销 = "已撤销";

		public final static String 卡口信息状态_已停用 = "已停用";
		
		// ~~~~~~~~~~~~~~~~~~~~~状态~~~~~~~~~~~~~~~~~~~~~~
		public final static String 状态 = "数据状态";
		
		public final static String 状态_草稿 = "0";
		
		public final static String 状态_启用 = "1";
		
		public final static String 状态_停用 = "2";
		
		
		// ~~~~~~~~~~~~~~~~~~~~~数据所属地~~~~~~~~~~~~~~~~~~~~~~
		public final static String 数据所属地 = "数据所属地";
		
		public final static String 数据所属地_本市 = "loacl";
		
		public final static String 数据所属地_非本市 = "notLocal";
		
		// ~~~~~~~~~~~~~~~~~~~~~比对任务状态~~~~~~~~~~~~~~~~~~~~~~
		public final static String 比对任务状态 = "XZD007000016";

		public final static String 比对任务状态_获取指纹 = "BD0001";
		
		public final static String 比对任务状态_正在碰撞 = "BD0002";
		
		public final static String 比对任务状态_手工更新 = "BD0003";
		
		public final static String 比对任务状态_排他处理 = "BD0004";
		
		public final static String 比对任务状态_推送网监 = "BD0005";
		
		public final static String 比对任务状态_办结 = "BD0006";
		
		public final static String 比对任务状态_碰撞完毕 = "BD0007";
		
		public final static String 比对任务状态_网监返回 = "BD0008";
		
		public final static String 比对任务状态_自动下发 = "BD0009";
		
		public final static String 比对任务状态_手动下发 = "BD0010";
		
		public final static String 比对任务状态_在侦 = "BD0011";
		
		public final static String 比对任务状态_自动分配 = "BD0012";
		
		public final static String 比对任务状态_手动分配 = "BD0013";

		// ~~~~~~~~~~~~~~~~~~~~~比对结果~~~~~~~~~~~~~~~~~~~~~~
		public final static String 比对结果 = "XZD007000017";
		
		public final static String 比对结果_初始结果 = "RS0001";
		
		public final static String 比对结果_已排他 = "RS0002";
		
		public final static String 比对结果_网监返回 = "RS0003";
		
		public final static String 比对结果_办结 = "RS0004";
		
		public final static String 比对结果_推送网监 = "RS0005";
		
		public final static String 比对结果_在侦 = "RS0006";
		
		// ~~~~~~~~~~~~~~~~~~~~~比对数据类型~~~~~~~~~~~~~~~~~~~~~~
		public final static String 比对数据类型 = "XZD007000018";
		
		public final static String 比对数据类型_手机号 = "cellPhoneNum";
		
		public final static String 比对数据类型_QQ = "QQ";
		
		public final static String 比对数据类型_微信 = "weChat";
		
		public final static String 比对数据类型_IMEI = "IMEI";
		
		public final static String 比对数据类型_IMSI = "IMSI";
		
		public final static String 比对数据类型_MAC = "MAC";
		
		public final static String 比对数据类型_地址 = "address";
		
		public final static String 比对数据类型_京东 = "JingDong";
		
		public final static String 比对数据类型_淘宝 = "TaoBao";
		
		public final static String 比对数据类型_经度 = "JingDu";
		
		public final static String 比对数据类型_纬度 = "WeiDu";

		public final static String 比对数据类型_车牌号 = "CarNum";
		
		
		// ~~~~~~~~~~~~~~~~~~~~~比对数据来源~~~~~~~~~~~~~~~~~~~~~~
		public final static String 比对数据来源 = "XZD007000020";
		
		public final static String 比对数据来源_系统碰撞 = "SJ0001";
		
		public final static String 比对数据来源_人工更新 = "SJ0002";
		
		public final static String 比对数据来源_网监返回 = "SJ0003";
		
		// ============== 颜色标志 ===============
		public static final String 颜色标志_红色	= "001";
		public static final String 颜色标志_黄色 	= "002";
		public static final String 颜色标志_绿色 	= "003";
		
		
		// ============== 比对数据结果处理 ===============
		public final static String 比对数据结果处理 = "XZD007000021";
		
		public static final String 比对数据结果处理_物证与案件无关联_事主及主要关系人所留	= "PC001";
		
		public static final String 比对数据结果处理_物证与案件无关联_其他情况可以排除嫌疑	= "PC002";
		
		public static final String 比对数据结果处理_未达立案标准无立案条件_案件未立	= "BJ001";
		
		public static final String 比对数据结果处理_人员已抓获_该案件已破或已移交	= "BJ002";
		
		public static final String 比对数据结果处理_人员已抓获_其他原因未达破案条件	= "BJ003";
		
		public static final String 比对数据结果处理_人员身份明确_确定嫌疑已临控 = "ZZ001";
		
		public static final String 比对数据结果处理_人员身份明确_暂未确定与案件关联性 = "ZZ002";
		
		public static final String 比对数据结果处理_人员身份不明确_待核实身份 = "ZZ003";
		
		public static final String 比对数据结果处理_其他 = "QT001";
		
		
				
		
}
