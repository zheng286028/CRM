package com.zzl.crm.workbench.service.Impl;

import com.zzl.crm.workbench.mapper.TranHistoryMapper;
import com.zzl.crm.workbench.pojo.TranHistory;
import com.zzl.crm.workbench.service.TranHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ��������
 *
 * @author ֣����
 * @date 2022/05/15  10:56
 */
@Service("tranHistoryService")
public class TranHistoryServiceImpl implements TranHistoryService {
    @Autowired
    private TranHistoryMapper tranHistoryMapper;

    /**
     * ���洴���Ľ�����ʷ
     * @param tranHistory
     * @return
     */
    @Override
    public int saveCreateTranHistory(TranHistory tranHistory) {
        return tranHistoryMapper.saveCreateTranHistory(tranHistory);
    }
    /**
     * ��ѯ��ǰ�����µ���ʷ��Ϣ
     * @param tranId
     * @return
     */
    @Override
    public List<TranHistory> inquireTranHistoryByTranId(String tranId) {
        return tranHistoryMapper.inquireTranHistoryByTranId(tranId);
    }
}
