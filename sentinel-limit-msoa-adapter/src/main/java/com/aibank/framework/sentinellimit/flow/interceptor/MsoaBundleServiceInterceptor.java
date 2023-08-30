package com.aibank.framework.sentinellimit.flow.interceptor;

import com.baidu.ub.msoa.container.support.governance.annotation.BundleService;
import org.aopalliance.intercept.MethodInvocation;

public interface MsoaBundleServiceInterceptor {

    boolean intercept(BundleService bundleService, MethodInvocation methodInvocation);

    String getResourceName(BundleService bundleService, MethodInvocation methodInvocation);
}
