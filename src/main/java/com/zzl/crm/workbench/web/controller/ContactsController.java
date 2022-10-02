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
 * ��������
 *
 * @author ֣����
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
     * ��ת��ϵ����ҳ
     * @param request
     * @return
     */
    @RequestMapping("/workbench/contacts/index.do")
    public String index(HttpServletRequest request){
        //��ѯ�����û����ƺ�����Դ
        List<User> users = service.selectAdd();
        List<DicValue> sources = dicValueService.queryDicValueByTypeCode("source");
        List<DicValue> appellations = dicValueService.queryDicValueByTypeCode("appellation");
        //�洢��request����
        request.setAttribute("users",users);
        request.setAttribute("sources",sources);
        request.setAttribute("appellations",appellations);
        //��ת
        return "workbench/contacts/index";
    }

    /**
     * �����ϵ��
     * @param map
     * @param session
     * @return
     */
    @RequestMapping("/workbench/contacts/createContacts.do")
    @ResponseBody
    public Object createContacts(@RequestParam Map<String,Object> map, HttpSession session){
        //��װ����
        map.put(Constants.SESSION_USER,session.getAttribute(Constants.SESSION_USER));
        ReturnObject obj = new ReturnObject();
        try {
            //���
            contactsService.addContacts(map);
            obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("ϵͳ��æ�����Ժ�����");
        };
        return obj;
    }

    /**
     * ��ҳ������ѯ
     * @param map
     * @return
     */
    @RequestMapping("/workbench/contacts/queryContactsByPageAnCondition.do")
    @ResponseBody
    public Object queryContactsByPageAnCondition(@RequestParam Map<String,Object> map,int current,int pageSize){
        //���㵱ǰ��ʼ����
       map.put("begin",(current-1)*pageSize);
       map.put("pageSize",pageSize);
        //���÷�����ѯ
         List<Contacts> contacts = contactsService.selectContactsByPageAndCondition(map);
        //�ܼ�¼��
         int totalCount = contactsService.selectContactsTotalAndCondition(map);
        //��Ӧ
        Map<String,Object>map2 = new HashMap<>();
        map2.put("contacts",contacts);
        map2.put("totalCount",totalCount);
        return map2;
    }

    /**
     * ����id����ɾ��
     * @param ids
     * @return
     */
    @RequestMapping("/workbench/contacts/deleteContactsByIds.do")
    @ResponseBody
    public Object deleteContactsByIds(String[] ids){
        ReturnObject obj = new ReturnObject();
        try{
            //ɾ��
            contactsService.deleteContactsByIds(ids);
            obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
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
    @RequestMapping("/workbench/contacts/queryContactsById.do")
    @ResponseBody
    public Object queryContactsById(String id){
        //��ѯ
        Contacts contacts = contactsService.selectContactsById(id);
        return contacts;
    }

    /**
     * ����id�޸�
     * @param map
     * @param session
     * @return
     */
    @RequestMapping("/workbench/contacts/ReviseContactsById.do")
    @ResponseBody
    public Object ReviseContactsById(@RequestParam Map<String,Object>map,HttpSession session){
        //��װ����
        map.put(Constants.SESSION_USER,session.getAttribute(Constants.SESSION_USER));
        ReturnObject obj = new ReturnObject();
        try {
            contactsService.updateContactsById(map);
            //�޸�
            obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("ϵͳ��æ�����Ժ�����");
        }
        return obj;
    }

    /**
     * ��ϵ�˱�ע����
     * @param contactsId
     * @param request
     * @return
     */
    @RequestMapping("/workbench/contacts/forwardDetail.do")
    public String forwardDetail(String contactsId,HttpServletRequest request){
        //��ѯ��ǰ��ϵ�˵���ϸ��Ϣ
        Contacts contacts = contactsService.selectContactsById(contactsId);
        //���ݵ�ǰid��ѯ��ϵ�˱�ע
        List<ContactsRemark> conReList = contactsRemarkService.selectContactsRemarkByContactsId(contactsId);
        //��ѯ����
        List<Tran> trans = tranService.selectTranDetailed();
        //���ݽ���stage��ѯ������
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        for(Tran re:trans){
            String bu = bundle.getString(re.getStage());
            re.setPossibility(bu);
        }
        //����contactsId��ѯ�г��
        List<Activity> activityList = activityService.selectActivityByContactsId(contactsId);
        //�洢��request����
        request.setAttribute("contacts",contacts);
        request.setAttribute("conReList",conReList);
        request.setAttribute("trans",trans);
        request.setAttribute("activityList",activityList);
        //ת��
        return "workbench/contacts/detail";
    }

    /**
     * �����ϵ�˱�ע
     * @param remark
     * @param session
     * @return
     */
    @RequestMapping("/workbench/contacts/addContactsRemark.do")
    @ResponseBody
    public Object addContactsRemark(ContactsRemark remark,HttpSession session){
        User user = (User)session.getAttribute(Constants.SESSION_USER);
        //��װ����
        remark.setId(UUIDUtils.getUUID());
        remark.setEditFlag(Constants.CONTACTS_REMARK_EDIT_FLAG_NO);
        remark.setCreateTime(DateUtils.formateDateTime(new Date()));
        remark.setCreateBy(user.getId());
        ReturnObject obj = new ReturnObject();
        try {
            //���
            contactsRemarkService.addContactsRemark(remark);
            obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            obj.setRetData(remark);
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
    @RequestMapping("/workbench/contacts/removeContactsRemark.do")
    @ResponseBody
    public Object removeContactsRemark(String id){
        ReturnObject obj = new ReturnObject();
        try {
            //ɾ��
            contactsRemarkService.emptyContactsRemarkById(id);
            obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("ϵͳ��æ�����Ժ�����");
        }
        return obj;
    }

    /**
     * ����id�޸�
     * @param remark
     * @param session
     * @return
     */
    @RequestMapping("/workbench/contacts/saveEditContactsRemarkById.do")
    @ResponseBody
    public Object saveEditContactsRemarkById(ContactsRemark remark,HttpSession session){
        User user=(User)session.getAttribute(Constants.SESSION_USER);
        //��װ����
        remark.setEditBy(user.getId());
        remark.setEditTime(DateUtils.formateDateTime(new Date()));
        remark.setEditFlag(Constants.CONTACTS_REMARK_EDIT_FLAG_YES);
        ReturnObject obj = new ReturnObject();
        try {
            //�޸�
            contactsRemarkService.saveEditContactsRemarkById(remark);
            obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("ϵͳ��æ�����Ժ�����");
        }
        return obj;
    }

    /**
     * ������ϵ�˺��г��nameģ����ѯ
     * @param map
     * @return
     */
    @RequestMapping("/workbench/contacts/inquireActivityByContactsId.do")
    @ResponseBody
    public Object inquireActivityByContactsId(@RequestParam Map<String,Object>map){
        //��ѯ
        final List<Activity> activityList = activityService.selectActivityByActivityByContactsId(map);
        return activityList;
    }

    /**
     * �����г��
     * @param ids
     * @param contactsId
     * @return
     */
    @RequestMapping("/workbench/contacts/inquireActivityContactsRelationByContactsId.do")
    @ResponseBody
    public Object inquireActivityContactsRelationByContactsId(String[]ids,String contactsId){
        //ÿ����һ�δ���һ������
        ContactsActivityRelation car=null;
        List<ContactsActivityRelation> carList = new ArrayList<>();
        //��������
        for(String cr:ids) {
            car = new ContactsActivityRelation();
            car.setId(UUIDUtils.getUUID());
            car.setActivityId(cr);
            car.setContactsId(contactsId);
            //�������Ķ���洢��List������
            carList.add(car);
        }
            ReturnObject obj = new ReturnObject();
            try {
                //���ContactsActivityRelation
                 int itn = contactsActivityRelationService.insertContactsActivityRelations(carList);
                 if(itn>0){
                     //����ѡ�е��г��id��ѯ���г��
                     final List<Activity> activityList = activityService.inquireContactsActivityRelationByContactsId(ids);
                     obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                     obj.setRetData(activityList);
                 }
            }catch (Exception e){
                e.printStackTrace();
                obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                obj.setMessage("ϵͳ��æ�����Ժ�����");
        }
            return obj;
    }

    /**
     * ɾ�����������г��
     * @param relation
     * @return
     */
    @RequestMapping("/workbench/contacts/liftContactsActivityRelationByActivityId.do")
    @ResponseBody
    public Object liftContactsActivityRelationByActivityId(ContactsActivityRelation relation){
        ReturnObject obj = new ReturnObject();
        try {
            //ɾ��ָ���г��
            int itn = contactsActivityRelationService.liftContactsActivityRelationByActivityId(relation);
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
}
