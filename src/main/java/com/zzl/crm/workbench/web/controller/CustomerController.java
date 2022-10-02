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
 * 功能描述
 *
 * @author 郑子浪
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
     * 客户首页面
     * @return
     */
    @RequestMapping("/workbench/customer/index.do")
    public String index(HttpServletRequest request){
        //查询用户表
        final List<User> user = userService.selectAdd();
        //存储到request域中
        request.setAttribute("user",user);
       return "workbench/customer/index";
    }

    /**
     * 根据当前线索添加客户
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
                //封装参数
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
            //调用service完成添加
            customerService.clueConvertBusiness(map);
            obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("系统繁忙，请稍后重试");
        }
        return obj;
    }

    /**
     * 分页条件查询
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
        //封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("phone",phone);
        map.put("website",website);
        map.put("begin",(currentPage-1)*pageSize);
        map.put("pageSize",pageSize);
        //调用customerService完成查询
        final List<Customer> customers = customerService.selectCustomerByPageAndCondition(map);
        final int total = customerService.selectCustomerTotalAndCondition(map);
        //响应
        Map<String,Object> map2 = new HashMap<>();
        map2.put("customers",customers);
        map2.put("total",total);
        return map2;
    }
    /**
     * 添加客户
     * @param customer
     * @param session
     * @return
     */
    @RequestMapping("/workbench/customer/insertCustomer.do")
    @ResponseBody
    public Object insertCustomer(Customer customer,HttpSession session){
        User user = (User)session.getAttribute(Constants.SESSION_USER);
        //封装参数
        customer.setId(UUIDUtils.getUUID());
        customer.setCreateTime(DateUtils.formateDateTime(new Date()));
        customer.setCreateBy(user.getId());
        ReturnObject obj = new ReturnObject();
        try {
            //调用customerService完成添加
            final int itn = customerService.insertCustomer(customer);
            if(itn>0){
                obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                obj.setMessage("系统繁忙，请稍后重试");
            }
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("系统繁忙，请稍后重试");
        }
        return obj;
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @RequestMapping("/workbench/customer/selectCustomerById.do")
    @ResponseBody
    public Object selectCustomerById(String id){
        //调用customerService完成查询
        final Customer customer = customerService.selectCustomerById(id);
        //响应
        return customer;
    }

    /**
     * 根据id修改
     * @param customer
     * @param session
     * @return
     */
    @RequestMapping("/workbench/customer/editSaveCustomerById.do")
    @ResponseBody
    public Object editSaveCustomerById(Customer customer,HttpSession session){
        User user =(User) session.getAttribute(Constants.SESSION_USER);
        //封装参数
        customer.setEditBy(user.getId());
        customer.setEditTime(DateUtils.formateDateTime(new Date()));

        ReturnObject obj =new ReturnObject();
        try {
            //调用CustomerService完成修改
            final int itn = customerService.updateCustomerById(customer);
            if(itn>0){
                obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                obj.setMessage("系统繁忙，请稍后重试");
            }
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("系统繁忙，请稍后重试");
        }
        return obj;
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @RequestMapping("/workbench/customer/deleteCustomerById.do")
    @ResponseBody
    public Object deleteCustomerById(String[] id){
        ReturnObject obj = new ReturnObject();
        try {
            //调用Service完成删除
            final int itn = customerService.deleteCustomerById(id);
            if(itn>0){
                obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                obj.setMessage("系统繁忙，请稍候重试");
            }
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("系统繁忙，请稍候重试");
        }
        return obj;
    }

    /**
     * 根据id查询客户,备注的详细信息，下拉列表，联系人，交易。并跳转到detail页面
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/workbench/customer/customerDetail.do")
    public String customerDetail(String id,HttpServletRequest request){
        //调用service查询
        final Customer customer = customerService.selectCustomerById(id);
        final List<CustomerRemark> crList = customerRemarkService.selectCustomerRemarkByCustomerId(id);
        final List<Contacts> contacts = contactsService.selectContactsDetailed();
        final List<User> users = userService.selectAdd();
        final List<DicValue> appellations = dicValueService.queryDicValueByTypeCode("appellation");
        final List<DicValue> sources = dicValueService.queryDicValueByTypeCode("source");
        final List<Tran> trans = tranService.selectTranDetailed();
        //查询到的数据存储到request域中;
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
     * 在当前客户下添加备注
     * @param remark
     * @param session
     * @return
     */
    @RequestMapping("/workbench/customer/insertCustomerRemark.do")
    @ResponseBody
    public Object insertCustomerRemark(CustomerRemark remark,HttpSession session){
        User user = (User)session.getAttribute(Constants.SESSION_USER);
        //封装参数
        remark.setId(UUIDUtils.getUUID());
        remark.setCreateBy(user.getId());
        remark.setCreateTime(DateUtils.formateDateTime(new Date()));
        remark.setEditFlag(Constants.CUSTOMER_REMARK_EDIT_FLAG_NO);
        ReturnObject obj = new ReturnObject();
        try {
            //调用service完成添加
            final int itn = customerRemarkService.insertCustomerRemark(remark);
            if(itn>0){
                obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                obj.setRetData(remark);
            }else{
                obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                obj.setMessage("系统繁忙，请稍候重试");
            }
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("系统繁忙，请稍候重试");
        }
        return obj;
    }

    /**
     * 根据id删除备注
     * @param id
     * @return
     */
    @RequestMapping("/workbench/customer/deleteCustomerRemarkById.do")
    @ResponseBody
    public Object deleteCustomerRemarkById(String id){
        ReturnObject obj = new ReturnObject();
        try {
            //调用service完成删除
            final int itn = customerRemarkService.deleteCustomerRemarkById(id);
            if(itn>0){
                obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                obj.setMessage("系统繁忙，请稍候重试");
            }
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("系统繁忙，请稍候重试");
        }
        return obj;
    }

    /**
     * 根据id修改备注
     * @param remark
     * @param session
     * @return
     */
    @RequestMapping("/workbench/customer/updateCustomerRemarkById.do")
    @ResponseBody
    public Object updateCustomerRemarkById(CustomerRemark remark,HttpSession session){
        User user = (User)session.getAttribute(Constants.SESSION_USER);
        //封装参数
        remark.setEditBy(user.getId());
        remark.setEditTime(DateUtils.formateDateTime(new Date()));
        remark.setEditFlag(Constants.CUSTOMER_REMARK_EDIT_FLAG_YES);

        ReturnObject obj = new ReturnObject();
        try {
            //调用service完成修改
            int itn=customerRemarkService.updateCustomerRemarkById(remark);
            if(itn>0){
                obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                obj.setRetData(remark);
            }else{
                obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                obj.setMessage("系统繁忙，请稍候重试");
            }
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("系统繁忙，请稍候重试");
        }
        return obj;
    }

    /**
     * 根据客户名称模糊查询
     * @param customerName
     * @return
     */
    @RequestMapping("/workbench/customer/selectCustomerByName.do")
    @ResponseBody
    public Object selectCustomerByName(String customerName){
        //查询
        final List<String> strings = customerService.selectAllCustomerName(customerName);
        //返回
        return strings;
    }

    /**
     * 保存添加的联系人
     * @param map
     * @param session
     * @return
     */
    @RequestMapping("/workbench/customer/insertContacts.do")
    @ResponseBody
    public Object saveCreateContacts(@RequestParam Map<String,Object> map, HttpSession session){
        //封装参数
        map.put(Constants.SESSION_USER,session.getAttribute(Constants.SESSION_USER));
        ReturnObject obj = new ReturnObject();
        try {
            //调用ContactsService完成添加
            final Contacts contact = contactsService.addContacts(map);
            obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            obj.setRetData(contact);
        }catch(Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("系统繁忙，请稍后重试");
        }
       return obj;
    }

    /**
     * 根据id删除客户下的联系人
     * @param id
     * @return
     */
    @RequestMapping("/workbench/customer/deleteContactsById.do")
    @ResponseBody
    public Object deleteContactsById(String id){
        ReturnObject obj = new ReturnObject();
        try {
            //删除
            contactsService.deleteContactsById(id);
            obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("系统繁忙，请稍后重试");
        }
        return obj;
    }
}
