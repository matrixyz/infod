package com.zzsc.infod.model;
import com.zzsc.infod.util.StringUtil;
public class Endowment{

        // 养老保险主键
        private int eid;
        // 姓名
        private String name;
        // 身份证号码
        private String cid;
        // 单位名称
        private String orgName;
        // 开户银行
        private String bankName;
        // 开户银行名称
        private String bankSub;
        // 数据标题
        private String tid;

          public void setEid(int eid) {
                 this.eid = eid;
          }
          public int getEid() {
            return eid;
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
          public void setOrgName(String orgName) {

                this.orgName = StringUtil.filterEmpty(orgName);

          }
          public String getOrgName() {
            return orgName;
          }
          public void setBankName(String bankName) {

                this.bankName = StringUtil.filterEmpty(bankName);

          }
          public String getBankName() {
            return bankName;
          }
          public void setBankSub(String bankSub) {

                this.bankSub = StringUtil.filterEmpty(bankSub);

          }
          public String getBankSub() {
            return bankSub;
          }
          public void setTid(String tid) {

                this.tid = StringUtil.filterEmpty(tid);

          }
          public String getTid() {
            return tid;
          }
    }