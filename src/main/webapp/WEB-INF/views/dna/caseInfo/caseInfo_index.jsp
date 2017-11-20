<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:include page="/WEB-INF/inc/common.jsp" />
<jsp:include page="/WEB-INF/inc/header.jsp">
	<jsp:param name="title" value="案件信息列表" />
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
<script type="text/javascript" src="${appStaticsPath}/js/dna/dna_collision_meta.js"></script>


<script type="text/javascript">

	var idCard = "${idCard}";

	var dataIndex = null;
	
	$(function() {
		
		$("#dataGrid").datagrid({
			idField : "ID",
			url : DnaAction.getLinkCaseInfo,
			toolbar : [{
				iconCls: 'u-icon-search0',
				text : '查询',
				handler: function(){
					loadAjDataGrid();
				}
			},{
				iconCls: 'icon-search',
				text : '查看案件',
				handler: function(){
					var rows = $("#dataGrid").datagrid('getChecked');
					if(rows.length > 1){
						K.msg.mini.error("请选择一条数据");
						return;
					}
					var row = rows[0];
					viewCaseInfo(row.ID);
				}
			}],
			fitColumns : false,
			rownumbers : true,
			nowrap : false,
			selectOnCheck : false,
			singleSelect : true,
			remoteSort : true,
			columns : [ [ {
				field : 'id',
				checkbox : true
			},{
				field : "AJBH",
				title : "案件编号",
				width : 180,
				align : 'center'
			},{
				field : "AJMC",
				title : "案件名称",
				width : 200,
				align : 'center'
			},{
				field:'ZYAQ',
				title:'简要案情',
				width: 350,
				align:'center'
			}] ],
			onBeforeLoad : function(param) {
				$.extend(param, {
					idCard : idCard
				});
				$("#dataGrid").datagrid("uncheckAll");
			},
			onLoadSuccess : function(data) {
				if(data.success == undefined || data.success == null || data.success) {
				} else {
					K.msg.mini.error(data.message);
				}
			}
		});
	});
	
	function viewCaseInfo(){
		var rows = $("#dataGrid").datagrid("getChecked");
		if(rows.length != 1){
			K.msg.mini.error("请选择一条数据");
			return;
		}
		var row = rows[0];
		K.log(parent);
		parent.dataIndex.viewCaseInfo(row.AJBH);
	}
	
	// 重新加载列表数据
	function loadAjDataGrid () {
		$("#dataGrid").datagrid('uncheckAll');
		var json = {};
		json = K.form.serializeJson($("#search_form"));
		$("#dataGrid").datagrid("load", json);
	}
	
</script>

<div class="easyui-layout" fit="true">
	<div data-options="region:'north'" style="height: 28px;">
		<form id="search_form">
			<table class="u-table">
				<tr>
					<td class='u-item'>案件编号：</td>
					<td><input type="text" name="AJBH"  style="width: 90%"></td>
					<td class='u-item'>案件名称：</td>
					<td><input type="text" name="AJMC" value="" style="width: 90%"></td>
				</tr>
			</table>
		</form>
	</div>
	<div  data-options="region:'center'" border="false">
		<div id="dataGrid"></div>
	</div>
</div>


<%@ include file="/WEB-INF/inc/footer.jsp"%>