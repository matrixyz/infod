package com.zzsc.infod.service;

import com.zzsc.infod.model.AnalyseExcelUploadDto;
import com.zzsc.infod.model.MedicalDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface MedicalAnalyseServiceExcel {
    List<MedicalDto> analyseCityExcel(MultipartFile file);
    List<MedicalDto> analyseVallageExcel(MultipartFile  file);
    File[] getFiles(String path);
    Map<String,MedicalDto> getMedicalFromRow(Map<String,MedicalDto> medicalDtoMap,MultipartFile file,String type);
    Map<String, MedicalDto> init( Map<String,MedicalDto>  res,MultipartFile file,String type);
    //Map<String, MedicalDto> initMerge(String pathCity,String pathVallage);
    List<AnalyseExcelUploadDto> getAnalyseExcelUploadDtoList(String path  );
}
