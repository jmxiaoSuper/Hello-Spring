/**
 * Created by Super.Xjm on 2017/7/4.
 */
Ext.define('XIRJS.domain.curd.demo.CurdDemo',{
    extend : 'XIRJS.extends.grid.BaseGrid',
    sqlPath: 'com.hello.spring.demo.dao.TestMapper',
    createGridColumns : function(){
        return [
            {dataIndex: 'ID', header: '主键', width : 150},
            {dataIndex: 'NAME', header: '名称', width : 150},
            {dataIndex: 'CREATE_TIME', header: '创建时间', width : 150},
        ];
    },
    createToolButtons: function(){
        var upButton = Ext.create('Ext.button.Button',{
            text : '上传文件',
            scope: this,
            handler: this.upLoadHandler
        });

        return [upButton];
    },

    upLoadHandler: function(){
        var upForm = Ext.create('Ext.form.Panel', {
        			frame : true,
        			id:'form',
        			title : '文件上传',
        			bodyPadding : '10 10 0',
        			defaults : {
        				anchor : '100%',
        				allowBlank : false,
        				msgTarget : 'side',
        				labelWidth : 100
        			},

        			items : [ {
        				xtype : 'textfield',
        				fieldLabel : '自定义文件名称',
        				emptyText : '请自定义文件名称,必填!',
        				name : "uplname"
        			}, {
        				xtype : 'filefield',
        				id : 'form-file',
        				emptyText : '请选择本地文件',
        				fieldLabel : '上传地址',
        				name : 'upFile',
        				buttonText : '上传',
        				buttonConfig : {
        					iconCls : 'upload-icon'
        				}
        			}],

        			buttons : [{
        				text : '上传',
        				handler : function() {
        					var form = this.up('form').getForm();
        					if (form.isValid()) {
        						form.submit({
        							url : 'queryAction/uploadFile.action',
        							waitMsg : '正在上传您的文件，请耐心等候...',
        							success : function(form, action) {
        								Ext.Msg.alert('提示信息', "文件保存成功");
        							},
        							failure : function() {
        								Ext.Msg.alert("提示信息", "对不起，文件保存失败");
        							}
        						});
        					}
        				}
        			}, {
        				text : '重置',
        				handler : function() {
        					this.up('form').getForm().reset();
        				}
        			}]
        		});
        		Ext.create('Ext.window.Window',{
        			title: '文件上传',
        			layout: 'fit',
        			width: 500,
        			height: 500,
        			items: [upForm]
        		}).show();
    }
});