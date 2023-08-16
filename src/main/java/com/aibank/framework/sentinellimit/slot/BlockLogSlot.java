package com.aibank.framework.sentinellimit.slot;

import com.aibank.framework.sentinellimit.dao.entity.BlockInfoEntity;
import com.aibank.framework.sentinellimit.service.DefaultBlockRequestInfoRecord;
import com.alibaba.csp.sentinel.Constants;
import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.log.RecordLog;
import com.alibaba.csp.sentinel.node.DefaultNode;
import com.alibaba.csp.sentinel.slotchain.AbstractLinkedProcessorSlot;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.spi.Spi;
import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;
import java.util.Date;

@Spi(order = Constants.ORDER_LOG_SLOT)
public class BlockLogSlot extends AbstractLinkedProcessorSlot<DefaultNode> {

    public static DataSource createDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/sentinel");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setInitialSize(5);
        dataSource.setMinIdle(5);
        dataSource.setMaxActive(20);
        dataSource.setMaxWait(60000);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setValidationQuery("SELECT 1 FROM DUAL");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
        return dataSource;
    }

    @Override
    public void entry(Context context, ResourceWrapper resourceWrapper, DefaultNode obj, int count, boolean prioritized, Object... args)
        throws Throwable {
        try {
            fireEntry(context, resourceWrapper, obj, count, prioritized, args);
        } catch (BlockException e) {
           // TODO  打印日志,入库
//            StringBuffer stringBuffer = new StringBuffer();
//            stringBuffer
//                    .append("totalRequest: ").append(obj.totalRequest()).append("\n")
//                    .append("totalPass: ").append(obj.totalPass()).append("\n")
//                    .append("totalSuccess: ").append(obj.totalSuccess()).append("\n")
//                    .append("totalException: ").append(obj.totalException()).append("\n")
//                    .append("blockQps: ").append(obj.blockQps()).append("\n")
//                    .append("passQps: ").append(obj.passQps()).append("\n")
//                    .append("successQps: ").append(obj.successQps()).append("\n")
//                    .append("exceptionQps: ").append(obj.exceptionQps()).append("\n")
//                    .append("blockQps: ").append(obj.blockQps()).append("\n")
//                    .append("rt: ").append(obj.avgRt()).append("\n")
//                    .append("curTheadNum: ").append(obj.curThreadNum()).append("\n")
//                    .append("metric: ").append(obj.metrics()).append("\n")
//                    .append("totalQps: ").append(obj.totalQps()).append("\n")
//                    .append("occupiedPassQps: ").append(obj.occupiedPassQps());

            DataSource dataSource = createDataSource();
            DefaultBlockRequestInfoRecord defaultBlockRequestInfoRecord = new DefaultBlockRequestInfoRecord(dataSource, "237000");
            BlockInfoEntity blockInfoEntity = new BlockInfoEntity();
            blockInfoEntity.setId(System.currentTimeMillis());
            blockInfoEntity.setResource(resourceWrapper.getName());
            blockInfoEntity.setTotalRequest(obj.totalRequest());
            blockInfoEntity.setTotalPass(obj.totalPass());
            blockInfoEntity.setTotalSuccess(obj.totalSuccess());
            blockInfoEntity.setTotalQps(obj.totalQps());
            blockInfoEntity.setPassQps(obj.passQps());
            blockInfoEntity.setBlockQps(obj.blockQps());
            blockInfoEntity.setSuccessQps(obj.successQps());
            blockInfoEntity.setCurTheadNum(obj.curThreadNum());
            blockInfoEntity.setAvgRt(obj.avgRt());
            blockInfoEntity.setCreateTime(new Date());
            defaultBlockRequestInfoRecord.blockInfoRecord(blockInfoEntity);

           // System.out.println(stringBuffer);
            throw e;
        } catch (Throwable e) {
            System.out.println(e.getStackTrace());
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
