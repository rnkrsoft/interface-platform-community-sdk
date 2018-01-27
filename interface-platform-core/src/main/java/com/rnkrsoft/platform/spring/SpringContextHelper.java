package com.rnkrsoft.platform.spring;


import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by rnkrsoft.com on 2018/4/23.
 * Spring工具类
 */
@Component
public class SpringContextHelper implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    private SpringContextHelper() {
    }

    public final synchronized void setApplicationContext(ApplicationContext applicationCon) {
        applicationContext = applicationCon;
    }

    /**
     * 根据Bean的名称获取Bean实例
     *
     * @param name Bean的名称
     * @param <T>  Bean类对象
     * @return 实例
     */
    public static <T> T getBean(String name) {
        checkApplicationContext();
        return (T) applicationContext.getBean(name);
    }

    /**
     * 根据Bean的类型获取Bean实例
     * @param clazz Bean类型
     * @param <T>
     * @return 实例
     */
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return applicationContext.getBean(clazz);
    }
    /**
     * 检测是否存在指定Bean名的Spring Bean
     * @param name Bean的名称
     * @return 是否存在
     */
    public static boolean containsBean(String name){
        checkApplicationContext();
        return applicationContext.containsBean(name);
    }


    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw ErrorContextFactory.instance()
                    .message("未在Spring 中配置Bean")
                    .solution("applicationContext.xml中定义SpringContextHelper")
                    .runtimeException();
        }
    }
}