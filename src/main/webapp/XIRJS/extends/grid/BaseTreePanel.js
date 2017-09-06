(function(){
/**
 * TreePanel 简易封装实现
 */
Ext.define('XIRJS.extends.grid.BaseTreePanel',{
	extend : 'Ext.tree.Panel',
	requires: ['XIRJS.extends.grid.BaseUtils', 'XIRJS.utils.CommonUtil'],
	rootVisible: false,
	config:{
		sqlPath: null,
		queryType: "1",
		actionUrl : 'queryAction/queryMT.action',
		storeAutoLoad: true,   // 是否进入列表自动加载数据
		idProperty: 'id',
		nodeParam: 'node',
		defaultRootId: 'root',
		treecolumnDataIndex: 'id',
		treecolumnHeader: '',
		sortableColumns: false
	},
	/** 创建列表列定义 **/
	createGridColumns: Ext.emptyFn,
	/** 创建操作按钮 **/
	createToolButtons: Ext.emptyFn,

	getExtraParams: Ext.emptyFn,
	
	constructor: function (config) {
		Ext.apply(this, config);
		Ext.suspendLayouts();
			buildColumns.call(this);
			buildTreeStore.call(this);
			buildToolButtons.call(this);
    	Ext.resumeLayouts(true);
    	this.mon(this.store, 'beforeload', this.beforeLoadX, this);
    	this.mon(this.store, 'load', this.londStoreHandler, this);
    	this.callParent(arguments);
        this.mon(this, 'selectionchange', this.refreshButtonsStatus, this);
    },
    
    beforeLoadX: function(store){
    	var extraParams = this.getExtraParams() || {};
    	Ext.apply(store.proxy.extraParams, extraParams);
    },
    
    /**
     * 刷新所有按钮状态
     * Button必须实现refreshStatus方法
     */
    refreshButtonsStatus: function(view, records){
    	Ext.Array.each(this._toolButtons, function(button) {
    		Ext.isFunction(button.refreshStatus) && button.refreshStatus.call(button.scope || button, button, records);
    	});
    },
    
    londStoreHandler : function(){
    	this.getSelectionModel().deselectAll();
    	this.fireEvent('selectionchange', this, null);
    },
});


var buildColumns = function(){
	var _columns = this.createGridColumns() || [];
	
	Ext.Array.forEach(_columns, function(col, index){
		if(col.formatString){
			col.renderer = function (value) {
				var ret = null;
				if (isNaN(value)) {
					ret = value; 
				} else {
					ret = XIRJS.extends.grid.BaseUtils.formatAmount(value, col.formatString);
				}
				return ret;
			};
			col.align = 'right'; 
		}
	});
	_columns.splice(0, 0, {
		xtype : 'treecolumn', 
		width : 250, 
		dataIndex : this.treecolumnDataIndex, 
		header: this.treecolumnHeader
	});
	
	return this.columns = _columns;
}

var buildTreeStore = function(){
	var treeModelId = 'treeModel-JMX-' + new Date().getTime();
	
	Ext.define(treeModelId, {
        extend: 'Ext.data.Model',
        idProperty : this.idProperty,
        fields: a(this.columns),
    });
	
	var treeStore = Ext.create('Ext.data.TreeStore', {
		model : treeModelId,
		autoLoad: true,
		pageSize: 20,
		proxy:{
			type:'ajax',
			url: this.actionUrl ,
            reader: {
				type: 'json',
				root : 'items',
			},
			actionMethods: { read: 'POST' },
			extraParams:{
				sqlPath: this.sqlPath,
				queryModel: this.queryModel,
				enablePager: false
			}
         },
         nodeParam : this.nodeParam,
         defaultRootId: this.defaultRootId,
//         root: {
//             loaded: this.storeAutoLoad
//         }
     });
	
	return this.store = treeStore;
}
var buildToolButtons = function(){
	var tbar = [];
	
	this._toolButtons = this.createToolButtons() || [];
	Ext.Array.forEach(this._toolButtons ,function(item){
		tbar.push(item);
		tbar.push('-');
	});
	tbar.push('->');

	return this.tbar = tbar;
}

var a = function(_columns){
	var _fields = [];
	Ext.Array.forEach(_columns, function(col, index){
		var name = col.dataIndex;
		var type = col.formatString ? 'number' : 'string';
		_fields.push({
			name: name,
			type: type
		});
	});
	return _fields;
}

}());