<%@ page language="java" pageEncoding="UTF-8"%>

<jsp:include page="/WEB-INF/inc/common.jsp" />
<jsp:include page="/WEB-INF/inc/header.jsp">
	<jsp:param name="title" value="资源查看" />
</jsp:include>

<style type="text/css">
.item-error {
	background: url('${mwareStaticsPath}/images/validate/error.gif') no-repeat;
}
</style>

<link href="${mwareStaticsPath}/css/fillin_css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${mwareStaticsPath}/js/mware/mware_com.js"></script>

<script type="text/javascript" src="${mwareStaticsPath}/js/mware/holder/utils/zygx_dict.js"></script>
<script type="text/javascript" src="${mwareStaticsPath}/js/mware/holder/utils/zygx_utils.js"></script>
<script type="text/javascript" src="${mwareStaticsPath}/js/mware/holder/utils/basic_label.js"></script>
<script type="text/javascript" src="${mwareStaticsPath}/js/mware/holder/dirItem_holder.js"></script>
<script type="text/javascript" src="${mwareStaticsPath}/js/mware/holder/dirInfo_holder.js"></script>
<script type="text/javascript" src="${mwareStaticsPath}/js/mware/holder/middleware.js"></script>

<script type="text/javascript" src="${mwareStaticsPath}/js/mware/mware_com.js"></script>

<script type="text/javascript">

	var configCode = '${configCode}'; // 个性化表单配置编码
	var ywId = '${id}';
	window.context = {
		iframeId : "${iframeId}",
		noFrame : "${noFrame}" == "true"
	};
	
	$(function() {
		Sys.msg.initCurPage(configCode + "$mware$view", "查看页面");
		
		$('#btn_close').click(function() {
			if(K.isBlank(window.context.iframeId)) {
				Sys.tabs.closeCurTab();
			} else if(window.context.noFrame) {
				if(parent.closeIframe != null && K.isFunction(parent.closeIframe)) {
					parent.closeIframe(window.context.iframeId);
				} else {
					K.msg.mini.error("父页面没有关闭函数[closeIframe]");
				}
			} else {
				Sys.msg.pub(configCode + "$mware$closeIframe@success", window.context.iframeId);
			}
		});
		
		var middleware = new Middleware();
		middleware.load({
			configCode : configCode,
			viewForm : "#viewForm",
			isSsingle : "${isSsingle}" == "true",
			ywId : ywId
		});
		
		// 初始化控件
		initComponent();
		
		var customFunction = "onLoadBefore";
		var code = resoveConfigCode(configCode);
		if(K.isNotBlank(code)) {
			customFunction += "_" + code;
		}
		if(typeof (window[customFunction]) != 'undefined') {
			window[onLoadBefore]();
		}
	});
	
	<%out.print(request.getAttribute("jsCode"));%>
	
</script>

<div class="easyui-layout" fit="true">
	<div data-options="region:'north'" border="false">
		<input type="button" id="btn_close" value="关闭">
	</div>
	<div id="viewForm" data-options="region:'center'" border="false"></div>
</div>

<%@ include file="/WEB-INF/inc/footer.jsp"%>