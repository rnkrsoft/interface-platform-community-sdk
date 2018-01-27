package com.rnkrsoft.platform.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by rnkrsoft.com on 2018/6/21.
 * 异常跟踪信息提取
 */
public class ExceptionTrackUtils {
    public static String toString(Throwable e){
        StringWriter stringWriter = new StringWriter(1024 * 1024 * 1);
        PrintWriter writer = new PrintWriter(stringWriter);
        writer.println("错误信息:" + e.getMessage());
        StackTraceElement[] trace = e.getStackTrace();
        for (StackTraceElement traceElement : trace) {
            writer.println("\tat " + traceElement);
        }
        return stringWriter.getBuffer().toString();
    }
}
