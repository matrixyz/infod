package com.zzsc.infod.service;

import com.zzsc.infod.model.AnalyseExcelUploadDto;
import com.zzsc.infod.model.EndowmentDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Map;

public interface EndowmentAnalyseServiceExcel {

   /* List<EndowmentDto> analyseCityExcel(MultipartFile file);
    List<EndowmentDto> analyseVallageExcel(MultipartFile  file);*/
    List<EndowmentDto> analyseCityExcel( File file);
    List<EndowmentDto> analyseVallageExcel( File  file);
    File[] getFiles(String path);
    /*Map<String,EndowmentDto> getEndowmentFromRow(Map<String,EndowmentDto> EndowmentDtoMap,MultipartFile file,String type);*/
    Map<String,EndowmentDto> getEndowmentFromRow( String type,File[] files) throws Exception;
    /*Map<String, EndowmentDto> init( Map<String,EndowmentDto>  res,MultipartFile file,String type);*/
    Map<String, EndowmentDto> initByPath(String path,String type) throws Exception;
    Map<String, EndowmentDto> initMerge(String pathCity,String pathVallage);
    Map<String, EndowmentDto> initMerge( Map<String, EndowmentDto> city, Map<String, EndowmentDto> vallage);
    List<AnalyseExcelUploadDto> getAnalyseExcelUploadDtoList(String path  );
    String checkEndowmentDifExcelFile(ServletContext applications, String appType, String errType, String emptyType);
    int getListEndowmentDifExcelFile(ServletContext applications, HttpServletResponse response, String appType, String excelFileTitle);

}
