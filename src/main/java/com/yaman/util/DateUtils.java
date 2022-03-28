package com.yaman.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss,SSS");


    public static Timestamp convertToTimestamp(String dateStr){
        Timestamp timestamp = null;
        Date parsedDate = null;
        try {
            parsedDate = dateFormat.parse(dateStr);
            timestamp = new java.sql.Timestamp(parsedDate.getTime());
        } catch (ParseException e) {

        }
        return timestamp;
    }
}
