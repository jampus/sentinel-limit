package com.aibank.framework.sentinellimit.dao.entity;

import java.util.Date;

/*create table `block_info` (
        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '阻塞信息id',
        `app` varchar(50) DEFAULT NULL COMMENT '系统编码',
        `resource` varchar(255) DEFAULT NULL COMMENT '资源名称',
        `totalRequest` bigint(20) DEFAULT NULL COMMENT '最近1分钟内的请求数',
        `totalPass` bigint(20) DEFAULT NULL COMMENT '最近1分钟内通过规则检测的请求数',
        `totalSuccess` bigint(20) DEFAULT NULL COMMENT '最近1分钟内通过规则检测完成调用并返回的请求数量',
        `totalQps` bigint(20) DEFAULT NULL COMMENT '每秒/此刻的请求数，包括（blockQps与passQps）',
        `passQps` bigint(20) DEFAULT NULL COMMENT '每秒/此刻通过规则检测的请求数量',
        `blockQps` bigint(20) DEFAULT NULL COMMENT '每秒/此刻该资源被拦截的数量',
        `successQps` bigint(20) DEFAULT NULL COMMENT '每秒/此刻通过规则检测完成调用并返回的请求数量',
        `curTheadNum` bigint(20) DEFAULT NULL COMMENT '此刻线程数量',
        `avgRt` bigint(20) DEFAULT NULL COMMENT '平均响应时间',
        `createTime` timestamp DEFAULT NULL COMMENT '每创建时间',
        PRIMARY KEY (`id`),
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Sentinel限流信息表'
*/

public class BlockInfoEntity {
    private Long id;    //阻塞信息id
    private String app;    //系统编码
    private String resource;    //资源名称

    private Long totalRequest;    //最近1分钟内的请求数
    private Long totalPass;    //最近1分钟内通过规则检测的请求数
    private Long totalSuccess;    //最近1分钟内通过规则检测完成调用并返回的请求数量
    private Double totalQps;    //每秒/此刻的请求数，包括（blockQps与passQps）
    private Double passQps;    //每秒/此刻通过规则检测的请求数量
    private Double blockQps;    //每秒/此刻该资源被拦截的数量
    private Double successQps;    //每秒/此刻通过规则检测完成调用并返回的请求数量
    private Integer curTheadNum;    //此刻线程数量
    private Double avgRt;    //平均响应时间
    private Date createTime;    //创建时间

    private Double exceptionQps;    //每秒/此刻异常的数量（不包括被拦截时所产生的异常）
    private Double occupiedPassQps;    //每秒/此刻占用未来请求的数目

    public BlockInfoEntity() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public void setTotalRequest(Long totalRequest) {
        this.totalRequest = totalRequest;
    }

    public void setTotalPass(Long totalPass) {
        this.totalPass = totalPass;
    }

    public void setTotalSuccess(Long totalSuccess) {
        this.totalSuccess = totalSuccess;
    }

    public void setTotalQps(Double totalQps) {
        this.totalQps = totalQps;
    }

    public void setPassQps(Double passQps) {
        this.passQps = passQps;
    }

    public void setBlockQps(Double blockQps) {
        this.blockQps = blockQps;
    }

    public void setSuccessQps(Double successQps) {
        this.successQps = successQps;
    }

    public void setCurTheadNum(Integer curTheadNum) {
        this.curTheadNum = curTheadNum;
    }

    public void setAvgRt(Double avgRt) {
        this.avgRt = avgRt;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public String getApp() {
        return app;
    }

    public String getResource() {
        return resource;
    }

    public Long getTotalRequest() {
        return totalRequest;
    }

    public Long getTotalPass() {
        return totalPass;
    }

    public Long getTotalSuccess() {
        return totalSuccess;
    }

    public Double getTotalQps() {
        return totalQps;
    }

    public Double getPassQps() {
        return passQps;
    }

    public Double getBlockQps() {
        return blockQps;
    }

    public Double getSuccessQps() {
        return successQps;
    }

    public Integer getCurTheadNum() {
        return curTheadNum;
    }

    public Double getAvgRt() {
        return avgRt;
    }

    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public String toString() {
        return "BlockInfoEntity{" +
                "id=" + id +
                ", app='" + app + '\'' +
                ", resource='" + resource + '\'' +
                ", totalRequest=" + totalRequest +
                ", totalPass=" + totalPass +
                ", totalSuccess=" + totalSuccess +
                ", totalQps=" + totalQps +
                ", passQps=" + passQps +
                ", blockQps=" + blockQps +
                ", successQps=" + successQps +
                ", curTheadNum=" + curTheadNum +
                ", avgRt=" + avgRt +
                ", createTime=" + createTime +
                '}';
    }
}
