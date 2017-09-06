<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" %>
<html>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="ext/resources/css/ext-all.css">
    <script type="text/javascript" src="ext/ext-all-debug.js"></script>
    <script type="text/javascript" src="ext/ext-theme-gray.js"></script>
    <script type="text/javascript" src="mock/mock.js" ></script>
    <script type="text/javascript" src="mock/mockRule.js" ></script>
 	<script type="text/javascript" src="treemenu.js"></script>
    <script type="text/javascript">
    	Ext.apply(Ext.Msg.buttonText,{
    		cancel : '取消',
    		no:'否',
    		ok:'OK',
    		yes:'是',
    	});
        Ext.onReady(function(){

        var centerPanel = Ext.create('Ext.tab.Panel', {
        			id : 'mainPanel',
        			region : 'center',
        			layout : 'fit',
        			closable: false,
        			closeAction:'destroy'
        		});

        var menuPanel = Ext.create('Ext.tree.Panel',{
                        region : 'west',
                        store: _treeMenuStore,
                        width : 200,
                        split : true,
                        rootVisible : false,
                        collapsible : true,
                        listeners : {
                                itemclick : function(view, record, item, index, e, eOpts) {
                                    if(!record.isLeaf()) return;

                                    var id = record.get('id');
                                    var com = centerPanel.getComponent(id);
                                    if(!com){
                                        com = Ext.create(record.get('jsName'), {
                                            id : id,
                                            title: record.get('text'),
                                            closable:true
                                        });
                                        centerPanel.add(com);
                                    }
                                    centerPanel.setActiveTab(com);

                                }
                      }

        });

        vp = new Ext.Viewport({ layout : 'border',
            items : [{region:'north',height:100,html:'logo'}, menuPanel, centerPanel]
        });
        });
    </script>
<body oncontextmenu="return false">

</body>
</html>