package com.zzsc.infod.model;
import com.zzsc.infod.util.StringUtil;
public class InfoTitle{

        // 数据标题主键
        private String tid;
        // 信息标题
        private String title;
        // 相关信息描述
        private String des;
        // 数据标题创建时间
        private java.util.Date creTime;
        // 创建数据用户标识
        private int creator;

          public void setTid(String tid) {

                this.tid = StringUtil.filterEmpty(tid);

          }
          public String getTid() {
            return tid;
          }
          public void setTitle(String title) {

                this.title = StringUtil.filterEmpty(title);

          }
          public String getTitle() {
            return title;
          }
          public void setDes(String des) {

                this.des = StringUtil.filterEmpty(des);

          }
          public String getDes() {
            return des;
          }
          public void setCreTime(java.util.Date creTime) {
                 this.creTime = creTime;
          }
          public java.util.Date getCreTime() {
            return creTime;
          }
          public void setCreator(int creator) {
                 this.creator = creator;
          }
          public int getCreator() {
            return creator;
          }
    }