package com.zzsc.infod.service;
import com.zzsc.infod.model.*;

import java.util.List;
import java.util.Map;

public interface MedicalService {
    List<MedicalDto> list(MedicalDto medicalDto);
    List<Map<String,String>> selectJoin(MedicalDto medicalDto);
    MedicalDto getCondition(MedicalDto medicalDto);
    MedicalDto get(int mid);
    int insert(MedicalDto  medicalDto);
    int update(MedicalDto  medicalDto);
    int del(int mid);
    }
