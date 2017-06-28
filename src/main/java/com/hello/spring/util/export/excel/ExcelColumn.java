package com.hello.spring.util.export.excel;

/**
 * excel列定义
 * @author
 *
 */
public class ExcelColumn {
	
	/**
	 * 字段名称
	 */
	private String fieldName;
	
	/**
	 * 显示的标题
	 */
	private String headerText;
	
	/**
	 * 格式化字符串
	 */
	private String formatString;
	
	/**
	 * 合并
	 */
	private String mergeText;
	
	public ExcelColumn(){
		
	}
	
	public ExcelColumn(String fieldName,String headerText,String formatString,String mergeText){
		this.fieldName = fieldName;
		this.headerText = headerText;
		this.formatString = formatString;
		this.mergeText = mergeText;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getHeaderText() {
		return headerText;
	}

	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}

	public String getFormatString() {
		return formatString;
	}

	public void setFormatString(String formatString) {
		this.formatString = formatString;
	}

	public String getMergeText() {
		return mergeText;
	}

	public void setMergeText(String mergeText) {
		this.mergeText = mergeText;
	}
	
	
}
