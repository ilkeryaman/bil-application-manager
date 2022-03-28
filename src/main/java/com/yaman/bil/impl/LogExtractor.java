package com.yaman.bil.impl;

import com.yaman.bil.ILogExtractor;
import com.yaman.util.DateUtils;
import com.yaman.util.StringUtils;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class LogExtractor implements ILogExtractor {

    private int datePartLength = 23;

    @Override
    public Timestamp extractDate(String logText) {
        String datePartStr = getDatePartStr(logText);
        return StringUtils.isEmpty(datePartStr) ? null : DateUtils.convertToTimestamp(logText);
    }

    private String getDatePartStr(String logText){
        String datePart = null;
        if(StringUtils.isNotEmpty(logText) && logText.length() >= datePartLength){
            datePart = logText.substring(0, datePartLength);
        }
        return datePart;
    }
}
