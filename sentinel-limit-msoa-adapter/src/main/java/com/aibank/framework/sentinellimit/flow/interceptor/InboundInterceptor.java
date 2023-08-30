package com.aibank.framework.sentinellimit.flow.interceptor;

import com.aibank.framework.sentinellimit.service.RateLimitUtil;
import com.alibaba.csp.sentinel.EntryType;
import com.baidu.ub.msoa.container.support.governance.annotation.BundleService;
import com.baidu.ub.msoa.utils.StringUtil;
import org.aopalliance.intercept.MethodInvocation;


public abstract class InboundInterceptor implements MsoaBundleServiceInterceptor {
    public boolean intercept(BundleService bundleService, MethodInvocation methodInvocation) {
        String resourceName = getResourceName(bundleService, methodInvocation);
        if (StringUtil.isBlank(resourceName)) {
            return false;
        }
        boolean success = RateLimitUtil.entry(resourceName, EntryType.IN);
        return !success;
    }
}
