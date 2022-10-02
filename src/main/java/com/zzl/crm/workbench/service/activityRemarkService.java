package com.zzl.crm.workbench.service;

import com.zzl.crm.workbench.pojo.activityRemark;

import java.util.List;

/**
 * ��������
 *
 * @author ֣����
 * @date 2022/05/04  14:38
 */
public interface activityRemarkService {
    /**
     * ����activityId��ѯ�г����ע��Ϣ
     * @param id
     * @return
     */
    List<com.zzl.crm.workbench.pojo.activityRemark> selectActivityRemarkById(String id);

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
