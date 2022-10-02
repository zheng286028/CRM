package com.zzl.crm.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/04/26  16:05
 */


/**
 * 对Date类型的时间格式进行修改：yyyy-MM-dd HH:mm:ss
 */
public class DateUtils {
    public static String formateDateTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        return dateStr;
    }
    public static String formateDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(date);
        return dateStr;
    }
}
