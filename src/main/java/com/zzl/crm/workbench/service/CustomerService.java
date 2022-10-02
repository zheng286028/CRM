package com.zzl.crm.workbench.service;

import com.zzl.crm.workbench.pojo.Customer;

import java.util.List;
import java.util.Map;

/**
 * ��������
 *
 * @author ֣����
 * @date 2022/05/09  20:04
 */
public interface CustomerService {
    /**
     * ����ת��ҵ��
     * @param map
     */
    void clueConvertBusiness(Map<String,Object> map);
    /**
     * ��ҳ������ѯ
     * @param map
     * @return
     */
    List<Customer> selectCustomerByPageAndCondition(Map<String,Object> map);

    /**
     * ����������ѯ�ܼ�¼��
     * @param map
     * @return
     */
    int selectCustomerTotalAndCondition(Map<String,Object>map);
    /**
     * ��ӿͻ�
     * @param customer
     * @return
     */
    int insertCustomer(Customer customer);
    /**
     * ����id��ѯ
     * @param id
     * @return
     */
    Customer selectCustomerById(String id);
    /**
     * ����id�޸�
     * @param customer
     * @return
     */
    int updateCustomerById(Customer customer);
    /**
     * ����id����ɾ��
     * @param id
     * @return
     */
    int deleteCustomerById(String[] id);
    /**
     * �������Ʋ�ѯ�ͻ�����
     * @return
     */
    List<String>selectAllCustomerName(String name);
    /**
     * �������ƾ�ȷ��ѯ
     * @param customerName
     * @return
     */
    Customer selectCustomerByName(String customerName);

}
