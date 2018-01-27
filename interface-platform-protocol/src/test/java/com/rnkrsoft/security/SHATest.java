package com.rnkrsoft.security;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by rnkrsoft.com on 2018/6/7.
 */
public class SHATest {

    @Test
    public void testSHA512() throws Exception {
        String str1 = SHA.SHA512("hello world, cryptopp 这是一个测试瀛");
        System.out.println(str1);
        Assert.assertEquals("312263cc031cd9e96287df05b4caef6e3b42cf4636dcc3af01dfa29cdc3473220547a0b998901ede0b0f99b6e9bc9b448ce3b33834f35605cf6ea3c27f633337", str1);
    }
}