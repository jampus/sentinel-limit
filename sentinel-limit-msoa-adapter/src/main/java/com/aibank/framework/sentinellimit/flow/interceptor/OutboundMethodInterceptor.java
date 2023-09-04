package com.aibank.framework.sentinellimit.flow.interceptor;

import com.aibank.framework.sentinellimit.domain.LimitConstants;
import com.aibank.framework.sentinellimit.exception.FlowLimitException;
import com.aibank.framework.sentinellimit.exception.OverloadFlowException;
import com.aibank.framework.sentinellimit.flow.domain.MsoaRequestContext;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.baidu.ub.msoa.container.support.governance.annotation.BundleService;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OutboundMethodInterceptor implements MethodInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(OutboundMethodInterceptor.class);

    public static Map<Method, BundleService> methodBundleServiceMap = new ConcurrentHashMap<>();

    private List<OutboundInterceptor> outboundInterceptors;

    public OutboundMethodInterceptor(List<OutboundInterceptor> outboundInterceptors) {
        this.outboundInterceptors = outboundInterceptors;
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        try {
            Method method = methodInvocation.getMethod();
            BundleService bundleService = methodBundleServiceMap.get(method);
            if (bundleService == null) {
                return methodInvocation.proceed();
            }
            MsoaRequestContext msoaRequestContext = new MsoaRequestContext(bundleService);
            msoaRequestContext.setMethod(method.getName());
            for (OutboundInterceptor outboundInterceptor : outboundInterceptors) {
                boolean intercepted = outboundInterceptor.intercept(msoaRequestContext, methodInvocation);
                if (intercepted) {
                    throw new FlowLimitException("请求受限,请稍后再试");
                }
            }
        } catch (FlowLimitException flowLimitException) {
            throw flowLimitException;
        } catch (Throwable throwable) {
            logger.error("inbound throwable: ", throwable);
            return methodInvocation.proceed();
        }
        return methodInvocation.proceed();
    }
}
