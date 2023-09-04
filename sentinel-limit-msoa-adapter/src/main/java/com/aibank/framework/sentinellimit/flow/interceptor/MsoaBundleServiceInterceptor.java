package com.aibank.framework.sentinellimit.flow.interceptor;

import com.aibank.framework.sentinellimit.flow.domain.MsoaRequestContext;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.baidu.ub.msoa.container.support.governance.annotation.BundleService;
import org.aopalliance.intercept.MethodInvocation;

public interface MsoaBundleServiceInterceptor {

    boolean intercept(MsoaRequestContext bundleService, MethodInvocation methodInvocation);

    String getResourceName(MsoaRequestContext bundleService, MethodInvocation methodInvocation);


    String getOrigin(MsoaRequestContext bundleService, MethodInvocation methodInvocation);
}
