var LifeCourseIndex = {

	initialize : function(pzbh,configCode) {
		Sys.msg.initCurPage("LifeCourseIndex" + "$index", "主页");
		var me = this;
		me.pzbh = pzbh;
		me.configCode = configCode;
		me.initDirInfoData();
	},

	initDirInfoData : function() {
		var me = this;
		var lifeData = [];
		var linksData = [];
		me.getData(lifeData,linksData);
		
		me.myChart = echarts.init(document.getElementById('main'), 'shine');

		option = {
			"color" : [ "#ff6666", "#ffe966", "#92ff66", "#66ffbd", "#66bdff",
					"#9266ff", "#ff66e9" ],
			title : {
				text : '碰撞人员信息生命历程'
			},
			tooltip : {},
			animationDurationUpdate : 1500,
			animationEasingUpdate : 'quinticInOut',
			toolbox: {
		        show : true,
		        feature : {
		            mark : {show: true},
		           // dataView : {show: true, readOnly: false},
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
			series : [ {
				type : 'graph',
				layout : 'none',
				focusNodeAdjacency : false,
				legendHoverLink: true,
				symbol : 'roundRect',
				symbolSize : 130,
				roam : true,
				label : {
					normal : {
						show : true
					}
				},
				edgeSymbol : [ 'circle', 'arrow' ],
				edgeSymbolSize : [ 4, 15 ],
				edgeLabel : {
					normal : {
						textStyle : {
							fontSize : 20
						}
					}
				},
				data : lifeData,
				links : linksData,
				lineStyle : {
					normal : {
						color : '#10a0a2',
						opacity : 0.9,
						width : 2,
						curveness : 0
					}
				}
			} ]
		};
		// 为echarts对象加载数据
		me.myChart.setOption(option);
	},
	
	getData : function(lifeData,linksData){
		var me = this;
		var life = null;
		var line = null;
		var lastLife = null;
		$.ajax({
            url : DnaAction.getLifeCourseData,
            data : {
            	configCode : me.configCode,
            	pzbh : me.pzbh,
            	order : 'asc',
            	sort : 'DCD_RWZXSJ'
            },
            type : 'post',
            dataType : 'json',
            async : false,
            success : function(jsonBean){
                if(jsonBean.success){
                	var datas = jsonBean.data;
                	var color = null;
                	$.each(datas,function(i,data){
                		if(i % 2 ==0){
                			color = "#8DB6CD";
                		}else{
                			color = "#66bdff";
                		}
                		var statue = data.DCD_RWXXZT;
                		var bgnr = data.DCD_BGNR;
                		var czr = data.DCD_CZR;
                		var time = data.DCD_RWZXSJ;
                		bgnr = me.formatBgnr(bgnr);
            			life = {
                 		   name : statue + "\n" +time,
            			   //name : statue,
                 		   x : i ,
                 		   y : 0.0,
                 		   //category : "工业时代",
                 		   itemStyle : {
 								normal : {
 									color : color
 								}
 							},
 							tooltip : {
 								formatter : ""+statue+"<br/>" +
 										"处理时间："+time+"<br/>"+
 										"处理人："+czr+"<br/>"+
 										"变更内容：<br/>"+
 										bgnr+"<br/>"
 							}	
                     	};
                		lifeData.push(life);
                		if(lastLife != null){
                			line = {
                				"source" : i - 1,
            					"target" : i
                        	};
                			linksData.push(line);
                		}
                		lastLife = life;
                	});
                }else{
                    K.msg.mini.error(jsonBean.message);
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

LifeCourseIndex = Class.create(LifeCourseIndex);