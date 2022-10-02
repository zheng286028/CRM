package com.zzl.crm.workbench.service.Impl;

import com.zzl.crm.commons.Constants.Constants;
import com.zzl.crm.commons.utils.DateUtils;
import com.zzl.crm.commons.utils.UUIDUtils;
import com.zzl.crm.settings.pojo.User;
import com.zzl.crm.workbench.mapper.ContactsMapper;
import com.zzl.crm.workbench.mapper.ContactsRemarkMapper;
import com.zzl.crm.workbench.mapper.CustomerMapper;
import com.zzl.crm.workbench.pojo.Contacts;
import com.zzl.crm.workbench.pojo.Customer;
import com.zzl.crm.workbench.service.ContactsService;
import com.zzl.crm.workbench.service.CustomerService;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ��������
 *
 * @author ֣����
 * @date 2022/05/09  21:34
 */
@Service
public class ContactsServiceImpl implements ContactsService {
    @Autowired
    private ContactsMapper contactsMapper;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;
    @Autowired
    private CustomerMapper customerMapper;
    /**
     * �����ϵ��
     * @param contacts
     * @return
     */
    @Override
    public void insertContacts(Contacts contacts) {
        contactsMapper.insertContacts(contacts);
    }
    /**
     * ���ݿͻ�id��ѯ��ϵ��
     * @return
     */
    @Override
    public List<Contacts> selectContactsDetailed() {
        return contactsMapper.selectContactsDetailed();
    }
    /**
     * ��������ģ����ѯ��ϵ��
     * @param name
     * @return
     */
    @Override
    public List<Contacts> selectContactsByName(String name) {
        return contactsMapper.selectContactsByName(name);
    }
    /**
     * �����ϵ��
     * @param map
     * @return
     */
    @Override
    public Contacts addContacts(Map<String,Object> map) {
        //���ݿͻ����ƾ�ȷ��ѯ��ǰ�ͻ��Ƿ����
        String customerName=(String)map.get("CustomerName");
        User user=(User)map.get(Constants.SESSION_USER);
        Customer customer = customerService.selectCustomerByName(customerName);
        //�ж��Ƿ����
        if(customer==null){
            //��������ӿͻ�
            customer=new Customer();
            customer.setId(UUIDUtils.getUUID());
            customer.setCreateBy(user.getId());
            customer.setCreateTime(DateUtils.formateDateTime(new Date()));
            customer.setOwner(user.getId());
            customer.setName(customerName);
            //����CustomerService������
            customerService.insertCustomer(customer);
        }
        //�����ϵ��
        Contacts con = new Contacts();
        con.setDescription((String)map.get("description"));
        con.setSource((String)map.get("source"));
        con.setNextContactTime((String)map.get("nextContactTime"));
        con.setMphone((String)map.get("mphone"));
        con.setJob((String)map.get("job"));
        con.setContactSummary((String)map.get("contactSummary"));
        con.setOwner(user.getId());
        con.setFullname((String)map.get("fullname"));
        con.setEmail((String)map.get("email"));
        con.setId(UUIDUtils.getUUID());
        con.setAppellation((String)map.get("appellation"));
        con.setCustomerId(customer.getId());
        con.setCreateTime((String) map.get("createTime"));
        con.setAddress((String) map.get("address"));
        con.setCreateBy(user.getId());
        //����ContactsMapper������
        contactsMapper.insertContacts(con);
        return con;
    }
    /**
     * ����idɾ��
     * @param id
     * @return
     */
    @Override
    public void deleteContactsById(String id) {
        //ɾ����ϵ�˱�ע
        contactsRemarkMapper.deleteContactsRemarkByContactsId(id);
        //ɾ����ϵ��
        contactsMapper.deleteContactsById(id);
    }
    /**
     * ��ҳ������ѯ
     * @param map
     * @return
     */
    @Override
    public List<Contacts> selectContactsByPageAndCondition(Map<String, Object> map) {
        return contactsMapper.selectContactsByPageAndCondition(map);
    }
    /**
     * ��ѯ�����ܼ�¼��
     * @param map
     * @return
     */
    @Override
    public int selectContactsTotalAndCondition(Map<String, Object> map) {
        return contactsMapper.selectContactsTotalAndCondition(map);
    }
    /**
     * ����ɾ��
     * @param ids
     * @return
     */
    @Override
    public void deleteContactsByIds(String[] ids) {
       //ɾ����ǰ��ϵ�˱�ע
        contactsRemarkMapper.deleteContactsRemarkByIds(ids);
        //ɾ����ϵ��
        contactsMapper.deleteContactsByIds(ids);
    }
    /**
     * ����id��ѯ
     * @param id
     * @return
     */
    @Override
    public Contacts selectContactsById(String id) {
        return contactsMapper.selectContactsById(id);
    }
    /**
     * ����id�޸�
     * @param map
     * @return
     */
    @Override
    public void updateContactsById(Map<String,Object>map) {
        //��ȡ����
        User user = (User)map.get(Constants.SESSION_USER);
        String CustomerName=(String)map.get("CustomerId");
        String id =(String) map.get("id");
        //�жϵ�ǰ�ͻ��Ƿ���ڣ��������򴴽�
        Customer customer = customerMapper.selectCustomerByName(CustomerName);
        if(customer==null){
            //�����ͻ�
            customer=new Customer();
            //��װ����
            customer.setName(CustomerName);
            customer.setOwner(user.getId());
            customer.setId(UUIDUtils.getUUID());
            customer.setCreateTime(DateUtils.formateDateTime(new Date()));
            customer.setCreateBy(user.getId());
            //�����ͻ�
            customerMapper.insertCustomer(customer);
        }
        //�޸���ϵ��
        Contacts con = new Contacts();
        con.setId(id);
        con.setAddress((String)map.get("address"));
        con.setAppellation((String)map.get("appellation"));
        con.setEmail((String)map.get("email"));
        con.setCreateTime((String)map.get("createTime"));
        con.setCustomerId(customer.getId());
        con.setFullname((String)map.get("fullname"));
        con.setOwner((String)map.get("owner"));
        con.setJob((String)map.get("job"));
        con.setContactSummary((String)map.get("contactSummary"));
        con.setMphone((String)map.get("mphone"));
        con.setNextContactTime((String)map.get("nextContactTime"));
        con.setSource((String)map.get("source"));
        con.setDescription((String)map.get("description"));
        con.setEditBy(user.getId());
        con.setEditTime(DateUtils.formateDateTime(new Date()));
        //����service����޸�
        contactsMapper.updateContactsById(con);
    }
}
