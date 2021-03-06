package com.github.romanqed.ctests.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseUtil {
    private static final Pattern TEST_NAME = Pattern.compile("\\d+");

    public static String formatNumber(int number) {
        if (number < 10) {
            return "0" + number;
        }
        return String.valueOf(number);
    }

    public static String decreaseIndex(String testName) {
        Matcher matcher = TEST_NAME.matcher(testName);
        if (!matcher.find()) {
            throw new IllegalStateException("Incorrect test name format!");
        }
        String number = matcher.group();
        return testName.replace(number, formatNumber(Integer.parseInt(number) - 1));
    }
}
