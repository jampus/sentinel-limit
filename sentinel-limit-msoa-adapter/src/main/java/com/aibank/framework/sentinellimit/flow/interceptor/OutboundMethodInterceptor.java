package com.aibank.framework.sentinellimit.flow.interceptor;

import com.aibank.framework.sentinellimit.flow.domain.MsoaRequestContext;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.baidu.ub.msoa.container.support.governance.annotation.BundleService;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OutboundMethodInterceptor implements MethodInterceptor {
    public static Map<Method, BundleService> methodBundleServiceMap = new ConcurrentHashMap<>();

    private List<OutboundInterceptor> outboundInterceptors;

    public OutboundMethodInterceptor(List<OutboundInterceptor> outboundInterceptors) {
        this.outboundInterceptors = outboundInterceptors;
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        BundleService bundleService = methodBundleServiceMap.get(method);
        if (bundleService == null) {
            return methodInvocation.proceed();
        }
        MsoaRequestContext msoaRequestContext = new MsoaRequestContext(bundleService);
        msoaRequestContext.setOriginProvider("");
        for (OutboundInterceptor outboundInterceptor : outboundInterceptors) {
            boolean intercepted = outboundInterceptor.intercept(bundleService, methodInvocation);
            if (intercepted) {
                throw new IllegalAccessException("请求过快,请稍后再试");
            }
        }
        return methodInvocation.proceed();
    }
}
