<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script type="text/javascript" src="${mwareStaticsPath}/js/mware/holder/condition_holder.js"></script>

<style>
 .button {
	background: #FFF;
	border: 1px solid #CCC;
	padding: 6px 10px 6px 10px;
	color: #333;
	border-radius: 4px;
}
.button:hover {
	color: #333;
	background-color: #EBEBEB;
	border-color: #ADADAD;
}
#oTable th {
	 background:#BFBFBF;
	 color:#fff;
	 padding:6px 4px;
	 text-align:center;
}
#oTable td {
 	
}
#oTable tr.focus{
    background-color:#eee;
}
.conditionForm td {
 	padding:6px 4px;
} 
</style>

<div id="searchForm" data-options="region:'north',split:true" style="border-top: 0px;">
	<form id="search_form"></form>
	<table id="condition_table" border="1" cellpadding="0" cellpadding="2" width="100%">
	</table>
	<div>
		<input type="button" id="addRow" class="button" style="font-size:12px;" disabled="disabled" value="+"/>
		<input type="button" id="deleteRow" class="button" style="font-size:12px;" disabled="disabled" value="-"/>
		<input type="button" id="btn_collect" class="button" style="font-size:12px;" disabled="disabled" value="收藏"/>
		<input type="button" id="btn_showGrid" class="button" style="font-size:12px;" disabled="disabled" value="选择"/>
	</div>
</div>

<div id="condition_window" class="easyui-window" style="width: 500px; height: 220px; display: none;" title="查询规则" resizable="false" minimizable="false" modal="true" closed="true"
	maximizable="false" closable="true" collapsible="false" >
	<input type="hidden" id="conditionId"/>
	<table id="conditionForm" class="conditionForm" class="u-table" style="width:100%;" align="center">
		<tr>
			<td class="u-item"><span>名称<span class="u-require">*</span>：</span></td>
			<td><input type="text" id="conditionName" name="conditionName" class="input required"></td>
		</tr>
		<tr>
			<td class="u-item"><span>描述<span class="u-require">*</span>：</span></td>
			<td><textarea id="describe" name="describe" class="input required" style="width:293px;height:90px;"></textarea></td>
		</tr>
	</table>
	<table class="u-table" class="conditionForm" style="width:100%;" >
		<colgroup>
				<col width="25%" />
				<col width="16%" />
				<col width="16%" />
				<col width="16%" />
				<col width="25%" />
		</colgroup>
		<tr>
			<td colspan="5">&nbsp&nbsp&nbsp温馨提示<span class="u-require">*</span>：更新操作会覆盖本条收藏</td>
		</tr>
		<tr>
			<td></td>
			<td><input type="button" id="btn_add"  value="新增"></td>
			<td><input type="button" id="btn_update" value="更新"></td>
			<td><input type="button" id="btn_close" value="取消" onclick="$('#condition_window').window('close');"></td>
			<td></td>
		</tr>
	</table>
</div>

<div id="condition_window" class="easyui-window" style="display: none;" title="我的查询条件" resizable="false" minimizable="false" modal="true" closed="true"
	maximizable="false" closable="true" collapsible="false">
	<table id="conditionGrid" border="false" style="display: none;" fit="true"></table>
</div>
