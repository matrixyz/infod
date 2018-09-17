package  com.zzsc.infod.service.impl;
import com.zzsc.infod.mapper.InfoTitleMapper;
import com.zzsc.infod.service.InfoTitleService;
import com.zzsc.infod.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
@Service
public class InfoTitleServiceImpl implements InfoTitleService {

    @Autowired
    private InfoTitleMapper infoTitleMapper;
    public List<InfoTitleDto> list(InfoTitleDto infoTitleDto) {
        return infoTitleMapper.list(infoTitleDto);
    }
    @Override
    public List<Map<String,String>> selectJoin(InfoTitleDto infoTitleDto) {
        return infoTitleMapper.selectJoin( infoTitleDto);
    }
    @Override
    public InfoTitleDto getCondition(InfoTitleDto infoTitleDto) {
        return infoTitleMapper.getCondition(infoTitleDto);
    }
    @Override
    public InfoTitleDto get(int tid) {
        return infoTitleMapper.get(tid);
    }
    @Override
    public int insert(InfoTitleDto infoTitleDto) {
        return infoTitleMapper.insert(infoTitleDto);
    }
    @Override
    public int update(InfoTitleDto infoTitleDto) {
        return infoTitleMapper.update(infoTitleDto);
    }
    @Override
    public int del(int tid) {
        return infoTitleMapper.del(tid);
    }
    }
