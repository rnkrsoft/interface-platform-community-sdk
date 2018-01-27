package com.rnkrsoft.platform.demo.service;

import com.rnkrsoft.platform.demo.domains.DemoRequest;
import com.rnkrsoft.platform.demo.domains.DemoResponse;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/6/19.
 */
@Service
public class DemoServiceImpl implements DemoService{
    @Override
    public DemoResponse demo(DemoRequest request) {
        System.out.println("---------------" + request);
        DemoResponse response = new DemoResponse();
        response.setAge(1234);
//        throw new RuntimeException("这是一个错误");
        return response;
    }
}
