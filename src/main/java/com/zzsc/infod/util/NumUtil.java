package com.zzsc.infod.util;

public class NumUtil {
    public static int getProgress(int total,int value){
        if(total==0)
            return 0;
        return (int)Math.ceil(value*1.0/total*100);
    }
}
