package com.zzsc.infod.model;
import com.zzsc.infod.util.StringUtil;

public class FinanceFeed {



    // 主键
        private int fid;
        // 姓名
        private String name;
        // 身份证号码
        private String cid;
        // 单位名称
        private String orgName;

        public int getFid() {
            return fid;
        }

        public void setFid(int fid) {
            this.fid = fid;
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


    }