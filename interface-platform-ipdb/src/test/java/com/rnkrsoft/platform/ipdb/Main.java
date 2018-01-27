package com.rnkrsoft.platform.ipdb;

import com.rnkrsoft.platform.ipdb.exception.IPFormatException;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    @Test
    public void testBaseStation() throws IOException, IPFormatException, URISyntaxException {
        IpDatabase db = new IpDatabase(Thread.currentThread().getContextClassLoader().getResource("ip.ipdb"));
        System.out.println(db.findBaseStation("218.201.78.116", "CN"));
    }

    @Test
    public void testFree() throws IOException, URISyntaxException, IPFormatException {
        IpDatabase db = new IpDatabase(Thread.currentThread().getContextClassLoader().getResource("ip.ipdb"));
        District info = db.findDistrict("218.201.78.116", "CN");
        System.out.println(info);
    }

    @Test
    public void testDistrict() throws IOException, URISyntaxException, IPFormatException {
        IpDatabase db = new IpDatabase(Thread.currentThread().getContextClassLoader().getResource("ip.ipdb"));
        District info = db.findDistrict("218.201.78.116", "CN");
        System.out.println(info);
    }

    @Test
    public void testIDC() throws IOException, URISyntaxException, IPFormatException {
        IpDatabase db = new IpDatabase(Thread.currentThread().getContextClassLoader().getResource("ip.ipdb"));
        IDC info = db.findIdc("8.8.8.8", "CN");
        System.out.println(info);
    }

    @Test
    public void testCityV4() throws IOException, URISyntaxException, IPFormatException {
        IpDatabase db = new IpDatabase(Thread.currentThread().getContextClassLoader().getResource("ip.ipdb"));
        City info = db.findCity("118.28.1.1", "CN");
        System.out.println(info);
    }

    @Test
    public void testCity() throws IOException, URISyntaxException, IPFormatException {
        IpDatabase db = new IpDatabase(Thread.currentThread().getContextClassLoader().getResource("ip.ipdb"));
        City info = db.findCity("2001:250:201::", "CN");
        System.out.println(info);
    }
}
