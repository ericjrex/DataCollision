package com.borosoft.ypfx.dnaCollision.utils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.borosoft.framework.exception.BusinessException;
import com.borosoft.framework.utils.StringUtils;
import com.borosoft.ypfx.Dictionary;
import com.borosoft.ypfx.dnaCollision.entity.PersonCollisonInfo;
import com.borosoft.ypfx.dnaCollision.entity.rules.PzryxxFields;

/**
 * excel导入工具
 * @author yepeicong
 *
 */
@Component("importHolder")
public class ImportHolder {
	
	public static Map<Integer,String> fieldNameMap = new HashMap<>();
	
	static{
		
		/*fieldNameMap.put(0, "dcd_ajbh");
		fieldNameMap.put(1, "dcd_xm");
		fieldNameMap.put(2, "dcd_sfzh");
		fieldNameMap.put(3, "dcd_sjh");
		fieldNameMap.put(4, "dcd_qq");
		fieldNameMap.put(5, "dcd_wx");
		fieldNameMap.put(6, "dcd_imei");
		fieldNameMap.put(7, "dcd_imsi");
		fieldNameMap.put(8, "dcd_mac");
		fieldNameMap.put(9, "dcd_dz");*/
		
		fieldNameMap.put(0, "dcd_xm");
		fieldNameMap.put(1, "leixing");
		fieldNameMap.put(2, "dcd_sfzh");
		fieldNameMap.put(3, "dcd_sjh");
		fieldNameMap.put(4, "dcd_qq");
		fieldNameMap.put(5, "dcd_wx");
		fieldNameMap.put(6, "dcd_imei");
		fieldNameMap.put(7, "dcd_imsi");
		fieldNameMap.put(8, "dcd_mac");
		fieldNameMap.put(9, "dcd_jd");
		fieldNameMap.put(10, "dcd_tb");
		fieldNameMap.put(11, "dcd_jd0");
		fieldNameMap.put(12, "dcd_wd");
		fieldNameMap.put(13, "dcd_dz");
		fieldNameMap.put(14, "xzd_cph");
		fieldNameMap.put(15, "dcd_zhbhsj");
		
	}
	
	public void choessXSSfWorkBook(InputStream inputStream,Set<String> sfzSet,Map<String,PersonCollisonInfo> personCollisonMapBySfzh) throws  Exception{
		XSSFWorkbook wb = new XSSFWorkbook(inputStream);
		// 第一行为标题，不取
		//int ignoreRows = 2;
		String fieldName = "";
		PersonCollisonInfo personCollisonInfo = null;
		Map<String,Object> dataMap = null;
 		XSSFSheet st = wb.getSheetAt(0);
		int lastRowNum = st.getLastRowNum();
		for (int rowIndex = 1; rowIndex <= lastRowNum; rowIndex++) {
			XSSFRow row = st.getRow(rowIndex);
			if (row != null) {
				dataMap = new HashMap<>();
				for (int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++) {
					XSSFCell cell = row.getCell(cellIndex);
					fieldName = fieldNameMap.get(cellIndex);
					/*if(fieldName.equals("dcd_ajbh")){
						if(StringUtils.isBlank(cell.toString())){
							throw new BusinessException("第【"+rowIndex+"】行数据【案件编号】不能为空");
						}
					}*/
					
					if(fieldName.equals("dcd_sfzh")){
						if(StringUtils.isBlank(cell.toString())){
							throw new BusinessException("第【"+rowIndex+"】行数据【身份证号】不能为空");
						}
					}
					
					if(cell == null || StringUtils.isBlank(cell.toString())){
						continue;
					}
					int cellType = cell.getCellType();
					String cellValue = cellValuetoString(cellType, cell.toString());
					
					if(StringUtils.isNotBlank(fieldName) && StringUtils.isNotBlank(cellValue)){
						dataMap.put(fieldName, cellValue);
					}
					
					if(PzryxxFields.field_SFZH.toLowerCase().equals(fieldName)){
						if(StringUtils.isNotBlank(cellValue)){
							sfzSet.add(cellValue);
						}
					}
				}
				if(dataMap.size() > 0){
					personCollisonInfo = new PersonCollisonInfo();
					BeanUtils.populate(personCollisonInfo, dataMap);
					personCollisonInfo.setDcd_sjzt(Dictionary.比对结果_网监返回);
					personCollisonMapBySfzh.put(personCollisonInfo.getDcd_sfzh(), personCollisonInfo);
				}
				
			}
		}
	}
	
	public void choessHSSfWorkBook(InputStream inputStream,Set<String> sfzSet, Map<String, PersonCollisonInfo> personCollisonMapBySfzh) throws  Exception{
		HSSFWorkbook wb = new HSSFWorkbook(inputStream);
		// 第一行为标题，不取
		//int ignoreRows = 2;
		String fieldName = "";
		PersonCollisonInfo personCollisonInfo = null;
		Map<String,Object> dataMap = null;
 		HSSFSheet st = wb.getSheetAt(0);
		int lastRowNum = st.getLastRowNum();
		for (int rowIndex = 1; rowIndex <= lastRowNum; rowIndex++) {
			HSSFRow row = st.getRow(rowIndex);
			if (row != null) {
				dataMap = new HashMap<>();
				for (int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++) {
					HSSFCell cell = row.getCell(cellIndex);
					fieldName = fieldNameMap.get(cellIndex);
					/*if(fieldName.equals("dcd_ajbh")){
						if(cell == null || StringUtils.isBlank(cell.toString())){
							throw new BusinessException("第【"+rowIndex+"】行数据【案件编号】不能为空");
						}
					}*/
					
					if(fieldName.equals("dcd_sfzh")){
						if(cell == null ||StringUtils.isBlank(cell.toString())){
							throw new BusinessException("第【"+rowIndex+"】行数据【身份证号】不能为空");
						}
					}
					
					if(cell == null || StringUtils.isBlank(cell.toString())){
						continue;
					}
					int cellType = cell.getCellType();
					String cellValue = cellValuetoString(cellType, cell.toString());
					
					if(StringUtils.isNotBlank(fieldName) && StringUtils.isNotBlank(cellValue)){
						dataMap.put(fieldName, cellValue);
					}
					
					if(PzryxxFields.field_SFZH.toLowerCase().equals(fieldName)){
						if(StringUtils.isNotBlank(cellValue)){
							sfzSet.add(cellValue);
						}
					}
				}
				if(dataMap.size() > 0){
					personCollisonInfo = new PersonCollisonInfo();
					BeanUtils.populate(personCollisonInfo, dataMap);
					personCollisonInfo.setDcd_sjzt(Dictionary.比对结果_网监返回);
					personCollisonMapBySfzh.put(personCollisonInfo.getDcd_sfzh(), personCollisonInfo);
				}
			}
		}
	}
	
	
	
	/**
	 * 去掉多余的0
	 * 
	 * @param cell
	 *            CELL_TYPE_NUMERIC 数值型 0 ,CELL_TYPE_STRING 字符串型 1, CELL_TYPE_FORMULA 公式型 2 ,CELL_TYPE_BLANK 空值 3,
	 *            CELL_TYPE_BOOLEAN 布尔型 4, CELL_TYPE_ERROR 错误 5
	 * @return
	 */
	private static String cellValuetoString(int cellType, String cellValue) {
		switch (cellType) {
		case 0:
			if (cellValue.indexOf(".") > 0) {
				cellValue = cellValue.replaceAll("0+?$", "");// 去掉多余的0
				cellValue = cellValue.replaceAll("[.]$", "");// 如最后一位是.则去掉
				cellValue=  cellValue.replaceAll("，", ",");
			}
			break;
		case 1:
			if (cellValue.indexOf("，") > 0) {
				cellValue=  cellValue.replaceAll("，", ",");
			}
			break;
		default:
			break;
		}
		return cellValue;
	}
	

}
