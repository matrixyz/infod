package com.zzsc.infod.service;

import com.zzsc.infod.model.AnalyseExcelUploadDto;
import com.zzsc.infod.model.MedicalDto;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface MedicalAnalyseServiceExcel {
    List<MedicalDto> analyseCityExcel(File file);
    List<MedicalDto> analyseVallageExcel(File  file);
    File[] getFiles(String path);
    Map<String,MedicalDto> getMedicalFromRow(File[] file,String type);
    Map<String, MedicalDto> init(String path,String type);
    Map<String, MedicalDto> initMerge(String pathCity,String pathVallage);
    List<AnalyseExcelUploadDto> getAnalyseExcelUploadDtoList(String path  );
}
