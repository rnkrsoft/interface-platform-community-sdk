package com.rnkrsoft.platform.api.web.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.rnkrsoft.platform.config.IpFirewallConfig;
import com.rnkrsoft.platform.domains.IpLocationRequest;
import com.rnkrsoft.platform.domains.IpLocationResponse;
import com.rnkrsoft.platform.protocol.ApiRequest;
import com.rnkrsoft.platform.protocol.ApiResponse;
import com.rnkrsoft.platform.protocol.enums.InterfaceRspCode;
import com.rnkrsoft.platform.service.FlowLimitingService;
import com.rnkrsoft.platform.service.InterfaceService;
import com.rnkrsoft.platform.service.IpInfoService;
import com.rnkrsoft.platform.utils.NetworkUtils;
import com.rnkrsoft.utils.DateUtils;
import com.rnkrsoft.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static com.rnkrsoft.platform.InterfacePlatformConstants.SESSION_ID;


/**
 * Created by rnkrsoft.com on 2018/5/24.
 * 接口平台SpringMVC控制器
 */
@Slf4j
@Controller
public class ApiController {

    static final int SECOND = 60 * 1000;
    static final int MINUTE = 60 * SECOND;
    static final int TIMEOUT = 5 * MINUTE;

    static Gson GSON = new GsonBuilder().serializeNulls().create();
    @Autowired
    InterfaceService interfaceService;

    @Autowired(required = false)
    IpFirewallConfig ipFirewallConfig;


    @Autowired(required = false)
    FlowLimitingService flowLimitingService;

    @Autowired(required = false)
    IpInfoService ipInfoService;

    @CrossOrigin("*")
    @RequestMapping(value = "/api", method = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.HEAD, RequestMethod.OPTIONS, RequestMethod.PATCH, RequestMethod.PUT, RequestMethod.TRACE})
    @ResponseBody
    public String rejected(HttpServletRequest req, HttpServletResponse rsp) {
        String clientIp = NetworkUtils.getClientIp(req);
        log.info("malicious attack ip:{}", clientIp);
        rsp.setStatus(404);
        return "malicious attack, firewall rejected!";
    }

    @CrossOrigin("*")
    @RequestMapping(value = "/api", method = RequestMethod.POST)
    @ResponseBody
    public String api(@RequestBody(required = false) String json, HttpServletRequest req, HttpServletResponse rsp) {
        ApiResponse response = new ApiResponse();
        //1.检查发送的请求是否有报文
        if (StringUtils.isBlank(json)) {
            log.error("request json is empty!");
            response.setCode(InterfaceRspCode.REQUEST_DATA_IS_NULL);
            String rspJson = GSON.toJson(response);
            log.error("response json:{}", rspJson);
            return rspJson;
        }
        String clientIp = NetworkUtils.getClientIp(req);
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
        ApiRequest request = null;
        try {
            request = GSON.fromJson(json, ApiRequest.class);
        } catch (JsonSyntaxException e) {
            log.error("request json is illegal!");
            response.setCode(InterfaceRspCode.REQUEST_DATA_IS_NULL);
            String rspJson = GSON.toJson(response);
            log.error("response json:{}", rspJson);
            return rspJson;
        }

        MDC.put(SESSION_ID, request.getSessionId());
        int clientPort = NetworkUtils.getClientPort(req);
        //进行流量检查，防止DDOS攻击
        if (flowLimitingService != null && !flowLimitingService.detects(request.getUid(), request.getUic(), clientIp, request.getChannel(), ipFirewallConfig.getMaxThresholdPreMin())) {
            rsp.setStatus(200);
            MDC.remove(SESSION_ID);
            return "malicious attack, firewall rejected!";
        }
        log.info("request json:{}", request.getSessionId(), json);
        if (StringUtils.isBlank(request.getTimestamp())) {
            response.setCode(InterfaceRspCode.PARAM_IS_NULL);
            String rspJson = GSON.toJson(response);
            log.error("response json{}", rspJson);
            MDC.remove(SESSION_ID);
            return rspJson;
        }
        Date date = DateUtils.toDate(request.getTimestamp());
        long ts = date.getTime();
        if (System.currentTimeMillis() - ts > TIMEOUT || ts - System.currentTimeMillis() > TIMEOUT) {
            log.error("the difference between request time '{}' and server time '{}' is more than '{}s'!", request.getTimestamp(), DateUtils.getTimestamp(), TIMEOUT);
            response.setCode(InterfaceRspCode.TIMESTAMP_ILLEGAL);
            String rspJson = GSON.toJson(response);
            log.error("response json:{}", rspJson);
            MDC.remove(SESSION_ID);
            return rspJson;
        }
        response = interfaceService.invoke(request, clientIp, clientPort);
        String rspJson = GSON.toJson(response);
        log.info("response json:{}", rspJson);
        MDC.remove(SESSION_ID);
        return rspJson;
    }
}
