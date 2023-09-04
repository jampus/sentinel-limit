package com.aibank.framework.sentinellimit.flow.interceptor.limit;

import com.aibank.framework.sentinellimit.flow.domain.MsoaRequestContext;
import com.aibank.framework.sentinellimit.flow.interceptor.InboundInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class InboundMethodOriginLimitation extends InboundInterceptor {
    @Override
    public String getResourceName(MsoaRequestContext msoaRequestContext, MethodInvocation methodInvocation) {
        return msoaRequestContext.getService() + "_" + msoaRequestContext.getMethod();
    }


    @Override
    public String getOrigin(MsoaRequestContext bundleService, MethodInvocation methodInvocation) {
        return bundleService.getOriginProvider();
    }
}
