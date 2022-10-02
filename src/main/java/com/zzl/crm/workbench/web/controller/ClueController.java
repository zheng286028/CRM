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
 * ��������
 *
 * @author ֣����
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
     * ������ҳ
     * @return
     */
    @RequestMapping("/workbench/clue/clueIndex.do")
    public String clueIndex(HttpServletRequest request){
        //��ѯ��������Ķ�̬����
        final List<User> user = userService.selectAdd();
        final List<DicValue> appellation = dicValueService.queryDicValueByTypeCode("appellation");
        final List<DicValue> clueState = dicValueService.queryDicValueByTypeCode("clueState");
        final List<DicValue> source = dicValueService.queryDicValueByTypeCode("source");
        //�����ݴ洢��request����
        request.setAttribute("user",user);
        request.setAttribute("appellation",appellation);
        request.setAttribute("clueState",clueState);
        request.setAttribute("source",source);
        return "workbench/clue/index";
    }

    /**
     * �������
     * @param clue
     * @return
     */
    @RequestMapping("/workbench/clue/insertClue.do")
    @ResponseBody
    public Object insertClue(Clue clue, HttpSession session){
      User user=(User) session.getAttribute(Constants.SESSION_USER);
        ReturnObject obj = new ReturnObject();
      //��װ����
      clue.setId(UUIDUtils.getUUID());
      clue.setCreateTime(DateUtils.formateDateTime(new Date()));
      clue.setCreateBy(user.getId());
      try {
          //����service���
          final int itn = clueService.insertClue(clue);
          //�ж��Ƿ�ɹ�
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
     * ��ҳ������ѯ
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
        //��װ����
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
        //����service��ѯ
        List<Clue> clueList = clueService.queryClueByPageAndCondition(map);
        int totalCount = clueService.queryClueByConditionTotal(map);
        //��Ӧ����
        Map<String,Object> mapRet = new HashMap<>();
        mapRet.put("clueList",clueList);
        mapRet.put("totalCount",totalCount);
        return mapRet;
    }
    /**
     * ����id����ɾ��
     * @param ids
     * @return
     */
    @RequestMapping("/workbench/clue/deleteClueByIds.do")
    @ResponseBody
    public Object deleteClueByIds(String[] ids){
        ReturnObject obj = new ReturnObject();
        try {
            //����service
            final int in = clueService.deleteClueByIds(ids);
            if(in>0){
                obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                obj.setMessage("ϵͳ��æ�����Ժ�����");
            }
        }catch(Exception e){
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
    @RequestMapping("/workbench/clue/selectById.do")
    @ResponseBody
    public Object selectById(String id){
        //����service��ѯ
        final Clue clue = clueService.selectById(id);
        //��Ӧ��Ϣ
        return clue;
    }

    /**
     * ����id�޸�
     * @param clue
     * @param session
     * @return
     */
    @RequestMapping("/workbench/clue/savaEditClueById.do")
    @ResponseBody
    public Object savaEditClueById(Clue clue,HttpSession session){
        User user = (User)session.getAttribute(Constants.SESSION_USER);
        //��װ����
        clue.setEditTime(DateUtils.formateDateTime(new Date()));
        clue.setEditBy(user.getId());

        ReturnObject obj = new ReturnObject();
        try {
            //����service����޸�
            final int itn = clueService.updateClueById(clue);
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
     * ����id��ѯ������������ע��ϸ��Ϣ,�͵�ǰ������ȫ���г��
     * @param id
     * @return
     */
    @RequestMapping("/workbench/clue/queryClueAndClueRemarkById.do")
    public String queryClueAndClueRemarkById(String id,HttpServletRequest request){
        //����service��ѯ
        final Clue clue = clueService.selectById(id);
        List<ClueRemark> clueRemarkList = clueRemarkService.selectByClueId(id);
        List<Activity> activityList=activityService.queryActivityByClueId(id);

        //���鵽����Ϣ�洢��request����
        request.setAttribute("clue",clue);
        request.setAttribute("clueRemarkList",clueRemarkList);
        request.setAttribute("activityList",activityList);
        //ת��
        return "workbench/clue/detail";
    }

    /**
     * ���������ע
     * @param clueRemark
     * @param session
     * @return
     */
    @RequestMapping("/workbench/clue/insertClueRemark.do")
    @ResponseBody
    public Object insertClueRemark(ClueRemark clueRemark,HttpSession session){
        //��װ����
        User user = (User)session.getAttribute(Constants.SESSION_USER);

        clueRemark.setId(UUIDUtils.getUUID());
        clueRemark.setCreateBy(user.getId());
        clueRemark.setCreateTime(DateUtils.formateDateTime(new Date()));
        clueRemark.setEditFlag(Constants.Clue_REMARK_EDIT_FLAG_NO);

        ReturnObject obj = new ReturnObject();
        try {
        //����service������
        final int itn = clueRemarkService.insertClueRemark(clueRemark);
        if(itn>0){
            obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            obj.setRetData(clueRemark);
        }else{
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("ϵͳ��æ�����Ժ�����");
        }
        }catch(Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("ϵͳ��æ�����Ժ�����");
        }
        return obj;
    }

    /**
     * ����idɾ��������ע
     * @param id
     * @return
     */
    @RequestMapping("/workbench/clue/deleteClueRemarkById.do")
    @ResponseBody
    public Object deleteClueRemarkById(String id){
        ReturnObject obj = new ReturnObject();
        try {
            //����service���ɾ��
            final int itn = clueRemarkService.deleteClueRemarkById(id);
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
     * ����id�޸�
     * @param clueRemark
     * @param session
     * @return
     */
    @RequestMapping("/workbench/clue/savaEditClueRemark.do")
    @ResponseBody
    public Object savaEditClueRemark(ClueRemark clueRemark,HttpSession session){
        User user = (User)session.getAttribute(Constants.SESSION_USER);
        //��װ����
        clueRemark.setEditFlag(Constants.Clue_REMARK_EDIT_FLAG_YES);
        clueRemark.setEditBy(user.getId());
        clueRemark.setEditTime(DateUtils.formateDateTime(new Date()));

        ReturnObject obj = new ReturnObject();
        try {
            //����service����޸�
            final int itn = clueRemarkService.updateClueRemarkById(clueRemark);
            if (itn>0){
                obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                obj.setRetData(clueRemark);
            }else{
                obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                obj.setMessage("ϵͳ��æ�����Ժ�����");
            }
        }catch(Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("ϵͳ��æ�����Ժ�����");
        }
        return obj;
    }

    /**
     * ����ת��
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
     * ��ѯ�г���������г������ and clueId
     * @param activityName
     * @param clueId
     * @return
     */
    @RequestMapping("/workbench/clue/queryActivityByActivityNameAndClueId.do")
    @ResponseBody
    public Object queryActivityByActivityNameAndClueId(String activityName,String clueId){
        //��װ����
        Map<String,Object> map = new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        //����service��ɲ�ѯ
        List<Activity> activityList=activityService.selectActivityByActivityNameAndClueId(map);
        //��Ӧ
        return activityList;
    }

    /**
     * ����г��id,����id,������ӳɹ���Ҫ������Щid��ѯ�г��
     * @param activityId
     * @param clueId
     * @return
     */
    @RequestMapping("/workbench/clue/insertActivityClueRelation.do")
    @ResponseBody
    public Object insertActivityClueRelation(String[] activityId,String clueId){
        //�������ռ��г����id
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
            //����service������
            final int itn = clueActivityRelationService.insertClueActivityRelationList(relationsList);
            if(itn>0){
                obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                //��ѯ�г��
                final List<Activity> activityList = activityService.selectActivityDetailedByIds(activityId);
                obj.setRetData(activityList);
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
     * �����г��id������idɾ���г��
     * @param relation
     * @return
     */
    @RequestMapping("/workbench/clue/removeClueActivityByAndClueIdAndActivityId.do")
    @ResponseBody
    public Object removeClueActivityByAndClueIdAndActivityId(ClueActivityRelation relation){
        ReturnObject obj = new ReturnObject();
        try {
            //����service���ɾ��
            final int itn = clueActivityRelationService.deleteClueActivityByClueAndActivityId(relation);
            if(itn>0){
                obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                obj.setMessage("ϵͳ��æ�����Ժ�����");
            }
        }catch(Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("ϵͳ��æ�����Ժ�����");
        }
        return obj;
    }

    /**
     * �����г�����Ƹ�����id��ѯ�г������
     * @param activityName
     * @param clueId
     * @return
     */
    @RequestMapping("/workbench/clue/selectActivityByActivityNameAndClueId.do")
    @ResponseBody
    public Object selectActivityByActivityNameAndClueId(String activityName,String clueId){
        //��װ����
        Map<String,Object> map = new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        //����activityService��ɲ�ѯ
        final List<Activity> activityList = activityService.selectActivityByActivityByClueId(map);
        //��Ӧ
        return activityList;
    }
}
