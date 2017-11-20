(function() {
	// {{字典配置
	var dictConfig = {
		dirInfoStatus : { // 目录状态
			name : "目录状态",
			code : "013025",
			items : {
				caogao : "013025000", // 草稿
				yifabu : "013025001", // 已发布
				yichexiao : "013025002", // 已撤销
				yitingyong : "013025003", // 已停用
				shenhezhong : "013025004", // 审核中
				shenhetongguodaifabu : "013010005" // 审核通过待发布
			},
			itemNames : {
				caogao : "草稿",
				yifabu : "已发布",
				yichexiao : "已撤销",
				yitingyong : "已停用",
				shenhezhong : "目录审核中"
			}
		},
		dirInfoPublicScope : { // 目录公开范围
			name : "目录公开范围",
			code : "013001",
			items : {
				duiwaigongkai : "013001000", // 全部对外公开
				duineigongkai : "013001001", // 全部对内公开
				bugongkai : "013001002", // 不公开
				bufengongkai : "013001003" // 部分公开
			},
			itemNames : {
				duiwaigongkai : "全部对外公开",
				duineigongkai : "全部对内公开",
				bugongkai : "不公开",
				bufengongkai : "部分公开"
			}
		},
		dirItemPublicScope : {
			name : "目录信息项公开范围",
			code : "013027",
			items : {
				duiwaigongkai : "013027000",
				duineigongkai : "013027001",
				bugongkai : "013027002",
				dingxianggongkai : "013027003"
			},
			itemNames : {
				duiwaigongkai : "对外公开",
				duineigongkai : "对内公开",
				bugongkai : "不公开",
				dingxianggongkai : "定向公开"
			}
		}
	};
	// 字典配置}}

	function Dict(opts) {
		var me = this;
		this.opts = opts;
		this.code = opts.code;
		this.items = opts.items || {};
		this.itemsArray = function() {
			var items = me.items;
			var arr = [];
			for ( var key in items) {
				arr.push(items[key]);
			}
			return arr;
		};
	}

	var zygxDict = {};
	for ( var key in dictConfig) {
		zygxDict[key] = new Dict(dictConfig[key]);
	}

	window.ZygxDict = zygxDict;
})();
