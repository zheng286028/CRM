package com.zzl.crm.workbench.web.controller;

import com.zzl.crm.commons.Constants.Constants;
import com.zzl.crm.commons.pojo.ReturnObject;
import com.zzl.crm.commons.utils.DateUtils;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
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
     * 将查询到的所有市场活动数据下载到浏览器
     * @param response
     * @throws Exception
     */
    @RequestMapping("/workbench/activity/selectAllActivityS.do")
    public void selectAllActivityS(HttpServletResponse response) throws Exception {
        //1.调用service查询数据
        final List<Activity> activityList = activityService.selectAllActivityS();
        //2.将查到的数据写道excel文件里
        //3.通过poi创建文件
        HSSFWorkbook wb = new HSSFWorkbook();
        //3.1.页
        HSSFSheet sheet=wb.createSheet("市场活动列");
        //3.2.行
        HSSFRow row = sheet.createRow(0);
        //3.3.列
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell=row.createCell(1);
        cell.setCellValue("所有者");
        cell=row.createCell(2);
        cell.setCellValue("名称");
        cell=row.createCell(3);
        cell.setCellValue("开始日期");
        cell=row.createCell(4);
        cell.setCellValue("结束日期");
        cell=row.createCell(5);
        cell.setCellValue("成本");
        cell=row.createCell(6);
        cell.setCellValue("描述");
        cell=row.createCell(7);
        cell.setCellValue("创建时间");
        cell=row.createCell(8);
        cell.setCellValue("创建者");
        cell=row.createCell(9);
        cell.setCellValue("修改时间");
        cell=row.createCell(10);
        cell.setCellValue("修改人");
        //判断activityList是否有值
        if(activityList!=null && activityList.size()>0){
            //遍历activityList，创建HSSFRow对象，生成所有数据
            Activity activity=null;//遍历一次值会覆盖掉已经写入Cell的值
            for (int i = 0; i<activityList.size() ; i++) {
                activity = activityList.get(i);//把遍历出来的Activity实体类赋给Activity
                //每遍历出一个activity对象创建一行
                row = sheet.createRow(i + 1);
                //每行有十一列
                cell = row.createCell(0);
                cell.setCellValue(activity.getId());
                cell=row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell=row.createCell(2);
                cell.setCellValue(activity.getName());
                cell=row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell=row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell=row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell=row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell=row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell=row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell=row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell=row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
        }
        //把生成excel文件下载到浏览器
        //1.设置响应信息
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=myActivity.xls");
        //2.获取响应输出流
        OutputStream out = response.getOutputStream();
        //将存储在Workbook里的数据写到out这个流里
        wb.write(out);
        //关闭资源
        wb.close();
    }

    /**
     * 根据id查询数据，选择导出市场活动
     */
    @RequestMapping("/workbench/activity/queryActivityByIds.do")
    public void queryActivityByIds(String id,HttpServletResponse response) throws IOException {
        //1.查询数据
        Activity activity = activityService.selectActivityByIds(id);
        //2.创建HSSFWorkbook
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("市场活动列表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("创建时间");
        cell = row.createCell(8);
        cell.setCellValue("创建人");
        cell = row.createCell(9);
        cell.setCellValue("修改时间");
        cell = row.createCell(10);
        cell.setCellValue("修改人");
        //创建第二行存储查出来的市场活动
        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue(activity.getId());
        cell=row.createCell(1);
        cell.setCellValue(activity.getOwner());
        cell=row.createCell(2);
        cell.setCellValue(activity.getName());
        cell=row.createCell(3);
        cell.setCellValue(activity.getStartDate());
        cell=row.createCell(4);
        cell.setCellValue(activity.getEndDate());
        cell=row.createCell(5);
        cell.setCellValue(activity.getCost());
        cell=row.createCell(6);
        cell.setCellValue(activity.getDescription());
        cell=row.createCell(7);
        cell.setCellValue(activity.getCreateTime());
        cell=row.createCell(8);
        cell.setCellValue(activity.getCreateBy());
        cell=row.createCell(9);
        cell.setCellValue(activity.getEditTime());
        cell=row.createCell(10);
        cell.setCellValue(activity.getEditBy());
        //把数据下载到浏览器
        //1.指定响应格式
        response.setContentType("application/octet-stream;charset=UTF-8");
        //2.修改响应头
        response.addHeader("Content-Disposition","attachment;filename=Activity.xls");
        //3.创建文件输出流
        OutputStream out = response.getOutputStream();
        //4.将数据写到输出流里
        wb.write(out);
        //5.释放资源
        wb.close();
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
