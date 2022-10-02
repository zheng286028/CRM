package com.zzl.crm.workbench.service.Impl;

import com.zzl.crm.workbench.mapper.ClueActivityRelationMapper;
import com.zzl.crm.workbench.pojo.ClueActivityRelation;
import com.zzl.crm.workbench.service.ClueActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/05/08  17:20
 */
@Service("ClueActivityRelationService")
public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;
    /**
     * 添加线索市场活动联系
     * @param ClueActivityRelation
     * @return
     */
    @Override
    public int insertClueActivityRelationList(List<ClueActivityRelation> ClueActivityRelation) {
        return clueActivityRelationMapper.insertClueActivityRelationList(ClueActivityRelation);
    }
    /**
     * 根据ClueIdAndActivityId删除删除活动
     * @param relation
     * @return
     */
    @Override
    public int deleteClueActivityByClueAndActivityId(ClueActivityRelation relation) {
        return clueActivityRelationMapper.deleteClueActivityByClueAndActivityId(relation);
    }
    /**
     * 根据ClueID查询当前线索的市场活动
     * @param ClueId
     * @return
     */
    @Override
    public List<ClueActivityRelation> selectClueActivityRelationByClueId(String ClueId) {
        return clueActivityRelationMapper.selectClueActivityRelationByClueId(ClueId);
    }
    /**
     *
     * 根据ClueId删除当前线索和市场活动的关联关系
     * @param ClueId
     * @return
     */
    @Override
    public int deleteClueActivityRelationByClueId(String ClueId) {
        return clueActivityRelationMapper.deleteClueActivityRelationByClueId(ClueId);
    }
}
