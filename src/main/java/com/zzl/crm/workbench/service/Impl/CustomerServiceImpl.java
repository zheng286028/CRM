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
 * ��������
 *
 * @author ֣����
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
     * ����ת��ҵ��
     * @param map
     */
    @Override
    public void clueConvertBusiness(Map<String,Object> map) {
        //����ת����ҵ��
        //��Ϊҵ��㲻�û�ȡ�洢��Session���е�user�����Դ�Controllerͨ��map������
        User user = (User) map.get(Constants.SESSION_USER);
        String isTransaction=(String)map.get("isTransaction");
        //�õ���ǰ����
        String clueId = (String) map.get("clueId");
        final Clue clue = clueService.selectClueById(clueId);
        //����ǰ������װ���ͻ�����
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
        //���
        customerMapper.insertCustomer(c);
        //����ǰ������ӵ���ϵ��
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
        //����contacts������
        contactsService.insertContacts(co);
        //��ѯ��ǰ������ע����ϸ��Ϣ
        final List<ClueRemark> crk = clueRemarkService.selectClueRemarkByClueId(clueId);
        if (crk != null && crk.size() > 0) {
            //��������ע��ӵ��ͻ���ע����
            //����crk
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
                //����������ע��ӵ���ϵ��
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
                //���
                customerRemarkService.insertCustomerRemarks(crsList);
                contactsRemarkService.insertContactsRemarks(contactsRemarkList);
            }
        }
        //��ѯ��ǰ�������г���Ĺ�����ϵ
        final List<ClueActivityRelation> carList = clueActivityRelationService.selectClueActivityRelationByClueId(clueId);
        if (carList != null && carList.size() > 0) {
            //����carList,��������ÿ����car����洢��list����
            ContactsActivityRelation car = null;
            List<ContactsActivityRelation> careList = new ArrayList<>();
            for (ClueActivityRelation care : carList) {
                //ÿ����һ�δ���һ��ContactsActivityRelation����
                car = new ContactsActivityRelation();
                car.setId(UUIDUtils.getUUID());
                car.setContactsId(co.getId());
                car.setActivityId(care.getActivityId());
                careList.add(car);
            }
            //����ContactsActivityRelationService������
            contactsActivityRelationService.insertContactsActivityRelations(careList);
        }
        //�ж��Ƿ���Ҫ�������ף����Ұѵ�ǰ�����ı�עת�����ױ�ע��
        if("true".equals(isTransaction)){
            //�������׶���
            Tran tran = new Tran();
            //�ռ�����
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
            //����TranService��ɴ���
            tranMapper.insertTran(tran);
            //�ж��Ƿ���ֵ���ڱ���crk
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
                    //����һ����ӵ�list������
                    tranRemarkList.add(trm);
                }
                //����TranRemarkService������
                remarkService.insertTranRemark(tranRemarkList);
            }
        }
            //ɾ����ǰ�����µı�ע�����г���Ĺ�����ϵ������ǰ����
            clueRemarkService.deleteClueRemarkById(clueId);
            clueActivityRelationService.deleteClueActivityRelationByClueId(clueId);
            clueService.deleteClueById(clueId);
    }
    /**
     * ��ҳ������ѯ
     * @param map
     * @return
     */
    @Override
    public List<Customer> selectCustomerByPageAndCondition(Map<String, Object> map) {
        return customerMapper.selectCustomerByPageAndCondition(map);
    }
    /**
     * ����������ѯ�ܼ�¼��
     * @param map
     * @return
     */
    @Override
    public int selectCustomerTotalAndCondition(Map<String, Object> map) {
        return customerMapper.selectCustomerTotalAndCondition(map);
    }
    /**
     * ��ӿͻ�
     * @param customer
     * @return
     */
    @Override
    public int insertCustomer(Customer customer) {
        return customerMapper.insertCustomer(customer);
    }
    /**
     * ����id��ѯ
     * @param id
     * @return
     */
    @Override
    public Customer selectCustomerById(String id) {
        return customerMapper.selectCustomerById(id);
    }
    /**
     * ����id�޸�
     * @param customer
     * @return
     */
    @Override
    public int updateCustomerById(Customer customer) {
        return customerMapper.updateCustomerById(customer);
    }
    /**
     * ����id����ɾ��
     * @param id
     * @return
     */
    @Override
    public int deleteCustomerById(String[] id) {
        return customerMapper.deleteCustomerById(id);
    }
    /**
     * �������Ʋ�ѯ�ͻ�����
     * @return
     */
    @Override
    public List<String> selectAllCustomerName(String name) {
        return customerMapper.selectAllCustomerName(name);
    }
    /**
     * �������ƾ�ȷ��ѯ
     * @param customerName
     * @return
     */
    @Override
    public Customer selectCustomerByName(String customerName) {
        return customerMapper.selectCustomerByName(customerName);
    }
}
