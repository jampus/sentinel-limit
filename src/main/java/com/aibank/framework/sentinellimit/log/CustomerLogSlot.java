package com.aibank.framework.sentinellimit.log;

import com.alibaba.csp.sentinel.Constants;
import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.log.RecordLog;
import com.alibaba.csp.sentinel.node.DefaultNode;
import com.alibaba.csp.sentinel.slotchain.AbstractLinkedProcessorSlot;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.spi.Spi;

@Spi(order = Constants.ORDER_LOG_SLOT)
public class CustomerLogSlot extends AbstractLinkedProcessorSlot<DefaultNode> {

    @Override
    public void entry(Context context, ResourceWrapper resourceWrapper, DefaultNode obj, int count, boolean prioritized, Object... args)
        throws Throwable {
        try {
            fireEntry(context, resourceWrapper, obj, count, prioritized, args);
        } catch (BlockException e) {
           // TODO  打印日志,入库
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer
                    .append("totalRequest: ").append(obj.totalRequest()).append("\n")
                    .append("totalPass: ").append(obj.totalPass()).append("\n")
                    .append("totalSuccess: ").append(obj.totalSuccess()).append("\n")
                    .append("totalException: ").append(obj.totalException()).append("\n")
                    .append("blockQps: ").append(obj.blockQps()).append("\n")
                    .append("passQps: ").append(obj.passQps()).append("\n")
                    .append("successQps: ").append(obj.successQps()).append("\n")
                    .append("exceptionQps: ").append(obj.exceptionQps()).append("\n")
                    .append("blockQps: ").append(obj.blockQps()).append("\n")
                    .append("rt: ").append(obj.avgRt()).append("\n")
                    .append("threadNum: ").append(obj.curThreadNum()).append("\n");

                 
            System.out.println(stringBuffer);
        } catch (Throwable e) {
            RecordLog.warn("Unexpected entry exception", e);
        }

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
