package com.zzsc.infod.mapper;

import com.zzsc.infod.model.InfoTitle;
import com.zzsc.infod.model.InfoTitleDto;
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
public interface InfoTitleMapper {
    
    @SelectProvider(type=SqlProvider.class,method="selectCondition")
    List<InfoTitleDto> list(InfoTitleDto infoTitleDto);

    List<Map<String,String>> selectJoin(InfoTitleDto infoTitleDto);

    @SelectProvider(type=SqlProvider.class,method="getCondition")
    InfoTitleDto getCondition(InfoTitleDto infoTitleDto);

    @Select("SELECT * FROM info_title WHERE tid=#{tid}")
    InfoTitleDto get(@Param("tid") int tid);

    @UpdateProvider(type=SqlProvider.class,method="updateCondition")
    int update(InfoTitleDto infoTitleDto);

    @InsertProvider(type=SqlProvider.class,method="insertCondition")
    int insert(InfoTitleDto infoTitleDto);

    @Delete("DELETE FROM info_title WHERE tid=#{tid}")
    int del(@Param("tid")int tid);


    class SqlProvider{
        public String selectCondition(InfoTitle InfoTitle){
            return new SQL(){{
                SELECT("*");
                FROM("info_title");


                if(InfoTitle.getTid() !=null){
                    WHERE("tid=#{tid}");
                }
                if(InfoTitle.getTitle() !=null){
                    WHERE("title=#{title}");
                }
                if(InfoTitle.getDes() !=null){
                    WHERE("des=#{des}");
                }
                if(InfoTitle.getCreTime() !=null){
                    WHERE("cre_time=#{creTime}");
                }
                if(InfoTitle.getCreator() !=-1){
                    WHERE("creator=#{creator}");
                }

            }}.toString();
        }
        public String getCondition(InfoTitle InfoTitle){
            return new SQL(){{
                SELECT("*");
                FROM("info_title");
                if(InfoTitle.getTid() !=null){
                    WHERE("tid=#{tid}");
                }
                if(InfoTitle.getTitle() !=null){
                    WHERE("title=#{title}");
                }
                if(InfoTitle.getDes() !=null){
                    WHERE("des=#{des}");
                }
                if(InfoTitle.getCreTime() !=null){
                    WHERE("cre_time=#{creTime}");
                }
                if(InfoTitle.getCreator() !=-1){
                    WHERE("creator=#{creator}");
                }
            }}.toString();
        }
        public String updateCondition(InfoTitle InfoTitle){
            return new SQL(){{
                UPDATE("info_title");

                if(InfoTitle.getTid() !=null){
                    SET("tid=#{tid}");
                }
                if(InfoTitle.getTitle() !=null){
                    SET("title=#{title}");
                }
                if(InfoTitle.getDes() !=null){
                    SET("des=#{des}");
                }
                if(InfoTitle.getCreTime() !=null){
                    SET("cre_time=#{creTime}");
                }
                if(InfoTitle.getCreator() !=-1){
                    SET("creator=#{creator}");
                }

                 WHERE("tid=#{tid}");


            }}.toString();
        }
        public String insertCondition(InfoTitle InfoTitle){
            return new SQL(){{
                INSERT_INTO("info_title");

                if(InfoTitle.getTid() !=null){
                    SET("tid=#{tid}");
                    VALUES("tid","#{tid}");
                }
                if(InfoTitle.getTitle() !=null){
                    SET("title=#{title}");
                    VALUES("title","#{title}");
                }
                if(InfoTitle.getDes() !=null){
                    SET("des=#{des}");
                    VALUES("des","#{des}");
                }
                if(InfoTitle.getCreTime() !=null){
                    SET("cre_time=#{creTime}");
                    VALUES("cre_time","#{creTime}");
                }
                if(InfoTitle.getCreator() !=-1){
                    SET("creator=#{creator}");
                    VALUES("creator","#{creator}");
                }

            }}.toString();
        }
    }




    }

