package com.zzl.crm.workbench.service;

import com.zzl.crm.workbench.pojo.TranRemark;

import java.util.List;

/**
 * ��������
 *
 * @author ֣����
 * @date 2022/05/10  13:51
 */
public interface TranRemarkService {
    /**
     * �������潻�ױ�ע
     * @param tranRemarks
     * @return
     */
    int insertTranRemark(List<TranRemark> tranRemarks);
    /**
     * ����transactionId��ѯ��ע
     * @param transactionId
     * @return
     */
    List<TranRemark>inquireTransactionRemarkByTransactionId(String transactionId);
}
