package com.zzl.crm.commons.utils;

import java.util.UUID;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/04/28  0:13
 */
public class UUIDUtils {
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
