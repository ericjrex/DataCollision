<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	pageContext.setAttribute("dataGridId", request.getParameter("dataGridId"));
%>

<script type="text/javascript">

	var dataGridId = "${dataGridId}";
	var batchDelete = null;
	$(function() {
		batchDelete = new BatchDelete();
		batchDelete.load();
	});

	// 按列表查询条件删除数据，该方法建议仅用于超级管理员使用
	var BatchDelete = function() {
		var validCode = {
			before : null,
			back : null
		};
		var delMenu = null;
			
		// 初始化
		this.load = function () {
			var me = this;
			$('#btn_cancel_delete').bind('click',function(){
				me.close();
			});
			$('#btn_batch_delete').bind('click',function(){
				me.deleteDataByQuery();
			});
			
			$('#batch_delete_data_window').window({
				closable : true,
				minimizable : false,
				maximizable : false,
				collapsible : false,
				resizable : false
			});
		};
		
		// 执行批量删除（这里暂且使用前端验证码，仅起提示之用）
		this.showBatchDelete = function (menu) {
			delMenu = menu;
			var data = $(dataGridId).datagrid('getData');
			this.refreshValidCode();
			$("#count").html(data.total);
			$('#batch_delete_data_window').show();
			$('#batch_delete_data_window').window('open');
		},
		
		// 执行批量删除
		this.deleteDataByQuery = function () {
			var me = this;
			var resultStr = $("#result").val();
			if(K.isBlank(resultStr)) {
				me.refreshValidCode();
				$("#errorMsg").html("验证码错误！");
				return;
			}
			var result = parseInt(resultStr);
			if(validCode.before + validCode.back != result) {
				$("#errorMsg").html("验证码错误！");
				me.refreshValidCode();
				return;
			}
			
			var params = K.form.serializeJson($("#search_form"));
			params.configCode = configCode;
			
			var delUrl = delMenu.url;
			if(K.isBlank(delUrl) || delUrl.endWith("/")){
				delUrl = CmsAction.deleteDataByQuery;
			}
			K.ajax({
				mask : true,
				dataType : 'json',
				url : delUrl,
				data : params,
				success : function(jsonBean) {
					K.unmask();
					if (K.showMessage(jsonBean)) {
						me.close();
						Sys.msg.pub(window.page_publish_message + "$mware$background$edit@success", jsonBean);
					}
				}
			});
		};
		
		this.close = function() {
			$('#batch_delete_data_window').window('close');
		};
		
		this.refreshValidCode = function() {
			var before = Math.floor(Math.random() * 10);
			var back = Math.floor(Math.random() * 10);
			validCode.before = before;
			validCode.back = back;
			$('#before').html(before);
			$('#back').html(back);
			$("#result").val("");
			$("#result").focus();
		}
	};
	
	// 批量删除
	function deleteDataByQuery(menu, rightMenu) {
		batchDelete.showBatchDelete(menu);
	}
	
	function refreshValidCode() {
		$("#errorMsg").html("");
		batchDelete.refreshValidCode();
	}
	
</script>

<div id="batch_delete_data_window" title="批量删除" style="width: 350px; height: 190px; display: none;" data-options="closed:true">
	<div style="line-height: 22px;">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<span style="color: red;">执行批量删除将删除列表搜索的所有数据，且数据删除后无法恢复。您确定要删除列表中的</span>
		<span id="count" style="color: red;font-size: 25px;font-weight: bolder;font-family: 楷体;"></span>
		<span style="color: red;">条数据吗？</span>
	</div>
	
	<div style="line-height: 22px;">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<span id="errorMsg" style="color: red;"></span>
	</div>
	
	<div style="line-height: 22px;">
		&nbsp;&nbsp;&nbsp;&nbsp;验证码：
		<span id="before" style="color: blue;"></span> + <span id="back" style="color: blue;"></span> = 
		<input type="text" id="result" style="width: 50px;"/>&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="#" onclick="javascript:refreshValidCode();" style="font-family: 楷体;font-size: 20px;" >刷新</a>
	</div>
	
	<br />
	<div style="text-align: center;">
		<input type="button" id="btn_batch_delete" value="确定 " />
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" id="btn_cancel_delete" value="关闭"/>
	</div>
</div>