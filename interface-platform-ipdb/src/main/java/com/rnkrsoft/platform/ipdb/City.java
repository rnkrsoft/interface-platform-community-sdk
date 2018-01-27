package com.rnkrsoft.platform.ipdb;

import lombok.Data;

@Data
public class City {
    String countryName;
    String regionName;
    String cityName;
    String ownerDomain;
    String ispDomain;
    String latitude;
    String longitude;
    String timeZone;
    String utcOffset;
    String chinaAdminCode;
    String iddCode;
    String countryCode;
    String continentCode;
    String idc;
    String baseStation;
    String countryCode3;
    String europeanUnion;
    String currencyCode;
    String currencyName;
    String anycast;
    public City(String[] names, String[] data) {

    }
}
