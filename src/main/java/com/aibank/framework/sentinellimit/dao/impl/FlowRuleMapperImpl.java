package com.aibank.framework.sentinellimit.dao.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.aibank.framework.sentinellimit.dao.FlowRuleMapper;
import com.aibank.framework.sentinellimit.dao.entity.FlowRuleEntity;
import com.aibank.framework.sentinellimit.domain.LimitConstants;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class FlowRuleMapperImpl implements FlowRuleMapper {

    private DataSource dataSource;

    public FlowRuleMapperImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<FlowRuleEntity> getAllFlowRule() {
        String app = LimitConstants.app;
        String sql = "SELECT id, app, resource, control_behavior,max_queueing_time_ms, count, grade, limit_app, strategy, effect_on_over_load, open, create_time," +
                "update_time, created_by, updated_by FROM flow_rule where open = 1 and effect_on_over_load = 0 and app='" + app + "' ";
        List<Entity> list;
        try {
            list = Db.use(dataSource).query(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        List<FlowRuleEntity> ruleEntities = list.stream().map(s -> {
            FlowRuleEntity flowRuleEntity = new FlowRuleEntity();
            BeanUtil.fillBeanWithMap(s, flowRuleEntity, true, true);
            return flowRuleEntity;
        }).collect(Collectors.toList());
        return ruleEntities;
    }

    @Override
    public List<FlowRuleEntity> getAllOverloadFlowRule() {
        String app = LimitConstants.app;
        String sql = "SELECT id, app, resource, control_behavior,max_queueing_time_ms, count, grade, limit_app, strategy, effect_on_over_load, open, create_time," +
                "update_time, created_by, updated_by FROM flow_rule where open = 1 and effect_on_over_load = 1 and app='" + app + "' ";
        List<Entity> list;
        try {
            list = Db.use(dataSource).query(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        List<FlowRuleEntity> ruleEntities = list.stream().map(s -> {
            FlowRuleEntity flowRuleEntity = new FlowRuleEntity();
            BeanUtil.fillBeanWithMap(s, flowRuleEntity, true, true);
            return flowRuleEntity;
        }).collect(Collectors.toList());
        return ruleEntities;
    }
}
