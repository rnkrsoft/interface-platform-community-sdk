package com.rnkrsoft.platform.ipdb;

import lombok.Data;

/**
 * 地级市精度库
 */
@Data
public class BaseStation {
    /**
     * 国家名称
     */
    String countryName;
    /**
     * 省名字
     */
    String regionName;
    /**
     * 城市名字
     */
    String cityName;
    /**
     * 所有者
     */
    String ownerDomain;
    /**
     * 运营商
     */
    String ispDomain;
    /**
     * 基站 | WIFI
     */
    String baseStation;

    public BaseStation(String[] names, String[] data) {
        int countryNameIdx = -1;
        int regionNameIdx = -1;
        int cityNameIdx = -1;
        int ownerDomainIdx = -1;
        int ispDomainIdx = -1;
        int baseStationIdx = -1;
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals("country_name")) {
                countryNameIdx = i;
            } else if (names[i].equals("region_name")) {
                regionNameIdx = i;
            } else if (names[i].equals("city_name")) {
                cityNameIdx = i;
            } else if (names[i].equals("owner_domain")) {
                ownerDomainIdx = i;
            } else if (names[i].equals("isp_domain")) {
                ispDomainIdx = i;
            } else if (names[i].equals("base_station")) {
                baseStationIdx = i;
            }
        }
        if (countryNameIdx > -1) {
            this.countryName = data[countryNameIdx];
        }
        if (regionNameIdx > -1) {
            this.regionName = data[regionNameIdx];
        }
        if (cityNameIdx > -1) {
            this.cityName = data[cityNameIdx];
        }
        if (ownerDomainIdx > -1) {
            this.ownerDomain = data[ownerDomainIdx];
        }
        if (ispDomainIdx > -1) {
            this.ispDomain = data[ispDomainIdx];
        }
        if (baseStationIdx > -1) {
            this.baseStation = data[baseStationIdx];
        }
    }
}
