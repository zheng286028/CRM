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
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/05/04  16:48
 */
@Controller
public class ActivityRemarkController {
    @Autowired
    private activityRemarkService activityRemarkService;
    /**
     * 添加市场活动备注
     * @param remark
     * @return
     */
    @RequestMapping("/workbench/activity/savaCreateActivityRemark.do")
    @ResponseBody
    public Object savaCreateActivityRemark(activityRemark remark, HttpSession session){
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        //封装参数
        remark.setId(UUIDUtils.getUUID());
        remark.setCreateTime(DateUtils.formateDateTime(new Date()));
        remark.setCreateBy(user.getId());
        remark.setEditFlag(Constants.ACTIVITY_REMARK_EDIT_FLAG_NO);

        ReturnObject returnObject = new ReturnObject();
        try {
            //调用service添加
            int obj = activityRemarkService.saveCreateActivityRemark(remark);
            //判断添加成功与否
            if(obj>0){
                //添加成功
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(remark);
            }else{
                //失败
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙，请稍后重试");
            }
        }catch(Exception e){
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试");
        }
        //响应
        return returnObject;
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
   @RequestMapping("/workbench/activity/deleteActivityRemarkById.do")
   @ResponseBody
    public Object deleteActivityRemarkById(String id){
       ReturnObject returnObject = new ReturnObject();
       try {
           //调用service
           final int obj = activityRemarkService.deleteActivityRemarkById(id);
           //判断删除成功与否
           if(obj>0){
               returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
           }else {
               returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
               returnObject.setMessage("系统繁忙，请稍后重试");
           }
       }catch(Exception e){
           e.printStackTrace();
           returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
           returnObject.setMessage("系统繁忙，请稍后重试");
       }
       return returnObject;
   }

    /**
     * 根据id修改市场活动备注
     * @param remark
     * @param session
     * @return
     */
   @RequestMapping("/workbench/activity/updateActivityRemarkById.do")
   @ResponseBody
   public Object updateActivityRemarkById(activityRemark remark,HttpSession session){
       //封装参数
       User user = (User)session.getAttribute(Constants.SESSION_USER);
       remark.setEditTime(DateUtils.formateDateTime(new Date()));
       remark.setEditBy(user.getId());
       remark.setEditFlag(Constants.ACTIVITY_REMARK_EDIT_FLAG_YES);

       ReturnObject obj = new ReturnObject();
       try{
           //调用service
           final int itn = activityRemarkService.editActivityRemarkById(remark);
           //判断成功与否
           if(itn>0){
               //成功
               obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
               obj.setRetData(remark);
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
