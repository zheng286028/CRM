package com.zzl.crm.workbench.service;

import com.zzl.crm.workbench.pojo.ClueActivityRelation;

import java.util.List;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/05/08  17:19
 */
public interface ClueActivityRelationService {
    /**
     * 添加线索市场活动联系
     * @param ClueActivityRelation
     * @return
     */
    int insertClueActivityRelationList(List<ClueActivityRelation> ClueActivityRelation);

    /**
     * 根据ClueIdAndActivityId删除删除活动
     * @param relation
     * @return
     */
    int deleteClueActivityByClueAndActivityId(ClueActivityRelation relation);
    /**
     * 根据ClueID查询当前线索的市场活动
     * @param ClueId
     * @return
     */
    List<ClueActivityRelation> selectClueActivityRelationByClueId(String ClueId);
    /**
     *
     * 根据ClueId删除当前线索和市场活动的关联关系
     * @param ClueId
     * @return
     */
    int deleteClueActivityRelationByClueId(String ClueId);
}
