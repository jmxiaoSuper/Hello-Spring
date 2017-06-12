Ext.define('XIRJS.utils.CommonUtil', {
	singleton: true,
	
    /** 必须项前的红“*” */
    required: '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',
    
	/** 普通信息提示框 */
	Info : function(msg) {
		Ext.MessageBox.show({
	           title: '消息', 
	           msg: msg,
	           buttons: Ext.MessageBox.OK,
	           icon: Ext.MessageBox.INFO
	       });
	},

	/** 警告信息提示框 */
	Warn :  function(msg) {
		Ext.MessageBox.show({
	           title: '警告', 
	           msg: msg,
	           buttons: Ext.MessageBox.OK,
	           icon: Ext.MessageBox.WARNING
	       });
	},

	/** 成功信息提示框 */
	Success :  function(msg,callBack) {
		Ext.MessageBox.show({
	           title: '成功', 
	           msg: msg,
	           fn : callBack,
	           buttons: Ext.MessageBox.OK,
	           icon: "x-message-box-success"
	       });
	},

	/** 失败信息提示框 */
	Error :  function(msg,callback) {
		if(!callback){
			Ext.MessageBox.show({
		           title: '失败', 
		           msg: msg,
		           buttons: Ext.MessageBox.OK,
		           icon: Ext.MessageBox.ERROR
		       });
		}else{
			Ext.MessageBox.show({
		           title: '失败', 
		           msg: msg,
		           fn : callback,
		           buttons: Ext.MessageBox.OK,
		           icon: Ext.MessageBox.ERROR
		       });
		}
	},

	/** 确认框 */
	Confirm :  function(msg, callback,scope) {
		Ext.MessageBox.confirm('确认', msg, callback,scope);
	},
	
	getGlobalMask : function(message){
		message = message ||'请稍候……';
		if(!this.globalMask){
			var target = vp || Ext.getBody();
			this.globalMask = Ext.create("Ext.LoadMask", { target: target });
		}
		this.globalMask.msg = message;
		
		return this.globalMask;
	},
	getTargetMask : function(target, message){
		message = message ||'请稍候……';
		return Ext.create("Ext.LoadMask", { target: target, msg : message});
	},
	
	getOnlyRecordsStore: function(fields, sqlPath ,extraParams){
		if(!fields){
			console.error('fields NullPointException!');
			return;
		}
		extraParams = extraParams || {};
		extraParams['sqlPath'] = sqlPath;
		
		return Ext.create('Ext.data.Store',{
			fields : fields,
			autoLoad : true,
			pagesize : 20,
			proxy : {
				type: 'ajax',
		        url: 'xcc_customquery/onlyQueryRecords.action', 
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
				},
				extraParams : extraParams
			}
		});
		
	},
	
	/************************************************* 方法区 *************************************************/
	GetDeepKey : function(obj, deepKey){
		var keys = deepKey.split('.');
		var ity = 0;
		var lenty = keys.length;
		var temp = obj;
		for(; ity < lenty; ity++){
			temp = temp[keys[ity]];
		}
		return temp;
	},
	/**
	 * 获取数组中是否存在与value一致的对象
	 * 不存在返回：-1
	 * 找到时：>=0
	 */
	FindValueInArray : function(arr, typeName, value){
		if(!arr || Array.isArray(arr) == false ){
			console.error('arr NullPointException!');
			return;
		}
		if(!typeName){
			console.error('typeName NullPointException!');
			return;
		}
		if(!value){
			console.error('value NullPointException!');
			return;
		}
		var i = 0;
		var len = arr.length
		var index = -1;
		for (; i < len; i++) {
			if (this.GetDeepKey(arr[i],typeName) === value) {
				index = i;
				break;
			}
		}
		
		return index;
	},

	/**
	 * Ajax请求
	 */
	AjaxRequest : function(url, params, scope ,succFuc, failFuc, isASync){
		if(!url){
			console.error('url NullPointException!');
			return;
		}
		if(!params){
			console.error('params NullPointException!');
			return;
		}
		if(!scope){
			console.error('scope NullPointException!');
			return;
		}
		if(typeof isASync === 'undefined') isASync = true;
		
		var me = this;
		var failFnWrapper = function(response, options){
			if (Ext.isFunction(failFuc)) {
				failFuc.call(scope, response, options);
            }else{
            	console.error(response, options);
            }
		};
		var succFnWrapper = function(response, options){
			if(response.responseText){
				var ret = null;
				try{
					ret = Ext.decode(response.responseText);
				}catch(e){
					console.error(e);
					return;
				}
				if (ret && ret.success) {
                    Ext.isFunction(succFuc) &&
                        succFuc.call(scope, response, options, ret.msg);
				} else if(ret && Ext.isDefined(ret.totals) && Ext.isDefined(ret.items)){
					Ext.isFunction(succFuc) &&
						succFuc.call(scope, response, options, ret.items, ret.totals);
				} else {
					failFnWrapper(response, options);
				}
			}else{
				failFnWrapper(response, options);
			}
		};

		
		//XIRJS.common.Util.ajaxRequest(options, scope);
		
		Ext.Ajax.request({
			url: url,
			params: params,
			success: succFnWrapper,
			failure: failFnWrapper,
			scope: scope,
			async: isASync,
			timeout: 60000
		});
	},

	queryRecordRequestSync:function(sqlPath, params, scope, succFn, failFn){
		if(!sqlPath){
			console.error('url NullPointException!');
			return;
		}
		params = params || {};
		params['sqlPath'] = sqlPath;
		this.AjaxRequest('xcc_customquery/onlyQueryRecords.action', params, scope, succFn, failFn, false);
	}
});
