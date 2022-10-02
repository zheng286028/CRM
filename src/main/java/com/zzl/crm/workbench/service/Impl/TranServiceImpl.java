package com.zzl.crm.workbench.service.Impl;

import com.zzl.crm.commons.Constants.Constants;
import com.zzl.crm.commons.utils.DateUtils;
import com.zzl.crm.commons.utils.UUIDUtils;
import com.zzl.crm.settings.pojo.User;
import com.zzl.crm.workbench.mapper.CustomerMapper;
import com.zzl.crm.workbench.mapper.TranMapper;
import com.zzl.crm.workbench.pojo.Customer;
import com.zzl.crm.workbench.pojo.FunnelVO;
import com.zzl.crm.workbench.pojo.Tran;
import com.zzl.crm.workbench.pojo.TranHistory;
import com.zzl.crm.workbench.service.TranHistoryService;
import com.zzl.crm.workbench.service.TranService;
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
 * @date 2022/05/10  13:33
 */
@Service("tranService")
public class TranServiceImpl implements TranService {
    @Autowired
    private TranMapper tranMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private TranHistoryService tranHistoryService;
    /**
     * ��ӽ���
     * @param tran
     * @return
     */
    @Override
    public int insertTran(Tran tran) {
        return tranMapper.insertTran(tran);
    }
    /**
     * ���ݿͻ�id��ѯ��ǰ�ͻ��µĽ���
     * @return
     */
    @Override
    public List<Tran> selectTranDetailed() {
        return tranMapper.selectTranDetailed();
    }
    /**
     * ��ӽ���
     * @param map
     * @return
     */
    @Override
    public void enterTransaction(Map<String, Object> map) {
      //�ռ�����
      User user =(User) map.get(Constants.SESSION_USER);
      String customerName=(String)map.get("customerId");
      //��ѯ��ǰ�ͻ��Ƿ����
        Customer customer = customerMapper.selectCustomerByName(customerName);
        if(customer==null){
            //��ӿͻ�
            customer = new Customer();
            customer.setCreateBy(user.getId());
            customer.setOwner(user.getId());
            customer.setCreateTime(DateUtils.formateDateTime(new Date()));
            customer.setName(customerName);
            customer.setId(UUIDUtils.getUUID());
            //���
            customerMapper.insertCustomer(customer);
        }
        //��ӽ���
        Tran t = new Tran();
        t.setOwner((String)map.get("owner"));
        t.setName((String)map.get("name"));
        t.setStage((String)map.get("stage"));
        t.setId(UUIDUtils.getUUID());
        t.setCustomerId(customer.getId());
        t.setContactsId((String)map.get("contactsId"));
        t.setActivityId((String)map.get("activityId"));
        t.setCreateTime(DateUtils.formateDateTime(new Date()));
        t.setMoney((String)map.get("money"));
        t.setExpectedDate((String)map.get("expectedDate"));
        t.setContactSummary((String)map.get("contactSummary"));
        t.setDescription((String)map.get("description"));
        t.setNextContactTime((String)map.get("nextContactTime"));
        t.setType((String)map.get("type"));
        t.setSource((String)map.get("source"));
        t.setCreateBy(user.getId());
        //���
        tranMapper.insertTran(t);
        //����������ʷ
        TranHistory tranHistory = new TranHistory();
        tranHistory.setTranId(t.getId());
        tranHistory.setCreateBy(user.getId());
        tranHistory.setCreateTime(DateUtils.formateDateTime(new Date()));
        tranHistory.setId(UUIDUtils.getUUID());
        tranHistory.setMoney(t.getMoney());
        tranHistory.setStage(t.getStage());
        tranHistory.setExpectedDate(t.getExpectedDate());
        tranHistoryService.saveCreateTranHistory(tranHistory);
    }
    /**
     * ��ҳ������ѯ
     * @param map
     * @return
     */
    @Override
    public List<Tran> inquireTransactionByPageAndCondition(Map<String, Object> map) {
        return tranMapper.inquireTransactionByPageAndCondition(map);
    }
    /**
     * ��ѯ�����ܼ�¼��
     * @param map
     * @return
     */
    @Override
    public int inquireTransactionTotalCountByCondition(Map<String, Object> map) {
        return tranMapper.inquireTransactionTotalCountByCondition(map);
    }
    /**
     * ����id��ѯ
     * @param id
     * @return
     */
    @Override
    public Tran inquireTransactionById(String id) {
        return tranMapper.inquireTransactionById(id);
    }
    /**
     * ����id�޸�
     * @param map
     * @return
     */
    @Override
    public void reviseTransactionById(Map<String,Object>map) {
        //�ռ�����
        User user =(User) map.get(Constants.SESSION_USER);
        String customerName=(String)map.get("customerId");
        //��ѯ��ǰ�ͻ��Ƿ����
        Customer customer = customerMapper.selectCustomerByName(customerName);
        if(customer==null){
            //��ӿͻ�
            customer = new Customer();
            customer.setCreateBy(user.getId());
            customer.setOwner(user.getId());
            customer.setCreateTime(DateUtils.formateDateTime(new Date()));
            customer.setName(customerName);
            customer.setId(UUIDUtils.getUUID());
            //���
            customerMapper.insertCustomer(customer);
        }
        //�޸Ľ���
        Tran t = new Tran();
        t.setId((String) map.get("id"));
        t.setOwner((String)map.get("owner"));
        t.setName((String)map.get("name"));
        t.setStage((String)map.get("stage"));
        t.setCustomerId(customer.getId());
        t.setContactsId((String)map.get("contactsId"));
        t.setActivityId((String)map.get("activityId"));
        t.setEditTime(DateUtils.formateDateTime(new Date()));
        t.setMoney((String)map.get("money"));
        t.setExpectedDate((String)map.get("expectedDate"));
        t.setContactSummary((String)map.get("contactSummary"));
        t.setDescription((String)map.get("description"));
        t.setNextContactTime((String)map.get("nextContactTime"));
        t.setType((String)map.get("type"));
        t.setSource((String)map.get("source"));
        t.setEditBy(user.getId());
       tranMapper.reviseTransactionById(t);
    }
    /**
     * ��ѯ����ͳ��ͼ��
     * @return
     */
    @Override
    public List<FunnelVO> selectCountOfTranGroupByStage() {
        return tranMapper.selectCountOfTranGroupByStage();
    }
}
