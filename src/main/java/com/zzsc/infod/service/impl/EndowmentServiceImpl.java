package  com.zzsc.infod.service.impl;
import com.zzsc.infod.mapper.EndowmentMapper;
import com.zzsc.infod.service.EndowmentService;
import com.zzsc.infod.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
@Service
public class EndowmentServiceImpl implements EndowmentService {

    @Autowired
    private EndowmentMapper endowmentMapper;
    public List<EndowmentDto> list(EndowmentDto endowmentDto) {
        return endowmentMapper.list(endowmentDto);
    }
    @Override
    public List<Map<String,String>> selectJoin(EndowmentDto endowmentDto) {
        return endowmentMapper.selectJoin( endowmentDto);
    }
    @Override
    public EndowmentDto getCondition(EndowmentDto endowmentDto) {
        return endowmentMapper.getCondition(endowmentDto);
    }
    @Override
    public EndowmentDto get(int eid) {
        return endowmentMapper.get(eid);
    }
    @Override
    public int insert(EndowmentDto endowmentDto) {
        return endowmentMapper.insert(endowmentDto);
    }
    @Override
    public int update(EndowmentDto endowmentDto) {
        return endowmentMapper.update(endowmentDto);
    }
    @Override
    public int del(int eid) {
        return endowmentMapper.del(eid);
    }
    }
