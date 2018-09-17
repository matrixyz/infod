package ${packageName};
import ${packageNameService}.${className}Service;
import ${packageNameModel}.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.validation.BindingResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import ${packageNameUtil}.UuidUtil;
@Controller
@EnableAutoConfiguration
@RequestMapping("/${className}")
public class ${className}Controller {
    @Autowired
    private ${className}Service ${className}Service;


    @RequestMapping(value="",method= RequestMethod.GET )
    public ${className} get(Model model,${className}Dto ${className?uncap_first} ){
        ${className} ${className?uncap_first}Temp = ${className}Service.getCondition(${className?uncap_first}) ;
        if (  ${className?uncap_first}Temp !=null){
            return  ${className?uncap_first}Temp;
        }

        return null;
    }


    @RequestMapping(value="/list",method= RequestMethod.GET )
    public String getList( ${className}Dto ${className?uncap_first}Dto  ){

        if (${className?uncap_first}Dto.getPage() == null||"".equals(${className?uncap_first}Dto.getPage())) {
            ${className?uncap_first}Dto.setPage("1");
        }
        List<${className}Dto> list=${className}Service.list(${className?uncap_first}Dto);
        return "adm/${className }-list";
    }

    @RequestMapping(value="/addPrepared",method= RequestMethod.GET )
    public String addPrepared(Model model,${className}Dto ${className?uncap_first}Dto ){
        ${className?uncap_first}Dto.set${primaryProp?cap_first}(UuidUtil.get16UUID());

        model.addAttribute("submitType", "POST");
        model.addAttribute("${className}Dto", ${className?uncap_first}Dto);
        return "adm/${className }-form";
    }

    @ResponseBody
    @RequestMapping(value="",method= RequestMethod.POST )
    public String post(@RequestBody ${className}Dto ${className?uncap_first} ){
        int res=${className}Service.insert(${className?uncap_first});
        if (res>0){
            return "添加信息成功!";
        }else {
            return "添加信息失败!";
        }
    }
    @ResponseBody
    @RequestMapping(value="",method= RequestMethod.PUT )
    public String put(@RequestBody ${className}Dto ${className?uncap_first} ){
        int res=${className}Service.update(${className?uncap_first});
        if (res>0){
        return "修改信息成功!";
        }else {
        return "修改信息失败!";
        }
    }



    public static void main(String argv[]){
        SpringApplication.run(${className}Controller.class,argv);
    }

}
