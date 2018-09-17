package ${packageNameDao};

import ${packageNameModel}.${className};
import ${packageNameModel}.${className}Dto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
@Mapper
@Repository
public interface ${className}Mapper {
    
    @SelectProvider(type=SqlProvider.class,method="selectCondition")
    List<${className}Dto> list(${className}Dto ${className?uncap_first}Dto);

    List<Map<String,String>> selectJoin(${className}Dto ${className?uncap_first}Dto);

    @SelectProvider(type=SqlProvider.class,method="getCondition")
    ${className}Dto getCondition(${className}Dto ${className?uncap_first}Dto);

    @Select("SELECT * FROM ${tableName} WHERE ${primaryColumn}=${r'#{'}${primaryProp}${r'}'}")
    ${className}Dto get(@Param("${primaryProp}") int ${primaryProp});

    @UpdateProvider(type=SqlProvider.class,method="updateCondition")
    int update(${className}Dto ${className?uncap_first}Dto);

    @InsertProvider(type=SqlProvider.class,method="insertCondition")
    int insert(${className}Dto ${className?uncap_first}Dto);

    @Delete("DELETE FROM ${tableName} WHERE ${primaryColumn}=${r'#{'}${primaryProp}${r'}'}")
    int del(@Param("${primaryProp}")int ${primaryProp});


    class SqlProvider{
        public String selectCondition(${className} ${className}){
            return new SQL(){{
                SELECT("*");
                FROM("${tableName}");


                <#list attrs as attr>
                if(${className}.get${attr.name?cap_first}() !=<#if (attr.type == "java.util.Date"|| attr.type == "String"||attr.type?index_of("byte")!=-1)>null<#else>-1</#if>){
                    WHERE("${attr.column}=${r'#{'}${attr.name}${r'}'}");
                }
                </#list>

            }}.toString();
        }
        public String getCondition(${className} ${className}){
            return new SQL(){{
                SELECT("*");
                FROM("${tableName}");
                <#list attrs as attr>
                if(${className}.get${attr.name?cap_first}() !=<#if (attr.type == "java.util.Date"|| attr.type == "String"||attr.type?index_of("byte")!=-1)>null<#else>-1</#if>){
                    WHERE("${attr.column}=${r'#{'}${attr.name}${r'}'}");
                }
                </#list>
            }}.toString();
        }
        public String updateCondition(${className} ${className}){
            return new SQL(){{
                UPDATE("${tableName}");

                <#list attrs as attr>
                if(${className}.get${attr.name?cap_first}() !=<#if (attr.type == "java.util.Date"|| attr.type == "String"||attr.type?index_of("byte")!=-1)>null<#else>-1</#if>){
                    SET("${attr.column}=${r'#{'}${attr.name}${r'}'}");
                }
                </#list>

                 WHERE("${primaryColumn}=${r'#{'}${primaryProp}${r'}'}");


            }}.toString();
        }
        public String insertCondition(${className} ${className}){
            return new SQL(){{
                INSERT_INTO("${tableName}");

                <#list attrs as attr>
                if(${className}.get${attr.name?cap_first}() !=<#if (attr.type == "java.util.Date"|| attr.type == "String"||attr.type?index_of("byte")!=-1)>null<#else>-1</#if>){
                    SET("${attr.column}=${r'#{'}${attr.name}${r'}'}");
                    VALUES("${attr.column}","${r'#{'}${attr.name}${r'}'}");
                }
                </#list>

            }}.toString();
        }
    }




    }

