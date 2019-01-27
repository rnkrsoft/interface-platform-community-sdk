package com.rnkrsoft.platform.servlet;

import com.rnkrsoft.com.google.gson.Gson;
import com.rnkrsoft.com.google.gson.GsonBuilder;
import com.rnkrsoft.com.google.gson.JsonSyntaxException;
import com.rnkrsoft.io.buffer.ByteBuf;
import com.rnkrsoft.platform.InterfacePlatformHolder;
import com.rnkrsoft.platform.domains.Request;
import com.rnkrsoft.platform.domains.Response;
import com.rnkrsoft.platform.protocol.enums.InterfaceRspCode;
import com.rnkrsoft.platform.service.FlowLimitingService;
import com.rnkrsoft.platform.service.InterfaceService;
import com.rnkrsoft.platform.utils.NetworkUtils;
import com.rnkrsoft.utils.DateUtils;
import com.rnkrsoft.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.rnkrsoft.platform.InterfacePlatformConstants.SESSION_ID;

/**
 * Created by rnkrsoft.com on 2019/1/26.
 */
@Slf4j
public class InterfacePlatformServlet extends HttpServlet {
    static Gson GSON = new GsonBuilder().create();
    static final int SECOND = 60 * 1000;
    static final int MINUTE = 60 * SECOND;
    static final int TIMEOUT = 5 * MINUTE;
    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";

    protected void service(HttpServletRequest req, HttpServletResponse rsp) throws IOException {
        InterfaceService interfaceService = InterfacePlatformHolder.get();
        String method = req.getMethod();
        if (METHOD_GET.equals(method)) {
            //TODO 展示接口平台介绍页面
        } else if (METHOD_POST.equals(method)) {
            //TODO 调用接口平台服务
            Response response = new Response();
            Request request;
            InputStream is = null;
            ByteBuf byteBuf = ByteBuf.allocate(1024).autoExpand(true);
            try {
                is = req.getInputStream();
                byteBuf.read(is);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            String body = byteBuf.getString("UTF-8", byteBuf.readableLength());
            try {
                request = GSON.fromJson(body, Request.class);
            } catch (JsonSyntaxException e) {
                log.error("请求JSON 无效");
                response.setCode(InterfaceRspCode.REQUEST_DATA_IS_NULL);
                String rspJson = GSON.toJson(response);
                log.error("response json:{}", rspJson);
                return;
            }

            MDC.put(SESSION_ID, request.getSessionId());
            String clientIp = NetworkUtils.getClientIp(req);
            int clientPort = NetworkUtils.getClientPort(req);
            request.setClientIp(clientIp);
            request.setClientPort(clientPort);
            FlowLimitingService flowLimitingService = interfaceService.getFlowLimitingService();
            //进行流量检查，防止DDOS攻击
            if (flowLimitingService != null && !flowLimitingService.detects(request.getUid(), request.getUic(), clientIp, request.getChannel(), 10, TimeUnit.SECONDS)) {
                rsp.setStatus(200);
                write(rsp, "malicious attack, firewall rejected!");
                MDC.remove(SESSION_ID);
                return;
            }
            log.info("request json:{}", body);
            if (StringUtils.isBlank(request.getTimestamp())) {
                response.setCode(InterfaceRspCode.PARAM_IS_NULL);
                String rspJson = GSON.toJson(response);
                log.error("response json{}", rspJson);
                write(rsp, rspJson);
                MDC.remove(SESSION_ID);
                return;
            }
            Date date = DateUtils.toDate(request.getTimestamp());
            long ts = date.getTime();
            if (System.currentTimeMillis() - ts > TIMEOUT || ts - System.currentTimeMillis() > TIMEOUT) {
                log.error("请求时间{}与服务器时间{}不一致", request.getTimestamp(), DateUtils.getCurrFullTime());
                response.setCode(InterfaceRspCode.TIMESTAMP_ILLEGAL);
                String rspJson = GSON.toJson(response);
                log.error("response json:{}", rspJson);
                write(rsp, rspJson);
                MDC.remove(SESSION_ID);
                return;
            }
            interfaceService.invoke(request);
        } else {
            //TODO 提示不支持的服务
            write(rsp, "unsupported!");
            MDC.remove(SESSION_ID);
            return;
        }
    }

    void write(HttpServletResponse response, String json) {
        ByteBuf buf = ByteBuf.allocate(1024).autoExpand(true);
        buf.put("UTF-8", json);
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            buf.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
