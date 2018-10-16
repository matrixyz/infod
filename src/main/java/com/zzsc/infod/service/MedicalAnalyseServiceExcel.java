package com.zzsc.infod.service;

import com.zzsc.infod.model.AnalyseExcelUploadDto;
import com.zzsc.infod.model.MedicalDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Map;

public interface MedicalAnalyseServiceExcel {
    List<MedicalDto> analyseCityExcel(MultipartFile file);
    List<MedicalDto> analyseVallageExcel(MultipartFile  file);
    List<MedicalDto> analyseCityExcel( File file);
    List<MedicalDto> analyseVallageExcel( File  file);
    File[] getFiles(String path);
    Map<String,MedicalDto> getMedicalFromRow(Map<String,MedicalDto> medicalDtoMap,MultipartFile file,String type);
    Map<String,MedicalDto> getMedicalFromRow( String type,File[] files) throws Exception;
    Map<String, MedicalDto> init( Map<String,MedicalDto>  res,MultipartFile file,String type);
      Map<String, MedicalDto> initByPath(String path,String type) throws Exception;
    Map<String, MedicalDto> initMerge(String pathCity,String pathVallage);
    Map<String, MedicalDto> initMerge( Map<String, MedicalDto> city, Map<String, MedicalDto> vallage);
    List<AnalyseExcelUploadDto> getAnalyseExcelUploadDtoList(String path  );
    String checkMedicalDifExcelFile(ServletContext applications, String appType,String errType,String emptyType);
    int getListMedicalDifExcelFile(ServletContext applications, HttpServletResponse response, String appType, String excelFileTitle);
}
