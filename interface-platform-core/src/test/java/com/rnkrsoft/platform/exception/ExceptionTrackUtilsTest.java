package com.rnkrsoft.platform.exception;

import org.junit.Test;

/**
 * Created by rnkrsoft.com on 2018/6/21.
 */
public class ExceptionTrackUtilsTest {

    @Test
    public void testToString() throws Exception {
        System.out.println(ExceptionTrackUtils.toString(new NullPointerException("this is a error")));
    }
}