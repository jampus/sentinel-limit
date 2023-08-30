package com.aibank.framework.sentinellimit.flow.interceptor.limit;

import com.aibank.framework.sentinellimit.flow.interceptor.InboundInterceptor;
import com.baidu.ub.msoa.container.support.governance.annotation.BundleService;
import org.aopalliance.intercept.MethodInvocation;


public class InboundMethodLimitation extends InboundInterceptor {
    @Override
    public String getResourceName(BundleService bundleService, MethodInvocation methodInvocation) {
        return bundleService.method();
    }
}