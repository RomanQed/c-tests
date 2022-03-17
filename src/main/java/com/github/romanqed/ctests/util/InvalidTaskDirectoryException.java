package com.github.romanqed.ctests.util;

import java.io.File;

public class InvalidTaskDirectoryException extends Exception {
    public InvalidTaskDirectoryException() {
        super();
    }

    public InvalidTaskDirectoryException(String message) {
        super(message);
    }

    public InvalidTaskDirectoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTaskDirectoryException(Throwable cause) {
        super(cause);
    }

    public InvalidTaskDirectoryException(File directory) {
        super("Invalid task directory " + directory);
    }
}
