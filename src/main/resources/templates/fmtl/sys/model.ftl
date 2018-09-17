package ${packageNameModel};
import ${packageName}.util.StringUtil;
public class ${className}{

    <#-- 循环类型及属性 -->
    <#list attrs as attr>
        // ${attr.comment}
        private ${attr.type} ${attr.name};
    </#list>

    <#-- 循环生成set get方法 -->
    <#list attrs as attr>
          public void set${attr.name?cap_first}(${attr.type} ${attr.name}) {
            <#if (attr.type=="String")>

                this.${attr.name} = StringUtil.filterEmpty(${attr.name});

            <#else>
                 this.${attr.name} = ${attr.name};
            </#if>
          }
          public ${attr.type} get${attr.name?cap_first}() {
            return ${attr.name};
          }
    </#list>
    }