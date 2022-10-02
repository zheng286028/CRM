package com.zzl.crm.workbench.service;

import com.zzl.crm.workbench.pojo.CustomerRemark;

import java.util.List;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/05/09  22:15
 */
public interface CustomerRemarkService {
    /**
     * 批量添加客户备注
     * @param customerRemarkList
     * @return
     */
    int insertCustomerRemarks(List<CustomerRemark> customerRemarkList);
    /**
     * 根据客户id查询当前客户下有多少备注
     * @return
     */
    List<CustomerRemark> selectCustomerRemarkByCustomerId(String CustomerId);
    /**
     * 在当前客户下添加备注
     * @param remark
     * @return
     */
    int insertCustomerRemark(CustomerRemark remark);
    /**
     * 根据id删除备注
     * @param id
     * @return
     */
    int deleteCustomerRemarkById(String id);
    /**
     * 根据id修改备注
     * @param remark
     * @return
     */
    int updateCustomerRemarkById(CustomerRemark remark);
}
