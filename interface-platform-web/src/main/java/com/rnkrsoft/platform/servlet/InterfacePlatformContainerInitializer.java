package com.rnkrsoft.platform.servlet;

import com.rnkrsoft.platform.InterfacePlatformHolder;
import com.rnkrsoft.platform.InterfaceServiceFactory;
import com.rnkrsoft.platform.InterfaceServiceImpl;
import com.rnkrsoft.platform.service.InterfaceService;
import lombok.extern.slf4j.Slf4j;

import javax.print.DocFlavor;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import javax.servlet.http.HttpServlet;
import java.util.Set;

/**
 * Created by rnkrsoft.com on 2019/1/26.
 */
@HandlesTypes({HttpServlet.class})
@Slf4j
public class InterfacePlatformContainerInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext ctx) throws ServletException {
        InterfaceServiceFactory interfaceServiceFactory = new InterfaceServiceFactory();
        InterfaceService interfaceService = interfaceServiceFactory.newInstance();
        InterfacePlatformHolder.set(interfaceService);
    }
}
