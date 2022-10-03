package com.zzl.crm.workbench.web.controller;

import com.zzl.crm.commons.Constants.Constants;
import com.zzl.crm.commons.pojo.ReturnObject;
import com.zzl.crm.commons.utils.DateUtils;
import com.zzl.crm.commons.utils.ExcelUtils;
import com.zzl.crm.commons.utils.HSSFUtils;
import com.zzl.crm.commons.utils.UUIDUtils;
import com.zzl.crm.settings.pojo.User;
import com.zzl.crm.settings.service.UserService;
import com.zzl.crm.workbench.pojo.Activity;
import com.zzl.crm.workbench.pojo.activityRemark;
import com.zzl.crm.workbench.service.ActivityService;
import com.zzl.crm.workbench.service.activityRemarkService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/04/27  21:23
 */
@Controller
public class ActivityController {
    @Autowired
    private UserService service;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private activityRemarkService activityRemark;
    /**
     * 跳转市场活动，把用户数据查询出来
     * @return
     */
    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        //查询数据
        final List<User> users = service.selectAdd();
        //将查到的数据存储到request域中
        request.setAttribute("users",users);
        //跳转
        return "workbench/activity/index";
    }

    /**
     * 保存添加的市场活动
     * @param activity
     * @return
     */
    @RequestMapping("/workbench/activity/saveActivity.do")
    @ResponseBody
    public Object saveActivity(Activity activity, HttpSession session){
        final User user = (User) session.getAttribute(Constants.SESSION_USER);
        //封装数据
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formateDateTime(new Date()));
        activity.setCreateBy(user.getId());
        //调用处理信息对象
        ReturnObject returnObject = new ReturnObject();
       try {
           //调用service层
           final int insert = activityService.insertActivity(activity);
           //判断是否添加成功
           if(insert>0){
               returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
           }else{
               returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
               returnObject.setMessage("系统繁忙,请稍后重试");
           }
       }catch (Exception e){
           e.printStackTrace();

           returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
           returnObject.setMessage("系统繁忙,请稍后重试");
       }
        return returnObject;
    }

    /**
     * 市场活动分页条件查询
     * @param name
     * @param owner
     * @param startDate
     * @param endDate
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping("/workbench/activity/queryActivityByPageAndCondition.do")
    @ResponseBody
    public Object queryActivityByPageAndCondition(String name,String owner,String startDate,
                                                  String endDate,int currentPage,int pageSize
                                                  ){
        //封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("begin",(currentPage-1)*pageSize);
        map.put("pageSize",pageSize);
        //调用service查询数据
        List<Activity> activityList = activityService.queryActivityByPageAndCondition(map);
        int totalCount = activityService.queryActivityByConditionAndTotal(map);
        //将查到的数据封装
        Map<String,Object>reqMap = new HashMap<>();
        reqMap.put("activityList",activityList);
        reqMap.put("totalCount",totalCount);
        //响应
        return reqMap;
    }

    /**
     * 根据id批量删除
     * @param id
     * @return
     */
    @RequestMapping("/workbench/activity/deleteActivityById.do")
    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRED)
    public Object deleteActivityById(String[] id){
        //创建信息对象
        ReturnObject obj = new ReturnObject();
       try {
           //调用activityService删除
           final int byIds = activityService.deleteActivityByIds(id);
           //判断是否删除成功
           if(byIds>0){
               obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
           }else{
               //失败给出信息
               obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
               obj.setMessage("服务器繁忙，请稍后重试");
           }
       }catch (Exception e){
           //异常信息
           e.printStackTrace();
           obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
           obj.setMessage("服务器繁忙，请稍后重试");
       }
       return obj;
    }

    /**
     * 根据ia查询
     * @param id
     * @return
     */
    @RequestMapping("/workbench/activity/selectActivityById.do")
    @ResponseBody
    public Object selectActivityById(String id){
        //调用service查询
        final Activity activity = activityService.selectActivityById(id);
        //响应
        return activity;
    }

    /**
     * 根据id修改市场活动
     * @param activity
     * @param session
     * @return
     */
    @RequestMapping("/workbench/activity/updateActivityById.do")
    @ResponseBody
    public Object updateActivityById(Activity activity,HttpSession session){
        //把修改时间和修改人写入Activity类中
        activity.setEditTime(DateUtils.formateDateTime(new Date()));
        //获取当前用户
        final User attribute = (User) session.getAttribute(Constants.SESSION_USER);
        activity.setEditBy(attribute.getId());
        //调用service修改
        final int byId = activityService.updateActivityById(activity);
        ReturnObject obj = new ReturnObject();
       try {
           //判断是否修改成功
           if(byId>0){
               //修改成功
               obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
           }else{
               //修改失败
               obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
               obj.setMessage("服务器繁忙，请稍后重试");
           }
       }catch (Exception e){
           e.printStackTrace();
           obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
           obj.setMessage("服务器繁忙，请稍后重试");
       }
       return obj;
    }
    /**
     * 全部导出
     * @param response 响应流
     */
    @RequestMapping("/workbench/activity/selectAllActivityS.do")
    public void selectAllActivityS(HttpServletResponse response) {
        //1.调用service查询数据
        final List<Activity> activityList = activityService.selectAllActivityS();
        //2、设置响应体
        response.setContentType("application/binary;charset=UTF-8");
        try {
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode("导出"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +".xls", "UTF-8"));
            ServletOutputStream out = response.getOutputStream();
            //3、开始导出
            ExcelUtils.exportExcel(activityList, out);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 批量导出
     */
    @RequestMapping("/workbench/activity/queryActivityByIds.do")
    public void queryActivityByIds(String[] ids,HttpServletResponse response)  {
        //1.查询数据
        List<Activity> activityList = activityService.selectActivityByIds(ids);
        //2、响应体
        response.setContentType("application/binary;charset=utf-8");
        try {
            response.setHeader("content-Disposition","attachment;fileName=" + URLEncoder.encode("导出"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +".xls", "UTF-8"));
            ServletOutputStream stream = response.getOutputStream();
            //3、开始导出
            ExcelUtils.exportExcel(activityList,stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入市场活动
     * @param activityFile
     */
    @RequestMapping("/workbench/activity/fileUploadActivity.do")
    @ResponseBody
    public Object fileUploadActivity(MultipartFile activityFile,HttpSession session){
        final User user = (User)session.getAttribute(Constants.SESSION_USER);
        ReturnObject object = new ReturnObject();
        try {
            //将传过来的文件读到内存中的输入流
            InputStream in = activityFile.getInputStream();
            HSSFWorkbook wb = new HSSFWorkbook(in);
            //页
            HSSFSheet sheet = wb.getSheetAt(0);
            //行
            HSSFRow row=null;
            HSSFCell cell = null;
            Activity activity=null;
            List<Activity> list = new ArrayList<>();
            for (int i = 1; i <=sheet.getLastRowNum() ; i++) {
                //有些数据只能手动生成
                activity=new Activity();
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
                activity.setStartDate(DateUtils.formateDate(new Date()));
                activity.setCreateTime(DateUtils.formateDateTime(new Date()));
                activity.setCreateBy(user.getId());
                //创建一行
                row=sheet.getRow(i);
                //列
                for (int j = 0; j <row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    //获取值
                    final String str = HSSFUtils.getHSSFCellStr(cell);
                    if(j==0){
                       activity.setName(str);
                    }else if(j==1){
                        activity.setEndDate(str);
                    }else if (j==2){
                        activity.setCost(str);
                    }else if(j==3){
                        activity.setDescription(str);
                    }
                }
                //遍历完成一次将数据封装到list集合
                list.add(activity);
            }
            //调用service完成添加
            final int index = activityService.insertActivityList(list);
            //给出成功信息
            object.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            object.setRetData("成功导入"+index+"条数据");
        } catch (IOException e) {
            e.printStackTrace();
            object.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            object.setMessage("系统繁忙，请稍后重试");
        }
        return object;
    }

    /**
     * 跳转到市场活动备注表
     * @return
     */
    @RequestMapping("/workbench/activity/detailActivity.do")
    public String detailActivity(String id,HttpServletRequest request){
        //查询市场活动数据
        final Activity activity = activityService.selectActivityAndDetailById(id);
        //查询市场活动备注
        final List<activityRemark> remarkList = activityRemark.selectActivityRemarkById(id);
        //存储到request域中
        request.setAttribute("activity",activity);
        request.setAttribute("remarkList",remarkList);
        //请求转发
        return "workbench/activity/detail";
    }
}
