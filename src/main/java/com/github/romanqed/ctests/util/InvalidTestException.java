package com.github.romanqed.ctests.util;

import com.github.romanqed.ctests.tests.MarkedTest;

public class InvalidTestException extends Exception {
    public InvalidTestException(String message) {
        super(message);
    }

    public InvalidTestException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTestException(Throwable cause) {
        super(cause);
    }

    public InvalidTestException(MarkedTest test) {
        super("Test " + test + " is invalid!");
    }
}
