package com.aibank.framework.sentinellimit.test;

import com.baidu.ub.msoa.container.support.governance.annotation.BundleService;
import msoa.javassist.util.proxy.Proxy;
import msoa.javassist.util.proxy.ProxyFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;


@Component
public class BundleServicePostProcessor implements BeanPostProcessor, InitializingBean {
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        Class<?> aClass = bean.getClass();
        Field[] fields = aClass.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(BundleService.class)) {
                BundleService bundleService = field.getAnnotation(BundleService.class);
                ProxyFactory proxyFactory = new ProxyFactory();
                proxyFactory.setInterfaces(new Class[]{bundleService.interfaceType()});
                try {
                    Proxy proxy = (Proxy) proxyFactory.createClass().getConstructor(new Class[0]).newInstance(new Object[0]);
                    proxy.setHandler((o, method, method1, objects) -> "invoked ");
                    field.setAccessible(true);
                    field.set(bean, proxy);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return bean;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(222);
    }
}
