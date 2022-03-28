package com.yaman.bil;


import java.sql.Timestamp;

public interface ILogExtractor {
    Timestamp extractDate(String logText);
}
