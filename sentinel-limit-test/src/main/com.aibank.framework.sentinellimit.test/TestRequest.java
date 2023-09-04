package com.aibank.framework.sentinellimit.test;

import com.aibank.framework.sentinellimit.flow.domain.BaseRequest;

public class TestRequest extends BaseRequest {

    private String identifyType;
    private String messageCode;
    private String productId;
    private String sourceAppId;
    private String sourcePlatformId;
    private String uniqueCustomerId;
    private String version;


    public String getIdentifyType() {
        return identifyType;
    }

    public void setIdentifyType(String identifyType) {
        this.identifyType = identifyType;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSourceAppId() {
        return sourceAppId;
    }

    public void setSourceAppId(String sourceAppId) {
        this.sourceAppId = sourceAppId;
    }

    public String getSourcePlatformId() {
        return sourcePlatformId;
    }

    public void setSourcePlatformId(String sourcePlatformId) {
        this.sourcePlatformId = sourcePlatformId;
    }

    public String getUniqueCustomerId() {
        return uniqueCustomerId;
    }

    public void setUniqueCustomerId(String uniqueCustomerId) {
        this.uniqueCustomerId = uniqueCustomerId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
