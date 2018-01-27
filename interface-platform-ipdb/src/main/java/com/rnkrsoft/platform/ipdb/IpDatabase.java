package com.rnkrsoft.platform.ipdb;

import com.rnkrsoft.platform.ipdb.exception.IPFormatException;
import com.rnkrsoft.platform.ipdb.exception.InvalidDatabaseException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by woate on 2018/12/2.
 */
public class IpDatabase {
    private Reader reader;

    public IpDatabase(URL in) throws IOException, URISyntaxException {
        this.reader = new Reader(in);
    }

    public boolean reload(URL in) {
        try {
            Reader r = new Reader(in);
            this.reader = r;
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public BaseStation findBaseStation(String addr, String language) throws IPFormatException, InvalidDatabaseException {
        String[] data = this.reader.find(addr, language);
        if (data == null) {
            return null;
        }
        return new BaseStation(this.reader.getSupportFields(), data);
    }

    public City findCity(String addr, String language) throws IPFormatException, InvalidDatabaseException {

        String[] data = this.reader.find(addr, language);
        if (data == null) {
            return null;
        }
        return new City( this.reader.getSupportFields(), data);
    }

    public District findDistrict(String addr, String language) throws IPFormatException, InvalidDatabaseException {
        String[] data = this.reader.find(addr, language);
        if (data == null) {
            return null;
        }

        return new District(this.reader.getSupportFields(),data);
    }

    public IDC findIdc(String addr, String language) throws IPFormatException, InvalidDatabaseException {
        String[] data = this.reader.find(addr, language);
        if (data == null) {
            return null;
        }
        return new IDC(this.reader.getSupportFields(), data);
    }


    public boolean isIPv4() {
        return (this.reader.getMeta().ipVersion & 0x01) == 0x01;
    }

    public boolean isIPv6() {
        return (this.reader.getMeta().ipVersion & 0x02) == 0x02;
    }

    public int buildTime() {
        return this.reader.getBuildUTCTime();
    }
}
