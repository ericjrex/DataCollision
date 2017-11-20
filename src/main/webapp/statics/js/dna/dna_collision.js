/**
 * 后端生成页面的js
 */
var DataIndex = {
	
	initialize : function(configCode) {
		if(K.isBlank(configCode)) {
			alert("表单个性化配置编码为空！");
			return;
		}
		this.configCode = configCode;
		Sys.msg.initCurPage(configCode + "$mware$index", "主页");
		var me = this;
		Sys.msg.sub(configCode + "$mware$edit@success", function(message) {
			me.loadDataGrid();
		});
		Sys.msg.sub(configCode + "$mware$import@success", function(message) { // 上传返回
			me.loadDataGrid();
		});
		Sys.msg.sub(configCode + "$mware$closeIframe@success", function(jsonBean) { // 上传返回
			closeIframe(jsonBean.data);
		});
		
		me.dealType = null;
		me.dealDescribeTitle = null;
		me.initCustomListen();
		
		me.dealDescribeCode = "PC002 ZZ001 ZZ002 ZZ003 QT001";

	},
	
	initCustomListen : function(){
		var me = this;
		me.dealBtn().bind('click',function(){
			me.collisionDeal(me.dealType,me.dealDescribeTitle);
		});
		
		me.closeBtn().bind('click',function(){
			me.dealWin().window('close');
		});
	},
	
	/**
	 * 渲染列表
	 */
	initDirInfoData : function(){
		var me = this;
		me.middleware = new Middleware();
		me.middleware.load({
			configCode : me.configCode,
			searchForm : "#searchForm"
		});
		
		// 加载列表数据
		var columns = me.middleware.getGridColumns();
		var url = CmsAction.dirInfoDataList;
		if(window.context != null && K.isObject(window.context) && K.isNotBlank(window.context.listAction)) {
			url = window.context.listAction;
		}
		
		var newColumns = [];
		$.each(columns,function(i,column){
			if(i == 0){
				newColumns.push(column);
			}
		});
		newColumns.push({
			field : 'DCD_YSBZ',
			formatter : function(value,row,index){
				var bgColor = row.DCD_YSBZ;
				var src = "";
				if (bgColor == '001'){
					src = $appStaticsPath + '/images/red_mark.png';
				}
				if (bgColor == '002'){
					src = $appStaticsPath + '/images/yellow_mark.png';
				}
				if (bgColor == '003' || K.isBlank(bgColor)){
					src = $appStaticsPath + '/images/green_mark.png';
				}
				return "<img style='width:24px;height:24px;' src='"+src+"'>";
			},
			width:30
		});
		
		$.each(columns,function(i,column){
			if(i > 0){
				newColumns.push(column);
			}
		});
		
		me.dirInfoGrid().datagrid({
			idField : "ID",
			url : url,
			toolbar : me.getGridToolbar(),
			fitColumns : false,
			rownumbers : true,
			nowrap : false,
			selectOnCheck : false,
			singleSelect : true,
			remoteSort : true,
			columns : [ newColumns ],
			onBeforeLoad : function(param) {
				$.extend(param, {
					configCode : me.configCode
					//sort : 'DCD_YSBZ,DCD_LQXSYTS',
					//order : 'ASC,DESC'
				});
				if(param.sort == null){
					$.extend(param, {
						sort : 'DCD_YSBZ,DCD_LQXSYTS',
						order : 'ASC,DESC'
					});
				}
				var flag = me.onBeforeLoadData(param);
				me.dirInfoGrid().datagrid("uncheckAll");
				me.dirInfoGrid().datagrid("unselectAll");
				return flag != false;
			},
			onLoadSuccess : function(data) {
				if(data.success) {
					if(typeof (onLoadGridSuccess) != 'undefined') {
						onLoadGridSuccess($(this), data);
					}
				} else {
					K.msg.mini.error(data.message);
				}
			}
		});
	},
	
	taskView : function(menu, rightMenu){
		var rows = this.dirInfoGrid().datagrid('getChecked');
		if (rows.length == 0) {
			alert("请选择记录数据");
			return null;
		}
		if (rows.length > 1) {
			alert("请选择一条记录数据");
			return null;
		}
		
		K.window.iframewindow({
            title : "任务流程日志",
           // href : $appPath + "/dna/collision/taskView.do",
            href : DnaAction.gotoLifeCourse,
            id : "taskViewJsp",
            width : document.documentElement.clientWidth * 0.99,
            height : document.documentElement.clientHeight * 0.8,
            params : {
            	ywId : rows[0].ID,
            	configCode : "YW_DCD_DGSGAJ_DNARWX",
            	pzbh : rows[0].DCD_PZBH,
                personID : rows[0].DCD_SFZH,
                name : rows[0].DCD_XM,
                caseCode : rows[0].DCD_AJBH
            },
            modal : true,
            maximizable : true,
            maximized : false,
            closable : true,
            minimizable : false,
            resizable : false,
            collapsible : false
        });
	},
	
	
	showDealWin : function(menu, rightMenu, dealStatue){
		var me = this;
		var rows = this.dirInfoGrid().datagrid('getChecked');
		if (rows.length == 0) {
			alert("请选择记录数据");
			return null;
		}
		if (rows.length > 1) {
			alert("请选择一条记录数据");
			return null;
		}
		
		var title = "";
		var tagName = "";
		var dealCategory = "";
		var dealName = "";
		var dealDescribeTitle = "";
		var trHtml = me.describe().parents('tr');
		if('1' == dealStatue){
			tagName = "排他";
			dealCategory = "PC";
			dealName = "排他选项：";
			dealDescribeTitle = '排除简要理由';
			$(trHtml).attr("disabled",false); 
			$(trHtml).show();
		}
		if('2' == dealStatue){
			tagName = "推送";
			dealDescribeTitle = "";
			dealCategory = "";
			me.dealDescribe().text('');
		}
		if('3' == dealStatue){
			tagName = "办结";
			dealCategory = "BJ";
			dealName = "办结选项：";
			dealDescribeTitle = '办结简要理由';
			$(trHtml).attr("disabled",true); 
			$(trHtml).hide();
		}
		if('4' == dealStatue){
			tagName = "在侦";
			dealCategory = "ZZ";
			dealName = "在侦选项：";
			dealDescribeTitle = '已掌握情况';
			$(trHtml).attr("disabled",false); 
			$(trHtml).show();
		}
		if('44' == dealStatue){
			tagName = "在侦";
			dealCategory = "ZZ003";
			dealName = "在侦选项：";
			dealStatue = "4";
			dealDescribeTitle = '已掌握情况';
			$(trHtml).attr("disabled",false); 
			$(trHtml).show();
		}
		
		me.dealOption().html('');
		if(K.isNotBlank(dealCategory)){
			me.dealName().text(dealName);
			var dealOptions = me.getDealOPtions(dealCategory);
			var optionHtml = "";
			
			$.each(dealOptions,function(i,dict){
				var name = dict.name;
				var customCode = dict.customCode;
				optionHtml += "<input type='radio' name='dealVal' value='"+customCode+"' id='"+customCode+"' onclick=\"showDescribe('"+customCode+"')\"><label id='"+customCode+"_label' for='"+customCode+"'>"+name+"</label><br><br>";
			});
			me.dealDescribeTitle = dealDescribeTitle;
			me.dealOption().append(optionHtml);
			me.dealDescribe().text(dealDescribeTitle+"：");
		}
		
		var radios =$("input[name=dealVal]");
		$.each(radios,function(i,radio){
			if(i == 0){
				$(radio).click();
			}
		});
		
		title +=tagName + "处理";
		me.dealWin().show();
		
		me.dealWin().window({
			title : title,
            modal:true,
            minimizable : false,
            maximizable : false,
            closable : true,
            collapsible : false,
            resizable : false
		});
		
		me.dealType = dealStatue;
		me.dealBtn().val(tagName);
	},
	
	collisionDeal : function(dealStatue,dealDescribeTitle){
		var me = this;
		var width = $("body").width();
		var height = $("body").height();
		var tagName = "";
		if('1' == dealStatue){
			tagName = "排他";
		}
		if('2' == dealStatue){
			tagName = "推送";
		}
		if('3' == dealStatue){
			tagName = "办结";
		}
		if('4' == dealStatue){
			tagName = "在侦";
		}
		var ids = [];
		var rows = this.dirInfoGrid().datagrid('getChecked');
		$.each(rows, function(i, v) {
			ids.push(v.ID);
		});
		
		if (K.isBlank(ids.join(',')) || ids.length != 1) {
			alert("请选择1条数据");
			return;
		}
		var describe = me.describe().val();
		var dealDescribeTile = me.dealDescribe().text();
		var dealCode = $("input[name=dealVal]:checked").val();
		var describeCn = $("#"+dealCode+"_label").text();
		
		
		
		if(dealStatue != '2' && K.isBlank(describe) && me.dealDescribeCode.indexOf(dealCode) > -1){
			K.msg.mini.error(dealDescribeTitle + "不能为空");
			return;
		}
		
		if(me.dealDescribeCode.indexOf(dealCode) > -1){
			describe = describeCn + "（" + dealDescribeTile + describe+"）";
		}else{
			describe = describeCn;
		}
		
		var Url = DnaAction.collisionDeal;
		//确认推送
		if(dealStatue == '2'){
			K.window.iframewindow({
	            title : "确认推送",
	            href : $appPath + "/zygl/mwareCms/updateDirInfoData.do",
	            id : "pushWindow",
	            width : width * 8/10,
	            height : height * 8/10,
	            params : {
	            	id : ids.join(","),
	            	configCode : me.configCode,
	            	isPush : true
	            },
	            modal : true,
	            maximizable : true,
	            maximized : false,
	            closable : true,
	            minimizable : false,
	            resizable : false,
	            collapsible : false
	        });
			/*if (confirm("确认要" + tagName + "记录吗？")) {
				K.ajax({
					mask : true,
					dataType : 'json',
					url : Url,
					data : {
						configCode : me.configCode,
						ID : ids.join(','),
						type : dealStatue
					},
					success : function(jsonBean) {
						if(jsonBean.success){
							K.msg.mini(jsonBean.message);
							me.describe().val('');
							me.loadDataGrid();
						}else{
							K.msg.mini.error(jsonBean.message);
						}
					},
					error : function(jsonBean) {
						alert(tagName + "失败");
						me.loadDataGrid();
					}
				});
			}*/
		}else{
			//排他，办结，在侦保存
			K.ajax({
				mask : true,
				dataType : 'json',
				url : Url,
				data : {
					configCode : me.configCode,
					ID : ids.join(','),
					type : dealStatue,
					dealCode : dealCode,
					describe : describe
				},
				success : function(jsonBean) {
					if(jsonBean.success){
						K.msg.mini(jsonBean.message);
						me.dealWin().window('close');
						me.describe().val('');
						me.loadDataGrid();
					}else{
						K.msg.mini.error(jsonBean.message);
					}
					//alert(menu.name + "成功");
				},
				error : function(jsonBean) {
					alert(tagName + "失败");
					me.loadDataGrid();
				}
			});
		}
	},
	//关闭推送窗口
	closePushWindow : function(message){
		alert(message);
		$("#pushWindow").window("close");		
	},
	
	getDealOPtions : function(dealCategory){
		var me = this;
		var rDicts = null;
		$.ajax({
			url : DnaAction.getDealOPtions,
			data : {
				dealCategory : dealCategory
			},
			dataType : 'json',
			type : 'post',
			async : false,
			success : function(dicts){
				rDicts = dicts;
			}
		});
		return rDicts;
	},
	
	showDescribe : function(customCode){
		var me = this;
		var trHtml = me.describe().parents('tr');
		if(me.dealDescribeCode.indexOf(customCode) == -1){
			$(trHtml).attr("disabled",true); 
			$(trHtml).hide();
		}else{
			$(trHtml).attr("disabled",false); 
			$(trHtml).show();
		}
	},
	
	manualIssue : function(){
		var me = this;
		var rows = me.dirInfoGrid().datagrid('getChecked');
		if(rows.length == 0){
			alert("请选择需要下发的数据");
			return;
		}
		var dataIds = [];
		$.each(rows,function(i,row){
			dataIds.push(row.ID);
		});
		me.showOrgWin(dataIds.join(","));
	},
	
	showOrgWin : function(dataIds){
		var me = this;
		me.orgWin().show();
		me.orgWin().window({
	        title : '下发分局-支持双击选中',
	        width: 600, 
	        height: 500,    
	        modal:false,
	        minimizable : false,
	        maximizable : false,
	        collapsible : false
	    });
		
		me.orgGrid().datagrid({
			idField : 'id',
			selectOnCheck : false,
			singleSelect : true,
			pagination : false,
			onDblClickCell: function(index,field,value){
				var id = $(this).datagrid('getSelected').id;
				me.saveManualIssue(dataIds,id);
			},
			url : DnaAction.getOrgList,
			toolbar : [ {
				id : "tbtn-search",
				text : "查询",
				iconCls : "u-icon-search",
				handler : function() {
					me.orgGrid().datagrid("reload", $("#org_search_Form").serializeArray());
				}
			}, {
				id : "tbtn-sure",
				text : "选择",
				iconCls : "u-icon-add",
				handler : function() {
					var rows = me.orgGrid().datagrid('getRows');
					var scope = me.orgGrid().datagrid("getPanel");
					var ckb;
					var falg = false;
					var orgIds = "";
					for (var i = 0; i < rows.length; i++) {
						var row = rows[i];
						ckb = $("input[type='checkbox']", scope)[i + 1];
						if(ckb.checked){
							if(orgIds == ""){
								orgIds = row.id;
							}else{
								orgIds = orgIds + ',' + row.id;
							}
							falg=true;
						}
					}
					if(!falg){
						alert("请选择分局");
						return false;
					}
					me.saveManualIssue(dataIds,orgIds);
				}
			} ],
			columns : [ [ {
				field : 'ckId',
				checkbox : true
			}, {
				field : 'name',
				title : '分局名称',
				align : 'center',
				width : 120
			}, {
				field : 'organizationCode',
				title : '分局机构编码',
				algin : 'center',
				width : 120
			} ] ],
			onCheck : function(rowIndex, rowData){
				var scope = me.orgGrid().datagrid("getPanel");
				var rowIndex = rowIndex + 1;
				$("input[type='checkbox']", scope).each(function(){
					$(this).attr("checked",false);
				}); 
				ckb = $("input[type='checkbox']", scope)[rowIndex];
				ckb.checked=true;
			},
			onBeforeLoad : function(param) {
				
			}
			/*onLoadSuccess : function(data) {
				var rows = data.rows;
				var roleCodes = parent.getRoleCode();	
				me.orgGrid().datagrid("uncheckAll");
				var scope = me.orgGrid().datagrid("getPanel");
				var  ckb;
				for (var i = 0; i < rows.length; i++) {
					var row = rows[i];
					ckb = $("input[type='checkbox']", scope)[i + 1];
					if(roleCodes !="" ||roleCodes != null){
						var s=roleCodes.split(",");
						for (var int = 0; int < s.length; int++) {
							if(s[int] == row.roleCode){
								ckb.checked = true;
							}
						}
					}
				}
			}*/
		});
	},
	
	saveManualIssue : function(ids,orgId){
		var me = this;
		$.ajax({
            url : DnaAction.manualIssue,
            data : {
            	ids : ids,
            	orgId : orgId
            },
            type : 'post',
            dataType : 'json',
            success : function(jsonBean){
                if(jsonBean.success){
                    me.orgWin().hide();
                    me.orgWin().window('close');
                    K.msg.mini(jsonBean.message);
                    me.loadDataGrid();
                }else{
                    K.msg.mini.error(jsonBean.message);
                }
            }
        });
	},
	
	getLinkCaseInfo : function(){
		var me = this;
		var rows = me.dirInfoGrid().datagrid("getChecked");
		if(rows.length != 1){
			K.msg.mini.error("请选择一条数据");
			return;
		}
		var row = rows[0];
		
		var width = $("body").width();
		var height = $("body").height();
		K.window.iframewindow({
            title : "相关案件",
            href : DnaAction.gotoCaseInfoListPage,
            id : "caseInfoListPage",
            width : width * 9/10 ,
            height : height * 9/10,
            params : {
            	idCard : row.DCD_SFZH
            },
            modal : true,
            maximizable : false,
            maximized : false,
            closable : true,
            minimizable : false,
            resizable : false,
            collapsible : false
        });
	},
	
	viewCaseInfo : function(ajbh){
		var url = DnaAction.gotoViewDirDataPage;
		var width = $("body").width();
		var height = $("body").height();
		K.window.iframewindow({
            title : "案件信息",
            href : url,
            id : "caseInfoViewPage",
            width : width * 9/10 ,
            height : height * 9/10,
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
	},
	
	
	dealWin : function(){
		return $("#dealWin");
	},
	
	dealBtn : function(){
		return $("#dealBtn");
	},
	
	closeBtn : function(){
		return $("#closeBtn");
	},
	
	describe : function(){
		return $("#describe");
	},
	
	orgWin : function(){
		return $("#orgWin");
	},
	
	orgName : function(){
		return $("#orgName");
	},
	
	orgCode : function(){
		return $("#orgCode");
	},
	
	orgGrid : function(){
		return $("#orgGrid");
	},
	
	sureOrgBtn : function(){
		return $("#sureOrgBtn");
	},
	
	closeOrgBtn : function(){
		return $("#closeOrgBtn");
	},
	
	dealDescribe : function(){
		return $("#dealDescribe");
	},
	
	dealOption : function(){
		return $("#dealOption");
	},
	
	dealName : function(){
		return $("#dealName");
	}
	
}

$.extend(AbstractDataIndex, DataIndex);
DataIndex = Class.create(AbstractDataIndex, AbstractDataIndex);