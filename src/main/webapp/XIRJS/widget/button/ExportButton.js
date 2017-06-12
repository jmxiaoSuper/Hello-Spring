Ext.define('XIRJS.widget.button.ExportButton',{
	extend : 'Ext.button.Button',
	text : '导出',
	icon :'images/icon/export.png',
	config:{
		exportUrl: null,
	},
	exportAction: function(parameter){
		var url = this.getExportUrl() + '?' + Ext.urlEncode(parameter);
		
		if (Ext.isIE6 || Ext.isIE7 || Ext.isSafari || Ext.isSafari2 || Ext.isSafari3) {
            Ext.getBody().insertHtml('afterEnd',
					'<iframe frameborder="0" width="0" location="' + url + '" />');
        } else {
            Ext.getBody().insertHtml('afterEnd',
					'<iframe frameborder="0" width="0" src="' + url + '" />');
        }
	}
});