package  ${packageNameServiceImpl};
import ${packageNameDao}.${className}Mapper;
import ${packageNameService}.${className}Service;
import ${packageNameModel}.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
@Service
public class ${className}ServiceImpl implements ${className}Service {

    @Autowired
    private ${className}Mapper ${className?uncap_first}Mapper;
    public List<${className}Dto> list(${className}Dto ${className?uncap_first}Dto) {
        return ${className?uncap_first}Mapper.list(${className?uncap_first}Dto);
    }
    @Override
    public List<Map<String,String>> selectJoin(${className}Dto ${className?uncap_first}Dto) {
        return ${className?uncap_first}Mapper.selectJoin( ${className?uncap_first}Dto);
    }
    @Override
    public ${className}Dto getCondition(${className}Dto ${className?uncap_first}Dto) {
        return ${className?uncap_first}Mapper.getCondition(${className?uncap_first}Dto);
    }
    @Override
    public ${className}Dto get(int ${primaryProp}) {
        return ${className?uncap_first}Mapper.get(${primaryProp});
    }
    @Override
    public int insert(${className}Dto ${className?uncap_first}Dto) {
        return ${className?uncap_first}Mapper.insert(${className?uncap_first}Dto);
    }
    @Override
    public int update(${className}Dto ${className?uncap_first}Dto) {
        return ${className?uncap_first}Mapper.update(${className?uncap_first}Dto);
    }
    @Override
    public int del(int ${primaryProp}) {
        return ${className?uncap_first}Mapper.del(${primaryProp});
    }
    }
