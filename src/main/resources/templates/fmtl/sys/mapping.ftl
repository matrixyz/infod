<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${packageNameDao}">

    <select id="selectJoin" resultType="${packageNameModel}.${className}Dto" parameterType="${packageNameModel}.${className}Dto"  >
        SELECT
          <#list attrs as attr>
              ${attr.column}<#if attr_has_next>,</#if>
          </#list>
        FROM
        ${tableName}
        <trim prefix="WHERE" prefixOverrides="AND|OR ">
        <#list attrs as attr>
           <if test="${attr.name}!=<#if (attr.type == "java.util.Date"|| attr.type == "String" ||attr.type?index_of("byte")!=-1)>null<#else>0</#if>">AND ${attr.column}=${r'#{'}${attr.name}${r'}'}</if>
        </#list>
        </trim>
    </select>
</mapper>