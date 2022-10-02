package com.zzl.crm.workbench.service;

import com.zzl.crm.workbench.pojo.FunnelVO;
import com.zzl.crm.workbench.pojo.Tran;

import java.util.List;
import java.util.Map;

/**
 * ��������
 *
 * @author ֣����
 * @date 2022/05/10  13:32
 */
public interface TranService {
    /**
     * ��ӽ���
     * @param tran
     * @return
     */
    int insertTran(Tran tran);
    /**
     * ���ݿͻ�id��ѯ��ǰ�ͻ��µĽ���
     * @return
     */
    List<Tran> selectTranDetailed();

    /**
     * ��ӽ���
     * @param map
     * @return
     */
    void enterTransaction(Map<String,Object> map);
    /**
     * ��ҳ������ѯ
     * @param map
     * @return
     */
    List<Tran> inquireTransactionByPageAndCondition(Map<String,Object>map);

    /**
     * ��ѯ�����ܼ�¼��
     * @param map
     * @return
     */
    int inquireTransactionTotalCountByCondition(Map<String,Object>map);
    /**
     * ����id��ѯ
     * @param id
     * @return
     */
    Tran inquireTransactionById(String id);
    /**
     * ����id�޸�
     * @param map
     * @return
     */
    void reviseTransactionById(Map<String,Object>map);

    /**
     * ��ѯ����ͳ��ͼ��
     * @return
     */
    List<FunnelVO> selectCountOfTranGroupByStage();
}
