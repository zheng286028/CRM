package com.zzl.crm.workbench.service.Impl;

import com.zzl.crm.workbench.mapper.activityRemarkMapper;
import com.zzl.crm.workbench.pojo.activityRemark;
import com.zzl.crm.workbench.service.activityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ��������
 *
 * @author ֣����
 * @date 2022/05/04  14:38
 */
@Service("activityRemarkImpl")
public class activityRemarkImpl implements activityRemarkService {
    @Autowired
    private activityRemarkMapper activityRemarkMapper;

    /**
     * ����activityId��ѯ�г����ע��Ϣ
     * @param id
     * @return
     */
    @Override
    public List<com.zzl.crm.workbench.pojo.activityRemark> selectActivityRemarkById(String id) {
        return activityRemarkMapper.selectActivityRemarkById(id);
    }
    /**
     * ����г����ע
     * @param remark
     * @return
     */
    @Override
    public int saveCreateActivityRemark(activityRemark remark) {
        return activityRemarkMapper.saveCreateActivityRemark(remark);
    }
    /**
     * ����idɾ���г����ע
     * @param id
     * @return
     */
    @Override
    public int deleteActivityRemarkById(String id) {
        return activityRemarkMapper.deleteActivityRemarkById(id);
    }
    /**
     * ����id�޸��г����ע
     * @param remark
     * @return
     */
    @Override
    public int editActivityRemarkById(activityRemark remark) {
        return activityRemarkMapper.editActivityRemarkById(remark);
    }
}
