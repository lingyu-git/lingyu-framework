package com.lingyu.common.util;

import cn.hutool.core.date.DateUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateTimeUtil {

    public static Date unixTimeStamp2Date(Long timeStamp) {
        return new Date(timeStamp * 1000);
    }

    public static Long date2UnixTimeStamp(Date date) {
        return date.getTime() / 1000;
    }

    public static Long date2UnixTimeStamp(String date, String format) {
        LocalDateTime dateTime = DateUtil.parseLocalDateTime(date, format);
        ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.systemDefault());
        return dateTime.toEpochSecond(zonedDateTime.getOffset());
    }

}
