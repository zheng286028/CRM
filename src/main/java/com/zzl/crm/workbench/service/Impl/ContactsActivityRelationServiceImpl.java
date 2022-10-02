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
 * ��������
 *
 * @author ֣����
 * @date 2022/05/10  12:42
 */
@Service("ContactsActivityRelationService")
public class ContactsActivityRelationServiceImpl implements ContactsActivityRelationService {
    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;
    /**
     * ���������ϵ�����г���Ĺ�����ϵ
     * @param relations
     * @return
     */
    @Override
    public int insertContactsActivityRelations(List<ContactsActivityRelation> relations) {
        return contactsActivityRelationMapper.insertContactsActivityRelations(relations);
    }
    /**
     * ɾ��activityId
     * @param relation
     * @return
     */
    @Override
    public int liftContactsActivityRelationByActivityId(ContactsActivityRelation relation) {
        return contactsActivityRelationMapper.liftContactsActivityRelationByActivityId(relation);
    }
}
