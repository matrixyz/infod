package com.zzsc.infod.service;
import com.zzsc.infod.model.*;

import java.util.List;
import java.util.Map;

public interface EndowmentService {
    List<EndowmentDto> list(EndowmentDto endowmentDto);
    List<Map<String,String>> selectJoin(EndowmentDto endowmentDto);
    EndowmentDto getCondition(EndowmentDto endowmentDto);
    EndowmentDto get(int eid);
    int insert(EndowmentDto  endowmentDto);
    int update(EndowmentDto  endowmentDto);
    int del(int eid);
    }
