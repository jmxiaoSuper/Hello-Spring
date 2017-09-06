(function(){
_treeMenuStore = Ext.create('Ext.data.TreeStore', {
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
                                    id : 12367,
                                    text : '树形列表',
                                    leaf : true,
                                    jsName : 'XIRJS.domain.treepane.TreePanelDemo'
                                }]
                    }]
        }
    });
}())