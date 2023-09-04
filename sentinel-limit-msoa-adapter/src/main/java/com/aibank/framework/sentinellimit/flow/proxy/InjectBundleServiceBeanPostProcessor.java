package com.aibank.framework.sentinellimit.flow.proxy;

import cn.hutool.core.util.ReflectUtil;
import com.aibank.framework.sentinellimit.flow.interceptor.OutboundInterceptor;
import com.aibank.framework.sentinellimit.flow.interceptor.OutboundMethodInterceptor;
import com.baidu.ub.msoa.container.support.governance.annotation.BundleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

@EnableAspectJAutoProxy(proxyTargetClass = true)
public class InjectBundleServiceBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(InjectBundleServiceBeanPostProcessor.class);

    private ApplicationContext applicationContext;

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!"testService".equals(beanName)) {
            return bean;
        }
        try {
            injectBundleServiceField(bean);
        } catch (Throwable throwable) {
            logger.error("OutboundBeanPostProcessor ", throwable);
            return bean;
        }
        return bean;
    }

    private void injectBundleServiceField(Object bean) {
        // 获取原始类
        Class<?> originalClass = AopProxyUtils.ultimateTargetClass(bean);
        // 获取原始对象
        Object target = AopProxyUtils.getSingletonTarget(bean);
        if (target == null) {
            target = bean;
        }
        Map<String, OutboundInterceptor> interceptorMap = applicationContext.getBeansOfType(OutboundInterceptor.class);
        if (interceptorMap.isEmpty()) {
            return;
        }

        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setOrder(Integer.MAX_VALUE);
        advisor.setAdvice(new OutboundMethodInterceptor(new ArrayList<>(interceptorMap.values())));

        Field[] fields = originalClass.getDeclaredFields();
        for (Field field : fields) {
            BundleService bundleService = field.getAnnotation(BundleService.class);
            Class<?> fieldDeclaringClass = field.getType();
            if (bundleService == null) {
                continue;
            }
            //获取属性的代理对象
            Object fieldTarget = ReflectUtil.getFieldValue(target, field);
            for (Method method : fieldDeclaringClass.getMethods()) {
                OutboundMethodInterceptor.methodBundleServiceMap.put(method, bundleService);
            }
            //对BundleService的属性再次代理
            Object proxy = CachingProxyFactory.getProxy(fieldTarget, fieldDeclaringClass, advisor, bundleService);
            ReflectUtil.setFieldValue(target, field, proxy);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
