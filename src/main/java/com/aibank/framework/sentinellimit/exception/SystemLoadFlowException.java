package com.aibank.framework.sentinellimit.exception;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;

/***
 * @author youji.zj
 */
public class SystemLoadFlowException extends BlockException {

    public SystemLoadFlowException(String ruleLimitApp) {
        super(ruleLimitApp);
    }

    public SystemLoadFlowException(String ruleLimitApp, FlowRule rule) {
        super(ruleLimitApp, rule);
    }

    public SystemLoadFlowException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemLoadFlowException(String ruleLimitApp, String message) {
        super(ruleLimitApp, message);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    /**
     * Get triggered rule.
     * Note: the rule result is a reference to rule map and SHOULD NOT be modified.
     *
     * @return triggered rule
     * @since 1.4.2
     */
    @Override
    public FlowRule getRule() {
        return rule.as(FlowRule.class);
    }
}
