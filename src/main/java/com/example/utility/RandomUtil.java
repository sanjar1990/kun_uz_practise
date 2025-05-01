package com.example.utility;

import java.util.Random;

public class RandomUtil {
    public static String getRandomString() {
        Random rand = new Random();
        return String.valueOf(rand.nextInt(10000,99999));
    }
}
