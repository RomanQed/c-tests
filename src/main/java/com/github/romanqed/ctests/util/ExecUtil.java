package com.github.romanqed.ctests.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ExecUtil {
    public static long TIMEOUT = 10;

    public static ExecData runProcess(String command, String input) throws IOException, InterruptedException {
        Objects.requireNonNull(command);
        ProcessBuilder builder = new ProcessBuilder(command);
        Process process = builder.start();
        OutputStream outputStream = process.getOutputStream();
        IOUtil.writeOutputStream(outputStream, input);
        InputStream inputStream = process.getInputStream();
        InputStream errorStream = process.getErrorStream();
        ExecData ret = new ExecData();
        ret.output = IOUtil.readInputStream(inputStream);
        ret.error = IOUtil.readInputStream(errorStream);
        process.waitFor(TIMEOUT, TimeUnit.SECONDS);
        ret.code = process.exitValue();
        return ret;
    }

    public static class ExecData {
        private int code;
        private String output;
        private String error;

        public int getCode() {
            return code;
        }

        public String getOutput() {
            return output;
        }

        public String getError() {
            return error;
        }

        @Override
        public String toString() {
            return "[" + code + "] stdout: " + output + " stderr: " + error;
        }
    }
}
