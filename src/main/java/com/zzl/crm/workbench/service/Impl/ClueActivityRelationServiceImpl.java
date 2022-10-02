package com.zzl.crm.workbench.service.Impl;

import com.zzl.crm.workbench.mapper.ClueActivityRelationMapper;
import com.zzl.crm.workbench.pojo.ClueActivityRelation;
import com.zzl.crm.workbench.service.ClueActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ��������
 *
 * @author ֣����
 * @date 2022/05/08  17:20
 */
@Service("ClueActivityRelationService")
public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;
    /**
     * ��������г����ϵ
     * @param ClueActivityRelation
     * @return
     */
    @Override
    public int insertClueActivityRelationList(List<ClueActivityRelation> ClueActivityRelation) {
        return clueActivityRelationMapper.insertClueActivityRelationList(ClueActivityRelation);
    }
    /**
     * ����ClueIdAndActivityIdɾ��ɾ���
     * @param relation
     * @return
     */
    @Override
    public int deleteClueActivityByClueAndActivityId(ClueActivityRelation relation) {
        return clueActivityRelationMapper.deleteClueActivityByClueAndActivityId(relation);
    }
    /**
     * ����ClueID��ѯ��ǰ�������г��
     * @param ClueId
     * @return
     */
    @Override
    public List<ClueActivityRelation> selectClueActivityRelationByClueId(String ClueId) {
        return clueActivityRelationMapper.selectClueActivityRelationByClueId(ClueId);
    }
    /**
     *
     * ����ClueIdɾ����ǰ�������г���Ĺ�����ϵ
     * @param ClueId
     * @return
     */
    @Override
    public int deleteClueActivityRelationByClueId(String ClueId) {
        return clueActivityRelationMapper.deleteClueActivityRelationByClueId(ClueId);
    }
}
