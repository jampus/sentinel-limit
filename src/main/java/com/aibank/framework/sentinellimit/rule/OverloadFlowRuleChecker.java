package com.aibank.framework.sentinellimit.rule;

import com.alibaba.csp.sentinel.Constants;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.node.DefaultNode;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleChecker;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.csp.sentinel.util.function.Function;

import java.util.Collection;

public class OverloadFlowRuleChecker extends FlowRuleChecker {

    public void checkFlow(Function<String, Collection<FlowRule>> ruleProvider, ResourceWrapper resource,
                          Context context, DefaultNode node, int count, boolean prioritized) throws BlockException {
        if (ruleProvider == null || resource == null) {
            return;
        }
        Collection<FlowRule> rules = ruleProvider.apply(resource.getName());
        if (rules != null) {
            for (FlowRule rule : rules) {
                if (!canPassCheck(resource, rule, context, node, count, prioritized)) {
                    throw new FlowException(rule.getLimitApp(), rule);
                }
            }
        }
    }

    public boolean canPassCheck(ResourceWrapper resource, FlowRule rule, Context context, DefaultNode node, int acquireCount, boolean prioritized) {
        if (triggerSystemOverload(resource, acquireCount)) {
            return super.canPassCheck(rule, context, node, acquireCount, prioritized);
        }
        return true;
    }

    public static boolean triggerSystemOverload(ResourceWrapper resourceWrapper, int count) {
        if (resourceWrapper == null) {
            return false;
        }
        // Ensure the checking switch is on.
        if (!GlobalOverloadConfig.isCheckSystemStatus()) {
            return false;
        }

        // for inbound traffic only
        if (resourceWrapper.getEntryType() != EntryType.IN) {
            return false;
        }
        // total qps
        double currentQps = Constants.ENTRY_NODE.passQps();
        if (currentQps + count > GlobalOverloadConfig.getMaxQps()) {
            return true;
        }

        // total thread
        int currentThread = Constants.ENTRY_NODE.curThreadNum();
        if (currentThread > GlobalOverloadConfig.getMaxThread()) {
            return true;
        }

        double rt = Constants.ENTRY_NODE.avgRt();
        if (rt > GlobalOverloadConfig.getMaxRt()) {
            return true;
        }

        // load. BBR algorithm.
        if (SystemRuleManager.getCurrentSystemAvgLoad() > GlobalOverloadConfig.getMaxSystemLoad()) {
            if (!checkBbr(currentThread)) {
                return true;
            }
        }

        // cpu usage
        if (SystemRuleManager.getCurrentCpuUsage() > GlobalOverloadConfig.getMaxCpuUsage()) {
            return true;
        }
        return false;
    }

    private static boolean checkBbr(int currentThread) {
        return currentThread <= 1 || !((double) currentThread > Constants.ENTRY_NODE.maxSuccessQps() * Constants.ENTRY_NODE.minRt() / 1000.0);
    }

}
