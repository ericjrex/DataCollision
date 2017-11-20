<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script type="text/javascript" src="${platformStaticsPath}/component/orgtree/OrgTree.js"></script>
<script type="text/javascript">

	var singleOrgTree = null, multiOrgTree = null;
	$(function() {
		// 单选树
		singleOrgTree = new OrgTree({
			title : "机构",
			single : true,
			scope : OrgTree.prototype.scope.ALL
		});

		// 多选树
		multiOrgTree = new OrgTree({
			title : "机构",
			single : false,
			scope : OrgTree.prototype.scope.ALL
		});
	});
	
	// 展现单选树
	function showSingleOrgTree(fieldName){
		singleOrgTree.opts.onAfterConfirm = function(nodes) {
			if (!nodes) {
				return;
			} else {
				var node = nodes[0];
				$("#" + fieldName + "_orgId").val(node.id);
				$("input[name='" + fieldName + "']").val(node.text);
				// js脚本中的自定义函数
				if(typeof (singleFunction) != 'undefined'){
					singleFunction(node);
				}
			}
		};
		singleOrgTree.setOrgId($("#" + fieldName + "_orgId").val());
		singleOrgTree.show();
	};
	
	// 展现多选树
	function showMultiOrgTree(fieldName){
		multiOrgTree.opts.onAfterConfirm = function(nodes) {
			var orgIds = [], orgNames = [];
			if (!nodes) {
				return;
			} else {
				for ( var i = 0; i < nodes.length; i++) {
					orgIds.push(nodes[i].id);
					orgNames.push(nodes[i].text);
				}
				$("#" + fieldName + "_orgId").val(orgIds.join(","));
				$("input[name='" + fieldName + "']").val(orgNames.join(","));
				// js脚本中的自定义函数
				if(typeof (multiFunction) != 'undefined'){
					multiFunction(nodes);
				}
			}
		};
		multiOrgTree.setOrgId($("#" + fieldName + "_orgId").val());
		multiOrgTree.show();
	};
</script>