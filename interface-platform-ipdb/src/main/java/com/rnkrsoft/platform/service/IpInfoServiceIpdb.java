package com.rnkrsoft.platform.service;

import com.rnkrsoft.platform.domains.IpLocationRequest;
import com.rnkrsoft.platform.domains.IpLocationResponse;
import com.rnkrsoft.platform.ipdb.District;
import com.rnkrsoft.platform.ipdb.IpDatabase;
import com.rnkrsoft.platform.ipdb.exception.IPFormatException;
import com.rnkrsoft.platform.ipdb.exception.InvalidDatabaseException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.web.doc.enums.Skeleton4jRspCode;

/**
 * Created by rnkrsoft.com on 2018/12/2.
 */
@Slf4j
public class IpInfoServiceIpdb implements IpInfoService, InitializingBean {
    private IpDatabase ipDatabase;
    @Setter
    String ipdbFileName;

    @Override
    public IpLocationResponse ipLocation(IpLocationRequest request) {
        IpLocationResponse response = new IpLocationResponse();
        try {
            District district = ipDatabase.findDistrict(request.getAddress(), "CN");
            response.setCountryName(district.getCountryName());
            response.setProvinceName(district.getRegionName());
            response.setCityName(district.getCityName());
        } catch (IPFormatException e) {
            log.error("IP库格式不正确", e);
            response.setRspCode(Skeleton4jRspCode.FAIL);
        } catch (InvalidDatabaseException e) {
            log.error("IP库格式不正确", e);
            response.setRspCode(Skeleton4jRspCode.FAIL);
        }
        return response;
    }



    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.ipdbFileName == null || this.ipdbFileName.isEmpty()) {
            this.ipdbFileName = "ip.ipdb";
        }
        this.ipDatabase = new IpDatabase(Thread.currentThread().getContextClassLoader().getResource(ipdbFileName));
    }
}
