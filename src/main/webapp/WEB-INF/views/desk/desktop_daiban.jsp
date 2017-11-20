<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:include page="/WEB-INF/inc/common.jsp" />
<jsp:include page="/WEB-INF/inc/header.jsp">
	<jsp:param name="title" value="桌面栏目" />
</jsp:include>

<script type="text/javascript">

	var groupCode = "${groupCode}";
	
	// 主页对象
	var appIndex = (function() {
		if (window.appIndex) {
			return window.appIndex;
		}
		function dog(parent) {
			if (!parent) {
				return null;
			}
			if (parent.appIndex) {
				return parent.appIndex;
			}
			if (parent == window.top) {
				return null;
			}
			return dog(parent.parent);
		}
		return window.appIndex = dog(window);
	})();
	
	$(function() {
		$("#desktop_grid").datagrid({
			idField : "id",
			url : $appPath + "/zygl/mwareCms/getDeskInfoList.do",
			selectOnCheck : false,
			singleSelect : true,
			pagination : false,
			columns : [ [ {
				field : "itemName",
				title : "待办事项名称",
				align : 'left',
				width : 100,
				formatter : function(value, rowData, rowIndex){
					return "<a href='#' onclick='javasctipt:taskList_openInTab(\"" + rowData["menuId"] + "\");'>" + value + "</a>";
				}
			}, {
				field : "count",
				title : "待办数量",
				align : 'left',
				width : 100
			}] ],
			onBeforeLoad : function(param) {
				$.extend(param, {
					appCode : _appCode_,
					groupCode : groupCode
				});
			}
		});
	});
	
	function taskList_openInTab(resId) {
		appIndex.menuModule.addTabByMenuId(resId, {
	        refresh : false
	    });
	}
	 
</script>

<table id="desktop_grid"></table>

<jsp:include page="/WEB-INF/inc/footer.jsp" />