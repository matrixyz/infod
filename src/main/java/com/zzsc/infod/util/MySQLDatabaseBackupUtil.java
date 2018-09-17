package com.zzsc.infod.util;

import java.io.*;

public class MySQLDatabaseBackupUtil {
    /**
     * Java代码实现MySQL数据库导出
     *
     * @author GaoHuanjie
     * @param hostIP MySQL数据库所在服务器地址IP
     * @param userName 进入数据库所需要的用户名
     * @param password 进入数据库所需要的密码
     * @param savePath 数据库导出文件保存路径
     * @param fileName 数据库导出文件文件名
     * @param databaseName 要导出的数据库名
     * @return 返回true表示导出成功，否则返回false。
     */
    public static String exportDatabaseTool(String hostIP, String userName, String password, String savePath, String fileName, String databaseName) throws InterruptedException {
        File saveFile = new File(savePath);
        if (!saveFile.exists()) {// 如果目录不存在
            saveFile.mkdirs();// 创建文件夹
        }
        if(!savePath.endsWith(File.separator)){
            savePath = savePath + File.separator;
        }

        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
        try {
            printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(savePath + fileName), "utf8"));
            //String[] cmd=new String[]{"/bin/sh ","-c ","/alidata/server/mysql/bin/mysqldump -uroot  -p"+password+" minas >/usr/2.sql "};
            String exePath="/alidata/server/mysql/bin/";
            if(System.getProperty("os.name").toLowerCase().indexOf("windows")>=0){
                exePath="";
            }
            if(new File(exePath).exists()==false){
                exePath="";
            }
            Process process = Runtime.getRuntime().exec(exePath+"mysqldump -h" + hostIP + " -u" + userName + " -p" + password + " --set-charset=UTF8 " + databaseName);
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), "utf8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while((line = bufferedReader.readLine())!= null){
                printWriter.println(line);
            }
            printWriter.flush();
            inputStreamReader.close();
            printWriter.close();
            if(process.waitFor() == 0){//0 表示线程正常终止。
                String source=savePath+fileName;
                String outPath=savePath+fileName.replace(".sql","")+".zip";
                boolean res=ZipUtils.FileToZipEncryption(source, outPath, "w5t6a_jjTgv3_");
                if (true){
                    FileUtil.delPath(source);
                    return outPath;

                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (printWriter != null) {
                    printWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args){
        try {
            if (null!=exportDatabaseTool("localhost", "root", "2AYCmmGY_", "E:\\backupDatabase", DateUtil.getDay()+".sql", "dt")) {
                System.out.println("数据库成功备份！！！");
            } else {
                System.out.println("数据库备份失败！！！");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
