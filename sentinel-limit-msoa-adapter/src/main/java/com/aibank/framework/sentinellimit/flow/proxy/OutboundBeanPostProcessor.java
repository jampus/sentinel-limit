package com.aibank.framework.sentinellimit.flow.proxy;

import com.aibank.framework.sentinellimit.flow.interceptor.OutboundInterceptor;
import com.aibank.framework.sentinellimit.flow.interceptor.limit.OutboundDestinationLimitation;
import com.aibank.framework.sentinellimit.flow.interceptor.limit.OutboundMethodLimitation;
import com.baidu.ub.msoa.container.support.governance.annotation.BundleService;
import com.baidu.ub.msoa.rpc.outbound.ServiceInvokerCreator;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OutboundBeanPostProcessor implements BeanPostProcessor {

    public static Map<Method,BundleService> methodBundleServiceMap = new ConcurrentHashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        try {
            Field proxies = ServiceInvokerCreator.class.getDeclaredField("proxies");
            proxies.setAccessible(true);
            Object o = proxies.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        if (!"testService".equals(beanName)) {
            return bean;
        }
        // 获取原始类
        Class<?> originalClass = bean.getClass().getSuperclass();
        if (originalClass == null) {
            return bean;
        }
        // 获取属性上的注解
        Field[] fields = originalClass.getDeclaredFields();
        for (Field field : fields) {
            BundleService annotation = field.getAnnotation(BundleService.class);
            if (annotation != null) {
                field.setAccessible(true);
                try {
                    Object target = AopProxyUtils.getSingletonTarget(bean);
                    Object o1 = field.get(target);

                    Class<?> declaringClass = field.getType();
                    for (Method method : declaringClass.getMethods()) {
                        methodBundleServiceMap.put(method, annotation);
                    }
                    Object proxy = CachingProxyFactory.getProxy(o1, declaringClass, new OutboundMethodLimitation(), annotation);
                    field.set(target, proxy);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return bean;
    }
}
