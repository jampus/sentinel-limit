package com.aibank.framework.sentinellimit.flow.domain;

import com.baidu.ub.msoa.container.support.governance.annotation.BundleService;

public class MsoaRequestContext {
    private String originProvider;
    private String provider;

    private String service;

    private String version;

    private String method;

    private Class<?> interfaceType;

    public MsoaRequestContext() {
    }

    public MsoaRequestContext(BundleService bundleService) {
        if (bundleService == null) {
            return;
        }
        this.method = bundleService.method();
        this.interfaceType = bundleService.interfaceType();
        this.version = bundleService.version();
        this.service = bundleService.service();
        if (bundleService.provider().length() >= 6) {
            this.provider = bundleService.provider().substring(0, 6);
        } else {
            this.provider = bundleService.provider();
        }
    }

    public String getOriginProvider() {
        return originProvider;
    }

    public void setOriginProvider(String originProvider) {
        this.originProvider = originProvider;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Class<?> getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(Class<?> interfaceType) {
        this.interfaceType = interfaceType;
    }
}
