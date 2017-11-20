package com.borosoft.ypfx.dnaCollision.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.borosoft.entityquery.EntityQuery;
import com.borosoft.framework.commons.QueryResult;
import com.borosoft.inside.client.holder.JsonBean;
import com.borosoft.ypfx.dnaCollision.entity.DnaInfo;

public interface DnaCollisionService {

	public List<DnaInfo> getDnaInfoByQuery(EntityQuery entityQuery);

	/**
	 * 碰撞结果更新
	 * @param request
	 * @param jsonBean
	 * @return
	 * @throws Exception 
	 */
	public Object collisionUpdate(HttpServletRequest request, JsonBean jsonBean) throws Exception;

	/**
	 * 排他或推送处理
	 * @param request
	 * @param jsonBean
	 * @return
	 * @throws Exception 
	 */
	public Object collisionDeal(HttpServletRequest request, JsonBean jsonBean) throws Exception;

	/**
	 * 手动下发
	 * @param ids
	 * @param orgId 
	 */
	public void manualIssue(String ids, String orgId) throws Exception;

	/**
	 * 获取关联案件
	 * @param idCard
	 * @param searchMap
	 * @return
	 */
	public QueryResult getLinkCaseInfo(String idCard, Map<String, Object> searchMap);
}
