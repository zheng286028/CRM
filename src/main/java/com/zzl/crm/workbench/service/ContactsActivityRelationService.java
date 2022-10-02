package com.zzl.crm.workbench.service;

import com.zzl.crm.workbench.pojo.Activity;
import com.zzl.crm.workbench.pojo.ContactsActivityRelation;

import java.util.List;
import java.util.Map;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/05/10  12:41
 */
public interface ContactsActivityRelationService {
    /**
     * 批量添加联系人域市场活动的关联关系
     * @param relations
     * @return
     */
    int insertContactsActivityRelations(List<ContactsActivityRelation> relations);
    /**
     * 删除activityId
     * @param relation
     * @return
     */
    int liftContactsActivityRelationByActivityId(ContactsActivityRelation relation);
}
