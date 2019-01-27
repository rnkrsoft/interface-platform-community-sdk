package com.rnkrsoft.platform.domains;

import lombok.Data;

import javax.web.doc.AbstractResponse;
import javax.web.doc.annotation.ApidocElement;

/**
 * Created by rnkrsoft.com on 2018/12/2.
 */
@Data
public class LocationResponse extends AbstractResponse{
    @ApidocElement("国家名字")
    String countryName;

    @ApidocElement("国家代码")
    String countryCode;

    @ApidocElement("省名字")
    String provinceName;

    @ApidocElement("省代码")
    String provinceCode;

    @ApidocElement("城市名字")
    String cityName;

    @ApidocElement("城市代码")
    String cityCode;

    @ApidocElement("区名字")
    String districtName;

    @ApidocElement("区代码")
    String districtCode;

    @ApidocElement("纬度")
    String latitude;

    @ApidocElement("经度")
    String longitude;
}
