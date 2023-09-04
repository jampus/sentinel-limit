package com.aibank.framework.sentinellimit.flow.interceptor;

import com.aibank.framework.sentinellimit.domain.LimitConstants;
import com.aibank.framework.sentinellimit.domain.TransIdHolder;
import com.aibank.framework.sentinellimit.flow.domain.BaseRequest;
import com.aibank.framework.sentinellimit.flow.domain.MsoaRequestContext;
import com.aibank.framework.sentinellimit.flow.util.RequestContentUtils;
import com.baidu.ub.msoa.container.support.governance.annotation.BundleService;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InboundMethodInterceptor implements MethodInterceptor, ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(InboundMethodInterceptor.class);

    private List<InboundInterceptor> inboundInterceptors = new ArrayList<>();
    private BlockHandler blockHandler;

    public InboundMethodInterceptor(BlockHandler blockHandler) {
        this.blockHandler = blockHandler;
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        try {
            BundleService bundleService = methodInvocation.getThis().getClass().getAnnotation(BundleService.class);
            MsoaRequestContext msoaRequestContext = new MsoaRequestContext(bundleService);
            String originProviderId;
            Object[] arguments = methodInvocation.getArguments();
            if (arguments.length > 0 && arguments[0] instanceof BaseRequest) {
                BaseRequest baseRequest = (BaseRequest) arguments[0];
                originProviderId = RequestContentUtils.getOriginProviderId(baseRequest);
                TransIdHolder.setTransId(baseRequest.getHead().getTransId());
            } else {
                originProviderId = LimitConstants.DEFAULT_ORIGIN;
            }
            msoaRequestContext.setOriginProvider(originProviderId);
            msoaRequestContext.setProvider(LimitConstants.app);

            msoaRequestContext.setMethod(methodInvocation.getMethod().getName());
            for (InboundInterceptor inboundInterceptor : inboundInterceptors) {
                boolean intercepted = inboundInterceptor.intercept(msoaRequestContext, methodInvocation);
                if (intercepted) {
                    return blockHandler.handle();
                }
            }
        } catch (Throwable throwable) {
            logger.error("outbound throwable:  ", throwable);
            return methodInvocation.proceed();
        }
        return methodInvocation.proceed();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        Map<String, InboundInterceptor> interceptorMap = applicationContext.getBeansOfType(InboundInterceptor.class);
        if (!interceptorMap.isEmpty()) {
            inboundInterceptors = new ArrayList<>(interceptorMap.values());
        }
    }
}
