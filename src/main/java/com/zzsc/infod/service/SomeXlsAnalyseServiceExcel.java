package com.zzsc.infod.service;

import com.zzsc.infod.model.AnalyseExcelUploadDto;
import com.zzsc.infod.model.FinanceFeedDto;
import com.zzsc.infod.model.SomeXlsDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Map;

public interface SomeXlsAnalyseServiceExcel {


    List<SomeXlsDto> analyseSomeExcel(File file,String[] cols);
    List<SomeXlsDto> analyseSomeExcelEventmode(File file);
    File[] getFiles(String path);

    Map<String,SomeXlsDto> getSomeXlsFromRow(String type, File[] files,String[] cols) throws Exception;

    Map<String, SomeXlsDto> initByPath(String path, String type,String[] cols) throws Exception;
    //Map<String, SomeXlsDto> initMerge(String pathCity, String pathVallage);
    Map<String, FinanceFeedDto> initMerge(Map<String, FinanceFeedDto> city, Map<String, SomeXlsDto> vallage);
    List<AnalyseExcelUploadDto> getAnalyseExcelUploadDtoList(String path);
    String checkSomeXlsDifExcelFile(ServletContext applications, String appType, String errType, String emptyType);
    String checkSomeXlsDifExcelFileAll(ServletContext applications, String appType, String errType, String emptyType);
    int getListSomeXlsDifExcelFile(ServletContext applications, HttpServletResponse response, String appType, String excelFileTitle);
    int getListSomeXlsDifExcelFileAll(ServletContext applications, HttpServletResponse response, String appType, String excelFileTitle);

}
