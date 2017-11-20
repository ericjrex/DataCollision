<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:include page="/WEB-INF/inc/common.jsp" />
<jsp:include page="/WEB-INF/inc/header.jsp">
	<jsp:param name="title" value="目录数据主页" />
</jsp:include>

<script type="text/javascript">
    var sessionId = '<%=session.getId()%>';
</script>

<link href="${mwareStaticsPath}/css/fillin_css.css" rel="stylesheet" type="text/css" />
<style type="text/css">
.customDiv {
	height:260;
	width:280;
    background: #FFF;
    border: 1px solid #CCC;
    padding: 6px 10px 6px 10px;
    color: #333;
    border-radius: 4px;
    float:left;
}
.customDiv:hover {
    background: #E3E3E3;
    border: 1px solid #CCC;
    padding: 6px 10px 6px 10px;
    color: #333;
    border-radius: 4px;
}

 .button {
background: #B0E2FF;
border: 1px solid #FFF;
padding: 6px 10px 6px 10px;
color: #333;
border-radius: 4px;
}
.button:hover {
color: #1874CD;
background-color: #FFF;
border-color: #FFF;
}
 .button-blue {
background: #C6E2FF;
border: 1px solid #CCC;
padding: 6px 10px 6px 10px;
color: #333;
border-radius: 4px;
}
.button-blue:hover {
color: #1874CD;
background-color: #EBEBEB;
border-color: #ADADAD;
}
 .button-red {
background: #FF8C69;
border: 1px solid #CCC;
padding: 6px 10px 6px 10px;
color: #333;
border-radius: 4px;
}
.button-red:hover {
color: #242424;
background-color: #FFC125;
border-color: #ADADAD;
}
.boxClass{
  border-radius:10px 10px 10px 10px;
  border: 2px solid gray;
}
.boxClass:hover{
border:1px solid bule;
border-color:#000;
background-color: #F0F0F0;
}
.noBorder{
  border:none;
}

.file {
    position: relative;
    display: inline-block;
    background: #D0EEFF;
    border: 1px solid #99D3F5;
    border-radius: 4px;
    padding: 4px 12px;
    overflow: hidden;
    color: #1E88C7;
    text-decoration: none;
    text-indent: 0;
    line-height: 20px;
}
.file input {
    position: absolute;
    font-size: 100px;
    right: 0;
    top: 0;
    opacity: 0;
}
.file:hover {
    background: #AADFFD;
    border-color: #78C3F3;
    color: #004974;
    text-decoration: none;
}

/*a  upload */
.a-upload {
    padding: 4px 10px;
    height: 20px;
    line-height: 20px;
    position: relative;
    cursor: pointer;
    color: #888;
    background: #fafafa;
    border: 2px solid #8C8C8C;
    border-right : 1px groove;
    border-radius: 5px 0 0 5px;
    overflow: hidden;
    display: inline-block;
    *display: inline;
    *zoom: 1
}

.a-upload  input {
    position: absolute;
    font-size: 100px;
    right: 0;
    top: 0;
    opacity: 0;
    filter: alpha(opacity=0);
    cursor: pointer
}

.a-upload:hover {
    color: #444;
    background: #eee;
    border-color: #ccc;
    text-decoration: none
}
.showFileName {
 	position: absolute;
	padding: 4px 10px;
	border: 2px solid #8C8C8C;
	border-left : none;
 	border-radius: 0 5px 5px 0;
 	vertical-align:text-bottom;
 	height : 32px;
 	width : 50%
}
.showFileName:hover {
    color: #444;
    background: #eee;
    border-color: #ccc;
    text-decoration: none;
    cursor:hand
}
</style>

<link href="${mwareStaticsPath}/css/fillin_css.css" rel="stylesheet" type="text/css" />



<script type="text/javascript" src="${appStaticsPath}/jqueryFileUpload/ajaxfileupload.js"></script>
<script type="text/javascript" src="${appStaticsPath}/js/dna/holder/UploadImage.js"></script>
<script type="text/javascript" src="${appStaticsPath}/js/dna/dna_inport.js"></script>

<script type="text/javascript">
	
	var importTimer = null;//导入时间变量
	var picType =  "BMP bmp JPG jpg JPEG jpeg PNG png GIF gif ";
	var dataInport = null;
	var index = 0;
	$(function() {
		var width = $("body").width() * 8/10;
		//$("#progressbar").width(width);
		
		dataInport = new DataInport();
		dataInport.render();
		$("#uploadBtn").bind('click',function(){
			var val = $("#files").val();
			if(K.isBlank(val)){
				K.msg.mini.error("请选择上传文件");
				return ;
			}
			K.form.ajaxSubmit({
				iframe : true,
				mask : false,
				url : $appPath + "/dna/collision/excelUpdate.do",
				form : "#fileForm",
				data : {
				}
			});
		});
		
		var height = window.innerHeight*5/8;
		var width = window.innerWidth*3/8;
		
		var divHtml ="<div style='width:99%' ><a href='javascript:;' class='a-upload'>选择文件<input id='files' class='files' name='files' accept='.xls,.xlsx' type='file' value='浏览' src=''   onchange='uploadFile(this)'>"
			+ "</a>"
			+ "<input class='showFileName'  type='text' readonly='true'>"
			+ "<input class='fileerrorTip' type='text' style='display:none;' readonly='true'>"
			+ "</div>"
			//+ "<input type='button' id='uploadBtn' value='上传'>"
			+ "<div class='boxClass' align='center'>"
			+ "<div id='box' class='noBorder' style='width: 99%; height: "+height+"; overflow-y:scroll;' contenteditable='false'>" 
			+ "<div id='tipDiv' style='color:#aaa;font-size: 40px;position:absolute;left:50%;top:30%;' align='center'>拖拽文件上传</div>" 
			+ "</div>"
			+ "</div>";
			
		$("#fileForm").append(divHtml);
		
		 $(".showFileName").bind('click',function(){
			 $("#files").click();
		});
		 
		var url = $appPath + "/dna/collision/excelUpdate.do";
		new UploadImage("box", url).upload(function (xhr) {//上传完成后的回调
			dataInport.buildFileDiv(xhr);
			//importHKey = K.random(32);
        }); 
		
	});
	
	function deleteDataInfo(index){
		dataInport.deleteDataInfo(index);
	}
	
	function uploadFile(label){
		 var filePath=$("#files").val();
		    if(filePath.indexOf("xls")!=-1 || filePath.indexOf("xlsx")!=-1){
		        $(".fileerrorTip").val("").hide();
		        var arr=filePath.split('\\');
		        var fileName=arr[arr.length-1];
		        $(".showFileName").val(fileName);
		    }else{
		        $(".showFileName").val("");
		        $(".fileerrorTip").val("您未上传文件，或者您上传文件类型有误！").show();
		        return false 
		    }
		dataInport.uploadFile();
	}
	
</script>

<div class="easyui-layout" fit="true">
	<div style="font-size: 15px;color: blue;">功能说明：系统根据上传的excel数据更新碰撞结果数据(一次只能上传一个excel文件)</div>
	<div>下载表格模板：
						Excel2003【<a href="${appPath}/dna/collision/xls/downloadModel.do">寻踪导入模板.xls</a>】&nbsp;&nbsp;&nbsp;&nbsp;
						Excel2007【<a href="${appPath}/dna/collision/xlsx/downloadModel.do">寻踪导入模板.xlsx</a>】&nbsp;&nbsp;&nbsp;&nbsp;
						</div>
	<form id="fileForm" method="post" style="width: 100%;">
	</form>
</div>

<div id="upload_excel_data_window" title="导入数据" style="width: 420px; height: 220px;" data-options="closed:true">
	<div style="line-height: 23px;">&nbsp;&nbsp;&nbsp;&nbsp;导入的数据可能比较多，请耐心等待！</div>
	<br />
	<table style="width: 100%;">
		<tr height="25px;">
			<td><div id="uploadInfo">正在打开文件，请等待....</div></td>
		</tr>
		<tr height="30px;">
			<td>
				<div id="progressbar" class="easyui-progressbar" style="width: 380px;"></div>
			</td>
		</tr>
	</table>
	<br />
</div>

<%@ include file="/WEB-INF/inc/footer.jsp"%>