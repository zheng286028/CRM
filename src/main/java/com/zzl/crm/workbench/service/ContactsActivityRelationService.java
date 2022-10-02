package com.zzl.crm.workbench.service;

import com.zzl.crm.workbench.pojo.Activity;
import com.zzl.crm.workbench.pojo.ContactsActivityRelation;

import java.util.List;
import java.util.Map;

/**
 * ��������
 *
 * @author ֣����
 * @date 2022/05/10  12:41
 */
public interface ContactsActivityRelationService {
    /**
     * ���������ϵ�����г���Ĺ�����ϵ
     * @param relations
     * @return
     */
    int insertContactsActivityRelations(List<ContactsActivityRelation> relations);
    /**
     * ɾ��activityId
     * @param relation
     * @return
     */
    int liftContactsActivityRelationByActivityId(ContactsActivityRelation relation);
}
