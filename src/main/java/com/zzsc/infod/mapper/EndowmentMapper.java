package com.zzsc.infod.mapper;

import com.zzsc.infod.model.Endowment;
import com.zzsc.infod.model.EndowmentDto;
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
public interface EndowmentMapper {
    
    @SelectProvider(type=SqlProvider.class,method="selectCondition")
    List<EndowmentDto> list(EndowmentDto endowmentDto);

    List<Map<String,String>> selectJoin(EndowmentDto endowmentDto);

    @SelectProvider(type=SqlProvider.class,method="getCondition")
    EndowmentDto getCondition(EndowmentDto endowmentDto);

    @Select("SELECT * FROM endowment WHERE eid=#{eid}")
    EndowmentDto get(@Param("eid") int eid);

    @UpdateProvider(type=SqlProvider.class,method="updateCondition")
    int update(EndowmentDto endowmentDto);

    @InsertProvider(type=SqlProvider.class,method="insertCondition")
    int insert(EndowmentDto endowmentDto);

    @Delete("DELETE FROM endowment WHERE eid=#{eid}")
    int del(@Param("eid")int eid);


    class SqlProvider{
        public String selectCondition(Endowment Endowment){
            return new SQL(){{
                SELECT("*");
                FROM("endowment");


                if(Endowment.getEid() !=0){
                    WHERE("eid=#{eid}");
                }
                if(Endowment.getName() !=null){
                    WHERE("name=#{name}");
                }
                if(Endowment.getCid() !=null){
                    WHERE("cid=#{cid}");
                }
                if(Endowment.getOrgName() !=null){
                    WHERE("org_name=#{orgName}");
                }
                if(Endowment.getBankName() !=null){
                    WHERE("bank_name=#{bankName}");
                }
                if(Endowment.getBankSub() !=null){
                    WHERE("bank_sub=#{bankSub}");
                }
                if(Endowment.getTid() !=null){
                    WHERE("tid=#{tid}");
                }

            }}.toString();
        }
        public String getCondition(Endowment Endowment){
            return new SQL(){{
                SELECT("*");
                FROM("endowment");
                if(Endowment.getEid() !=-1){
                    WHERE("eid=#{eid}");
                }
                if(Endowment.getName() !=null){
                    WHERE("name=#{name}");
                }
                if(Endowment.getCid() !=null){
                    WHERE("cid=#{cid}");
                }
                if(Endowment.getOrgName() !=null){
                    WHERE("org_name=#{orgName}");
                }
                if(Endowment.getBankName() !=null){
                    WHERE("bank_name=#{bankName}");
                }
                if(Endowment.getBankSub() !=null){
                    WHERE("bank_sub=#{bankSub}");
                }
                if(Endowment.getTid() !=null){
                    WHERE("tid=#{tid}");
                }
            }}.toString();
        }
        public String updateCondition(Endowment Endowment){
            return new SQL(){{
                UPDATE("endowment");

                if(Endowment.getEid() !=-1){
                    SET("eid=#{eid}");
                }
                if(Endowment.getName() !=null){
                    SET("name=#{name}");
                }
                if(Endowment.getCid() !=null){
                    SET("cid=#{cid}");
                }
                if(Endowment.getOrgName() !=null){
                    SET("org_name=#{orgName}");
                }
                if(Endowment.getBankName() !=null){
                    SET("bank_name=#{bankName}");
                }
                if(Endowment.getBankSub() !=null){
                    SET("bank_sub=#{bankSub}");
                }
                if(Endowment.getTid() !=null){
                    SET("tid=#{tid}");
                }

                 WHERE("eid=#{eid}");


            }}.toString();
        }
        public String insertCondition(Endowment Endowment){
            return new SQL(){{
                INSERT_INTO("endowment");

                if(Endowment.getEid() !=-1){
                    SET("eid=#{eid}");
                    VALUES("eid","#{eid}");
                }
                if(Endowment.getName() !=null){
                    SET("name=#{name}");
                    VALUES("name","#{name}");
                }
                if(Endowment.getCid() !=null){
                    SET("cid=#{cid}");
                    VALUES("cid","#{cid}");
                }
                if(Endowment.getOrgName() !=null){
                    SET("org_name=#{orgName}");
                    VALUES("org_name","#{orgName}");
                }
                if(Endowment.getBankName() !=null){
                    SET("bank_name=#{bankName}");
                    VALUES("bank_name","#{bankName}");
                }
                if(Endowment.getBankSub() !=null){
                    SET("bank_sub=#{bankSub}");
                    VALUES("bank_sub","#{bankSub}");
                }
                if(Endowment.getTid() !=null){
                    SET("tid=#{tid}");
                    VALUES("tid","#{tid}");
                }

            }}.toString();
        }
    }




    }

