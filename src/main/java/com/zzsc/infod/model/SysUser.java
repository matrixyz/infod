package com.zzsc.infod.model;


import com.zzsc.infod.util.StringUtil;

public class SysUser {

        // 用户ID
        private String userId;
        // 姓名
        private String userName;
        // 用户电话，也是登录用户名，必须是手机号
        private long userPhone;
        // 用户角色ID
        private String userRoleId;
        // 身份证号码
        private String userIdno;
        // 登录密码
        private String userPwd;
        // 工作电话号码
        private String userTel;

          public void setUserId(String userId) {
            this.userId = userId;
          }
          public String getUserId() {
            return userId;
          }
          public void setUserName(String userName) {

              this.userName = StringUtil.filterEmpty(userName);
          }
          public String getUserName() {
            return userName;
          }
          public void setUserPhone(long userPhone) {
            this.userPhone = userPhone;
          }
          public long getUserPhone() {
            return userPhone;
          }
          public void setUserRoleId(String userRoleId) {
            this.userRoleId = userRoleId;
          }
          public String getUserRoleId() {
            return userRoleId;
          }
          public void setUserIdno(String userIdno) {
            this.userIdno = userIdno;
          }
          public String getUserIdno() {
            return userIdno;
          }
          public void setUserPwd(String userPwd) {
            this.userPwd = userPwd;
          }
          public String getUserPwd() {
            return userPwd;
          }
          public void setUserTel(String userTel) {
            this.userTel = userTel;
          }
          public String getUserTel() {
            return userTel;
          }
    }