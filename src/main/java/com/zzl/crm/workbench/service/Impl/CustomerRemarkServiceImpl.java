package com.zzl.crm.workbench.service.Impl;

import com.zzl.crm.workbench.mapper.CustomerRemarkMapper;
import com.zzl.crm.workbench.pojo.CustomerRemark;
import com.zzl.crm.workbench.service.CustomerRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ��������
 *
 * @author ֣����
 * @date 2022/05/09  22:16
 */
@Service("CustomerRemarkService")
public class CustomerRemarkServiceImpl implements CustomerRemarkService {
    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;
    /**
     * ������ӿͻ���ע
     * @param customerRemarkList
     * @return
     */
    @Override
    public int insertCustomerRemarks(List<CustomerRemark> customerRemarkList) {
        return customerRemarkMapper.insertCustomerRemarks(customerRemarkList);
    }
    /**
     * ���ݿͻ�id��ѯ��ǰ�ͻ����ж��ٱ�ע
     * @return
     */
    @Override
    public List<CustomerRemark> selectCustomerRemarkByCustomerId(String CustomerId) {
        return customerRemarkMapper.selectCustomerRemarkByCustomerId(CustomerId);
    }
    /**
     * �ڵ�ǰ�ͻ�����ӱ�ע
     * @param remark
     * @return
     */
    @Override
    public int insertCustomerRemark(CustomerRemark remark) {
        return customerRemarkMapper.insertCustomerRemark(remark);
    }
    /**
     * ����idɾ����ע
     * @param id
     * @return
     */
    @Override
    public int deleteCustomerRemarkById(String id) {
        return customerRemarkMapper.deleteCustomerRemarkById(id);
    }
    /**
     * ����id�޸ı�ע
     * @param remark
     * @return
     */
    @Override
    public int updateCustomerRemarkById(CustomerRemark remark) {
        return customerRemarkMapper.updateCustomerRemarkById(remark);
    }
}
