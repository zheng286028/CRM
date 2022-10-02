package com.zzl.crm.workbench.service;

import com.zzl.crm.workbench.pojo.Clue;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * ��������
 *
 * @author ֣����
 * @date 2022/05/05  19:36
 */
public interface ClueService {
    /**
     * �������
     * @param clue
     * @return
     */
    int insertClue(Clue clue);

    /**
     * ��ҳ������ѯ
     * @param map
     * @return
     */
    List<Clue> queryClueByPageAndCondition(Map<String,Object> map);

    /**
     * ������ѯ�ܼ�¼��
     * @param map
     * @return
     */
    int queryClueByConditionTotal(Map<String,Object> map);
    /**
     * ����id����ɾ��
     * @param ids
     * @return
     */
    int deleteClueByIds(String[] ids);
    /**
     * ����id��ѯ
     * @param id
     * @return
     */
    Clue selectById(String id);

    /**
     * ����id�޸�
     * @param clue
     * @return
     */
    int updateClueById(Clue clue);
    /**
     * ����id��ѯ��������ϸ��Ϣ���Ͳ��id��
     * @param id
     * @return
     */
    Clue selectClueById(String id);
    /**
     * ����idɾ����ǰ����
     * @param id
     * @return
     */
    int deleteClueById(String id);
}
