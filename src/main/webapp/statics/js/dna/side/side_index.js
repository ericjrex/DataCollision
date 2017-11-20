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
			columns : [ columns ],
			onBeforeLoad : function(param) {
				$.extend(param, {
					configCode : me.configCode
				});
				var flag = me.onBeforeLoadData(param);
				me.dirInfoGrid().datagrid("uncheckAll");
				return flag != false;
			},
			onLoadSuccess : function(data) {
				if(data.success == undefined || data.success == null || data.success) {
					if(typeof (onLoadGridSuccess) != 'undefined') {
						onLoadGridSuccess($(this), data);
					}
				} else {
					K.msg.mini.error(data.message);
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
            	idCard : row.XZD_SFZ
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
	}
	
}

$.extend(AbstractDataIndex, DataIndex);
DataIndex = Class.create(AbstractDataIndex, AbstractDataIndex);