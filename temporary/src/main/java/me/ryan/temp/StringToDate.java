package me.ryan.temp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class StringToDate {

    private static final Logger log = LogManager.getLogger(StringToDate.class);

    public static void main(String[] args) {
        String strDate = "Sat, 26 Dec 2020 15:30:00 +0800";
        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("EE, dd MMM yyyy HH:mm:ss Z");
        ZonedDateTime dateTime = ZonedDateTime.parse(strDate, dtFormatter);
        strDate = dateTime.format(dtFormatter);
        log.info(strDate);
        dateTime = ZonedDateTime.now();
        log.info(dateTime.format(dtFormatter));
    }
}
