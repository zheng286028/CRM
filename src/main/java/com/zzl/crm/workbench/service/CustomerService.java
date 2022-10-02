package com.zzl.crm.workbench.service;

import com.zzl.crm.workbench.pojo.Customer;

import java.util.List;
import java.util.Map;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/05/09  20:04
 */
public interface CustomerService {
    /**
     * 线索转换业务
     * @param map
     */
    void clueConvertBusiness(Map<String,Object> map);
    /**
     * 分页条件查询
     * @param map
     * @return
     */
    List<Customer> selectCustomerByPageAndCondition(Map<String,Object> map);

    /**
     * 根据条件查询总记录数
     * @param map
     * @return
     */
    int selectCustomerTotalAndCondition(Map<String,Object>map);
    /**
     * 添加客户
     * @param customer
     * @return
     */
    int insertCustomer(Customer customer);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    Customer selectCustomerById(String id);
    /**
     * 根据id修改
     * @param customer
     * @return
     */
    int updateCustomerById(Customer customer);
    /**
     * 根据id批量删除
     * @param id
     * @return
     */
    int deleteCustomerById(String[] id);
    /**
     * 根据名称查询客户名称
     * @return
     */
    List<String>selectAllCustomerName(String name);
    /**
     * 根据名称精确查询
     * @param customerName
     * @return
     */
    Customer selectCustomerByName(String customerName);

}
