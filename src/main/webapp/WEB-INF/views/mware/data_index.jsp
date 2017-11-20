<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:include page="/WEB-INF/inc/common.jsp" />
<jsp:include page="/WEB-INF/inc/header.jsp">
	<jsp:param name="title" value="目录数据主页" />
</jsp:include>

<script type="text/javascript">
    var sessionId = '<%=session.getId()%>';
</script>

<link href="${mwareStaticsPath}/css/fillin_css.css" rel="stylesheet" type="text/css" />
<style type="text/css">
.item-error {
	background: url('${mwareStaticsPath}/images/validate/error.gif') no-repeat;
}
</style>
<link href="${mwareStaticsPath}/css/fillin_css.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="${mwareStaticsPath}/js/mware/holder/utils/zygx_dict.js"></script>
<script type="text/javascript" src="${mwareStaticsPath}/js/mware/holder/utils/zygx_utils.js"></script>
<script type="text/javascript" src="${mwareStaticsPath}/js/mware/holder/utils/basic_label.js"></script>
<script type="text/javascript" src="${mwareStaticsPath}/js/mware/holder/dirItem_holder.js"></script>
<script type="text/javascript" src="${mwareStaticsPath}/js/mware/holder/dirInfo_holder.js"></script>
<script type="text/javascript" src="${mwareStaticsPath}/js/mware/holder/middleware.js"></script>

<script type="text/javascript" src="${mwareStaticsPath}/js/mware/mware_com.js"></script>
<script type="text/javascript" src="${mwareStaticsPath}/js/mware/mware_meta.js"></script>
<script type="text/javascript" src="${mwareStaticsPath}/js/mware/holder/abstract_data_index.js"></script>
<script type="text/javascript" src="${mwareStaticsPath}/js/mware/data_index.js"></script>

<script type="text/javascript">
	
	window.__menuId = "${param['menuId']}";
	window.__subMenuId = "${subMenuId}";
	var configCode = "${configCode}"; // 表单个性化编码
	window.context = {
		listAction : "${listAction}",
		parentId : "${parentId}"
	};
	
	var dataIndex = null;
	$(function() {
		dataIndex = new DataIndex(configCode);
		dataIndex.render();

		resizePanel();
	});
	
	function resizePanel() {
		var sHeight = $("#search_form").height() + 10;
		if( sHeight > 100 ){
			sHeight = 100;
		}
		$($('.search_panel')[0]).panel("resize", {
			height : sHeight
		});
		$('#mainLayout').layout("resize");
	}

	// {{导航菜单函数
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
	// 数据导入
	function upLoadData(menu, rightMenu) {
		dataIndex.upLoadData(menu, rightMenu);
	}
	// 数据导出
	function exportData(menu, rightMenu) {
		dataIndex.exportData(menu, rightMenu);
	}
	// ============= 其他点击函数 ===============
	// 直接跳转页面
	function gotoPage(menu, rightMenu) {
		dataIndex.gotoPage(menu, rightMenu);
	}
	// 根据ID跳转页面
	function gotoPageById(menu, rightMenu) {
		dataIndex.gotoPageById(menu, rightMenu);
	}
	// 根据IDs跳转页面
	function gotoPageByIds(menu, rightMenu) {
		dataIndex.gotoPageByIds(menu, rightMenu);
	}
	// post请求
	function postRequest(menu, rightMenu) {
		dataIndex.postRequest(menu, rightMenu);
	}
	// 导航菜单函数}}

	// 没有权限时列表显示的信息
	function format_no_auth(value) {
		return "<span style='color:red;padding-left:20px;' title='该项数据不公开' class='item-error'>该项数据不公开</span>";
	};
	function format_encrypt(value) {
		if ("*该项数据已加密" == value) {
			return "<span style='color:red;padding-left:20px;' title='该项数据已加密' class='item-error'>该项数据已加密</span>";
		} else {
			return value;
		}
	}
	
</script>

<div id="mainLayout" class="easyui-layout" fit="true">

	<!-- {{搜索条件}} -->
	<div id="searchForm" data-options="region:'north',split:true" class="search_panel" style="border-left: none; border-right: none;"></div>
	<!-- {{规则条件}} -->
	<%-- <jsp:include page="/WEB-INF/views/mware/holder/data_condition.jsp" /> --%>
	
	<!-- {{列表 -->
	<div data-options="region:'center'" style="border-left: none; border-right: none; border-bottom: none;">
		<table id="dirInfoGrid" border="false" style="display: none;" fit="true"></table>
	</div>
	<!-- 列表}} -->
</div>

<!-- 导出页面 -->
<jsp:include page="/WEB-INF/views/mware/holder/data_export.jsp" />
<!-- 按查询条件删除（一般不用就不要加载了） -->
<%-- <jsp:include page="/WEB-INF/views/mware/holder/data_batchDelete.jsp"> --%>
<%-- <jsp:param name="dataGridId" value="#dirInfoGrid" /> --%>
<%-- </jsp:include> --%>

<%@ include file="/WEB-INF/inc/footer.jsp"%>