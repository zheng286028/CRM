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
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/05/05  18:22
 */
@Controller
public class ClueController {
    @Autowired
    private UserService userService;
    @Autowired
    private ClueService clueService;
    @Autowired
    private ClueRemarkService clueRemarkService;
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ClueActivityRelationService clueActivityRelationService;
    /**
     * 线索首页
     * @return
     */
    @RequestMapping("/workbench/clue/clueIndex.do")
    public String clueIndex(HttpServletRequest request){
        //查询线索所需的动态数据
        final List<User> user = userService.selectAdd();
        final List<DicValue> appellation = dicValueService.queryDicValueByTypeCode("appellation");
        final List<DicValue> clueState = dicValueService.queryDicValueByTypeCode("clueState");
        final List<DicValue> source = dicValueService.queryDicValueByTypeCode("source");
        //将数据存储到request域中
        request.setAttribute("user",user);
        request.setAttribute("appellation",appellation);
        request.setAttribute("clueState",clueState);
        request.setAttribute("source",source);
        return "workbench/clue/index";
    }

    /**
     * 添加线索
     * @param clue
     * @return
     */
    @RequestMapping("/workbench/clue/insertClue.do")
    @ResponseBody
    public Object insertClue(Clue clue, HttpSession session){
      User user=(User) session.getAttribute(Constants.SESSION_USER);
        ReturnObject obj = new ReturnObject();
      //封装参数
      clue.setId(UUIDUtils.getUUID());
      clue.setCreateTime(DateUtils.formateDateTime(new Date()));
      clue.setCreateBy(user.getId());
      try {
          //调用service添加
          final int itn = clueService.insertClue(clue);
          //判断是否成功
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
     * 分页条件查询
     * @param fullname
     * @param company
     * @param phone
     * @param source
     * @param owner
     * @param mphone
     * @param state
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping("/workbench/clue/selectClueByPageAndCondition.do")
    @ResponseBody
    public Object selectClueByPageAndCondition(String fullname,String company,String phone,
                                               String source,String owner,String mphone,String state,
                                               int currentPage,int pageSize
                                               ){
        //封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("source",source);
        map.put("owner",owner);
        map.put("mphone",mphone);
        map.put("state",state);
        map.put("begin",(currentPage-1)*pageSize);
        map.put("pageSize",pageSize);
        //调用service查询
        List<Clue> clueList = clueService.queryClueByPageAndCondition(map);
        int totalCount = clueService.queryClueByConditionTotal(map);
        //响应数据
        Map<String,Object> mapRet = new HashMap<>();
        mapRet.put("clueList",clueList);
        mapRet.put("totalCount",totalCount);
        return mapRet;
    }
    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    @RequestMapping("/workbench/clue/deleteClueByIds.do")
    @ResponseBody
    public Object deleteClueByIds(String[] ids){
        ReturnObject obj = new ReturnObject();
        try {
            //调用service
            final int in = clueService.deleteClueByIds(ids);
            if(in>0){
                obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                obj.setMessage("系统繁忙，请稍后重试");
            }
        }catch(Exception e){
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
    @RequestMapping("/workbench/clue/selectById.do")
    @ResponseBody
    public Object selectById(String id){
        //调用service查询
        final Clue clue = clueService.selectById(id);
        //响应信息
        return clue;
    }

    /**
     * 根据id修改
     * @param clue
     * @param session
     * @return
     */
    @RequestMapping("/workbench/clue/savaEditClueById.do")
    @ResponseBody
    public Object savaEditClueById(Clue clue,HttpSession session){
        User user = (User)session.getAttribute(Constants.SESSION_USER);
        //封装参数
        clue.setEditTime(DateUtils.formateDateTime(new Date()));
        clue.setEditBy(user.getId());

        ReturnObject obj = new ReturnObject();
        try {
            //调用service完成修改
            final int itn = clueService.updateClueById(clue);
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
     * 根据id查询线索及线索备注详细信息,和当前线索的全部市场活动
     * @param id
     * @return
     */
    @RequestMapping("/workbench/clue/queryClueAndClueRemarkById.do")
    public String queryClueAndClueRemarkById(String id,HttpServletRequest request){
        //调用service查询
        final Clue clue = clueService.selectById(id);
        List<ClueRemark> clueRemarkList = clueRemarkService.selectByClueId(id);
        List<Activity> activityList=activityService.queryActivityByClueId(id);

        //将查到的信息存储到request域中
        request.setAttribute("clue",clue);
        request.setAttribute("clueRemarkList",clueRemarkList);
        request.setAttribute("activityList",activityList);
        //转发
        return "workbench/clue/detail";
    }

    /**
     * 添加线索备注
     * @param clueRemark
     * @param session
     * @return
     */
    @RequestMapping("/workbench/clue/insertClueRemark.do")
    @ResponseBody
    public Object insertClueRemark(ClueRemark clueRemark,HttpSession session){
        //封装参数
        User user = (User)session.getAttribute(Constants.SESSION_USER);

        clueRemark.setId(UUIDUtils.getUUID());
        clueRemark.setCreateBy(user.getId());
        clueRemark.setCreateTime(DateUtils.formateDateTime(new Date()));
        clueRemark.setEditFlag(Constants.Clue_REMARK_EDIT_FLAG_NO);

        ReturnObject obj = new ReturnObject();
        try {
        //调用service完成添加
        final int itn = clueRemarkService.insertClueRemark(clueRemark);
        if(itn>0){
            obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            obj.setRetData(clueRemark);
        }else{
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("系统繁忙，请稍后重试");
        }
        }catch(Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("系统繁忙，请稍后重试");
        }
        return obj;
    }

    /**
     * 根据id删除线索备注
     * @param id
     * @return
     */
    @RequestMapping("/workbench/clue/deleteClueRemarkById.do")
    @ResponseBody
    public Object deleteClueRemarkById(String id){
        ReturnObject obj = new ReturnObject();
        try {
            //调用service完成删除
            final int itn = clueRemarkService.deleteClueRemarkById(id);
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
     * 根据id修改
     * @param clueRemark
     * @param session
     * @return
     */
    @RequestMapping("/workbench/clue/savaEditClueRemark.do")
    @ResponseBody
    public Object savaEditClueRemark(ClueRemark clueRemark,HttpSession session){
        User user = (User)session.getAttribute(Constants.SESSION_USER);
        //封装参数
        clueRemark.setEditFlag(Constants.Clue_REMARK_EDIT_FLAG_YES);
        clueRemark.setEditBy(user.getId());
        clueRemark.setEditTime(DateUtils.formateDateTime(new Date()));

        ReturnObject obj = new ReturnObject();
        try {
            //调用service完成修改
            final int itn = clueRemarkService.updateClueRemarkById(clueRemark);
            if (itn>0){
                obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                obj.setRetData(clueRemark);
            }else{
                obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                obj.setMessage("系统繁忙，请稍后重试");
            }
        }catch(Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("系统繁忙，请稍后重试");
        }
        return obj;
    }

    /**
     * 线索转换
     * @return
     */
    @RequestMapping("/workbench/clue/clueConvert.do")
    public String clueConvert(String id,HttpServletRequest request){
        Clue clue = clueService.selectById(id);
        final List<DicValue> dicValueList = dicValueService.queryDicValueByTypeCode("stage");
        request.setAttribute("clue",clue);
        request.setAttribute("dicValueList",dicValueList);
        return "workbench/clue/convert";
    }

    /**
     * 查询市场活动，根据市场活动名称 and clueId
     * @param activityName
     * @param clueId
     * @return
     */
    @RequestMapping("/workbench/clue/queryActivityByActivityNameAndClueId.do")
    @ResponseBody
    public Object queryActivityByActivityNameAndClueId(String activityName,String clueId){
        //封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        //调用service完成查询
        List<Activity> activityList=activityService.selectActivityByActivityNameAndClueId(map);
        //响应
        return activityList;
    }

    /**
     * 添加市场活动id,线索id,并且添加成功后还要根据这些id查询市场活动
     * @param activityId
     * @param clueId
     * @return
     */
    @RequestMapping("/workbench/clue/insertActivityClueRelation.do")
    @ResponseBody
    public Object insertActivityClueRelation(String[] activityId,String clueId){
        //遍历，收集市场活动的id
        ClueActivityRelation car=null;
        List<ClueActivityRelation> relationsList = new ArrayList<>();
        for (String al:activityId){
            car = new ClueActivityRelation();
            car.setId(UUIDUtils.getUUID());
            car.setClueId(clueId);
            car.setActivityId(al);
            relationsList.add(car);
        }
        ReturnObject obj = new ReturnObject();
        try {
            //调用service完成添加
            final int itn = clueActivityRelationService.insertClueActivityRelationList(relationsList);
            if(itn>0){
                obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                //查询市场活动
                final List<Activity> activityList = activityService.selectActivityDetailedByIds(activityId);
                obj.setRetData(activityList);
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
     * 根据市场活动id和线索id删除市场活动
     * @param relation
     * @return
     */
    @RequestMapping("/workbench/clue/removeClueActivityByAndClueIdAndActivityId.do")
    @ResponseBody
    public Object removeClueActivityByAndClueIdAndActivityId(ClueActivityRelation relation){
        ReturnObject obj = new ReturnObject();
        try {
            //调用service完成删除
            final int itn = clueActivityRelationService.deleteClueActivityByClueAndActivityId(relation);
            if(itn>0){
                obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                obj.setMessage("系统繁忙，请稍后重试");
            }
        }catch(Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("系统繁忙，请稍后重试");
        }
        return obj;
    }

    /**
     * 根据市场活动名称跟线索id查询市场活动对象动
     * @param activityName
     * @param clueId
     * @return
     */
    @RequestMapping("/workbench/clue/selectActivityByActivityNameAndClueId.do")
    @ResponseBody
    public Object selectActivityByActivityNameAndClueId(String activityName,String clueId){
        //封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        //调用activityService完成查询
        final List<Activity> activityList = activityService.selectActivityByActivityByClueId(map);
        //响应
        return activityList;
    }
}
