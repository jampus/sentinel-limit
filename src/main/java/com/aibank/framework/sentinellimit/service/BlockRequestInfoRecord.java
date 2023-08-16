package com.aibank.framework.sentinellimit.service;

import com.aibank.framework.sentinellimit.dao.entity.BlockInfoEntity;

public interface BlockRequestInfoRecord {
    void blockInfoRecord(BlockInfoEntity blockInfoEntity) throws Exception;
}
