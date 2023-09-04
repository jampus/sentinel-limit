package com.aibank.framework.sentinellimit.flow.interceptor.limit;

import com.aibank.framework.sentinellimit.flow.domain.MsoaRequestContext;
import com.aibank.framework.sentinellimit.flow.interceptor.OutboundInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class OutboundMethodLimitation extends OutboundInterceptor {
    @Override
    public String getResourceName(MsoaRequestContext msoaRequestContext, MethodInvocation methodInvocation) {
        return msoaRequestContext.getProvider() + "_" + msoaRequestContext.getService() + "_" + msoaRequestContext.getMethod();
    }
}
