/**
 * Created by Super.Xjm on 2017/7/13.
 */
Ext.define('XIRJS.domain.treepane.TreePanelDemo',{
    extend : 'XIRJS.extends.grid.BaseTreePanel',
    sqlPath: 'com.hello.spring.demo.dao.TestTree',
    idProperty: 'NODE_ID',
    treecolumnDataIndex: 'C_NAME',
    treecolumnHeader: '国家分布',
    createGridColumns: function(){
        return [
        {dataIndex: 'C_NAME', header: '国家名称'}
        ];
    }

});