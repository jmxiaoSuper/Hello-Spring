
var store = Ext.create('Ext.data.TreeStore', {
			fields : ['jsName', 'id', 'text', 'leaf'],
			root : {// 树状图有且只有一个树根
				expanded : false,// 这树状图是可以打开叶子的
				children : [{
							text : '自定义查询',
							expanded : false,
							children : [{
										id : 1212121,
										text : '自定义查询',
										leaf : true,
										jsName : 'XIRJS.domain.curd.demo.CurdDemo'
									},{

										id : 1232,
										text : 'CURD查询',
										leaf : true,
										jsName : 'XIRJS.xjm.CurdPanel'
									}]
//						}, {
//							text : '表扩展',
//							expanded : false,
//							children : [{
//										id : 23425,
//										text : '表扩展定义',
//										leaf : true,
//										jsName : 'XIRJS.widget.exttab.ExtTableDefinedMain'
//									},{
//										id : 223425,
//										text : '扩展表新增数据',
//										leaf : true,
//										jsName : 'XIRJS.widget.TableExtendPanel'
//									}]
						}]
			}
		});

var searchPanel = Ext.create('Ext.tab.Panel', {
			id : 'mainPanel',
			region : 'center',
			layout : 'fit',
			closable: false,
			closeAction:'destroy'
		});

var menupanel = {
	region : 'west',
	xtype : 'treepanel',
	store : store,// 添加树状图
	width : 200,// 这里一定要设置好宽度，否则不显示
	height : 500,
	rootVisible : false,// 不显示树根
	split : true,// 可以自己调整其大小
	collapsible : true,// 可以折叠
	listeners : {
		itemclick : function(view, record, item, index, e, eOpts) {
			if (record.get('leaf')) { // 叶子节点
				var id = record.get('id');
				var com = searchPanel.getComponent(id);
				if(!com){
					com = Ext.create(record.get('jsName'), {
						id : id,
						title: record.get('text'),
						closable:true
					});
					searchPanel.add(com);
				}
				searchPanel.setActiveTab(com);
			}
		}
	}
};
