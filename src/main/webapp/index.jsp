<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" %>
<html>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="ext/resources/css/ext-all.css">
    <script type="text/javascript" src="ext/ext-all-dev.js"></script>
 	<script type="text/javascript" src="treemenu.js"></script>
    <script type="text/javascript">
    	Ext.apply(Ext.Msg.buttonText,{
    		cancel : '取消',
    		no:'否',
    		ok:'OK',
    		yes:'是',
    	});
        Ext.onReady(function(){
            vp = new Ext.Viewport({
                layout:'border',
                items:[
                    {region:'north',height:100,html:'logo'},
                    {region:'west',width:200,items:[menupanel]},
                    searchPanel
                    ]
            });
        });
    </script>
<body oncontextmenu="return false">

</body>
</html>