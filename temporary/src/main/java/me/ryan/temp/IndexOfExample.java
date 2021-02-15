package me.ryan.temp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IndexOfExample {

    public static final Logger log = LogManager.getLogger(IndexOfExample.class);

    public static void main(String[] args) {

        String str1 = "hello";
        String str2 = "hel";

        log.info(str1.indexOf(str2));
    }
}
