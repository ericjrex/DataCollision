/**
 * 后端生成页面的js
 */
var DataIndex = {
	
	initialize : function(configCode ,mianID) {
		if(K.isBlank(configCode)) {
			alert("表单个性化配置编码为空！");
			return;
		}
		this.configCode = configCode;
		this.mianID = mianID;
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
		
		
		var bgnrItem = me.middleware.getDirItem("bgnr");
		bgnrItem._align = "left";
		var functionArr = [{
			fieldName : bgnrItem.fieldName,
			formatter : function(value){
				return me.formatBgnr(value);
			}
		}]
		
		// 加载列表数据
		var columns = me.middleware.getGridColumns(functionArr);
		var url = CmsAction.dirInfoDataList;
		if(window.context != null && K.isObject(window.context) && K.isNotBlank(window.context.listAction)) {
			url = window.context.listAction;
		}

		var newColumns = [];
		$.each(columns,function(i,column){
			if(i != 0){
				newColumns.push(column);
			}
		});

		
		me.dirInfoGrid().datagrid({
			idField : "ID",
			url : url,
			fitColumns : false,
			rownumbers : false,
			nowrap : false,
			selectOnCheck : false,
			singleSelect : true,
			remoteSort : true,
			columns : [ columns ],
			onBeforeLoad : function(param) {
				$.extend(param, {
					configCode : me.configCode,
					customSQL : "DCD_ZRWID = '" + mainID + "'",
					sort : "DCD_RWZXSJ",
					order : "DESC"
				});
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
	
	formatBgnr : function(bgnr) {
		var bgnrName = "";
		if(K.isNotBlank(bgnr)) {
			var bgnrArr = [];
			$.each(bgnr.split("|"), function(i, bgnr) {
				bgnrArr.push("（" + (i + 1) + "） " + bgnr + "。");
			});
			bgnrName = bgnrArr.join("</br>");
		}
		return bgnrName;
	}
}

$.extend(AbstractDataIndex, DataIndex);
DataIndex = Class.create(AbstractDataIndex, AbstractDataIndex);