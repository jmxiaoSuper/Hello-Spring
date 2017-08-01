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

	private final static String HEADER_FONT_NAME    = "Times New Roman";
	private final static short FONT_HEIGHT_IN_POINT = 8;
	
	private Map<String, Field> fieldsMap;
	
	private Map<String, CellStyle> cellStylesMap;
	
	/**
	 * excel导出列定义
	 */
	private List<ExcelColumn> columns;

	private int size;
	
	/**
	 * 检查是否存在合并列头
	 * @return
	 */
	private boolean existsMerge(){
		for(ExcelColumn column : columns){
			if(StringUtils.isNotBlank(column.getMergeText())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 从start位置开始检查有多少个列需要合并
	 * @param start
	 * @param merge
	 * @return
	 */
	private int getLastMergeIndex(int start, String merge){
		for(start = start + 1; start < this.size; start++){
			String mergeText = columns.get(start).getMergeText();
			if(StringUtils.isBlank(mergeText) || !merge.equals(mergeText)){
				break;
			}
		}
		return --start;
	}
	
	/**
	 * 创建子标题
	 * @param row
	 * @param start
	 * @param last
	 */
	private void createSubHeader(CellStyle style,Row row ,int start ,int last){
		while (start <= last) {
			Cell cell = row.createCell(start);
			cell.setCellStyle(style);
			cell.setCellValue(columns.get(start).getHeaderText());

			start++;
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
	 * @return
	 * @throws IOException
	 */
	private Sheet createHeader(String sheetName, Workbook wb) throws IOException{

	    String safeName = WorkbookUtil.createSafeSheetName(sheetName);
		Sheet sheet     = wb.createSheet(safeName);

		CellStyle cellStyle   = this.createHeaderStyle(wb);
		boolean isExistsMerge = this.existsMerge();

		if(isExistsMerge){
			buildMergeHeader(sheet, cellStyle);
			sheet.createFreezePane(0, 2);
		}
		else{
			buildNormalHeader(sheet, cellStyle);
			sheet.createFreezePane(0, 1);
		}

		return sheet;
	}

	/**
	 * 构建正常的标题栏
	 * @param sheet
	 * @param cellStyle
	 */
	private void buildNormalHeader(Sheet sheet, CellStyle cellStyle){
		Row row0 = sheet.createRow(0);

		int c = 0;
		while(c < this.size){
			Cell cell = row0.createCell(c);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(columns.get(c).getHeaderText());

			c++;
		}
	}

	/**
	 * 构建有合并列的标题栏
	 * @param sheet
	 * @param cellStyle
	 */
	private void buildMergeHeader(Sheet sheet, CellStyle cellStyle){
		Row row0 = sheet.createRow(0);
		Row row1 = sheet.createRow(1);

		int c = 0;
		while(c < this.size){
			Cell cell = row0.createCell(c);
			cell.setCellStyle(cellStyle);

			String merge = columns.get(c).getMergeText();
			if(StringUtils.isNotBlank(merge)){

				cell.setCellValue(merge);
				int lastCol = this.getLastMergeIndex(c, merge);
				CellRangeAddress range = new CellRangeAddress(0, 0, c, lastCol);
				for(int i = c + 1; i <= lastCol; i++){
					Cell splitCell = row0.createCell(i);
					splitCell.setCellStyle(cellStyle);
				}
				sheet.addMergedRegion(range);
				createSubHeader(cellStyle, row1, c, lastCol);
				c = lastCol + 1;
			}
			else{
				cell.setCellValue(columns.get(c).getHeaderText());
				CellRangeAddress range = new CellRangeAddress(0, 1, c, c);
				sheet.addMergedRegion(range);
				Cell splitCell = row1.createCell(c);
				splitCell.setCellStyle(cellStyle);

				c++;
			}
		}
	}
	
	/**
	 * 获取数据对象上的值
	 * @param data
	 * @param fieldName
	 * @return
	 */
	private Object getFieldValue(Object data, String fieldName){
		if(data instanceof Map){
			Object obj = ((Map)data).get(fieldName);
			return obj == null ? "" : obj.toString();
		}

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
		}

		try {
			return field.get(data);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

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
		boolean isNumber = false;
		if(o instanceof Integer ||
				o instanceof Double ||
				o instanceof Long ||
				o instanceof BigDecimal ||
				StringUtils.isNotBlank(column.getFormatString())){

			isNumber = true;
		}
		
		CellStyle st = getBodyCellStyle(cell.getSheet().getWorkbook(), StringUtils.isEmpty(column.getFormatString()) ? "*" : column.getFormatString());
		cell.setCellStyle(st);

		if(o == null){
			cell.setCellValue("");
		}
		else if(isNumber) {
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
	private void writeData(Sheet sheet, List<?> datas , ExcelDrill drill){
		int lastRow = sheet.getLastRowNum();

		for(Object data : datas){
			if(lastRow > 10000) break;
			Row row = sheet.createRow(++lastRow);
			for(int i = 0; i < this.size; i++){
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
						for(int i = 0; i < this.size; i++){
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
		this.size    = columns.size();

		this.cellStylesMap = new HashMap<String, CellStyle>();
		this.fieldsMap     = new HashMap<String, Field>();
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
	public void export(String sheetName, List<?> ds, boolean isXlsx, ExcelDrill drill, OutputStream os){
		Workbook wb = null;

		try{
			wb = isXlsx ? new XSSFWorkbook() : new HSSFWorkbook();

			Sheet sheet = createHeader(sheetName, wb);
			writeData(sheet, ds, drill);

			for(int i = 0 ;i < this.size;i++){
				sheet.autoSizeColumn(i);
			}
			wb.write(os);
			os.flush();

		} catch (IOException e) {
			throw new RuntimeException(e);
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
