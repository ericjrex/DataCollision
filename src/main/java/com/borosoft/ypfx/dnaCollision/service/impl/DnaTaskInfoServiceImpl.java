package com.borosoft.ypfx.dnaCollision.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.borosoft.component.save.SaveDataApi;
import com.borosoft.framework.dao.EntityDAO;
import com.borosoft.framework.exception.BusinessException;
import com.borosoft.framework.utils.StringUtils;
import com.borosoft.ypfx.dnaCollision.entity.DnaTaskDetailedInfo;
import com.borosoft.ypfx.dnaCollision.entity.DnaTaskInfo;
import com.borosoft.ypfx.dnaCollision.service.DnaTaskInfoService;
import com.borosoft.ypfx.dnaCollision.utils.DataFormatUtils;

@Service("dnaTaskInfoService")
public class DnaTaskInfoServiceImpl implements DnaTaskInfoService{
	
	@Resource
	private SaveDataApi saveDataApi;
	
	private EntityDAO<DnaTaskInfo, String> dnaTaskInfoDAO;
	
	private EntityDAO<DnaTaskDetailedInfo, String> DnaTaskDetailedInfoDAO;
	
	
	@Override
	public void saveTaskLog(List<DnaTaskInfo> dnaTaskInfos) throws BusinessException{
		dnaTaskInfoDAO.save(dnaTaskInfos);
	}
	
	@Override
	public void updateTaskLog(List<DnaTaskInfo> dnaTaskInfos) throws BusinessException{
		dnaTaskInfoDAO.update(dnaTaskInfos);
	}
	
	/**
	 * 组装日志
	 * @param itemName
	 * @param storeVal
	 * @param newVal
	 */
	@Override
	public void buildChangeInfo(String itemName,Object storeVal,Object newVal,DnaTaskDetailedInfo dnaTaskDetailedInfo){
		storeVal = DataFormatUtils.formatData(storeVal);
		newVal = DataFormatUtils.formatData(newVal);
		String dcd_bgnr = dnaTaskDetailedInfo.getDcd_bgnr();
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isBlank(storeVal)) {
			sb.append(itemName+ "：" + newVal).append("（增加）");
		} else if (!newVal.equals(storeVal)) {
			sb.append(itemName + "：由“" + storeVal + "”变更为“" + newVal + "”");
		}
		if(sb.length() > 0){
			String newBgnr = "";
			if(StringUtils.isNotBlank(dcd_bgnr)){
				newBgnr = dcd_bgnr + "|" + sb.toString();
			}else{
				newBgnr = sb.toString();
			}
			dnaTaskDetailedInfo.setDcd_bgnr(newBgnr);
		}
	}
	
	
	@Override
	public void saveTaskDetailedLog(List<DnaTaskDetailedInfo> dnaTaskDetailedInfos) throws BusinessException{
		
		DnaTaskDetailedInfoDAO.save(dnaTaskDetailedInfos);
	}

	public EntityDAO<DnaTaskInfo, String> getDnaTaskInfoDAO() {
		return dnaTaskInfoDAO;
	}

	public void setDnaTaskInfoDAO(EntityDAO<DnaTaskInfo, String> dnaTaskInfoDAO) {
		this.dnaTaskInfoDAO = dnaTaskInfoDAO;
	}

	public EntityDAO<DnaTaskDetailedInfo, String> getDnaTaskDetailedInfoDAO() {
		return DnaTaskDetailedInfoDAO;
	}

	public void setDnaTaskDetailedInfoDAO(
			EntityDAO<DnaTaskDetailedInfo, String> dnaTaskDetailedInfoDAO) {
		DnaTaskDetailedInfoDAO = dnaTaskDetailedInfoDAO;
	}
	
	
	

}
