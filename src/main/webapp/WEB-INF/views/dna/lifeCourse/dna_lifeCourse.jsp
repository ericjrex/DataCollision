<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:include page="/WEB-INF/inc/common.jsp" />
<jsp:include page="/WEB-INF/inc/header.jsp">
	<jsp:param name="title" value="碰撞人员信息生命历程主页" />
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

<script type="text/javascript" src="${appStaticsPath}/echarts/echarts.js"></script>
<script type="text/javascript" src="${appStaticsPath}/js/dna/dna_collision_meta.js"></script>
<script type="text/javascript" src="${appStaticsPath}/js/dna/lifeCourse/dna_lifeCourse.js"></script>

<script type="text/javascript">

	var lifeCourseIndex = null;
	var pzbh = "${pzbh}";
	var configCode = "${configCode}"; 
	
	$(function() {
		lifeCourseIndex = new LifeCourseIndex(pzbh,configCode);
	});
	

</script>

<div id="main" style="height:100%;-webkit-tap-highlight-color: transparent; -webkit-user-select: none; background: rgb(51, 51, 51);"></div>

<%@ include file="/WEB-INF/inc/footer.jsp"%>