package com.zzl.crm.workbench.service;

import com.zzl.crm.workbench.pojo.CustomerRemark;

import java.util.List;

/**
 * ��������
 *
 * @author ֣����
 * @date 2022/05/09  22:15
 */
public interface CustomerRemarkService {
    /**
     * ������ӿͻ���ע
     * @param customerRemarkList
     * @return
     */
    int insertCustomerRemarks(List<CustomerRemark> customerRemarkList);
    /**
     * ���ݿͻ�id��ѯ��ǰ�ͻ����ж��ٱ�ע
     * @return
     */
    List<CustomerRemark> selectCustomerRemarkByCustomerId(String CustomerId);
    /**
     * �ڵ�ǰ�ͻ�����ӱ�ע
     * @param remark
     * @return
     */
    int insertCustomerRemark(CustomerRemark remark);
    /**
     * ����idɾ����ע
     * @param id
     * @return
     */
    int deleteCustomerRemarkById(String id);
    /**
     * ����id�޸ı�ע
     * @param remark
     * @return
     */
    int updateCustomerRemarkById(CustomerRemark remark);
}
