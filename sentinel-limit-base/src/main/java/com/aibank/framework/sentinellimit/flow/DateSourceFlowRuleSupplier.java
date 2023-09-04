package com.aibank.framework.sentinellimit.flow;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.aibank.framework.sentinellimit.dao.FlowRuleMapper;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;

import java.util.List;
import java.util.stream.Collectors;

public class DateSourceFlowRuleSupplier implements FlowRuleSupplier {
    private FlowRuleMapper flowRuleMapper;

    public DateSourceFlowRuleSupplier(FlowRuleMapper flowRuleMapper) {
        this.flowRuleMapper = flowRuleMapper;
    }

    @Override
    public List<FlowRule> getFlowRule() {
        List<FlowRule> flowRules = flowRuleMapper.getAllFlowRule().stream().map(s -> {
            FlowRule flowRule = new FlowRule();
            BeanUtil.copyProperties(s, flowRule, CopyOptions.create().ignoreNullValue());
            return flowRule;
        }).collect(Collectors.toList());
        return flowRules;
    }

    @Override
    public int getOrder() {
        return 2000;
    }
}
