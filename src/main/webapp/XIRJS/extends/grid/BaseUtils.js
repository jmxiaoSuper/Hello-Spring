Ext.define('XIRJS.extends.grid.BaseUtils', {
	singleton: true,
	
	/**
	 * 格式化金额格式
	 *
	 * @param num
	 * @returns string 格式化后的金额格式
	 */
	formatAmount : function (num, formatStrIn) {
		if(Ext.isEmpty(num)) return '';
	    if(num == 'Infinity'){
			return '无穷大';
		}
	    if(num == '-Infinity'){
			return '无穷小';
		}
	    if (isNaN(num)) {
	        num = this.unFormatAmount(num);
	    }
	    if (num == '-') {
	        return num;
	    }
	    if (isNaN(num)) {
	        return '';
	    }
	    var formatStr = formatStrIn || '0,000.0000';
	    var number  = Number(num);
	    if( number < 0){
	        var fmtNumber = Ext.util.Format.number(Number(num).mul(-1), formatStr);
	        return '-' + fmtNumber;
	    }else{
	        return Ext.util.Format.number(Number(num), formatStr);
	    }
	},

	unFormatAmount : function (numStr) {
	    if (!!numStr) {
	        var num = String(numStr).replace(new RegExp(',', 'g'), '');
	        if (isNaN(num)) {
	            return numStr;
	        } else {
	            return Number(num);
	        }
	    } else {
	        return '';
	    }
	},
});
