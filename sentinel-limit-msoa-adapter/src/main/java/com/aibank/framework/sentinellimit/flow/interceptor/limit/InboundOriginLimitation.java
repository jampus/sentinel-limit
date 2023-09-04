package com.aibank.framework.sentinellimit.flow.interceptor.limit;

import com.aibank.framework.sentinellimit.domain.LimitConstants;
import com.aibank.framework.sentinellimit.flow.domain.MsoaRequestContext;
import com.aibank.framework.sentinellimit.flow.interceptor.InboundInterceptor;
import com.aibank.framework.sentinellimit.service.RateLimitUtil;
import com.alibaba.csp.sentinel.EntryType;
import com.baidu.ub.msoa.utils.StringUtil;
import org.aopalliance.intercept.MethodInvocation;

public class InboundOriginLimitation extends InboundInterceptor {
    @Override
    public String getOrigin(MsoaRequestContext bundleService, MethodInvocation methodInvocation) {
        return bundleService.getOriginProvider();
    }
    public boolean intercept(MsoaRequestContext msoaRequestContext, MethodInvocation methodInvocation) {
        String resourceName = getResourceName(msoaRequestContext, methodInvocation);
        if (StringUtil.isBlank(resourceName)) {
            return false;
        }
        String origin = getOrigin(msoaRequestContext, methodInvocation);
        //trafficType 为 out ,这样便不会统计进入整个系统的入口流量
        boolean success = RateLimitUtil.entry(resourceName, origin, EntryType.OUT);
        return !success;
    }
    @Override
    public String getResourceName(MsoaRequestContext msoaRequestContext, MethodInvocation methodInvocation) {
        return LimitConstants.ANY_RESOURCE;
    }
}
