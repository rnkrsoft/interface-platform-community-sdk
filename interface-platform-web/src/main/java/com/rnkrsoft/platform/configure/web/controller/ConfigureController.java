package com.rnkrsoft.platform.configure.web.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.rnkrsoft.platform.config.IpFirewallConfig;
import com.rnkrsoft.platform.domains.IpLocationRequest;
import com.rnkrsoft.platform.domains.IpLocationResponse;
import com.rnkrsoft.platform.protocol.service.ConfigureService;
import com.rnkrsoft.platform.protocol.service.FetchConfigureRequest;
import com.rnkrsoft.platform.protocol.service.FetchConfigureResponse;
import com.rnkrsoft.platform.service.FlowLimitingService;
import com.rnkrsoft.platform.service.IpInfoService;
import com.rnkrsoft.platform.utils.NetworkUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by rnkrsoft.com on 2018/10/7.
 * 接口远程配置分发
 */
@Slf4j
@Controller
public class ConfigureController {

    static Gson GSON = new GsonBuilder().serializeNulls().create();

    @Autowired
    ConfigureService configureService;

    @Autowired(required = false)
    IpFirewallConfig ipFirewallConfig;


    @Autowired(required = false)
    FlowLimitingService flowLimitingService;

    @Autowired(required = false)
    IpInfoService ipInfoService;


    @CrossOrigin("*")
    @RequestMapping(value = "/configure", method = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.HEAD, RequestMethod.OPTIONS, RequestMethod.PATCH, RequestMethod.PUT, RequestMethod.TRACE})
    @ResponseBody
    public String rejected(HttpServletRequest req, HttpServletResponse rsp) {
        String clientIp = NetworkUtils.getClientIp(req);
        log.info("malicious attack ip:{}", clientIp);
        rsp.setStatus(404);
        return "malicious attack, firewall rejected!";
    }


    @CrossOrigin("*")
    @RequestMapping(value = "/configure", method = RequestMethod.POST)
    @ResponseBody
    public String configure(@RequestBody(required = false) String json, HttpServletRequest req, HttpServletResponse rsp) {
        String clientIp = NetworkUtils.getClientIp(req);
        log.info("ip access:{}", clientIp);
        IpLocationResponse ipLocationResponse = null;
        if (ipInfoService != null) {
            ipLocationResponse = ipInfoService.ipLocation(IpLocationRequest.builder().address(clientIp).build());
            if (!ipLocationResponse.isSuccess()) {
                if (ipFirewallConfig != null && ipFirewallConfig.getStrictCheckIp() != null) {
                    if (ipFirewallConfig.getStrictCheckIp()) {
                        log.error("已启用严格IP检测,来自ip({})是不被允许的,可能是恶意攻击,请求已拒绝：'{}'", clientIp, json);
                        rsp.setStatus(200);
                        return "malicious attack, firewall rejected!";
                    } else {
                        log.error("ip({}) location lookup fail! please update ip database!", clientIp);
                    }
                }
            }
        }
        //如果能够获取IP地址信息，且配置了允许访问的省名称，则进行检测，防止CC和DDOS攻击
        if (ipLocationResponse != null && ipFirewallConfig != null && ipFirewallConfig.getAllowProvinces() != null && !ipFirewallConfig.getAllowProvinces().isEmpty()) {
            boolean found = false;
            String provinceName = ipLocationResponse.getProvinceName();
            String[] provinceNames = ipFirewallConfig.getAllowProvinces().split(";");
            for (String allowProvince : provinceNames) {
                if (allowProvince.equals(provinceName)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                log.error("已启用严格IP检测,来自ip({})属于不被允许的省‘{}’,可能是恶意攻击,请求已拒绝：'{}'", clientIp, provinceName, json);
                rsp.setStatus(200);
                return "malicious attack, firewall rejected!";
            }
        }
        FetchConfigureRequest request = null;
        try {
            request = GSON.fromJson(json, FetchConfigureRequest.class);
        } catch (JsonSyntaxException e) {
            log.error("请求JSON 无效");
            rsp.setStatus(200);
            return "malicious attack, firewall rejected!";
        }
        int clientPort = NetworkUtils.getClientPort(req);
        //进行流量检查，防止DDOS攻击
        if (flowLimitingService != null && !flowLimitingService.detects(null, request.getUic(), clientIp, "", ipFirewallConfig.getMaxThresholdPreMin())) {
            rsp.setStatus(200);
            return "malicious attack, firewall rejected!";
        }

        FetchConfigureResponse response = configureService.fetchConfigure(request, clientIp, clientPort);
        String rspJson = GSON.toJson(response);
        return rspJson;
    }
}
