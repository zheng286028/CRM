package com.zzl.crm.workbench.service.Impl;

import com.zzl.crm.workbench.mapper.activityRemarkMapper;
import com.zzl.crm.workbench.pojo.activityRemark;
import com.zzl.crm.workbench.service.activityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/05/04  14:38
 */
@Service("activityRemarkImpl")
public class activityRemarkImpl implements activityRemarkService {
    @Autowired
    private activityRemarkMapper activityRemarkMapper;

    /**
     * 根据activityId查询市场活动备注信息
     * @param id
     * @return
     */
    @Override
    public List<com.zzl.crm.workbench.pojo.activityRemark> selectActivityRemarkById(String id) {
        return activityRemarkMapper.selectActivityRemarkById(id);
    }
    /**
     * 添加市场活动备注
     * @param remark
     * @return
     */
    @Override
    public int saveCreateActivityRemark(activityRemark remark) {
        return activityRemarkMapper.saveCreateActivityRemark(remark);
    }
    /**
     * 根据id删除市场活动备注
     * @param id
     * @return
     */
    @Override
    public int deleteActivityRemarkById(String id) {
        return activityRemarkMapper.deleteActivityRemarkById(id);
    }
    /**
     * 根据id修改市场活动备注
     * @param remark
     * @return
     */
    @Override
    public int editActivityRemarkById(activityRemark remark) {
        return activityRemarkMapper.editActivityRemarkById(remark);
    }
}
