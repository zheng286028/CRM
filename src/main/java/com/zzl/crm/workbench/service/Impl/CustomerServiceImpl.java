package com.zzl.crm.workbench.service.Impl;

import com.zzl.crm.commons.Constants.Constants;
import com.zzl.crm.commons.utils.DateUtils;
import com.zzl.crm.commons.utils.UUIDUtils;
import com.zzl.crm.settings.pojo.User;
import com.zzl.crm.workbench.mapper.CustomerMapper;
import com.zzl.crm.workbench.mapper.TranMapper;
import com.zzl.crm.workbench.pojo.*;
import com.zzl.crm.workbench.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/05/09  20:05
 */
@Service("CustomerService")
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ClueService clueService;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private ClueRemarkService clueRemarkService;
    @Autowired
    private CustomerRemarkService customerRemarkService;
    @Autowired
    private ContactsRemarkService contactsRemarkService;
    @Autowired
    private ClueActivityRelationService clueActivityRelationService;
    @Autowired
    private ContactsActivityRelationService contactsActivityRelationService;
    @Autowired
    private TranMapper tranMapper;
    @Autowired
    private TranRemarkService remarkService;
    /**
     * 线索转换业务
     * @param map
     */
    @Override
    public void clueConvertBusiness(Map<String,Object> map) {
        //线索转换总业务
        //因为业务层不好获取存储在Session域中的user，所以从Controller通过map传过来
        User user = (User) map.get(Constants.SESSION_USER);
        String isTransaction=(String)map.get("isTransaction");
        //拿到当前线索
        String clueId = (String) map.get("clueId");
        final Clue clue = clueService.selectClueById(clueId);
        //将当前线索封装到客户对象
        Customer c = new Customer();
        c.setId(UUIDUtils.getUUID());
        c.setCreateBy(user.getId());
        c.setCreateTime(DateUtils.formateDateTime(new Date()));
        c.setAddress(clue.getAddress());
        c.setContactSummary(clue.getContactSummary());
        c.setDescription(clue.getDescription());
        c.setWebsite(clue.getWebsite());
        c.setName(clue.getCompany());
        c.setOwner(user.getId());
        c.setPhone(clue.getPhone());
        c.setNextContactTime(clue.getNextContactTime());
        //添加
        customerMapper.insertCustomer(c);
        //将当前线索添加到联系人
        Contacts co = new Contacts();
        co.setAddress(clue.getAddress());
        co.setCreateBy(user.getId());
        co.setCreateTime(DateUtils.formateDateTime(new Date()));
        co.setCustomerId(c.getId());
        co.setDescription(clue.getDescription());
        co.setContactSummary(clue.getContactSummary());
        co.setAppellation(clue.getAppellation());
        co.setEmail(clue.getEmail());
        co.setFullname(clue.getFullname());
        co.setJob(clue.getJob());
        co.setMphone(clue.getMphone());
        co.setId(UUIDUtils.getUUID());
        co.setNextContactTime(clue.getNextContactTime());
        co.setSource(clue.getSource());
        co.setOwner(user.getId());
        //调用contacts完成添加
        contactsService.insertContacts(co);
        //查询当前线索备注的详细信息
        final List<ClueRemark> crk = clueRemarkService.selectClueRemarkByClueId(clueId);
        if (crk != null && crk.size() > 0) {
            //将线索备注添加到客户备注表中
            //遍历crk
            CustomerRemark csr = null;
            List<CustomerRemark> crsList = new ArrayList<>();
            for (ClueRemark cr : crk) {
                csr = new CustomerRemark();
                csr.setCreateBy(cr.getCreateBy());
                csr.setCreateTime(cr.getCreateTime());
                csr.setCustomerId(c.getId());
                csr.setId(UUIDUtils.getUUID());
                csr.setEditBy(cr.getEditBy());
                csr.setEditTime(cr.getEditTime());
                csr.setEditFlag(cr.getEditFlag());
                csr.setNoteContent(cr.getNoteContent());
                crsList.add(csr);
                //将该线索备注添加到联系人
                ContactsRemark ctr = null;
                List<ContactsRemark> contactsRemarkList = new ArrayList<>();
                ctr = new ContactsRemark();
                ctr.setCreateBy(cr.getCreateBy());
                ctr.setCreateTime(cr.getCreateTime());
                ctr.setEditBy(cr.getEditBy());
                ctr.setEditTime(cr.getEditTime());
                ctr.setEditFlag(cr.getEditFlag());
                ctr.setId(UUIDUtils.getUUID());
                ctr.setNoteContent(cr.getNoteContent());
                ctr.setContactsId(co.getId());
                contactsRemarkList.add(ctr);
                //添加
                customerRemarkService.insertCustomerRemarks(crsList);
                contactsRemarkService.insertContactsRemarks(contactsRemarkList);
            }
        }
        //查询当前线索和市场活动的关联关系
        final List<ClueActivityRelation> carList = clueActivityRelationService.selectClueActivityRelationByClueId(clueId);
        if (carList != null && carList.size() > 0) {
            //遍历carList,将遍历的每个对car对象存储到list集合
            ContactsActivityRelation car = null;
            List<ContactsActivityRelation> careList = new ArrayList<>();
            for (ClueActivityRelation care : carList) {
                //每遍历一次创建一个ContactsActivityRelation对象
                car = new ContactsActivityRelation();
                car.setId(UUIDUtils.getUUID());
                car.setContactsId(co.getId());
                car.setActivityId(care.getActivityId());
                careList.add(car);
            }
            //调用ContactsActivityRelationService完成添加
            contactsActivityRelationService.insertContactsActivityRelations(careList);
        }
        //判断是否需要创建交易，并且把当前线索的备注转到交易备注中
        if("true".equals(isTransaction)){
            //创建交易对象
            Tran tran = new Tran();
            //收集参数
            tran.setCustomerId(c.getId());
            tran.setActivityId((String)map.get("activityId"));
            tran.setCreateBy(user.getId());
            tran.setContactsId(co.getId());
            tran.setCreateTime(DateUtils.formateDateTime(new Date()));
            tran.setId(UUIDUtils.getUUID());
            tran.setExpectedDate((String)map.get("expectedDate"));
            tran.setMoney((String)map.get("money"));
            tran.setName((String)map.get("name"));
            tran.setStage((String)map.get("stage"));
            tran.setOwner(user.getId());
            //调用TranService完成创建
            tranMapper.insertTran(tran);
            //判断是否有值，在遍历crk
            if (carList != null && carList.size() > 0){
                TranRemark trm=null;
                List<TranRemark> tranRemarkList = new ArrayList<>();
                for (ClueRemark cr:crk){
                    trm = new TranRemark();
                    trm.setCreateBy(cr.getCreateBy());
                    trm.setCreateTime(cr.getCreateTime());
                    trm.setEditBy(cr.getEditBy());
                    trm.setEditFlag(cr.getEditFlag());
                    trm.setId(UUIDUtils.getUUID());
                    trm.setNoteContent(cr.getNoteContent());
                    trm.setTranId(tran.getId());
                    trm.setEditTime(cr.getEditTime());
                    //遍历一次添加到list集合中
                    tranRemarkList.add(trm);
                }
                //调用TranRemarkService完成添加
                remarkService.insertTranRemark(tranRemarkList);
            }
        }
            //删除当前线索下的备注，和市场活动的关联关系，及当前线索
            clueRemarkService.deleteClueRemarkById(clueId);
            clueActivityRelationService.deleteClueActivityRelationByClueId(clueId);
            clueService.deleteClueById(clueId);
    }
    /**
     * 分页条件查询
     * @param map
     * @return
     */
    @Override
    public List<Customer> selectCustomerByPageAndCondition(Map<String, Object> map) {
        return customerMapper.selectCustomerByPageAndCondition(map);
    }
    /**
     * 根据条件查询总记录数
     * @param map
     * @return
     */
    @Override
    public int selectCustomerTotalAndCondition(Map<String, Object> map) {
        return customerMapper.selectCustomerTotalAndCondition(map);
    }
    /**
     * 添加客户
     * @param customer
     * @return
     */
    @Override
    public int insertCustomer(Customer customer) {
        return customerMapper.insertCustomer(customer);
    }
    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public Customer selectCustomerById(String id) {
        return customerMapper.selectCustomerById(id);
    }
    /**
     * 根据id修改
     * @param customer
     * @return
     */
    @Override
    public int updateCustomerById(Customer customer) {
        return customerMapper.updateCustomerById(customer);
    }
    /**
     * 根据id批量删除
     * @param id
     * @return
     */
    @Override
    public int deleteCustomerById(String[] id) {
        return customerMapper.deleteCustomerById(id);
    }
    /**
     * 根据名称查询客户名称
     * @return
     */
    @Override
    public List<String> selectAllCustomerName(String name) {
        return customerMapper.selectAllCustomerName(name);
    }
    /**
     * 根据名称精确查询
     * @param customerName
     * @return
     */
    @Override
    public Customer selectCustomerByName(String customerName) {
        return customerMapper.selectCustomerByName(customerName);
    }
}
