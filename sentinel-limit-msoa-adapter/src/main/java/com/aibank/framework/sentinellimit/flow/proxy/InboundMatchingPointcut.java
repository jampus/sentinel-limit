package com.aibank.framework.sentinellimit.flow.proxy;

import com.baidu.ub.msoa.container.support.governance.annotation.BundleService;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.StaticMethodMatcher;
import org.springframework.aop.support.annotation.AnnotationClassFilter;

import java.lang.reflect.Method;

public class InboundMatchingPointcut implements Pointcut {
    private final ClassFilter classFilter;
    private final MethodMatcher methodMatcher;
    public InboundMatchingPointcut() {
        this.classFilter = new AnnotationClassFilter(BundleService.class, true);
        this.methodMatcher = new StaticMethodMatcher() {
            @Override
            public boolean matches(Method method, Class<?> aClass) {
                Class<?> interfaceType = aClass.getAnnotation(BundleService.class).interfaceType();
                for (Method interfaceTypeMethod : interfaceType.getMethods()) {
                    if (interfaceTypeMethod.getName().equals(method.getName())) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public ClassFilter getClassFilter() {
        return this.classFilter;
    }

    public MethodMatcher getMethodMatcher() {
        return this.methodMatcher;
    }
}