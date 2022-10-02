package com.zzl.crm.workbench.web.controller;

import com.zzl.crm.commons.Constants.Constants;
import com.zzl.crm.commons.pojo.ReturnObject;
import com.zzl.crm.commons.utils.DateUtils;
import com.zzl.crm.commons.utils.UUIDUtils;
import com.zzl.crm.settings.pojo.DicValue;
import com.zzl.crm.settings.pojo.User;
import com.zzl.crm.settings.service.DicValueService;
import com.zzl.crm.settings.service.UserService;
import com.zzl.crm.workbench.pojo.*;
import com.zzl.crm.workbench.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/05/13  12:16
 */
@Controller
public class ContactsController {
    @Autowired
    private UserService service;
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private ContactsRemarkService contactsRemarkService;
    @Autowired
    private TranService tranService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ContactsActivityRelationService contactsActivityRelationService;
    /**
     * 跳转联系人首页
     * @param request
     * @return
     */
    @RequestMapping("/workbench/contacts/index.do")
    public String index(HttpServletRequest request){
        //查询所有用户，称呼，来源
        List<User> users = service.selectAdd();
        List<DicValue> sources = dicValueService.queryDicValueByTypeCode("source");
        List<DicValue> appellations = dicValueService.queryDicValueByTypeCode("appellation");
        //存储到request域中
        request.setAttribute("users",users);
        request.setAttribute("sources",sources);
        request.setAttribute("appellations",appellations);
        //跳转
        return "workbench/contacts/index";
    }

    /**
     * 添加联系人
     * @param map
     * @param session
     * @return
     */
    @RequestMapping("/workbench/contacts/createContacts.do")
    @ResponseBody
    public Object createContacts(@RequestParam Map<String,Object> map, HttpSession session){
        //封装数据
        map.put(Constants.SESSION_USER,session.getAttribute(Constants.SESSION_USER));
        ReturnObject obj = new ReturnObject();
        try {
            //添加
            contactsService.addContacts(map);
            obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("系统繁忙，请稍后重试");
        };
        return obj;
    }

    /**
     * 分页条件查询
     * @param map
     * @return
     */
    @RequestMapping("/workbench/contacts/queryContactsByPageAnCondition.do")
    @ResponseBody
    public Object queryContactsByPageAnCondition(@RequestParam Map<String,Object> map,int current,int pageSize){
        //计算当前开始索引
       map.put("begin",(current-1)*pageSize);
       map.put("pageSize",pageSize);
        //调用方法查询
         List<Contacts> contacts = contactsService.selectContactsByPageAndCondition(map);
        //总记录数
         int totalCount = contactsService.selectContactsTotalAndCondition(map);
        //响应
        Map<String,Object>map2 = new HashMap<>();
        map2.put("contacts",contacts);
        map2.put("totalCount",totalCount);
        return map2;
    }

    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    @RequestMapping("/workbench/contacts/deleteContactsByIds.do")
    @ResponseBody
    public Object deleteContactsByIds(String[] ids){
        ReturnObject obj = new ReturnObject();
        try{
            //删除
            contactsService.deleteContactsByIds(ids);
            obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
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
    @RequestMapping("/workbench/contacts/queryContactsById.do")
    @ResponseBody
    public Object queryContactsById(String id){
        //查询
        Contacts contacts = contactsService.selectContactsById(id);
        return contacts;
    }

    /**
     * 根据id修改
     * @param map
     * @param session
     * @return
     */
    @RequestMapping("/workbench/contacts/ReviseContactsById.do")
    @ResponseBody
    public Object ReviseContactsById(@RequestParam Map<String,Object>map,HttpSession session){
        //封装数据
        map.put(Constants.SESSION_USER,session.getAttribute(Constants.SESSION_USER));
        ReturnObject obj = new ReturnObject();
        try {
            contactsService.updateContactsById(map);
            //修改
            obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("系统繁忙，请稍后重试");
        }
        return obj;
    }

    /**
     * 联系人备注界面
     * @param contactsId
     * @param request
     * @return
     */
    @RequestMapping("/workbench/contacts/forwardDetail.do")
    public String forwardDetail(String contactsId,HttpServletRequest request){
        //查询当前联系人的详细信息
        Contacts contacts = contactsService.selectContactsById(contactsId);
        //根据当前id查询联系人备注
        List<ContactsRemark> conReList = contactsRemarkService.selectContactsRemarkByContactsId(contactsId);
        //查询交易
        List<Tran> trans = tranService.selectTranDetailed();
        //根据交易stage查询可能性
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        for(Tran re:trans){
            String bu = bundle.getString(re.getStage());
            re.setPossibility(bu);
        }
        //根据contactsId查询市场活动
        List<Activity> activityList = activityService.selectActivityByContactsId(contactsId);
        //存储到request域中
        request.setAttribute("contacts",contacts);
        request.setAttribute("conReList",conReList);
        request.setAttribute("trans",trans);
        request.setAttribute("activityList",activityList);
        //转发
        return "workbench/contacts/detail";
    }

    /**
     * 添加联系人备注
     * @param remark
     * @param session
     * @return
     */
    @RequestMapping("/workbench/contacts/addContactsRemark.do")
    @ResponseBody
    public Object addContactsRemark(ContactsRemark remark,HttpSession session){
        User user = (User)session.getAttribute(Constants.SESSION_USER);
        //封装参数
        remark.setId(UUIDUtils.getUUID());
        remark.setEditFlag(Constants.CONTACTS_REMARK_EDIT_FLAG_NO);
        remark.setCreateTime(DateUtils.formateDateTime(new Date()));
        remark.setCreateBy(user.getId());
        ReturnObject obj = new ReturnObject();
        try {
            //添加
            contactsRemarkService.addContactsRemark(remark);
            obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            obj.setRetData(remark);
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
    @RequestMapping("/workbench/contacts/removeContactsRemark.do")
    @ResponseBody
    public Object removeContactsRemark(String id){
        ReturnObject obj = new ReturnObject();
        try {
            //删除
            contactsRemarkService.emptyContactsRemarkById(id);
            obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("系统繁忙，请稍后重试");
        }
        return obj;
    }

    /**
     * 根据id修改
     * @param remark
     * @param session
     * @return
     */
    @RequestMapping("/workbench/contacts/saveEditContactsRemarkById.do")
    @ResponseBody
    public Object saveEditContactsRemarkById(ContactsRemark remark,HttpSession session){
        User user=(User)session.getAttribute(Constants.SESSION_USER);
        //封装参数
        remark.setEditBy(user.getId());
        remark.setEditTime(DateUtils.formateDateTime(new Date()));
        remark.setEditFlag(Constants.CONTACTS_REMARK_EDIT_FLAG_YES);
        ReturnObject obj = new ReturnObject();
        try {
            //修改
            contactsRemarkService.saveEditContactsRemarkById(remark);
            obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("系统繁忙，请稍后重试");
        }
        return obj;
    }

    /**
     * 根据联系人和市场活动name模糊查询
     * @param map
     * @return
     */
    @RequestMapping("/workbench/contacts/inquireActivityByContactsId.do")
    @ResponseBody
    public Object inquireActivityByContactsId(@RequestParam Map<String,Object>map){
        //查询
        final List<Activity> activityList = activityService.selectActivityByActivityByContactsId(map);
        return activityList;
    }

    /**
     * 关联市场活动
     * @param ids
     * @param contactsId
     * @return
     */
    @RequestMapping("/workbench/contacts/inquireActivityContactsRelationByContactsId.do")
    @ResponseBody
    public Object inquireActivityContactsRelationByContactsId(String[]ids,String contactsId){
        //每遍历一次创建一个对象
        ContactsActivityRelation car=null;
        List<ContactsActivityRelation> carList = new ArrayList<>();
        //遍历数组
        for(String cr:ids) {
            car = new ContactsActivityRelation();
            car.setId(UUIDUtils.getUUID());
            car.setActivityId(cr);
            car.setContactsId(contactsId);
            //将遍历的对象存储到List集合中
            carList.add(car);
        }
            ReturnObject obj = new ReturnObject();
            try {
                //添加ContactsActivityRelation
                 int itn = contactsActivityRelationService.insertContactsActivityRelations(carList);
                 if(itn>0){
                     //根据选中的市场活动id查询该市场活动
                     final List<Activity> activityList = activityService.inquireContactsActivityRelationByContactsId(ids);
                     obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                     obj.setRetData(activityList);
                 }
            }catch (Exception e){
                e.printStackTrace();
                obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                obj.setMessage("系统繁忙，请稍后重试");
        }
            return obj;
    }

    /**
     * 删除被关联的市场活动
     * @param relation
     * @return
     */
    @RequestMapping("/workbench/contacts/liftContactsActivityRelationByActivityId.do")
    @ResponseBody
    public Object liftContactsActivityRelationByActivityId(ContactsActivityRelation relation){
        ReturnObject obj = new ReturnObject();
        try {
            //删除指定市场活动
            int itn = contactsActivityRelationService.liftContactsActivityRelationByActivityId(relation);
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
}
