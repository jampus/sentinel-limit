package com.aibank.framework.sentinellimit.flow.proxy;

import com.aibank.framework.sentinellimit.flow.interceptor.*;
import com.aibank.framework.sentinellimit.flow.interceptor.limit.*;
import com.aibank.framework.sentinellimit.service.DefaultFlowLimitLoad;
import com.baidu.ub.msoa.container.support.governance.annotation.BundleService;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcher;
import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.*;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class LimitConfig implements BeanDefinitionRegistryPostProcessor, SmartInitializingSingleton {
    private BlockHandler blockHandler;

    private DataSource dataSource;

    private String app;

    private List<Class<? extends MsoaBundleServiceInterceptor>> defaultInterceptor;

    public LimitConfig(DataSource dataSource, String app) {
        this.dataSource = dataSource;
        this.app = app;
        blockHandler = new DefaultBlockHandler();
        defaultInterceptor = new ArrayList<>();
        defaultInterceptor.add(InboundMethodOriginLimitation.class);
        defaultInterceptor.add(InboundOriginLimitation.class);
        defaultInterceptor.add(OutboundDestinationLimitation.class);
        defaultInterceptor.add(OutboundMethodLimitation.class);
    }

    public LimitConfig(BlockHandler blockHandler, DataSource dataSource, String app) {
        this.blockHandler = blockHandler;
        this.dataSource = dataSource;
        this.app = app;
    }

    public LimitConfig(BlockHandler blockHandler, DataSource dataSource, String app, List<Class<? extends MsoaBundleServiceInterceptor>> defaultInterceptor) {
        this.blockHandler = blockHandler;
        this.dataSource = dataSource;
        this.app = app;
        this.defaultInterceptor = defaultInterceptor;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        registryConfigBean(registry);
        registryMethodLimitationBean(registry);
    }

    private void registryConfigBean(BeanDefinitionRegistry registry) {
        registry.registerBeanDefinition(InjectBundleServiceBeanPostProcessor.class.getName(), new RootBeanDefinition(InjectBundleServiceBeanPostProcessor.class));
        ConstructorArgumentValues constructorArgs = new ConstructorArgumentValues();
        constructorArgs.addIndexedArgumentValue(0, blockHandler);
        RootBeanDefinition inboundBeanDefinition = new RootBeanDefinition(InboundMethodInterceptor.class);
        inboundBeanDefinition.setConstructorArgumentValues(constructorArgs);
        registry.registerBeanDefinition(InboundMethodInterceptor.class.getName(), inboundBeanDefinition);
    }

    private void registryMethodLimitationBean(BeanDefinitionRegistry registry) {
        for (Class<?> interceptorClass : defaultInterceptor) {
            registry.registerBeanDefinition(interceptorClass.getName(), new RootBeanDefinition(interceptorClass));
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(new InboundMatchingPointcut());
        advisor.setOrder(Integer.MAX_VALUE);
        advisor.setAdvice(beanFactory.getBean(InboundMethodInterceptor.class));
        beanFactory.registerSingleton("inboundPointcutAdvisor", advisor);
    }

    @Override
    public void afterSingletonsInstantiated() {
        DefaultFlowLimitLoad flowLimitService = new DefaultFlowLimitLoad(dataSource, app);
        flowLimitService.init();
    }

    public static class InboundMatchingPointcut implements Pointcut {
        @Override
        public ClassFilter getClassFilter() {
            return new AnnotationClassFilter(BundleService.class);
        }

        @Override
        public MethodMatcher getMethodMatcher() {
            return new StaticMethodMatcher() {
                @Override
                public boolean matches(Method method, Class<?> aClass) {
                    Class<?> interfaceType = aClass.getAnnotation(BundleService.class).interfaceType();
                    for (Method interfaceTypeMethod : interfaceType.getMethods()) {
                        if (interfaceTypeMethod.getName().equals(method.getName())) {
                            return true;
                        }
                    }
                    return false;
                }
            };
        }
    }

    public void setBlockHandler(BlockHandler blockHandler) {
        this.blockHandler = blockHandler;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setApp(String app) {
        this.app = app;
    }
}
