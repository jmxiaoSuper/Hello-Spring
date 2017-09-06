package com.hello.spring.util;

import com.hello.spring.helper.SpringHelper;
import com.hello.spring.util.export.excel.ExcelColumn;
import com.hello.spring.util.export.excel.ExcelDrill;
import com.hello.spring.util.export.excel.ExcelExportService;
import org.apache.commons.lang3.Validate;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.List;
import java.util.UUID;

/**
 * Created by jingmin.xiao on 2017/6/28.
 */
public class DownLoadUtil {

    public static String COMM_FILE_TEMP_PATH = File.separatorChar + "downLoadFileTempPath" + File.separatorChar;

    public static String UP_LOAD_FILE_TEMP_PATH = File.separator + "upLoadFileTempPath" + File.separator;
    /**
     * 导出excel到临时目录下
     * @param columns
     * 	列定义
     * @param ds
     *  数据源
     * @param drill
     * 	数据钻取接口
     */
    public static String export2Excel(List<ExcelColumn> columns, List<?> ds, ExcelDrill<?> drill){
        Validate.notEmpty(columns,"列定义columns不允许为空！");
        Validate.notEmpty(ds,"数据源ds不允许为空！");

        String title = UUID.randomUUID() + ".xls";
        OutputStream os = DownLoadUtil.getWebFileOutputStream(title);

        ExcelExportService service = new ExcelExportService(columns);
        service.export(title, ds, false, drill, os);

        return title;
    }

    /**
     * 获取临时文件输出流
     * @param fileName
     * 	临时文件名称
     * @return
     */
    public static OutputStream getWebFileOutputStream(String fileName){
        String path = SpringHelper.getServletContext().getRealPath("/");
        String completeFileName = path + DownLoadUtil.COMM_FILE_TEMP_PATH + fileName;
        File file = new File(completeFileName);

        if(!file.getParentFile().exists()){
            file.getParentFile().mkdir();
        }

        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return bos;
    }

    /**
     * 下载临时文件
     * @param os
     * 	输出流
     * @param fileName
     * 	临时文件名称
     * @param isDelete
     * 	下载完之后是否删除
     */
    public static void downLoadFileName(OutputStream os, String fileName, boolean isDelete){
        String path = SpringHelper.getServletContext().getRealPath("/");
        String completeFileName = path + DownLoadUtil.COMM_FILE_TEMP_PATH + fileName;
        File file = new File(completeFileName);

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            bos = new BufferedOutputStream(os);

            byte[] buff = new byte[2048];
            int bytesRead;

            while(-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff,0,bytesRead);
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
                if (isDelete)
                    file.delete();
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
        }

    }
}
