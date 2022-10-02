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
 * 功能描述
 *
 * @author 郑子浪
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
     * 添加交易
     * @param tran
     * @return
     */
    @Override
    public int insertTran(Tran tran) {
        return tranMapper.insertTran(tran);
    }
    /**
     * 根据客户id查询当前客户下的交易
     * @return
     */
    @Override
    public List<Tran> selectTranDetailed() {
        return tranMapper.selectTranDetailed();
    }
    /**
     * 添加交易
     * @param map
     * @return
     */
    @Override
    public void enterTransaction(Map<String, Object> map) {
      //收集参数
      User user =(User) map.get(Constants.SESSION_USER);
      String customerName=(String)map.get("customerId");
      //查询当前客户是否存在
        Customer customer = customerMapper.selectCustomerByName(customerName);
        if(customer==null){
            //添加客户
            customer = new Customer();
            customer.setCreateBy(user.getId());
            customer.setOwner(user.getId());
            customer.setCreateTime(DateUtils.formateDateTime(new Date()));
            customer.setName(customerName);
            customer.setId(UUIDUtils.getUUID());
            //添加
            customerMapper.insertCustomer(customer);
        }
        //添加交易
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
        //添加
        tranMapper.insertTran(t);
        //创建交易历史
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
     * 分页条件查询
     * @param map
     * @return
     */
    @Override
    public List<Tran> inquireTransactionByPageAndCondition(Map<String, Object> map) {
        return tranMapper.inquireTransactionByPageAndCondition(map);
    }
    /**
     * 查询条件总记录数
     * @param map
     * @return
     */
    @Override
    public int inquireTransactionTotalCountByCondition(Map<String, Object> map) {
        return tranMapper.inquireTransactionTotalCountByCondition(map);
    }
    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public Tran inquireTransactionById(String id) {
        return tranMapper.inquireTransactionById(id);
    }
    /**
     * 根据id修改
     * @param map
     * @return
     */
    @Override
    public void reviseTransactionById(Map<String,Object>map) {
        //收集参数
        User user =(User) map.get(Constants.SESSION_USER);
        String customerName=(String)map.get("customerId");
        //查询当前客户是否存在
        Customer customer = customerMapper.selectCustomerByName(customerName);
        if(customer==null){
            //添加客户
            customer = new Customer();
            customer.setCreateBy(user.getId());
            customer.setOwner(user.getId());
            customer.setCreateTime(DateUtils.formateDateTime(new Date()));
            customer.setName(customerName);
            customer.setId(UUIDUtils.getUUID());
            //添加
            customerMapper.insertCustomer(customer);
        }
        //修改交易
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
     * 查询交易统计图表
     * @return
     */
    @Override
    public List<FunnelVO> selectCountOfTranGroupByStage() {
        return tranMapper.selectCountOfTranGroupByStage();
    }
}
