package com.hello.spring.util.export.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * excel导出服务(非线程安全）
 * @author
 *
 */
public class ExcelExportService {

	private final static String HEADER_FONT_NAME = "Times New Roman";
	private final static short FONT_HEIGHT_IN_POINT = 8;
	
	private Map<String, Field> fieldsMap;
	
	private Map<String, CellStyle> cellStylesMap;
	
	/**
	 * excel导出列定义
	 */
	private List<ExcelColumn> columns;
	
	/**
	 * 检查是否存在合并列头
	 * @param columns
	 * @return
	 */
	private boolean existsMerge(List<ExcelColumn> columns){
		for(ExcelColumn column : columns){
			if(StringUtils.isNoneBlank(column.getMergeText())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 从stsart位置开始检查有多少个列需要合并
	 * @param start
	 * @param merge
	 * @param columns
	 * @return
	 */
	private int getLastMergeIndex(int start,String merge,List<ExcelColumn> columns){
		for(start = start + 1;start < columns.size(); start++){
			ExcelColumn column = columns.get(start);
			if(StringUtils.isBlank(column.getMergeText()) || !merge.equals(column.getMergeText())){
				break;
			}
		}
		return --start;
	}
	
	/**
	 * 创建子标题
	 * @param row
	 * @param columns
	 * @param start
	 * @param last
	 */
	private void createSubHeader(CellStyle style,Row row ,List<ExcelColumn> columns,int start ,int last){
		for(;start <= last ; start++){
			Cell cell = row.createCell(start);
			cell.setCellStyle(style);
			cell.setCellValue(columns.get(start).getHeaderText());
		}
	}
	
	/**
	 * 创建标题单元格样式
	 * @param wb
	 * @return
	 */
	private CellStyle createHeaderStyle(Workbook wb){
	        CellStyle style = wb.createCellStyle();
	        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	        style.setAlignment(CellStyle.ALIGN_CENTER);
	        // 边框
	        style.setBorderBottom(CellStyle.BORDER_THIN); 
	        style.setBorderLeft(CellStyle.BORDER_THIN);
	        style.setBorderTop(CellStyle.BORDER_THIN);
	        style.setBorderRight(CellStyle.BORDER_THIN);
	        // 字体
	        Font headerFont = wb.createFont();
	        headerFont.setFontName(HEADER_FONT_NAME);
	        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
	        headerFont.setFontHeightInPoints(FONT_HEIGHT_IN_POINT);
	        style.setFont(headerFont);
	        
	        return style;
	    }
	
	/**
	 * 创建标题
	 * @param sheetName
	 * @param wb
	 * @param columns
	 * @return
	 * @throws IOException
	 */
	private Sheet createHeader(String sheetName,Workbook wb) throws IOException{	
	    String safeName = WorkbookUtil.createSafeSheetName(sheetName);
		Sheet sheet = wb.createSheet(safeName);
		CellStyle cellStyle = createHeaderStyle(wb);
		boolean isExistsMerge = existsMerge(columns);
		Row row0 = sheet.createRow(0);
		Row row1 = isExistsMerge ? sheet.createRow(1) : null;
		int c = 0;
		while(c < columns.size()){
			Cell cell = row0.createCell(c);
			cell.setCellStyle(cellStyle);
			if(!isExistsMerge){
				cell.setCellValue(columns.get(c).getHeaderText());
				c++;
			}else{
				String merge = columns.get(c).getMergeText();
				if(StringUtils.isNoneBlank(merge)){
					cell.setCellValue(merge);
					int lastCol = getLastMergeIndex(c,merge,columns);
				    CellRangeAddress range = new CellRangeAddress(0, 0, c, lastCol);
				    for(int i = c + 1; i <= lastCol; i++){
				    	Cell splitCell = row0.createCell(i);
				    	splitCell.setCellStyle(cellStyle);
				    }
				    sheet.addMergedRegion(range);
				    createSubHeader(cellStyle,row1,columns,c,lastCol);
				    c = lastCol + 1;
				}else{
					cell.setCellValue(columns.get(c).getHeaderText());
					CellRangeAddress range = new CellRangeAddress(0, 1, c, c);
					sheet.addMergedRegion(range);
					Cell splitCell = row1.createCell(c);
			    	splitCell.setCellStyle(cellStyle);
					c++;
				}
			}
		}
		if(isExistsMerge) sheet.createFreezePane(0, 2);
		else sheet.createFreezePane(0, 1);
		return sheet;
	}
	
	/**
	 * 获取数据对象上的值
	 * @param data
	 * @param fieldName
	 * @return
	 */
	private Object getFieldValue(Object data,String fieldName){
		Object value = null;
		if(data instanceof Map){
			Object obj = ((Map)data).get(fieldName);
			value = obj == null ? "" : obj.toString();
		}else{
			Field field = null;
			try{
				 if(fieldsMap == null || !fieldsMap.containsKey(fieldName)){
					 if(fieldsMap == null) fieldsMap = new HashMap<String, Field>();
					 field = data.getClass().getDeclaredField(fieldName);
					 field.setAccessible(true);
					 fieldsMap.put(fieldName, field);
				 }
			    field = fieldsMap.get(fieldName);
					
			} catch (NoSuchFieldException e) {
				throw new RuntimeException(e);
			};
		    
		    try {
				value = field.get(data);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		return value;
	}

	/**
	 * 创建数据单元格样式
	 * @param wb
	 * @param cellFormat
	 * @return
	 */
	private CellStyle getBodyCellStyle(Workbook wb, String cellFormat) {
		if(cellStylesMap.containsKey(cellFormat)){
			return cellStylesMap.get(cellFormat);
		}
	        
        CellStyle st = wb.createCellStyle();
        st.setBorderBottom(CellStyle.BORDER_THIN); 
        st.setBorderLeft(CellStyle.BORDER_THIN);
        st.setBorderTop(CellStyle.BORDER_THIN);
        st.setBorderRight(CellStyle.BORDER_THIN);
        st.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        if(!cellFormat.equals("*")){
        	short formatStr = wb.createDataFormat().getFormat(cellFormat);
        	st.setDataFormat(formatStr);
        }
        cellStylesMap.put(cellFormat, st);
        return st;
	}
	
	/**
	 * 设置数据单元格样式
	 * @param cell
	 * @param o
	 * @param column
	 */
	private void setDataCellValue(Cell cell,Object o,ExcelColumn column){
		boolean isNumberic = false;
		if(o instanceof Integer ||
				o instanceof Double ||
				o instanceof Long ||
				o instanceof BigDecimal){
			isNumberic = true;
		}
		
		CellStyle st = getBodyCellStyle(cell.getSheet().getWorkbook(), StringUtils.isEmpty(column.getFormatString()) ? "*" : column.getFormatString());
		cell.setCellStyle(st);
		if(o == null){
			cell.setCellValue("");
		}
		else if(isNumberic) {
			cell.setCellValue(Double.parseDouble(o.toString()));
		}
		else {
			cell.setCellValue(o.toString());
		}
	}
	
	/**
	 * 写入数据
	 * @param sheet
	 * @param datas
	 * @param drill
	 */
	private void writeData(Sheet sheet,List<?> datas,ExcelDrill drill){
		int lastRow = sheet.getLastRowNum();
		for(Object data : datas){
			if(lastRow > 10000) break;
			Row row = sheet.createRow(++lastRow);
			for(int i = 0; i < columns.size(); i++){
				ExcelColumn column = columns.get(i);
				String fieldName = column.getFieldName();
				Object value = null;
				if(!StringUtils.isEmpty(fieldName)){
					value = getFieldValue(data,fieldName);
				}
				Cell cell = row.createCell(i);
				setDataCellValue(cell, value, column);
			}
			if(drill != null){
				List<?> details = drill.drill(data);
				if(CollectionUtils.isNotEmpty(details)){
					int from = lastRow;
					for(Object detail : details){
						Row row4Detail = sheet.createRow(++lastRow);
						for(int i = 0; i < columns.size(); i++){
							ExcelColumn column = columns.get(i);
							String fieldName = column.getFieldName();
							Object value = null;
							if(!StringUtils.isEmpty(fieldName)) {
								value = getFieldValue(detail,fieldName);
							}
							Cell cell = row4Detail.createCell(i);
							setDataCellValue(cell, value, column);
						}
					}
					sheet.groupRow(from + 1, lastRow);
					sheet.setRowGroupCollapsed(from + 1, true);
				}
			}
		}
	}
	
	public ExcelExportService(List<ExcelColumn> columns){
		this.columns = columns;
		cellStylesMap = new HashMap<String, CellStyle>(); 
	}
	
	/**
	 * 导出
	 * @param sheetName
	 * 	sheet名称
	 * @param ds
	 * 	数据源
	 * @param isXlsx
	 *  false : excel97-2003工作簿  true:xlsx工作簿
	 * @param os
	 * 	输出流
	 */
	public void export(String sheetName,List<?> ds,boolean isXlsx,ExcelDrill drill,OutputStream os){
		Workbook wb = null;
		try{
			wb = isXlsx ? new XSSFWorkbook() : new HSSFWorkbook();
			try {
				Sheet sheet= createHeader(sheetName, wb);
				writeData(sheet, ds, drill);
				for(int i = 0 ;i < this.columns.size();i++){
					sheet.autoSizeColumn(i);
				}
				wb.write(os);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}finally{
			if(wb != null){
				try {
					wb.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			if(os != null){
				try {
					os.close();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	
}
