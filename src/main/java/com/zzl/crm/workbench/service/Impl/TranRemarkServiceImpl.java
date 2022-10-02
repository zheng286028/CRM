package com.zzl.crm.workbench.service.Impl;

import com.zzl.crm.workbench.mapper.TranRemarkMapper;
import com.zzl.crm.workbench.pojo.TranRemark;
import com.zzl.crm.workbench.service.TranRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/05/10  13:51
 */
@Service("TranRemarkService")
public class TranRemarkServiceImpl implements TranRemarkService {
    @Autowired
    private TranRemarkMapper tranRemarkMapper;
    /**
     * 批量保存交易备注
     * @param tranRemarks
     * @return
     */
    @Override
    public int insertTranRemark(List<TranRemark> tranRemarks) {
        return tranRemarkMapper.insertTranRemark(tranRemarks);
    }
    /**
     * 根据transactionId查询备注
     * @param transactionId
     * @return
     */
    @Override
    public List<TranRemark> inquireTransactionRemarkByTransactionId(String transactionId) {
        return tranRemarkMapper.inquireTransactionRemarkByTransactionId(transactionId);
    }
}
