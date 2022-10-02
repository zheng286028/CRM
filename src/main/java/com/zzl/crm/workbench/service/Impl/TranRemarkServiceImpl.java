package com.zzl.crm.workbench.service.Impl;

import com.zzl.crm.workbench.mapper.TranRemarkMapper;
import com.zzl.crm.workbench.pojo.TranRemark;
import com.zzl.crm.workbench.service.TranRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ��������
 *
 * @author ֣����
 * @date 2022/05/10  13:51
 */
@Service("TranRemarkService")
public class TranRemarkServiceImpl implements TranRemarkService {
    @Autowired
    private TranRemarkMapper tranRemarkMapper;
    /**
     * �������潻�ױ�ע
     * @param tranRemarks
     * @return
     */
    @Override
    public int insertTranRemark(List<TranRemark> tranRemarks) {
        return tranRemarkMapper.insertTranRemark(tranRemarks);
    }
    /**
     * ����transactionId��ѯ��ע
     * @param transactionId
     * @return
     */
    @Override
    public List<TranRemark> inquireTransactionRemarkByTransactionId(String transactionId) {
        return tranRemarkMapper.inquireTransactionRemarkByTransactionId(transactionId);
    }
}
