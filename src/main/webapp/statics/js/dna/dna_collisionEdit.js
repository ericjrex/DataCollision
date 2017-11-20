var DataEdit = {
	
	initialize : function(configCode, ywId) {
		this.configCode = configCode;
		this.ywId = ywId;
		Sys.msg.initCurPage(configCode + "$mware$edit", "编辑页面");
		
		this.valMap = K.utils.map();
		
		this.valFieldName = ["DCD_SJH","DCD_QQ","DCD_WX","DCD_JD","DCD_TB","DCD_MAC","DCD_IMEI","DCD_IMSI","DCD_JD0","DCD_WD","XZD_CPH","DCD_DZ"];
		this.fieldNameValIndexMap = K.utils.map();
		this.fieldNameValMap = K.utils.map();
		this.isPush = K.isNotBlank(isPush) ? true : false;
		this.isEditSfz = false;
	},
		
	initDirInfoData : function() {
		var me = this;
		me.middleware = new Middleware();
		me.middleware.load({
			configCode : me.configCode,
			editForm : "#editForm",
			isSsingle : window.context.isSsingle,
			ywId : me.ywId,
			onAfterRender : function() {
				me.isTempData = me.middleware.isTempData();
				me.dirInfoId = me.middleware.getDirInfo().id;
			}
		});
		
		
		// 处理特殊字段
		// 碰撞编号
		var pzbh_td = $("input[name='DCD_PZBH']").parent('td');
		var pzbh = $("input[name='DCD_PZBH']").val();
		$("input[name='DCD_PZBH']").hide();
		pzbh_td.append("<span id='DCD_PZBH'>" + pzbh + "</span>");
		// 案件编号
		var ajbh_td = $("input[name='DCD_AJBH']").parent('td');
		var ajbh = $("input[name='DCD_AJBH']").val();
		$("input[name='DCD_AJBH']").hide();
		ajbh_td.append("<span id='DCD_AJBH'>" + ajbh + "</span>");
		// 姓名
		var xm_td = $("input[name='DCD_XM']").parent('td');
		var xm = $("input[name='DCD_XM']").val();
		$("input[name='DCD_XM']").hide();
		xm_td.append("<span id='DCD_XM'>" + xm + "</span>");
		// 身份证号
		var sfzh_td = $("input[name='DCD_SFZH']").parent('td');
		var sfzh = $("input[name='DCD_SFZH']").val();
		if(K.isNotBlank(sfzh)){
			$("input[name='DCD_SFZH']").hide();
			sfzh_td.append("<span id='DCD_SFZH'>" + sfzh + "</span>");
		}else{
			me.isEditSfz = true;
		}
		
		//受理单位
		var sldw_td = $("input[name=DCD_SLDW]").parent('td');
		var sldw= $("input[name=DCD_SLDW]").val();
		$("input[name=DCD_SLDW]").hide();
		sldw_td.append("<span id='DCD_SLDW'>"+sldw+"</span>");
		
		var sldwbh_td = $("input[name=DCD_SLDWBH]").parent('td');
		var sldwbh= $("input[name=DCD_SLDWBH]").val();
		$("input[name=DCD_SLDWBH]").hide();
		sldwbh_td.append("<span id='DCD_SLDWBH'>"+sldwbh+"</span>");
		
		//分配时间
		var fpsj_td = $("input[name=DCD_FPSJ]").parent('td');
		var fpsj= $("input[name=DCD_FPSJ]").val();
		$("input[name=DCD_FPSJ]").hide();
		fpsj_td.append("<span id='DCD_FPSJ'>"+fpsj+"</span>");
		
		//下发时间
		var xfsj_td = $("input[name=DCD_XFSJ]").parent('td');
		var xfsj= $("input[name=DCD_XFSJ]").val();
		$("input[name=DCD_XFSJ]").hide();
		//受理单位编号
		var sldwbh_td = $("input[name=DCD_SLDWBH]").parent('td');
		var sldwbh= $("input[name=DCD_SLDWBH]").val();
		xfsj_td.append("<span id='DCD_XFSJ'>"+xfsj+"</span>");
		
		//数据状态
		var xfsj_td = $("input[name=DCD_XFSJ]").parent('td');
		var xfsj= $("input[name=DCD_XFSJ]").val();
		$("input[name=DCD_XFSJ]").hide();
		xfsj_td.append("<span id='DCD_XFSJ'>"+xfsj+"</span>");
		
		
		$.each(me.valFieldName,function(i,fieldName){
			var sjh_td = $("input[name="+fieldName+"]").parent('td');
			var storeVals = $("input[name="+fieldName+"]").val();
			$("input[name="+fieldName+"]").hide();
			var indexList = [];
			var sjh_html = "";
			if(K.isNotBlank($("input[name=ID]").val())){
				if(K.isNotBlank(storeVals)){
					var valStr = storeVals.split(";");
					$.each(valStr,function(i,val){
						if(i==0){
							if("DCD_DZ" == fieldName){
								sjh_html += "<input name='"+fieldName+"_"+i+"' class='"+fieldName+"'  style='width:90%;'  value='"+val+"'>" +
								"<input id='add_"+fieldName+"' class='addBtn' type='button' value='添加' >";
							}else{
								sjh_html += "<input name='"+fieldName+"_"+i+"' class='"+fieldName+"'  style='width:85%;'  value='"+val+"'>" +
								"<input id='add_"+fieldName+"' class='addBtn' type='button' value='添加' >";
							}
							
						}else{
							if("DCD_DZ" == fieldName){
								sjh_html += "<div id='div_"+fieldName+"_"+i+"'>" +
								"<input name='"+fieldName+"_"+i+"' class='"+fieldName+"'  style='width:90%;' value='"+val+"'>" +
								"<input onclick='delValLabel(this)' name='del_"+fieldName+"_"+i+"' type='button' value='删除'>" +
								"</div>";
							}else{
								sjh_html += "<div id='div_"+fieldName+"_"+i+"'>" +
								"<input name='"+fieldName+"_"+i+"' class='"+fieldName+"'  style='width:85%;'  value='"+val+"'>" +
								"<input onclick='delValLabel(this)' name='del_"+fieldName+"_"+i+"' type='button' value='删除'>" +
								"</div>";
							}
						}
						indexList.push(i);
					});
				}else{
					if("DCD_DZ" == fieldName){
						sjh_html = "<input name='"+fieldName+"_0' class='"+fieldName+"'  style='width:90%;'>" +
						"<input id='add_"+fieldName+"' class='addBtn' type='button' value='添加' >";
						indexList.push(0);
					}else{
						sjh_html = "<input name='"+fieldName+"_0' class='"+fieldName+"'  style='width:85%;'>" +
						"<input id='add_"+fieldName+"' class='addBtn' type='button' value='添加' >";
						indexList.push(0);
					}
				}
			}else{
				sjh_html = "<input name='"+fieldName+"_0' class='"+fieldName+"'  style='width:85%;'>" +
							"<input id='add_"+fieldName+"' class='addBtn' type='button' value='添加' >";
				indexList.push(0);
			}
			me.fieldNameValIndexMap.put(fieldName,indexList);
			sjh_td.append(sjh_html);
		});
		
		me.initMyListen();
		
	},
	
	initMyListen : function(){
		var me = this;
		$(".addBtn").bind('click',function(){
			me.addValLabel(this);
		});
		
		$("#saveBtn").bind("click",function(){
			me.saveDirInfoData();
		});
		
		
		
	},
	
	addValLabel : function(label){
		var me = this;
		var labelId = label.id;
		var fieldName = labelId.split("_")[1] +"_"+ labelId.split("_")[2] ;
		var indexList = me.fieldNameValIndexMap.get(fieldName);
		var index = indexList[indexList.length -1];
		/*if(indexList == undefined || indexList == null){
			indexList = [];
			index = 0;
		}*/
		index ++;
		indexList.push(index);
		
		me.fieldNameValIndexMap.put(fieldName,indexList);
		
		var parent_td = $("input[name="+fieldName+"]").parent('td');
		var valLength = $("."+fieldName+"").length;
		var inputHtml = "";
		if("DCD_DZ" == fieldName){
			inputHtml = "<div id='div_"+fieldName+"_"+index+"'>" +
			"<input name='"+fieldName+"_"+index+"' class='"+fieldName+"'  style='width:90%;'>" +
			"<input onclick='delValLabel(this)' name='del_"+fieldName+"_"+index+"' type='button' value='删除'>" +
			"</div>";
		}else{
			inputHtml = "<div id='div_"+fieldName+"_"+index+"'>" +
			"<input name='"+fieldName+"_"+index+"' class='"+fieldName+"'  style='width:85%;'>" +
			"<input onclick='delValLabel(this)' name='del_"+fieldName+"_"+index+"' type='button' value='删除'>" +
			"</div>";
		}
		parent_td.append(inputHtml);
	},
	
	delValLabel : function(label){
		var me = this;
		
		var labelName = label.name;
		var lenght = labelName.split("_").length;
		
		var labelIndex = labelName.split("_")[lenght-1];
		var fieldName = labelName.split("_")[1] +"_"+ labelName.split("_")[2] ;
		
		var indexList = me.fieldNameValIndexMap.get(fieldName);
		var deleteListIndex = indexList.indexOf(labelIndex);
		if(deleteListIndex > -1){
			indexList.splice(1,deleteListIndex);
		}
		
		$("#div_"+fieldName+"_"+labelIndex+"").remove();
	},
	
	saveDirInfoData : function(data) {
		var me = this;
		var form = $('.edit-form')[0];
		// 保存之前处理
		var params = {
			configCode : me.configCode,
			isTempData : me.isTempData,
			isPush : me.isPush
		};
		isValidData = data == "save_valid" || data == "save_valid_add";
		params.validForm = isValidData;
		
		var isReturn = me.preSaveDirInfoData(form, params);
		if (!isReturn) {
			return;
		}
		if(isValidData) {
			if (!me.validateForm(form)) {
				K.msg.mini.error('表单验证不通过');
				return;
			}
		}
		var addAction = CmsAction.addDirInfoData, updateAction = CmsAction.updateDirInfoData;
		if(window.context != null && K.isObject(window.context)) {
			if(K.isNotBlank(window.context.addAction)) {
				addAction = window.context.addAction;
			}
			if(K.isNotBlank(window.context.updateAction)) {
				updateAction = window.context.updateAction;
			}
		}
		
		$.each(me.valFieldName,function(i,fieldName){
			var valList = [];
			var indexList =  me.fieldNameValIndexMap.get(fieldName);
			
			if(indexList != undefined){
				$.each(indexList,function(i,index){
					var input = $("input[name="+fieldName+"_"+index+"]");
					if(input){
						var val = input.val();
						if(K.isNotBlank(val)){
							valList.push(val);
						}
					}
				});
				$("input[name="+fieldName+"]").val(valList.join(";"));
			}
		});
		
		if(!checkMobile()){
			K.msg.mini.error("手机号码有误，请重填");
			return false;
		}
		
		if(!checkVehicleNumber()){
			K.msg.mini.error("车牌号有误，请重填");
			return false;
		}
		
		var sfzh = $("input[name='DCD_SFZH']").val();
		if(K.isBlank(sfzh)){
			$("input[name='DCD_SFZH']").addClass("error");
			K.msg.mini.error("身份证不能为空，请填充");
			return false;
		}else{
			if(me.isEditSfz){
				var isFalg = IdCardValidate(sfzh);
				if(!isFalg){
					$("input[name='DCD_SFZH']").addClass("error");
					K.msg.mini.error("身份证填写有误");
					return false;
				}
			}else{
				$("input[name='DCD_SFZH']").removeClass("error");
			}
		}
		var checkMessage = me.checkPersonCollisonIsExist(sfzh);
		if(K.isNotBlank(checkMessage)){
			if(confirm(checkMessage)){
				K.form.ajaxSubmit({
					url : me.isAdd() ? addAction : updateAction,
					form : form,
					validate : isValidData,
					data : params,
					success : function(jsonBean) {
						if (jsonBean.success) {
							//alert(jsonBean.message);
							//K.msg.mini(jsonBean.message);
							Sys.msg.pub(me.configCode + "$mware$edit@success", jsonBean);
							var ywId = jsonBean.data;
							if(K.isString(ywId)) {
								$("#ID").val(ywId);
							}
							if(me.isPush){
								parent.dataIndex.closePushWindow(jsonBean.message);
							}
							// 保存之后处理
							me.afterSaveDirInfoData(jsonBean, data, form);
							if (data == "save_add" || data == "save_valid_add") {
								me.clearForm(form);
							}
						} else {
							if(K.isNotBlank(jsonBean.message)){
								K.msg.mini.error(jsonBean.message);
							}
							var validateResult = jsonBean.attributes.validateResult;
							if (validateResult != null && K.isArray(validateResult)) {
								me.renderValidateResult(validateResult);
								$('#check_info_window').show();
								$('#check_info_window').window("open");
								$.each(validateResult, function(i, v) {
									var el = $('#' + v.fieldName);
									el.addClass('validError');
									el.one('change', function() {
										$(this).removeClass('validError');
									});
								});
								return;
							}
						}
					}
				});
			}
		}
	},
	
	checkPersonCollisonIsExist : function(sfzh){
		var me = this;
		var message = "";
		$.ajax({
            url : DnaAction.checkPersonCollisonIsExist,
            data : {
            	sfzh : sfzh
            },
            type : 'post',
            dataType : 'json',
            async : false,
            success : function(jsonBean){
                if(jsonBean.success){
                	message = jsonBean.message;
                }else{
                    K.msg.mini.error(jsonBean.message);
                }
            }
        });
		return message;
	}

};

$.extend(AbstractDataEdit, DataEdit);
DataEdit = Class.create(AbstractDataEdit, AbstractDataEdit);

//校验手机号
function checkMobile() {
	var isFalg = true;
	var inputs = $(".DCD_SJH");
	$.each(inputs, function(i, input) {
		var sMobile = $(input).val();
		if(K.isNotBlank(sMobile)){
			if(!(/^1(3|4|5|7|8)\d{9}$/.test(sMobile))){ 
				$(input).addClass("error");
				//$(input).focus();
				//K.msg.mini.error("手机号码有误，请重填");
				isFalg = false;
			}
		}else{
			$(input).removeClass("error");
		}
	});
	return isFalg;
} 

//校验车牌号
function checkVehicleNumber() {
	var isFalg = true;
	var inputs = $(".XZD_CPH");
	$.each(inputs, function(i, input) {
		var vehicleNumber = $(input).val();
		K.log(vehicleNumber);
		
		if(K.isNotBlank(vehicleNumber)){
			var express = /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$/;
			isFalg = express.test(vehicleNumber);
			if(!isFalg){
				$(input).addClass("error");
				//$(input).focus();
				//K.msg.mini.error("车牌号有误，请重填");
			}
		}
	});
    return isFalg;
}





var Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 ];    // 加权因子     
var ValideCode = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ];            // 身份证验证位值.10代表X     
function IdCardValidate(idCard) {   
    idCard = trim(idCard.replace(/ /g, ""));               //去掉字符串头尾空格                       
    if (idCard.length == 15) {     
        return isValidityBrithBy15IdCard(idCard);       //进行15位身份证的验证      
    } else if (idCard.length == 18) {     
        var a_idCard = idCard.split("");                // 得到身份证数组     
        if(isValidityBrithBy18IdCard(idCard)&&isTrueValidateCodeBy18IdCard(a_idCard)){   //进行18位身份证的基本验证和第18位的验证  
            return true;     
        }else {     
            return false;     
        }     
    } else {     
        return false;     
    }     
}   

/**   
 * 判断身份证号码为18位时最后的验证位是否正确   
 * @param a_idCard 身份证号码数组   
 * @return   
 */    
function isTrueValidateCodeBy18IdCard(a_idCard) {     
    var sum = 0;                             // 声明加权求和变量     
    if (a_idCard[17].toLowerCase() == 'x') {     
        a_idCard[17] = 10;                    // 将最后位为x的验证码替换为10方便后续操作     
    }     
    for ( var i = 0; i < 17; i++) {     
        sum += Wi[i] * a_idCard[i];            // 加权求和     
    }     
    valCodePosition = sum % 11;                // 得到验证码所位置     
    if (a_idCard[17] == ValideCode[valCodePosition]) {     
        return true;     
    } else {     
        return false;     
    }     
}     
/**   
  * 验证18位数身份证号码中的生日是否是有效生日   
  * @param idCard 18位书身份证字符串   
  * @return   
  */    
function isValidityBrithBy18IdCard(idCard18){     
    var year =  idCard18.substring(6,10);     
    var month = idCard18.substring(10,12);     
    var day = idCard18.substring(12,14);     
    var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));     
    // 这里用getFullYear()获取年份，避免千年虫问题     
    if(temp_date.getFullYear()!=parseFloat(year)     
          ||temp_date.getMonth()!=parseFloat(month)-1     
          ||temp_date.getDate()!=parseFloat(day)){     
            return false;     
    }else{     
        return true;     
    }     
}     
  /**   
   * 验证15位数身份证号码中的生日是否是有效生日   
   * @param idCard15 15位书身份证字符串   
   * @return   
   */    
  function isValidityBrithBy15IdCard(idCard15){     
      var year =  idCard15.substring(6,8);     
      var month = idCard15.substring(8,10);     
      var day = idCard15.substring(10,12);     
      var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));     
      // 对于老身份证中的你年龄则不需考虑千年虫问题而使用getYear()方法     
      if(temp_date.getYear()!=parseFloat(year)     
              ||temp_date.getMonth()!=parseFloat(month)-1     
              ||temp_date.getDate()!=parseFloat(day)){     
                return false;     
        }else{     
            return true;     
        }     
  }     
//去掉字符串头尾空格     
function trim(str) {     
    return str.replace(/(^\s*)|(\s*$)/g, "");     
}  