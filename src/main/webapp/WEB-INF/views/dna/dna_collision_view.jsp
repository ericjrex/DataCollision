<%@ page language="java" pageEncoding="UTF-8"%>

<jsp:include page="/WEB-INF/inc/common.jsp" />
<jsp:include page="/WEB-INF/inc/header.jsp">
	<jsp:param name="title" value="碰撞人员信息查看" />
</jsp:include>

<style type="text/css">
.item-error {
	background: url('${mwareStaticsPath}/images/validate/error.gif') no-repeat;
}
.caseInfoA{
 	background: #EDEDED;
	border: 1px solid #CCC;
	padding: 6px 10px 6px 10px;
	border-radius: 4px;
}
.caseInfoA:hover {
    background: #B0B0B0;
    border: 1px solid #CCC;
    padding: 6px 10px 6px 10px;
    color: #333;
    border-radius: 4px;
    cursor : pointer ;
    text-decoration:none;
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
<script type="text/javascript" src="${appStaticsPath}/js/dna/dna_collision_meta.js"></script>
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
		
		var ajbh = $("#DCD-3FFB25-2500042011").text();
		appendButton(ajbh);
		
	});
	
	
	 function  appendButton(id){
		var me = this;
		var aHtml = $(".caseInfoA");
		if(aHtml.length == 0){
			//var ptd = $("span[field=DCD_AJBH]").parent();

var ptd = $("#DCD-3FFB25-2500042011").parent();
			var html = "&nbsp;&nbsp;<a class='caseInfoA' onclick=\"viewCaseInfo('"+id+"')\">查看案件</a>";
			ptd.append(html);
		}
	}
	 
	 
	function viewCaseInfo(ajbh) {
		var me = this;
	
		var falg = checkCaseInfoExist(ajbh);
		if(!falg){
			return ;
		}
		var url = DnaAction.gotoViewDirDataPage;
		var width = $("body").width();
		var height = $("body").height();
		K.window.iframewindow({
			title : "案件信息",
			href : url,
			id : "caseInfoViewPage",
			width : width * 9 / 10,
			height : height * 9 / 10,
			params : {
				ajbh : ajbh,
				isSingle : false
			},
			modal : true,
			maximizable : false,
			maximized : false,
			closable : true,
			minimizable : false,
			resizable : false,
			collapsible : false
		});
	}
	
	function checkCaseInfoExist(ajbh){
		var falg = true;
		K.log( DnaAction.checkCaseInfoExist);
		$.ajax({
            url : DnaAction.checkCaseInfoExist,
            data : {
            	ajbh : ajbh
            },
            type : 'post',
            dataType : 'json',
            async : false,
            success : function(jsonBean){
                if(jsonBean.success){
                }else{
                    K.msg.mini.error(jsonBean.message);
                    falg = false;
                    return false;
                }
            }
        });
		return falg;
	}
	
<%out.print(request.getAttribute("jsCode"));%>
	
</script>

<div class="easyui-layout" fit="true">
	<div data-options="region:'north'" border="false">
		<input type="button" id="btn_close" value="关闭">
	</div>
	<div id="viewForm" data-options="region:'center'" border="false"></div>
</div>

<%@ include file="/WEB-INF/inc/footer.jsp"%>