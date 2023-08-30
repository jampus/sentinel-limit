package com.aibank.framework.sentinellimit.flow.interceptor.limit;

import com.aibank.framework.sentinellimit.flow.interceptor.OutboundInterceptor;
import com.baidu.ub.msoa.container.support.governance.annotation.BundleService;
import org.aopalliance.intercept.MethodInvocation;

public class OutboundMethodLimitation extends OutboundInterceptor {
    @Override
    public String getResourceName(BundleService bundleService, MethodInvocation methodInvocation) {
        return bundleService.method();
    }
}
