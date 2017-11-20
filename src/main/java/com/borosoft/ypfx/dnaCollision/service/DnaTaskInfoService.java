package com.borosoft.ypfx.dnaCollision.service;

import java.util.List;

import com.borosoft.framework.exception.BusinessException;
import com.borosoft.ypfx.dnaCollision.entity.DnaTaskDetailedInfo;
import com.borosoft.ypfx.dnaCollision.entity.DnaTaskInfo;

public interface DnaTaskInfoService {

	public void saveTaskLog(List<DnaTaskInfo> dnaTaskInfos) throws BusinessException;

	public void saveTaskDetailedLog(List<DnaTaskDetailedInfo> dnaTaskDetailedInfos)
			throws BusinessException;

	public void updateTaskLog(List<DnaTaskInfo> dnaTaskInfos) throws BusinessException;

	public void buildChangeInfo(String itemName, Object storeVal, Object value,
			DnaTaskDetailedInfo dnaTaskDetailedInfo);

}
