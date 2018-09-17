package ${packageNameService};
import ${packageNameModel}.*;

import java.util.List;
import java.util.Map;

public interface ${className}Service {
    List<${className}Dto> list(${className}Dto ${className?uncap_first}Dto);
    List<Map<String,String>> selectJoin(${className}Dto ${className?uncap_first}Dto);
    ${className}Dto getCondition(${className}Dto ${className?uncap_first}Dto);
    ${className}Dto get(int ${primaryProp});
    int insert(${className}Dto  ${className?uncap_first}Dto);
    int update(${className}Dto  ${className?uncap_first}Dto);
    int del(int ${primaryProp});
    }
