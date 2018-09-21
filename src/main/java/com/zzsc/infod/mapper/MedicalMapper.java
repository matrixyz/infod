package com.zzsc.infod.mapper;

import com.zzsc.infod.model.Medical;
import com.zzsc.infod.model.MedicalDto;
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
public interface MedicalMapper {
    
    @SelectProvider(type=SqlProvider.class,method="selectCondition")
    List<MedicalDto> list(MedicalDto medicalDto);

    List<Map<String,String>> selectJoin(MedicalDto medicalDto);

    @SelectProvider(type=SqlProvider.class,method="getCondition")
    MedicalDto getCondition(MedicalDto medicalDto);

    @Select("SELECT * FROM medical WHERE mid=#{mid}")
    MedicalDto get(@Param("mid") int mid);

    @UpdateProvider(type=SqlProvider.class,method="updateCondition")
    int update(MedicalDto medicalDto);

    @InsertProvider(type=SqlProvider.class,method="insertCondition")
    int insert(MedicalDto medicalDto);

    @Delete("DELETE FROM medical WHERE mid=#{mid}")
    int del(@Param("mid")int mid);


    class SqlProvider{
        public String selectCondition(Medical Medical){
            return new SQL(){{
                SELECT("*");
                FROM("medical");


                if(Medical.getMid() !=-1){
                    WHERE("mid=#{mid}");
                }
                if(Medical.getName() !=null){
                    WHERE("name=#{name}");
                }
                if(Medical.getCid() !=null){
                    WHERE("cid=#{cid}");
                }
                if(Medical.getAreaName() !=null){
                    WHERE("area_name=#{areaName}");
                }
                if(Medical.getTid() !=-1){
                    WHERE("tid=#{tid}");
                }

            }}.toString();
        }
        public String getCondition(Medical Medical){
            return new SQL(){{
                SELECT("*");
                FROM("medical");
                if(Medical.getMid() !=-1){
                    WHERE("mid=#{mid}");
                }
                if(Medical.getName() !=null){
                    WHERE("name=#{name}");
                }
                if(Medical.getCid() !=null){
                    WHERE("cid=#{cid}");
                }
                if(Medical.getAreaName() !=null){
                    WHERE("area_name=#{areaName}");
                }
                if(Medical.getTid() !=-1){
                    WHERE("tid=#{tid}");
                }
            }}.toString();
        }
        public String updateCondition(Medical Medical){
            return new SQL(){{
                UPDATE("medical");

                if(Medical.getMid() !=-1){
                    SET("mid=#{mid}");
                }
                if(Medical.getName() !=null){
                    SET("name=#{name}");
                }
                if(Medical.getCid() !=null){
                    SET("cid=#{cid}");
                }
                if(Medical.getAreaName() !=null){
                    SET("area_name=#{areaName}");
                }
                if(Medical.getTid() !=-1){
                    SET("tid=#{tid}");
                }

                 WHERE("mid=#{mid}");


            }}.toString();
        }
        public String insertCondition(Medical Medical){
            return new SQL(){{
                INSERT_INTO("medical");

                if(Medical.getMid() !=-1){
                    SET("mid=#{mid}");
                    VALUES("mid","#{mid}");
                }
                if(Medical.getName() !=null){
                    SET("name=#{name}");
                    VALUES("name","#{name}");
                }
                if(Medical.getCid() !=null){
                    SET("cid=#{cid}");
                    VALUES("cid","#{cid}");
                }
                if(Medical.getAreaName() !=null){
                    SET("area_name=#{areaName}");
                    VALUES("area_name","#{areaName}");
                }
                if(Medical.getTid() !=-1){
                    SET("tid=#{tid}");
                    VALUES("tid","#{tid}");
                }

            }}.toString();
        }
    }




    }

