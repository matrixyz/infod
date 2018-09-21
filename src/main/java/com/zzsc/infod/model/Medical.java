package com.zzsc.infod.model;
import com.zzsc.infod.util.StringUtil;
public class Medical{

        // 医疗保险主键
        private int mid;
        // 姓名
        private String name;
        // 身份证号码
        private String cid;
        // 单位(地区)名称
        private String areaName;
        // 数据标题
        private int tid;

          public void setMid(int mid) {
                 this.mid = mid;
          }
          public int getMid() {
            return mid;
          }
          public void setName(String name) {

                this.name = StringUtil.filterEmpty(name);

          }
          public String getName() {
            return name;
          }
          public void setCid(String cid) {

                this.cid = StringUtil.filterEmpty(cid);

          }
          public String getCid() {
            return cid;
          }
          public void setAreaName(String areaName) {

                this.areaName = StringUtil.filterEmpty(areaName);

          }
          public String getAreaName() {
            return areaName;
          }
          public void setTid(int tid) {
                 this.tid = tid;
          }
          public int getTid() {
            return tid;
          }
    }