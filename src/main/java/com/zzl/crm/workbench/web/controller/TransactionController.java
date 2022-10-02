package com.zzl.crm.workbench.web.controller;

import com.alibaba.druid.mock.MockPreparedStatement;
import com.zzl.crm.commons.Constants.Constants;
import com.zzl.crm.commons.pojo.ReturnObject;
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
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * ��������
 *
 * @author ֣����
 * @date 2022/05/12  16:28
 */
@Controller
public class TransactionController {
    @Autowired
    private UserService userService;
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private TranService tranService;
    @Autowired
    private TranRemarkService tranRemarkService;
    @Autowired
    private TranHistoryService tranHistoryService;

    /**
     * ������ҳ��
     * @param request
     * @return
     */
    @RequestMapping("/workbench/transaction/forwardTransactionIndex.do")
    public String forwardTransactionIndex(HttpServletRequest request){
        //��ѯ��̬�����б�
        final List<DicValue> stages = dicValueService.queryDicValueByTypeCode("stage");
        final List<DicValue> sources = dicValueService.queryDicValueByTypeCode("source");
        final List<DicValue> transactionTypes = dicValueService.queryDicValueByTypeCode("transactionType");
        //�洢��request����
        request.setAttribute("stages",stages);
        request.setAttribute("sources",sources);
        request.setAttribute("transactionTypes",transactionTypes);
        return "workbench/transaction/index";
    }

    /**
     * ��ת��������ҳ��
     * @return
     */
    @RequestMapping("/workbench/transaction/transactionSave.do")
    public String transactionSave(HttpServletRequest request){
        //��ѯ���������б�
        final List<User> users = userService.selectAdd();
        final List<DicValue> stages = dicValueService.queryDicValueByTypeCode("stage");
        final List<DicValue> sources = dicValueService.queryDicValueByTypeCode("source");
        final List<DicValue> transactionTypes = dicValueService.queryDicValueByTypeCode("transactionType");
        //�洢��request����
        request.setAttribute("users",users);
        request.setAttribute("stages",stages);
        request.setAttribute("sources",sources);
        request.setAttribute("transactionTypes",transactionTypes);
        return "workbench/transaction/save";
    }

    /**
     * ��ȡ�׶εĿ�����
     * @param stageValue
     * @return
     */
    @RequestMapping("/workbench/transaction/getPossibilityValue.do")
    @ResponseBody
    public Object getPossibilityValue(String stageValue){
        //����possibility.properties�ļ�
        final ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility=bundle.getString(stageValue);
        return possibility;
    }

    /**
     * ����nameģ����ѯ�г��
     * @param name
     * @return
     */
    @RequestMapping("/workbench/transaction/selectActivityNames.do")
    @ResponseBody
    public Object selectActivityByNames(String name){
        //����ActivityService
        final List<Activity> activityList = activityService.selectActivityByName(name);
        return activityList;
    }
    /**
     * ����nameģ����ѯ��ϵ��
     * @param name
     * @return
     */
    @RequestMapping("/workbench/transaction/selectContactsByNames.do")
    @ResponseBody
    public Object selectContactsByNames(String name){

        //����ActivityService
        final List<Contacts> contacts = contactsService.selectContactsByName(name);
        return contacts;
    }

    /**
     * ��������ģ����ѯ�ͻ�
     * @return
     */
    @RequestMapping("/workbench/transaction/selectAllCustomerName.do")
    @ResponseBody
    public Object selectAllCustomerName(String name){
        //����CustomerService��ѯ
        final List<String> customers = customerService.selectAllCustomerName(name);
        return customers;
    }

    /**
     * ��������
     * @param map
     * @param session
     * @return
     */
    @RequestMapping("/workbench/transaction/saveCreateTransaction.do")
    @ResponseBody
    public Object saveCreateTransaction(@RequestParam Map<String,Object>map, HttpSession session){
        map.put(Constants.SESSION_USER,session.getAttribute(Constants.SESSION_USER));

        ReturnObject obj = new ReturnObject();
        try{
            //���
            tranService.enterTransaction(map);
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
     * @param map
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping("/workbench/tran/queryTransactionByPageAndCondition.do")
    @ResponseBody
    public Object queryTransactionByPageAndCondition(@RequestParam Map<String,Object>map,int currentPage,int pageSize){
        //��װ����
        map.put("begin",(currentPage-1)*pageSize);
        map.put("pageSize",pageSize);
        //��ѯ
        List<Tran> trans = tranService.inquireTransactionByPageAndCondition(map);
        int totalCount = tranService.inquireTransactionTotalCountByCondition(map);
        //��Ӧ
        Map<String,Object>map2 = new HashMap<>();
        map2.put("trans",trans);
        map2.put("totalCount",totalCount);
        return map2;
    }

    /**
     * ����id��ѯ���ݣ�����ת���޸�ҳ��
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/workbench/tran/queryTransactionById.do")
    public String queryTransactionById(String id,HttpServletRequest request){
        //��ѯ���������б�
        final List<User> users = userService.selectAdd();
        final List<DicValue> stages = dicValueService.queryDicValueByTypeCode("stage");
        final List<DicValue> sources = dicValueService.queryDicValueByTypeCode("source");
        final List<DicValue> transactionTypes = dicValueService.queryDicValueByTypeCode("transactionType");
        //��ѯ��ǰ����
        final Tran tran = tranService.inquireTransactionById(id);
        //�洢��request����
        request.setAttribute("users",users);
        request.setAttribute("stages",stages);
        request.setAttribute("sources",sources);
        request.setAttribute("transactionTypes",transactionTypes);
        request.setAttribute("tran",tran);
        //��ת
        return "workbench/transaction/edit";
    }

    /**
     * ����id�޸�
     * @param map
     * @param session
     * @return
     */
    @RequestMapping("/workbench/transaction/reviseTransactionById.do")
    @ResponseBody
    public Object reviseTransactionById(@RequestParam Map<String,Object>map,HttpSession session){
        //��װ����
        map.put(Constants.SESSION_USER,session.getAttribute(Constants.SESSION_USER));

        ReturnObject obj = new ReturnObject();
        try{
           tranService.reviseTransactionById(map);
            obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("ϵͳ��æ�����Ժ�����");
        }
        return obj;
    }

    /**
     * ��ע
     * @param transactionId
     * @param request
     * @return
     */
    @RequestMapping("/workbench.transaction/TransactionRemarkByTransactionId.do")
    public String TransactionRemarkByTransactionId(String transactionId,HttpServletRequest request){
        //��ѯ���׵���ϸ��ϸ��Ϣ������ע
        final Tran tran = tranService.inquireTransactionById(transactionId);
        //������
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String bun=bundle.getString(tran.getStage());
        List<TranRemark> tranRemarkList = tranRemarkService.inquireTransactionRemarkByTransactionId(transactionId);
        List<TranHistory> tranHistories = tranHistoryService.inquireTranHistoryByTranId(transactionId);
        List<DicValue> stages = dicValueService.queryDicValueByTypeCode("stage");
        request.setAttribute("tran",tran);
        request.setAttribute("tranRemarkList",tranRemarkList);
        request.setAttribute("bun",bun);
        request.setAttribute("tranHistories",tranHistories);
        request.setAttribute("stages",stages);
        return "workbench/transaction/detail";
    }
}
