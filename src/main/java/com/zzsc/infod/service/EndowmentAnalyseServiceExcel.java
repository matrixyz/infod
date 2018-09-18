package com.zzsc.infod.service;

import com.zzsc.infod.model.EndowmentDto;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface EndowmentAnalyseServiceExcel {

    List<EndowmentDto> analyseCityExcel(File  file);
    File[] getFiles(String path);
    Map<String,EndowmentDto> getEndowmentFromRow(File[] file);
    Map<String, EndowmentDto> init(String path);

}
