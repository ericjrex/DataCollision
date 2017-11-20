<%@page language="java" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/inc/common.jsp" />
<jsp:include page="/WEB-INF/inc/header.jsp">
	<jsp:param name="title" value="统计报表" />
</jsp:include>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>

<script type="text/javascript">
	var sysTime = null, monthFirstDay = null, today = null;
	var organizationCode = "${organizationCode}";
	var isPstartTime = "${startTime}";
	var isPendTime = "${endTime}";
	var isFirstLoad = true;
	
	$(function() {
		sysTime = format_time(Sys.systemTime);
		sysTime = sysTime.substring(0, 10);
		endTime = format_time(Sys.systemTime);
		K.log(sysTime);
		
		monthFirstDay = sysTime.substring(0, 8) + "01" + " 00:00:00";
		
		K.log(monthFirstDay);
		//monthFirstDay= monthFirstDay;
		tsSubmit();
	});
	function tsSubmit() {
		if(isFirstLoad){
			$("#startTimeIn").val(isPstartTime);
			$("#endTimeIn").val(isPendTime);
		}
		isFirstLoad = false;
		
		if ($("#startTimeIn").val() == "") {
			$("#startTime").val(monthFirstDay);
		} else {
			$("#startTime").val($("#startTimeIn").val());
		}
		if ($("#endTimeIn").val() == "") {
			$("#endTime").val(endTime);
		} else {
			$("#endTime").val($("#endTimeIn").val());
		}
		
		var isOrgCode = "1";
		if(K.isBlank(organizationCode)) {
			var subOrgCodes = $("#sub_orgCode").zzChosen("getValue");
			if(K.isBlank(subOrgCodes)) {
				isOrgCode = "0";
			}
		}
		
		/* if(isPolice){
			$("#sub_orgCode").val(organizationCode);
		}else{
			
		} */
		$("#is_orgCode").val(isOrgCode);
		
		K.log($("#startTime").val());
		$('#searchForm').submit();
	}
	
	
	//派出所报表
	function showPolice(code){
		var options = $("#sub_orgCode").find("option");
		/* $.each(options,function(i,option){
			var orgNaME = option.innerHTML;
			
			var vaule = option.value.substr(0,6);
			var url = $appPath + "/report/default/countByPolice.do";
			var width = $("body").width();
			var height = $("body").height();
			if(orgNaME.indexOf(code) > -1){
				var startTime = $("#startTime").val();
				var endTime = $("#endTime").val();
				K.window.iframewindow({
		            title : code + "-案件标准化工作进度汇总表",
		            href : url,
		            id : "policReportJsp",
		            width : width * 8/9,
		            height : height * 8/9,
		            params : {
		            	orgName : encodeURI(code),
		            	reportName: 'caseProgressByPolice',
		            	startTime : startTime,
		            	endTime : endTime,
		            	orgCode : vaule
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
		}); */
	}
	
</script>

<div class="easyui-layout" fit="true">
	<div data-options="region:'north'" style="height: 50px;" border="false">
		<div style="height:15px;"></div>
		<form id="searchForm" target="main" action="${appPath}/report/reportJsp/loadReport.jsp" method="post"
			style="text-align: center; heigth: 35px;">
			<input type="hidden" name="raq" value="${reportName}.raq" />
			<input type="hidden" name="orgName" value="${orgName}" />
			<!-- <input id="is_orgCode" name="is_orgCode" type="hidden"/> -->
			<table style="margin: auto;" class="search3">
				<tr valign="bottom">
					<c:if test="${orgList != null}">
						<td class="item"><span id="queryLabel">公安分局名称</span>：</td>
						<td width="280px">
							<select id="sub_orgCode" name="sub_orgCode" style="width: 200px;" class="zzui-zzchosen" multiple="multiple">
								<c:forEach items="${orgList}" var="org">
									<option value="${org.organizationCode}" letter="${org.extend1}">${org.name}</option>
								</c:forEach>
							</select>
							<input id="is_orgCode" name="is_orgCode" type="hidden" value="0"/>
						</td>
					</c:if>
					<c:if test="${orgList == null}">
						<input id="sub_orgCode" name="sub_orgCode" type="hidden" value="${organizationCode}"/>
					</c:if>
					<%-- <c:if test="${isYYth == null}">
						<td class="item"><span id="queryLabel">${queryLabel}</span>：</td>
						<td>从<input id="startTimeIn" class="input Wdate"
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',maxDate:'#F{$dp.$D(\\\'endTimeIn\\\')}'})" /> 
							至<input id="endTimeIn" class="input Wdate"
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',minDate:'#F{$dp.$D(\\\'startTimeIn\\\')}'})" /> 
							<input id="startTime" name="startTime" type="hidden" />
							<input id="endTime" name="endTime" type="hidden" />
						</td>
						<td style="text-align: left;">
							&nbsp;&nbsp;&nbsp;
							<img src="${appPath}/report/images/query.jpg" onclick="tsSubmit()" style="cursor: pointer; vertical-align: middle">
						</td>
					</c:if> --%>
					<td style="text-align: left;">
						&nbsp;&nbsp;&nbsp;
						<img src="${appPath}/report/images/query.jpg" onclick="tsSubmit()" style="cursor: pointer; vertical-align: middle">
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',split:true" border="false">
		<iframe name="main" style="width: 100%; height: 800px;" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="auto"
			allowtransparency="yes"> </iframe>
	</div>
</div>

<jsp:include page="/WEB-INF/inc/footer.jsp" />