package com.aibank.framework.sentinellimit.slot;

import com.aibank.framework.sentinellimit.domain.LimitData;
import com.aibank.framework.sentinellimit.enums.LimitType;
import com.aibank.framework.sentinellimit.enums.SystemLimitType;
import com.aibank.framework.sentinellimit.exception.OverloadFlowException;
import com.alibaba.csp.sentinel.Constants;
import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.log.RecordLog;
import com.alibaba.csp.sentinel.node.DefaultNode;
import com.alibaba.csp.sentinel.slotchain.AbstractLinkedProcessorSlot;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import com.alibaba.csp.sentinel.slots.block.AbstractRule;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.csp.sentinel.spi.Spi;

@Spi(order = Constants.ORDER_LOG_SLOT)
public class BlockLogSlot extends AbstractLinkedProcessorSlot<DefaultNode> {

    @Override
    public void entry(Context context, ResourceWrapper resourceWrapper, DefaultNode obj, int count, boolean prioritized, Object... args)
            throws Throwable {
        try {
            fireEntry(context, resourceWrapper, obj, count, prioritized, args);
        } catch (BlockException e) {
            LimitData limitData = getLimitData(e, obj, count);
            // TODO  打印日志,入库

            throw e;
        } catch (Throwable e) {
            RecordLog.warn("Unexpected entry exception", e);
        }
    }

    private LimitData getLimitData(BlockException e, DefaultNode node, int count) {
        LimitData limitData = new LimitData();
        if (e instanceof FlowException) {
            FlowRule rule = (FlowRule) e.getRule();
            limitData.setLimitType(LimitType.flowRule);
            //只考虑 qps,不考虑线程数
            limitData.setLimitValue(node.passQps());
            limitData.setLimitConfigValue(rule.getCount());
        } else if (e instanceof SystemBlockException) {
            SystemBlockException systemBlockException = (SystemBlockException) e;
            SystemLimitType systemLimitType = SystemLimitType.valueOf(systemBlockException.getLimitType());

            //非限流那一刻的值,可能有误差
            double overloadConfigValue = 0;
            double overloadValue = 0;
            switch (systemLimitType) {
                case rt:
                    overloadConfigValue = SystemRuleManager.getRtThreshold();
                    overloadValue = Constants.ENTRY_NODE.avgRt();
                    break;
                case qps:
                    overloadConfigValue = SystemRuleManager.getInboundQpsThreshold();
                    overloadValue = Constants.ENTRY_NODE.passQps() + count;
                    break;
                case cpu:
                    overloadConfigValue = SystemRuleManager.getCpuUsageThreshold();
                    overloadValue = SystemRuleManager.getCurrentCpuUsage();
                    break;
                case load:
                    overloadConfigValue = SystemRuleManager.getSystemLoadThreshold();
                    overloadValue = SystemRuleManager.getCurrentSystemAvgLoad();
                    break;
                case thread:
                    overloadConfigValue = SystemRuleManager.getMaxThreadThreshold();
                    overloadValue = Constants.ENTRY_NODE.curThreadNum();
                    break;

            }
            limitData.setLimitType(LimitType.systemRule);
            limitData.setSystemLimitType(systemLimitType);
            limitData.setOverloadConfigValue(overloadConfigValue);
            limitData.setOverloadValue(overloadValue);
        } else if (e instanceof OverloadFlowException) {
            limitData = ((OverloadFlowException) e).getLimitData();
        }
        return limitData;
    }

    @Override
    public void exit(Context context, ResourceWrapper resourceWrapper, int count, Object... args) {
        try {
            fireExit(context, resourceWrapper, count, args);
        } catch (Throwable e) {
            RecordLog.warn("Unexpected entry exit exception", e);
        }
    }
}
