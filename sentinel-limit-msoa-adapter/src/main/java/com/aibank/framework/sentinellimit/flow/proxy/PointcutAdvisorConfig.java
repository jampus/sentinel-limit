package com.aibank.framework.sentinellimit.flow.proxy;

import com.aibank.framework.sentinellimit.flow.interceptor.limit.InboundMethodLimitation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;


@Configuration
@Import(OutboundBeanPostProcessor.class)
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class PointcutAdvisorConfig {


    @Bean
    public DefaultPointcutAdvisor inboundBundleServiceIntercept() {
        InboundMethodLimitation inboundMethodLimitation = new InboundMethodLimitation();
        Pointcut pointcut = new InboundMatchingPointcut();
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(pointcut);
        advisor.setOrder(Integer.MAX_VALUE);
        advisor.setAdvice(inboundMethodLimitation);
        return advisor;
    }
}
