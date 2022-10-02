package com.zzl.crm.workbench.service;

import com.zzl.crm.workbench.pojo.TranHistory;

import java.util.List;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/05/15  10:55
 */
public interface TranHistoryService {
    /**
     * 保存创建的交易历史
     * @param tranHistory
     * @return
     */
    int saveCreateTranHistory(TranHistory tranHistory);
    /**
     * 查询当前交易下的历史信息
     * @param tranId
     * @return
     */
    List<TranHistory> inquireTranHistoryByTranId(String tranId);
}
