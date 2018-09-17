package com.zzsc.infod.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JDBCUtil {

    private static String jdbcUrl = "";
    private static  String userName = "";//
    private static  String password = "";//JKpF1Mkd3sf
    private static  String jdbcDriver = "";
   static StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
    static {
    Properties prop = new Properties();
    try {
        prop.load( JDBCUtil.class.getResourceAsStream("/application.properties"));
        jdbcUrl=( prop.getProperty("spring.datasource.url"));
        userName=( prop.getProperty("spring.datasource.username"));
        password=( prop.getProperty("spring.datasource.password"));
        jdbcDriver=( prop.getProperty("spring.datasource.driver-class-name"));
        encryptor.setPassword(prop.getProperty("jasypt.encryptor.password"));
        password= encryptor.decrypt("9drT0VyJQ9VW68ZbpNuXIhUjuR4BARKf");
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    public static Connection getConn() throws Exception {
        Class.forName(jdbcDriver);
        return DriverManager.getConnection(jdbcUrl,userName,password);

    }
    public static  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void close(Connection conn,PreparedStatement ps,ResultSet rs){
        try {
            if(rs!=null){


                rs.close();

                rs=null;
            }
            if(ps!=null){
                ps.close();
                ps=null;
            }
            if(conn!=null){
                conn.close();
                conn=null;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void close(Connection conn){
        try {

            if(conn!=null){
                conn.close();
                conn=null;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new JDBCUtil();

         List<BeanCommon> ls=getComment("g_content","dt");

        for(BeanCommon bs : ls){
            System.out.println(bs.getName());
        }
    }
    public static List<BeanCommon> getComment(String table_name, String db_name){

        Connection con=null;
        ResultSet rs=null;
        PreparedStatement ps=null;

        String sql="select COLUMN_NAME,DATA_TYPE,COLUMN_COMMENT,COLUMN_DEFAULT from " +
                "information_schema.COLUMNS where table_name = '"+table_name+"' and table_schema = '"+db_name+"'";
        List<BeanCommon> tableInfo=new ArrayList<BeanCommon>();
        try {
            con= getConn();
            ps=con.prepareStatement(sql);

            rs=ps.executeQuery();
            while(rs.next()){
                BeanCommon bc=new BeanCommon();
                bc.setName(rs.getString("COLUMN_NAME"));
                bc.setType(rs.getString("DATA_TYPE"));
                bc.setComment(rs.getString("COLUMN_COMMENT"));
                bc.setDefaults(rs.getString("COLUMN_DEFAULT"));
                tableInfo.add(bc);

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(con,ps,rs);
        }
        return tableInfo;
    }
    public static String getPrimaryColumn(String table_name, String db_name){

        Connection con=null;
        ResultSet rs=null;
        PreparedStatement ps=null;
        String res=null;
        String sql="SELECT k.column_name\n" +
                "\n" +
                "FROM information_schema.table_constraints t\n" +
                "\n" +
                "JOIN information_schema.key_column_usage k\n" +
                "\n" +
                "USING (constraint_name,table_schema,table_name)\n" +
                "\n" +
                "WHERE t.constraint_type='PRIMARY KEY'\n" +
                "\n" +
                "  AND t.table_schema='"+db_name+"'\n" +
                "\n" +
                "  AND t.table_name='"+table_name+"'";

        try {
            con= getConn();
            ps=con.prepareStatement(sql);

            rs=ps.executeQuery();
            while(rs.next()){
               res=rs.getString("column_name");

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(con,ps,rs);
        }
        return res;
    }
    public static List<String> getTables( String db_name){

        Connection con=null;
        ResultSet rs=null;
        PreparedStatement ps=null;

        String sql="select table_name from " +
                "information_schema.TABLES  where   table_schema = '"+db_name+"'";
        List<String> tableInfo=new ArrayList<String>();
        try {
            con= getConn();
            ps=con.prepareStatement(sql);

            rs=ps.executeQuery();
            while(rs.next()){

                tableInfo.add(rs.getString("table_name"));

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(con,ps,rs);
        }
        return tableInfo;
    }
//批量修改 的工具
    public static List<List<String >> getQuery( ){

        Connection con=null;
        ResultSet rs=null;
        PreparedStatement ps=null;

        String sql="select *  from  g_content  " ;
        List<List<String >> tableInfo=new ArrayList<List<String >>();
        try {
            con= getConn();
            ps=con.prepareStatement(sql);

            rs=ps.executeQuery();
            while(rs.next()){
                List<String> bc = new ArrayList<String>();
                bc.add(rs.getString("cust_phone"));
                String sqls="inert into g_content";

                tableInfo.add(bc);
                Thread.sleep(10);
                System.out.println(sqls);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(con,ps,rs);
        }
        return tableInfo;
    }
}
