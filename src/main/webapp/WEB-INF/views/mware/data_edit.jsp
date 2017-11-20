<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:include page="/WEB-INF/inc/common.jsp" />
<jsp:include page="/WEB-INF/inc/header.jsp">
	<jsp:param name="title" value="目录数据主页" />
</jsp:include>

<script type="text/javascript">
    var sessionId = '<%=session.getId()%>';
</script>

<link href="${mwareStaticsPath}/css/fillin_css.css" rel="stylesheet" type="text/css" />
<link href="${mwareStaticsPath}/jquery/huploadify/Huploadify.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${mwareStaticsPath}/jquery/huploadify/jquery.Huploadify.js"></script>
<script type="text/javascript" src="${mwareStaticsPath}/js/mware/holder/utils/zygx_dict.js"></script>
<script type="text/javascript" src="${mwareStaticsPath}/js/mware/holder/utils/zygx_utils.js"></script>
<script type="text/javascript" src="${mwareStaticsPath}/js/mware/holder/utils/basic_label.js"></script>
<script type="text/javascript" src="${mwareStaticsPath}/js/mware/holder/dirItem_holder.js"></script>
<script type="text/javascript" src="${mwareStaticsPath}/js/mware/holder/dirInfo_holder.js"></script>
<script type="text/javascript" src="${mwareStaticsPath}/js/mware/holder/middleware.js"></script>

<script type="text/javascript" src="${mwareStaticsPath}/js/mware/mware_com.js"></script>
<script type="text/javascript" src="${mwareStaticsPath}/js/mware/mware_meta.js"></script>
<script type="text/javascript" src="${mwareStaticsPath}/js/mware/holder/abstract_data_edit.js"></script>
<script type="text/javascript" src="${mwareStaticsPath}/js/mware/data_edit.js"></script>

<script type="text/javascript">

	var configCode = '${configCode}'; // 个性化表单配置编码
	var ywId = '${id}';
	window.context = {
		addAction : "${addAction}",
		updateAction : "${updateAction}",
		iframeId : "${iframeId}",
		noFrame : "${noFrame}" == "true",
		isSsingle : "${isSsingle}" == "true"
	};
	var dataEdit = null;
	
	$(function() {
		dataEdit = new DataEdit(configCode, ywId);
		dataEdit.render();
	});
	
	// 供子页面调用
	function getYwId() {
		return $("#ID").val();
	}
	
</script>

<div class="easyui-layout" fit="true">
	<div data-options="region:'north'" border="false" style="height: 35px;">
		<div class="edit-toolbar">
			<input type="button" id="btn_save_temp" value="临时保存"></input> 
			<input type="button" id="btn_save_valid" value="检查并保存"></input>
			<input type="button" id="btn_save_add" value="保存并新增"></input>
			<input type="button" id="btn_close" value="关闭"></input>
		</div>	
	</div>
	<div id="editForm" data-options="region:'center'" border="false" class="edit-panel"></div>
</div>

<!-- {{验证信息 -->
<div id="check_info_window" title="验证信息" style="width: 600px; height: 400px; display: none;" data-options="iconCls:'icon-save',closed:true">
	<div id="validateResult" fit='true'></div>
</div>
<!-- 验证信息}} -->

<!-- 加载机构树 -->
<%-- <jsp:include page="/WEB-INF/views/mware/holder/data_edit_orgTree.jsp" /> --%>

<jsp:include page="/WEB-INF/inc/footer.jsp" />