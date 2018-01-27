package com.rnkrsoft.platform.ipdb;

import lombok.Data;

@Data
public class District {
    /**
     * 国家名字
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
     * 区
     */
    String districtName;
    /**
     * 中国行政区划代码
     */
    String chinaAdminCode;
    /**
     *
     */
    String coveringRadius;
    /**
     * 纬度
     */
    String latitude;
    /**
     * 经度
     */
    String longitude;

    public District(String[] names, String[] data) {
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
        int len = data.length;
        if (countryNameIdx > -1) {
            this.countryName = data[countryNameIdx];
        }
        if (regionNameIdx > -1) {
            this.regionName = data[regionNameIdx];
        }
        if (cityNameIdx > -1) {
            this.cityName = data[cityNameIdx];
        }
        if (len > 3){
            this.districtName = data[3];
        }
        if (len > 4){
            this.chinaAdminCode = data[4];
        }
        if (len > 5){
            this.coveringRadius = data[5];
        }
        if (len > 6){
            this.latitude = data[6];
        }
        if (len > 7){
            this.longitude = data[7];
        }
    }
}
