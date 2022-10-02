package com.zzl.crm.workbench.service;

import com.zzl.crm.workbench.pojo.activityRemark;

import java.util.List;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/05/04  14:38
 */
public interface activityRemarkService {
    /**
     * 根据activityId查询市场活动备注信息
     * @param id
     * @return
     */
    List<com.zzl.crm.workbench.pojo.activityRemark> selectActivityRemarkById(String id);

    /**
     * 添加市场活动备注
     * @param remark
     * @return
     */
    int saveCreateActivityRemark(activityRemark remark);

    /**
     * 根据id删除市场活动备注
     * @param id
     * @return
     */
    int deleteActivityRemarkById(String id);

    /**
     * 根据id修改市场活动备注
     * @param remark
     * @return
     */
    int editActivityRemarkById(activityRemark remark);
}
