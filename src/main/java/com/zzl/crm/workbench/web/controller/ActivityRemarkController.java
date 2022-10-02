package com.zzl.crm.workbench.web.controller;

import com.zzl.crm.commons.Constants.Constants;
import com.zzl.crm.commons.pojo.ReturnObject;
import com.zzl.crm.commons.utils.DateUtils;
import com.zzl.crm.commons.utils.UUIDUtils;
import com.zzl.crm.settings.pojo.User;
import com.zzl.crm.workbench.pojo.activityRemark;
import com.zzl.crm.workbench.service.activityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * ��������
 *
 * @author ֣����
 * @date 2022/05/04  16:48
 */
@Controller
public class ActivityRemarkController {
    @Autowired
    private activityRemarkService activityRemarkService;
    /**
     * ����г����ע
     * @param remark
     * @return
     */
    @RequestMapping("/workbench/activity/savaCreateActivityRemark.do")
    @ResponseBody
    public Object savaCreateActivityRemark(activityRemark remark, HttpSession session){
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        //��װ����
        remark.setId(UUIDUtils.getUUID());
        remark.setCreateTime(DateUtils.formateDateTime(new Date()));
        remark.setCreateBy(user.getId());
        remark.setEditFlag(Constants.ACTIVITY_REMARK_EDIT_FLAG_NO);

        ReturnObject returnObject = new ReturnObject();
        try {
            //����service���
            int obj = activityRemarkService.saveCreateActivityRemark(remark);
            //�ж���ӳɹ����
            if(obj>0){
                //��ӳɹ�
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(remark);
            }else{
                //ʧ��
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("ϵͳ��æ�����Ժ�����");
            }
        }catch(Exception e){
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("ϵͳ��æ�����Ժ�����");
        }
        //��Ӧ
        return returnObject;
    }

    /**
     * ����idɾ��
     * @param id
     * @return
     */
   @RequestMapping("/workbench/activity/deleteActivityRemarkById.do")
   @ResponseBody
    public Object deleteActivityRemarkById(String id){
       ReturnObject returnObject = new ReturnObject();
       try {
           //����service
           final int obj = activityRemarkService.deleteActivityRemarkById(id);
           //�ж�ɾ���ɹ����
           if(obj>0){
               returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
           }else {
               returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
               returnObject.setMessage("ϵͳ��æ�����Ժ�����");
           }
       }catch(Exception e){
           e.printStackTrace();
           returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
           returnObject.setMessage("ϵͳ��æ�����Ժ�����");
       }
       return returnObject;
   }

    /**
     * ����id�޸��г����ע
     * @param remark
     * @param session
     * @return
     */
   @RequestMapping("/workbench/activity/updateActivityRemarkById.do")
   @ResponseBody
   public Object updateActivityRemarkById(activityRemark remark,HttpSession session){
       //��װ����
       User user = (User)session.getAttribute(Constants.SESSION_USER);
       remark.setEditTime(DateUtils.formateDateTime(new Date()));
       remark.setEditBy(user.getId());
       remark.setEditFlag(Constants.ACTIVITY_REMARK_EDIT_FLAG_YES);

       ReturnObject obj = new ReturnObject();
       try{
           //����service
           final int itn = activityRemarkService.editActivityRemarkById(remark);
           //�жϳɹ����
           if(itn>0){
               //�ɹ�
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
}
