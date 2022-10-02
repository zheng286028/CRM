package com.zzl.crm.workbench.service;

import com.zzl.crm.workbench.pojo.ContactsRemark;
import com.zzl.crm.workbench.pojo.CustomerRemark;

import java.util.List;

/**
 * ��������
 *
 * @author ֣����
 * @date 2022/05/09  22:35
 */
public interface ContactsRemarkService {
    /**
     * ���������ϵ�˱�ע
     * @param ctList
     * @return
     */
    int insertContactsRemarks(List<ContactsRemark> ctList);
    /**
     * ������ϵ��id��ѯ��ע
     * @param contactsId
     * @return
     */
    List<ContactsRemark> selectContactsRemarkByContactsId(String contactsId);
    /**
     * �����ϵ�˱�ע
     * @param remark
     * @return
     */
    int addContactsRemark(ContactsRemark remark);
    /**
     * ����idɾ��
     * @param id
     * @return
     */
    int emptyContactsRemarkById(String id);
    /**
     * ����id�޸�
     * @param remark
     * @return
     */
    int saveEditContactsRemarkById(ContactsRemark remark);
}
