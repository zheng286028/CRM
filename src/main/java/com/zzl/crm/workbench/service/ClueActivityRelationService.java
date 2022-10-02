package com.zzl.crm.workbench.service;

import com.zzl.crm.workbench.pojo.ClueActivityRelation;

import java.util.List;

/**
 * ��������
 *
 * @author ֣����
 * @date 2022/05/08  17:19
 */
public interface ClueActivityRelationService {
    /**
     * ��������г����ϵ
     * @param ClueActivityRelation
     * @return
     */
    int insertClueActivityRelationList(List<ClueActivityRelation> ClueActivityRelation);

    /**
     * ����ClueIdAndActivityIdɾ��ɾ���
     * @param relation
     * @return
     */
    int deleteClueActivityByClueAndActivityId(ClueActivityRelation relation);
    /**
     * ����ClueID��ѯ��ǰ�������г��
     * @param ClueId
     * @return
     */
    List<ClueActivityRelation> selectClueActivityRelationByClueId(String ClueId);
    /**
     *
     * ����ClueIdɾ����ǰ�������г���Ĺ�����ϵ
     * @param ClueId
     * @return
     */
    int deleteClueActivityRelationByClueId(String ClueId);
}
