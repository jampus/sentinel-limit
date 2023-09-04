package com.aibank.framework.sentinellimit.flow.interceptor;

import com.aibank.framework.sentinellimit.flow.domain.MsoaRequestContext;
import com.aibank.framework.sentinellimit.service.RateLimitUtil;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.baidu.ub.msoa.container.support.governance.annotation.BundleService;
import com.baidu.ub.msoa.utils.StringUtil;
import org.aopalliance.intercept.MethodInvocation;

public abstract class OutboundInterceptor implements MsoaBundleServiceInterceptor {
    public boolean intercept(MsoaRequestContext msoaRequestContext, MethodInvocation methodInvocation) {
        String resourceName = getResourceName(msoaRequestContext, methodInvocation);
        if (StringUtil.isBlank(resourceName)) {
            return false;
        }
        boolean success = RateLimitUtil.entry(resourceName, EntryType.OUT);
        return !success;
    }
    @Override
    public String getOrigin(MsoaRequestContext bundleService, MethodInvocation methodInvocation) {
        return RuleConstant.LIMIT_APP_DEFAULT;
    }
}
