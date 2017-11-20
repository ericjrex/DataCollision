$.extend($.fn.datagrid.methods, {

	/**
	 * 开打提示功能
	 */
	doCellTip : function(jq, params) {
		function showTip(showParams, td, dg) {
			if ($(td).text() == "")
				return;

			params = params || {};
			showParams.content = '<div class="tipcontent">' + showParams.content + '</div>';

			$(td).tooltip({
				content : showParams.content,
				trackMouse : true,
				position : params.position,
				onHide : function() {
					$(this).tooltip('destroy');
				},
				onShow : function() {
					var tip = $(this).tooltip('tip');
					if (showParams.tipStyler) {
						tip.css(showParams.tipStyler);
					}
					if (showParams.contentStyler) {
						tip.find('div.tipcontent').css(showParams.contentStyler);
					}
				}
			}).tooltip('show');
		}

		function cellMouseOver(grid, me) {
			var options = grid.data('datagrid');
			options.factContent = me.find('>div').clone().css({
				'margin-left' : '-5000px',
				'width' : 'auto',
				'display' : 'inline',
				'position' : 'absolute'
			}).appendTo('body');

			var factContentWidth = options.factContent.width();
			params.content = me.text();
			if (params.onlyShowInterrupt) {
				if (factContentWidth > me.width()) {
					showTip(params, me, grid);
				}
			} else {
				showTip(params, me, grid);
			}
		}

		function cellMouseOut(grid, me) {

			var options = grid.data('datagrid');

			if (options.factContent) {
				options.factContent.remove();
				options.factContent = null;
			}
		}

		return jq.each(function() {
			var grid = $(this);
			var options = $(this).data('datagrid');
			if (!options.tooltip) {
				var panel = grid.datagrid('getPanel').panel('panel');

				var dbs = panel.find('.datagrid-body');
				$.each(dbs, function() {

					var delegateEle = $(this).find('> div.datagrid-body-inner').length ? $(this).find('> div.datagrid-body-inner')[0]
							: this;
					$(delegateEle).undelegate('td', 'mouseover');
					$(delegateEle).undelegate('td', 'mouseout');
					$(delegateEle).undelegate('td', 'mousemove');

					$(delegateEle).delegate('td[field]', {
						'mouseover' : function(e) {
							cellMouseOver(grid, $(this));
						},
						'mouseout' : function(e) {
							cellMouseOut(grid, $(this));
						}
					});
				});
			}
		});
	},

	/**
	 * 关闭消息提示功能
	 */
	cancelCellTip : function(jq) {
		return jq.each(function() {
			var data = $(this).data('datagrid');
			if (data.factContent) {
				data.factContent.remove();
				data.factContent = null;
			}
			var panel = $(this).datagrid('getPanel').panel('panel');
			panel.find('.datagrid-body').undelegate('td', 'mouseover').undelegate('td', 'mouseout').undelegate('td', 'mousemove');
		});
	},

	/**
	 * 列头提示功能
	 */
	doTitleTip : function(jq, params) {

		function showTip(showParams, td, dg) {
			if ($(td).text() == "")
				return;
			params = params || {};

			showParams.content = '<div class="tipcontent">' + showParams.content + '</div>';
			$(td).tooltip({
				content : showParams.content,
				trackMouse : true,
				position : params.position,
				onHide : function() {
					$(this).tooltip('destroy');
				},
				onShow : function() {
					var tip = $(this).tooltip('tip');
					if (showParams.tipStyler) {
						tip.css(showParams.tipStyler);
					}
					if (showParams.contentStyler) {
						tip.find('div.tipcontent').css(showParams.contentStyler);
					}
				}
			}).tooltip('show');
		};

		function titleMouseOver(grid, me) {
			var options = grid.data('datagrid');
			options.factContent = me.find('>div').clone().css({
				'margin-left' : '-5000px',
				'width' : 'auto',
				'display' : 'inline',
				'position' : 'absolute'
			}).appendTo('body');

			var factContentWidth = options.factContent.width();
			var el = $("th[field=" + me.attr('field') + "]", grid)[0];
			if(el == null || el.length <= 0 ){
				return;
			}
			var content = $(el).attr('itemRemark');// me.text();
			if( content == null || content === '' ){
				return;
			}
			params.content = content;
			if (params.onlyShowInterrupt) {
				if (factContentWidth > me.width()) {
					showTip(params, me, grid);
				}
			} else {
				showTip(params, me, grid);
			}
		};

		function titleMouseOut(grid, me) {
			var options = grid.data('datagrid');
			if (options.factContent) {
				options.factContent.remove();
				options.factContent = null;
			}
		};

		return jq.each(function() {

			var grid = $(this);
			var options = $(this).data('datagrid');

			if (!options.tooltip) {
				var panel = grid.datagrid('getPanel').panel('panel');

				var dbs = panel.find('.datagrid-header');
				$.each(dbs, function() {
					var delegateEle = $(this).find('> div.datagrid-header-inner').length ? $(this).find('> div.datagrid-header-inner')[0]
							: this;
					$(delegateEle).undelegate('td', 'mouseover');
					$(delegateEle).undelegate('td', 'mouseout');
					$(delegateEle).undelegate('td', 'mousemove');

					$(delegateEle).delegate('td[field]', {
						'mouseover' : function(e) {
							titleMouseOver(grid, $(this));
						},
						'mouseout' : function(e) {
							titleMouseOut(grid, $(this));
						}
					});
				});
			}
		});
	}
});
