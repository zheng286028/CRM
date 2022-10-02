package com.zzl.crm.workbench.mapper;

import com.zzl.crm.workbench.pojo.activityRemark;

import java.util.List;

public interface activityRemarkMapper {
    /**
     * ����activityId��ѯ�г����ע��Ϣ
     * @param id
     * @return
     */
    List<activityRemark> selectActivityRemarkById(String id);

    /**
     * ����г����ע
     * @param remark
     * @return
     */
    int saveCreateActivityRemark(activityRemark remark);

    /**
     * ����idɾ���г����ע
     * @param id
     * @return
     */
    int deleteActivityRemarkById(String id);

    /**
     * ����id�޸��г����ע
     * @param remark
     * @return
     */
    int editActivityRemarkById(activityRemark remark);
}
