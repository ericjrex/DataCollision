<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:include page="/WEB-INF/inc/common.jsp" />
<jsp:include page="/WEB-INF/inc/header.jsp">
	<jsp:param name="title" value="案件信息查看页面" />
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

	var ajbh = "${ajbh}";

	var dataIndex = null;
	
	$(function() {
		$.ajax({
            url : DnaAction.viewDirData,
            data : {
            	ajbh : ajbh
            },
            type : 'post',
            dataType : 'json',
            async : false,
            success : function(jsonBean){
                if(jsonBean.success){
                	var viewForm = jsonBean.data;
                	$("#viewForm").append(viewForm);
                	$(".u-table").css("padding",0);
                	$(".u-table").addClass("m-table");
                	$(".u-table").find("td").addClass("m-text");
                	$(".u-table").removeClass("u-table");
                	$(".u-item").addClass("m-item");
                	$(".u-item").removeClass("u-item m-text");
                }else{
                    K.msg.mini.error(jsonBean.message);
                }
            }
        });
	});
	
	
</script>

<div class="easyui-layout" fit="true">
	<div id="viewForm" data-options="region:'center'" border="false"></div>
</div>


<%@ include file="/WEB-INF/inc/footer.jsp"%>