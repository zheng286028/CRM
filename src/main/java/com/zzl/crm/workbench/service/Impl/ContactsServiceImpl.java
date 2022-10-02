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
 * 功能描述
 *
 * @author 郑子浪
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
     * 添加联系人
     * @param contacts
     * @return
     */
    @Override
    public void insertContacts(Contacts contacts) {
        contactsMapper.insertContacts(contacts);
    }
    /**
     * 根据客户id查询联系人
     * @return
     */
    @Override
    public List<Contacts> selectContactsDetailed() {
        return contactsMapper.selectContactsDetailed();
    }
    /**
     * 根据名称模糊查询联系人
     * @param name
     * @return
     */
    @Override
    public List<Contacts> selectContactsByName(String name) {
        return contactsMapper.selectContactsByName(name);
    }
    /**
     * 添加联系人
     * @param map
     * @return
     */
    @Override
    public Contacts addContacts(Map<String,Object> map) {
        //根据客户名称精确查询当前客户是否存在
        String customerName=(String)map.get("CustomerName");
        User user=(User)map.get(Constants.SESSION_USER);
        Customer customer = customerService.selectCustomerByName(customerName);
        //判断是否存在
        if(customer==null){
            //不存在添加客户
            customer=new Customer();
            customer.setId(UUIDUtils.getUUID());
            customer.setCreateBy(user.getId());
            customer.setCreateTime(DateUtils.formateDateTime(new Date()));
            customer.setOwner(user.getId());
            customer.setName(customerName);
            //调用CustomerService完成添加
            customerService.insertCustomer(customer);
        }
        //添加联系人
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
        //调用ContactsMapper完成添加
        contactsMapper.insertContacts(con);
        return con;
    }
    /**
     * 根据id删除
     * @param id
     * @return
     */
    @Override
    public void deleteContactsById(String id) {
        //删除联系人备注
        contactsRemarkMapper.deleteContactsRemarkByContactsId(id);
        //删除联系人
        contactsMapper.deleteContactsById(id);
    }
    /**
     * 分页条件查询
     * @param map
     * @return
     */
    @Override
    public List<Contacts> selectContactsByPageAndCondition(Map<String, Object> map) {
        return contactsMapper.selectContactsByPageAndCondition(map);
    }
    /**
     * 查询条件总记录数
     * @param map
     * @return
     */
    @Override
    public int selectContactsTotalAndCondition(Map<String, Object> map) {
        return contactsMapper.selectContactsTotalAndCondition(map);
    }
    /**
     * 批量删除
     * @param ids
     * @return
     */
    @Override
    public void deleteContactsByIds(String[] ids) {
       //删除当前联系人备注
        contactsRemarkMapper.deleteContactsRemarkByIds(ids);
        //删除联系人
        contactsMapper.deleteContactsByIds(ids);
    }
    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public Contacts selectContactsById(String id) {
        return contactsMapper.selectContactsById(id);
    }
    /**
     * 根据id修改
     * @param map
     * @return
     */
    @Override
    public void updateContactsById(Map<String,Object>map) {
        //获取参数
        User user = (User)map.get(Constants.SESSION_USER);
        String CustomerName=(String)map.get("CustomerId");
        String id =(String) map.get("id");
        //判断当前客户是否存在，不存在则创建
        Customer customer = customerMapper.selectCustomerByName(CustomerName);
        if(customer==null){
            //创建客户
            customer=new Customer();
            //封装参数
            customer.setName(CustomerName);
            customer.setOwner(user.getId());
            customer.setId(UUIDUtils.getUUID());
            customer.setCreateTime(DateUtils.formateDateTime(new Date()));
            customer.setCreateBy(user.getId());
            //创建客户
            customerMapper.insertCustomer(customer);
        }
        //修改联系人
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
        //调用service完成修改
        contactsMapper.updateContactsById(con);
    }
}
