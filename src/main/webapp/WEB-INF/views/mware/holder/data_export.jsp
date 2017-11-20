<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script type="text/javascript">

	$(function() {
		// 导出说明
		$('#btn_export_instruction').bind('click',function(){
			$('#export_instruction_window').show();
			$('#export_instruction_window').window("open");
		});
	});
	
</script>

<!-- {{导出目录Excel数据信息 -->
<div id="dirinfo_excel_data_window" title="导出数据" style="width: 400px; height: 250px; display: none;" data-options="closed:true">
	<div style="line-height: 22px;">
		&nbsp;&nbsp;&nbsp;&nbsp;此信息资源仅供政府部门内部办公使用，不能用于其他非政府办公用途，下载人具有对信息资源的保密责任！
		<span style="color: red;">如果下载的数据量大于20万，建议添加查询条件分批下载或者通过前置机共享数据</span>
	</div>
	<br />
	<table style="width: 100%;">
		<tr height="30xp;">
			<td colspan="2"><div id="downInfo"></div></td>
		</tr>
		<tr height="30xp;">
			<td colspan="2">
				<div id="progressbar" class="easyui-progressbar" style="width: 380;"></div>
			</td>
		</tr>
		<tr>
			<td style="text-align: center;">选择下载类型： <select id="downType" style="width: 100px;">
					<option value="excel" selected="selected">excel</option>
					<option value="pdf">pdf</option>
					<option value="json">json</option>
					<option value="csv">csv</option>
					<option value="xml">xml</option>
			</select>
			</td>
			<td>
				<a href="javascript:;" id="btn_export_instruction">下载说明</a>		
			</td>
		</tr>
	</table>
	<br />
	<div style="text-align: center;">
		<input type="button" id="btn_startdown" value="下载 " /> <input type="button" id="btn_cancel_export" value="关闭" />
	</div>
</div>
<!-- 导出目录Excel数据信息}} -->

<!-- {{导出说明 -->
<div id="export_instruction_window" class="easyui-window" title="下载说明" style="width: 600px; height: 300px; display: none;" 
	data-options="closed:true,minimizable:false,maximizable:false,resizable:false,collapsible:false">
	
	<div style="margin: 10px;padding:5px;border: 1px solid #ccc;background: #f0f7fb;">
		EXCEL：电子表格文件，可用微软的EXCEL软件打开，带有数据格式，一般用于下载人查看及分析数据使用
	</div>
	<div style="margin: 10px;padding:5px;border: 1px solid #ccc;">
		PDF：带格式的文档文件，用于下载人查看数据使用。
	</div>
	<div style="margin: 10px;padding:5px;border: 1px solid #ccc;background: #f0f7fb;">
		JSON：是一种轻量级的数据交换格式，可用记事本打开，一般用于应用程序读取，不方便查看和统计数据。
	</div>
	<div style="margin: 10px;padding:5px;border: 1px solid #ccc;">
		CSV：纯数据文本文件，数据用逗号分隔，主要用于应用程序读取数据使用，不方便查看和统计数据。
	</div>
	<div style="margin: 10px;padding:5px;border: 1px solid #ccc;background: #f0f7fb;">
		XML：是一种数据交换格式文件，主要用于应用程序读取，不方便查看和统计数据。
	</div>
</div>
<!-- 导出目录Excel数据信息}} -->