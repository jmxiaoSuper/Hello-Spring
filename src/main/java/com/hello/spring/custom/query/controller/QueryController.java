package com.hello.spring.custom.query.controller;

import com.hello.spring.common.CommonResponse;
import com.hello.spring.custom.query.bean.GridDataBean;
import com.hello.spring.custom.query.common.QueryTypeEnum;
import com.hello.spring.custom.query.service.CustomQueryService;
import com.hello.spring.helper.SpringHelper;
import com.hello.spring.util.DownLoadUtil;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by jingmin.xiao on 2017/6/28.
 */
@Controller
@RequestMapping("queryAction")
public class QueryController {

    @Autowired
    private CustomQueryService queryService;

    @ResponseBody
    @RequestMapping("queryMT")
    public Object query(@RequestParam Map<String, Object> params, HttpServletRequest request){
        Object sqlPath =  params.get("sqlPath");
        Object queryType = params.get("queryType");

        try {
            Validate.notNull(sqlPath, "sqlPath 不能为空");
            Validate.notNull(queryType, "queryType 不能为空");

            GridDataBean data = queryService.query(QueryTypeEnum.getType(queryType.toString()), sqlPath.toString(), params);

            return data;
        } catch (Exception e) {
            return new CommonResponse(false, "message", e.getMessage());
        }
    }
    /**
     * 下载Excel文件到临时目录下
     * @param params
     * @param request
     */
    @ResponseBody
    @RequestMapping("export4Xls")
    public CommonResponse export4Xls(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        Object sqlPath =  params.get("sqlPath");
        Object queryType = params.get("queryType");

        try {
            Validate.notNull(sqlPath, "sqlPath 不能为空");
            Validate.notNull(queryType, "queryType 不能为空");

            GridDataBean data = queryService.query(QueryTypeEnum.valueOf(queryType.toString()), sqlPath.toString(), params);

            String fileName = DownLoadUtil.export2Excel(null, data.getItems(), null);
            return new CommonResponse(true, "message", fileName);
        } catch (Exception e) {
            return new CommonResponse(false, "message", e.getMessage());
        }
    }


    /**
     * 从临时目录下载文件到浏览器
     * @param response
     * @throws IOException
     */
    @RequestMapping("downLoadFile")
    public void downLoadFile(@RequestParam("title") String title, @RequestParam("downLoadFileName") String downLoadFileName,
                             HttpServletResponse response)throws IOException {
        response.setContentType("application/msexcel");
        response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(title, "UTF-8") + ".xls");

        DownLoadUtil.downLoadFileName(response.getOutputStream(), downLoadFileName, true);
    }

    /**
     * 上传文件到临时目录
     * @param uploadFile
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("uploadFile")
    public CommonResponse uploadFile(@RequestParam("upFile")CommonsMultipartFile uploadFile, HttpServletRequest request){
        String path = SpringHelper.getServletContext().getRealPath("/");
        String completeFileName = path + DownLoadUtil.UP_LOAD_FILE_TEMP_PATH + uploadFile.getOriginalFilename();

        try{
            File file = new File(completeFileName);
            DownLoadUtil.writeFile(uploadFile.getInputStream(), file);
            return new CommonResponse(true);

        }catch (Exception e) {
            return new CommonResponse(false, "errorMsg", e.getMessage());
        }
    }
}
