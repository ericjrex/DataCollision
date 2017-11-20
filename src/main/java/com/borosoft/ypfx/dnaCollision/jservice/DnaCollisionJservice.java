package com.borosoft.ypfx.dnaCollision.jservice;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.borosoft.commons.utils.AssistUtils;
import com.borosoft.commons.utils.UserInfo;
import com.borosoft.component.delete.DeleteDataApi;
import com.borosoft.component.imp.status.ReadinStatusAssist;
import com.borosoft.component.query.impl.BatchQueryImpl;
import com.borosoft.component.save.SaveDataApi;
import com.borosoft.dirInfo.entity.DirInfo;
import com.borosoft.dirInfo.entity.DirItem;
import com.borosoft.dirInfo.enums.ZygxEnums;
import com.borosoft.dirInfo.holder.DirItemHolder;
import com.borosoft.dirInfo.holder.rules.ReservedFields;
import com.borosoft.dirInfo.queryApi.DirInfoQueryApi;
import com.borosoft.entityquery.EntityQuery;
import com.borosoft.entityquery.jdbc.EntityService;
import com.borosoft.framework.AppConfig;
import com.borosoft.framework.codec.MD5;
import com.borosoft.framework.codec.UUID;
import com.borosoft.framework.commons.QueryResult;
import com.borosoft.framework.commons.query.Criteria;
import com.borosoft.framework.commons.query.Query;
import com.borosoft.framework.exception.BusinessException;
import com.borosoft.framework.log.BizLogger;
import com.borosoft.framework.utils.StringUtils;
import com.borosoft.platform.org.domain.Org;
import com.borosoft.platform.org.service.OrgService;
import com.borosoft.query.query.DataQuery;
import com.borosoft.query.query.ui.DataCriteria;
import com.borosoft.query.query.ui.DataCriterion;
import com.borosoft.query.source.jdbc.JdbcQuerier;
import com.borosoft.ypfx.Dictionary;
import com.borosoft.ypfx.dnaCollision.entity.DnaInfo;
import com.borosoft.ypfx.dnaCollision.entity.DnaTaskDetailedInfo;
import com.borosoft.ypfx.dnaCollision.entity.DnaTaskInfo;
import com.borosoft.ypfx.dnaCollision.entity.PersonCollisonInfo;
import com.borosoft.ypfx.dnaCollision.entity.PersonCollisonInfoSide;
import com.borosoft.ypfx.dnaCollision.entity.PersonInfoDetailed;
import com.borosoft.ypfx.dnaCollision.entity.PersonInfoMian;
import com.borosoft.ypfx.dnaCollision.entity.holder.CollisionTimeLog;
import com.borosoft.ypfx.dnaCollision.entity.rules.DgAlertFields;
import com.borosoft.ypfx.dnaCollision.entity.rules.DgCaseFields;
import com.borosoft.ypfx.dnaCollision.entity.rules.PersonCollisonInfoSideFields;
import com.borosoft.ypfx.dnaCollision.entity.rules.PersonInfoDetailedFields;
import com.borosoft.ypfx.dnaCollision.entity.rules.PzryxxFields;
import com.borosoft.ypfx.dnaCollision.entity.rules.XyrxxFields;
import com.borosoft.ypfx.dnaCollision.entity.rules.XyrxxMiddFields;
import com.borosoft.ypfx.dnaCollision.service.DnaTaskInfoService;
import com.borosoft.ypfx.dnaCollision.utils.BeanUitl;
import com.borosoft.ypfx.dnaCollision.utils.ImportHolder;
import com.borosoft.ypfx.dnaCollision.utils.SubofficeOrgUtils;

@Component("dnaCollisionJservice")
public class DnaCollisionJservice {
	
	@Resource
	private EntityService entityService;

	@Resource
	private JdbcQuerier jdbcQuerier;

	@Resource
	private DnaTaskInfoService dnaTaskInfoService;

	@Resource
	private SaveDataApi saveDataApi;

	@Resource
	private DirInfoQueryApi dirInfoQueryApi;
	
	@Resource
	private ImportHolder importHolder;

	@Resource(name = "deleteDataApiImpl")
	protected DeleteDataApi deleteDataApi;
	
	@Resource
	private OrgService orgService;
	
	@Resource
	private AppConfig appConfig;

	private int maxNum = 1000;
	
	public static Set<String> fieldNameSet = new HashSet<>();
	
	static{
		//fieldNameSet.add("dcd_ajbh");
		fieldNameSet.add("dcd_xm");
		fieldNameSet.add("dcd_sfzh");
		fieldNameSet.add("dcd_sjh");
		fieldNameSet.add("dcd_qq");
		fieldNameSet.add("dcd_wx");
		fieldNameSet.add("dcd_imei");
		fieldNameSet.add("dcd_imsi");
		fieldNameSet.add("dcd_mac");
		fieldNameSet.add("dcd_jd");
		fieldNameSet.add("dcd_tb");
		fieldNameSet.add("dcd_jd0");
		fieldNameSet.add("dcd_wd");
		fieldNameSet.add("dcd_dz");
		fieldNameSet.add("dcd_zhbhsj");
		fieldNameSet.add("xzd_cph");
		fieldNameSet.add("dcd_sjzt");
	}

	public List<DnaInfo> getDnaInfoByQuery(EntityQuery entityQuery) {
		List<DnaInfo> queryForEntity = entityService.queryForEntity(entityQuery);
		return queryForEntity;
	}
	
	public void dataCollisionByTime(){
		try {
			String dataCollisionTime = appConfig.getString("dataCollisionTime");
			long beginTime = System.currentTimeMillis();
			System.out.println("");
			System.out.println("");
			System.err.println("--------------------【dna全库碰撞】开始------------------------");
			DataQuery dataQuery = DataQuery.create(DnaInfo.tableName);
			dataQuery.addOrder(com.borosoft.query.query.ui.Order.desc("RKSJ"));
			dataQuery.add(DataCriteria.isNotNull("RKSJ"));
			dataQuery.setLimit(1, 1);
			Date startDate = null;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<Map<String, Object>> queryForList = jdbcQuerier.queryForList(dataQuery);
			if(queryForList.size() > 0){
				Map<String, Object> dnaMap = queryForList.get(0);
				Date rksj = (Date) dnaMap.get("RKSJ");
				dataQuery = DataQuery.create(DnaInfo.tableName);
				//dataQuery.addOrder();
				dataQuery.addOrder(com.borosoft.query.query.ui.Order.asc("BZSJ"),com.borosoft.query.query.ui.Order.asc("XM"));
				DataQuery timeLogDataQuery = DataQuery.create(CollisionTimeLog.tableName);
				timeLogDataQuery.addOrder(com.borosoft.query.query.ui.Order.desc(CollisionTimeLog.SJC));
				timeLogDataQuery.setLimit(1,1);
				List<Map<String, Object>> timeLogList = jdbcQuerier.queryForList(timeLogDataQuery);
				if(timeLogList.size() > 0){
					Map<String, Object> storeTimeLogMap = timeLogList.get(0);
					startDate = (Date) storeTimeLogMap.get(CollisionTimeLog.SJC);
				}
				if(startDate != null){
					dataQuery.add(DataCriteria.gt("RKSJ", startDate));
				}else{
					Date parse = simpleDateFormat.parse(dataCollisionTime);
					dataQuery.add(DataCriteria.gte("RKSJ", parse));
				}
				dataQuery.add(DataCriteria.lte("RKSJ", rksj));
				
				//保存最后碰撞的时间
				DirInfo timeLogDirInfo = dirInfoQueryApi.getDirInfoByTableName(CollisionTimeLog.tableName, ZygxEnums.dirItems);
				Map<String, Object> timeLogMap = new HashMap<String,Object>();
				timeLogMap.put(CollisionTimeLog.WYBH, UUID.randomUUID());
				timeLogMap.put(CollisionTimeLog.SJC, rksj);
				timeLogMap.put(CollisionTimeLog.KSSJ, new Date());
				saveDataApi.addDirInfoData(timeLogDirInfo, timeLogMap, false, AssistUtils.getCurrUserInfo(), null);
				
				
				BatchQueryImpl batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
				batchQuery.getDataQuery().setLimit(1,1000);
			    batchQuery.getTotal();
				
				Map<String,Map<String,Object>> dnaInfoMap = new HashMap<>();
				List<Map<String,Object>> noNeedDealDnaInfoList = new ArrayList<>();
				List<Map<String, Object>> noSfzhDnaInfoList = new ArrayList<Map<String, Object>>();
				
				while(batchQuery.hasNext()){
					List<Map<String, Object>> dataList = batchQuery.next();
					for (Map<String, Object> map : dataList) {
						String sfzh = (String)map.get("SFZH");
						if(StringUtils.isNotBlank(sfzh)){
							if("无".equals(sfzh)){
								noSfzhDnaInfoList.add(map);
							}else{
								if(!dnaInfoMap.containsKey(sfzh)){
									dnaInfoMap.put(sfzh, map);
								}else{
									noNeedDealDnaInfoList.add(map);
								}
							}
						}else{
							noSfzhDnaInfoList.add(map);
						}
					}
				}
				
				List<Map<String,Object>> saveDnaList = new ArrayList<>(dnaInfoMap.values());
				List<Map<String, Object>> subList = null;
				int size = saveDnaList.size();
				int curIndex = 0;
				int endIndex = 0;
				List<DnaInfo> dnaInfoList = new ArrayList<DnaInfo>();
				DnaInfo dnaInfo = null;
				
				double count = size / 1000;
				int index = 0;
				
				while(size > 0){
					index ++;
					System.out.println("");
					System.out.println("");
					System.out.println("-------------------------------------------------------------------第【"+index+"】次开始---------运行总次数【"+count+"】---------------------------------------------------------------------");
					System.out.println("");
					System.out.println("");
					
					if(size >= 1000){
						endIndex += 999;
						subList = saveDnaList.subList(curIndex, endIndex);
						curIndex = endIndex;
						size -=999;
					}else{
						endIndex = saveDnaList.size();
						subList = saveDnaList.subList(curIndex, endIndex);
						size = 0;
					}
					
					for (Map<String, Object> curMap : subList) {
						dnaInfo = (DnaInfo) BeanUitl.convertMap(DnaInfo.class, curMap);
						
						dnaInfoList.add(dnaInfo);
					}
					this.dnaDataCollision(dnaInfoList);
				}
				
				
				timeLogMap.put(CollisionTimeLog.JSSJ, new Date());
				saveDataApi.updateDirInfoData(timeLogDirInfo, timeLogMap, false, AssistUtils.getCurrUserInfo(), null);
				
				long endTime = System.currentTimeMillis();
				System.out.println("--------------------【dna全库碰撞】结束，共耗时："+(endTime-beginTime)/1000+"秒------------------------");
				System.out.println("");
				System.out.println("");  
				
				
				
				//无身份证数据碰撞
				saveDnaList = noSfzhDnaInfoList;
				subList = null;
				size = saveDnaList.size();
				curIndex = 0;
				endIndex = 0;
				dnaInfoList = new ArrayList<DnaInfo>();
				dnaInfo = null;
				
				count = size / 1000;
				index = 0;
				
				while(size > 0){
					index ++;
					System.out.println("");
					System.out.println("");
					System.out.println("-------------------------------------------------------------------第【"+index+"】次开始---------运行总次数【"+count+"】---------------------------------------------------------------------");
					System.out.println("");
					System.out.println("");
					
					if(size >= 1000){
						endIndex += 999;
						subList = saveDnaList.subList(curIndex, endIndex);
						curIndex = endIndex;
						size -=999;
					}else{
						endIndex = saveDnaList.size();
						subList = saveDnaList.subList(curIndex, endIndex);
						size = 0;
					}
					
					for (Map<String, Object> curMap : subList) {
						dnaInfo = (DnaInfo) BeanUitl.convertMap(DnaInfo.class, curMap);
						
						dnaInfoList.add(dnaInfo);
					}
					this.dnaDataCollisionByNoSfzh(dnaInfoList);
				}//
				
				//有dna数据，但不是优先比中的碰撞信息任务
				saveDnaList = noNeedDealDnaInfoList;
				subList = null;
				size = saveDnaList.size();
				curIndex = 0;
				endIndex = 0;
				dnaInfoList = new ArrayList<DnaInfo>();
				dnaInfo = null;
				
				count = size / 1000;
				index = 0;
				
				while(size > 0){
					index ++;
					System.out.println("");
					System.out.println("");
					System.out.println("-------------------------------------------------------------------第【"+index+"】次开始---------运行总次数【"+count+"】---------------------------------------------------------------------");
					System.out.println("");
					System.out.println("");
					
					if(size >= 1000){
						endIndex += 999;
						subList = saveDnaList.subList(curIndex, endIndex);
						curIndex = endIndex;
						size -=999;
					}else{
						endIndex = saveDnaList.size();
						subList = saveDnaList.subList(curIndex, endIndex);
						size = 0;
					}
					
					for (Map<String, Object> curMap : subList) {
						dnaInfo = (DnaInfo) BeanUitl.convertMap(DnaInfo.class, curMap);
						
						dnaInfoList.add(dnaInfo);
					}
					this.secondDataCollision(dnaInfoList);
				}
				
				
				System.out.println("-----------------------------------无身份证人员【"+noSfzhDnaInfoList.size()+"】--------------------------------------------------------");
				endTime = System.currentTimeMillis();
				System.out.println("--------------------【最终dna全库碰撞】结束，共耗时："+(endTime-beginTime)/1000+"秒------------------------");
				System.out.println("");
				System.out.println("");  
			}
		} catch (Exception e) {
			e.printStackTrace();
			BizLogger.setExceptionLog(e);
		}
			
	}

	@SuppressWarnings("deprecation")
	public void dnaDataCollision(List<DnaInfo> dnaInfoByQuery) throws Exception{
		long beginTime1 = System.currentTimeMillis();
		System.out.println("");
		System.out.println("");
		System.err.println("--------------------【DNA碰撞任务开始】开始------------------------");

		UserInfo userInfo = AssistUtils.getCurrUserInfo();
		DnaTaskInfo dnaTaskInfo = null;
		DnaTaskDetailedInfo dnaTaskDetailedInfo = null;
		// String dnaId = "";
		String ajbh = "";
		Date date = null;
		String sfzh = "";
		String jzrybh = "";
		List<DnaTaskInfo> taskList = new ArrayList<DnaTaskInfo>();
		List<DnaTaskDetailedInfo> taskDetailedList = new ArrayList<DnaTaskDetailedInfo>();

		long beginTime = System.currentTimeMillis();
		System.out.println("");
		System.out.println("");
		System.err.println("--------------------【组装案件编号，人员编号，身份证条件】开始------------------------");
		List<String> ajbhList = new ArrayList<String>();
		List<String> jqAjbhList = new ArrayList<String>();
		List<String> rybhList = new ArrayList<String>();
		List<String> sfzList = new ArrayList<String>();
		
		Map<String,DnaInfo> dnaMapByAjbh = new HashMap<>();
		Map<String,DnaInfo> dnaMapsBySfzh = new HashMap<>();
		for (DnaInfo dnaInfo : dnaInfoByQuery) {
			date = dnaInfo.getAddTime();
			// dnaId = dnaInfo.getId();
			sfzh = dnaInfo.getSfzh();
			if (StringUtils.isNotBlank(sfzh)) {
				if(!dnaMapsBySfzh.containsKey(sfzh)){
					dnaMapsBySfzh.put(sfzh, dnaInfo);
				}
				
				if (!sfzList.contains(sfzh)) {
					sfzList.add(sfzh);
				}
			}
			//警综案件编号
			ajbh = dnaInfo.getJzajbh();
			if (StringUtils.isNotBlank(ajbh)) {
				if (!ajbhList.contains(ajbh)) {
					ajbhList.add(ajbh);
				}
			}
			dnaMapByAjbh.put(ajbh, dnaInfo);
			//物证案件编号
			String wzAjbh = dnaInfo.getWzajbh();
			if (StringUtils.isNotBlank(wzAjbh)) {
				if (!ajbhList.contains(wzAjbh)) {
					ajbhList.add(wzAjbh);
				}
			}
			dnaMapByAjbh.put(wzAjbh, dnaInfo);
			
			//警综人员编号
			jzrybh = dnaInfo.getJzrybh();
			if (StringUtils.isNotBlank(jzrybh)) {
				if (!rybhList.contains(jzrybh)) {
					rybhList.add(jzrybh);
				}
			}
			//样本人员编号
			String ryybbh = dnaInfo.getRyybbh();
			if (StringUtils.isNotBlank(ryybbh)) {
				if (!rybhList.contains(ryybbh)) {
					rybhList.add(ryybbh);
				}
			}
		}

		long endTime = System.currentTimeMillis();
		System.out.println("--------------------【组装案件编号，人员编号，身份证条件】结束，共耗时：" + (endTime - beginTime) / 1000 + "秒------------------------");
		System.out.println("");
		System.out.println("");
		
		//先查一次嫌疑人，获取相关嫌疑人编号
		if(sfzList.size() > 0){
			DataQuery dataQuerySfz = DataQuery.create(XyrxxFields.tableName);
			dataQuerySfz.add(DataCriteria.in(XyrxxFields.field_ZJHM, sfzList));
			BatchQueryImpl batchQuerySfz = BatchQueryImpl.newInstance(dataQuerySfz, jdbcQuerier);
			batchQuerySfz.getDataQuery().setLimit(1, maxNum);
			batchQuerySfz.getTotal();
			while (batchQuerySfz.hasNext()) {
				List<Map<String, Object>> dataList = batchQuerySfz.next();
				for (Map<String, Object> map : dataList) {
					jzrybh = (String)map.get(XyrxxFields.field_SYSTEMID);
					if(!rybhList.contains(jzrybh)){
						rybhList.add(jzrybh);
					}
				}
			}
		}
		

		//Map<String, String> middlePersonMapByRybh = new HashMap<String, String>();
		Map<String, Set<String>> middlePersonMapByRybh = new HashMap<>();
		
		beginTime = System.currentTimeMillis();
		System.out.println("");
		System.out.println("");
		System.err.println("--------------------【查询人员涉案情况-中间表】开始------------------------");
		DataQuery dataQuery = DataQuery.create(XyrxxMiddFields.tableName);
		List<DataCriterion> dataCriterias = new ArrayList<DataCriterion>();
		if (rybhList.size() > 0) {
			dataCriterias.add(DataCriteria.in(XyrxxMiddFields.field_RYBH, rybhList));
		}
		if (ajbhList.size() > 0) {
			dataCriterias.add(DataCriteria.in(XyrxxMiddFields.field_AJBH, ajbhList));
		}

		dataQuery.add(DataCriteria.or(dataCriterias));

		// 先查询警综_人员涉案情况-中间表
		BatchQueryImpl batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
		batchQuery.getDataQuery().setLimit(1, maxNum);
		batchQuery.getTotal();
		Set<String> middelInfoRybhSet = new HashSet<String>();
		Set<String> ajbhGroupSet = null;
		while (batchQuery.hasNext()) {
			List<Map<String, Object>> dataList = batchQuery.next();
			for (Map<String, Object> dataMap : dataList) {
				ajbh = (String) dataMap.get(XyrxxMiddFields.field_AJBH);
				if (!ajbhList.contains(ajbh)) {
					ajbhList.add(ajbh);
				}
				if(ajbh.startsWith("J") || ajbh.startsWith("j")){
					if (!jqAjbhList.contains(ajbh)) {
						jqAjbhList.add(ajbh);
					}
				}
				if(ajbh.equals("A4419685200002017040223")){
					System.out.println(123);
				}
				jzrybh = (String) dataMap.get(XyrxxMiddFields.field_RYBH);
				ajbhGroupSet = middlePersonMapByRybh.get(jzrybh);
				if(ajbhGroupSet == null){
					ajbhGroupSet = new HashSet<>();
					middlePersonMapByRybh.put(jzrybh, ajbhGroupSet);
				}
				ajbhGroupSet.add(ajbh);
				
				middelInfoRybhSet.add(jzrybh);
			}
		}

		endTime = System.currentTimeMillis();
		System.out.println("--------------------【查询人员涉案情况-中间表】结束，共耗时：" + (endTime - beginTime) / 1000 + "秒------------------------");
		System.out.println("");
		System.out.println("");
		
		
		
		beginTime = System.currentTimeMillis();
		System.out.println("");
		System.out.println("");
		System.err.println("--------------------【查询案件信息表】开始------------------------");
		Map<String,Map<String,Object>> caseInfoMapByAjbh = new HashMap<>();
		Map<String,Map<String,Object>> jqCaseInfoMapByAjbh = new HashMap<>();
		dataQuery = DataQuery.create(DgCaseFields.tableName);
		//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//Date parse = simpleDateFormat.parse("2017-1-1 00:00:00");
		if (ajbhList.size() > 0) {
			dataQuery.add(DataCriteria.in(DgCaseFields.AJBH, ajbhList));
			dataQuery.addSelectField(DgCaseFields.AJBH);
			dataQuery.addSelectField(DgCaseFields.SLJSDW);
			dataQuery.addSelectField(DgCaseFields.FASJCZ);
			dataQuery.addSelectField(DgCaseFields.JA_JASJ);
			//dataQuery.add(DataCriteria.gte(DgCaseFields.FASJCZ, parse));
			batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
			batchQuery.getDataQuery().setLimit(1, maxNum);
			batchQuery.getTotal();
			while(batchQuery.hasNext()){
				List<Map<String, Object>> dataList = batchQuery.next();
				for (Map<String, Object> map : dataList) {
					caseInfoMapByAjbh.put(map.get(DgCaseFields.AJBH).toString(), map);
				}
			}
		}
		
		
		//查询警情信息
		dataQuery = DataQuery.create(DgAlertFields.tableName);
		if (jqAjbhList.size() > 0) {
			dataQuery.add(DataCriteria.in(DgAlertFields.field_AJBH, jqAjbhList));
			batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
			batchQuery.getDataQuery().setLimit(1, maxNum);
			batchQuery.getTotal();
			while(batchQuery.hasNext()){
				List<Map<String, Object>> dataList = batchQuery.next();
				for (Map<String, Object> map : dataList) {
					jqCaseInfoMapByAjbh.put(map.get(DgAlertFields.field_AJBH).toString(), map);
				}
			}
		}
		
		
		//组装机构map
		Org org = null;
		Map<String, Org> orgMap = getOrgMap();
		
		endTime = System.currentTimeMillis();
		System.out.println("--------------------【查询案件信息表】结束，共耗时：" + (endTime - beginTime) / 1000 + "秒------------------------");
		System.out.println("");
		System.out.println("");
		

		beginTime = System.currentTimeMillis();
		System.out.println("");
		System.out.println("");
		System.err.println("--------------------【查询嫌疑人】开始------------------------");
		// 再查询嫌疑人信息
		dataQuery = DataQuery.create(XyrxxFields.tableName);
		dataCriterias = new ArrayList<DataCriterion>();
		if (rybhList.size() > 0) {
			dataCriterias.add(DataCriteria.in(XyrxxFields.field_SYSTEMID, rybhList));
		}
		if (middelInfoRybhSet.size() > 0) {
			dataCriterias.add(DataCriteria.in(XyrxxFields.field_SYSTEMID, middelInfoRybhSet));
		}
		if (sfzList.size() > 0) {
			dataCriterias.add(DataCriteria.in(XyrxxFields.field_ZJHM, sfzList));
		}
		
		dataQuery.add(DataCriteria.or(dataCriterias));

		batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
		batchQuery.getDataQuery().setLimit(1, maxNum);
		batchQuery.getTotal();
		
		String qq = "";
		// 户籍地址
		String hjdz = "";
		// 联系电话
		String lxdh = "";
		// 手机号码1
		String sjhm1 = "";
		// 手机号码2
		String sjhm2 = "";
		// 姓名
		String xm = "";
		// 微信
		 String weChat = "";
		// imei
		String imei = "";
		// imsi
		String imsi = "";
		// mac
		String mac = "";
		// 京东
		String jd = "";
		// 淘宝
		String tb = "";
		// jd1经度
		String jd1 = "";
		//纬度
		String wd= "";
		//车牌号
		String cph= "";
		

		String sjlx = "";
		PersonCollisonInfo personCollisonInfo = null;
		PersonInfoMian personInfoMian = null;
		List<PersonInfoDetailed> personInfoDetailedList = null;
		Map<String, PersonInfoMian> personInfoMianMap = new HashMap<String, PersonInfoMian>();
		Map<String, List<PersonInfoDetailed>> personInfoDetailedMap = new HashMap<String, List<PersonInfoDetailed>>();
		Map<String, DnaTaskInfo> dnaTaskInfoMap = new HashMap<String,DnaTaskInfo>();
		Map<String, DnaTaskDetailedInfo> dnaTaskInfoDetailedMap = new HashMap<String,DnaTaskDetailedInfo>();
		//人员编号分组map，以案件编号为key，身份证为子map key，人员编号为子map val
		Map<String,	Map<String,String>> rybhAllGroupMap = new HashMap<>();
		Map<String, String> rybhGroupMap = null;
		
		//人员相关案件编号， 人员主表数据编号为key，案件编号为val
		Map<String, List<String>> personAjbhMap = new HashMap<String,List<String>>();
		List<String> personAjbhList = null;
		
		Set<String> personSfzhmSet = new HashSet<String>();
		Set<String> pzphSet = new HashSet<String>();
		Set<String> detailedWybhSet = new HashSet<>();

		List<Map<String, Object>> allSuspectList = new ArrayList<>();
		Set<String> searchSfzhSet = new HashSet<>();
		
		//不必新增碰撞信息的身份证号
		Set<String> noNeedToAddSfzh = new HashSet<>();
		//不必新增碰撞信息的碰撞编号
		Set<String> noNeedToAddPzbh = new HashSet<>();
				
		//已办结或排他的身份证号
		Set<String> hasDealSfzh = new HashSet<>();
		Map<String, Map<String,Object>> hasDealSfzhMap = new HashMap<>();
		
		date = new Date();
		Set<String> mainInfoIdSet =new HashSet<>();
		//查出已碰撞的人员数据（未办结，未排他）
		while (batchQuery.hasNext()) {
			List<Map<String, Object>> dataList = batchQuery.next();
			allSuspectList.addAll(dataList);
			for (Map<String, Object> dataMap : dataList) {
				sfzh = (String) dataMap.get(XyrxxFields.field_ZJHM);
				searchSfzhSet.add(sfzh);
			}
			if(searchSfzhSet.size() > 0){
				DataQuery dataQuerySfz = DataQuery.create(PzryxxFields.tableName);
				dataQuerySfz.add(DataCriteria.in(PzryxxFields.field_SFZH, searchSfzhSet));
				//dataQuerySfz.add(DataCriteria.not(DataCriteria.eq(PzryxxFields.field_SJZT, Dictionary.比对结果_办结)));
				//dataQuerySfz.add(DataCriteria.not(DataCriteria.eq(PzryxxFields.field_SJZT, Dictionary.比对结果_已排他)));
				List<Map<String, Object>> storePzxxResultList = jdbcQuerier.queryForList(dataQuerySfz);
				for (Map<String, Object> map : storePzxxResultList) {
					String sjzt = (String)map.get(PzryxxFields.field_SJZT);
					sfzh = (String)map.get(PzryxxFields.field_SFZH);
					xm = (String)map.get(PzryxxFields.field_XM);
					ajbh = (String)map.get(PzryxxFields.field_AJBH);
					String pzbh = (String)map.get(PzryxxFields.field_PZBH);
					if(StringUtils.isNotBlank(sfzh) && StringUtils.isNotBlank(xm)){
						String mainInfoId = MD5.md5(sfzh + xm + ajbh);
						
						mainInfoIdSet.add(mainInfoId);
					}
					if(!Dictionary.比对结果_办结.equals(sjzt) && !Dictionary.比对结果_已排他.equals(sjzt)){
						noNeedToAddSfzh.add(sfzh);
						noNeedToAddPzbh.add(pzbh);
					}else{
						hasDealSfzhMap.put(sfzh,map);
						hasDealSfzh.add(sfzh);
					}
				}
			}
		}
		
		
		//第一次对子表信息分组，用于曾经办结、排他的新碰撞信息
		Map<String, Map<String,List<Map<String, Object>>>> storePersonDetailedMapByZbbh = new HashMap<>();
		List<Map<String, Object>> detailedGroupList = null;
		Map<String, List<Map<String, Object>>> detailedGroupMap = null;
		Set<Object> hasAddSfzh = new HashSet<>();
		
		Map<String,Object> storePzxxInfoMap = null;
		for (Map<String, Object> dataMap : allSuspectList) {
			jzrybh = (String) dataMap.get(XyrxxFields.field_SYSTEMID);
			sfzh = (String) dataMap.get(XyrxxFields.field_ZJHM);
			xm= (String) dataMap.get(XyrxxFields.field_XM);
			qq = (String) dataMap.get(XyrxxFields.field_RESERVATION35);
			hjdz = (String) dataMap.get(XyrxxFields.field_HJDZ);
			lxdh = (String) dataMap.get(XyrxxFields.field_LXDH);
			sjhm1 = (String) dataMap.get(XyrxxFields.field_RESERVATION41);
			sjhm2 = (String) dataMap.get(XyrxxFields.field_RESERVATION42);
			
			//当已办结，已排他的碰撞人员重新有数据时，获取旧的基本信息进行填充
			storePzxxInfoMap = hasDealSfzhMap.get(sfzh);
			if(storePzxxInfoMap != null){
				qq = (String)storePzxxInfoMap.get(PzryxxFields.field_QQ);
				hjdz = (String)storePzxxInfoMap.get(PzryxxFields.field_DZ);
				lxdh = (String)storePzxxInfoMap.get(PzryxxFields.field_SJH);
				weChat = (String)storePzxxInfoMap.get(PzryxxFields.field_WX);
				imei = (String)storePzxxInfoMap.get(PzryxxFields.field_IMEI);
				imsi = (String)storePzxxInfoMap.get(PzryxxFields.field_IMSI);
				mac = (String)storePzxxInfoMap.get(PzryxxFields.field_MAC);
				jd = (String)storePzxxInfoMap.get(PzryxxFields.field_JD);
				tb = (String)storePzxxInfoMap.get(PzryxxFields.field_TB);
				jd1 = (String)storePzxxInfoMap.get(PzryxxFields.field_JD0);
				wd = (String)storePzxxInfoMap.get(PzryxxFields.field_WD);
				cph = (String)storePzxxInfoMap.get(PzryxxFields.field_CPH);
			}
			if("王中安".equals(xm)){
				System.out.println(123);
			}
			
			ajbhGroupSet = middlePersonMapByRybh.get(jzrybh);
			
			DnaInfo dnaInfo = dnaMapsBySfzh.get(sfzh);
			if(dnaInfo != null){
				String jzajbh = dnaInfo.getJzajbh();
				String wzajbh = dnaInfo.getWzajbh();
				
				Map<String, Object> caseInfoMap = caseInfoMapByAjbh.get(jzajbh);
				ajbh = jzajbh;
				if(caseInfoMap == null){
					if(StringUtils.isNotBlank(wzajbh)){
						caseInfoMap = caseInfoMapByAjbh.get(wzajbh);
						ajbh = wzajbh;
					}
				}
				//该判断用于DNA有无案件编号
				if(!hasAddSfzh.contains(sfzh)){
					if(StringUtils.isBlank(ajbh)  ){
						if(ajbhGroupSet != null){
							for (String curAjbh : ajbhGroupSet) {
								caseInfoMap = caseInfoMapByAjbh.get(curAjbh);
								if(caseInfoMap != null){
									ajbh = curAjbh;
									hasAddSfzh.add(sfzh);
									break;
								}
							}
						}else{
							continue;
						}
						
					}else{
						DnaInfo dnaInfoByAjbh = dnaMapByAjbh.get(ajbh);
						if(dnaInfoByAjbh == null ){
							continue;
						}else{
							hasAddSfzh.add(sfzh);
						}
					}
				}else{
					continue;
				}
				
				if(StringUtils.isBlank(ajbh)){
					continue;
				}
				
				String pzbh = MD5.md5(xm + ajbh);
				if(noNeedToAddPzbh.contains(pzbh)){
					continue;
				}
				
				if(StringUtils.isNotBlank(sfzh) && !noNeedToAddSfzh.contains(sfzh)  ){
					String mainInfoId = MD5.md5(sfzh + xm + ajbh);
					if (StringUtils.isNotBlank(sfzh)) {
						rybhGroupMap = rybhAllGroupMap.get(ajbh);
						if(rybhGroupMap == null){
							rybhGroupMap = new HashMap<>();
							rybhAllGroupMap.put(ajbh, rybhGroupMap);
						}
						rybhGroupMap.put(sfzh, jzrybh);
						
						personSfzhmSet.add(sfzh);
						if (!personInfoMianMap.containsKey(mainInfoId)) {
							personInfoMian = new PersonInfoMian();
							personInfoMian.setDcd_sfz(sfzh);
							personInfoMian.setDcd_sjbh(mainInfoId);
							personInfoMian.setDcd_xm(xm);
							personInfoMian.setXzd_ajbh(ajbh);
							personInfoMianMap.put(mainInfoId, personInfoMian);
						}
						
						personAjbhList =  personAjbhMap.get(mainInfoId);
						
						if(personAjbhList == null){
							personAjbhList = new ArrayList<>();
							personAjbhMap.put(mainInfoId, personAjbhList);
						}
						personAjbhList.add(ajbh);
						
						personInfoDetailedList = personInfoDetailedMap.get(jzrybh);
						if (personInfoDetailedList == null) {
							personInfoDetailedList = new ArrayList<PersonInfoDetailed>();
							personInfoDetailedMap.put(jzrybh, personInfoDetailedList);
						}
					}

					if (personInfoDetailedList != null) {
						if (StringUtils.isNotBlank(qq)) {
							sjlx = Dictionary.比对数据类型_QQ;
							qq = qq.replaceAll("，", ",");
							String[] qqStr = qq.split(",");
							for (String str : qqStr) {
								buildPersonDetailedByInit(mainInfoId, sjlx, str, ajbh, jzrybh, date, personInfoDetailedList,detailedWybhSet);
							}
						}

						if (StringUtils.isNotBlank(lxdh)) {
							sjlx = Dictionary.比对数据类型_手机号;
							lxdh = lxdh.replaceAll("，", ",");
							String[] lxdhStr = lxdh.split(",");
							for (String str : lxdhStr) {
								buildPersonDetailedByInit(mainInfoId, sjlx, str, ajbh, jzrybh, date, personInfoDetailedList,detailedWybhSet);
							}
						}

						if (StringUtils.isNotBlank(sjhm1)) {
							sjlx = Dictionary.比对数据类型_手机号;
							sjhm1 = sjhm1.replaceAll("，", ",");
							String[] sjhm1Str = sjhm1.split(",");
							for (String str : sjhm1Str) {
								buildPersonDetailedByInit(mainInfoId, sjlx, str, ajbh, jzrybh, date, personInfoDetailedList,detailedWybhSet);
							}
						}

						if (StringUtils.isNotBlank(sjhm2)) {
							sjlx = Dictionary.比对数据类型_手机号;
							sjhm2 = sjhm2.replaceAll("，", ",");
							String[] sjhm2Str = sjhm2.split(",");
							for (String str : sjhm2Str) {
								buildPersonDetailedByInit(mainInfoId, sjlx, str, ajbh, jzrybh, date, personInfoDetailedList,detailedWybhSet);
							}
						}
						
						//当已办结，已排他的碰撞人员重新有数据时，获取旧的基本信息进行填充
						if(storePzxxInfoMap != null){
							if (StringUtils.isNotBlank(hjdz)) {
								sjlx = Dictionary.比对数据类型_地址;
								hjdz = hjdz.replaceAll("，", ",");
								String[] hjdzStr = hjdz.split(",");
								for (String str : hjdzStr) {
									buildPersonDetailedByInit(mainInfoId, sjlx, str, ajbh, jzrybh, date, personInfoDetailedList,detailedWybhSet);
								}
							}
							if (StringUtils.isNotBlank(weChat)) {
								sjlx = Dictionary.比对数据类型_微信;
								weChat = weChat.replaceAll("，", ",");
								String[] weChatStr = weChat.split(",");
								for (String str : weChatStr) {
									buildPersonDetailedByInit(mainInfoId, sjlx, str, ajbh, jzrybh, date, personInfoDetailedList,detailedWybhSet);
								}
							}
							if (StringUtils.isNotBlank(jd)) {
								sjlx = Dictionary.比对数据类型_京东;
								jd = jd.replaceAll("，", ",");
								String[] jdStr = jd.split(",");
								for (String str : jdStr) {
									buildPersonDetailedByInit(mainInfoId, sjlx, str, ajbh, jzrybh, date, personInfoDetailedList,detailedWybhSet);
								}
							}
							if (StringUtils.isNotBlank(tb)) {
								sjlx = Dictionary.比对数据类型_淘宝;
								tb = tb.replaceAll("，", ",");
								String[] tbStr = tb.split(",");
								for (String str : tbStr) {
									buildPersonDetailedByInit(mainInfoId, sjlx, str, ajbh, jzrybh, date, personInfoDetailedList,detailedWybhSet);
								}
							}
							if (StringUtils.isNotBlank(jd1)) {
								sjlx = Dictionary.比对数据类型_经度;
								jd1 = jd1.replaceAll("，", ",");
								String[] jd1Str = jd1.split(",");
								for (String str : jd1Str) {
									buildPersonDetailedByInit(mainInfoId, sjlx, str, ajbh, jzrybh, date, personInfoDetailedList,detailedWybhSet);
								}
							}
							if (StringUtils.isNotBlank(wd)) {
								sjlx = Dictionary.比对数据类型_纬度;
								wd = wd.replaceAll("，", ",");
								String[] wdStr = wd.split(",");
								for (String str : wdStr) {
									buildPersonDetailedByInit(mainInfoId, sjlx, str, ajbh, jzrybh, date, personInfoDetailedList,detailedWybhSet);
								}
							}
							if (StringUtils.isNotBlank(imei)) {
								sjlx = Dictionary.比对数据类型_IMEI;
								imei = imei.replaceAll("，", ",");
								String[] imeiStr = imei.split(",");
								for (String str : imeiStr) {
									buildPersonDetailedByInit(mainInfoId, sjlx, str, ajbh, jzrybh, date, personInfoDetailedList,detailedWybhSet);
								}
							}
							if (StringUtils.isNotBlank(imsi)) {
								sjlx = Dictionary.比对数据类型_IMSI;
								imsi = imsi.replaceAll("，", ",");
								String[] imsiStr = imsi.split(",");
								for (String str : imsiStr) {
									buildPersonDetailedByInit(mainInfoId, sjlx, str, ajbh, jzrybh, date, personInfoDetailedList,detailedWybhSet);
								}
							}
							if (StringUtils.isNotBlank(mac)) {
								sjlx = Dictionary.比对数据类型_MAC;
								mac = mac.replaceAll("，", ",");
								String[] macStr = mac.split(",");
								for (String str : macStr) {
									buildPersonDetailedByInit(mainInfoId, sjlx, str, ajbh, jzrybh, date, personInfoDetailedList,detailedWybhSet);
								}
							}
							if (StringUtils.isNotBlank(cph)) {
								sjlx = Dictionary.比对数据类型_车牌号;
								cph = cph.replaceAll("，", ",");
								String[] cphStr = cph.split(",");
								for (String str : cphStr) {
									buildPersonDetailedByInit(mainInfoId, sjlx, str, ajbh, jzrybh, date, personInfoDetailedList,detailedWybhSet);
								}
							}
						}
					}
					//任务编号与碰撞编号生成规则相同
					//String taskBh = MD5.md5(sfzh + ajbh);
					String taskBh = MD5.md5(xm + ajbh);
					pzphSet.add(taskBh);
					//pzphSet.add(sfzh);
					dnaTaskInfo = new DnaTaskInfo();
					dnaTaskInfo.setDcd_rwbh(taskBh);
					// dnaTaskInfo.setDcd_dnaid(dnaId);
					dnaTaskInfo.setDcd_ajbh(ajbh);
					dnaTaskInfo.setDcd_rwkssj(date);
					dnaTaskInfo.setDcd_rwzxsj(date);
					dnaTaskInfo.setDcd_rysjbh(jzrybh);
					dnaTaskInfo.setDcd_rwzt(Dictionary.比对任务状态_获取指纹);
					//dnaTaskInfo.setDcd_dnaid(sfzh);
					dnaTaskInfoMap.put(taskBh, dnaTaskInfo);
					//dnaTaskInfoMap.put(sfzh, dnaTaskInfo);
					
					dnaTaskDetailedInfo = new DnaTaskDetailedInfo();
					dnaTaskDetailedInfo.setDcd_zrwid(taskBh);
					dnaTaskDetailedInfo.setDcd_wybh(UUID.randomUUID());
					dnaTaskDetailedInfo.setDcd_rwzxsj(date);
					dnaTaskDetailedInfo.setDcd_rwxxzt(Dictionary.比对任务状态_获取指纹);
					dnaTaskDetailedInfo.setDcd_czr("系统");
					dnaTaskDetailedInfo.setDcd_pzbh(taskBh);
					dnaTaskInfoDetailedMap.put(taskBh, dnaTaskDetailedInfo);
					//dnaTaskInfoDetailedMap.put(sfzh, dnaTaskDetailedInfo);
				}
			}
		}
		
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		if(personInfoDetailedList !=null){
			System.out.println("-------------------------------------------人员子表list数量【"+personInfoDetailedList.size()+"】--------------------------------------------------------------------");
		}
        System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		
		Map<String, PersonCollisonInfo> newPersonCollisonInfoMap = new HashMap<String,PersonCollisonInfo>();
		//查询已存在的碰撞信息
		Map<String, PersonCollisonInfo> storePersonCollisonInfoMap = new HashMap<String,PersonCollisonInfo>();
		if(pzphSet.size() > 0){
			dataQuery = DataQuery.create(PersonCollisonInfo.tableName);
			//dataQuery.addSelectField(PzryxxFields.field_PZBH);
			dataQuery.add(DataCriteria.in(PzryxxFields.field_PZBH, pzphSet));
			//dataQuery.add(DataCriteria.in(PzryxxFields.field_SFZH, pzphSet));
			batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
			batchQuery.getDataQuery().setLimit(1, maxNum);
			batchQuery.getTotal();
			while(batchQuery.hasNext()){
				List<Map<String, Object>> dataList = batchQuery.next();
				for (Map<String, Object> map : dataList) {
					String pzbh = (String)map.get(PzryxxFields.field_PZBH);
					//String pzbh = (String)map.get(PzryxxFields.field_SFZH);
					personCollisonInfo = (PersonCollisonInfo) BeanUitl.convertMap(PersonCollisonInfo.class, map);
					storePersonCollisonInfoMap.put(pzbh, personCollisonInfo);
					//移除已存在的任务日志
					dnaTaskInfoMap.remove(pzbh);
					dnaTaskInfoDetailedMap.remove(pzbh);
				}
			}
		}
		
		Set<Entry<String, DnaTaskInfo>> dnaTaskInfoEntrySet = dnaTaskInfoMap.entrySet();
		for (Entry<String, DnaTaskInfo> e : dnaTaskInfoEntrySet) {
			taskList.add(e.getValue());
		}
		Set<Entry<String, DnaTaskDetailedInfo>> dnaTaskInfoDetailedEntrySet = dnaTaskInfoDetailedMap.entrySet();
		for (Entry<String, DnaTaskDetailedInfo> e : dnaTaskInfoDetailedEntrySet) {
			taskDetailedList.add(e.getValue());
		}
		
		// 记录数据录入日志
		dnaTaskInfoService.saveTaskLog(taskList);
		dnaTaskInfoService.saveTaskDetailedLog(taskDetailedList);

		endTime = System.currentTimeMillis();
		System.out.println("--------------------【查询嫌疑人】结束，共耗时：" + (endTime - beginTime) / 1000 + "秒------------------------");
		System.out.println("");
		System.out.println("");
		
		if(detailedWybhSet.size() > 0){
			//防止重新初始化已有的数据
			dataQuery = DataQuery.create(PersonInfoDetailedFields.tableName);
			dataQuery.add(DataCriteria.in(PersonInfoDetailedFields.field_DCD_WYBH, detailedWybhSet));
			batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
			batchQuery.getDataQuery().setLimit(1, maxNum);
			int total = batchQuery.getTotal();
			while(batchQuery.hasNext()){
				List<Map<String, Object>> dataList = batchQuery.next();
				for (Map<String, Object> map : dataList) {
					String wybh = (String)map.get(PersonInfoDetailedFields.field_DCD_WYBH);
					detailedWybhSet.remove(wybh);
				}
			}
			if(total <= 0){
				detailedWybhSet = new HashSet<>();
			}
		}

		// 保存人员主表信息
		List<PersonInfoMian> personInfoMianList = new ArrayList<>(personInfoMianMap.values());
		List<Map<String, Object>> savepersonInfoMianMapList = BeanUitl.beanToMaps(personInfoMianList);
		DirInfo personInfoMianDirInfo = dirInfoQueryApi.getDirInfoByTableName(PersonInfoMian.tableName, ZygxEnums.dirItems);
		if(savepersonInfoMianMapList != null){
			saveDataApi.saveOrUpdateDirInfoData(personInfoMianDirInfo, savepersonInfoMianMapList, false, userInfo, null);
		}

	/*	Map<String, List<Map<String, Object>>> storePersonDetailedMap = new HashMap<String, List<Map<String, Object>>>();
		List<Map<String, Object>> storePersonDetailedList = null;
		String storePersonDetailedMd5Key = "";*/
		//主表数据编号为val  ，detailedGroupMap为子map ，detailedGroupList为子map val
		/*Map<String, Map<String,List<Map<String, Object>>>> storePersonDetailedMapByZbbh = new HashMap<>();
		List<Map<String, Object>> detailedGroupList = null;
		Map<String, List<Map<String, Object>>> detailedGroupMap = null;*/
		// 保存人员子表详细信息
		List<PersonInfoDetailed> savePersonInfoDetailedList = new ArrayList<>();
		Set<Entry<String, List<PersonInfoDetailed>>> entrySet = personInfoDetailedMap.entrySet();
		for (Entry<String, List<PersonInfoDetailed>> e : entrySet) {
			List<PersonInfoDetailed> detailedList = e.getValue();
			for (PersonInfoDetailed personInfoDetailed : detailedList) {
				String dcd_wybh = personInfoDetailed.getDcd_wybh();
				if(detailedWybhSet.size() == 0){
					savePersonInfoDetailedList.add(personInfoDetailed);
				}else{
					if(!detailedWybhSet.contains(dcd_wybh)){
						savePersonInfoDetailedList.add(personInfoDetailed);
					}
				}
			}
		}
		List<Map<String, Object>> savePersonInfoDetailedMapList = BeanUitl.beanToMaps(savePersonInfoDetailedList);
		DirInfo personInfoDetailedDirInfo = dirInfoQueryApi.getDirInfoByTableName(PersonInfoDetailed.tableName, ZygxEnums.dirItems);
		saveDataApi.saveOrUpdateDirInfoData(personInfoDetailedDirInfo, savePersonInfoDetailedMapList, false, userInfo, null);
		
		// Set<String> addSfzSet = personInfoMianMap.keySet();
		//对人员子表详细信息分组
		if(ajbhList.size() > 0){
			dataQuery = DataQuery.create(PersonInfoDetailed.tableName);
			if(personAjbhMap.size() > 0){
				dataQuery.add(DataCriteria.in(PersonInfoDetailedFields.field_DCD_ZBSJBH, personAjbhMap.keySet()));
				//dataQuery.add(DataCriteria.in(PersonInfoDetailedFields.field_DCD_AJBH, ajbhList));
				dataQuery.add(DataCriteria.not(DataCriteria.eq(PersonInfoDetailedFields.field_DCD_SJLX, Dictionary.比对数据来源_系统碰撞)));
				batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
				batchQuery.getDataQuery().setLimit(1, maxNum);
				batchQuery.getTotal();
				while (batchQuery.hasNext()) {
					List<Map<String, Object>> dataList = batchQuery.next();
					for (Map<String, Object> map : dataList) {
						String zbsjbh = (String) map.get(PersonInfoDetailedFields.field_DCD_ZBSJBH);
						ajbh = (String) map.get(PersonInfoDetailedFields.field_DCD_AJBH);
						detailedGroupMap = storePersonDetailedMapByZbbh.get(zbsjbh);
						if(detailedGroupMap == null){
							detailedGroupMap = new HashMap<>();
							storePersonDetailedMapByZbbh.put(zbsjbh, detailedGroupMap);
						}
						detailedGroupList =detailedGroupMap.get(ajbh);
						if(detailedGroupList == null){
							detailedGroupList = new ArrayList<Map<String, Object>>();
							detailedGroupMap.put(ajbh, detailedGroupList);
						}
						detailedGroupList.add(map);
					}
				}
			}
		}
		
		// 保存碰撞人员信息-start
		Map<String, Object> caseInfoMap = null;
		Map<String, Map<String, Set<String>>> valGroupMap = new HashMap<String, Map<String, Set<String>>>();
		List<PersonCollisonInfo> perCollisonInfoList = new ArrayList<PersonCollisonInfo>();
		Map<String, Set<String>> valMap = null;
		Set<String> valSet = null;
		Set<String> autoAllotCollisonInfoSet = new HashSet<>();
		//自动办结set
		Set<String> autoBjSet = new HashSet<>();
		
		//Set<Entry<String, Map<String, List<Map<String, Object>>>>> storePersonDetailedMapByZbbhEntrySet = storePersonDetailedMapByZbbh.entrySet();
		Set<Entry<String, List<String>>> personAjbhEntrySet = personAjbhMap.entrySet();
		for (Entry<String, List<String>> es : personAjbhEntrySet) {
			String zbsjbh = es.getKey();
			personInfoMian = personInfoMianMap.get(zbsjbh);
			personAjbhList = es.getValue();
			for (String personAjbh : personAjbhList) {
				ajbh = personAjbh;
				
				if(personInfoMian != null){
					sfzh = personInfoMian.getDcd_sfz();
					//如果身份证不存在set中，才组装
					if(!noNeedToAddSfzh.contains(sfzh)){
						rybhGroupMap = rybhAllGroupMap.get(ajbh);
						jzrybh = rybhGroupMap.get(sfzh);
						
						valMap = valGroupMap.get(jzrybh);
						if (valMap == null) {
							valMap = new HashMap<String, Set<String>>();
							valGroupMap.put(jzrybh, valMap);
						}
						
						detailedGroupMap = storePersonDetailedMapByZbbh.get(zbsjbh);
						if(detailedGroupMap != null){
							detailedGroupList = detailedGroupMap.get(ajbh);
							if(detailedGroupList == null){
								Collection<List<Map<String, Object>>> values = detailedGroupMap.values();
								for (List<Map<String, Object>> list : values) {
									detailedGroupList = list;
									if(detailedGroupList != null){
										for (Map<String, Object> map : detailedGroupList) {
											if(ajbh.equals(map.get(PersonInfoDetailedFields.field_DCD_AJBH))){
												sjlx = (String) map.get(PersonInfoDetailedFields.field_DCD_SJLX);
												valSet = valMap.get(sjlx);
												if (valSet == null) {
													valSet = new HashSet<String>();
													valMap.put(sjlx, valSet);
												}
												valSet.add((String) map.get(PersonInfoDetailedFields.field_DCD_SJZ));
											}
										}
									}
								}
							}else{
								for (Map<String, Object> map : detailedGroupList) {
									if(ajbh.equals(map.get(PersonInfoDetailedFields.field_DCD_AJBH))){
										sjlx = (String) map.get(PersonInfoDetailedFields.field_DCD_SJLX);
										valSet = valMap.get(sjlx);
										if (valSet == null) {
											valSet = new HashSet<String>();
											valMap.put(sjlx, valSet);
										}
										valSet.add((String) map.get(PersonInfoDetailedFields.field_DCD_SJZ));
									}
								}
							}
						}
						
						xm = personInfoMian.getDcd_xm();
						String pzbh = MD5.md5(xm + ajbh);
						//已存在的只更新 手机 QQ 微信 IMEI IMSI MAC 地址 字段，其他不改变
						//personCollisonInfo = storePersonCollisonInfoMap.get(pzbh);
						personCollisonInfo = storePersonCollisonInfoMap.get(pzbh);
						if(personCollisonInfo == null){
							personCollisonInfo = new PersonCollisonInfo();
							personCollisonInfo.setDcd_sfzh(sfzh);
							personCollisonInfo.setDcd_xm(xm);
							personCollisonInfo.setDcd_ajbh(ajbh);
							personCollisonInfo.setDcd_pzbh(pzbh);
						}
						if(ajbh.startsWith("J") || ajbh.startsWith("j")){
							caseInfoMap = jqCaseInfoMapByAjbh.get(ajbh);
							if(caseInfoMap != null){
								String sldw = (String)caseInfoMap.get(DgAlertFields.field_SLJJDW);
								if(StringUtils.isNotBlank(sldw)){
									if(sldw.length() > 6){
										sldw = sldw.substring(0, 6);
									}
									personCollisonInfo.setDcd_sldwbh(sldw);
									org = orgMap.get(sldw + "000000");
									if(org != null){
										personCollisonInfo.setDcd_sldw(org.getName());
									}
									autoAllotCollisonInfoSet.add(pzbh);
									//autoAllotCollisonInfoSet.add(sfzh);
									if(!storePersonCollisonInfoMap.containsKey(pzbh)){
										personCollisonInfo.setDcd_fpsj(date);
										personCollisonInfo.setDcd_lqxsyts(3);
										personCollisonInfo.setDcd_ysbz(Dictionary.颜色标志_绿色);
									}
									/*if(!storePersonCollisonInfoMap.containsKey(sfzh)){
										personCollisonInfo.setDcd_fpsj(date);
										personCollisonInfo.setDcd_lqxsyts(3);
										personCollisonInfo.setDcd_ysbz(Dictionary.颜色标志_绿色);
									}*/
								}
							}
						}else{
							caseInfoMap = caseInfoMapByAjbh.get(ajbh);
							if(caseInfoMap != null){
								String sldw = (String)caseInfoMap.get(DgCaseFields.SLJSDW);
								if(StringUtils.isNotBlank(sldw)){
									if(sldw.length() > 6){
										sldw = sldw.substring(0, 6);
									}
									personCollisonInfo.setDcd_sldwbh(sldw);
									org = orgMap.get(sldw + "000000");
									if(org != null){
										personCollisonInfo.setDcd_sldw(org.getName());
									}
									autoAllotCollisonInfoSet.add(pzbh);
									//autoAllotCollisonInfoSet.add(sfzh);
									if(caseInfoMap.get(DgCaseFields.JA_JASJ) != null && StringUtils.isNotBlank(caseInfoMap.get(DgCaseFields.JA_JASJ).toString())){
										autoBjSet.add(pzbh);
										//autoBjSet.add(sfzh);
									}else if(caseInfoMap.get(DgCaseFields.PASJ) != null && StringUtils.isNotBlank(caseInfoMap.get(DgCaseFields.PASJ).toString())){
										autoBjSet.add(pzbh);
										//autoBjSet.add(sfzh);
									}else{
										if(!storePersonCollisonInfoMap.containsKey(pzbh)){
											personCollisonInfo.setDcd_fpsj(date);
											personCollisonInfo.setDcd_lqxsyts(3);
											personCollisonInfo.setDcd_ysbz(Dictionary.颜色标志_绿色);
										}
										/*if(!storePersonCollisonInfoMap.containsKey(sfzh)){
											personCollisonInfo.setDcd_fpsj(date);
											personCollisonInfo.setDcd_lqxsyts(3);
											personCollisonInfo.setDcd_ysbz(Dictionary.颜色标志_绿色);
										}*/
									}
								}
							}
						}

						valSet = valMap.get(Dictionary.比对数据类型_QQ);
						qq = "";
						if (valSet != null) {
							for (String val : valSet) {
								qq += val + ";";
							}
							//qq.replaceAll("，", ",");
						}

						if (StringUtils.isNotBlank(qq)) {
							if (qq.lastIndexOf(";") > -1) {
								qq = qq.substring(0, qq.length() - 1);
							}
						}

						valSet = valMap.get(Dictionary.比对数据类型_手机号);
						lxdh = "";
						if (valSet != null) {
							for (String val : valSet) {
								lxdh += val + ";";
							}
							//lxdh.replaceAll("，", ",");
						}

						if (StringUtils.isNotBlank(lxdh)) {
							if (lxdh.lastIndexOf(";") > -1) {
								lxdh = lxdh.substring(0, lxdh.length() - 1);
							}
						}

						valSet = valMap.get(Dictionary.比对数据类型_地址);
						hjdz = "";
						if (valSet != null) {
							for (String val : valSet) {
								hjdz += val + ";";
							}
						//	hjdz.replaceAll("，", ",");
						}

						if (StringUtils.isNotBlank(hjdz)) {
							if (hjdz.lastIndexOf(";") > -1) {
								hjdz = hjdz.substring(0, hjdz.length() - 1);
							}
						}

						valSet = valMap.get(Dictionary.比对数据类型_微信);
						weChat = "";
						if (valSet != null) {
							for (String val : valSet) {
								weChat += val + ";";
							}
							//weChat.replaceAll("，", ",");
						}

						if (StringUtils.isNotBlank(weChat)) {
							if (weChat.lastIndexOf(";") > -1) {
								weChat = weChat.substring(0, weChat.length() - 1);
							}
						}
						
						valSet = valMap.get(Dictionary.比对数据类型_IMEI);
						imei = "";
						if (valSet != null) {
							for (String val : valSet) {
								imei += val + ";";
							}
							//imei.replaceAll("，", ",");
						}

						if (StringUtils.isNotBlank(imei)) {
							if (imei.lastIndexOf(";") > -1) {
								imei = imei.substring(0, imei.length() - 1);
							}
						}
						
						valSet = valMap.get(Dictionary.比对数据类型_IMSI);
						imsi = "";
						if (valSet != null) {
							for (String val : valSet) {
								imsi += val + ";";
							}
							//imsi.replaceAll("，", ",");
						}

						if (StringUtils.isNotBlank(imsi)) {
							if (imsi.lastIndexOf(";") > -1) {
								imsi = imsi.substring(0, imsi.length() - 1);
							}
						}
						
						valSet = valMap.get(Dictionary.比对数据类型_MAC);
						mac = "";
						if (valSet != null) {
							for (String val : valSet) {
								mac += val + ";";
							}
							//mac.replaceAll("，", ",");
						}

						if (StringUtils.isNotBlank(mac)) {
							if (mac.lastIndexOf(";") > -1) {
								mac = mac.substring(0, mac.length() - 1);
							}
						}
						
						valSet = valMap.get(Dictionary.比对数据类型_京东);
						jd = "";
						if (valSet != null) {
							for (String val : valSet) {
								jd += val + ";";
							}
							//jd.replaceAll("，", ",");
						}
						if (StringUtils.isNotBlank(jd)) {
							if (jd.lastIndexOf(";") > -1) {
								jd = jd.substring(0, jd.length() - 1);
							}
						}
						
						valSet = valMap.get(Dictionary.比对数据类型_淘宝);
						tb = "";
						if (valSet != null) {
							for (String val : valSet) {
								tb += val + ";";
							}
							//tb.replaceAll("，", ",");
						}
						if (StringUtils.isNotBlank(tb)) {
							if (tb.lastIndexOf(";") > -1) {
								tb = tb.substring(0, tb.length() - 1);
							}
						}
						
						
						valSet = valMap.get(Dictionary.比对数据类型_经度);
						jd1 = "";
						if (valSet != null) {
							for (String val : valSet) {
								jd1 += val + ";";
							}
							//jd1.replaceAll("，", ",");
						}
						if (StringUtils.isNotBlank(jd1)) {
							if (jd1.lastIndexOf(";") > -1) {
								jd1 = jd1.substring(0, jd1.length() - 1);
							}
						}
						
						
						valSet = valMap.get(Dictionary.比对数据类型_纬度);
						wd = "";
						if (valSet != null) {
							for (String val : valSet) {
								wd += val + ";";
							}
							//wd.replaceAll("，", ",");
						}
						if (StringUtils.isNotBlank(wd)) {
							if (wd.lastIndexOf(";") > -1) {
								wd = wd.substring(0, wd.length() - 1);
							}
						}
						
						valSet = valMap.get(Dictionary.比对数据类型_车牌号);
						cph = "";
						if (valSet != null) {
							for (String val : valSet) {
								cph += val + ";";
							}
							//wd.replaceAll("，", ",");
						}
						if (StringUtils.isNotBlank(cph)) {
							if (cph.lastIndexOf(";") > -1) {
								cph = cph.substring(0, cph.length() - 1);
							}
						}
						

						personCollisonInfo.setDcd_qq(qq);
						personCollisonInfo.setDcd_sjh(lxdh);
						/*if(storePersonCollisonInfoMap.containsKey(pzbh)){
							personCollisonInfo.setDcd_dz(hjdz);
						}*/
						if(storePersonCollisonInfoMap.containsKey(sfzh)){
							personCollisonInfo.setDcd_dz(hjdz);
						}
						personCollisonInfo.setDcd_wx(weChat);
						personCollisonInfo.setDcd_imei(imei);
						personCollisonInfo.setDcd_imsi(imsi);
						personCollisonInfo.setDcd_mac(mac);
						personCollisonInfo.setDcd_dz(hjdz);
						personCollisonInfo.setDcd_jd(jd);
						personCollisonInfo.setDcd_tb(tb);
						personCollisonInfo.setDcd_jd0(jd1);
						personCollisonInfo.setDcd_wd(wd);
						personCollisonInfo.setXzd_cph(cph);
						if(!storePersonCollisonInfoMap.containsKey(pzbh)){
							personCollisonInfo.setDcd_sjzt(Dictionary.比对结果_初始结果);
							//personCollisonInfo.setDcd_sjzt(Dictionary.比对结果_推送网监);
						}
						/*if(!storePersonCollisonInfoMap.containsKey(sfzh)){
							personCollisonInfo.setDcd_sjzt(Dictionary.比对结果_初始结果);
							//personCollisonInfo.setDcd_sjzt(Dictionary.比对结果_推送网监);
						}*/
						if(autoBjSet.contains(pzbh)){
							personCollisonInfo.setDcd_sjzt(Dictionary.比对结果_办结);
						}
						/*if(autoBjSet.contains(sfzh)){
							personCollisonInfo.setDcd_sjzt(Dictionary.比对结果_办结);
						}*/
						
						newPersonCollisonInfoMap.put(pzbh, personCollisonInfo);
						//newPersonCollisonInfoMap.put(sfzh, personCollisonInfo);
						perCollisonInfoList.add(personCollisonInfo);
					}
				}
			}
		}
		
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
        System.out.println("-------------------------------------------碰撞人员list数量【"+perCollisonInfoList.size()+"】--------------------------------------------------------------------");
        System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
        List<Map<String, Object>> savePerCollisonInfoList = BeanUitl.beanToMaps(perCollisonInfoList);
		DirInfo personCollisonInfoDirInfo = dirInfoQueryApi.getDirInfoByTableName(PersonCollisonInfo.tableName, ZygxEnums.dirItems);
		Set<DirItem> personCollisonDirItems = personCollisonInfoDirInfo.getDirItems();
		DirItem dirItem =null;
		Map<String, DirItem> personCollisonDirItemMap = DirItemHolder.getDirItemMapByFieldName(personCollisonDirItems);
		saveDataApi.saveOrUpdateDirInfoData(personCollisonInfoDirInfo, savePerCollisonInfoList, false, userInfo, null);
		// 保存碰撞人员信息-end

		taskDetailedList = new ArrayList<DnaTaskDetailedInfo>();
		// 记录任务日志
		date = new Date();
		date.setSeconds(date.getSeconds() + 1);
		Date fpDate = new Date();
		fpDate.setSeconds(fpDate.getSeconds() + 2);
		Date bjDate = new Date();
		bjDate.setSeconds(bjDate.getSeconds() + 3);
		for (DnaTaskInfo task : taskList) {
			jzrybh = task.getDcd_rysjbh();
			ajbhGroupSet = middlePersonMapByRybh.get(jzrybh);
			//ajbh = middlePersonMapByRybh.get(jzrybh);
			if (StringUtils.isBlank(task.getDcd_ajbh())) {
				if(ajbhGroupSet != null){
					for (String curAjbh : ajbhGroupSet) {
						task.setDcd_ajbh(curAjbh);
					}
				}
			}
			/*
			 * if(StringUtils.isBlank(task.getDcd_ajbh())){
			 * task.setDcd_ajbh(ajbh); }
			 */
			String taskBh = task.getDcd_rwbh();
			sfzh = task.getDcd_dnaid();
			task.setDcd_rwzt(Dictionary.比对任务状态_碰撞完毕);
			task.setDcd_rwzxsj(date);
			//如果不存在旧数据，即不是第一次碰撞的结果
			if(!storePersonCollisonInfoMap.containsKey(taskBh)){
				personCollisonInfo = newPersonCollisonInfoMap.get(taskBh);
				
				dnaTaskDetailedInfo = new DnaTaskDetailedInfo();
				dnaTaskDetailedInfo.setDcd_zrwid(taskBh);
				dnaTaskDetailedInfo.setDcd_wybh(UUID.randomUUID());
				dnaTaskDetailedInfo.setDcd_rwzxsj(date);
				dnaTaskDetailedInfo.setDcd_rwxxzt(Dictionary.比对任务状态_碰撞完毕);
				dnaTaskDetailedInfo.setDcd_czr("系统");
				dnaTaskDetailedInfo.setDcd_pzbh(taskBh);
				taskDetailedList.add(dnaTaskDetailedInfo);
				
				if(autoAllotCollisonInfoSet.contains(taskBh)){
					dnaTaskDetailedInfo = new DnaTaskDetailedInfo();
					dnaTaskDetailedInfo.setDcd_zrwid(taskBh);
					dnaTaskDetailedInfo.setDcd_wybh(UUID.randomUUID());
					dnaTaskDetailedInfo.setDcd_rwzxsj(fpDate);
					dnaTaskDetailedInfo.setDcd_rwxxzt(Dictionary.比对任务状态_自动分配);
					dnaTaskDetailedInfo.setDcd_czr("系统");
					dnaTaskDetailedInfo.setDcd_pzbh(taskBh);
					dirItem = personCollisonDirItemMap.get(PzryxxFields.field_SLDW);
					dnaTaskInfoService.buildChangeInfo(dirItem.getItemName(), "", personCollisonInfo.getDcd_sldw(), dnaTaskDetailedInfo);
					dirItem = personCollisonDirItemMap.get(PzryxxFields.field_SLDWBH);
					dnaTaskInfoService.buildChangeInfo(dirItem.getItemName(), "", personCollisonInfo.getDcd_sldwbh(), dnaTaskDetailedInfo);
					taskDetailedList.add(dnaTaskDetailedInfo);
				}
				
				//自动办结处理
				if(autoBjSet.contains(taskBh)){
					task.setDcd_rwzt(Dictionary.比对任务状态_办结);
					task.setDcd_rwzxsj(bjDate);
					
					dnaTaskDetailedInfo = new DnaTaskDetailedInfo();
					dnaTaskDetailedInfo.setDcd_zrwid(taskBh);
					dnaTaskDetailedInfo.setDcd_wybh(UUID.randomUUID());
					dnaTaskDetailedInfo.setDcd_rwzxsj(bjDate);
					dnaTaskDetailedInfo.setDcd_rwxxzt(Dictionary.比对任务状态_办结);
					dnaTaskDetailedInfo.setDcd_czr("系统");
					dnaTaskDetailedInfo.setDcd_pzbh(taskBh);
					taskDetailedList.add(dnaTaskDetailedInfo);
				}
			}
		}

		// 记录数据录入日志
		dnaTaskInfoService.updateTaskLog(taskList);
		dnaTaskInfoService.saveTaskDetailedLog(taskDetailedList);

		long endTime1 = System.currentTimeMillis();
		System.out.println("--------------------【DNA碰撞任务】结束，共耗时：" + (endTime1 - beginTime1) / 1000 + "秒------------------------");
		System.out.println("");
		System.out.println("");
	}
	
	//二次比中，不是优先比中数据处理，不会生成碰撞结果
	public void secondDataCollision(List<DnaInfo> dnaInfoList) throws Exception{
		long beginTime1 = System.currentTimeMillis();
		System.out.println("");
		System.out.println("");
		System.err.println("--------------------【DNA二次碰撞任务开始】开始------------------------");

		UserInfo userInfo = AssistUtils.getCurrUserInfo();
		String ajbh = "";
		String sfzh = "";
		String jzrybh = "";

		long beginTime = System.currentTimeMillis();
		System.out.println("");
		System.out.println("");
		System.err.println("--------------------【组装案件编号，人员编号，身份证条件】开始------------------------");
		List<String> ajbhList = new ArrayList<String>();
		List<String> jqAjbhList = new ArrayList<String>();
		List<String> rybhList = new ArrayList<String>();
		List<String> sfzList = new ArrayList<String>();
		
		Map<String,DnaInfo> dnaMapByAjbh = new HashMap<>();
		Map<String,DnaInfo> dnaMapsBySfzh = new HashMap<>();
		for (DnaInfo dnaInfo : dnaInfoList) {
			sfzh = dnaInfo.getSfzh();
			if (StringUtils.isNotBlank(sfzh)) {
				if(!dnaMapsBySfzh.containsKey(sfzh)){
					dnaMapsBySfzh.put(sfzh, dnaInfo);
				}
				
				if (!sfzList.contains(sfzh)) {
					sfzList.add(sfzh);
				}
			}
			//警综案件编号
			ajbh = dnaInfo.getJzajbh();
			if (StringUtils.isNotBlank(ajbh)) {
				if (!ajbhList.contains(ajbh)) {
					ajbhList.add(ajbh);
				}
			}
			dnaMapByAjbh.put(ajbh, dnaInfo);
			//物证案件编号
			String wzAjbh = dnaInfo.getWzajbh();
			if (StringUtils.isNotBlank(wzAjbh)) {
				if (!ajbhList.contains(wzAjbh)) {
					ajbhList.add(wzAjbh);
				}
			}
			dnaMapByAjbh.put(wzAjbh, dnaInfo);
			
			//警综人员编号
			jzrybh = dnaInfo.getJzrybh();
			if (StringUtils.isNotBlank(jzrybh)) {
				if (!rybhList.contains(jzrybh)) {
					rybhList.add(jzrybh);
				}
			}
			//样本人员编号
			String ryybbh = dnaInfo.getRyybbh();
			if (StringUtils.isNotBlank(ryybbh)) {
				if (!rybhList.contains(ryybbh)) {
					rybhList.add(ryybbh);
				}
			}
		}

		long endTime = System.currentTimeMillis();
		System.out.println("--------------------【组装案件编号，人员编号，身份证条件】结束，共耗时：" + (endTime - beginTime) / 1000 + "秒------------------------");
		System.out.println("");
		System.out.println("");
		
		//先查一次嫌疑人，获取相关嫌疑人编号
		if(sfzList.size() > 0){
			DataQuery dataQuerySfz = DataQuery.create(XyrxxFields.tableName);
			dataQuerySfz.add(DataCriteria.in(XyrxxFields.field_ZJHM, sfzList));
			BatchQueryImpl batchQuerySfz = BatchQueryImpl.newInstance(dataQuerySfz, jdbcQuerier);
			batchQuerySfz.getDataQuery().setLimit(1, maxNum);
			batchQuerySfz.getTotal();
			while (batchQuerySfz.hasNext()) {
				List<Map<String, Object>> dataList = batchQuerySfz.next();
				for (Map<String, Object> map : dataList) {
					jzrybh = (String)map.get(XyrxxFields.field_SYSTEMID);
					if(!rybhList.contains(jzrybh)){
						rybhList.add(jzrybh);
					}
				}
			}
		}

		//Map<String, String> middlePersonMapByRybh = new HashMap<String, String>();
		Map<String, Set<String>> middlePersonMapByRybh = new HashMap<>();
		
		beginTime = System.currentTimeMillis();
		System.out.println("");
		System.out.println("");
		System.err.println("--------------------【查询人员涉案情况-中间表】开始------------------------");
		DataQuery dataQuery = DataQuery.create(XyrxxMiddFields.tableName);
		List<DataCriterion> dataCriterias = new ArrayList<DataCriterion>();
		if (rybhList.size() > 0) {
			dataCriterias.add(DataCriteria.in(XyrxxMiddFields.field_RYBH, rybhList));
		}
		if (ajbhList.size() > 0) {
			dataCriterias.add(DataCriteria.in(XyrxxMiddFields.field_AJBH, ajbhList));
		}

		dataQuery.add(DataCriteria.or(dataCriterias));

		// 先查询警综_人员涉案情况-中间表
		BatchQueryImpl batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
		batchQuery.getDataQuery().setLimit(1, maxNum);
		batchQuery.getTotal();
		Set<String> middelInfoRybhSet = new HashSet<String>();
		Set<String> ajbhGroupSet = null;
		while (batchQuery.hasNext()) {
			List<Map<String, Object>> dataList = batchQuery.next();
			for (Map<String, Object> dataMap : dataList) {
				ajbh = (String) dataMap.get(XyrxxMiddFields.field_AJBH);
				if (!ajbhList.contains(ajbh)) {
					ajbhList.add(ajbh);
				}
				if(ajbh.startsWith("J") || ajbh.startsWith("j")){
					if (!jqAjbhList.contains(ajbh)) {
						jqAjbhList.add(ajbh);
					}
				}
				
				jzrybh = (String) dataMap.get(XyrxxMiddFields.field_RYBH);
				//middlePersonMapByRybh.put(jzrybh, ajbh);
				ajbhGroupSet = middlePersonMapByRybh.get(jzrybh);
				if(ajbhGroupSet == null){
					ajbhGroupSet = new HashSet<>();
					middlePersonMapByRybh.put(jzrybh, ajbhGroupSet);
				}
				ajbhGroupSet.add(ajbh);
				
				middelInfoRybhSet.add(jzrybh);
			}
		}

		endTime = System.currentTimeMillis();
		System.out.println("--------------------【查询人员涉案情况-中间表】结束，共耗时：" + (endTime - beginTime) / 1000 + "秒------------------------");
		System.out.println("");
		System.out.println("");
		
		
		beginTime = System.currentTimeMillis();
		System.out.println("");
		System.out.println("");
		System.err.println("--------------------【查询案件信息表】开始------------------------");
		Map<String,Map<String,Object>> caseInfoMapByAjbh = new HashMap<>();
		Map<String,Map<String,Object>> jqCaseInfoMapByAjbh = new HashMap<>();
		dataQuery = DataQuery.create(DgCaseFields.tableName);
		//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//Date parse = simpleDateFormat.parse("2017-1-1 00:00:00");
		if (ajbhList.size() > 0) {
			dataQuery.add(DataCriteria.in(DgCaseFields.AJBH, ajbhList));
			dataQuery.addSelectField(DgCaseFields.AJBH);
			dataQuery.addSelectField(DgCaseFields.SLJSDW);
			dataQuery.addSelectField(DgCaseFields.FASJCZ);
			dataQuery.addSelectField(DgCaseFields.JA_JASJ);
			//dataQuery.add(DataCriteria.gte(DgCaseFields.FASJCZ, parse));
			batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
			batchQuery.getDataQuery().setLimit(1, maxNum);
			batchQuery.getTotal();
			while(batchQuery.hasNext()){
				List<Map<String, Object>> dataList = batchQuery.next();
				for (Map<String, Object> map : dataList) {
					caseInfoMapByAjbh.put(map.get(DgCaseFields.AJBH).toString(), map);
				}
			}
		}
		
		//查询警情信息
		dataQuery = DataQuery.create(DgAlertFields.tableName);
		if (jqAjbhList.size() > 0) {
			dataQuery.add(DataCriteria.in(DgAlertFields.field_AJBH, jqAjbhList));
			batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
			batchQuery.getDataQuery().setLimit(1, maxNum);
			batchQuery.getTotal();
			while(batchQuery.hasNext()){
				List<Map<String, Object>> dataList = batchQuery.next();
				for (Map<String, Object> map : dataList) {
					jqCaseInfoMapByAjbh.put(map.get(DgAlertFields.field_AJBH).toString(), map);
				}
			}
		}
		
		//组装机构map
		Org org = null;
		Map<String, Org> orgMap = getOrgMap();
		
		endTime = System.currentTimeMillis();
		System.out.println("--------------------【查询案件信息表】结束，共耗时：" + (endTime - beginTime) / 1000 + "秒------------------------");
		System.out.println("");
		System.out.println("");
		
		beginTime = System.currentTimeMillis();
		System.out.println("");
		System.out.println("");
		System.err.println("--------------------【查询嫌疑人】开始------------------------");
		// 再查询嫌疑人信息
		dataQuery = DataQuery.create(XyrxxFields.tableName);
		dataCriterias = new ArrayList<DataCriterion>();
		if (rybhList.size() > 0) {
			dataCriterias.add(DataCriteria.in(XyrxxFields.field_SYSTEMID, rybhList));
		}
		if (middelInfoRybhSet.size() > 0) {
			dataCriterias.add(DataCriteria.in(XyrxxFields.field_SYSTEMID, middelInfoRybhSet));
		}
		if (sfzList.size() > 0) {
			dataCriterias.add(DataCriteria.in(XyrxxFields.field_ZJHM, sfzList));
		}
		
		dataQuery.add(DataCriteria.or(dataCriterias));

		batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
		batchQuery.getDataQuery().setLimit(1, maxNum);
		batchQuery.getTotal();
		
		Set<String> searchSfzhSet = new HashSet<>();
		Set<String> secendAddSfzh = new HashSet<>();
	
		List<Map<String,Object>> allSuspectList = new ArrayList<>();
		
		Map<String,Map<String,Object>> hasDealSfzhMap = new HashMap<>();
		while (batchQuery.hasNext()) {
			List<Map<String, Object>> dataList = batchQuery.next();
			allSuspectList.addAll(dataList);
			for (Map<String, Object> map : dataList) {
				sfzh = (String) map.get(XyrxxFields.field_ZJHM);
				searchSfzhSet.add(sfzh);
			}
			if(searchSfzhSet.size() > 0){
				DataQuery dataQuerySfz = DataQuery.create(PzryxxFields.tableName);
				dataQuerySfz.add(DataCriteria.in(PzryxxFields.field_SFZH, searchSfzhSet));
				List<Map<String, Object>> storePzxxResultList = jdbcQuerier.queryForList(dataQuerySfz);
				for (Map<String, Object> map : storePzxxResultList) {
					String sjzt = (String)map.get(PzryxxFields.field_SJZT);
					sfzh = (String)map.get(PzryxxFields.field_SFZH);
					hasDealSfzhMap.put(sfzh, map);
					
					if(!Dictionary.比对结果_办结.equals(sjzt) && !Dictionary.比对结果_已排他.equals(sjzt)){
						secendAddSfzh.add(sfzh);
					}
				}
			}
		}
		
		// 姓名
		String xm = "";
		//受理单位
		//String sldw ="";
		//优先分配单位
		String yxfpdw = "";
		//优先分配单位
		String yxfpdwMc = "";
		//数据状态
		String sjzt = "";
		//优先碰撞数据ID
		String yxpzsjId=  "";
		//碰撞生成时间
		Date scDate = new Date();
		
		List<PersonCollisonInfoSide> saveSecendPzryxxList = new ArrayList<>();
		PersonCollisonInfoSide personCollisonInfoSide = null;
		Map<String,Object> storePzxxInfoMap = null;
		for (Map<String, Object> dataMap : allSuspectList) {
			jzrybh = (String) dataMap.get(XyrxxFields.field_SYSTEMID);
			sfzh = (String) dataMap.get(XyrxxFields.field_ZJHM);
			xm= (String) dataMap.get(XyrxxFields.field_XM);
			
			ajbhGroupSet = middlePersonMapByRybh.get(jzrybh);
			storePzxxInfoMap = hasDealSfzhMap.get(sfzh);
			if(storePzxxInfoMap != null){
				String storeAjbh = (String)storePzxxInfoMap.get(PzryxxFields.field_AJBH);
				if(ajbhGroupSet != null){
					for (String groupAjbh : ajbhGroupSet) {
						Map<String, Object> caseInfoMap = caseInfoMapByAjbh.get(groupAjbh);
						if(caseInfoMap != null){
							String sljsdw = (String)caseInfoMap.get(DgCaseFields.SLJSDW);
							ajbh = (String)caseInfoMap.get(DgCaseFields.AJBH);
							if(!ajbh.equals(storeAjbh)){
								if(StringUtils.isNotBlank(sljsdw)){
									if(sljsdw.length() > 6){
										sljsdw = sljsdw.substring(0, 6);
									}
								}
								personCollisonInfoSide = new PersonCollisonInfoSide();
								sjzt = (String)storePzxxInfoMap.get(PzryxxFields.field_SJZT);
								yxfpdw= (String)storePzxxInfoMap.get(PzryxxFields.field_SLDWBH);
								yxfpdwMc= (String)storePzxxInfoMap.get(PzryxxFields.field_SLDW);
								yxpzsjId= (String)storePzxxInfoMap.get(ReservedFields.ID);
								
								personCollisonInfoSide.setXzd_ajbh(ajbh);
								personCollisonInfoSide.setXzd_scsj(scDate);
								personCollisonInfoSide.setXzd_sjzt(sjzt);
								personCollisonInfoSide.setXzd_xm(xm);
								personCollisonInfoSide.setXzd_sfz(sfzh);
								personCollisonInfoSide.setXzd_yxfpdw(yxfpdw);
								personCollisonInfoSide.setXzd_yxfpdwmc(yxfpdwMc);
								personCollisonInfoSide.setXzd_sldw(sljsdw);
								org = orgMap.get(sljsdw + "000000");
								if(org != null){
									personCollisonInfoSide.setXzd_sldwmc(org.getName());
								}
								personCollisonInfoSide.setXzd_yxpzsjid(yxpzsjId);
								personCollisonInfoSide.setXzd_wybh(UUID.randomUUID());
								saveSecendPzryxxList.add(personCollisonInfoSide);
							}
						}
					}
				}
				
			}
		}
		endTime = System.currentTimeMillis();
		System.out.println("--------------------【查询二次嫌疑人】结束，共耗时：" + (endTime - beginTime) / 1000 + "秒------------------------");
		System.out.println("");
		System.out.println("");
		
		DirInfo dirInfo = dirInfoQueryApi.getDirInfoByTableName(PersonCollisonInfoSideFields.tableName, ZygxEnums.dirItems);
		if(saveSecendPzryxxList.size() > 0){
			List<Map<String, Object>> beanToMaps = BeanUitl.beanToMaps(saveSecendPzryxxList);
			saveDataApi.addDirInfoData(dirInfo, beanToMaps, false, userInfo, null);
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
	        System.out.println("-------------------------------------------碰撞副表人员list数量【"+beanToMaps.size()+"】--------------------------------------------------------------------");
	        System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
		}
		
		long endTime1 = System.currentTimeMillis();
		System.out.println("--------------------【二次DNA碰撞任务】结束，共耗时：" + (endTime1 - beginTime1) / 1000 + "秒------------------------");
		System.out.println("");
		
	}
	
	
	
	@SuppressWarnings("deprecation")
	public void dnaDataCollisionByNoSfzh(List<DnaInfo> dnaInfoByQuery) throws Exception{
		long beginTime1 = System.currentTimeMillis();
		System.out.println("");
		System.out.println("");
		System.err.println("--------------------【DNA无身份证碰撞任务开始】开始------------------------");

		UserInfo userInfo = AssistUtils.getCurrUserInfo();
		DnaTaskInfo dnaTaskInfo = null;
		DnaTaskDetailedInfo dnaTaskDetailedInfo = null;
		// String dnaId = "";
		String ajbh = "";
		Date date = null;
		String sfzh = "";
		String jzrybh = "";
		List<DnaTaskInfo> taskList = new ArrayList<DnaTaskInfo>();
		List<DnaTaskDetailedInfo> taskDetailedList = new ArrayList<DnaTaskDetailedInfo>();

		long beginTime = System.currentTimeMillis();
		System.out.println("");
		System.out.println("");
		System.err.println("--------------------【组装案件编号，人员编号，身份证条件】开始------------------------");
		List<String> ajbhList = new ArrayList<String>();
		List<String> jqAjbhList = new ArrayList<String>();
		List<String> rybhList = new ArrayList<String>();
		
		Map<String,DnaInfo> dnaMapByAjbh = new HashMap<>();
		Map<String,DnaInfo> dnaMapByXm = new HashMap<>();
		for (DnaInfo dnaInfo : dnaInfoByQuery) {
			date = dnaInfo.getAddTime();
			// dnaId = dnaInfo.getId();
			//警综案件编号
			ajbh = dnaInfo.getJzajbh();
			String xm = dnaInfo.getXm();
			if (StringUtils.isNotBlank(xm)) {
				dnaMapByXm.put(xm, dnaInfo);
			}
			if (StringUtils.isNotBlank(ajbh)) {
				if (!ajbhList.contains(ajbh)) {
					ajbhList.add(ajbh);
				}
				dnaMapByAjbh.put(ajbh, dnaInfo);
			}
			//物证案件编号
			String wzAjbh = dnaInfo.getWzajbh();
			if (StringUtils.isNotBlank(wzAjbh)) {
				if (!ajbhList.contains(wzAjbh)) {
					ajbhList.add(wzAjbh);
				}
				dnaMapByAjbh.put(wzAjbh, dnaInfo);
			}
			
			//警综人员编号
			jzrybh = dnaInfo.getJzrybh();
			if (StringUtils.isNotBlank(jzrybh)) {
				if (!rybhList.contains(jzrybh)) {
					rybhList.add(jzrybh);
				}
			}
			//样本人员编号
			String ryybbh = dnaInfo.getRyybbh();
			if (StringUtils.isNotBlank(ryybbh)) {
				if (!rybhList.contains(ryybbh)) {
					rybhList.add(ryybbh);
				}
			}
		}

		long endTime = System.currentTimeMillis();
		System.out.println("--------------------【组装案件编号，人员编号，身份证条件】结束，共耗时：" + (endTime - beginTime) / 1000 + "秒------------------------");
		System.out.println("");
		System.out.println("");
		
		//先查一次嫌疑人，获取相关嫌疑人编号
		if(rybhList.size() > 0){
			DataQuery dataQuerySfz = DataQuery.create(XyrxxFields.tableName);
			dataQuerySfz.add(DataCriteria.in(XyrxxFields.field_RYBH, rybhList));
			BatchQueryImpl batchQuerySfz = BatchQueryImpl.newInstance(dataQuerySfz, jdbcQuerier);
			batchQuerySfz.getDataQuery().setLimit(1, maxNum);
			batchQuerySfz.getTotal();
			while (batchQuerySfz.hasNext()) {
				List<Map<String, Object>> dataList = batchQuerySfz.next();
				for (Map<String, Object> map : dataList) {
					jzrybh = (String)map.get(XyrxxFields.field_SYSTEMID);
					if(!rybhList.contains(jzrybh)){
						rybhList.add(jzrybh);
					}
				}
			}
		}
				
		
		Map<String, Set<String>> middlePersonMapByRybh = new HashMap<>();
		
		beginTime = System.currentTimeMillis();
		System.out.println("");
		System.out.println("");
		System.err.println("--------------------【查询人员涉案情况-中间表】开始------------------------");
		DataQuery dataQuery = DataQuery.create(XyrxxMiddFields.tableName);
		List<DataCriterion> dataCriterias = new ArrayList<DataCriterion>();
		if (rybhList.size() > 0) {
			dataCriterias.add(DataCriteria.in(XyrxxMiddFields.field_RYBH, rybhList));
		}
		if (ajbhList.size() > 0) {
			dataCriterias.add(DataCriteria.in(XyrxxMiddFields.field_AJBH, ajbhList));
		}

		dataQuery.add(DataCriteria.or(dataCriterias));

		// 先查询警综_人员涉案情况-中间表
		BatchQueryImpl batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
		batchQuery.getDataQuery().setLimit(1, maxNum);
		batchQuery.getTotal();
		Set<String> middelInfoRybhSet = new HashSet<String>();
		Set<String> ajbhGroupSet = null;
		while (batchQuery.hasNext()) {
			List<Map<String, Object>> dataList = batchQuery.next();
			for (Map<String, Object> dataMap : dataList) {
				ajbh = (String) dataMap.get(XyrxxMiddFields.field_AJBH);
				if (!ajbhList.contains(ajbh)) {
					ajbhList.add(ajbh);
				}
				if(ajbh.startsWith("J") || ajbh.startsWith("j")){
					if (!jqAjbhList.contains(ajbh)) {
						jqAjbhList.add(ajbh);
					}
				}
				
				jzrybh = (String) dataMap.get(XyrxxMiddFields.field_RYBH);
				ajbhGroupSet = middlePersonMapByRybh.get(jzrybh);
				if(ajbhGroupSet == null){
					ajbhGroupSet = new HashSet<>();
					middlePersonMapByRybh.put(jzrybh, ajbhGroupSet);
				}
				ajbhGroupSet.add(ajbh);
				
				middelInfoRybhSet.add(jzrybh);
			}
		}

		endTime = System.currentTimeMillis();
		System.out.println("--------------------【查询人员涉案情况-中间表】结束，共耗时：" + (endTime - beginTime) / 1000 + "秒------------------------");
		System.out.println("");
		System.out.println("");
		
		
		
		beginTime = System.currentTimeMillis();
		System.out.println("");
		System.out.println("");
		System.err.println("--------------------【查询案件信息表】开始------------------------");
		Map<String,Map<String,Object>> caseInfoMapByAjbh = new LinkedHashMap<>();
		Map<String,Map<String,Object>> jqCaseInfoMapByAjbh = new HashMap<>();
		dataQuery = DataQuery.create(DgCaseFields.tableName);
		//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//Date parse = simpleDateFormat.parse("2017-1-1 00:00:00");
		if (ajbhList.size() > 0) {
			dataQuery.add(DataCriteria.in(DgCaseFields.AJBH, ajbhList));
			dataQuery.addSelectField(DgCaseFields.AJBH);
			dataQuery.addSelectField(DgCaseFields.SLJSDW);
			dataQuery.addSelectField(DgCaseFields.FASJCZ);
			dataQuery.addSelectField(DgCaseFields.JA_JASJ);
			dataQuery.addSelectField(DgCaseFields.LASJ);
			//dataQuery.add(DataCriteria.gte(DgCaseFields.FASJCZ, parse));
			batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
			batchQuery.getDataQuery().setLimit(1, maxNum);
			batchQuery.getTotal();
			while(batchQuery.hasNext()){
				List<Map<String, Object>> dataList = batchQuery.next();
				for (Map<String, Object> map : dataList) {
					caseInfoMapByAjbh.put(map.get(DgCaseFields.AJBH).toString(), map);
				}
			}
		}
		
		
		//查询警情信息
		dataQuery = DataQuery.create(DgAlertFields.tableName);
		if (jqAjbhList.size() > 0) {
			dataQuery.add(DataCriteria.in(DgAlertFields.field_AJBH, jqAjbhList));
			batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
			batchQuery.getDataQuery().setLimit(1, maxNum);
			batchQuery.getTotal();
			while(batchQuery.hasNext()){
				List<Map<String, Object>> dataList = batchQuery.next();
				for (Map<String, Object> map : dataList) {
					jqCaseInfoMapByAjbh.put(map.get(DgAlertFields.field_AJBH).toString(), map);
				}
			}
		}
		
		//组装机构map
		Org org = null;
		Map<String, Org> orgMap = getOrgMap();
		
		endTime = System.currentTimeMillis();
		System.out.println("--------------------【查询案件信息表】结束，共耗时：" + (endTime - beginTime) / 1000 + "秒------------------------");
		System.out.println("");
		System.out.println("");
		

		beginTime = System.currentTimeMillis();
		System.out.println("");
		System.out.println("");
		System.err.println("--------------------【查询嫌疑人】开始------------------------");
		// 再查询嫌疑人信息
		dataQuery = DataQuery.create(XyrxxFields.tableName);
		dataCriterias = new ArrayList<DataCriterion>();
		if (rybhList.size() > 0) {
			dataCriterias.add(DataCriteria.in(XyrxxFields.field_SYSTEMID, rybhList));
			dataCriterias.add(DataCriteria.in(XyrxxFields.field_RYBH, rybhList));
		}
		if (middelInfoRybhSet.size() > 0) {
			dataCriterias.add(DataCriteria.in(XyrxxFields.field_SYSTEMID, middelInfoRybhSet));
		}
		
		dataQuery.add(DataCriteria.or(dataCriterias));

		batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
		batchQuery.getDataQuery().setLimit(1, maxNum);
		batchQuery.getTotal();
		
		String qq = "";
		// 户籍地址
		String hjdz = "";
		// 联系电话
		String lxdh = "";
		// 手机号码1
		String sjhm1 = "";
		// 手机号码2
		String sjhm2 = "";
		// 姓名
		String xm = "";
		// 微信
		 String weChat = "";
		// imei
		String imei = "";
		// imsi
		String imsi = "";
		// mac
		String mac = "";
		// 京东
		String jd = "";
		// 淘宝
		String tb = "";
		// jd1经度
		String jd1 = "";
		//纬度
		String wd= "";
		//车牌号
		String cph= "";
		

		String sjlx = "";
		
		PersonCollisonInfo personCollisonInfo = null;
		PersonInfoMian personInfoMian = null;
		List<PersonInfoDetailed> personInfoDetailedList = null;
		Map<String, PersonInfoMian> personInfoMianMap = new HashMap<String, PersonInfoMian>();
		Map<String, List<PersonInfoDetailed>> personInfoDetailedMap = new HashMap<String, List<PersonInfoDetailed>>();
		Map<String, DnaTaskInfo> dnaTaskInfoMap = new HashMap<String,DnaTaskInfo>();
		Map<String, DnaTaskDetailedInfo> dnaTaskInfoDetailedMap = new HashMap<String,DnaTaskDetailedInfo>();
		//人员编号分组map，以案件编号为key，身份证为子map key，人员编号为子map val
		Map<String,	Map<String,String>> rybhAllGroupMap = new HashMap<>();
		Map<String, String> rybhGroupMap = null;
		
		//人员相关案件编号， 人员主表数据编号为key，案件编号为val
		Map<String, List<String>> personAjbhMap = new HashMap<String,List<String>>();
		List<String> personAjbhList = null;
		
		Set<String> pzphSet = new HashSet<String>();
		Set<String> detailedWybhSet = new HashSet<>();

		List<Map<String, Object>> allSuspectList = new ArrayList<>();
		
		//Map<String,Map<String,List<Map<String,Object>>>> ryxxMapByRybh = new HashMap<>();
		Map<String,List<Map<String,Object>>> ryxxMapByRybh = new HashMap<>();
		List<Map<String,Object>> ryxxGroupList = null;
		
		date = new Date();
		//不必新增碰撞信息的身份证号
		Set<String> noNeedToAddSfzh = new HashSet<>();
		//已办结或排他的身份证号
		Set<String> hasDealSfzh = new HashSet<>();
		//不必新增碰撞信息的碰撞编号
		Set<String> noNeedToAddPzbh = new HashSet<>();
		//不必新增碰撞信息的主表数据ID
		Set<String> noNeedToAddmainInfoIdSet =new HashSet<>();
		//已办结或排他的主表数据ID
		Set<String> hasDealmainId = new HashSet<>();
		
		
		Map<String, Map<String,Object>> hasDealSfzhMap = new HashMap<>();
		
		Map<String, Map<String,Object>> hasDealmainIdMap = new HashMap<>();
		
		Set<String> searchSfzhSet = new HashSet<>();
		Set<String> searchXmSet = new HashSet<>();
		//查出已碰撞的人员数据（未办结，未排他）
		while (batchQuery.hasNext()) {
			List<Map<String, Object>> dataList = batchQuery.next();
			allSuspectList.addAll(dataList);
			for (Map<String, Object> dataMap : dataList) {
				sfzh = (String) dataMap.get(XyrxxFields.field_ZJHM);
				jzrybh = (String) dataMap.get(XyrxxFields.field_RYBH);
				xm = (String) dataMap.get(XyrxxFields.field_XM);
				if("陆军".equals(xm)){
 					System.out.println(123);	
				}
				if(StringUtils.isNotBlank(jzrybh)){
					/*ryxxGroupMap = ryxxMapByRybh.get(jzrybh);
					if(ryxxGroupMap == null){
						ryxxGroupMap = new HashMap<>();
						ryxxMapByRybh.put(jzrybh, ryxxGroupMap);
					}*/
					ryxxGroupList = ryxxMapByRybh.get(jzrybh);
					if(ryxxGroupList == null){
						ryxxGroupList = new ArrayList<>();
						ryxxMapByRybh.put(jzrybh, ryxxGroupList);
					}
					ryxxGroupList.add(dataMap);
				}
				if(StringUtils.isNotBlank(xm)){
					searchXmSet.add(xm);
				}
				searchSfzhSet.add(sfzh);
			}
			
			DataQuery dataQuerySfz = DataQuery.create(PzryxxFields.tableName);
			dataQuerySfz.addSelectField(PzryxxFields.field_AJBH);
			dataQuerySfz.addSelectField(PzryxxFields.field_SJZT);
			dataQuerySfz.addSelectField(PzryxxFields.field_SFZH);
			dataQuerySfz.addSelectField(PzryxxFields.field_XM);
			dataQuerySfz.addSelectField(PzryxxFields.field_PZBH);
			//dataQuerySfz.add(DataCriteria.or(DataCriteria.in(PzryxxFields.field_SFZH, searchSfzhSet),DataCriteria.in(PzryxxFields.field_XM, searchXmSet)));
			List<Map<String, Object>> storePzxxResultList = jdbcQuerier.queryForList(dataQuerySfz);
			for (Map<String, Object> map : storePzxxResultList) {
				String sjzt = (String)map.get(PzryxxFields.field_SJZT);
				sfzh = (String)map.get(PzryxxFields.field_SFZH);
				xm = (String)map.get(PzryxxFields.field_XM);
				ajbh = (String)map.get(PzryxxFields.field_AJBH);
				String mainInfoId = MD5.md5(sfzh + xm + ajbh);
				String pzbh = (String)map.get(PzryxxFields.field_PZBH);
				
				noNeedToAddPzbh.add(pzbh);
				if(!Dictionary.比对结果_办结.equals(sjzt) && !Dictionary.比对结果_已排他.equals(sjzt)){
					noNeedToAddSfzh.add(sfzh);
				//	String pzbh = MD5.md5(xm + ajbh);
					noNeedToAddmainInfoIdSet.add(mainInfoId);
				}else{
					hasDealmainId.add(mainInfoId);
					hasDealmainIdMap.put(mainInfoId, map);
					hasDealSfzhMap.put(sfzh,map);
					hasDealSfzh.add(sfzh);
				}
			}
		}
		
		
		//第一次对子表信息分组，用于曾经办结、排他的新碰撞信息
		Map<String, Map<String,List<Map<String, Object>>>> storePersonDetailedMapByZbbh = new HashMap<>();
		List<Map<String, Object>> detailedGroupList = null;
		Map<String, List<Map<String, Object>>> detailedGroupMap = null;

		Map<String, Object> storePzxxInfoMap = null;
		for (DnaInfo dnaInfo : dnaInfoByQuery) {
			jzrybh = dnaInfo.getJzrybh();
			String ryybbh = dnaInfo.getRyybbh();
			// ajbh = dnaInfo.getJzajbh();
			String jzajbh = dnaInfo.getJzajbh();
			//String wzajbh = dnaInfo.getWzajbh();
			xm = dnaInfo.getXm();
			if("王中安".equals(xm)){
				System.out.println(123);
			}
			sfzh = dnaInfo.getSfzh();
			String mainInfoId = "";
			if (StringUtils.isNotBlank(jzrybh) || StringUtils.isNotBlank(ryybbh) ) {
				ryxxGroupList = ryxxMapByRybh.get(jzrybh);
				if(ryxxGroupList == null){
					ryxxGroupList = ryxxMapByRybh.get(ryybbh);
				}
			}
			//qq = "";
			//hjdz = "";
			ajbhGroupSet = new HashSet<>();
			if(ryxxGroupList != null){
				for (Map<String, Object> dataMap : ryxxGroupList) {
					String rySystemId = (String)dataMap.get(XyrxxFields.field_SYSTEMID);
					if(StringUtils.isNotBlank((String) dataMap.get(XyrxxFields.field_ZJHM))){
						sfzh = (String) dataMap.get(XyrxxFields.field_ZJHM);
					}
					if(middlePersonMapByRybh.get(rySystemId) != null){
						ajbhGroupSet.addAll( middlePersonMapByRybh.get(rySystemId));
					}
				}
				if(StringUtils.isNotBlank(jzajbh)){
					ajbhGroupSet.add(jzajbh);
				}
			}
			if("无".equals(sfzh)){
				sfzh = "";
			}
					
			if (ajbhGroupSet != null) {
				String earlyAjbh = "";
				Date secendLarq = null;
				for (String curAjbh : ajbhGroupSet) {
					if(ajbhGroupSet.size() == 1){
						earlyAjbh = curAjbh;
						continue;
					}
					Map<String, Object> caseInfoMap = caseInfoMapByAjbh.get(curAjbh);
					if(caseInfoMap != null){
						if(caseInfoMap.get(DgCaseFields.LASJ) != null  ){
							Date larq = (Date)caseInfoMap.get(DgCaseFields.LASJ);
							if(secendLarq != null){
								if(larq.after(secendLarq)){
									larq = secendLarq;
									earlyAjbh = (String)caseInfoMap.get(DgCaseFields.AJBH);
								}
							}else{
								secendLarq = larq;
								//earlyAjbh = (String)caseInfoMap.get(DgCaseFields.AJBH);
							}
						}
					}
				}
				
				if(StringUtils.isBlank(jzajbh) && StringUtils.isNotBlank(earlyAjbh)){
					jzajbh = earlyAjbh;
				}
				//for (String curAjbh : ajbhGroupSet) {
				if(StringUtils.isBlank(jzajbh)){
					continue;
				}
				ajbh = jzajbh;
				mainInfoId = MD5.md5(sfzh + xm + ajbh);
				//如果数据库已有了不必再新增了
				if(noNeedToAddmainInfoIdSet.contains(mainInfoId)){
					continue;
				}
				
				String taskBh = MD5.md5(xm + ajbh);
					
				//如果已存在数据库中，无需再添加
				if(noNeedToAddPzbh.contains(taskBh)){
					continue; 
				}
				if (!personInfoMianMap.containsKey(mainInfoId)) {
					personInfoMian = new PersonInfoMian();
					personInfoMian.setDcd_sfz(sfzh);
					personInfoMian.setDcd_sjbh(mainInfoId);
					personInfoMian.setDcd_xm(xm);
					personInfoMian.setXzd_ajbh(ajbh);
					personInfoMianMap.put(mainInfoId,personInfoMian);
					
					// 任务编号与碰撞编号生成规则相同
					//String taskBh = MD5.md5(sfzh + ajbh);
					pzphSet.add(taskBh);
					dnaTaskInfo = new DnaTaskInfo();
					dnaTaskInfo.setDcd_rwbh(taskBh);
					//dnaTaskInfo.setDcd_dnaid(dnaId);
					dnaTaskInfo.setDcd_ajbh(ajbh);
					dnaTaskInfo.setDcd_rwkssj(date);
					dnaTaskInfo.setDcd_rwzxsj(date);
					dnaTaskInfo.setDcd_rysjbh(jzrybh);
					dnaTaskInfo.setDcd_rwzt(Dictionary.比对任务状态_获取指纹);
					dnaTaskInfoMap.put(taskBh, dnaTaskInfo);
				}
				
				personAjbhList = personAjbhMap.get(mainInfoId);
				if (personAjbhList == null) {
					personAjbhList = new ArrayList<>();
					personAjbhMap.put(mainInfoId, personAjbhList);
				}
				personAjbhList.add(ajbh);
				
				rybhGroupMap = rybhAllGroupMap.get(ajbh);
				if (rybhGroupMap == null) {
					rybhGroupMap = new HashMap<>();
					rybhAllGroupMap.put(ajbh, rybhGroupMap);
				}
				rybhGroupMap.put(xm, jzrybh);

				//personSfzhmSet.add(sfzh);

				personInfoDetailedList = personInfoDetailedMap.get(jzrybh);
				if (personInfoDetailedList == null) {
					personInfoDetailedList = new ArrayList<PersonInfoDetailed>();
					personInfoDetailedMap.put(jzrybh,personInfoDetailedList);
				}
				
				
				//组装嫌疑人基本信息
				if(ryxxGroupList != null){
					for (Map<String, Object> dataMap : ryxxGroupList) {
						if(StringUtils.isNotBlank((String) dataMap.get(XyrxxFields.field_RESERVATION35))){
							qq = (String) dataMap.get(XyrxxFields.field_RESERVATION35);
						}
						if(StringUtils.isNotBlank((String) dataMap.get(XyrxxFields.field_HJDZ))){
							hjdz = (String) dataMap.get(XyrxxFields.field_HJDZ);
						}
						lxdh = (String) dataMap.get(XyrxxFields.field_LXDH);
						sjhm1 = (String) dataMap.get(XyrxxFields.field_RESERVATION41);
						sjhm2 = (String) dataMap.get(XyrxxFields.field_RESERVATION42);
						
						//当已办结，已排他的碰撞人员重新有数据时，获取旧的基本信息进行填充
						storePzxxInfoMap = hasDealmainIdMap.get(mainInfoId);
						if(storePzxxInfoMap != null){
							qq = (String)storePzxxInfoMap.get(PzryxxFields.field_QQ);
							hjdz = (String)storePzxxInfoMap.get(PzryxxFields.field_DZ);
							lxdh = (String)storePzxxInfoMap.get(PzryxxFields.field_SJH);
							weChat = (String)storePzxxInfoMap.get(PzryxxFields.field_WX);
							imei = (String)storePzxxInfoMap.get(PzryxxFields.field_IMEI);
							imsi = (String)storePzxxInfoMap.get(PzryxxFields.field_IMSI);
							mac = (String)storePzxxInfoMap.get(PzryxxFields.field_MAC);
							jd = (String)storePzxxInfoMap.get(PzryxxFields.field_JD);
							tb = (String)storePzxxInfoMap.get(PzryxxFields.field_TB);
							jd1 = (String)storePzxxInfoMap.get(PzryxxFields.field_JD0);
							wd = (String)storePzxxInfoMap.get(PzryxxFields.field_WD);
							cph = (String)storePzxxInfoMap.get(PzryxxFields.field_CPH);
						}
						
						if (StringUtils.isNotBlank(ajbh)) {
							if (personInfoDetailedList != null) {
								if (StringUtils.isNotBlank(qq)) {
									sjlx = Dictionary.比对数据类型_QQ;
									qq = qq.replaceAll("，", ",");
									String[] qqStr = qq.split(",");
									for (String str : qqStr) {
										buildPersonDetailedByInit(mainInfoId, sjlx, str,ajbh, jzrybh, date,personInfoDetailedList,detailedWybhSet);
									}
								}

								if (StringUtils.isNotBlank(lxdh)) {
									sjlx = Dictionary.比对数据类型_手机号;
									lxdh = lxdh.replaceAll("，", ",");
									String[] lxdhStr = lxdh.split(",");
									for (String str : lxdhStr) {
										buildPersonDetailedByInit(mainInfoId, sjlx, str,ajbh, jzrybh, date,personInfoDetailedList,detailedWybhSet);
									}
								}

								if (StringUtils.isNotBlank(sjhm1)) {
									sjlx = Dictionary.比对数据类型_手机号;
									sjhm1 = sjhm1.replaceAll("，", ",");
									String[] sjhm1Str = sjhm1.split(",");
									for (String str : sjhm1Str) {
										buildPersonDetailedByInit(mainInfoId, sjlx, str,ajbh, jzrybh, date,personInfoDetailedList,detailedWybhSet);
									}
								}

								if (StringUtils.isNotBlank(sjhm2)) {
									sjlx = Dictionary.比对数据类型_手机号;
									sjhm2 = sjhm2.replaceAll("，", ",");
									String[] sjhm2Str = sjhm2.split(",");
									for (String str : sjhm2Str) {
										buildPersonDetailedByInit(mainInfoId, sjlx, str,ajbh, jzrybh, date,personInfoDetailedList,detailedWybhSet);
									}
								}

								// 当已办结，已排他的碰撞人员重新有数据时，获取旧的基本信息进行填充
								if (storePzxxInfoMap != null) {
									if (StringUtils.isNotBlank(hjdz)) {
										sjlx = Dictionary.比对数据类型_地址;
										hjdz = hjdz.replaceAll("，", ",");
										String[] hjdzStr = hjdz.split(",");
										for (String str : hjdzStr) {
											buildPersonDetailedByInit(mainInfoId, sjlx, str,ajbh, jzrybh, date,personInfoDetailedList,detailedWybhSet);
										}
									}
									if (StringUtils.isNotBlank(weChat)) {
										sjlx = Dictionary.比对数据类型_微信;
										weChat = weChat.replaceAll("，", ",");
										String[] weChatStr = weChat.split(",");
										for (String str : weChatStr) {
											buildPersonDetailedByInit(mainInfoId, sjlx, str,ajbh, jzrybh, date,personInfoDetailedList,detailedWybhSet);
										}
									}
									if (StringUtils.isNotBlank(jd)) {
										sjlx = Dictionary.比对数据类型_京东;
										jd = jd.replaceAll("，", ",");
										String[] jdStr = jd.split(",");
										for (String str : jdStr) {
											buildPersonDetailedByInit(mainInfoId, sjlx, str,ajbh, jzrybh, date,personInfoDetailedList,detailedWybhSet);
										}
									}
									if (StringUtils.isNotBlank(tb)) {
										sjlx = Dictionary.比对数据类型_淘宝;
										tb = tb.replaceAll("，", ",");
										String[] tbStr = tb.split(",");
										for (String str : tbStr) {
											buildPersonDetailedByInit(mainInfoId, sjlx, str,ajbh, jzrybh, date,personInfoDetailedList,detailedWybhSet);
										}
									}
									if (StringUtils.isNotBlank(jd1)) {
										sjlx = Dictionary.比对数据类型_经度;
										jd1 = jd1.replaceAll("，", ",");
										String[] jd1Str = jd1.split(",");
										for (String str : jd1Str) {
											buildPersonDetailedByInit(mainInfoId, sjlx, str,ajbh, jzrybh, date,personInfoDetailedList,detailedWybhSet);
										}
									}
									if (StringUtils.isNotBlank(wd)) {
										sjlx = Dictionary.比对数据类型_纬度;
										wd = wd.replaceAll("，", ",");
										String[] wdStr = wd.split(",");
										for (String str : wdStr) {
											buildPersonDetailedByInit(mainInfoId, sjlx, str,ajbh, jzrybh, date,personInfoDetailedList,detailedWybhSet);
										}
									}
									if (StringUtils.isNotBlank(imei)) {
										sjlx = Dictionary.比对数据类型_IMEI;
										imei = imei.replaceAll("，", ",");
										String[] imeiStr = imei.split(",");
										for (String str : imeiStr) {
											buildPersonDetailedByInit(mainInfoId, sjlx, str,ajbh, jzrybh, date,personInfoDetailedList,detailedWybhSet);
										}
									}
									if (StringUtils.isNotBlank(imsi)) {
										sjlx = Dictionary.比对数据类型_IMSI;
										imsi = imsi.replaceAll("，", ",");
										String[] imsiStr = imsi.split(",");
										for (String str : imsiStr) {
											buildPersonDetailedByInit(mainInfoId, sjlx, str,ajbh, jzrybh, date,personInfoDetailedList,detailedWybhSet);
										}
									}
									if (StringUtils.isNotBlank(mac)) {
										sjlx = Dictionary.比对数据类型_MAC;
										mac = mac.replaceAll("，", ",");
										String[] macStr = mac.split(",");
										for (String str : macStr) {
											buildPersonDetailedByInit(mainInfoId, sjlx, str,ajbh, jzrybh, date,personInfoDetailedList,detailedWybhSet);
										}
									}
									if (StringUtils.isNotBlank(cph)) {
										sjlx = Dictionary.比对数据类型_车牌号;
										cph = cph.replaceAll("，", ",");
										String[] cphStr = cph.split(",");
										for (String str : cphStr) {
											buildPersonDetailedByInit(mainInfoId, sjlx, str,ajbh, jzrybh, date,personInfoDetailedList,detailedWybhSet);
										}
									}
								}
							}
						}
					
						if(StringUtils.isNotBlank(taskBh)){
							dnaTaskDetailedInfo = new DnaTaskDetailedInfo();
							dnaTaskDetailedInfo.setDcd_zrwid(taskBh);
							dnaTaskDetailedInfo.setDcd_wybh(UUID.randomUUID());
							dnaTaskDetailedInfo.setDcd_rwzxsj(date);
							dnaTaskDetailedInfo.setDcd_rwxxzt(Dictionary.比对任务状态_获取指纹);
							dnaTaskDetailedInfo.setDcd_czr("系统");
							dnaTaskDetailedInfo.setDcd_pzbh(taskBh);
							dnaTaskInfoDetailedMap.put(taskBh,dnaTaskDetailedInfo);
						}
					}
				}
			}
		}
		
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		if(personInfoDetailedList !=null){
			System.out.println("-------------------------------------------人员子表list数量【"+personInfoDetailedList.size()+"】--------------------------------------------------------------------");
		}
        System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		
		Map<String, PersonCollisonInfo> newPersonCollisonInfoMap = new HashMap<String,PersonCollisonInfo>();
		//查询已存在的碰撞信息
		Map<String, PersonCollisonInfo> storePersonCollisonInfoMap = new HashMap<String,PersonCollisonInfo>();
		if(pzphSet.size() > 0){
			dataQuery = DataQuery.create(PersonCollisonInfo.tableName);
			//dataQuery.addSelectField(PzryxxFields.field_PZBH);
			dataQuery.add(DataCriteria.in(PzryxxFields.field_PZBH, pzphSet));
			batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
			batchQuery.getDataQuery().setLimit(1, maxNum);
			batchQuery.getTotal();
			while(batchQuery.hasNext()){
				List<Map<String, Object>> dataList = batchQuery.next();
				for (Map<String, Object> map : dataList) {
					String pzbh = (String)map.get(PzryxxFields.field_PZBH);
					personCollisonInfo = (PersonCollisonInfo) BeanUitl.convertMap(PersonCollisonInfo.class, map);
					storePersonCollisonInfoMap.put((String)map.get(PzryxxFields.field_PZBH), personCollisonInfo);
					//移除已存在的任务日志
					dnaTaskInfoMap.remove(pzbh);
					dnaTaskInfoDetailedMap.remove(pzbh);
				}
			}
		}
		
		Set<Entry<String, DnaTaskInfo>> dnaTaskInfoEntrySet = dnaTaskInfoMap.entrySet();
		for (Entry<String, DnaTaskInfo> e : dnaTaskInfoEntrySet) {
			taskList.add(e.getValue());
		}
		Set<Entry<String, DnaTaskDetailedInfo>> dnaTaskInfoDetailedEntrySet = dnaTaskInfoDetailedMap.entrySet();
		for (Entry<String, DnaTaskDetailedInfo> e : dnaTaskInfoDetailedEntrySet) {
			taskDetailedList.add(e.getValue());
		}
		
		// 记录数据录入日志
		dnaTaskInfoService.saveTaskLog(taskList);
		dnaTaskInfoService.saveTaskDetailedLog(taskDetailedList);

		endTime = System.currentTimeMillis();
		System.out.println("--------------------【查询嫌疑人】结束，共耗时：" + (endTime - beginTime) / 1000 + "秒------------------------");
		System.out.println("");
		System.out.println("");
		
		if(detailedWybhSet.size() > 0){
			//防止重新初始化已有的数据
			dataQuery = DataQuery.create(PersonInfoDetailedFields.tableName);
			dataQuery.add(DataCriteria.in(PersonInfoDetailedFields.field_DCD_WYBH, detailedWybhSet));
			batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
			batchQuery.getDataQuery().setLimit(1, maxNum);
			int total = batchQuery.getTotal();
			while(batchQuery.hasNext()){
				List<Map<String, Object>> dataList = batchQuery.next();
				for (Map<String, Object> map : dataList) {
					String wybh = (String)map.get(PersonInfoDetailedFields.field_DCD_WYBH);
					detailedWybhSet.remove(wybh);
				}
			}
			if(total <= 0){
				detailedWybhSet = new HashSet<>();
			}
		}

		// 保存人员主表信息
		List<PersonInfoMian> personInfoMianList = new ArrayList<>(personInfoMianMap.values());
		List<Map<String, Object>> savepersonInfoMianMapList = BeanUitl.beanToMaps(personInfoMianList);
		DirInfo personInfoMianDirInfo = dirInfoQueryApi.getDirInfoByTableName(PersonInfoMian.tableName, ZygxEnums.dirItems);
		if(savepersonInfoMianMapList != null){
			System.out.println("----------------------------------无身份证人员主表保存【"+savepersonInfoMianMapList.size()+"】-----------------------------------------");
			//saveDataApi.saveOrUpdateDirInfoData(personInfoMianDirInfo, savepersonInfoMianMapList, false, userInfo, null);
			saveDataApi.saveOrUpdateDirInfoData(personInfoMianDirInfo, savepersonInfoMianMapList, false, userInfo, null);
		}

		// 保存人员子表详细信息
		List<PersonInfoDetailed> savePersonInfoDetailedList = new ArrayList<>();
		Set<Entry<String, List<PersonInfoDetailed>>> entrySet = personInfoDetailedMap.entrySet();
		for (Entry<String, List<PersonInfoDetailed>> e : entrySet) {
			List<PersonInfoDetailed> detailedList = e.getValue();
			for (PersonInfoDetailed personInfoDetailed : detailedList) {
				String dcd_wybh = personInfoDetailed.getDcd_wybh();
				if(detailedWybhSet.size() == 0){
					savePersonInfoDetailedList.add(personInfoDetailed);
				}else{
					if(!detailedWybhSet.contains(dcd_wybh)){
						savePersonInfoDetailedList.add(personInfoDetailed);
					}
				}
			}
		}
		List<Map<String, Object>> savePersonInfoDetailedMapList = BeanUitl.beanToMaps(savePersonInfoDetailedList);
		DirInfo personInfoDetailedDirInfo = dirInfoQueryApi.getDirInfoByTableName(PersonInfoDetailed.tableName, ZygxEnums.dirItems);
		saveDataApi.saveOrUpdateDirInfoData(personInfoDetailedDirInfo, savePersonInfoDetailedMapList, false, userInfo, null);
		
		// Set<String> addSfzSet = personInfoMianMap.keySet();
		//对人员子表详细信息分组
		if(ajbhList.size() > 0){
			dataQuery = DataQuery.create(PersonInfoDetailed.tableName);
			if(personAjbhMap.size() > 0){
				dataQuery.add(DataCriteria.in(PersonInfoDetailedFields.field_DCD_ZBSJBH, personAjbhMap.keySet()));
				//dataQuery.add(DataCriteria.in(PersonInfoDetailedFields.field_DCD_AJBH, ajbhList));
				dataQuery.add(DataCriteria.not(DataCriteria.eq(PersonInfoDetailedFields.field_DCD_SJLX, Dictionary.比对数据来源_系统碰撞)));
				batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
				batchQuery.getDataQuery().setLimit(1, maxNum);
				batchQuery.getTotal();
				while (batchQuery.hasNext()) {
					List<Map<String, Object>> dataList = batchQuery.next();
					for (Map<String, Object> map : dataList) {
						String zbsjbh = (String) map.get(PersonInfoDetailedFields.field_DCD_ZBSJBH);
						ajbh = (String) map.get(PersonInfoDetailedFields.field_DCD_AJBH);
						detailedGroupMap = storePersonDetailedMapByZbbh.get(zbsjbh);
						if(detailedGroupMap == null){
							detailedGroupMap = new HashMap<>();
							storePersonDetailedMapByZbbh.put(zbsjbh, detailedGroupMap);
						}
						detailedGroupList =detailedGroupMap.get(ajbh);
						if(detailedGroupList == null){
							detailedGroupList = new ArrayList<Map<String, Object>>();
							detailedGroupMap.put(ajbh, detailedGroupList);
						}
						detailedGroupList.add(map);
					}
				}
			}
		}
		
		// 保存碰撞人员信息-start
		Map<String, Object> caseInfoMap = null;
		Map<String, Map<String, Set<String>>> valGroupMap = new HashMap<String, Map<String, Set<String>>>();
		List<PersonCollisonInfo> perCollisonInfoList = new ArrayList<PersonCollisonInfo>();
		Map<String, Set<String>> valMap = null;
		Set<String> valSet = null;
		Set<String> autoAllotCollisonInfoSet = new HashSet<>();
		//自动办结set
		Set<String> autoBjSet = new HashSet<>();
		
		Set<String> hasNewSfzhSet= new HashSet<>();
		Set<Entry<String, List<String>>> personAjbhEntrySet = personAjbhMap.entrySet();
		for (Entry<String, List<String>> es : personAjbhEntrySet) {
			String zbsjbh = es.getKey();
			personInfoMian = personInfoMianMap.get(zbsjbh);
			personAjbhList = es.getValue();
			xm = personInfoMian.getDcd_xm();
			if("HOANG VAN CUONG".equals(xm)){
				System.out.println(123);
			}
			for (String personAjbh : personAjbhList) {
				ajbh = personAjbh;
				
				if(personInfoMian != null){
					sfzh = personInfoMian.getDcd_sfz();
					
					if(StringUtils.isNotBlank(xm)){
						if(StringUtils.isNotBlank(sfzh) && noNeedToAddSfzh.contains(sfzh)){
							continue;
						}
						rybhGroupMap = rybhAllGroupMap.get(ajbh);
						jzrybh = rybhGroupMap.get(xm);
						
						if(StringUtils.isNotBlank(jzrybh)){
							valMap = valGroupMap.get(jzrybh);
							if (valMap == null) {
								valMap = new HashMap<String, Set<String>>();
								valGroupMap.put(jzrybh, valMap);
							}
						}else{
							valMap = null;
						}
					}
				
					if(valMap != null){
						detailedGroupMap = storePersonDetailedMapByZbbh.get(zbsjbh);
						if(detailedGroupMap != null){
							detailedGroupList = detailedGroupMap.get(ajbh);
							if(detailedGroupList == null){
								Collection<List<Map<String, Object>>> values = detailedGroupMap.values();
								for (List<Map<String, Object>> list : values) {
									detailedGroupList = list;
									if(detailedGroupList != null){
										for (Map<String, Object> map : detailedGroupList) {
											if(ajbh.equals(map.get(PersonInfoDetailedFields.field_DCD_AJBH))){
												sjlx = (String) map.get(PersonInfoDetailedFields.field_DCD_SJLX);
												valSet = valMap.get(sjlx);
												if (valSet == null) {
													valSet = new HashSet<String>();
													valMap.put(sjlx, valSet);
												}
												valSet.add((String) map.get(PersonInfoDetailedFields.field_DCD_SJZ));
											}
										}
									}
								}
							}else{
								for (Map<String, Object> map : detailedGroupList) {
									if(ajbh.equals(map.get(PersonInfoDetailedFields.field_DCD_AJBH))){
										sjlx = (String) map.get(PersonInfoDetailedFields.field_DCD_SJLX);
										valSet = valMap.get(sjlx);
										if (valSet == null) {
											valSet = new HashSet<String>();
											valMap.put(sjlx, valSet);
										}
										valSet.add((String) map.get(PersonInfoDetailedFields.field_DCD_SJZ));
									}
								}
							}
						}
					}
					
					String pzbh = MD5.md5(xm + ajbh);
					//已存在的只更新 手机 QQ 微信 IMEI IMSI MAC 地址 字段，其他不改变
					//personCollisonInfo = storePersonCollisonInfoMap.get(pzbh);
					personCollisonInfo = storePersonCollisonInfoMap.get(sfzh);
					if(noNeedToAddPzbh.contains(pzbh)){
						continue;
					}
					//用做过滤无身份证dna数据一起保存，避免同一人重复新增碰撞信息
					if(!hasNewSfzhSet.contains(sfzh)){
						if(personCollisonInfo == null){
							if(StringUtils.isNotBlank(sfzh)){
								hasNewSfzhSet.add(sfzh);
							}
							personCollisonInfo = new PersonCollisonInfo();
							xm = personInfoMian.getDcd_xm();
							personCollisonInfo.setDcd_sfzh(sfzh);
							personCollisonInfo.setDcd_xm(xm);
							personCollisonInfo.setDcd_ajbh(ajbh);
							personCollisonInfo.setDcd_pzbh(pzbh);
						}
						if(ajbh.startsWith("J") || ajbh.startsWith("j")){
							caseInfoMap = jqCaseInfoMapByAjbh.get(ajbh);
							if(caseInfoMap != null){
								String sldw = (String)caseInfoMap.get(DgAlertFields.field_SLJJDW);
								if(StringUtils.isNotBlank(sldw)){
									if(sldw.length() > 6){
										sldw = sldw.substring(0, 6);
									}
									personCollisonInfo.setDcd_sldwbh(sldw);
									org = orgMap.get(sldw + "000000");
									if(org != null){
										personCollisonInfo.setDcd_sldw(org.getName());
									}
									autoAllotCollisonInfoSet.add(pzbh);
									if(!storePersonCollisonInfoMap.containsKey(pzbh)){
										personCollisonInfo.setDcd_fpsj(date);
										personCollisonInfo.setDcd_lqxsyts(3);
										personCollisonInfo.setDcd_ysbz(Dictionary.颜色标志_绿色);
									}
								}
							}
						}else{
							caseInfoMap = caseInfoMapByAjbh.get(ajbh);
							if(caseInfoMap != null){
								String sldw = (String)caseInfoMap.get(DgCaseFields.SLJSDW);
								if(StringUtils.isNotBlank(sldw)){
									if(sldw.length() > 6){
										sldw = sldw.substring(0, 6);
									}
									personCollisonInfo.setDcd_sldwbh(sldw);
									org = orgMap.get(sldw + "000000");
									if(org != null){
										personCollisonInfo.setDcd_sldw(org.getName());
									}
									autoAllotCollisonInfoSet.add(pzbh);
									if(caseInfoMap.get(DgCaseFields.JA_JASJ) != null && StringUtils.isNotBlank(caseInfoMap.get(DgCaseFields.JA_JASJ).toString())){
										autoBjSet.add(pzbh);
									}else if(caseInfoMap.get(DgCaseFields.PASJ) != null && StringUtils.isNotBlank(caseInfoMap.get(DgCaseFields.PASJ).toString())){
										autoBjSet.add(pzbh);
									}else{
										if(!storePersonCollisonInfoMap.containsKey(pzbh)){
											personCollisonInfo.setDcd_fpsj(date);
											personCollisonInfo.setDcd_lqxsyts(3);
											personCollisonInfo.setDcd_ysbz(Dictionary.颜色标志_绿色);
										}
									}
								}
							}
						}

						if(valMap != null){
							valSet = valMap.get(Dictionary.比对数据类型_QQ);
							qq = "";
							if (valSet != null) {
								for (String val : valSet) {
									qq += val + ";";
								}
								//qq.replaceAll("，", ",");
							}

							if (StringUtils.isNotBlank(qq)) {
								if (qq.lastIndexOf(";") > -1) {
									qq = qq.substring(0, qq.length() - 1);
								}
							}

							valSet = valMap.get(Dictionary.比对数据类型_手机号);
							lxdh = "";
							if (valSet != null) {
								for (String val : valSet) {
									lxdh += val + ";";
								}
								//lxdh.replaceAll("，", ",");
							}

							if (StringUtils.isNotBlank(lxdh)) {
								if (lxdh.lastIndexOf(";") > -1) {
									lxdh = lxdh.substring(0, lxdh.length() - 1);
								}
							}

							valSet = valMap.get(Dictionary.比对数据类型_地址);
							hjdz = "";
							if (valSet != null) {
								for (String val : valSet) {
									hjdz += val + ";";
								}
							//	hjdz.replaceAll("，", ",");
							}

							if (StringUtils.isNotBlank(hjdz)) {
								if (hjdz.lastIndexOf(";") > -1) {
									hjdz = hjdz.substring(0, hjdz.length() - 1);
								}
							}

							valSet = valMap.get(Dictionary.比对数据类型_微信);
							weChat = "";
							if (valSet != null) {
								for (String val : valSet) {
									weChat += val + ";";
								}
								//weChat.replaceAll("，", ",");
							}

							if (StringUtils.isNotBlank(weChat)) {
								if (weChat.lastIndexOf(";") > -1) {
									weChat = weChat.substring(0, weChat.length() - 1);
								}
							}
							
							valSet = valMap.get(Dictionary.比对数据类型_IMEI);
							imei = "";
							if (valSet != null) {
								for (String val : valSet) {
									imei += val + ";";
								}
								//imei.replaceAll("，", ",");
							}

							if (StringUtils.isNotBlank(imei)) {
								if (imei.lastIndexOf(";") > -1) {
									imei = imei.substring(0, imei.length() - 1);
								}
							}
							
							valSet = valMap.get(Dictionary.比对数据类型_IMSI);
							imsi = "";
							if (valSet != null) {
								for (String val : valSet) {
									imsi += val + ";";
								}
								//imsi.replaceAll("，", ",");
							}

							if (StringUtils.isNotBlank(imsi)) {
								if (imsi.lastIndexOf(";") > -1) {
									imsi = imsi.substring(0, imsi.length() - 1);
								}
							}
							
							valSet = valMap.get(Dictionary.比对数据类型_MAC);
							mac = "";
							if (valSet != null) {
								for (String val : valSet) {
									mac += val + ";";
								}
								//mac.replaceAll("，", ",");
							}

							if (StringUtils.isNotBlank(mac)) {
								if (mac.lastIndexOf(";") > -1) {
									mac = mac.substring(0, mac.length() - 1);
								}
							}
							
							valSet = valMap.get(Dictionary.比对数据类型_京东);
							jd = "";
							if (valSet != null) {
								for (String val : valSet) {
									jd += val + ";";
								}
								//jd.replaceAll("，", ",");
							}
							if (StringUtils.isNotBlank(jd)) {
								if (jd.lastIndexOf(";") > -1) {
									jd = jd.substring(0, jd.length() - 1);
								}
							}
							
							valSet = valMap.get(Dictionary.比对数据类型_淘宝);
							tb = "";
							if (valSet != null) {
								for (String val : valSet) {
									tb += val + ";";
								}
								//tb.replaceAll("，", ",");
							}
							if (StringUtils.isNotBlank(tb)) {
								if (tb.lastIndexOf(";") > -1) {
									tb = tb.substring(0, tb.length() - 1);
								}
							}
							
							
							valSet = valMap.get(Dictionary.比对数据类型_经度);
							jd1 = "";
							if (valSet != null) {
								for (String val : valSet) {
									jd1 += val + ";";
								}
								//jd1.replaceAll("，", ",");
							}
							if (StringUtils.isNotBlank(jd1)) {
								if (jd1.lastIndexOf(";") > -1) {
									jd1 = jd1.substring(0, jd1.length() - 1);
								}
							}
							
							
							valSet = valMap.get(Dictionary.比对数据类型_纬度);
							wd = "";
							if (valSet != null) {
								for (String val : valSet) {
									wd += val + ";";
								}
								//wd.replaceAll("，", ",");
							}
							if (StringUtils.isNotBlank(wd)) {
								if (wd.lastIndexOf(";") > -1) {
									wd = wd.substring(0, wd.length() - 1);
								}
							}
							
							valSet = valMap.get(Dictionary.比对数据类型_车牌号);
							cph = "";
							if (valSet != null) {
								for (String val : valSet) {
									cph += val + ";";
								}
								//wd.replaceAll("，", ",");
							}
							if (StringUtils.isNotBlank(cph)) {
								if (cph.lastIndexOf(";") > -1) {
									cph = cph.substring(0, cph.length() - 1);
								}
							}
							personCollisonInfo.setDcd_qq(qq);
							personCollisonInfo.setDcd_sjh(lxdh);
							if(storePersonCollisonInfoMap.containsKey(pzbh)){
								personCollisonInfo.setDcd_dz(hjdz);
							}
							personCollisonInfo.setDcd_wx(weChat);
							personCollisonInfo.setDcd_imei(imei);
							personCollisonInfo.setDcd_imsi(imsi);
							personCollisonInfo.setDcd_mac(mac);
							personCollisonInfo.setDcd_dz(hjdz);
							personCollisonInfo.setDcd_jd(jd);
							personCollisonInfo.setDcd_tb(tb);
							personCollisonInfo.setDcd_jd0(jd1);
							personCollisonInfo.setDcd_wd(wd);
							personCollisonInfo.setXzd_cph(cph);
						}
						
						
						
						if(!storePersonCollisonInfoMap.containsKey(pzbh)){
							personCollisonInfo.setDcd_sjzt(Dictionary.比对结果_初始结果);
							//personCollisonInfo.setDcd_sjzt(Dictionary.比对结果_推送网监);
						}
						if(autoBjSet.contains(pzbh)){
							personCollisonInfo.setDcd_sjzt(Dictionary.比对结果_办结);
						}
						
						newPersonCollisonInfoMap.put(pzbh, personCollisonInfo);
						perCollisonInfoList.add(personCollisonInfo);
					}
				}
			}
		}
		
		
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
        System.out.println("-------------------------------------------碰撞人员list数量【"+perCollisonInfoList.size()+"】--------------------------------------------------------------------");
        System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
        List<Map<String, Object>> savePerCollisonInfoList = BeanUitl.beanToMaps(perCollisonInfoList);
		DirInfo personCollisonInfoDirInfo = dirInfoQueryApi.getDirInfoByTableName(PersonCollisonInfo.tableName, ZygxEnums.dirItems);
		Set<DirItem> personCollisonDirItems = personCollisonInfoDirInfo.getDirItems();
		DirItem dirItem =null;
		Map<String, DirItem> personCollisonDirItemMap = DirItemHolder.getDirItemMapByFieldName(personCollisonDirItems);
		saveDataApi.saveOrUpdateDirInfoData(personCollisonInfoDirInfo, savePerCollisonInfoList, false, userInfo, null);
		// 保存碰撞人员信息-end

		taskDetailedList = new ArrayList<DnaTaskDetailedInfo>();
		// 记录任务日志
		date = new Date();
		date.setSeconds(date.getSeconds() + 1);
		Date fpDate = new Date();
		fpDate.setSeconds(fpDate.getSeconds() + 2);
		Date bjDate = new Date();
		bjDate.setSeconds(bjDate.getSeconds() + 3);
		for (DnaTaskInfo task : taskList) {
			jzrybh = task.getDcd_rysjbh();
			ajbhGroupSet = middlePersonMapByRybh.get(jzrybh);
			//ajbh = middlePersonMapByRybh.get(jzrybh);
			if (StringUtils.isBlank(task.getDcd_ajbh())) {
				if(ajbhGroupSet != null){
					for (String curAjbh : ajbhGroupSet) {
						task.setDcd_ajbh(curAjbh);
					}
				}
			}
			/*
			 * if(StringUtils.isBlank(task.getDcd_ajbh())){
			 * task.setDcd_ajbh(ajbh); }
			 */
			String taskBh = task.getDcd_rwbh();
			task.setDcd_rwzt(Dictionary.比对任务状态_碰撞完毕);
			task.setDcd_rwzxsj(date);
			//如果不存在旧数据，即不是第一次碰撞的结果
			if(!storePersonCollisonInfoMap.containsKey(taskBh)){
				personCollisonInfo = newPersonCollisonInfoMap.get(taskBh);
				
				dnaTaskDetailedInfo = new DnaTaskDetailedInfo();
				dnaTaskDetailedInfo.setDcd_zrwid(taskBh);
				dnaTaskDetailedInfo.setDcd_wybh(UUID.randomUUID());
				dnaTaskDetailedInfo.setDcd_rwzxsj(date);
				dnaTaskDetailedInfo.setDcd_rwxxzt(Dictionary.比对任务状态_碰撞完毕);
				dnaTaskDetailedInfo.setDcd_czr("系统");
				dnaTaskDetailedInfo.setDcd_pzbh(taskBh);
				taskDetailedList.add(dnaTaskDetailedInfo);
				
				if(autoAllotCollisonInfoSet.contains(taskBh)){
					dnaTaskDetailedInfo = new DnaTaskDetailedInfo();
					dnaTaskDetailedInfo.setDcd_zrwid(taskBh);
					dnaTaskDetailedInfo.setDcd_wybh(UUID.randomUUID());
					dnaTaskDetailedInfo.setDcd_rwzxsj(fpDate);
					dnaTaskDetailedInfo.setDcd_rwxxzt(Dictionary.比对任务状态_自动分配);
					dnaTaskDetailedInfo.setDcd_czr("系统");
					dnaTaskDetailedInfo.setDcd_pzbh(taskBh);
					dirItem = personCollisonDirItemMap.get(PzryxxFields.field_SLDW);
					dnaTaskInfoService.buildChangeInfo(dirItem.getItemName(), "", personCollisonInfo.getDcd_sldw(), dnaTaskDetailedInfo);
					dirItem = personCollisonDirItemMap.get(PzryxxFields.field_SLDWBH);
					dnaTaskInfoService.buildChangeInfo(dirItem.getItemName(), "", personCollisonInfo.getDcd_sldwbh(), dnaTaskDetailedInfo);
					taskDetailedList.add(dnaTaskDetailedInfo);
				}
				
				//自动办结处理
				if(autoBjSet.contains(taskBh)){
					task.setDcd_rwzt(Dictionary.比对任务状态_办结);
					task.setDcd_rwzxsj(bjDate);
					
					dnaTaskDetailedInfo = new DnaTaskDetailedInfo();
					dnaTaskDetailedInfo.setDcd_zrwid(taskBh);
					dnaTaskDetailedInfo.setDcd_wybh(UUID.randomUUID());
					dnaTaskDetailedInfo.setDcd_rwzxsj(bjDate);
					dnaTaskDetailedInfo.setDcd_rwxxzt(Dictionary.比对任务状态_办结);
					dnaTaskDetailedInfo.setDcd_czr("系统");
					dnaTaskDetailedInfo.setDcd_pzbh(taskBh);
					taskDetailedList.add(dnaTaskDetailedInfo);
				}
			}
		}

		// 记录数据录入日志
		dnaTaskInfoService.updateTaskLog(taskList);
		dnaTaskInfoService.saveTaskDetailedLog(taskDetailedList);

		long endTime1 = System.currentTimeMillis();
		System.out.println("--------------------【DNA碰撞任务】结束，共耗时：" + (endTime1 - beginTime1) / 1000 + "秒------------------------");
		System.out.println("");
		
		System.out.println("");
	}
	
	

	public void excelUpdate(MultipartFile file, String importHKey) throws Exception {
		UserInfo userInfo = AssistUtils.getCurrUserInfo();
		// for (MultipartFile file : files) {
		String fileName = file.getOriginalFilename();
		String fileType = "";
		if (StringUtils.isNotBlank(fileName)) {
			int index = fileName.indexOf(".");
			fileType = fileName.substring(index + 1);
		} else {
			throw new BusinessException("请选择文件");
		}
		InputStream inputStream = file.getInputStream();
		excuteUpload(inputStream, fileType, userInfo, importHKey);
		// }
	}

	public void excelUpdate(InputStream inputStream, String fileName, String importHKey) throws Exception {
		UserInfo userInfo = AssistUtils.getCurrUserInfo();
		String fileType = "";
		if (StringUtils.isNotBlank(fileName)) {
			int index = fileName.indexOf(".");
			fileType = fileName.substring(index + 1);
		} else {
			throw new BusinessException("请选择文件");
		}
		excuteUpload(inputStream, fileType, userInfo, importHKey);
	}

	@SuppressWarnings("deprecation")
	private void excuteUpload(InputStream inputStream, String fileType, UserInfo userInfo, String importHKey) throws Exception {
		long beginTime1 = System.currentTimeMillis();
		try {
			DirInfo personCollisonInfoDirInfo = dirInfoQueryApi.getDirInfoByTableName(PersonCollisonInfo.tableName, ZygxEnums.dirItems);
			Set<DirItem>  personCollisonInfoDirItems = personCollisonInfoDirInfo.getDirItems();
			Map<String, DirItem> personCollisonInfoDirItemMap = DirItemHolder.getDirItemMapByFieldName(personCollisonInfoDirItems);
			
			List<String> taskInfoRwbhs = new ArrayList<String>();
			Set<String> sfzSet = new HashSet<>();
			Map<String,PersonCollisonInfo> personCollisonMapBySfzh = new HashMap<>();
			
			long beginTime = System.currentTimeMillis();
			System.out.println("");
			System.out.println("");
			System.err.println("--------------------【扫描excel】开始------------------------");

			if ("xlsx".equals(fileType)) {
				importHolder.choessXSSfWorkBook(inputStream, sfzSet , personCollisonMapBySfzh);
			} else if ("xls".equals(fileType)) {
				importHolder.choessHSSfWorkBook(inputStream, sfzSet , personCollisonMapBySfzh);
			} else {
				throw new BusinessException("上传非EXCEL文件类型");
			}

			long endTime = System.currentTimeMillis();
			System.out.println("--------------------【扫描excel】结束，共耗时：" + (endTime - beginTime) / 1000 + "秒------------------------");
			System.out.println("");
			System.out.println("");

			ReadinStatusAssist.setInitImportNum(importHKey, personCollisonMapBySfzh.size());

			DirItem dirItem = null;
			PersonInfoMian personInfoMian = null;
			DnaTaskInfo dnaTaskInfo = null;
			DnaTaskDetailedInfo dnaTaskDetailedInfo = null;
			//更新状态的主任务list
			List<DnaTaskInfo> updateTaskInfoList = new ArrayList<DnaTaskInfo>();
			//保存的任务子表详细信息list
			//List<DnaTaskDetailedInfo> saveTaskDetailedList = new ArrayList<DnaTaskDetailedInfo>();
			List<DnaTaskDetailedInfo> saveTaskDetailedList = new ArrayList<DnaTaskDetailedInfo>();
			//保存的人员子表信息list
			List<PersonInfoDetailed> savePersonDetailedList = new ArrayList<>();
			//人员子表信息map 唯一编号为key，子表信息为val
			Map<String, PersonInfoDetailed> personInfoDetailedMap = new HashMap<>();
			//人员子表唯一编号set
			Set<String> detailedMd5s = null;
			// 人员主表map 身份证为key,人员对象为val
			Map<String, PersonInfoMian> personInfoMainMap = new HashMap<String, PersonInfoMian>();
			//碰撞人员信息-碰撞编号list
			List<String> pzbhList = new ArrayList<String>();
			//碰撞人员信息map 碰撞编号为key
			//Map<String, PersonCollisonInfo> storePersonCollisonInfoMap = new HashMap<String, PersonCollisonInfo>();
			Map<String, Map<String,Object>> storePersonCollisonInfoMap = new HashMap<>();
			//案件编号set
			Set<String> ajbhSet = new HashSet<String>();
			PersonCollisonInfo storePersonCollisonInfo = null;
			PersonCollisonInfo newPersonCollisonInfo = null;
			
			
			String ajbh = "";
			String sjlx = "";
			String sjz = "";
			String sfz = "";
			// 人员主表数据编号
			String mainSjbh = "";
			Date date = new Date();
			if (personCollisonMapBySfzh.size() > 0) {
				beginTime = System.currentTimeMillis();

				beginTime = System.currentTimeMillis();
				System.out.println("");
				System.out.println("");
				System.err.println("--------------------【查询人员主表信息,并组装人员子表信息保存】开始------------------------");
				DataQuery dataQuery = DataQuery.create(PersonInfoMian.tableName);
				dataQuery.add(DataCriteria.in("DCD_SFZ", sfzSet));
				BatchQueryImpl batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
				batchQuery.getDataQuery().setLimit(1, maxNum);
				batchQuery.getTotal();

				List<DataCriterion> dataCriteriaOr = new ArrayList<>();
				List<DataCriterion> dataCriteriaAnd = null;
				
				
				while (batchQuery.hasNext()) {
					List<Map<String, Object>> dataList = batchQuery.next();
					for (Map<String, Object> map : dataList) {
						personInfoMian = (PersonInfoMian) BeanUitl.convertMap(PersonInfoMian.class, map);
						if (!personInfoMainMap.containsKey(personInfoMian.getDcd_sfz())) {
							personInfoMainMap.put(personInfoMian.getDcd_sfz(), personInfoMian);
						}
					}
				}

				dataQuery = DataQuery.create(PersonCollisonInfo.tableName);
				dataQuery.add(DataCriteria.in(PzryxxFields.field_SFZH, sfzSet));
				batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
				batchQuery.getDataQuery().setLimit(1, maxNum);
				int personCollisonInfoSize = batchQuery.getTotal();
				int count = 0;
				int totalSize = personCollisonInfoSize / 1000;
				int index = 0;
				while (batchQuery.hasNext()) {
					
					List<Map<String, Object>> dataList = batchQuery.next();
					for (Map<String, Object> map : dataList) {
						storePersonCollisonInfo = (PersonCollisonInfo) BeanUitl.convertMap(PersonCollisonInfo.class, map);
						String sjzt = storePersonCollisonInfo.getDcd_sjzt();
						count++;
						//只处理推送状态的数据
						if(Dictionary.比对结果_推送网监.equals(sjzt)){
							storePersonCollisonInfoMap.put(storePersonCollisonInfo.getDcd_pzbh(), map);
							
							detailedMd5s = new HashSet<String>();
							dataCriteriaAnd = new ArrayList<>();
							pzbhList.add(storePersonCollisonInfo.getDcd_pzbh());
							ajbh = storePersonCollisonInfo.getDcd_ajbh();
							sfz = storePersonCollisonInfo.getDcd_sfzh();
							personInfoMian = personInfoMainMap.get(sfz);
							newPersonCollisonInfo = personCollisonMapBySfzh.get(sfz);
							
							taskInfoRwbhs.add(MD5.md5(sfz +  ajbh));
							
							ajbhSet.add(ajbh);
							if(personInfoMian != null){
								mainSjbh = personInfoMian.getDcd_sjbh();
								dataCriteriaAnd.add(DataCriteria.eq(PersonInfoDetailedFields.field_DCD_AJBH, ajbh));
								dataCriteriaAnd.add(DataCriteria.eq(PersonInfoDetailedFields.field_DCD_ZBSJBH, mainSjbh));

								sjz = newPersonCollisonInfo.getDcd_sjh();
								if (StringUtils.isNotBlank(sjz)) {
									sjlx = Dictionary.比对数据类型_手机号;
									buildPersonDetailed(sjlx, sjz, ajbh, mainSjbh, date, savePersonDetailedList, personInfoDetailedMap, detailedMd5s);
								}

								sjz = newPersonCollisonInfo.getDcd_qq();
								if (StringUtils.isNotBlank(sjz)) {
									sjlx = Dictionary.比对数据类型_QQ;
									buildPersonDetailed(sjlx, sjz, ajbh, mainSjbh, date, savePersonDetailedList, personInfoDetailedMap, detailedMd5s);
								}

								sjz = newPersonCollisonInfo.getDcd_wx();
								if (StringUtils.isNotBlank(sjz)) {
									sjlx = Dictionary.比对数据类型_微信;
									buildPersonDetailed(sjlx, sjz, ajbh, mainSjbh, date, savePersonDetailedList, personInfoDetailedMap, detailedMd5s);
								}

								sjz = newPersonCollisonInfo.getDcd_imei();
								if (StringUtils.isNotBlank(sjz)) {
									sjlx = Dictionary.比对数据类型_IMEI;
									buildPersonDetailed(sjlx, sjz, ajbh, mainSjbh, date, savePersonDetailedList, personInfoDetailedMap, detailedMd5s);
								}

								sjz = newPersonCollisonInfo.getDcd_imsi();
								if (StringUtils.isNotBlank(sjz)) {
									sjlx = Dictionary.比对数据类型_IMSI;
									buildPersonDetailed(sjlx, sjz, ajbh, mainSjbh, date, savePersonDetailedList, personInfoDetailedMap, detailedMd5s);
								}

								sjz = newPersonCollisonInfo.getDcd_mac();
								if (StringUtils.isNotBlank(sjz)) {
									sjlx = Dictionary.比对数据类型_MAC;
									buildPersonDetailed(sjlx, sjz, ajbh, mainSjbh, date, savePersonDetailedList, personInfoDetailedMap, detailedMd5s);
								}
								
								sjz = newPersonCollisonInfo.getDcd_jd();
								if (StringUtils.isNotBlank(sjz)) {
									sjlx = Dictionary.比对数据类型_京东;
									buildPersonDetailed(sjlx, sjz, ajbh, mainSjbh, date, savePersonDetailedList, personInfoDetailedMap, detailedMd5s);
								}
								
								sjz = newPersonCollisonInfo.getDcd_tb();
								if (StringUtils.isNotBlank(sjz)) {
									sjlx = Dictionary.比对数据类型_淘宝;
									buildPersonDetailed(sjlx, sjz, ajbh, mainSjbh, date, savePersonDetailedList, personInfoDetailedMap, detailedMd5s);
								}

								sjz = newPersonCollisonInfo.getDcd_jd0();
								if (StringUtils.isNotBlank(sjz)) {
									sjlx = Dictionary.比对数据类型_纬度;
									buildPersonDetailed(sjlx, sjz, ajbh, mainSjbh, date, savePersonDetailedList, personInfoDetailedMap, detailedMd5s);
								}
								
								sjz = newPersonCollisonInfo.getDcd_wd();
								if (StringUtils.isNotBlank(sjz)) {
									sjlx = Dictionary.比对数据类型_经度;
									buildPersonDetailed(sjlx, sjz, ajbh, mainSjbh, date, savePersonDetailedList, personInfoDetailedMap, detailedMd5s);
								}

								sjz = newPersonCollisonInfo.getDcd_dz();
								if (StringUtils.isNotBlank(sjz)) {
									sjlx = Dictionary.比对数据类型_地址;
									buildPersonDetailed(sjlx, sjz, ajbh, mainSjbh, date, savePersonDetailedList, personInfoDetailedMap, detailedMd5s);
								}
								/*sjz = newPersonCollisonInfo.getXzd_cph();
								if (StringUtils.isNotBlank(sjz)) {
									sjlx = Dictionary.比对数据类型_车牌号;
									buildPersonDetailed(sjlx, sjz, ajbh, mainSjbh, date, savePersonDetailedList, personInfoDetailedMap, detailedMd5s);
								}*/
								dataCriteriaAnd.add(DataCriteria.not(DataCriteria.in(PersonInfoDetailedFields.field_DCD_WYBH, detailedMd5s)));
								dataCriteriaOr.add(DataCriteria.and(dataCriteriaAnd));
							}
						}

						if(count % 1000 == 0 || count == personCollisonInfoSize){
							index ++;
							System.out.println("");
							System.out.println("");
							System.out.println("");
							System.out.println("-------------------------------------------------------------------------------------删除不在网监数据里面的人员子表数据,第【"+index+"】次 ----- 总次数【"+totalSize+"】----------------------------------------------");
							System.out.println("");
							System.out.println("");
							System.out.println("");
							// 删除不在网监数据里面的人员子表数据
							if(dataCriteriaOr.size() > 0){
								DataQuery deleteDataQuery = DataQuery.create(PersonInfoDetailed.tableName);
								deleteDataQuery.add(DataCriteria.or(dataCriteriaOr));
								deleteDataApi.deleteDataByDataQuery(deleteDataQuery);
							}
						
							
							
							System.out.println("");
							System.out.println("");
							System.out.println("");
							System.out.println("-------------------------------------------------------------------------------------查询人员子表信息开始,第【"+index+"】次 ----- 总次数【"+totalSize+"】--------------------------------------------------");
							System.out.println("");
							System.out.println("");
							System.out.println("");
							dataQuery = DataQuery.create(PersonInfoDetailed.tableName);
							if (personInfoDetailedMap.size() > 0) {
								dataQuery.add(DataCriteria.in(PersonInfoDetailedFields.field_DCD_WYBH, personInfoDetailedMap.keySet()));
							} else {
								dataQuery.add(DataCriteria.eq(PersonInfoDetailedFields.field_DCD_WYBH, "0"));
							}
							dataQuery.addSelectField(PersonInfoDetailedFields.field_DCD_WYBH);
							batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
							batchQuery.getDataQuery().setLimit(1, maxNum);
							batchQuery.getTotal();
							while (batchQuery.hasNext()) {
								List<Map<String, Object>> detailDataList = batchQuery.next();
								for (Map<String, Object> curMap : detailDataList) {
									String md5 = (String) curMap.get(PersonInfoDetailedFields.field_DCD_WYBH);
									//去除已存在的人员详细信息
									personInfoDetailedMap.remove(md5);
								}
							}
							
							if (personInfoDetailedMap.size() > 0) {
								List<PersonInfoDetailed> personInfoDetailedList = new ArrayList<>(personInfoDetailedMap.values());
								List<Map<String, Object>> savePersonDetailedMapList = BeanUitl.beanToMaps(personInfoDetailedList);
								DirInfo personInfoDetailedDirInfo = dirInfoQueryApi.getDirInfoByTableName(PersonInfoDetailed.tableName, ZygxEnums.dirItems);
								saveDataApi.addDirInfoData(personInfoDetailedDirInfo, savePersonDetailedMapList, false, userInfo, null);
							}
							endTime = System.currentTimeMillis();
							System.out.println("--------------------【查询人员主表信息,并组装人员子表信息保存】结束，共耗时：" + (endTime - beginTime) / 1000 + "秒------------------------");
							System.out.println("");
							System.out.println("");
							
							personInfoDetailedMap = new HashMap<>();
							detailedMd5s = new HashSet<String>();
							dataCriteriaOr = new ArrayList<>();
							dataCriteriaAnd = new ArrayList<>();
						}
						// 同步redis导入数量
						ReadinStatusAssist.setDataImportNum(importHKey, count);
					}
				}
				
				ReadinStatusAssist.isImportError(importHKey, "正在处理数据，请稍后...");
				
				//查询案件信息----用做自动办结
				Map<String, Map<String, Object>> caseInfoMapByAjbh = new HashMap<String,Map<String,Object>>();
				dataQuery = DataQuery.create(DgCaseFields.tableName);
				if(ajbhSet.size() > 0){
					dataQuery.add(DataCriteria.in(DgCaseFields.AJBH, ajbhSet));
				}else{
					dataQuery.add(DataCriteria.eq(DgCaseFields.AJBH, "0"));
				}
				batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
				batchQuery.getDataQuery().setLimit(1, maxNum);
				batchQuery.getTotal();
				while(batchQuery.hasNext()){
					List<Map<String, Object>> dataList = batchQuery.next();
					for (Map<String, Object> map : dataList) {
						map.get(DgCaseFields.JA_JASJ);
						caseInfoMapByAjbh.put((String)map.get(DgCaseFields.AJBH), map);
					}
				}
				
				Object storeVal , newVal;
				List<Map<String, Object>> updatePersonCollisonList = new ArrayList<Map<String,Object>>();
				dataQuery = DataQuery.create(PersonCollisonInfo.tableName);
				dataQuery.add(DataCriteria.in(PzryxxFields.field_SFZH, sfzSet));
				//dataQuery.add(DataCriteria.in(PzryxxFields.field_PZBH, pzbhList));
				batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
				batchQuery.getDataQuery().setLimit(1, maxNum);
				batchQuery.getTotal();
				Date xfsjDate = new Date();
				xfsjDate.setSeconds(date.getSeconds() + 1);
				Date bjsjDate = new Date();
				bjsjDate.setSeconds(date.getSeconds() + 2);
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String xfsjDateFormat = sf.format(xfsjDate);
				Map<String,Object> caseInfoMap = null;
				Set<String> authXfPzbhSet = new HashSet<String>();
				String storeSjzt = "";
				boolean isAutoXf = false;
				boolean isAutoBj = false;
			
				Set<Entry<String, Map<String, Object>>> entrySet = storePersonCollisonInfoMap.entrySet();
				for (Entry<String, Map<String, Object>> e : entrySet) {
					Map<String, Object> map = e.getValue();
					
					isAutoXf = false;
					isAutoBj = false;
					String pzbh =(String) map.get(PzryxxFields.field_PZBH);
					ajbh =(String) map.get(PzryxxFields.field_AJBH);
					sfz = (String) map.get(PzryxxFields.field_SFZH);
					caseInfoMap = caseInfoMapByAjbh.get(ajbh);
					if(storePersonCollisonInfoMap.get(pzbh) != null){
						storePersonCollisonInfo = (PersonCollisonInfo) BeanUitl.convertMap(PersonCollisonInfo.class, storePersonCollisonInfoMap);
					}
					
					if(storePersonCollisonInfo != null){
						dnaTaskDetailedInfo = new DnaTaskDetailedInfo();
						dnaTaskDetailedInfo.setDcd_zrwid(pzbh);
						if(caseInfoMap != null){
							if(caseInfoMap.get(DgCaseFields.JA_JASJ) != null && StringUtils.isNotBlank(caseInfoMap.get(DgCaseFields.JA_JASJ).toString())){
								isAutoBj = true;
							}
						}
						dnaTaskDetailedInfo.setDcd_rwxxzt(Dictionary.比对任务状态_网监返回);
						dnaTaskDetailedInfo.setDcd_czr("系统");
						dnaTaskDetailedInfo.setDcd_rwzxsj(date);
						dnaTaskDetailedInfo.setDcd_wybh(UUID.randomUUID());
						dnaTaskDetailedInfo.setDcd_pzbh(pzbh);
						
						newPersonCollisonInfo = personCollisonMapBySfzh.get(sfz);
						Map<String, Object> newDataMap = BeanUitl.beanToMap(newPersonCollisonInfo);
						Set<String> keySet = newDataMap.keySet();
						for (String fieldName : keySet) {
							if(fieldName.toUpperCase().equals(PzryxxFields.field_XFSJ)){
								if(map.get(PzryxxFields.field_SLDW) != null && map.get(PzryxxFields.field_XFSJ) == null){
									newDataMap.put(fieldName, xfsjDateFormat);
									isAutoXf = true;
									authXfPzbhSet.add(pzbh);
									continue;
								}
							}
							newVal = newDataMap.get(fieldName);
							storeVal = map.get(fieldName);
							if(fieldNameSet.contains(fieldName)){
								if(StringUtils.isBlank(newVal)){
									if(StringUtils.isBlank(storeVal)){
										continue;
									}
								}
								if(newVal != null && newVal.getClass().equals(String.class)){
									/*int indexOf = newVal.toString().indexOf("，");
									//newVal = newVal.toString().replaceAll(";", ",");
									if(indexOf > -1){
										newVal = newVal.toString().replaceAll("，", ",");
										newDataMap.put(fieldName, newVal);
									}*/
									/*if(StringUtils.isNotBlank(storeVal)){
										String[] newValStr = newVal.toString().split(",");
										newVal = storeVal.toString();
										for (String val : newValStr) {
											if(storeVal.toString().indexOf(val) == -1){
												newVal += "," + val;
											}
										}
									}*/
								}
								if(fieldName.toUpperCase().equals(PzryxxFields.field_SJZT)){
									storeSjzt = (String)map.get(fieldName);
									if(storeSjzt.equals(Dictionary.比对结果_推送网监)){
										if(map.get(PzryxxFields.field_XFSJ) == null){
											map.put(PzryxxFields.field_YSBZ, Dictionary.颜色标志_绿色);
											map.put(PzryxxFields.field_LQXSYTS, 3);
										}
									}
									//已有一下3个状态的数据，不再改状态
									if(!storeSjzt.equals(Dictionary.比对结果_办结) && !storeSjzt.equals(Dictionary.比对结果_在侦) && !storeSjzt.equals(Dictionary.比对结果_已排他) ){
										map.put(fieldName.toUpperCase(), Dictionary.比对结果_网监返回);
									}
								}else{
									map.put(fieldName.toUpperCase(), newVal);
								}
								dirItem = personCollisonInfoDirItemMap.get(fieldName.toUpperCase());
								if(!fieldName.toUpperCase().equals(PzryxxFields.field_SJZT)){
									if(dirItem != null){
										dnaTaskInfoService.buildChangeInfo(dirItem.getItemName(), storeVal, newVal, dnaTaskDetailedInfo);
									}
								}
							}
						}
						String dcd_bgnr = dnaTaskDetailedInfo.getDcd_bgnr();
						if(StringUtils.isBlank(dcd_bgnr)){
							dnaTaskDetailedInfo.setDcd_bgnr("无变化");
						}
						saveTaskDetailedList.add(dnaTaskDetailedInfo);
						//自动下发处理
						if(isAutoXf){
							dirItem = personCollisonInfoDirItemMap.get(PzryxxFields.field_XFSJ);
							if(dirItem != null){
								newVal = newDataMap.get(PzryxxFields.field_XFSJ.toLowerCase());
								storeVal = map.get(PzryxxFields.field_XFSJ);
								map.put(PzryxxFields.field_XFSJ, newVal);
								dnaTaskDetailedInfo = new DnaTaskDetailedInfo();
								dnaTaskDetailedInfo.setDcd_zrwid(pzbh);
								dnaTaskDetailedInfo.setDcd_czr("系统");
								dnaTaskDetailedInfo.setDcd_rwxxzt(Dictionary.比对任务状态_自动下发);
								dnaTaskDetailedInfo.setDcd_rwzxsj(xfsjDate);
								dnaTaskDetailedInfo.setDcd_wybh(UUID.randomUUID());
								dnaTaskDetailedInfo.setDcd_pzbh(pzbh);
								
								dnaTaskInfoService.buildChangeInfo(dirItem.getItemName(), storeVal, newVal, dnaTaskDetailedInfo);
							}
							saveTaskDetailedList.add(dnaTaskDetailedInfo);
						}
						
						//自动办结处理
						if(isAutoBj){
							//已办结的数据无需再办结
							if(!storeSjzt.equals(Dictionary.比对结果_办结)){
								dirItem = personCollisonInfoDirItemMap.get(PzryxxFields.field_SJZT);
								if(dirItem != null){
									newVal = newDataMap.get(PzryxxFields.field_SJZT.toLowerCase());
									storeVal = map.get(PzryxxFields.field_SJZT);
									map.put(PzryxxFields.field_SJZT, Dictionary.比对结果_办结);
									map.put(PzryxxFields.field_YSBZ, "");
									map.put(PzryxxFields.field_LQXSYTS, "");
									dnaTaskDetailedInfo = new DnaTaskDetailedInfo();
									dnaTaskDetailedInfo.setDcd_zrwid(pzbh);
									dnaTaskDetailedInfo.setDcd_czr("系统");
									dnaTaskDetailedInfo.setDcd_rwxxzt(Dictionary.比对任务状态_办结);
									dnaTaskDetailedInfo.setDcd_rwzxsj(bjsjDate);
									dnaTaskDetailedInfo.setDcd_wybh(UUID.randomUUID());
									dnaTaskDetailedInfo.setDcd_pzbh(pzbh);
									//dnaTaskInfoService.buildChangeInfo(dirItem.getItemName(), storeVal, newVal, dnaTaskDetailedInfo);
								}
								saveTaskDetailedList.add(dnaTaskDetailedInfo);
							}
						}
						updatePersonCollisonList.add(map);
					}
				}
			
				System.out.println("");
				System.out.println("");
				System.err.println("--------------------【保存新碰撞人员信息】开始------------------------");
				//List<Map<String, Object>> savePersonCollisonInfoList = BeanUitl.beanToMaps(personCollisonInfoList);
				saveDataApi.updateDirInfoData(personCollisonInfoDirInfo, updatePersonCollisonList, false, userInfo, null);
				endTime = System.currentTimeMillis();
				System.out.println("--------------------【保存新碰撞人员信息】结束，共耗时：" + (endTime - beginTime) / 1000 + "秒------------------------");
				System.out.println("");
				System.out.println("");

				// 处理任务流程-start
				dataQuery = DataQuery.create(DnaTaskInfo.tableName);
				if(taskInfoRwbhs.size() == 0){
					dataQuery.add(DataCriteria.eq("DCD_RWBH", "0"));
				}else{
					dataQuery.add(DataCriteria.in("DCD_RWBH", taskInfoRwbhs));
				}
				batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
				batchQuery.getDataQuery().setLimit(1, maxNum);
				batchQuery.getTotal();
				/*SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String sfDate = sf.format(date);
				String updateTaskInfoSQL = "update "+DnaTaskInfo.tableName+" set DCD_RWZT = '"+Dictionary.比对任务状态_网监返回+"' ,DCD_RWZXSJ = to_date('"+sfDate+"','yyyy-mm-dd hh24:mi:ss') where DCD_RWBH in()";*/
				ReadinStatusAssist.isImportError(importHKey, "正在记录日志，请稍后...");
				while (batchQuery.hasNext()) {
					List<Map<String, Object>> taskInfoList = batchQuery.next();
					for (Map<String, Object> map : taskInfoList) {
						String mainTaskBh = (String) map.get("DCD_RWBH");
						if(authXfPzbhSet.contains(mainTaskBh)){
							dnaTaskInfo = (DnaTaskInfo) BeanUitl.convertMap(DnaTaskInfo.class, map);
							dnaTaskInfo.setDcd_rwzt(Dictionary.比对任务状态_自动下发);
							dnaTaskInfo.setDcd_rwzxsj(xfsjDate);
						}else{
							dnaTaskInfo = (DnaTaskInfo) BeanUitl.convertMap(DnaTaskInfo.class, map);
							dnaTaskInfo.setDcd_rwzt(Dictionary.比对任务状态_网监返回);
							dnaTaskInfo.setDcd_rwzxsj(date);
						}
						updateTaskInfoList.add(dnaTaskInfo);
					}
				}
				dnaTaskInfoService.updateTaskLog(updateTaskInfoList);
				dnaTaskInfoService.saveTaskDetailedLog(saveTaskDetailedList);
				// 处理任务流程-end
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
		ReadinStatusAssist.isImportError(importHKey, "导入数据完成");
		// 结束导出
		ReadinStatusAssist.finishImport(importHKey);
		long endTime1 = System.currentTimeMillis();
		System.out.println("--------------------【网监返回数据更新程序】结束，共耗时：" + (endTime1 - beginTime1) / 1000 + "秒------------------------");
		System.out.println("");
		System.out.println("");
		System.err.println("------------------------------------------------------------------------------------------------导入完毕-------------------------------------------------");
	}


	/**
	 * 构建人员子表信息(网监返回)
	 * 
	 * @param sjlx
	 * @param sjz
	 * @param ajbh
	 * @param mainSjbh
	 * @param date
	 * @param saveList
	 * @param personInfoDetailedMap
	 * @param detailedMd5s
	 */
	private void buildPersonDetailed(String sjlx, String sjz, String ajbh, String mainSjbh, Date date, List<PersonInfoDetailed> saveList, Map<String, PersonInfoDetailed> personInfoDetailedMap, Set<String> detailedMd5s) {
		PersonInfoDetailed personInfoDetailed = null;
		String md5 = "";
		String[] sjzStr = sjz.split(";");
		for (String csjz : sjzStr) {
			personInfoDetailed = new PersonInfoDetailed();
			md5 = MD5.md5(mainSjbh + sjlx + csjz + ajbh);
			detailedMd5s.add(md5);
			personInfoDetailed.setDcd_sjlx(sjlx);
			personInfoDetailed.setDcd_sjz(csjz);
			personInfoDetailed.setDcd_ajbh(ajbh);
			personInfoDetailed.setDcd_wybh(md5);
			personInfoDetailed.setDcd_xzsj(date);
			personInfoDetailed.setDcd_zbsjbh(mainSjbh);
			personInfoDetailed.setDcd_sjly(Dictionary.比对数据来源_网监返回);
			personInfoDetailedMap.put(md5, personInfoDetailed);
			saveList.add(personInfoDetailed);
		}
	}
	
	/**
	 * 构建人员子表信息(系统碰撞)
	 * @param mainInfoId
	 * @param sjlx
	 * @param val
	 * @param ajbh
	 * @param jzrybh
	 * @param date
	 * @param personInfoDetailedList
	 * @param detailedWybhSet 
	 */
	private void buildPersonDetailedByInit(String mainInfoId,String sjlx,String val,String ajbh,String jzrybh,Date date,List<PersonInfoDetailed> personInfoDetailedList, Set<String> detailedWybhSet){
		String md5 = "";
		md5 = MD5.md5(mainInfoId + sjlx + val + ajbh);
		detailedWybhSet.add(md5);
		PersonInfoDetailed personInfoDetailed = new PersonInfoDetailed();
		personInfoDetailed.setDcd_wybh(md5);
		personInfoDetailed.setDcd_sjlx(sjlx);
		personInfoDetailed.setDcd_sjz(val);
		personInfoDetailed.setDcd_sjly(Dictionary.比对数据来源_系统碰撞);
		personInfoDetailed.setDcd_rybh(jzrybh);
		personInfoDetailed.setDcd_xzsj(date);
		personInfoDetailed.setDcd_zbsjbh(mainInfoId);
		personInfoDetailed.setDcd_ajbh(ajbh);
		personInfoDetailedList.add(personInfoDetailed);
	}
	
	/**
	 * 计算离到期天数
	 * @throws Exception
	 */
	public void countTimeLimit() throws Exception{
		DataQuery dataQuery = DataQuery.create(PersonCollisonInfo.tableName);
		//dataQuery.add(DataCriteria.isNotNull(PzryxxFields.field_XFSJ));
		dataQuery.add(DataCriteria.isNotNull(PzryxxFields.field_SLDWBH));
		dataQuery.add(DataCriteria.or(DataCriteria.eq(PzryxxFields.field_SJZT, Dictionary.比对结果_网监返回),DataCriteria.eq(PzryxxFields.field_SJZT, Dictionary.比对结果_初始结果)));
		
		BatchQueryImpl batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
		batchQuery.getDataQuery().setLimit(1, maxNum);
		batchQuery.getTotal();
		Date sysDate = new Date();
		Date dataDate = null;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		sysDate = sf.parse(sf.format(sysDate));
		
		DirInfo dirInfo = dirInfoQueryApi.getDirInfoByTableName(PersonCollisonInfo.tableName, ZygxEnums.dirItems);
		UserInfo userInfo = AssistUtils.getCurrUserInfo();
		while(batchQuery.hasNext()){
			List<Map<String, Object>> dataList = batchQuery.next();
			for (Map<String, Object> map : dataList) {
				String sjzt = (String)map.get(PzryxxFields.field_SJZT);
				if(Dictionary.比对结果_网监返回.equals(sjzt)){
					if(map.get(PzryxxFields.field_XFSJ) != null){
						dataDate = (Date)map.get(PzryxxFields.field_XFSJ);
					}
				}else if(Dictionary.比对结果_初始结果.equals(sjzt)){
					if(map.get(PzryxxFields.field_FPSJ) != null){
						dataDate = (Date)map.get(PzryxxFields.field_FPSJ);
					}
				}
				
				if(dataDate != null){
					String sfDate = sf.format(dataDate);
					dataDate = sf.parse(sfDate);
					//long countDay= (dataDate.getTime() - sysDate.getTime())/(24*60*60*1000);
					long limitTime = dataDate.getTime() + 24*60*60*1000*3;
					long countDay= (limitTime - sysDate.getTime())/(24*60*60*1000);
					if(countDay >= 3){
						map.put(PzryxxFields.field_YSBZ, Dictionary.颜色标志_绿色);
					}else {
						if(countDay == 2){
							map.put(PzryxxFields.field_YSBZ, Dictionary.颜色标志_黄色);
						}
						if(countDay <= 1){
							map.put(PzryxxFields.field_YSBZ, Dictionary.颜色标志_红色);
						}
					}
					//countDay = Math.abs(countDay);
					map.put(PzryxxFields.field_LQXSYTS, countDay);
				}
			}
			saveDataApi.updateDirInfoData(dirInfo, dataList, false, userInfo, null);
		}
	}
	
	
	public Map<String,Org> getOrgMap(){
		//组装机构map
		Map<String, Org> orgMap = new HashMap<String,Org>();
		Set<String> orgCodes = SubofficeOrgUtils.getOrgCodes();
		Set<String> newOrgCodes = new HashSet<String>();
		for (String orgCode : orgCodes) {
			newOrgCodes.add(orgCode + "000000");
		}
		Query query = Query.create(Org.class);
		query.add(Criteria.in("organizationCode", newOrgCodes));
		QueryResult orgResult = orgService.getOrg(query);
		List<Org> orgList = orgResult.getResultList();
		for (Org curOrg : orgList) {
			orgMap.put(curOrg.getOrganizationCode(), curOrg);
		}
		return orgMap;
	}
}
