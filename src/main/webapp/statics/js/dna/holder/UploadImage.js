"use strict";
(function(mod) {
    if (typeof exports == "object" && typeof module == "object") // CommonJS
        module.exports = mod();
    else if (typeof define == "function" && define.amd) // AMD
        return define([], mod);
    else // Plain browser env
        window.UploadImage = mod();
})(function () {


    //public
    function UploadImage(id, url, key) {
        this.element = document.getElementById(id);
        this.url = url; //��˴���ͼƬ��·��
        this.imgKey = key || "AreaImgKey"; //�ᵽ����˵�name

    }
    UploadImage.prototype.paste = function (callback, formData) {
        var thatthat = this;
        
        this.element.addEventListener('paste', function (e) {//����Ŀ��������id����paste�¼�
        	if(e.clipboardData.files.length > 0){
        		 if (e.clipboardData && e.clipboardData.items[0].type.indexOf('image') > -1) {
                     var that = this,
                     file = e.clipboardData.items[0].getAsFile();//��ȡe.clipboardData�е����
                     dataReader(file,function (e) { //reader��ȡ��ɺ�xhr�ϴ�
                         var fd = formData || (new FormData());
                         fd.append(thatthat.imgKey, this.result); // this.result�õ�ͼƬ��base64

                         xhRequest('POST',thatthat.url,fd,callback,that);

                     });
                 }else{
                  	K.msg.mini.error("请上传图片类型文件");
                 	return false;
                }
        		 
        	}
        }, false);

    };

    UploadImage.prototype.drag=function(callback,formData)
    {
        var that = this;
        this.element.addEventListener('drop', function (e) {//����Ŀ��������id����drop�¼�
            e.preventDefault(); //ȡ��Ĭ���������קЧ��
            var fileList = e.dataTransfer.files; //��ȡ�ļ�����
            //����Ƿ�����ק�ļ���ҳ��Ĳ���
            if(fileList.length == 0){
                return;
            }
            //����ļ��ǲ���ͼƬ
            if(fileList[0].type.indexOf('application/vnd.openxmlformats-officedocument.spreadsheetml.sheet') == -1 && fileList[0].type.indexOf('application/vnd.ms-excel') == -1){
                //console.log&&console.log("���ϵĲ���ͼƬ��");
                K.msg.mini.error("请上传excel类型文件");
                return false;
            }
            K.mask();
            var fd = formData || (new FormData());
            $.each(fileList,function(i,imfile){
            	fd.append(that.imgKey+i, imfile); //
            });
            fd.append('importHKey', importHKey);
           // fd.append(that.imgKey, fileList[0]);
            xhRequest('POST',that.url,fd,callback,this);
            K.unmask();
        }, false);
    };

    UploadImage.prototype.upload=function(callback,formData)
    {
        this.drag(callback,formData);
        this.paste(callback,formData);
    };

    preventDragDefault();
    //private

    function xhRequest(method,url,formData,callback,callbackContext)
    {
    	var me =this;
        var xhr=new XMLHttpRequest();
        $('#uploadInfo').html('');
        $('#upload_excel_data_window').window('open');
        xhr.open(method,url,true);
        xhr.onload=function()
        {
            callback&&callback.call(callbackContext||this,xhr);
        };
        xhr.send(formData||(new FormData()));
        $('#progressbar').progressbar('setValue', 0);
		importTimer = setInterval(dataInport.updateProgressbar.bind(dataInport), 3000);
    }

    function preventDragDefault()//��ֹ�����Ĭ�Ͻ�ͼƬ�򿪵���Ϊ
    {
        document.addEventListener("dragleave",preventDefault);//����
        document.addEventListener("drop",preventDefault);//�Ϻ��
        document.addEventListener("dragenter",preventDefault);//�Ͻ�
        document.addEventListener("dragover",preventDefault);//������ȥ
    }

    function preventDefault(e){
        e.preventDefault();
    }

    function dataReader(file,callback)
    {
        var  reader = new FileReader();
        reader.onload =callback;
        reader.readAsDataURL(file);//��ȡbase64����
    }
    return UploadImage;
});