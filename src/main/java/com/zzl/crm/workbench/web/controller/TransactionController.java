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
 * 功能描述
 *
 * @author 郑子浪
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
     * 交易首页面
     * @param request
     * @return
     */
    @RequestMapping("/workbench/transaction/forwardTransactionIndex.do")
    public String forwardTransactionIndex(HttpServletRequest request){
        //查询动态下拉列表
        final List<DicValue> stages = dicValueService.queryDicValueByTypeCode("stage");
        final List<DicValue> sources = dicValueService.queryDicValueByTypeCode("source");
        final List<DicValue> transactionTypes = dicValueService.queryDicValueByTypeCode("transactionType");
        //存储到request域中
        request.setAttribute("stages",stages);
        request.setAttribute("sources",sources);
        request.setAttribute("transactionTypes",transactionTypes);
        return "workbench/transaction/index";
    }

    /**
     * 跳转创建交易页面
     * @return
     */
    @RequestMapping("/workbench/transaction/transactionSave.do")
    public String transactionSave(HttpServletRequest request){
        //查询所有下拉列表
        final List<User> users = userService.selectAdd();
        final List<DicValue> stages = dicValueService.queryDicValueByTypeCode("stage");
        final List<DicValue> sources = dicValueService.queryDicValueByTypeCode("source");
        final List<DicValue> transactionTypes = dicValueService.queryDicValueByTypeCode("transactionType");
        //存储到request域中
        request.setAttribute("users",users);
        request.setAttribute("stages",stages);
        request.setAttribute("sources",sources);
        request.setAttribute("transactionTypes",transactionTypes);
        return "workbench/transaction/save";
    }

    /**
     * 获取阶段的可能性
     * @param stageValue
     * @return
     */
    @RequestMapping("/workbench/transaction/getPossibilityValue.do")
    @ResponseBody
    public Object getPossibilityValue(String stageValue){
        //调用possibility.properties文件
        final ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility=bundle.getString(stageValue);
        return possibility;
    }

    /**
     * 根据name模糊查询市场活动
     * @param name
     * @return
     */
    @RequestMapping("/workbench/transaction/selectActivityNames.do")
    @ResponseBody
    public Object selectActivityByNames(String name){
        //调用ActivityService
        final List<Activity> activityList = activityService.selectActivityByName(name);
        return activityList;
    }
    /**
     * 根据name模糊查询联系人
     * @param name
     * @return
     */
    @RequestMapping("/workbench/transaction/selectContactsByNames.do")
    @ResponseBody
    public Object selectContactsByNames(String name){

        //调用ActivityService
        final List<Contacts> contacts = contactsService.selectContactsByName(name);
        return contacts;
    }

    /**
     * 根据名称模糊查询客户
     * @return
     */
    @RequestMapping("/workbench/transaction/selectAllCustomerName.do")
    @ResponseBody
    public Object selectAllCustomerName(String name){
        //调用CustomerService查询
        final List<String> customers = customerService.selectAllCustomerName(name);
        return customers;
    }

    /**
     * 创建交易
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
            //添加
            tranService.enterTransaction(map);
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
     * @param map
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping("/workbench/tran/queryTransactionByPageAndCondition.do")
    @ResponseBody
    public Object queryTransactionByPageAndCondition(@RequestParam Map<String,Object>map,int currentPage,int pageSize){
        //封装参数
        map.put("begin",(currentPage-1)*pageSize);
        map.put("pageSize",pageSize);
        //查询
        List<Tran> trans = tranService.inquireTransactionByPageAndCondition(map);
        int totalCount = tranService.inquireTransactionTotalCountByCondition(map);
        //响应
        Map<String,Object>map2 = new HashMap<>();
        map2.put("trans",trans);
        map2.put("totalCount",totalCount);
        return map2;
    }

    /**
     * 根据id查询数据，且跳转到修改页面
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/workbench/tran/queryTransactionById.do")
    public String queryTransactionById(String id,HttpServletRequest request){
        //查询所有下拉列表
        final List<User> users = userService.selectAdd();
        final List<DicValue> stages = dicValueService.queryDicValueByTypeCode("stage");
        final List<DicValue> sources = dicValueService.queryDicValueByTypeCode("source");
        final List<DicValue> transactionTypes = dicValueService.queryDicValueByTypeCode("transactionType");
        //查询当前交易
        final Tran tran = tranService.inquireTransactionById(id);
        //存储到request域中
        request.setAttribute("users",users);
        request.setAttribute("stages",stages);
        request.setAttribute("sources",sources);
        request.setAttribute("transactionTypes",transactionTypes);
        request.setAttribute("tran",tran);
        //跳转
        return "workbench/transaction/edit";
    }

    /**
     * 根据id修改
     * @param map
     * @param session
     * @return
     */
    @RequestMapping("/workbench/transaction/reviseTransactionById.do")
    @ResponseBody
    public Object reviseTransactionById(@RequestParam Map<String,Object>map,HttpSession session){
        //封装参数
        map.put(Constants.SESSION_USER,session.getAttribute(Constants.SESSION_USER));

        ReturnObject obj = new ReturnObject();
        try{
           tranService.reviseTransactionById(map);
            obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMessage("系统繁忙，请稍后重试");
        }
        return obj;
    }

    /**
     * 备注
     * @param transactionId
     * @param request
     * @return
     */
    @RequestMapping("/workbench.transaction/TransactionRemarkByTransactionId.do")
    public String TransactionRemarkByTransactionId(String transactionId,HttpServletRequest request){
        //查询交易的详细详细信息，及备注
        final Tran tran = tranService.inquireTransactionById(transactionId);
        //可能性
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
