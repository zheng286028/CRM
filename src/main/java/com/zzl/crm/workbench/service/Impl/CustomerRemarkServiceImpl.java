package com.zzl.crm.workbench.service.Impl;

import com.zzl.crm.workbench.mapper.CustomerRemarkMapper;
import com.zzl.crm.workbench.pojo.CustomerRemark;
import com.zzl.crm.workbench.service.CustomerRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/05/09  22:16
 */
@Service("CustomerRemarkService")
public class CustomerRemarkServiceImpl implements CustomerRemarkService {
    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;
    /**
     * 批量添加客户备注
     * @param customerRemarkList
     * @return
     */
    @Override
    public int insertCustomerRemarks(List<CustomerRemark> customerRemarkList) {
        return customerRemarkMapper.insertCustomerRemarks(customerRemarkList);
    }
    /**
     * 根据客户id查询当前客户下有多少备注
     * @return
     */
    @Override
    public List<CustomerRemark> selectCustomerRemarkByCustomerId(String CustomerId) {
        return customerRemarkMapper.selectCustomerRemarkByCustomerId(CustomerId);
    }
    /**
     * 在当前客户下添加备注
     * @param remark
     * @return
     */
    @Override
    public int insertCustomerRemark(CustomerRemark remark) {
        return customerRemarkMapper.insertCustomerRemark(remark);
    }
    /**
     * 根据id删除备注
     * @param id
     * @return
     */
    @Override
    public int deleteCustomerRemarkById(String id) {
        return customerRemarkMapper.deleteCustomerRemarkById(id);
    }
    /**
     * 根据id修改备注
     * @param remark
     * @return
     */
    @Override
    public int updateCustomerRemarkById(CustomerRemark remark) {
        return customerRemarkMapper.updateCustomerRemarkById(remark);
    }
}
