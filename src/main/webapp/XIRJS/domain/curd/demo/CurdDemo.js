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

});