package com.zzl.crm.workbench.web.controller;

import com.zzl.crm.commons.Constants.Constants;
import com.zzl.crm.commons.pojo.ReturnObject;
import com.zzl.crm.commons.utils.DateUtils;
import com.zzl.crm.commons.utils.UUIDUtils;
import com.zzl.crm.settings.pojo.DicValue;
import com.zzl.crm.settings.pojo.User;
import com.zzl.crm.settings.service.DicValueService;
import com.zzl.crm.settings.service.UserService;
import com.zzl.crm.workbench.pojo.Contacts;
import com.zzl.crm.workbench.pojo.Customer;
import com.zzl.crm.workbench.pojo.CustomerRemark;
import com.zzl.crm.workbench.pojo.Tran;
import com.zzl.crm.workbench.service.ContactsService;
import com.zzl.crm.workbench.service.CustomerRemarkService;
import com.zzl.crm.workbench.service.CustomerService;
import com.zzl.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ��������
 *
 * @author ֣����
 * @date 2022/05/09  1:20
 */
@Controller
public class CustomerController {
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRemarkService customerRemarkService;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private TranService tranService;
    /**
     * �ͻ���ҳ��
     * @return
     */
    @RequestMapping("/workbench/customer/index.do")
    public String index(HttpServletRequest request){
        //��ѯ�û���
        final List<User> user = userService.selectAdd();
        //�洢��request����
        request.setAttribute("user",user);
       return "workbench/customer/index";
    }

    /**
     * ���ݵ�ǰ������ӿͻ�
     * @param money
     * @param name
     * @param expectedDate
     * @param stage
     * @param activityId
     * @param isTransaction
     * @param clueId
     * @return
     */
    @RequestMapping("/workbench/customer/CustomerClue.do")
    @ResponseBody
    public Object CustomerClue(String money,String name, String expectedDate,String clueId,
                               String stage,String activityId,String isTransaction,HttpSession session
                               ){
                //��װ����
        Map<String,Object> map = new HashMap<>();
        map.put("money",money);
        map.put("name",name);
        map.put("expectedDate",expectedDate);
        map.put("clueId",clueId);
        map.put("stage",stage);
        map.put("activityId",activityId);
        map.put("isTransaction",isTransaction);
        map.put(Constants.SESSION_USER,session.getAttribute(Constants.SESSION_USER));
        ReturnObject obj = new ReturnObject();
        try {
            //����service������
            customerService.clueConvertBusiness(map);
            obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("ϵͳ��æ�����Ժ�����");
        }
        return obj;
    }

    /**
     * ��ҳ������ѯ
     * @param name
     * @param owner
     * @param phone
     * @param website
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping("/workbench/customer/queryCustomerByPageAndCondition.do")
    @ResponseBody
    public Object queryCustomerByPageAndCondition(String name,String owner,String phone,String website,
                                                  int currentPage,int pageSize
                                                  ){
        //��װ����
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("phone",phone);
        map.put("website",website);
        map.put("begin",(currentPage-1)*pageSize);
        map.put("pageSize",pageSize);
        //����customerService��ɲ�ѯ
        final List<Customer> customers = customerService.selectCustomerByPageAndCondition(map);
        final int total = customerService.selectCustomerTotalAndCondition(map);
        //��Ӧ
        Map<String,Object> map2 = new HashMap<>();
        map2.put("customers",customers);
        map2.put("total",total);
        return map2;
    }
    /**
     * ��ӿͻ�
     * @param customer
     * @param session
     * @return
     */
    @RequestMapping("/workbench/customer/insertCustomer.do")
    @ResponseBody
    public Object insertCustomer(Customer customer,HttpSession session){
        User user = (User)session.getAttribute(Constants.SESSION_USER);
        //��װ����
        customer.setId(UUIDUtils.getUUID());
        customer.setCreateTime(DateUtils.formateDateTime(new Date()));
        customer.setCreateBy(user.getId());
        ReturnObject obj = new ReturnObject();
        try {
            //����customerService������
            final int itn = customerService.insertCustomer(customer);
            if(itn>0){
                obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                obj.setMessage("ϵͳ��æ�����Ժ�����");
            }
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("ϵͳ��æ�����Ժ�����");
        }
        return obj;
    }

    /**
     * ����id��ѯ
     * @param id
     * @return
     */
    @RequestMapping("/workbench/customer/selectCustomerById.do")
    @ResponseBody
    public Object selectCustomerById(String id){
        //����customerService��ɲ�ѯ
        final Customer customer = customerService.selectCustomerById(id);
        //��Ӧ
        return customer;
    }

    /**
     * ����id�޸�
     * @param customer
     * @param session
     * @return
     */
    @RequestMapping("/workbench/customer/editSaveCustomerById.do")
    @ResponseBody
    public Object editSaveCustomerById(Customer customer,HttpSession session){
        User user =(User) session.getAttribute(Constants.SESSION_USER);
        //��װ����
        customer.setEditBy(user.getId());
        customer.setEditTime(DateUtils.formateDateTime(new Date()));

        ReturnObject obj =new ReturnObject();
        try {
            //����CustomerService����޸�
            final int itn = customerService.updateCustomerById(customer);
            if(itn>0){
                obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                obj.setMessage("ϵͳ��æ�����Ժ�����");
            }
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("ϵͳ��æ�����Ժ�����");
        }
        return obj;
    }

    /**
     * ����idɾ��
     * @param id
     * @return
     */
    @RequestMapping("/workbench/customer/deleteCustomerById.do")
    @ResponseBody
    public Object deleteCustomerById(String[] id){
        ReturnObject obj = new ReturnObject();
        try {
            //����Service���ɾ��
            final int itn = customerService.deleteCustomerById(id);
            if(itn>0){
                obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                obj.setMessage("ϵͳ��æ�����Ժ�����");
            }
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("ϵͳ��æ�����Ժ�����");
        }
        return obj;
    }

    /**
     * ����id��ѯ�ͻ�,��ע����ϸ��Ϣ�������б���ϵ�ˣ����ס�����ת��detailҳ��
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/workbench/customer/customerDetail.do")
    public String customerDetail(String id,HttpServletRequest request){
        //����service��ѯ
        final Customer customer = customerService.selectCustomerById(id);
        final List<CustomerRemark> crList = customerRemarkService.selectCustomerRemarkByCustomerId(id);
        final List<Contacts> contacts = contactsService.selectContactsDetailed();
        final List<User> users = userService.selectAdd();
        final List<DicValue> appellations = dicValueService.queryDicValueByTypeCode("appellation");
        final List<DicValue> sources = dicValueService.queryDicValueByTypeCode("source");
        final List<Tran> trans = tranService.selectTranDetailed();
        //��ѯ�������ݴ洢��request����;
        request.setAttribute("customer",customer);
        request.setAttribute("crList",crList);
        request.setAttribute("contacts",contacts);
        request.setAttribute("users",users);
        request.setAttribute("appellations",appellations);
        request.setAttribute("sources",sources);
        request.setAttribute("trans",trans);
        return "workbench/customer/detail";
    }

    /**
     * �ڵ�ǰ�ͻ�����ӱ�ע
     * @param remark
     * @param session
     * @return
     */
    @RequestMapping("/workbench/customer/insertCustomerRemark.do")
    @ResponseBody
    public Object insertCustomerRemark(CustomerRemark remark,HttpSession session){
        User user = (User)session.getAttribute(Constants.SESSION_USER);
        //��װ����
        remark.setId(UUIDUtils.getUUID());
        remark.setCreateBy(user.getId());
        remark.setCreateTime(DateUtils.formateDateTime(new Date()));
        remark.setEditFlag(Constants.CUSTOMER_REMARK_EDIT_FLAG_NO);
        ReturnObject obj = new ReturnObject();
        try {
            //����service������
            final int itn = customerRemarkService.insertCustomerRemark(remark);
            if(itn>0){
                obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                obj.setRetData(remark);
            }else{
                obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                obj.setMessage("ϵͳ��æ�����Ժ�����");
            }
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("ϵͳ��æ�����Ժ�����");
        }
        return obj;
    }

    /**
     * ����idɾ����ע
     * @param id
     * @return
     */
    @RequestMapping("/workbench/customer/deleteCustomerRemarkById.do")
    @ResponseBody
    public Object deleteCustomerRemarkById(String id){
        ReturnObject obj = new ReturnObject();
        try {
            //����service���ɾ��
            final int itn = customerRemarkService.deleteCustomerRemarkById(id);
            if(itn>0){
                obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                obj.setMessage("ϵͳ��æ�����Ժ�����");
            }
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("ϵͳ��æ�����Ժ�����");
        }
        return obj;
    }

    /**
     * ����id�޸ı�ע
     * @param remark
     * @param session
     * @return
     */
    @RequestMapping("/workbench/customer/updateCustomerRemarkById.do")
    @ResponseBody
    public Object updateCustomerRemarkById(CustomerRemark remark,HttpSession session){
        User user = (User)session.getAttribute(Constants.SESSION_USER);
        //��װ����
        remark.setEditBy(user.getId());
        remark.setEditTime(DateUtils.formateDateTime(new Date()));
        remark.setEditFlag(Constants.CUSTOMER_REMARK_EDIT_FLAG_YES);

        ReturnObject obj = new ReturnObject();
        try {
            //����service����޸�
            int itn=customerRemarkService.updateCustomerRemarkById(remark);
            if(itn>0){
                obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                obj.setRetData(remark);
            }else{
                obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                obj.setMessage("ϵͳ��æ�����Ժ�����");
            }
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("ϵͳ��æ�����Ժ�����");
        }
        return obj;
    }

    /**
     * ���ݿͻ�����ģ����ѯ
     * @param customerName
     * @return
     */
    @RequestMapping("/workbench/customer/selectCustomerByName.do")
    @ResponseBody
    public Object selectCustomerByName(String customerName){
        //��ѯ
        final List<String> strings = customerService.selectAllCustomerName(customerName);
        //����
        return strings;
    }

    /**
     * ������ӵ���ϵ��
     * @param map
     * @param session
     * @return
     */
    @RequestMapping("/workbench/customer/insertContacts.do")
    @ResponseBody
    public Object saveCreateContacts(@RequestParam Map<String,Object> map, HttpSession session){
        //��װ����
        map.put(Constants.SESSION_USER,session.getAttribute(Constants.SESSION_USER));
        ReturnObject obj = new ReturnObject();
        try {
            //����ContactsService������
            final Contacts contact = contactsService.addContacts(map);
            obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            obj.setRetData(contact);
        }catch(Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("ϵͳ��æ�����Ժ�����");
        }
       return obj;
    }

    /**
     * ����idɾ���ͻ��µ���ϵ��
     * @param id
     * @return
     */
    @RequestMapping("/workbench/customer/deleteContactsById.do")
    @ResponseBody
    public Object deleteContactsById(String id){
        ReturnObject obj = new ReturnObject();
        try {
            //ɾ��
            contactsService.deleteContactsById(id);
            obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("ϵͳ��æ�����Ժ�����");
        }
        return obj;
    }
}
