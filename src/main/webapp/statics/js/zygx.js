var zygx = {};

zygx.qqFileUploadTemplate = ''//
		+ '<div class="qq-uploader u-uploader">' //
		+ '  <pre class="qq-upload-drop-area"><span>{dragZoneText}</span></pre>'
		+ '  <div class="u-qq-upload-button"><input type="button" value="{uploadButtonText}" style="width:40px;"/></div>' //
		+ '  <span class="qq-drop-processing"><span>{dropProcessingText}</span>' //
		+ '  <span class="qq-drop-processing-spinner"></span></span>' // 
		+ '  <ul class="qq-upload-list" style="margin-top: 10px; text-align: center;"></ul>' //
		+ '<div>';

zygx.getFileUploadQQUploadOpts = function() {
	return {
		template : zygx.qqFileUploadTemplate,
		text : {
			uploadButton : "上传",
			dragZone : "这拖放在这儿"
		},
		debug : true,
		classes : {
			button : "u-qq-upload-button",
			buttonHover : "",
			buttonFocus : ""
		},
		validation : {
			allowedExtensions : [ 'doc', 'docx' ],
			minSizeLimit : 1,
			sizeLimit : 2048000
		},
		request : {
			inputName : "zygxFile",
			endpoint : $appPath + '/upload/uploadFile.do'
		}
	};
};

