package com.hello.spring.util.export.excel;

import java.util.List;

/**
 * 数据下钻接口
 * @author
 *
 */
public interface ExcelDrill <T>{
	List<?> drill(T o);
}
