package com.zzsc.infod.service;

import com.zzsc.infod.model.AnalyseExcelUploadDto;
import com.zzsc.infod.model.FinanceFeedDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Map;

public interface FinanceFeedAnalyseServiceExcel {

    List<FinanceFeedDto> analyseCityExcel(MultipartFile file);
    List<FinanceFeedDto> analyseVallageExcel(MultipartFile file);
    List<FinanceFeedDto> analyseCityExcel(File file);
    List<FinanceFeedDto> analyseVallageExcel(File file);
    File[] getFiles(String path);
    Map<String,FinanceFeedDto> getFinanceFeedFromRow(Map<String, FinanceFeedDto> FinanceFeedDtoMap, MultipartFile file, String type);
    Map<String,FinanceFeedDto> getFinanceFeedFromRow(String type, File[] files) throws Exception;
    Map<String, FinanceFeedDto> init(Map<String, FinanceFeedDto> res, MultipartFile file, String type);
    Map<String, FinanceFeedDto> initByPath(String path, String type) throws Exception;
    //Map<String, FinanceFeedDto> initMerge(String pathCity, String pathVallage);
    Map<String, FinanceFeedDto> initMerge(Map<String, FinanceFeedDto> city, Map<String, FinanceFeedDto> vallage);
    List<AnalyseExcelUploadDto> getAnalyseExcelUploadDtoList(String path);
    String checkFinanceFeedDifExcelFile(ServletContext applications, String appType, String errType, String emptyType);
    int getListFinanceFeedDifExcelFile(ServletContext applications, HttpServletResponse response, String appType, String excelFileTitle);

}
