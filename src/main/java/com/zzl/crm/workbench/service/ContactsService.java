package com.zzl.crm.workbench.service;

import com.zzl.crm.commons.Constants.Constants;
import com.zzl.crm.workbench.pojo.Contacts;

import java.util.List;
import java.util.Map;

/**
 * ��������
 *
 * @author ֣����
 * @date 2022/05/09  21:33
 */
public interface ContactsService {
    /**
     * �����ϵ��
     * @param contacts
     * @return
     */
    void insertContacts(Contacts contacts);
    /**
     * ���ݿͻ�id��ѯ��ϵ��
     * @return
     */
    List<Contacts> selectContactsDetailed();
    /**
     * ��������ģ����ѯ��ϵ��
     * @param name
     * @return
     */
    List<Contacts>selectContactsByName(String name);

    /**
     * �����ϵ��
     * @param map
     * @return
     */
    Contacts addContacts(Map<String,Object> map);
    /**
     * ����idɾ��
     * @param id
     * @return
     */
    void deleteContactsById(String id);

    /**
     * ��ҳ������ѯ
     * @param map
     * @return
     */
    List<Contacts>selectContactsByPageAndCondition(Map<String,Object>map);

    /**
     * ��ѯ�����ܼ�¼��
     * @param map
     * @return
     */
    int selectContactsTotalAndCondition(Map<String,Object>map);
    /**
     * ����ɾ��
     * @param ids
     * @return
     */
    void deleteContactsByIds(String[] ids);
    /**
     * ����id��ѯ
     * @param id
     * @return
     */
    Contacts selectContactsById(String id);
    /**
     * ����id�޸�
     * @param map
     * @return
     */
    void updateContactsById(Map<String,Object>map);
}
