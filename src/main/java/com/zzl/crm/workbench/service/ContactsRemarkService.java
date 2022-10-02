package com.zzl.crm.workbench.service;

import com.zzl.crm.workbench.pojo.ContactsRemark;
import com.zzl.crm.workbench.pojo.CustomerRemark;

import java.util.List;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/05/09  22:35
 */
public interface ContactsRemarkService {
    /**
     * 批量添加联系人备注
     * @param ctList
     * @return
     */
    int insertContactsRemarks(List<ContactsRemark> ctList);
    /**
     * 根据联系人id查询备注
     * @param contactsId
     * @return
     */
    List<ContactsRemark> selectContactsRemarkByContactsId(String contactsId);
    /**
     * 添加联系人备注
     * @param remark
     * @return
     */
    int addContactsRemark(ContactsRemark remark);
    /**
     * 根据id删除
     * @param id
     * @return
     */
    int emptyContactsRemarkById(String id);
    /**
     * 根据id修改
     * @param remark
     * @return
     */
    int saveEditContactsRemarkById(ContactsRemark remark);
}
