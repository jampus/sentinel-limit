package com.aibank.framework.sentinellimit.flow.interceptor;

import com.aibank.framework.sentinellimit.flow.domain.MsoaRequestContext;
import com.baidu.ub.msoa.container.support.governance.annotation.BundleService;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.List;

public class InboundMethodInterceptor implements MethodInterceptor {
    private List<InboundInterceptor> inboundInterceptors;
    private BlockHandler blockHandler;

    public InboundMethodInterceptor(List<InboundInterceptor> inboundInterceptors, BlockHandler blockHandler) {
        this.blockHandler = blockHandler;
        this.inboundInterceptors = inboundInterceptors;
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        BundleService bundleService = methodInvocation.getThis().getClass().getAnnotation(BundleService.class);
        MsoaRequestContext msoaRequestContext = new MsoaRequestContext(bundleService);
        msoaRequestContext.setOriginProvider("");
        for (InboundInterceptor inboundInterceptor : inboundInterceptors) {
            boolean intercepted = inboundInterceptor.intercept(bundleService, methodInvocation);
            if (intercepted) {
                return blockHandler.handle();
            }
        }
        return methodInvocation.proceed();
    }
}
