package com.zzl.crm.workbench.service.Impl;

import com.zzl.crm.workbench.mapper.ContactsRemarkMapper;
import com.zzl.crm.workbench.pojo.ContactsRemark;
import com.zzl.crm.workbench.service.ContactsRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/05/09  22:36
 */
@Service("ContactsRemarkService")
public class ContactsRemarkServiceImpl implements ContactsRemarkService {
    @Autowired
    ContactsRemarkMapper contactsRemarkMapper;
    /**
     * 批量添加联系人备注
     * @param ctList
     * @return
     */
    @Override
    public int insertContactsRemarks(List<ContactsRemark> ctList) {
        return contactsRemarkMapper.insertContactsRemarks(ctList);
    }
    /**
     * 根据联系人id查询备注
     * @param contactsId
     * @return
     */
    @Override
    public List<ContactsRemark> selectContactsRemarkByContactsId(String contactsId) {
        return contactsRemarkMapper.selectContactsRemarkByContactsId(contactsId);
    }
    /**
     * 添加联系人备注
     * @param remark
     * @return
     */
    @Override
    public int addContactsRemark(ContactsRemark remark) {
        return contactsRemarkMapper.addContactsRemark(remark);
    }
    /**
     * 根据id删除
     * @param id
     * @return
     */
    @Override
    public int emptyContactsRemarkById(String id) {
        return contactsRemarkMapper.emptyContactsRemarkById(id);
    }
    /**
     * 根据id修改
     * @param remark
     * @return
     */
    @Override
    public int saveEditContactsRemarkById(ContactsRemark remark) {
        return contactsRemarkMapper.saveEditContactsRemarkById(remark);
    }
}
