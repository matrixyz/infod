package com.zzsc.infod.service;
import com.zzsc.infod.model.*;

import java.util.List;
import java.util.Map;

public interface InfoTitleService {
    List<InfoTitleDto> list(InfoTitleDto infoTitleDto);
    List<Map<String,String>> selectJoin(InfoTitleDto infoTitleDto);
    InfoTitleDto getCondition(InfoTitleDto infoTitleDto);
    InfoTitleDto get(int tid);
    int insert(InfoTitleDto  infoTitleDto);
    int update(InfoTitleDto  infoTitleDto);
    int del(int tid);
    }
