package com.aibank.framework.sentinellimit.flow.proxy;

import com.aibank.framework.sentinellimit.flow.interceptor.*;
import com.aibank.framework.sentinellimit.flow.interceptor.limit.InboundMethodLimitation;
import com.aibank.framework.sentinellimit.flow.interceptor.limit.OutboundMethodLimitation;
import com.baidu.ub.msoa.container.support.governance.annotation.BundleService;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcher;
import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.List;


@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import(InjectBundleServiceBeanPostProcessor.class)
public class LimitConfig implements BeanFactoryPostProcessor, InitializingBean {
    private BlockHandler blockHandler;

    private DataSource dataSource;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        configurableListableBeanFactory.registerSingleton("InboundMethodLimitation", new InboundMethodLimitation());
        configurableListableBeanFactory.registerSingleton("OutboundMethodLimitation", new OutboundMethodLimitation());
    }

    @Bean
    public DefaultPointcutAdvisor processStartPointcutAdvisor(List<InboundInterceptor> inboundInterceptors) {
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(new InboundMatchingPointcut());
        advisor.setOrder(Integer.MAX_VALUE);
        advisor.setAdvice(new InboundMethodInterceptor(inboundInterceptors, blockHandler));
        return advisor;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //   assert dataSource != null;
        if (blockHandler == null) {
            blockHandler = new DefaultBlockHandler();
        }
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
}
