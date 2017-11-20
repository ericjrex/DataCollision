package com.borosoft.ypfx.dnaCollision.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.borosoft.commons.utils.AssistUtils;
import com.borosoft.commons.utils.UserInfo;
import com.borosoft.component.delete.DeleteDataApi;
import com.borosoft.component.query.holder.AassembleRequestQuery;
import com.borosoft.component.save.SaveDataApi;
import com.borosoft.component.save.holder.SaveResult;
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
import com.borosoft.framework.configuration.Config;
import com.borosoft.framework.exception.BusinessException;
import com.borosoft.framework.utils.StringUtils;
import com.borosoft.inside.client.holder.JsonBean;
import com.borosoft.middleware.dirEntry.Input;
import com.borosoft.platform.org.domain.Org;
import com.borosoft.platform.org.service.OrgService;
import com.borosoft.query.query.DataQuery;
import com.borosoft.query.query.ui.DataCriteria;
import com.borosoft.query.query.ui.Order;
import com.borosoft.query.source.jdbc.JdbcQuerier;
import com.borosoft.ypfx.Dictionary;
import com.borosoft.ypfx.dnaCollision.entity.DnaInfo;
import com.borosoft.ypfx.dnaCollision.entity.DnaTaskDetailedInfo;
import com.borosoft.ypfx.dnaCollision.entity.DnaTaskInfo;
import com.borosoft.ypfx.dnaCollision.entity.PersonCollisonInfo;
import com.borosoft.ypfx.dnaCollision.entity.PersonCollisonInfoSide;
import com.borosoft.ypfx.dnaCollision.entity.PersonInfoDetailed;
import com.borosoft.ypfx.dnaCollision.entity.PersonInfoMian;
import com.borosoft.ypfx.dnaCollision.entity.rules.DgCaseFields;
import com.borosoft.ypfx.dnaCollision.entity.rules.PersonCollisonInfoSideFields;
import com.borosoft.ypfx.dnaCollision.entity.rules.PersonInfoDetailedFields;
import com.borosoft.ypfx.dnaCollision.entity.rules.PzryxxFields;
import com.borosoft.ypfx.dnaCollision.entity.rules.RyxxzbFields;
import com.borosoft.ypfx.dnaCollision.entity.rules.XyrxxFields;
import com.borosoft.ypfx.dnaCollision.entity.rules.XyrxxMiddFields;
import com.borosoft.ypfx.dnaCollision.service.DnaCollisionService;
import com.borosoft.ypfx.dnaCollision.service.DnaTaskInfoService;
import com.borosoft.ypfx.dnaCollision.utils.BeanUitl;
import com.borosoft.ypfx.dnaCollision.utils.ImportHolder;

@Component("dnaCollisionService")
public class DnaCollisionServiceImpl implements DnaCollisionService {

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
	private JdbcTemplate dirDataJdbcTemplate;
	
	@Resource
	private OrgService orgService;
	//碰撞人员信息表不用于修改页面更新的字段
	private static Set<String> noUpdateFieldSet = new HashSet<>();
	
	static{
		noUpdateFieldSet.add(PzryxxFields.field_AJBH);
		noUpdateFieldSet.add(PzryxxFields.field_FPSJ);
		noUpdateFieldSet.add(PzryxxFields.field_LQXSYTS);
		noUpdateFieldSet.add(PzryxxFields.field_PZBH);
		//noUpdateFieldSet.add(PzryxxFields.field_SFZH);
		//noUpdateFieldSet.add(PzryxxFields.field_SJZT);
		noUpdateFieldSet.add(PzryxxFields.field_SLDW);
		noUpdateFieldSet.add(PzryxxFields.field_SLDWBH);
		noUpdateFieldSet.add(PzryxxFields.field_XFSJ);
		noUpdateFieldSet.add(PzryxxFields.field_XM);
		noUpdateFieldSet.add(PzryxxFields.field_YSBZ);
		noUpdateFieldSet.add(PzryxxFields.field_ZHBHSJ);
	}
	
	@Override
	public List<DnaInfo> getDnaInfoByQuery(EntityQuery entityQuery) {
		List<DnaInfo> queryForEntity = entityService.queryForEntity(entityQuery);
		return queryForEntity;
	}


	// 碰撞结果修改
	@Override
	public Object collisionUpdate(HttpServletRequest request, JsonBean jsonBean) throws Exception{
		UserInfo userInfo = AssistUtils.getCurrUserInfo();
		// 获取页面传值
		Input input = Input.create();
		input.setParameter(request);
		Map<String, Object> dataMap = input.getParameters();
		String isPush = request.getParameter("isPush");
		Object sfzh = dataMap.get(PzryxxFields.field_SFZH);
		
		if(sfzh == null){
			throw new BusinessException("身份证不能为空");
		}
		
		// 特殊字段数组和对应字典
		String[] fields = { PzryxxFields.field_SJH, PzryxxFields.field_QQ, PzryxxFields.field_WX, PzryxxFields.field_IMEI, PzryxxFields.field_IMSI, PzryxxFields.field_MAC, PzryxxFields.field_DZ,PzryxxFields.field_JD,PzryxxFields.field_TB,PzryxxFields.field_JD0,PzryxxFields.field_WD,PzryxxFields.field_CPH };
		String[] dicts = { Dictionary.比对数据类型_手机号, Dictionary.比对数据类型_QQ, Dictionary.比对数据类型_微信, Dictionary.比对数据类型_IMEI, Dictionary.比对数据类型_IMSI, Dictionary.比对数据类型_MAC, Dictionary.比对数据类型_地址,Dictionary.比对数据类型_京东,Dictionary.比对数据类型_淘宝,Dictionary.比对数据类型_经度,Dictionary.比对数据类型_纬度,Dictionary.比对数据类型_车牌号};

		String curDataId = "";
		for (int i = 0; i < fields.length; i++) {
			if (StringUtils.isNotBlank(dataMap.get(fields[i]))) {
				String fieldValue = dataMap.get(fields[i]).toString();
				//String replaceValue = fieldValue.replace("，", ",");
				dataMap.put(fields[i], fieldValue);
			}
		}
		//当前修改数据id
		curDataId = (String)dataMap.get(ReservedFields.ID);
		DataQuery dataQuery = DataQuery.create(PersonCollisonInfo.tableName);
		dataQuery.add(DataCriteria.eq(ReservedFields.ID, curDataId));
		List<Map<String, Object>> personCollisonList = jdbcQuerier.queryForList(dataQuery);
		Map<String, Object> storePersonCollisonMap = null;
		if(personCollisonList.size() > 0){
			storePersonCollisonMap = personCollisonList.get(0);
			Set<Entry<String, Object>> entrySet = storePersonCollisonMap.entrySet();
			for (Entry<String, Object> e : entrySet) {
				 String fieldName = e.getKey();
				if(noUpdateFieldSet.contains(fieldName)){
					dataMap.put(fieldName, e.getValue());
				}
			}
			
			dataQuery = DataQuery.create(PzryxxFields.tableName);
			dataQuery.add(DataCriteria.eq(PzryxxFields.field_SFZH, sfzh.toString()));
			List<Map<String, Object>> queryForList = jdbcQuerier.queryForList(dataQuery);
			if(queryForList.size() > 0){
				Map<String, Object> storeMap = queryForList.get(0);
				
				PersonCollisonInfo sotrePersonCollisonInfo = (PersonCollisonInfo) BeanUitl.convertMap(PersonCollisonInfo.class, storePersonCollisonMap);
				PersonCollisonInfo storeRepeatPersonCollisonInfo= (PersonCollisonInfo) BeanUitl.convertMap(PersonCollisonInfo.class, storeMap);
				
				String repeatSldw = storeRepeatPersonCollisonInfo.getDcd_sldw();
				
				PersonCollisonInfoSide personCollisonInfoSide = new PersonCollisonInfoSide();
				personCollisonInfoSide.setXzd_ajbh(sotrePersonCollisonInfo.getDcd_ajbh());
				personCollisonInfoSide.setXzd_sfz(storeRepeatPersonCollisonInfo.getDcd_sfzh());
				personCollisonInfoSide.setXzd_scsj(sotrePersonCollisonInfo.getAddTime());
				personCollisonInfoSide.setXzd_sjzt(storeRepeatPersonCollisonInfo.getDcd_sjzt());
				personCollisonInfoSide.setXzd_sldw(sotrePersonCollisonInfo.getDcd_sldwbh());
				personCollisonInfoSide.setXzd_sldwmc(sotrePersonCollisonInfo.getDcd_sldw());
				personCollisonInfoSide.setXzd_wybh(UUID.randomUUID());
				personCollisonInfoSide.setXzd_xm(sotrePersonCollisonInfo.getDcd_xm());
				personCollisonInfoSide.setXzd_yxfpdw(storeRepeatPersonCollisonInfo.getDcd_sldwbh());
				personCollisonInfoSide.setXzd_yxfpdwmc(storeRepeatPersonCollisonInfo.getDcd_sldw());
				personCollisonInfoSide.setXzd_yxpzsjid(storeRepeatPersonCollisonInfo.getId());
				List<PersonCollisonInfoSide> saveMapInfoSideList = new ArrayList<>();
				saveMapInfoSideList.add(personCollisonInfoSide);
				List<Map<String, Object>> saveMapList= BeanUitl.beanToMaps(saveMapInfoSideList);
				DirInfo sideDirInfo = dirInfoQueryApi.getDirInfoByTableName(PersonCollisonInfoSideFields.tableName, ZygxEnums.dirItems);
				saveDataApi.saveOrUpdateDirInfoData(sideDirInfo, saveMapList, false, userInfo, null);
				
				//删除当前碰撞人员信息
				deletePersonCollisonInfo(sotrePersonCollisonInfo);
				String message= "";
				if(StringUtils.isNotBlank(repeatSldw)){
					message = "该人员已优先分配给【"+repeatSldw+"】处理，可在【优先比中查询】菜单查询详情";
				}else{
					message = "该人员暂无处理单位，需下发人员手工下发";
				}
				
				jsonBean.setMessage(message);
			}else{
				if("true".equals(isPush)){
					dataMap.put(PzryxxFields.field_SFZH, sfzh.toString());
					dataMap.put(PzryxxFields.field_SJZT, Dictionary.比对结果_推送网监);
				}
				// 人员名称
				String name = (String)dataMap.get(PzryxxFields.field_XM);
				// 案件编号
				String caseCode = (String)dataMap.get(PzryxxFields.field_AJBH);
				

				// 获取目录
				DirInfo dirInfo = dirInfoQueryApi.getDirInfoByTableName(PersonCollisonInfo.tableName, ZygxEnums.dirItems);

				// 保存修改信息
				List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
				dataList.add(dataMap);
				SaveResult saveResult = new SaveResult(dataList.size());
				saveResult = saveDataApi.saveOrUpdateDirInfoData(dirInfo, dataList, false, userInfo, null);
				
				
				String updateSQL = "update "+PersonInfoMian.tableName+" set "+RyxxzbFields.field_DCD_SFZ+" = '"+sfzh.toString()+"' where "+RyxxzbFields.field_DCD_XM+" = '"+name+"' and "+RyxxzbFields.field_XZD_AJBH+" = '"+caseCode+"'";
				dirDataJdbcTemplate.update(updateSQL);
				
				if (saveResult.getSuccessCount() == dataList.size()) {
					/**
					 * 根据修改内容做特殊处理
					 */
					// md5码集合
					List<String> md5s = new ArrayList<String>();

					// 身份证号码
					//String IDNum = dataMap.get(PzryxxFields.field_SFZH).toString();
					
					// 数据编号
					String sjbh = "";
					// 通过人员信息主表查询数据编号
					DataQuery mainPerson = DataQuery.create(PersonInfoMian.tableName);
					//mainPerson.add(DataCriteria.eq(RyxxzbFields.field_DCD_SFZ, IDNum));
					mainPerson.add(DataCriteria.eq(RyxxzbFields.field_DCD_XM, name));
					mainPerson.add(DataCriteria.eq(RyxxzbFields.field_XZD_AJBH, caseCode));
					
					List<Map<String, Object>> mainPersonList = jdbcQuerier.queryForList(mainPerson);
					if (mainPersonList.size() > 0)
						sjbh = mainPersonList.get(0).get(RyxxzbFields.field_DCD_SJBH).toString();

					// 人员子表新信息
					DirInfo detailsInfo = dirInfoQueryApi.getDirInfoByTableName(PersonInfoDetailed.tableName, ZygxEnums.dirItems);
					List<Map<String, Object>> detailsList = new ArrayList<Map<String, Object>>();

					// 循环特殊字段
					for (int i = 0; i < fields.length; i++) {
						if (StringUtils.isNotBlank(dataMap.get(fields[i]))) {
							// 子详细信息字段
							String fieldsData = dataMap.get(fields[i]).toString();

							// 处理中文逗号，并拆分字段
							// fieldsData = fieldsData.replace("，", ",");
							//String[] fieldsDatas = fieldsData.split(",");
							String[] fieldsDatas = fieldsData.split(";");
							// 循环拆分值
							for (int j = 0; j < fieldsDatas.length; j++) {
								// 生成md5码
								String md5 = MD5.md5(sjbh + dicts[i] + fieldsDatas[j] + caseCode);
								md5s.add(md5);

								// 查询该记录是否存在
								dataQuery = DataQuery.create(PersonInfoDetailed.tableName);
								dataQuery.add(DataCriteria.eq(PersonInfoDetailedFields.field_DCD_WYBH, md5));
								List<Map<String, Object>> queryList = jdbcQuerier.queryForList(dataQuery);

								// 当存在则不操作，不存在则插入信息到子信息表
								if (queryList.size() == 0) {
									// 初始化变量
									Map<String, Object> detailsMap = new HashMap<String, Object>();

									String sjly = Dictionary.比对数据来源_人工更新;
									Date xzsj = new Date();

									// 加入map后加入待新增的列表中
									detailsMap.put(PersonInfoDetailedFields.field_DCD_ZBSJBH, sjbh);
									detailsMap.put(PersonInfoDetailedFields.field_DCD_SJLX, dicts[i]);
									detailsMap.put(PersonInfoDetailedFields.field_DCD_SJZ, fieldsDatas[j]);
									detailsMap.put(PersonInfoDetailedFields.field_DCD_SJLY, sjly);
									detailsMap.put(PersonInfoDetailedFields.field_DCD_AJBH, caseCode);
									detailsMap.put(PersonInfoDetailedFields.field_DCD_XZSJ, xzsj);
									detailsMap.put(PersonInfoDetailedFields.field_DCD_WYBH, md5);
									detailsList.add(detailsMap);
								}
							}
						}
					}

					// 插入新增数据
					if (detailsList.size() > 0)
						saveDataApi.saveOrUpdateDirInfoData(detailsInfo, detailsList, false, userInfo, null);

					// 删除修改数据
					if(md5s.size() > 0){
						DataQuery deleteInfo = DataQuery.create(PersonInfoDetailed.tableName);
						deleteInfo.add(DataCriteria.eq(PersonInfoDetailedFields.field_DCD_ZBSJBH, sjbh));
						deleteInfo.add(DataCriteria.eq(PersonInfoDetailedFields.field_DCD_AJBH, caseCode));
						deleteInfo.add(DataCriteria.not(DataCriteria.in(PersonInfoDetailedFields.field_DCD_WYBH, md5s)));
						List<Map<String, Object>> deleteList = jdbcQuerier.queryForList(deleteInfo);
						if (deleteList.size() > 0) {
							String ids = "";
							for (Map<String, Object> map : deleteList) {
								ids += map.get("ID") + ",";
							}
							this.deleteDataApi.deleteDirInfoData(detailsInfo.getDirId(), ids, false);
						}
					}

					// 记录任务流程
					DnaTaskDetailedInfo dnaTaskDetailedInfo = new DnaTaskDetailedInfo();
					
					String md5 = MD5.md5(name + caseCode);
					dnaTaskDetailedInfo.setDcd_zrwid(md5);
					dnaTaskDetailedInfo.setDcd_rwxxzt(Dictionary.比对任务状态_推送网监);
					dnaTaskDetailedInfo.setDcd_rwzxsj(new Date());
					dnaTaskDetailedInfo.setDcd_wybh(UUID.randomUUID());
					
					dnaTaskDetailedInfo.setDcd_pzbh(md5);
					dnaTaskDetailedInfo.setDcd_czr(userInfo.getUserName());
					dnaTaskDetailedInfo.setDcd_czrid(userInfo.getUserId());
					
					Map<String, DirItem> dirItemMap = new HashMap<>();
					for (DirItem dirItem : dirInfo.getDirItems()) {
						dirItemMap.put(dirItem.getFieldName(), dirItem);
					}
					DirItem dirItem = null;
					Set<DirItem> dirItemSet = new HashSet<>();
					Object value, storeVal;
					for (String fieldName : storePersonCollisonMap.keySet()) {
						if (!dirItemMap.containsKey(fieldName)) {
							continue;
						}
						dirItem = dirItemMap.get(fieldName);
						dirItem.setIsEdit("1");
						dirItemSet.add(dirItem);
						value = dataMap.get(fieldName);
						
						storeVal = storePersonCollisonMap.get(fieldName);
						
						if (StringUtils.isBlank(value) ) {
							if(StringUtils.isBlank(storeVal)){
								continue;
							}
						}
						// 放在格式化前面
						storePersonCollisonMap.put(fieldName, value);
						dnaTaskInfoService.buildChangeInfo(dirItem.getItemName(), storeVal, value, dnaTaskDetailedInfo);
					}
					
					List<DnaTaskDetailedInfo> taskDetailedList = new ArrayList<DnaTaskDetailedInfo>();
					taskDetailedList.add(dnaTaskDetailedInfo);

					dnaTaskInfoService.saveTaskDetailedLog(taskDetailedList);
					
					if("true".equals(isPush)){
						jsonBean.setMessage("推送成功");
					}else{
						jsonBean.setMessage("修改成功");
					}
				} else {
					if("true".equals(isPush)){
						jsonBean.setMessage(false, "推送失败");
					}else{
						jsonBean.setMessage(false, "修改失败");
					}
				}
			}
		}
		return jsonBean;
	}

	// 排他或推送处理
	@Override
	public Object collisionDeal(HttpServletRequest request, JsonBean jsonBean) throws Exception {
		UserInfo userInfo = AssistUtils.getCurrUserInfo();
		DirInfo dirInfo = dirInfoQueryApi.getDirInfoByTableName(PersonCollisonInfo.tableName, ZygxEnums.dirItems);
		Set<DirItem> dirItems = dirInfo.getDirItems();
		Map<String, DirItem> dirItemMap = DirItemHolder.getDirItemMapByFieldName(dirItems);
		String[] ids = request.getParameter(ReservedFields.ID).split(",");
		String type = request.getParameter("type");
		//处理描述
		String describe = request.getParameter("describe");
		//处理状态编号
		String dealCode = request.getParameter("dealCode");
		
		String sjzt = "";
		String rwzt = "";

		// 标注状态
		switch (type) {
			case "1":
				sjzt = Dictionary.比对结果_已排他;
				rwzt = Dictionary.比对任务状态_排他处理;
				break;
			case "2":
				sjzt = Dictionary.比对结果_推送网监;
				rwzt = Dictionary.比对任务状态_推送网监;
				break;
			case "3":
				sjzt = Dictionary.比对结果_办结;
				rwzt = Dictionary.比对任务状态_办结;
				break;
			case "4":
				sjzt = Dictionary.比对结果_在侦;
				rwzt = Dictionary.比对任务状态_在侦;
				break;
			default:
				break;
		}
		
		List<DnaTaskInfo> taskList = new ArrayList<DnaTaskInfo>();
		List<DnaTaskDetailedInfo> taskDetailedList = new ArrayList<DnaTaskDetailedInfo>();
		// 根据id查询出所有数据
		DataQuery dataQuery = DataQuery.create(PersonCollisonInfo.tableName);
		dataQuery.add(DataCriteria.in(ReservedFields.ID, ids));
		List<Map<String, Object>> queryList = jdbcQuerier.queryForList(dataQuery);

		//String sfzh = "";
		String xm = "";
		String ajbh = "";
		String mainTaskBh = "";
		String storeSjzt = "";
		Date date = new Date();
		List<String> mainTaskBhList = new ArrayList<String>();
		DnaTaskDetailedInfo dnaTaskDetailedInfo =null;
		DirItem dirItem = null;
		// 循环更改状态
		for (Map<String, Object> map : queryList) {
			storeSjzt = (String)map.get(PzryxxFields.field_SJZT);
			
			if(sjzt.equals(Dictionary.比对结果_办结)){
				if(storeSjzt.equals(Dictionary.比对结果_已排他)){
					throw new BusinessException("已排他数据不能办结");
				}
				if(storeSjzt.equals(Dictionary.比对结果_办结)){
					throw new BusinessException("该数据已办结");
				}
			}
			
			if(sjzt.equals(Dictionary.比对结果_已排他)){
				if(storeSjzt.equals(Dictionary.比对结果_办结)){
					throw new BusinessException("已办结数据不能排他");
				}
				if(storeSjzt.equals(Dictionary.比对结果_已排他)){
					throw new BusinessException("该数据已排他");
				}
			}
			
			if(sjzt.equals(Dictionary.比对结果_在侦)){
				if(storeSjzt.equals(Dictionary.比对结果_在侦)){
					throw new BusinessException("该数据已在侦");
				}
				if(storeSjzt.equals(Dictionary.比对结果_办结)){
					throw new BusinessException("已办结数据不能在侦");
				}
			}
			map.put(PzryxxFields.field_YSBZ, "");
			map.put(PzryxxFields.field_LQXSYTS, "");
			if(!sjzt.equals(storeSjzt)){
				map.put(PzryxxFields.field_SJZT, sjzt);
				map.put(PzryxxFields.field_CLMS, describe);
				map.put(PzryxxFields.field_CLZTBH, dealCode);
				//sfzh = (String)map.get(PzryxxFields.field_SFZH);
				xm = (String)map.get(PzryxxFields.field_XM);
				ajbh = (String)map.get(PzryxxFields.field_AJBH);
				mainTaskBh = MD5.md5(xm + ajbh);
				mainTaskBhList.add(mainTaskBh);
				// 记录任务流程
				dnaTaskDetailedInfo = new DnaTaskDetailedInfo();
				dnaTaskDetailedInfo.setDcd_zrwid(mainTaskBh);
				dnaTaskDetailedInfo.setDcd_rwxxzt(rwzt);
				dnaTaskDetailedInfo.setDcd_rwzxsj(date);
				dnaTaskDetailedInfo.setDcd_wybh(UUID.randomUUID());
				dnaTaskDetailedInfo.setDcd_pzbh(mainTaskBh);
				dnaTaskDetailedInfo.setDcd_czr(userInfo.getUserName());
				dnaTaskDetailedInfo.setDcd_czrid(userInfo.getUserId());
				
				dirItem = dirItemMap.get(PzryxxFields.field_SJZT);
				//dnaTaskInfoService.buildChangeInfo(dirItem.getItemName(), storeSjzt, sjzt, dnaTaskDetailedInfo);
				if(!Dictionary.比对结果_推送网监.equals(sjzt)){
					dirItem = dirItemMap.get(PzryxxFields.field_CLMS);
					dnaTaskInfoService.buildChangeInfo(dirItem.getItemName(), "", describe, dnaTaskDetailedInfo);
					dirItem = dirItemMap.get(PzryxxFields.field_CLZTBH);
					dnaTaskInfoService.buildChangeInfo(dirItem.getItemName(), "", dealCode, dnaTaskDetailedInfo);
				}
				taskDetailedList.add(dnaTaskDetailedInfo);
			}
		}
		
		DnaTaskInfo dnaTaskInfo = null;
		dataQuery = DataQuery.create(DnaTaskInfo.tableName);
		dataQuery.add(DataCriteria.in("DCD_RWBH", mainTaskBhList));
		
		List<Map<String, Object>> taskInfoList = jdbcQuerier.queryForList(dataQuery);
		for (Map<String, Object> map : taskInfoList) {
			dnaTaskInfo = (DnaTaskInfo) BeanUitl.convertMap(DnaTaskInfo.class, map);
			dnaTaskInfo.setDcd_rwzt(rwzt);
			dnaTaskInfo.setDcd_rwzxsj(date);
			taskList.add(dnaTaskInfo);
		}
		
		dnaTaskInfoService.updateTaskLog(taskList);
		dnaTaskInfoService.saveTaskDetailedLog(taskDetailedList);

		// 执行修改
		saveDataApi.saveOrUpdateDirInfoData(dirInfo, queryList, false, userInfo, null);
		
		//更新碰撞副表数据状态
		String updateSQl = "update "+PersonCollisonInfoSideFields.tableName+" set "+PersonCollisonInfoSideFields.field_XZD_SJZT+" = '"+sjzt+"' where "+PersonCollisonInfoSideFields.field_XZD_YXPZSJID+" in("+AssistUtils.warpDataIds(ids)+")";
		dirDataJdbcTemplate.update(updateSQl);
		
		jsonBean.setMessage("修改成功");
		return jsonBean;
	}


	/**
	 * 手动下发
	 * @throws Exception 
	 */
	@Override
	public void manualIssue(String ids,String orgId) throws Exception {
		Org org = orgService.getOrgById(orgId);
		Config config = AppConfig.getConfig();
		//下发期限
		int issuedDeadline = config.getInt("issuedDeadline");
		//分配期限
		int allocationDeadline = config.getInt("allocationDeadline");
		
		String updateSQL = "";
		if(org == null){
			throw new BusinessException("机构查询为空，请联系管理员");
		}
		String orgOrganizationCode = org.getOrganizationCode().substring(0, 6);
		UserInfo userInfo = AssistUtils.getCurrUserInfo();
		Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formatDate = sf.format(date);
		
		String[] idStr = ids.split(",");
		DataQuery dataQuery = DataQuery.create(PzryxxFields.tableName);
		dataQuery.add(DataCriteria.in(ReservedFields.ID, idStr));
		List<Map<String, Object>> queryForList = jdbcQuerier.queryForList(dataQuery);
		
		//记录任务日志
		ArrayList<DnaTaskDetailedInfo> taskDetailedInfoList = new ArrayList<DnaTaskDetailedInfo>();
		DnaTaskDetailedInfo dnaTaskDetailedInfo = null;
		//String sfzh = "";
		String ajbh = "";
		String md5 = "";
		String sjzt = "";
		String id = "";
		String xm = "";
		DirItem dirItem = null;
		Object storeVal , newVal;
		StringBuffer xfrwSb = new StringBuffer();
		StringBuffer fprwSb = new StringBuffer();
		
		StringBuffer xfIdSb = new StringBuffer();
		StringBuffer fpIdSb = new StringBuffer();
		
		DirInfo collisionDirInfo = dirInfoQueryApi.getDirInfoByTableName(PzryxxFields.tableName, ZygxEnums.dirItems);
		Set<DirItem> dirItems = collisionDirInfo.getDirItems();
		Map<String, DirItem> dirItemMap = DirItemHolder.getDirItemMapByFieldName(dirItems);
		for (Map<String, Object> map : queryForList) {
			dnaTaskDetailedInfo = new DnaTaskDetailedInfo();
			id = (String) map.get(ReservedFields.ID);
			//sfzh = (String)map.get(PzryxxFields.field_SFZH);
			ajbh = (String)map.get(PzryxxFields.field_AJBH);
			sjzt = (String)map.get(PzryxxFields.field_SJZT);
			xm = (String)map.get(PzryxxFields.field_XM);
			
			md5 = MD5.md5(xm + ajbh);
			
			dnaTaskDetailedInfo.setDcd_czr(userInfo.getUserName());
			dnaTaskDetailedInfo.setDcd_czrid(userInfo.getUserId());
			dnaTaskDetailedInfo.setDcd_pzbh(md5);
			dnaTaskDetailedInfo.setDcd_wybh(UUID.randomUUID());
			dnaTaskDetailedInfo.setDcd_zrwid(md5);
			if(Dictionary.比对结果_初始结果.equals(sjzt)){
				fprwSb.append(md5).append(",");
				fpIdSb.append(id).append(",");
				dnaTaskDetailedInfo.setDcd_rwxxzt(Dictionary.比对任务状态_手动分配);
			}else{
				xfrwSb.append(md5).append(",");
				xfIdSb.append(id).append(",");
				dnaTaskDetailedInfo.setDcd_rwxxzt(Dictionary.比对任务状态_手动下发);
			}
			dnaTaskDetailedInfo.setDcd_rwzxsj(date);
			
			storeVal = map.get(PzryxxFields.field_XFSJ);
			newVal = date;
			dirItem = dirItemMap.get("DCD_XFSJ");
			dnaTaskInfoService.buildChangeInfo(dirItem.getItemName(), storeVal, newVal, dnaTaskDetailedInfo);
			
			storeVal = map.get(PzryxxFields.field_SLDW);
			newVal = org.getName();
			dirItem = dirItemMap.get("DCD_SLDW");
			dnaTaskInfoService.buildChangeInfo(dirItem.getItemName(), storeVal, newVal, dnaTaskDetailedInfo);
			
			storeVal = map.get(PzryxxFields.field_SLDWBH);
			newVal = orgOrganizationCode;
			dirItem = dirItemMap.get("DCD_SLDWBH");
			dnaTaskInfoService.buildChangeInfo(dirItem.getItemName(), storeVal, newVal, dnaTaskDetailedInfo);
			
			
			taskDetailedInfoList.add(dnaTaskDetailedInfo);
		}
		if(xfrwSb.length() > 0){
			xfrwSb.deleteCharAt(xfrwSb.length()-1);
			updateSQL = "update "+DnaTaskInfo.tableName+" set DCD_RWZT = '"+Dictionary.比对任务状态_手动下发+"' , DCD_RWZXSJ = to_date('"+formatDate+"','yyyy-mm-dd hh24:mi:ss') where DCD_RWBH in("+AssistUtils.warpDataIds(xfrwSb.toString())+")";
			dirDataJdbcTemplate.update(updateSQL);
			
			//保存主信息
			updateSQL = "update "+PzryxxFields.tableName+" set "
					+ ""+PzryxxFields.field_XFSJ+" = to_date('"+formatDate+"','yyyy-mm-dd hh24:mi:ss'),"
					+ ""+PzryxxFields.field_YSBZ+" = '"+Dictionary.颜色标志_绿色+"',"
					+ ""+PzryxxFields.field_LQXSYTS+" = "+issuedDeadline+","
					+ ""+PzryxxFields.field_SLDW+" = '"+org.getName()+"',"+PzryxxFields.field_SLDWBH+" = '"+orgOrganizationCode+"'"
					+ " where "+ReservedFields.ID+" in ("+AssistUtils.warpDataIds(xfIdSb.toString())+")";
			dirDataJdbcTemplate.update(updateSQL);
		}
		
		if(fprwSb.length() > 0){
			fprwSb.deleteCharAt(fprwSb.length()-1);
			updateSQL = "update "+DnaTaskInfo.tableName+" set DCD_RWZT = '"+Dictionary.比对任务状态_手动分配+"' , DCD_RWZXSJ = to_date('"+formatDate+"','yyyy-mm-dd hh24:mi:ss') where DCD_RWBH in("+AssistUtils.warpDataIds(fprwSb.toString())+")";
			dirDataJdbcTemplate.update(updateSQL);
			
			//保存主信息
			updateSQL = "update "+PzryxxFields.tableName+" set "
					+ ""+PzryxxFields.field_FPSJ+" = to_date('"+formatDate+"','yyyy-mm-dd hh24:mi:ss'),"
					+ ""+PzryxxFields.field_YSBZ+" = '"+Dictionary.颜色标志_绿色+"',"
					+ ""+PzryxxFields.field_LQXSYTS+" = "+allocationDeadline+","
					+ ""+PzryxxFields.field_SLDW+" = '"+org.getName()+"',"+PzryxxFields.field_SLDWBH+" = '"+orgOrganizationCode+"'"
					+ " where "+ReservedFields.ID+" in ("+AssistUtils.warpDataIds(fpIdSb.toString())+")";
			dirDataJdbcTemplate.update(updateSQL);
		}
		dnaTaskInfoService.saveTaskDetailedLog(taskDetailedInfoList);
	}
	
	
	@Override
	public QueryResult getLinkCaseInfo(String idCard,Map<String,Object> searchMap){
		QueryResult queryResult = null;
		UserInfo userInfo = AssistUtils.getCurrUserInfo();
		//查询嫌疑人
		DataQuery dataQuery = DataQuery.create(XyrxxFields.tableName);
		dataQuery.add(DataCriteria.eq(XyrxxFields.field_ZJHM, idCard));
		dataQuery.addSelectField(XyrxxFields.field_SYSTEMID);
		List<Map<String, Object>> suspectList = jdbcQuerier.queryForList(dataQuery);
		
		Set<String> rybhSet = new HashSet<>();
		for (Map<String, Object> map : suspectList) {
			String rybh = (String)map.get(XyrxxFields.field_SYSTEMID);
			if(StringUtils.isNotBlank(rybh)){
				rybhSet.add(rybh);
			}
		}
		
		Set<String> ajbhSet = new HashSet<>();
		if(rybhSet.size() > 0){
			dataQuery = DataQuery.create(XyrxxMiddFields.tableName);
			dataQuery.add(DataCriteria.in(XyrxxMiddFields.field_RYBH, rybhSet));
			List<Map<String, Object>> suspectMiddleList = jdbcQuerier.queryForList(dataQuery);
			for (Map<String, Object> map : suspectMiddleList) {
				String ajbh = (String)map.get(XyrxxMiddFields.field_AJBH);
				if(StringUtils.isNotBlank(ajbh)){
					ajbhSet.add(ajbh);
				}
			}
		}
		
		if(ajbhSet.size() > 0){
			DirInfo dirInfo = dirInfoQueryApi.getDirInfoByTableName(DgCaseFields.tableName, ZygxEnums.dirItems);
			Set<DirItem> dirItems = dirInfo.getDirItems();
			dataQuery = AassembleRequestQuery.assemble(DgCaseFields.tableName, dirItems, searchMap, false, userInfo);
			dataQuery.add(DataCriteria.in(DgCaseFields.AJBH, ajbhSet));
			List<Order> orders = dataQuery.getOrders();
			if(orders.size() == 0){
				dataQuery.addOrder(Order.asc(DgCaseFields.LA_LRSJ));
			}
			
			queryResult = jdbcQuerier.query(dataQuery);
		}
	
		return queryResult;
	}
	
	public void deletePersonCollisonInfo(PersonCollisonInfo personCollisonInfo){
		String id = personCollisonInfo.getId();
		String name = personCollisonInfo.getDcd_xm();
		String sfzh = personCollisonInfo.getDcd_sfzh();
		String ajbh = personCollisonInfo.getDcd_ajbh();
		String mainSjbh = MD5.md5(sfzh + name + ajbh);
		
		DataQuery dataQuery = DataQuery.create(PzryxxFields.tableName);
		dataQuery.add(DataCriteria.eq(ReservedFields.ID, id));
		deleteDataApi.deleteDataByDataQuery(dataQuery);
		
		String pzbh = personCollisonInfo.getDcd_pzbh();
		//删除人员主表信息
		dataQuery = DataQuery.create(PersonInfoMian.tableName);
		dataQuery.add(DataCriteria.eq(RyxxzbFields.field_DCD_SJBH, mainSjbh));
		deleteDataApi.deleteDataByDataQuery(dataQuery);
		
		//删除人员子表详细信息
		dataQuery = DataQuery.create(PersonInfoDetailedFields.tableName);
		dataQuery.add(DataCriteria.eq(PersonInfoDetailedFields.field_DCD_ZBSJBH, mainSjbh));
		deleteDataApi.deleteDataByDataQuery(dataQuery);
		
		//删除任务主表信息
		dataQuery = DataQuery.create(DnaTaskInfo.tableName);
		dataQuery.add(DataCriteria.eq("DCD_RWBH", pzbh));
		deleteDataApi.deleteDataByDataQuery(dataQuery);
		
		//删除任务子表信息
		dataQuery = DataQuery.create(DnaTaskDetailedInfo.tableName);
		dataQuery.add(DataCriteria.eq("DCD_PZBH", pzbh));
		deleteDataApi.deleteDataByDataQuery(dataQuery);
	}
	
}
