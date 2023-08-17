package com.aibank.framework.sentinellimit.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.db.Entity;
import com.aibank.framework.sentinellimit.dao.BlockInfoRecordMapper;
import com.aibank.framework.sentinellimit.dao.BlockInfoRecordMapperImpl;
import com.aibank.framework.sentinellimit.dao.entity.FlowRuleEntity;
import com.aibank.framework.sentinellimit.dao.entity.SystemRuleEntity;
import com.aibank.framework.sentinellimit.datasource.JdbcDataSource;
import com.aibank.framework.sentinellimit.domain.LimitConstants;
import com.aibank.framework.sentinellimit.rule.GlobalOverloadConfig;
import com.aibank.framework.sentinellimit.rule.OverloadFlowRuleManager;
import com.aibank.framework.sentinellimit.task.MetricPrintTask;
import com.aibank.framework.sentinellimit.task.RecordsBlockTask;
import com.alibaba.csp.sentinel.concurrent.NamedThreadFactory;
import com.alibaba.csp.sentinel.datasource.*;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DefaultFlowLimitLoad implements FlowLimitLoad {
    private DataSource dataSource;

    private String app;

    private final ScheduledExecutorService executorService;
    private BlockInfoRecordMapper blockInfoRecordMapper;


    public DefaultFlowLimitLoad(DataSource dataSource, String app) {
        this.dataSource = dataSource;
        this.app = app;
        LimitConstants.app = app;
        executorService = new ScheduledThreadPoolExecutor(5, new NamedThreadFactory("sentinel-record-task", true));
        blockInfoRecordMapper = new BlockInfoRecordMapperImpl(dataSource);
    }

    public DefaultFlowLimitLoad(DataSource dataSource, String app, ScheduledExecutorService executorService, BlockInfoRecordMapper blockInfoRecordMapper) {
        this.dataSource = dataSource;
        this.app = app;
        LimitConstants.app = app;
        this.executorService = executorService;
        this.blockInfoRecordMapper = blockInfoRecordMapper;
    }

    public void init() {
        // 正常限流
        flowRule();
        // 系统高负荷限流
        overloadFlowRule();
        //系统负载限流
        systemRule();
        // 启动定时任务
        scheduleTask();
    }

    protected void scheduleTask() {
        executorService.scheduleWithFixedDelay(new RecordsBlockTask(blockInfoRecordMapper), 0, 1, TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(new MetricPrintTask(), 0, 1, TimeUnit.SECONDS);
    }

    public void close() {
        executorService.shutdown();
    }


    //TODO  sql 写在 mapper 里面
    @Override
    public void flowRule() {
        String sql = "SELECT id, app, resource, control_behavior, count, grade, limit_app, strategy, effect_on_over_load, open, create_time," +
                "update_time, created_by, updated_by FROM flow_rule where open = 1 and effect_on_over_load = 0 and app='" + app + "' ";
        ReadableDataSource<List<Entity>, List<FlowRule>> readableDataSource = new JdbcDataSource<>(dataSource, sql, source -> source.stream().map(s -> {
            FlowRuleEntity flowRuleEntity = new FlowRuleEntity();
            BeanUtil.fillBeanWithMap(s, flowRuleEntity, true, true);
            FlowRule flowRule = new FlowRule();
            BeanUtil.copyProperties(flowRuleEntity, flowRule);
            return flowRule;
        }).collect(Collectors.toList()));
        FlowRuleManager.register2Property(readableDataSource.getProperty());
    }

    @Override
    public void overloadFlowRule() {
        String sql = "SELECT id, app, resource, control_behavior, count, grade, limit_app, strategy, effect_on_over_load, open, create_time," +
                "update_time, created_by, updated_by FROM flow_rule where open = 1 and effect_on_over_load = 1 and app='" + app + "' ";
        ReadableDataSource<List<Entity>, List<FlowRule>> readableDataSource = new JdbcDataSource<>(dataSource, sql, source -> source.stream().map(s -> {
            FlowRuleEntity flowRuleEntity = new FlowRuleEntity();
            BeanUtil.fillBeanWithMap(s, flowRuleEntity, true, true);
            FlowRule flowRule = new FlowRule();
            BeanUtil.copyProperties(flowRuleEntity, flowRule);
            return flowRule;
        }).collect(Collectors.toList()));
        OverloadFlowRuleManager.register2Property(readableDataSource.getProperty());
    }

    @Override
    public void systemRule() {
        String sql = "select id, app, highest_system_load, highest_cpu_usage, qps, avg_rt, max_thread,system_overload_flag, open, create_time, update_time, created_by, updated_by from system_rule where open = 1 and app='" + app + "'";
        ReadableDataSource<List<Entity>, List<SystemRule>> readableDataSource = new JdbcDataSource<>(dataSource, sql, source -> source.stream().map(s -> {
            SystemRuleEntity systemRuleEntity = new SystemRuleEntity();
            BeanUtil.fillBeanWithMap(s, systemRuleEntity, true, true);

            SystemRule systemRule = new SystemRule();
            BeanUtil.copyProperties(systemRuleEntity, systemRule);

            if (systemRuleEntity.getSystemOverloadFlag()) {
                GlobalOverloadConfig.loadConfig(systemRule);
                return null;
            } else {
                return systemRule;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList()));
        SystemRuleManager.register2Property(readableDataSource.getProperty());
    }
}
