package com.zzl.crm.workbench.service;

import com.zzl.crm.commons.Constants.Constants;
import com.zzl.crm.workbench.pojo.Contacts;

import java.util.List;
import java.util.Map;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/05/09  21:33
 */
public interface ContactsService {
    /**
     * 添加联系人
     * @param contacts
     * @return
     */
    void insertContacts(Contacts contacts);
    /**
     * 根据客户id查询联系人
     * @return
     */
    List<Contacts> selectContactsDetailed();
    /**
     * 根据名称模糊查询联系人
     * @param name
     * @return
     */
    List<Contacts>selectContactsByName(String name);

    /**
     * 添加联系人
     * @param map
     * @return
     */
    Contacts addContacts(Map<String,Object> map);
    /**
     * 根据id删除
     * @param id
     * @return
     */
    void deleteContactsById(String id);

    /**
     * 分页条件查询
     * @param map
     * @return
     */
    List<Contacts>selectContactsByPageAndCondition(Map<String,Object>map);

    /**
     * 查询条件总记录数
     * @param map
     * @return
     */
    int selectContactsTotalAndCondition(Map<String,Object>map);
    /**
     * 批量删除
     * @param ids
     * @return
     */
    void deleteContactsByIds(String[] ids);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    Contacts selectContactsById(String id);
    /**
     * 根据id修改
     * @param map
     * @return
     */
    void updateContactsById(Map<String,Object>map);
}
