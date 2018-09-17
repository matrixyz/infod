package com.zzsc.infod.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	private final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

	private final static SimpleDateFormat sdfDay = new SimpleDateFormat(
			"yyyy-MM-dd");
	
	private final static SimpleDateFormat sdfDays = new SimpleDateFormat(
	"yyyyMMdd");
	private final static SimpleDateFormat sdfDayTime = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	private final static SimpleDateFormat sdfTime = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * 获取YYYY格式
	 * 
	 * @return
	 */
	public static String getYear() {
		return sdfYear.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD格式
	 * 
	 * @return
	 */
	public static String getDay() {
		return sdfDay.format(new Date());
	}
	public static String getDay(String date) {
		try {
			return 	sdfDay.format(sdfTime.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return  null;
	}

	/**
	 * 获取YYYYMMDD格式
	 * 
	 * @return
	 */
	public static String getDays(){
		return sdfDays.format(new Date());
	}

	/**
	 * 获取20150505112030格式
	 * 
	 * @return
	 */
	public static String getDayTime(){
		return sdfDayTime.format(new Date());
	}
	
	
	/**
	 * 获取YYYY-MM-DD HH:mm:ss格式
	 * 
	 * @return
	 */
	public static String getTime() {
		return sdfTime.format(new Date());
	}
	/**
	 * 两个日期比较大小 date1在date2之前返回true
	 * 
	 * @return
	 */
	public static Boolean getCompare(Date date1,Date date2) {
	
	return date1.before(date2);
	}

	/**
	* @Title: compareDate
	* @Description: TODO(日期比较，如果s>=e 返回true 否则返回false)
	* @param s
	* @param e
	* @return boolean  
	* @throws
	* @author luguosui
	 */
	public static boolean compareDate(String s, String e) {
		if(fomatDate(s)==null||fomatDate(e)==null){
			return false;
		}
		return fomatDate(s).getTime() >=fomatDate(e).getTime();
	}

	/**
	 * 格式化日期  yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static Date fomatDate(String date) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 格式化日期  yyyy-MM-dd
	 * 
	 * @return yyyy-MM-dd
	 */
	public static Date fomatDatetime(String date) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 格式化日期  yyyy-MM-dd
	 *
	 * @return yyyy-MM-dd
	 */
	public static Date fomatDatetimeYY(String date) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 格式化日期  yyyy-MM-dd HH:mm:ss
	 * 日期转换成string yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String fomatString(Date date) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   return	fmt.format(date);
	
	}
	/**
	 * 格式化日期  yyyy年MM月dd日
	 * 日期转换成string yyyy年MM月dd日
	 * @return
	 */
	public static String fomatString4Day(Date date) {
		DateFormat fmt = new SimpleDateFormat("yyyy年MM月dd日");
	   return	fmt.format(date);

	}
	public static String fomatString4DayEn(Date date) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		String res=null;
		try {
			res=fmt.format(date);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return	null;

	}
	/**
	 * 校验日期是否合法
	 * 
	 * @return
	 */
	public static boolean isValidDate(String s) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			fmt.parse(s);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return false;
		}
	}
	public static int getDiffYear(String startTime,String endTime) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			long aa=0;
			int years=(int) (((fmt.parse(endTime).getTime()-fmt.parse(startTime).getTime())/ (1000 * 60 * 60 * 24))/365);
			return years;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return 0;
		}
	}
	  /**
     * <li>功能描述：时间相减得到天数
     * @param beginDateStr
     * @param endDateStr
     * @return
     * long 
     * @author Administrator
     */
    public static long getDaySub(String beginDateStr,String endDateStr){
        long day=0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = null;
        Date endDate = null;
        
            try {
				beginDate = format.parse(beginDateStr);
				endDate= format.parse(endDateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
            day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
            //System.out.println("相隔的天数="+day);
      
        return day;
    }
    
    /**
     * 得到n天之后的日期
     * @param days
     * @return
     */
    public static String getAfterDayDate(String days) {
    	int daysInt = Integer.parseInt(days);
    	
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();
        
        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdfd.format(date);
        
        return dateStr;
    }
	/**
	 * 得到n天之前或之后的日期
	 * @param date  当前日期
	 * @param days 负数则为之前，正数位之后
	 * @return
	 */
	public static String getPreOrAfterDayDate(Date date,int days) {


		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		// add方法中的第二个参数n中，正数表示该日期后n天，负数表示该日期的前n天
		calendar.add(Calendar.DATE, days);
		Date date1 = calendar.getTime();
		SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdfd.format(date1);
		return dateStr;
	}
    /**
     * 得到n天之后是周几
     * @param days
     * @return
     */
    public static String getAfterDayWeek(String days) {
    	int daysInt = Integer.parseInt(days);
    	
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();
        
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dateStr = sdf.format(date);
        
        return dateStr;
    }
    
    /**
    * 当前时间加多少分钟 
    * 传入时间和要的分钟
    */
    public static Date AddMinutes(Date date, Integer minutes){
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.add(Calendar.MINUTE, minutes);
  
    return c.getTime();
//    public static void main(String[] args) {
//    	DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     //格式化成年月日时分秒
//    	System.out.println(df.format(AddMinutes(new Date(),60)));	;   //当前日期60分钟以后
//    	}
    }
    
    
    public static String getStringDate(Date date){
    	DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
    	return df.format(date);
    }


	/**
	 * 获取随机日期
	 * @param beginDate 起始日期，格式为：yyyy-MM-dd
	 * @param endDate 结束日期，格式为：yyyy-MM-dd
	 * @return
	 */
	public static Date randomDate(String beginDate,String endDate){
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date start = format.parse(beginDate);
			Date end = format.parse(endDate);

			if(start.getTime() >= end.getTime()){
				return null;
			}

			long date = random(start.getTime(),end.getTime());

			return new Date(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static long random(long begin,long end){
		long rtn = begin + (long)(Math.random() * (end - begin));
		if(rtn == begin || rtn == end){
			return random(begin,end);
		}
		return rtn;
	}
	public static void main(String[] args) {
		//randomDate("2018-01-22","2018-02-22");
		for (int i = 0; i < 20; i++) {
			System.out.println(getStringDate(randomDate("2018-01-22","2018-02-22")));
		}


//    	System.out.println(getDays());  //20150503
//    System.out.println(	getDayTime());
//    	System.out.println(getYear()); //2015
//    	System.out.println(getAfterDayWeek("3")); 
//    	
    	//System.out.println(getCompare());
    
    }

}
