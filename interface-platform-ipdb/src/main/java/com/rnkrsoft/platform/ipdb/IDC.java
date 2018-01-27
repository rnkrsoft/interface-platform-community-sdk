package com.rnkrsoft.platform.ipdb;

import lombok.Data;

@Data
public class IDC {
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

    String ownerDomain;
    String ispDomain;
    String idc;

    public IDC(String[] names, String[] data) {
        int countryNameIdx = -1;
        int regionNameIdx = -1;
        int cityNameIdx = -1;
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals("country_name")){
                countryNameIdx = i;
            }else if (names[i].equals("region_name")){
                regionNameIdx = i;
            }else if (names[i].equals("city_name")){
                cityNameIdx = i;
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
    }
}
