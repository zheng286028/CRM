package com.zzl.crm.workbench.service.Impl;

import com.zzl.crm.workbench.mapper.ContactsRemarkMapper;
import com.zzl.crm.workbench.pojo.ContactsRemark;
import com.zzl.crm.workbench.service.ContactsRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ��������
 *
 * @author ֣����
 * @date 2022/05/09  22:36
 */
@Service("ContactsRemarkService")
public class ContactsRemarkServiceImpl implements ContactsRemarkService {
    @Autowired
    ContactsRemarkMapper contactsRemarkMapper;
    /**
     * ���������ϵ�˱�ע
     * @param ctList
     * @return
     */
    @Override
    public int insertContactsRemarks(List<ContactsRemark> ctList) {
        return contactsRemarkMapper.insertContactsRemarks(ctList);
    }
    /**
     * ������ϵ��id��ѯ��ע
     * @param contactsId
     * @return
     */
    @Override
    public List<ContactsRemark> selectContactsRemarkByContactsId(String contactsId) {
        return contactsRemarkMapper.selectContactsRemarkByContactsId(contactsId);
    }
    /**
     * �����ϵ�˱�ע
     * @param remark
     * @return
     */
    @Override
    public int addContactsRemark(ContactsRemark remark) {
        return contactsRemarkMapper.addContactsRemark(remark);
    }
    /**
     * ����idɾ��
     * @param id
     * @return
     */
    @Override
    public int emptyContactsRemarkById(String id) {
        return contactsRemarkMapper.emptyContactsRemarkById(id);
    }
    /**
     * ����id�޸�
     * @param remark
     * @return
     */
    @Override
    public int saveEditContactsRemarkById(ContactsRemark remark) {
        return contactsRemarkMapper.saveEditContactsRemarkById(remark);
    }
}
