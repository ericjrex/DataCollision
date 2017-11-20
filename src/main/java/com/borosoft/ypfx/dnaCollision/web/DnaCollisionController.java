package com.borosoft.ypfx.dnaCollision.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.borosoft.commons.utils.AssistUtils;
import com.borosoft.commons.utils.UserInfo;
import com.borosoft.component.imp.status.ReadinStatusAssist;
import com.borosoft.component.jservice.DataAuthHolder;
import com.borosoft.component.page.DirInfoPageHolder;
import com.borosoft.component.query.impl.BatchQueryImpl;
import com.borosoft.component.save.SaveDataApi;
import com.borosoft.dirAuth.entity.DataAuthItem;
import com.borosoft.dirAuth.entity.ResApply;
import com.borosoft.dirAuth.entity.ResApplyItem;
import com.borosoft.dirAuth.queryApi.ResApplyApi;
import com.borosoft.dirInfo.entity.DirGroup;
import com.borosoft.dirInfo.entity.DirInfo;
import com.borosoft.dirInfo.entity.DirItem;
import com.borosoft.dirInfo.enums.ZygxEnums;
import com.borosoft.dirInfo.holder.DictTranslate;
import com.borosoft.dirInfo.holder.DirItemHolder;
import com.borosoft.dirInfo.holder.rules.ReservedFields;
import com.borosoft.dirInfo.queryApi.DirGroupQueryApi;
import com.borosoft.dirInfo.queryApi.DirInfoQueryApi;
import com.borosoft.entityquery.EntityQuery;
import com.borosoft.framework.AppConfig;
import com.borosoft.framework.codec.MD5;
import com.borosoft.framework.codec.UUID;
import com.borosoft.framework.commons.QueryResult;
import com.borosoft.framework.commons.query.Criteria;
import com.borosoft.framework.commons.query.Order;
import com.borosoft.framework.commons.query.Query;
import com.borosoft.framework.configuration.Config;
import com.borosoft.framework.json.JSON;
import com.borosoft.framework.log.BizLogger;
import com.borosoft.framework.utils.StringUtils;
import com.borosoft.framework.web.BaseController;
import com.borosoft.inside.client.holder.JsonBean;
import com.borosoft.middleware.dirEntry.DataGridBean;
import com.borosoft.middleware.holder.DirEntityException;
import com.borosoft.middleware.jservice.MiddlewareJService;
import com.borosoft.platform.dict.domain.Dict;
import com.borosoft.platform.dict.service.DictService;
import com.borosoft.platform.org.domain.Org;
import com.borosoft.platform.org.service.OrgService;
import com.borosoft.query.query.DataQuery;
import com.borosoft.query.query.ui.DataCriteria;
import com.borosoft.query.query.ui.DataCriterion;
import com.borosoft.query.source.jdbc.JdbcQuerier;
import com.borosoft.ypfx.Dictionary;
import com.borosoft.ypfx.dnaCollision.entity.DnaInfo;
import com.borosoft.ypfx.dnaCollision.entity.PersonCollisonInfo;
import com.borosoft.ypfx.dnaCollision.entity.PersonInfoDetailed;
import com.borosoft.ypfx.dnaCollision.entity.PersonInfoMian;
import com.borosoft.ypfx.dnaCollision.entity.rules.DgAlertFields;
import com.borosoft.ypfx.dnaCollision.entity.rules.DgCaseFields;
import com.borosoft.ypfx.dnaCollision.entity.rules.PzryxxFields;
import com.borosoft.ypfx.dnaCollision.jservice.DnaCollisionJservice;
import com.borosoft.ypfx.dnaCollision.service.DnaCollisionService;
import com.borosoft.ypfx.dnaCollision.service.DnaTaskInfoService;
import com.borosoft.ypfx.dnaCollision.thread.AutoCollisionDataThread;
import com.borosoft.ypfx.dnaCollision.thread.AutoCountXfsjThread;
import com.borosoft.ypfx.dnaCollision.utils.BeanUitl;

@RequestMapping("/dna/collision/")
@Controller
public class DnaCollisionController extends BaseController{

	private String pageIndex = "/dna/";
	
	@Resource
	private JdbcQuerier jdbcQuerier;
	
	@Resource
	private DnaCollisionService dnaCollisionService;
	
	@Resource
	private DnaCollisionJservice dnaCollisionJservice;
	
	@Resource
	private DnaTaskInfoService dnaTaskInfoService;
	
	@Resource
	private DirInfoQueryApi dirInfoQueryApi;
	
	@Resource
	private SaveDataApi saveDataApi;
	
	@Resource
	private MiddlewareJService middlewareJService;
	
	@Resource
	private DictService dictService;
	
	@Resource
	private OrgService orgService;
	
	@Resource
	private DirGroupQueryApi dirGroupQueryApi;
	
	@Resource(name = "dataAuthHolder")
	private DataAuthHolder dataAuthHolder;
	
	@Resource
	private ResApplyApi resApplyApi;
	
	@Resource
	private DictTranslate dictTranslate;
	
	@Resource
	private DirInfoPageHolder dirInfoPageHolder;
	
	/**
	 * dna全库碰撞(只适用于第一次初始化数据)  废弃
	 * @param request
	 * @param jsonBean
	 * @return
	 */
	@RequestMapping("dataAllCollisionJob.do")
	@ResponseBody
	public Object dataAllCollisionJob(HttpServletRequest request,JsonBean jsonBean){
		try {
			long beginTime = System.currentTimeMillis();
			System.out.println("");
			System.out.println("");
			System.err.println("--------------------【dna全库碰撞】开始------------------------");
			
			DataQuery dataQuery = DataQuery.create(DnaInfo.tableName);
			//num 用于测试使用
			String num = request.getParameter("num");
			if(num != null && num.equals("1")){
				List<DataCriterion> dataCriterList = new ArrayList<>();
				//dataCriterList.add(DataCriteria.eq("XM", "李平强"));
				//dataCriterList.add(DataCriteria.eq("XM", "李尧强"));
				//dataCriterList.add(DataCriteria.eq("XM", "黄培金"));
				dataCriterList.add(DataCriteria.eq("XM", "龙发恩"));
				dataCriterList.add(DataCriteria.eq("XM", "何金香"));
				dataCriterList.add(DataCriteria.eq("XM", "王勇"));
				dataCriterList.add(DataCriteria.eq("XM", "谢凯龙"));
				dataCriterList.add(DataCriteria.eq("XM", "朱国政"));
				dataCriterList.add(DataCriteria.eq("XM", "刘红桥"));
				dataCriterList.add(DataCriteria.eq("XM", "唐军"));
				dataCriterList.add(DataCriteria.eq("XM", "朱大宝"));
				//dataQuery.add(DataCriteria.or(DataCriteria.eq("XM", "李平强"),DataCriteria.eq("XM", "李尧强")));
				dataQuery.add(DataCriteria.or(dataCriterList));
			}
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date parse = simpleDateFormat.parse("2017-1-1 00:00:00");
			//dataQuery.add(DataCriteria.gte("BZSJ", parse));
			dataQuery.add(DataCriteria.gte("RKSJ", parse));
			BatchQueryImpl batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
			batchQuery.getDataQuery().setLimit(1,1000);
			int total = batchQuery.getTotal();
			double count = total / 1000;
			int index = 0;
			while(batchQuery.hasNext()){
				index ++;
				System.out.println("");
				System.out.println("");
				System.out.println("-------------------------------------------------------------------第【"+index+"】次开始---------运行总次数【"+count+"】---------------------------------------------------------------------");
				System.out.println("");
				System.out.println("");
				List<Map<String, Object>> dataList = batchQuery.next();
				List<DnaInfo> dnaInfoList = new ArrayList<DnaInfo>();
				DnaInfo dnaInfo = null;
				for (Map<String, Object> map : dataList) {
					dnaInfo = (DnaInfo) BeanUitl.convertMap(DnaInfo.class, map);
					
					dnaInfoList.add(dnaInfo);
				}
				dnaCollisionJservice.dnaDataCollision(dnaInfoList);
			}
			
			long endTime = System.currentTimeMillis();
			System.out.println("--------------------【dna全库碰撞】结束，共耗时："+(endTime-beginTime)/1000+"秒------------------------");
			System.out.println("");
			System.out.println("");    
		
		} catch (Exception e) {
			e.printStackTrace();
			jsonBean.setMessage(false, "碰撞失败，原因："+e.getMessage());
		}
		return jsonBean;
	}
	
	@RequestMapping("dataCollisionByTime.do")
	@ResponseBody
	public Object dataCollisionByTime(HttpServletRequest request,JsonBean jsonBean){
		AutoCollisionDataThread autoCollisionDataThread = new AutoCollisionDataThread(dnaCollisionJservice);
		Thread thread = new Thread(autoCollisionDataThread);
		thread.start();
		return "success";
	}
	
	
	@RequestMapping("dataCollisionJob.reform")
	@ResponseBody
	public Object dataCollisionJob(HttpServletRequest request,JsonBean jsonBean,HttpServletResponse response){
		
		try {
			//String ids = request.getParameter("ids");
			
			ServletInputStream inputStream = request.getInputStream();
			int i = 0;
			StringBuffer sb = new StringBuffer();
			while ((i = inputStream.read()) != -1) {
				sb.append((char) i);
			}
			Map<String, Object> map = JSON.toMap(sb.toString());
			@SuppressWarnings("unchecked")
			List<String> idList = (List<String>) map.get("ids");
			StringBuffer ids =new StringBuffer();
			for (String id : idList) {
				ids.append(id).append(",");
			}
			if(StringUtils.isNotBlank(ids)){
				ids.deleteCharAt(ids.length()-1);
			}
			
			if (StringUtils.isNotBlank(ids.toString())) {
				//List<String> isList = JSON.toList(ids.toString());
				EntityQuery entityQuery = EntityQuery.create(DnaInfo.class);
				entityQuery.add(DataCriteria.in(ReservedFields.ID, idList));
				
				List<DnaInfo> dnaInfoByQuery = dnaCollisionService.getDnaInfoByQuery(entityQuery);
				if(dnaInfoByQuery.size() > 0){
					dnaCollisionJservice.dnaDataCollision(dnaInfoByQuery);
				}
			}
			jsonBean.setMessage("success");
		} catch (Exception e) {
			e.printStackTrace();
			BizLogger.setExceptionLog(e);
		}
		return jsonBean;
	}
	
	@RequestMapping("excelUpdate.do")
	@ResponseBody
	public Object excelUpdate(@RequestParam("files") MultipartFile[] multipartFile,HttpServletRequest request,JsonBean jsonBean){
		
		String importHKey = request.getParameter("importHKey");
		try {
			//转换成multi 。。。。
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
			
			List<String> nameList = new ArrayList<String>();
			if(multipartFile.length > 0){
				/*for (MultipartFile file : multipartFiles) {
					nameList.add(file.getOriginalFilename());
				}*/
				/*nameList.add(multipartFile.getOriginalFilename());
				dnaCollisionService.excelUpdate(multipartFile);*/
			}else{
				if(fileMap.size() > 0){
					int fileSize = fileMap.size();
					if(fileSize > 1){
						jsonBean.setMessage(false,"请上传单个文件");
						return jsonBean;
					}
					for (int i = 0; i < fileSize; i++) {
						CommonsMultipartFile commonsMultipartFile =(CommonsMultipartFile) fileMap.get("AreaImgKey"+i);
						if(multipartFile != null){
							InputStream inputStream = commonsMultipartFile.getInputStream();
							FileItem fileItem = commonsMultipartFile.getFileItem();
							String name = fileItem.getName();
							nameList.add(name);
							dnaCollisionJservice.excelUpdate(inputStream, name,importHKey);
						}
					}
				}
			}
			jsonBean.setData(nameList);
		} catch (Exception e) {
			e.printStackTrace();
			String message = e.getMessage();
			jsonBean.setMessage(false, message);
			ReadinStatusAssist.isImportError(importHKey, "导入数据失败，错误原因："+message);
		}
		return jsonBean;
	}
	
	@RequestMapping("excelUpdateByManual.do")
	@ResponseBody
	public Object excelUpdateByManual(@RequestParam("files") MultipartFile multipartFile,HttpServletRequest request,JsonBean jsonBean){
		
		try {
			//转换成multi 。。。。
			List<String> nameList = new ArrayList<String>();
			String importHKey = request.getParameter("importHKey");
			if(multipartFile != null){
				/*for (MultipartFile file : multipartFiles) {
					nameList.add(file.getOriginalFilename());
				}*/
				nameList.add(multipartFile.getOriginalFilename());
				dnaCollisionJservice.excelUpdate(multipartFile,importHKey);
			}
			jsonBean.setData(nameList);
		} catch (Exception e) {
			e.printStackTrace();
			String message = e.getMessage();
			jsonBean.setMessage(false, message);
		}
		return jsonBean;
	}
	
	
	
	@RequestMapping("gotoUploadPage.do")
	public Object gotoUploadPage(HttpServletRequest request,JsonBean jsonBean){
		return pageIndex + "dna_inport.jsp";
	}
	
	// 碰撞结果修改
	@RequestMapping("collisionUpdate.do")
	@ResponseBody
	public Object collisionUpdate(HttpServletRequest request, JsonBean jsonBean) {
		try {
			
			
			jsonBean = (JsonBean)dnaCollisionService.collisionUpdate(request, jsonBean);
		} catch (Exception e) {
			e.printStackTrace();
			BizLogger.setExceptionLog(e);
			jsonBean.setMessage(false,"修改失败，请联系管理员");
		}
		return jsonBean;
	}
	
	// 查看任务流程日志
	@RequestMapping("taskView.do")
	public Object taskView(String configCode, String personID, String caseCode, HttpServletRequest request, JsonBean jsonBean){
		request.setAttribute("configCode", configCode);
		request.setAttribute("mainID", MD5.md5(personID + caseCode));
		
		return pageIndex + "dna_taskDetail.jsp";
	}
	
	// 排他或推送或办结或在侦数据
	@RequestMapping("collisionDeal.do")
	@ResponseBody
	public Object collisionDeal(HttpServletRequest request, JsonBean jsonBean){
		try {
			String id = request.getParameter(ReservedFields.ID);
			if(StringUtils.isBlank(id)){
				jsonBean.setMessage(false,"处理失败，id不能为空");
				return jsonBean;
			}
			dnaCollisionService.collisionDeal(request, jsonBean);
			jsonBean.setMessage("处理成功");
		} catch (Exception e) {
			e.printStackTrace();
			String message = e.getMessage();
			//jsonBean.setMessage("处理失败，请联系管理员");
			jsonBean.setMessage(false,"处理失败，"+message+"");
			BizLogger.setExceptionLog(e);
		}
		return jsonBean;
	}
	
	/**
	 * 根据碰撞人员信息拆分出人员子表信息(不对外用)
	 * @param request
	 * @param jsonBean
	 * @return
	 */
	@RequestMapping("init.do")
	@ResponseBody
	public Object init(HttpServletRequest request,JsonBean jsonBean){
		try {
			List<PersonInfoMian> personInfoMianList = new ArrayList<PersonInfoMian>();
			List<PersonInfoDetailed> personInfoDetList = new ArrayList<PersonInfoDetailed>();
			DataQuery dataQuery = DataQuery.create(PersonCollisonInfo.tableName);
			BatchQueryImpl batchQuery = BatchQueryImpl.newInstance(dataQuery, jdbcQuerier);
			batchQuery.getDataQuery().setLimit(1, 1000);
			batchQuery.getTotal();
			Date date = new Date();
			PersonInfoDetailed personInfoDetailed = null;
			while(batchQuery.hasNext()){
				List<Map<String, Object>> dataList = batchQuery.next();
				for (Map<String, Object> map : dataList) {
					Object ajbh = map.get(PzryxxFields.field_AJBH);
					Object xm = map.get(PzryxxFields.field_XM);
					Object sfzh = map.get(PzryxxFields.field_SFZH);
					Object sjh = map.get(PzryxxFields.field_SJH);
					Object dz = map.get(PzryxxFields.field_DZ);
					PersonInfoMian personInfoMian = new PersonInfoMian();
					String md5 = MD5.md5(sfzh.toString()+xm.toString());
					personInfoMian.setDcd_sfz(sfzh.toString());
					personInfoMian.setDcd_xm(xm.toString());
					personInfoMian.setDcd_sjbh(md5);
					personInfoMianList.add(personInfoMian);
					
					
					
					if(sjh != null){
						String string = sjh.toString();
						String replaceAll = string.replaceAll("，", ",");
						String[] split = replaceAll.split(",");
						for (String string2 : split) {
							personInfoDetailed= new PersonInfoDetailed();
							personInfoDetailed.setDcd_ajbh(ajbh.toString());
							personInfoDetailed.setDcd_sjlx(Dictionary.比对数据类型_手机号);
							personInfoDetailed.setDcd_sjly(Dictionary.比对数据来源_系统碰撞);
							personInfoDetailed.setDcd_sjz(string2);
							personInfoDetailed.setDcd_wybh(UUID.randomUUID());
							personInfoDetailed.setDcd_xzsj(date);
							personInfoDetailed.setDcd_zbsjbh(md5);
							personInfoDetList.add(personInfoDetailed);
						}
						
					}
					if(dz != null){
						String string = dz.toString();
						String replaceAll = string.replaceAll("，", ",");
						String[] split = replaceAll.split(",");
						for (String string2 : split) {
							personInfoDetailed= new PersonInfoDetailed();
							personInfoDetailed.setDcd_ajbh(ajbh.toString());
							personInfoDetailed.setDcd_sjlx(Dictionary.比对数据类型_地址);
							personInfoDetailed.setDcd_sjly(Dictionary.比对数据来源_系统碰撞);
							personInfoDetailed.setDcd_sjz(string2);
							personInfoDetailed.setDcd_wybh(UUID.randomUUID());
							personInfoDetailed.setDcd_xzsj(date);
							personInfoDetailed.setDcd_zbsjbh(md5);
							personInfoDetList.add(personInfoDetailed);
						}
					}
				}
			}
			UserInfo userInfo = AssistUtils.getCurrUserInfo();
			DirInfo personInfoMainDirInfo = dirInfoQueryApi.getDirInfoByTableName(PersonInfoMian.tableName, ZygxEnums.dirItems);
			List<Map<String, Object>> beanToMainMaps = BeanUitl.beanToMaps(personInfoMianList);
			saveDataApi.saveOrUpdateDirInfoData(personInfoMainDirInfo, beanToMainMaps, false, userInfo, null);
			
			
			DirInfo personInfoDetailedDirInfo = dirInfoQueryApi.getDirInfoByTableName(PersonInfoDetailed.tableName, ZygxEnums.dirItems);
			List<Map<String, Object>> beanToMaps = BeanUitl.beanToMaps(personInfoDetList);
			saveDataApi.saveOrUpdateDirInfoData(personInfoDetailedDirInfo, beanToMaps, false, userInfo, null);
		} catch (Exception e) {
			e.printStackTrace();
			jsonBean.setMessage(false, "保存失败");
		}
		return "";
	}
	
	/**
	 * 下载导入模板
	 * 
	 * @param dirInfoId
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/{type}/downloadModel.do", method = RequestMethod.GET)
	public void downloadModel(@PathVariable String type, HttpServletResponse response, HttpServletRequest request) {
		String filePath = request.getServletContext().getRealPath("/") + "WEB-INF/寻踪导入模板."+type;
		downLoadFile(filePath, request, response);
	}
	
	private void downLoadFile(String fileName, HttpServletRequest request, HttpServletResponse response) {
		try {
			File file = new File(fileName);
			if (file.exists()) {
				String name = file.getName();
				name = URLEncoder.encode(name, "UTF-8");
				response.setContentLength((int) file.length());
				response.setHeader("Content-Disposition", "attachment;filename=" + name);// 设置在下载框默认显示的文件名
				response.setContentType("application/octet-stream");// 指明response的返回对象是文件流
				// 读出文件到response
				// 这里是先需要把要把文件内容先读到缓冲区
				// 再把缓冲区的内容写到response的输出流供用户下载
				FileInputStream fileInputStream = new FileInputStream(file);
				BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
				byte[] b = new byte[bufferedInputStream.available()];
				bufferedInputStream.read(b);
				OutputStream outputStream = response.getOutputStream();
				outputStream.write(b);

				bufferedInputStream.close();
				outputStream.flush();
				outputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping("gotoLifeCourse.do")
	public String gotoLifeCourse(HttpServletRequest request){
		String configCode = request.getParameter("configCode");
		String ywId = request.getParameter("ywId");
		//String personID = request.getParameter("personID");
		//String name = request.getParameter("name");
		//String caseCode = request.getParameter("caseCode");
		String pzbh = request.getParameter("pzbh");
		DataQuery dataQuery = DataQuery.create(PzryxxFields.tableName);
		dataQuery.add(DataCriteria.eq(ReservedFields.ID, ywId));
		dataQuery.addSelectField(PzryxxFields.field_PZBH);
		List<Map<String, Object>> queryForList = jdbcQuerier.queryForList(dataQuery);
		if(queryForList.size() > 0){
			Map<String, Object> map = queryForList.get(0);
			pzbh = (String)map.get(PzryxxFields.field_PZBH);
		}
		
		request.setAttribute("configCode", configCode);
		request.setAttribute("pzbh", pzbh);
		
		return pageIndex + "lifeCourse/dna_lifeCourse.jsp";
		
	}
	
	/**
	 * 获取碰撞人员信息生命历程
	 * @param request
	 * @param jsonBean
	 * @return
	 */
	@RequestMapping("getLifeCourseData.do")
	@ResponseBody
	public Object getLifeCourseData(HttpServletRequest request,JsonBean jsonBean) {
		try {
			String pzbh = request.getParameter("pzbh");
			String configCode = request.getParameter("configCode");
			// 获取目录信息
			UserInfo userInfo = AssistUtils.getCurrUserInfo();
			Map<String, Object> searchMap = AssistUtils.parseRequest(request);
			searchMap.put("DCD_ZRWID", pzbh);
			QueryResult queryResult = this.middlewareJService.getDirInfoDataList(configCode, searchMap, userInfo);
			List<Map<String, Object>> queryForList = queryResult.getResultList();
			jsonBean.setData(queryForList);
		} catch (DirEntityException e) {
			e.printStackTrace();
			jsonBean.setMessage(false, "获取碰撞人员信息生命历程失败，请联系管理员");
			BizLogger.setExceptionLog(e);
		}
		return jsonBean;
	}
	
	/**
	 * 计算离到期天数(测试方法)
	 * @return
	 */
	@RequestMapping("countTimeLimit.do")
	@ResponseBody
	public Object countTimeLimit(){
		try {
			AutoCountXfsjThread autoCountXfsjThread = new AutoCountXfsjThread(dnaCollisionJservice);
			Thread thread = new Thread(autoCountXfsjThread);
			thread.start();
			//dnaCollisionJservice.countTimeLimit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
	
	/**
	 * 检测是否存在案件信息或警情信息
	 * @param request
	 * @param jsonBean
	 * @return
	 */
	@RequestMapping("checkCaseInfoExist.do")
	@ResponseBody
	public Object checkCaseInfoExist(HttpServletRequest request,JsonBean jsonBean){
		try {
			String ajbh = request.getParameter("ajbh");
			String tableName = "";
			DataQuery dataQuery = null;
			if(ajbh.startsWith("J")){
				tableName = DgAlertFields.tableName;
				dataQuery = DataQuery.create(tableName);
				dataQuery.add(DataCriteria.eq(DgAlertFields.field_DCD_XGJQBH, ajbh));
			}else{
				tableName = DgCaseFields.tableName;
				dataQuery = DataQuery.create(tableName);
				dataQuery.add(DataCriteria.eq(DgCaseFields.AJBH, ajbh));
			}
			int count = jdbcQuerier.count(dataQuery);
			
			if(count == 0){
				jsonBean.setMessage(false, "无该案件信息");
			}
		} catch (Exception e) {
			e.printStackTrace();
			BizLogger.setExceptionLog(e);
			jsonBean.setMessage(false,"检测案件信息存在失败，请联系管理员");
		}
		return jsonBean;
	}
	
	
	@RequestMapping("gotoViewDirDataPage.do")
	public String gotoViewDirDataPage(HttpServletRequest request){
		String ajbh = request.getParameter("ajbh");
		request.setAttribute("ajbh", ajbh);
		return "/dna/caseInfo/caseInfo_view.jsp";
	}
	
	@ResponseBody
	@RequestMapping("viewDirData.do")
	public Object viewDirData(HttpServletRequest request,JsonBean jsonBean){
		//String id = request.getParameter("id");
		String ajbh = request.getParameter("ajbh");
		String isSingleStr = request.getParameter("isSingle");
		String tableName = "";
		//String caseInfoJson = request.getParameter("caseInfoJson");
		//Map<String, Object> dataMap = JSON.toMap(caseInfoJson);
		Map<String, Object> dataMap = null;
		DataQuery dataQuery = null;
		System.out.println(ajbh);
		if(ajbh.startsWith("J") || ajbh.startsWith("j")){
			tableName = DgAlertFields.tableName;
			dataQuery = DataQuery.create(tableName);
			dataQuery.add(DataCriteria.eq(DgAlertFields.field_AJBH, ajbh));
		}else{
			tableName = DgCaseFields.tableName;
			dataQuery = DataQuery.create(tableName);
			dataQuery.add(DataCriteria.eq(DgCaseFields.AJBH, ajbh));
		}
		System.out.println("表名");
		System.out.println(tableName);
		List<Map<String, Object>> dataList = jdbcQuerier.queryForList(dataQuery);
		if(dataList.size() > 0){
			dataMap = dataList.get(0);
			boolean isSingle = StringUtils.isNotBlank(isSingleStr) && "true".equalsIgnoreCase(isSingleStr); // 是否一行显示一组控件
			
			DirInfo dirInfo = dirInfoQueryApi.getDirInfoByTableName(tableName, ZygxEnums.dirItems);
			System.out.println(dirInfo);
			System.out.println(dirGroupQueryApi);
			List<DirGroup> dirGroups = dirGroupQueryApi.getDetailDirGroups(dirInfo.getId());
			Set<DirItem> viewItems = new HashSet<DirItem>();
			Set<DirItem> dirItems = dirInfo.getDirItems();
			/*for (DirItem dirItem : dirItems) {
				if("1".equals(dirItem.getIsView())){
					viewItems.add(dirItem);
				}
			}	*/
			
			// 格式化数据
			Set<DirItem> authDirItems = dataAuthHolder.getListAuthDiritems(dirInfo, AssistUtils.getCurrUserInfo());
			Set<String> authItemIds = new HashSet<>();
			// 申请查看不脱敏
			Set<String> desensitizeSet = new HashSet<>();
			List<ResApply> resApplys = resApplyApi.getResApplyOnViewDesensitize(dirInfo.getDirId(), AssistUtils.getCurrUserInfo().getOrgId());
			for (ResApply resApply : resApplys) {
				Set<ResApplyItem> resApplyItems = resApply.getResApplyItems();
				for (ResApplyItem resApplyItem : resApplyItems) {
					desensitizeSet.add(resApplyItem.getItemId());
				}
			}
			Map<String, DirItem> authDirItemMap = new HashMap<>();
			for (DirItem dirItem : authDirItems) {
				authItemIds.add(dirItem.getId());
				authDirItemMap.put(dirItem.getFieldName(), dirItem);
				if ("1".equals(dirItem.getIsDesensitize()) && desensitizeSet.contains(dirItem.getId())) {
					dirItem.setIsDesensitize("0");
				}
			}
			Map<String, Map<String, String>> codeSetMap = dictTranslate.warpCodeSetMap(authDirItems);// 信息项字典缓存
			dataAuthHolder.formatDataMap(dirInfo, authDirItemMap, null, dataMap, codeSetMap, true);
			List<DataAuthItem> authItemList = this.dataAuthHolder.getCellDataAuthItems(dirInfo.getDirId(), AssistUtils.getCurrUserInfo());
			TreeSet<DirItem> sortDirItem = DirItemHolder.getSortDirItem(dirItems);
			this.cleanViewItems(authItemList, sortDirItem, viewItems);
			
			String viewForm = dirInfoPageHolder.resolveViewDirInfo(dirInfo, viewItems, dirGroups, authItemIds, dataMap, isSingle);
			jsonBean.setData(viewForm);
			return jsonBean;
		}else{
			jsonBean.setMessage(false,"无该案件信息");
			return jsonBean;
		}
	}
	
	/**
	 * 手动下发
	 * @param request
	 * @param jsonBean
	 * @return
	 */
	@RequestMapping("manualIssue.do")
	@ResponseBody
	public Object manualIssue(HttpServletRequest request,JsonBean jsonBean){
		
		try {
			
			String ids = request.getParameter("ids");
			String orgId = request.getParameter("orgId");
			if(StringUtils.isBlank(ids)){
				jsonBean.setMessage(false, "下发失败，数据id不能为空");
				BizLogger.error(getClass(), "下发失败，数据id不能为空");
				return jsonBean;
			}
			if(StringUtils.isBlank(orgId)){
				jsonBean.setMessage(false, "下发失败，部门不能为空");
				BizLogger.error(getClass(), "下发失败，部门不能为空");
				return jsonBean;
			}
			
			
			dnaCollisionService.manualIssue(ids,orgId);
			
			
			jsonBean.setMessage( "下发成功");
		} catch (Exception e) {
			e.printStackTrace();
			BizLogger.setExceptionLog(e);
			jsonBean.setMessage(false, "下发失败，请联系管理员");
		}
		
		return jsonBean;
	}
	
	
	@RequestMapping("getOrgList.do")
	@ResponseBody
	public Object getOrgList(HttpServletRequest request,JsonBean jsonBean,Query query){
		//List<Map<String, Object>> treeList;
		try {
			String orgName = request.getParameter("orgName");
			String orgCode = request.getParameter("orgCode");
			Config config = AppConfig.getConfig();
			String orgCodes = config.getString("suboffice.orgCode");
			List<String> orgCodeList = new ArrayList<String>();
			
			String[] orgCodeArr = orgCodes.split("\\|");
			for (String code : orgCodeArr) {
				orgCodeList.add(code + "000000");
			}
			query.add(Criteria.in("organizationCode", orgCodeList));
			query.add(Criteria.like("organizationCode", orgCode,MatchMode.ANYWHERE,false));
			query.add(Criteria.like("name", orgName,MatchMode.ANYWHERE,false));
			
			QueryResult queryResult = orgService.getOrg(query);
			List<Org> orgList = queryResult.getResultList();
			for (Org org : orgList) {
				org.setOrganizationCode(org.getOrganizationCode().substring(0, 6));
			}
			/*List<Org> orgList = queryResult.getResultList();
			treeList = new ArrayList<Map<String, Object>>();
			Map<String, Object> treeMap = null;
			Map<String, Object> attrMap = null;
			for (Org org : orgList) {
				String name = org.getName();
				String id = org.getId();
				treeMap = new HashMap<String,Object>();
				attrMap = new HashMap<String,Object>();
				treeMap.put("id", id);
				treeMap.put("text", name);
				treeMap.put("attributes", attrMap);
				treeList.add(treeMap);
			}*/
			//jsonBean.setData(treeList);
			return DataGridBean.create(queryResult);
		} catch (Exception e) {
			e.printStackTrace();
			jsonBean.setMessage(false,"获取分局列表失败，请联系管理员");
			BizLogger.setExceptionLog(e);
		}
		return DataGridBean.create(new ArrayList<>());
	}
	
	@RequestMapping("getDealOPtions.do")
	@ResponseBody
	public Object getDealOPtions(HttpServletRequest request,JsonBean jsonBean){
		String dealCategory = request.getParameter("dealCategory");
		Query query = Query.create(Dict.class);
		List<Criterion> queryAndList = new ArrayList<>();
		queryAndList.add(Criteria.like("code", Dictionary.比对数据结果处理,MatchMode.ANYWHERE));
		queryAndList.add(Criteria.like("customCode", dealCategory,MatchMode.START));
		
		//query.add(Criteria.like("code", Dictionary.比对数据结果处理,MatchMode.ANYWHERE));
		query.add(Criteria.like("customCode", dealCategory,MatchMode.START));
		
		
		
		query.addOrder(Order.asc("customCode"));
		QueryResult queryResult = dictService.getDict(query);
		List<Dict> resultList = queryResult.getResultList();
		
		
		query = Query.create(Dict.class);
		query.add(Criteria.eq("customCode", Dictionary.比对数据结果处理_其他));
		queryResult = dictService.getDict(query);
		List<Dict> resultList2 = queryResult.getResultList();
		resultList.addAll(resultList2);
		
		return resultList;
	}
	
	@RequestMapping("gotoCaseInfoListPage.do")
	public String gotoCaseInfoListPage(HttpServletRequest request){
		String idCard = request.getParameter("idCard");
		request.setAttribute("idCard", idCard);
		return "/dna/caseInfo/caseInfo_index.jsp";
	}
	
	
	@RequestMapping("getLinkCaseInfo.do")
	@ResponseBody
	public Object getLinkCaseInfo(HttpServletRequest request,JsonBean jsonBean){
		DataGridBean dataGridBean = DataGridBean.create();
		try {
			String idCard = request.getParameter("idCard");
			if(StringUtils.isBlank(idCard)){
				jsonBean.setMessage(false, "身份证号不能为空");
				BizLogger.error(getClass(), "身份证号不能为空");
				return jsonBean;
			}
			Map<String, Object> searchMap = AssistUtils.parseRequest(request);
			
			QueryResult queryResult = dnaCollisionService.getLinkCaseInfo(idCard, searchMap);
			
			if(queryResult == null){
				queryResult = new QueryResult();
			}
			dataGridBean.setQueryResult(queryResult);
		} catch (Exception e) {
			e.printStackTrace();
			BizLogger.setExceptionLog(e);
			dataGridBean.setMessage(false, "获取关联案件失败，请联系管理员");
			dataGridBean.setQueryResult(new QueryResult());
		}
		return dataGridBean;
	}
	
	@RequestMapping("checkPersonCollisonIsExist.do")
	@ResponseBody
	public Object checkPersonCollisonIsExist(HttpServletRequest request,JsonBean jsonBean){
		try {
			
			String sfzh = request.getParameter("sfzh");
			if(StringUtils.isBlank(sfzh)){
				jsonBean.setMessage(false, "身份证不能为空");
				return jsonBean;
			}
			
			DataQuery dataQuery = DataQuery.create(PzryxxFields.tableName);
			dataQuery.add(DataCriteria.eq(PzryxxFields.field_SFZH, sfzh.toString()));
			List<Map<String, Object>> queryForList = jdbcQuerier.queryForList(dataQuery);
			if(queryForList.size() > 0){
				Map<String, Object> storeMap = queryForList.get(0);
				PersonCollisonInfo storeRepeatPersonCollisonInfo= (PersonCollisonInfo) BeanUitl.convertMap(PersonCollisonInfo.class, storeMap);
				String repeatSldw = storeRepeatPersonCollisonInfo.getDcd_sldw();
				String message= "";
				if(StringUtils.isNotBlank(repeatSldw)){
					message = "该人员身份证已优先分配给【"+repeatSldw+"】处理，还继续推送吗？";
				}else{
					message = "该人员暂无处理单位，需下发人员手工下发，还继续推送吗？";
				}
				jsonBean.setMessage(message);
			}	
		} catch (Exception e) {
			e.printStackTrace();
			jsonBean.setMessage(false, "查询碰撞信息失败，请联系管理员");
			BizLogger.setExceptionLog(e);
		}
		
		
		return jsonBean;
	}
	
	private void cleanViewItems(List<DataAuthItem> authItemList, Set<DirItem> dirItems,Set<DirItem> viewItems) {
		Set<String> viewItemIds = new HashSet<String>();
		if ((authItemList != null && authItemList.size() > 0)) {
			if (authItemList != null) {
				for (DataAuthItem item : authItemList) {
					viewItemIds.add(item.getItemId());
				}
			}
			/*for (ResPerconfigItem ri : resItems) {
				if ("0".equals(ri.getIsView())) {
					viewItemIds.add(ri.getItemId());
				}
			}*/
			if (viewItemIds.size() > 0) {
				for (DirItem sd : dirItems) {
					if (!viewItemIds.contains(sd.getId())) {
						viewItems.add(sd);
					}
				}
			} else {
				viewItems.addAll(dirItems);
			}
		} else {
			viewItems.addAll(dirItems);
		}
	}

}
