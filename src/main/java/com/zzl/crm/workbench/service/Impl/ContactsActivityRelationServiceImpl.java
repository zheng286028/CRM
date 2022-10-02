package com.zzl.crm.workbench.service.Impl;

import com.zzl.crm.workbench.mapper.ContactsActivityRelationMapper;
import com.zzl.crm.workbench.pojo.Activity;
import com.zzl.crm.workbench.pojo.ContactsActivityRelation;
import com.zzl.crm.workbench.service.ContactsActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/05/10  12:42
 */
@Service("ContactsActivityRelationService")
public class ContactsActivityRelationServiceImpl implements ContactsActivityRelationService {
    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;
    /**
     * 批量添加联系人域市场活动的关联关系
     * @param relations
     * @return
     */
    @Override
    public int insertContactsActivityRelations(List<ContactsActivityRelation> relations) {
        return contactsActivityRelationMapper.insertContactsActivityRelations(relations);
    }
    /**
     * 删除activityId
     * @param relation
     * @return
     */
    @Override
    public int liftContactsActivityRelationByActivityId(ContactsActivityRelation relation) {
        return contactsActivityRelationMapper.liftContactsActivityRelationByActivityId(relation);
    }
}
