package com.zzl.crm.workbench.service;

import com.zzl.crm.workbench.pojo.TranHistory;

import java.util.List;

/**
 * ��������
 *
 * @author ֣����
 * @date 2022/05/15  10:55
 */
public interface TranHistoryService {
    /**
     * ���洴���Ľ�����ʷ
     * @param tranHistory
     * @return
     */
    int saveCreateTranHistory(TranHistory tranHistory);
    /**
     * ��ѯ��ǰ�����µ���ʷ��Ϣ
     * @param tranId
     * @return
     */
    List<TranHistory> inquireTranHistoryByTranId(String tranId);
}
