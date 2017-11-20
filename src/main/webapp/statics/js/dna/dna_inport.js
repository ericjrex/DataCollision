var importHKey = K.random(32); // 导入任务key

var DataInport =Class.create({
	
	// 页面渲染
	render : function() {
		// 导入窗口
		$('#upload_excel_data_window').window({
			closable : true,
			minimizable : false,
			maximizable : false,
			collapsible : false,
			modal : true,
			resizable : false,
			onClose : function() {
				clearInterval(importTimer);
				importTimer = null;
			}
		});
	},
		
	uploadFile : function(){
		var me =this;
		$('#uploadInfo').html('');
		var cellIndex = [];
		var url = $appPath + "/dna/collision/excelUpdateByManual.do";
		var fileVal = $("#files").val();
		if(K.isNotBlank(fileVal)){
			$('#upload_excel_data_window').window('open');
			//K.mask();
			$.ajaxFileUpload({  
		   		 url : url,
		   		 secureuri : false,// 一般设置为false
		   		 fileElementId : "files" ,// 文件上传表单的id <input type="file"
											// id="fileUpload" name="file" />
		   		 dataType : 'json',// 返回值类型 一般设置为json
		   		 data : {
		   			importHKey : importHKey
		   		 },
		   		 type : 'post',
		   		 //async : false,
		   		 success : function(jsonBean) {
		   			var xhr = {};
		   			xhr.responseText = jsonBean;
		   			 me.buildFileDiv(xhr);
		   			$(".showFileName").val('');
		   			//K.unmask();
		   		 }
			});
			$('#progressbar').progressbar('setValue', 0);
			importTimer = setInterval(me.updateProgressbar.bind(me), 3000);
		}
		
	},
	
	// 更新
	updateProgressbar : function() {
		var me = this;
		K.ajax({
			dataType : 'json',
			mask : false,
			url : $appPath + "/zygl/mwareCms/importState.do",
			data : {
				importKey : importHKey
			},
			success : function(jsonBean) {
				if (jsonBean.success) {
					me.uProgressFun(jsonBean);
				}
			}
		});
	},
	
	uProgressFun : function(jsonBean) {
		var me = this;
		var data = jsonBean.data;

		// 导出进度
		if (data.importDataCount != null && data.importDataTotal != null) {
			var val = 0;
			if(data.importDataTotal > 0) {
				val = Math.floor(data.importDataCount / data.importDataTotal * 100);
			}
			$('#progressbar').progressbar('setValue', val);
			var str = '已导入数据' + val + '%(' + data.importDataCount + '/' + data.importDataTotal + ')，请稍候...';
			$('#uploadInfo').html(str);
		}

		// 导出错误
		if (data.importError) {
			
			var importInfo = data.importErrorInfo;
			$('#uploadInfo').html("<span style='color:red;padding-left:20px;' class='item-error'>" + importInfo + "</span>");
			if("正在处理数据，请稍后..." == importInfo || "正在记录日志，请稍后..."  == importInfo || "导入数据完成" == importInfo){
			}else{
				clearInterval(importTimer);
				exportTimer = null;
				return;
			}
		}
		
		// 导出完成
		if (data.importFinished) {
			var importInfo = data.importErrorInfo;
			$('#progressbar').progressbar('setValue', 100);
			$('#uploadInfo').html("<span style='color:red;padding-left:20px;' class='item-error'>" + importInfo + "</span>");
			if("正在处理数据，请稍后..." == importInfo || "正在记录日志，请稍后..."  == importInfo){
			}else{
				//$('#uploadInfo').html('导入数据完成！');
				clearInterval(importTimer);
				importTimer = null;
				importHKey = K.random(32);
			//	Sys.msg.pub("zygl$zytg$cms$upload@success", "上传成功");
				
				K.msg.mini("上传数据成功");
				//$('#upload_excel_data_window').window('close');
			}
		}
	},
	
	buildFileDiv : function(xhr){
		var me = this;
		var src = "";
		var fileName = "";
		var fileListJson = xhr.responseText;
		var jsonBean = null;
		var fileList = null;
		if(fileListJson instanceof Object){
			jsonBean = fileListJson;
			if(!jsonBean.success){
				K.msg.mini.error(jsonBean.message);
				return false;
			}
			fileList = jsonBean.data;
		}else{
			if(K.isNotBlank(fileListJson)){
				jsonBean = eval('(' + fileListJson + ')'); 
				if(!jsonBean.success){
					K.msg.mini.error(jsonBean.message);
					return false;
				}
				fileList = jsonBean.data;
			}
		}
		if(fileList != null){
			$.each(fileList,function(i,fileName){
				index++;
				/*src = data.fileId;
				src = src.replace(/\"/g,"");
				*/
				
				var origName = fileName;
				var lastOfIndex = fileName.lastIndexOf(".");
				var fileType = fileName.substr(lastOfIndex + 1,fileName.length);
				fileName = fileName.substr(0,lastOfIndex);
				if(fileName.length > 20){
					fileName = fileName.substr(0,18) +"……"+ "."+fileType;
				}else{
					fileName = fileName + "."+fileType;
				}
				
				var imgHtml = "";
				var filePicUrl = $appStaticsPath + '/fileUploadJs/img/filePic.png';
				var errorPicUrl = $appStaticsPath + '/fileUploadJs/imgerror_big.png';
				if(picType.indexOf(fileType)>-1){
					if($("#tipDiv")){
						$("#tipDiv").hide();
					}
					 imgHtml += "<div id='div_"+index+"' class='customDiv'>"
					 +"<div align='center'>"
					 +"<img style='width:240px;height:220px;' src='"+src+"' title='"+origName+"'>"
					 +"<div title='"+origName+"'>"+fileName+"</div>"
					 +"<input class='button-red' onclick=\"deleteDataInfo('"+src+"','"+index+"')\" id='delete_"+src+"' name='delete_"+src+"' style='width:65px'  type='button' value='删除'>"
					 +"<div>"
					 +"<div>";
				}else{
					if($("#tipDiv")){
						$("#tipDiv").hide();
					}
					 imgHtml += "<div id='div_"+index+"' class='customDiv'>"
					 +"<div align='center'>"
					 +"<img style='width:240px;height:220px;' src='"+filePicUrl+"' title='"+fileName+"'>"
					 +"<div title='"+fileName+"'>"+fileName+"</div>"
					 +"<input class='button-red' onclick=\"deleteDataInfo('"+index+"')\"  style='width:65px'  type='button' value='删除'>"
					 +"<div>"
					 +"<div>";
				}
				
				$("#box").append(imgHtml);
				$("#box").append("&nbsp;");
			});
		}
	},

	deleteDataInfo : function(index){
		var me = this;
		me.reBuildPicDiv(index);
	},
	
	reBuildPicDiv : function(index){
		var me = this;
		$("#div_"+index+"").remove();
		
		if($(".customDiv").length == 0){
			$("#tipDiv").show();
		}
	}
	
	
		
		
});