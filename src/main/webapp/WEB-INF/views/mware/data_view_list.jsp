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
<script type="text/javascript" src="${mwareStaticsPath}/js/mware/holder/abstract_data_index.js"></script>
<script type="text/javascript" src="${mwareStaticsPath}/js/mware/data_view_list.js"></script>

<script type="text/javascript">
	window.__menuId = "${menuId}";
	var configCode = '${configCode}'; // 个性化表单配置编码
	var ywId = '${id}';
	window.context = {
		listAction : "${listAction}",
		addAction : "${addAction}",
		updateAction : "${updateAction}",
		iframeId : "${iframeId}",
		noFrame : "${noFrame}" == "true",
		parentId : "${parentId}",
		foreignKey : "${foreignKey}"
	};
	var dataEditList = null;
	
	$(function() {
		dataEditList = new DataEditList(configCode, ywId);
		dataEditList.render();
		
		resizePanel();
	});
	
	function resizePanel() {
		var sHeight = $("#search_form").height() + 10;
		if( sHeight > 40 ){
			sHeight = 40;
		}
		$($('.search_panel')[0]).panel("resize", {
			height : sHeight
		});
		$('#mainLayout').layout("resize");
	}
	
	// 列表查询（用于子页面模式）
	function searchDataGrid() {
		dataIndex.loadDataGrid();
	}
	// 新增
	function addDirInfoData(menu, rightMenu) {
		dataIndex.addDirInfoData(menu, rightMenu);
	}
	// 修改
	function updateDirInfoData(menu, rightMenu) {
		dataIndex.updateDirInfoData(menu, rightMenu);
	}	
	// 删除
	function deleteDirInfoData(menu, rightMenu) {
		dataIndex.deleteDirInfoData(menu, rightMenu);
	}
	// 查看
	function viewDirInfoData(menu, rightMenu) {
		dataIndex.viewDirInfoData(menu, rightMenu);
	}
	// 直接跳转页面
	function gotoPage(menu, rightMenu) {
		dataIndex.gotoPage(menu, rightMenu);
	}
	// 根据ID跳转页面
	function gotoPageById(menu, rightMenu) {
		dataIndex.gotoPageById(menu, rightMenu);
	}
	// post请求
	function postRequest(menu, rightMenu) {
		dataIndex.postRequest(menu, rightMenu);
	}
</script>

<div class="easyui-layout" fit="true">
	<div data-options="region:'center'" border="false">
		<div id="mainLayout" class="easyui-layout" fit="true">
			<div id="searchForm" data-options="region:'north',split:true" class="search_panel" style="border-left: none; border-right: none;"></div>
			<div data-options="region:'center'" style="border-left: none; border-right: none; border-bottom: none;">
				<table id="dirInfoGrid" border="false" style="display: none;" fit="true"></table>
			</div>
		</div>
	</div>
	<div data-options="region:'east'" border="false" style="width: 500px;">
		<div class="easyui-layout" fit="true">
			<div data-options="region:'north'" border="false" style="height: 35px;">
				<div class="edit-toolbar">
					<input type="button" id="btn_add" value="新增"></input>
					<input type="button" id="btn_save_temp" value="临时保存"></input> 
					<input type="button" id="btn_save_valid" value="检查并保存"></input>
					<input type="button" id="btn_save_add" value="保存并新增"></input>
				</div>
			</div>
			<div id="editForm" data-options="region:'center'" border="false" class="edit-panel"></div>
		</div>
	</div>
</div>

<!-- {{验证信息 -->
<div id="check_info_window" title="验证信息" style="width: 600px; height: 400px; display: none;" data-options="iconCls:'icon-save',closed:true">
	<div id="validateResult" fit='true'></div>
</div>
<!-- 验证信息}} -->

<!-- 加载机构树 -->
<%-- <jsp:include page="/WEB-INF/views/mware/holder/data_edit_orgTree.jsp" /> --%>

<jsp:include page="/WEB-INF/inc/footer.jsp" />