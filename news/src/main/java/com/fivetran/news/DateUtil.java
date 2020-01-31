package com.fivetran.news;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    static LocalDate stringToDate(String string) {
        return LocalDate.parse(string, DateTimeFormatter.ofPattern("MM-dd-yyyy"));
    }

    static String dateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
    }
}
