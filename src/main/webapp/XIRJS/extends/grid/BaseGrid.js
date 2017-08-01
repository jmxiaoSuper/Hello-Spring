(function(){
/**
 * 查询窗体实例
 */
Ext.define('XIRJS.extends.grid.BaseGrid', {
	extend : 'Ext.grid.Panel',
	requires : ['XIRJS.utils.CommonUtil', 'XIRJS.extends.grid.BaseUtils',],
	sortableColumns: false,
	superPanel: null,
	visitNum: 0,

	config:{// 自定义参数
		pageSize: 20,
		selectModel: 'SINGLE', //"SINGLE"/"SIMPLE"/"MULTI"
		sqlPath: null,
		queryType: '3',        // 1 onlyRecord, 2 onlyTotals  3: all
		exportTitle : null,
		enablePager: true,
		isCanSearch : true,
		isCanExport : false,
		showSummaryRow: false,
		storeAutoLoad: true,
		isShowRowNum: true,
		sortableColumns: false,
	},
	/** 创建列表列定义 **/
    createGridColumns: Ext.emptyFn,
    /** 创建操作按钮 **/
    createToolButtons: Ext.emptyFn,
    /** 创建查询条件 **/
    createSearchConditions: Ext.emptyFn,
    /** 获取额外的参数 **/
    getExtraParams : Ext.emptyFn,
    /** 是否需要行选择器 **/
    allowCheckSelModel: Ext.emptyFn,
    /** 列表单击事件**/
    itemclickHandler : Ext.emptyFn,
    /** 查看详情 **/
    itemdbclickHandler: Ext.emptyFn,

	constructor: function (config) {
		Ext.apply(this, config);
    	Ext.suspendLayouts();
    		ComponentExtUtil.buildColumn.call(this);
    		ComponentExtUtil.buildStore.call(this);
    		ComponentExtUtil.buildTopBar.call(this);
    		ComponentExtUtil.buildBottomBar.call(this);
    		ComponentExtUtil.buildCheckModel.call(this);
    	Ext.resumeLayouts(true);

    	this.callParent(arguments);
        this.mon(this.store, 'beforeload', this.beforeLoadStore, this);
        this.isCanSearch && this.mon(this.store, 'beforeload', this.setConditionData, this);
        this.mon(this.store, 'load', this.londStoreHandler, this);
        this.mon(this, 'itemclick', this.itemclickHandler, this);
        this.mon(this, 'itemdbclick', this.itemdbclickHandler, this);
        this.mon(this, 'columnhide', this.columnHideHandler, this);
        this.mon(this, 'columnshow', this.columnShowHandler, this);
        this.mon(this, 'selectionchange', this.refreshButtonsStatus, this);
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
    beforeLoadStore: function(store){
    	Ext.apply(store.proxy.extraParams,{
    		sqlPath : this.sqlPath,
    		queryType: this.queryType,
    		end: store.currentPage * store.pageSize,
    		showSummaryRow: this.showSummaryRow,
    		enablePager : this.enablePager,
    		panelClass : this.panelClass
    	});

    	var extraParams = this.getExtraParams() || {};
    	Ext.apply(store.proxy.extraParams, extraParams);
    },
    londStoreHandler : function(){
    	this.getSelectionModel().deselectAll();
    	this.fireEvent('selectionchange', this, null);
    },
    // 列展示
    columnShowHandler:function(ct, column, opts){
    	var index = XIRJS.utils.CommonUtil.FindValueInArray(this._columns, 'dataIndex', column.dataIndex);
    	if(index != -1){
    		this._columns[index].hidden = false;
    	}
    },
    // 列隐藏
    columnHideHandler: function(ct, column, opts){
    	 var index = XIRJS.utils.CommonUtil.FindValueInArray(this._columns, 'dataIndex', column.dataIndex);
    	 if(index != -1){
    		 this._columns[index].hidden = true;
    	 }
    },

    getSelectRecord: function(){
    	return this.getSelectionModel().getSelection();
    },

    exportButtonHandler : function(button){
    	var params  = Ext.clone(this.store.proxy.extraParams);
    	var title   = this.exportTitle || this.superPanel.title || 'default';
    	var columns = [];
    	Ext.Array.forEach(this._columns, function(column){
    		column.hidden ||
    			columns.push(Ext.copyTo({}, column, 'dataIndex,header,align,merge,formatString') );
    	});

    	button.exportAction(Ext.apply(params,{
    		title : title,
    		columns : Ext.JSON.encode(columns),
    	}));
    },

    searchButtonHandler: function(){
    	this.visitNum ++;
    	this.store.loadPage(1);
    },

    setConditionData: function(store){
    	if(this.visitNum == 0) return true;

    	this.visitNum = 0;
    	var conditions = {};
    	var isSafeSearch = true;
    	Ext.Array.each(this._searchConditions, function(item) {
    		if(Ext.isFunction(item.isValid) && !item.isValid()){
    			Ext.Msg.alert('错误', item.getFieldLabel()+'格式不正确或为空！');
    			isSafeSearch = false;
    		}else{
    			if(Ext.isFunction(item.getSubmitValue)){
    				conditions[item.getName()] = item.getSubmitValue();
    			}else{
    				conditions[item.getName()] = item.getValue();
    			}
    		}
    	});

    	isSafeSearch && Ext.apply(store.proxy.extraParams, conditions);
    	// false 查询取消
    	return isSafeSearch;
    },
});

// 私有工具类
var ComponentExtUtil = {};
/**
 * 根据merge属性合并列表
 */
ComponentExtUtil.MergeColumns = function(columns){
    if(typeof columns === 'undefined' || Array.isArray(columns) == false ){
        console.error('columns NullPointException!');
        return;
    }

    var _columns = [];
        record = null,
        len = columns.length,
        index = 0;
    for (; index < len; index++) {
        record = columns[index];

        if (!record.merge) {
            _columns.push(record);
        } else {
            var d = XIRJS.widget.CommonUtil.FindValueInArray(_columns, 'header', record.merge);
            if(d == -1){
                _columns.push({
                    header : record.merge,
                    columns : [record]
                });
            } else {
                _columns[d].columns.push(record);
            }
        }
    }
    return _columns;
};

/**
 * 表格处理
 */
ComponentExtUtil.BuildColumns = function(columns){
    if(typeof columns === 'undefined' || Array.isArray(columns) == false ){
        console.error('columns NullPointException!');
        return;
    }

    var cloneColumns = [];

    Ext.Array.forEach(columns, function(col, index){
        var column = Ext.clone(col);
        if(column.formatString){
            column.renderer = function (value) {
                var ret = null;
                if (isNaN(value)) {
                    ret = value;
                } else {
                    ret = XIRJS.extends.grid.BaseUtils.formatAmount(value, column.formatString);
                }
                return ret;
            };
            column.align = 'right';
        }
        cloneColumns.push(column);
    });

    return ComponentExtUtil.MergeColumns(cloneColumns);
};

ComponentExtUtil.buildColumn = function(){
    this._columns = this.createGridColumns() || [];
    this.columns = ComponentExtUtil.BuildColumns(this._columns);
    this.isShowRowNum && this.columns.splice(0,0,{xtype:'rownumberer', width:30});
},
ComponentExtUtil.buildStore = function(){
    var _fields = [];
    Ext.Array.forEach(this._columns, function(item, index){
        _fields.push({
            name : item.dataIndex,
            type : item.type || 'string'
        });
    });
    this.store = Ext.create('Ext.data.Store', {
        fields : _fields,
        pageSize: this.pageSize,
        autoLoad : this.storeAutoLoad,
        proxy : {
            type: 'ajax',
            url: 'queryAction/queryMT.action',
            reader : {
                type : 'json',
                root : 'items',
                totalProperty : 'totals'
            },
            limitParam : 'pagesize',
            actionMethods : {
                create : 'POST',
                read : 'POST',
                update : 'POST',
                destroy : 'POST'
            }
        }
    });
},
ComponentExtUtil.buildTopBar = function(){
    var tbar = [];

    if(this.isCanExport){
        var exportButton = Ext.create('XIRJS.widget.button.ExportButton', {
            scope : this,
            handler: this.exportButtonHandler,
            exportUrl: 'queryAction/export4Xls.action'
        });
        tbar.push(exportButton);
        tbar.push('-')
    }
    this._toolButtons = this.createToolButtons() || [];
    this._searchConditions = this.createSearchConditions() || [];

    Ext.Array.forEach(this._toolButtons ,function(item){
        tbar.push(item);
        tbar.push('-');
    });
    tbar.push('->');
    Array.prototype.push.apply(tbar, this._searchConditions);

    if(this.isCanSearch){
        var searchButton = Ext.create('Ext.button.Button', {
            text : '搜索',
            icon :'images/icon/search.gif',
            scope : this,
            handler: this.searchButtonHandler,
        });
        tbar.push(searchButton);
    }
    this.tbar = tbar;
},
ComponentExtUtil.buildBottomBar = function(){
    if(this.enablePager){
        this.bbar = Ext.create('Ext.PagingToolbar', {
            store: this.store,
            displayInfo: true,
            beforePageText: '第',
            afterPageText: '页,共 {0} 页',
            displayMsg: '显示  {0}-{1} 条,共  {2} 条',
            emptyMsg: '查询数据为空'
        });
    }
},
ComponentExtUtil.buildCheckModel = function(){
    if(this.allowCheckSelModel()){
        this.selModel = Ext.create("Ext.selection.CheckboxModel",{
            mode : this.selectModel
        });
    }
};
}());