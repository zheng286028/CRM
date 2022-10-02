package com.zzl.crm.workbench.service;

import com.zzl.crm.workbench.pojo.TranRemark;

import java.util.List;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/05/10  13:51
 */
public interface TranRemarkService {
    /**
     * 批量保存交易备注
     * @param tranRemarks
     * @return
     */
    int insertTranRemark(List<TranRemark> tranRemarks);
    /**
     * 根据transactionId查询备注
     * @param transactionId
     * @return
     */
    List<TranRemark>inquireTransactionRemarkByTransactionId(String transactionId);
}
