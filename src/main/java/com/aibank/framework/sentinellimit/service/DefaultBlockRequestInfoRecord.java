package com.aibank.framework.sentinellimit.service;

import com.aibank.framework.sentinellimit.dao.entity.BlockInfoEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DefaultBlockRequestInfoRecord implements BlockRequestInfoRecord {
    private DataSource dataSource;

    private String app;

    public DefaultBlockRequestInfoRecord(DataSource dataSource, String app) {
        this.dataSource = dataSource;
        this.app = app;
    }

    public void write(DataSource dataSource, String sql) throws Exception {
        dataSource.getConnection();
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
        connection.close();
    }

    @Override
    public void blockInfoRecord(BlockInfoEntity blockInfoEntity) throws Exception {
        String sql = "insert into block_info (id,app,resource,totalRequest,totalPass,totalSuccess,totalQps,passQps,blockQps,successQps,avgRt,curTheadNum) values(" +
                 + blockInfoEntity.getId() + "," + app + "," + "\"" + blockInfoEntity.getResource() + "\"" + "," + blockInfoEntity.getTotalRequest() + "," +
                blockInfoEntity.getTotalPass() + "," + blockInfoEntity.getTotalSuccess() + "," + blockInfoEntity.getTotalQps() + "," +
                blockInfoEntity.getPassQps() + "," + blockInfoEntity.getBlockQps() + "," + blockInfoEntity.getSuccessQps() + "," +
                blockInfoEntity.getCurTheadNum() + "," + blockInfoEntity.getAvgRt() + ")";
        write(dataSource,sql);
    }
}
