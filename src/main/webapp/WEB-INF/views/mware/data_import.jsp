<%@ page language="java" pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/inc/common.jsp"%>
<jsp:include page="/WEB-INF/inc/header.jsp">
	<jsp:param name="title" value="目录业务数据上传" />
</jsp:include>

<%@ taglib prefix="zz" uri="/WEB-INF/tld/zhizheng.tld"%>

<style type="text/css">
.item-error {
	background: url('${appStaticsPath}/images/validate/error.gif') no-repeat;
}
</style>
<script type="text/javascript" src="${mwareStaticsPath}/js/mware/mware_meta.js"></script>
<script type="text/javascript" src="${mwareStaticsPath}/js/mware/data_import.js"></script>

<script type="text/javascript">
	var importTimer = null;//导入时间变量
	var configCode = "${configCode}"; // 表单个性化编码
	$(function() {
		var cmsUploadExcel = new CmsUploadExcel();
		cmsUploadExcel.render();
		
		$(":input[name='hasTemp']").change(function(){
			if(this.value == "1"){
				$("#notes").hide();
			}else{
				$("#notes").show();
			}
		});
		
	});
</script>

<!-- {{main -->
<div class="easyui-layout" fit="true">

	<div data-options="region:'north'" border="false">
		<div class="edit-toolbar">
			<input type="button" id="btn_save" value="保存" /> <input type="button" id="btn_close" value="关闭" />
		</div>
	</div>

	<div data-options="region:'center'" border="false" class="edit-panel">
		<form class="u-form">
			<fieldset style="border-left-width: 1px; border-color: #E6EEF8">
				<legend>目录信息</legend>
				<!-- {{form -->
				<table style="width: 100%;" class="form-table">
					<colgroup>
						<col width="12%" />
						<col width="20%" />
						<col width="12%" />
						<col width="20%" />
						<col width="12%" />
						<col width="20%" />
					</colgroup>
					<tr>
						<td class="u-item">目录编号：</td>
						<td>${dirInfo.dirCode}</td>
						<td class="u-item">资源名称：</td>
						<td>${dirInfo.dirName}</td>
						<td class="u-item">所属部门：</td>
						<td>${dirInfo.orgName}</td>
					</tr>
					<tr>
						<td class="u-item">资源提供方式：</td>
						<td><zz:dicName code="${dirInfo.provideType}" /></td>
						<td class="u-item">提供周期：</td>
						<td><zz:dicName code="${dirInfo.provideCycle}" /></td>
						<td class="u-item">提供时限：</td>
						<td>${dirInfo.provideTimelimit}</td>
					</tr>
					<tr>
						<td class="u-item">资源说明：</td>
						<td colspan="5">${dirInfo.dirRemark}</td>
					</tr>
				</table>
			</fieldset>
		</form>

		<form id="edit_from" name="edit_from" method="post" class="u-form">
			<input type="hidden" name="dirInfoId" value="${dirInfo.id}" />
			<fieldset style="border-left-width: 1px; border-color: #E6EEF8">
				<legend>上传数据步骤</legend>
				<table style="width: 100%;" class="form-table">
					<tr>
						<td>一、下载表格模板：
						Excel2003【<a href="${appPath}/zygl/mwareCms/downloadModel.do;jsessionid=<%=session.getId()%>?fileType=xls&dirInfoId=${dirInfo.id}">${dirInfo.dirName}.xls</a>】&nbsp;&nbsp;&nbsp;&nbsp;
						Excel2007【<a href="${appPath}/zygl/mwareCms/downloadModel.do;jsessionid=<%=session.getId()%>?fileType=xlsx&dirInfoId=${dirInfo.id}">${dirInfo.dirName}.xlsx</a>】&nbsp;&nbsp;&nbsp;&nbsp;
						Csv【<a href="${appPath}/zygl/mwareCms/downloadModel.do;jsessionid=<%=session.getId()%>?fileType=csv&dirInfoId=${dirInfo.id}">${dirInfo.dirName}.csv</a>】&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
					</tr>
					<tr>
						<td></td>
					</tr>
					<tr>
						<td>二、选择数据文件：<input type="radio" name="hasTemp" value="1" checked="checked"/>模版<input type="radio" name="hasTemp" value="0"/>无模版
						<span id="notes" style="display:none">&nbsp;&nbsp;&nbsp;&nbsp;注：文件A1单元格必须是 "目录编号：${dirInfo.dirCode}"；第2行是字段名称；第3行开始是数据行。</span>
						</td>
					</tr>
					<tr>
						<td><input type="file" name="excelFile" class="required" style="margin-left: 110px;"/> 
						</td>
					</tr>
					<tr>
						<td></td>
					</tr>
					<tr>
						<td>三、返回临时数据库，检查数据，并提交数据。</td>
					</tr>
				</table>
			</fieldset>
		</form>
	</div>
</div>

<!-- {{导入Excel数据等待窗口 -->
<div id="upload_excel_data_window" title="导入数据" style="width: 420px; height: 220px;" data-options="closed:true">
	<div style="line-height: 23px;">&nbsp;&nbsp;&nbsp;&nbsp;导入的数据可能比较多，请耐心等待！</div>
	<br />
	<table style="width: 100%;">
		<tr height="25px;">
			<td><div id="uploadInfo">正在打开文件，请等待....</div></td>
		</tr>
		<tr height="30px;">
			<td>
				<div id="progressbar" class="easyui-progressbar" style="width: 380px;"></div>
			</td>
		</tr>
	</table>
	<br />
</div>
<!-- 导出目录Excel数据信息}} -->

<!-- panel}} -->
<%@ include file="/WEB-INF/inc/footer.jsp"%>