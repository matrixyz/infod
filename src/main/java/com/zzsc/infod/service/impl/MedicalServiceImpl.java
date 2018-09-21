package  com.zzsc.infod.service.impl;
import com.zzsc.infod.mapper.MedicalMapper;
import com.zzsc.infod.service.MedicalService;
import com.zzsc.infod.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
@Service
public class MedicalServiceImpl implements MedicalService {

    @Autowired
    private MedicalMapper medicalMapper;
    public List<MedicalDto> list(MedicalDto medicalDto) {
        return medicalMapper.list(medicalDto);
    }
    @Override
    public List<Map<String,String>> selectJoin(MedicalDto medicalDto) {
        return medicalMapper.selectJoin( medicalDto);
    }
    @Override
    public MedicalDto getCondition(MedicalDto medicalDto) {
        return medicalMapper.getCondition(medicalDto);
    }
    @Override
    public MedicalDto get(int mid) {
        return medicalMapper.get(mid);
    }
    @Override
    public int insert(MedicalDto medicalDto) {
        return medicalMapper.insert(medicalDto);
    }
    @Override
    public int update(MedicalDto medicalDto) {
        return medicalMapper.update(medicalDto);
    }
    @Override
    public int del(int mid) {
        return medicalMapper.del(mid);
    }
    }
