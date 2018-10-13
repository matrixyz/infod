package com.zzsc.infod.service;

import com.zzsc.infod.model.AnalyseExcelUploadDto;
import com.zzsc.infod.model.SomeXlsDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Map;

public interface SomeXlsAnalyseServiceExcel {


    List<SomeXlsDto> analyseSomeExcel(File file);
    List<SomeXlsDto> analyseSomeExcelEventmode(File file);
    File[] getFiles(String path);

    Map<String,SomeXlsDto> getSomeXlsFromRow(String type, File[] files);

    Map<String, SomeXlsDto> initByPath(String path, String type);
    Map<String, SomeXlsDto> initMerge(String pathCity, String pathVallage);
    Map<String, SomeXlsDto> initMerge(Map<String, SomeXlsDto> city, Map<String, SomeXlsDto> vallage);
    List<AnalyseExcelUploadDto> getAnalyseExcelUploadDtoList(String path);
    String checkSomeXlsDifExcelFile(ServletContext applications, String appType, String errType, String emptyType);
    int getListSomeXlsDifExcelFile(ServletContext applications, HttpServletResponse response, String appType, String excelFileTitle);

}
