Ext.define('XIRJS.widget.button.ExportButton',{
	extend : 'Ext.button.Button',
	text : '导出',
	icon :'images/icon/export.png',
	config:{
		exportUrl: null,
	},
	requires : ['XIRJS.widget.CommonUtil'],
    	config:{
    		exportUrl: null,
    	},
    	exportAction: function(parameter){
    		this.setDisabled(true);

    		var title = parameter.title;
    		var url = this.getExportUrl();
    		var scope = this;
    		var sucFuc = function(response, opts) {
    			var rsp = Ext.JSON.decode(response.responseText);
    			if(rsp.success && rsp.msg.message){
    				this.downLoadFile(title, rsp.msg.message);
    			}else{
    				XIRJS.widget.CommonUtil.Error(rsp.msg.message);
    			}
    			this.setDisabled(false);
    		};
    		var failFn = function(){
    			this.setDisabled(false);
    			console.log('导出失败', arguments);
    		};
    		XIRJS.widget.CommonUtil.AjaxRequest(url, parameter, scope, sucFuc, null, true);
    	},

    	downLoadFile: function(title, downLoadFileName){
    		var parameter = {
    				title: title,
    				downLoadFileName : downLoadFileName,
    				_dc : new Date().getTime()
    		};

    		var url = 'customquery/downLoadFile.action' + '?' + Ext.urlEncode(parameter);

    		if (Ext.isIE6 || Ext.isIE7 || Ext.isSafari || Ext.isSafari2 || Ext.isSafari3) {
    	        Ext.getBody().insertHtml('afterEnd',
    					'<iframe frameborder="0" width="0" location="' + url + '" />');
    	    } else {
    	        Ext.getBody().insertHtml('afterEnd',
    					'<iframe frameborder="0" width="0" src="' + url + '" />');
    	    }
    	}
});