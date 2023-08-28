package com.aibank.framework.sentinellimit.test;

import com.baidu.ub.msoa.container.support.governance.annotation.BundleService;
import msoa.javassist.util.proxy.MethodHandler;
import msoa.javassist.util.proxy.Proxy;
import msoa.javassist.util.proxy.ProxyFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;


@Component
public class BundleServicePostProcessor implements BeanPostProcessor {

    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (beanName.equals("testService")) {
            System.out.println("postProcessBeforeInitialization: " + beanName);
        }
        Class<?> aClass = bean.getClass();
        Field[] fields = aClass.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(BundleService.class)) {
                BundleService bundleService = field.getAnnotation(BundleService.class);
                ProxyFactory proxyFactory = new ProxyFactory();
                proxyFactory.setInterfaces(new Class[]{bundleService.interfaceType()});
                try {
                    Proxy proxy = (Proxy) proxyFactory.createClass().getConstructor(new Class[0]).newInstance(new Object[0]);
                    proxy.setHandler((o, method, method1, objects) -> "invoked");
                    field.setAccessible(true);
                    field.set(bean, proxy);
                    Object o = field.get(bean);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return bean;
    }
}
