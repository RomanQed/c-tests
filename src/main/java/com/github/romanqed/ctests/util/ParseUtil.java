package com.github.romanqed.ctests.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseUtil {
    private static final Pattern ARGUMENTS = Pattern.compile("\"([^\"]+)\"|\\S+");
    private static final Pattern TEST_NAME = Pattern.compile("\\d+");
    private static final char OPEN = '(';
    private static final char CLOSE = ')';
    private static final char BLANK = ' ';

    public static List<String> parseArguments(String rawArguments) {
        Matcher matcher = ARGUMENTS.matcher(rawArguments);
        List<String> ret = new ArrayList<>();
        while (matcher.find()) {
            String toAdd = matcher.group().replace("\"", "");
            ret.add(toAdd);
        }
        return ret;
    }

    public static List<String> parseBrackets(String rawBrackets) throws InvalidBracketException {
        List<String> ret = new ArrayList<>();
        String source = rawBrackets.trim() + " ";
        int count = 0;
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < source.length(); ++i) {
            char cur = source.charAt(i);
            if (cur == OPEN) {
                count += 1;
                if (count > 1) {
                    temp.append(cur);
                }
                continue;
            }
            if (cur == CLOSE) {
                if (count < 1) {
                    throw new InvalidBracketException("Invalid bracket )", i);
                }
                if (count == 1) {
                    ret.add(temp.toString());
                    temp = new StringBuilder();
                    count = 0;
                } else {
                    temp.append(cur);
                    count -= 1;
                }
                continue;
            }
            if (cur == BLANK && count == 0) {
                if (temp.length() != 0) {
                    ret.add(temp.toString());
                    temp = new StringBuilder();
                }
                continue;
            }
            temp.append(cur);
        }
        if (count != 0) {
            throw new InvalidBracketException("Invalid bracket )", rawBrackets.length() - 1);
        }
        return ret;
    }

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
