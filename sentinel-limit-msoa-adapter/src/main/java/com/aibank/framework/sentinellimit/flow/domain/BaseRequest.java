package com.aibank.framework.sentinellimit.flow.domain;

public class BaseRequest {


    private RequestHead head;

    public class RequestHead {
        /**
         * async : true
         * branchId : 800001
         * destBranchNo : 01301
         * priority : 0
         * sceneId : 237000bsOPTCLO20190424
         * serviceId : 237000bsOPTCLO20190424
         * sourceBranchNo : uap999999
         * sourceServerId : bs1ificorap2001
         * sourceServiceId : 174000bsifi17420230904
         * sourceType : 9999
         * subTransSeq : 10000000000000000000
         * sysDate : 20230904
         * sysTimestamp : 20230904 143714742
         * transCode : OPT00UN015
         * transId : 230904bsifi1746685474659079600714
         * userId : CP1002
         * version : V1
         * wsId :
         */

        private boolean async;
        private String branchId;
        private String destBranchNo;
        private int priority;
        private String sceneId;
        private String serviceId;
        private String sourceBranchNo;
        private String sourceServerId;
        private String sourceServiceId;
        private String sourceType;
        private String subTransSeq;
        private String sysDate;
        private String sysTimestamp;
        private String transCode;
        private String transId;
        private String userId;
        private String version;
        private String wsId;

        public boolean isAsync() {
            return async;
        }

        public void setAsync(boolean async) {
            this.async = async;
        }

        public String getBranchId() {
            return branchId;
        }

        public void setBranchId(String branchId) {
            this.branchId = branchId;
        }

        public String getDestBranchNo() {
            return destBranchNo;
        }

        public void setDestBranchNo(String destBranchNo) {
            this.destBranchNo = destBranchNo;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public String getSceneId() {
            return sceneId;
        }

        public void setSceneId(String sceneId) {
            this.sceneId = sceneId;
        }

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public String getSourceBranchNo() {
            return sourceBranchNo;
        }

        public void setSourceBranchNo(String sourceBranchNo) {
            this.sourceBranchNo = sourceBranchNo;
        }

        public String getSourceServerId() {
            return sourceServerId;
        }

        public void setSourceServerId(String sourceServerId) {
            this.sourceServerId = sourceServerId;
        }

        public String getSourceServiceId() {
            return sourceServiceId;
        }

        public void setSourceServiceId(String sourceServiceId) {
            this.sourceServiceId = sourceServiceId;
        }

        public String getSourceType() {
            return sourceType;
        }

        public void setSourceType(String sourceType) {
            this.sourceType = sourceType;
        }

        public String getSubTransSeq() {
            return subTransSeq;
        }

        public void setSubTransSeq(String subTransSeq) {
            this.subTransSeq = subTransSeq;
        }

        public String getSysDate() {
            return sysDate;
        }

        public void setSysDate(String sysDate) {
            this.sysDate = sysDate;
        }

        public String getSysTimestamp() {
            return sysTimestamp;
        }

        public void setSysTimestamp(String sysTimestamp) {
            this.sysTimestamp = sysTimestamp;
        }

        public String getTransCode() {
            return transCode;
        }

        public void setTransCode(String transCode) {
            this.transCode = transCode;
        }

        public String getTransId() {
            return transId;
        }

        public void setTransId(String transId) {
            this.transId = transId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getWsId() {
            return wsId;
        }

        public void setWsId(String wsId) {
            this.wsId = wsId;
        }
    }

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

}
