// mock规则 前后台分离方案
// API文档  https://github.com/nuysoft/Mock/wiki
 Mock.mock('xcc_customquery/queryComponentRecords.action', {
	items:[{
		iCode: '也是',
		mType: '@city',
		aType: 'asd',
		'amount|1-100000' : 1000000,
		pClass: '@First',
		pType: '@Name'
	}]
 });
