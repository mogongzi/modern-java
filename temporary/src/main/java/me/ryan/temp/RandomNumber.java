package me.ryan.temp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class RandomNumber {

    public static final Logger log = LogManager.getLogger(RandomNumber.class);

    public static void main(String[] args) {
        Random rnd = new Random();
        for (int i = 0; i < 20; i++) {
            log.info(rnd.nextInt(9));
        }

        log.info((int)(Math.random() * 1000));
    }
}
