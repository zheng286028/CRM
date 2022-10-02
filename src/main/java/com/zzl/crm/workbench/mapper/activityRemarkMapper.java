package com.zzl.crm.workbench.mapper;

import com.zzl.crm.workbench.pojo.activityRemark;

import java.util.List;

public interface activityRemarkMapper {
    /**
     * 根据activityId查询市场活动备注信息
     * @param id
     * @return
     */
    List<activityRemark> selectActivityRemarkById(String id);

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
