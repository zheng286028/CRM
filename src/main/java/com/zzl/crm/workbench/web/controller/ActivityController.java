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
 * ��������
 *
 * @author ֣����
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
     * ��ת�г�������û����ݲ�ѯ����
     * @return
     */
    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        //��ѯ����
        final List<User> users = service.selectAdd();
        //���鵽�����ݴ洢��request����
        request.setAttribute("users",users);
        //��ת
        return "workbench/activity/index";
    }

    /**
     * ������ӵ��г��
     * @param activity
     * @return
     */
    @RequestMapping("/workbench/activity/saveActivity.do")
    @ResponseBody
    public Object saveActivity(Activity activity, HttpSession session){
        final User user = (User) session.getAttribute(Constants.SESSION_USER);
        //��װ����
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formateDateTime(new Date()));
        activity.setCreateBy(user.getId());
        //���ô�����Ϣ����
        ReturnObject returnObject = new ReturnObject();
       try {
           //����service��
           final int insert = activityService.insertActivity(activity);
           //�ж��Ƿ���ӳɹ�
           if(insert>0){
               returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
           }else{
               returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
               returnObject.setMessage("ϵͳ��æ,���Ժ�����");
           }
       }catch (Exception e){
           e.printStackTrace();

           returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
           returnObject.setMessage("ϵͳ��æ,���Ժ�����");
       }
        return returnObject;
    }

    /**
     * �г����ҳ������ѯ
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
        //��װ����
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("begin",(currentPage-1)*pageSize);
        map.put("pageSize",pageSize);
        //����service��ѯ����
        List<Activity> activityList = activityService.queryActivityByPageAndCondition(map);
        int totalCount = activityService.queryActivityByConditionAndTotal(map);
        //���鵽�����ݷ�װ
        Map<String,Object>reqMap = new HashMap<>();
        reqMap.put("activityList",activityList);
        reqMap.put("totalCount",totalCount);
        //��Ӧ
        return reqMap;
    }

    /**
     * ����id����ɾ��
     * @param id
     * @return
     */
    @RequestMapping("/workbench/activity/deleteActivityById.do")
    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRED)
    public Object deleteActivityById(String[] id){
        //������Ϣ����
        ReturnObject obj = new ReturnObject();
       try {
           //����activityServiceɾ��
           final int byIds = activityService.deleteActivityByIds(id);
           //�ж��Ƿ�ɾ���ɹ�
           if(byIds>0){
               obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
           }else{
               //ʧ�ܸ�����Ϣ
               obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
               obj.setMessage("��������æ�����Ժ�����");
           }
       }catch (Exception e){
           //�쳣��Ϣ
           e.printStackTrace();
           obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
           obj.setMessage("��������æ�����Ժ�����");
       }
       return obj;
    }

    /**
     * ����ia��ѯ
     * @param id
     * @return
     */
    @RequestMapping("/workbench/activity/selectActivityById.do")
    @ResponseBody
    public Object selectActivityById(String id){
        //����service��ѯ
        final Activity activity = activityService.selectActivityById(id);
        //��Ӧ
        return activity;
    }

    /**
     * ����id�޸��г��
     * @param activity
     * @param session
     * @return
     */
    @RequestMapping("/workbench/activity/updateActivityById.do")
    @ResponseBody
    public Object updateActivityById(Activity activity,HttpSession session){
        //���޸�ʱ����޸���д��Activity����
        activity.setEditTime(DateUtils.formateDateTime(new Date()));
        //��ȡ��ǰ�û�
        final User attribute = (User) session.getAttribute(Constants.SESSION_USER);
        activity.setEditBy(attribute.getId());
        //����service�޸�
        final int byId = activityService.updateActivityById(activity);
        ReturnObject obj = new ReturnObject();
       try {
           //�ж��Ƿ��޸ĳɹ�
           if(byId>0){
               //�޸ĳɹ�
               obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
           }else{
               //�޸�ʧ��
               obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
               obj.setMessage("��������æ�����Ժ�����");
           }
       }catch (Exception e){
           e.printStackTrace();
           obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
           obj.setMessage("��������æ�����Ժ�����");
       }
       return obj;
    }
    /**
     * ����ѯ���������г���������ص������
     * @param response
     * @throws Exception
     */
    @RequestMapping("/workbench/activity/selectAllActivityS.do")
    public void selectAllActivityS(HttpServletResponse response) throws Exception {
        //1.����service��ѯ����
        final List<Activity> activityList = activityService.selectAllActivityS();
        //2.���鵽������д��excel�ļ���
        //3.ͨ��poi�����ļ�
        HSSFWorkbook wb = new HSSFWorkbook();
        //3.1.ҳ
        HSSFSheet sheet=wb.createSheet("�г����");
        //3.2.��
        HSSFRow row = sheet.createRow(0);
        //3.3.��
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell=row.createCell(1);
        cell.setCellValue("������");
        cell=row.createCell(2);
        cell.setCellValue("����");
        cell=row.createCell(3);
        cell.setCellValue("��ʼ����");
        cell=row.createCell(4);
        cell.setCellValue("��������");
        cell=row.createCell(5);
        cell.setCellValue("�ɱ�");
        cell=row.createCell(6);
        cell.setCellValue("����");
        cell=row.createCell(7);
        cell.setCellValue("����ʱ��");
        cell=row.createCell(8);
        cell.setCellValue("������");
        cell=row.createCell(9);
        cell.setCellValue("�޸�ʱ��");
        cell=row.createCell(10);
        cell.setCellValue("�޸���");
        //�ж�activityList�Ƿ���ֵ
        if(activityList!=null && activityList.size()>0){
            //����activityList������HSSFRow����������������
            Activity activity=null;//����һ��ֵ�Ḳ�ǵ��Ѿ�д��Cell��ֵ
            for (int i = 0; i<activityList.size() ; i++) {
                activity = activityList.get(i);//�ѱ���������Activityʵ���ำ��Activity
                //ÿ������һ��activity���󴴽�һ��
                row = sheet.createRow(i + 1);
                //ÿ����ʮһ��
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
        //������excel�ļ����ص������
        //1.������Ӧ��Ϣ
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=myActivity.xls");
        //2.��ȡ��Ӧ�����
        OutputStream out = response.getOutputStream();
        //���洢��Workbook�������д��out�������
        wb.write(out);
        //�ر���Դ
        wb.close();
    }

    /**
     * ����id��ѯ���ݣ�ѡ�񵼳��г��
     */
    @RequestMapping("/workbench/activity/queryActivityByIds.do")
    public void queryActivityByIds(String id,HttpServletResponse response) throws IOException {
        //1.��ѯ����
        Activity activity = activityService.selectActivityByIds(id);
        //2.����HSSFWorkbook
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("�г���б�");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("������");
        cell = row.createCell(2);
        cell.setCellValue("����");
        cell = row.createCell(3);
        cell.setCellValue("��ʼ����");
        cell = row.createCell(4);
        cell.setCellValue("��������");
        cell = row.createCell(5);
        cell.setCellValue("�ɱ�");
        cell = row.createCell(6);
        cell.setCellValue("����");
        cell = row.createCell(7);
        cell.setCellValue("����ʱ��");
        cell = row.createCell(8);
        cell.setCellValue("������");
        cell = row.createCell(9);
        cell.setCellValue("�޸�ʱ��");
        cell = row.createCell(10);
        cell.setCellValue("�޸���");
        //�����ڶ��д洢��������г��
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
        //���������ص������
        //1.ָ����Ӧ��ʽ
        response.setContentType("application/octet-stream;charset=UTF-8");
        //2.�޸���Ӧͷ
        response.addHeader("Content-Disposition","attachment;filename=Activity.xls");
        //3.�����ļ������
        OutputStream out = response.getOutputStream();
        //4.������д���������
        wb.write(out);
        //5.�ͷ���Դ
        wb.close();
    }

    /**
     * �����г��
     * @param activityFile
     */
    @RequestMapping("/workbench/activity/fileUploadActivity.do")
    @ResponseBody
    public Object fileUploadActivity(MultipartFile activityFile,HttpSession session){
        final User user = (User)session.getAttribute(Constants.SESSION_USER);
        ReturnObject object = new ReturnObject();
        try {
            //�����������ļ������ڴ��е�������
            InputStream in = activityFile.getInputStream();
            HSSFWorkbook wb = new HSSFWorkbook(in);
            //ҳ
            HSSFSheet sheet = wb.getSheetAt(0);
            //��
            HSSFRow row=null;
            HSSFCell cell = null;
            Activity activity=null;
            List<Activity> list = new ArrayList<>();
            for (int i = 1; i <=sheet.getLastRowNum() ; i++) {
                //��Щ����ֻ���ֶ�����
                activity=new Activity();
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
                activity.setStartDate(DateUtils.formateDate(new Date()));
                activity.setCreateTime(DateUtils.formateDateTime(new Date()));
                activity.setCreateBy(user.getId());
                //����һ��
                row=sheet.getRow(i);
                //��
                for (int j = 0; j <row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    //��ȡֵ
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
                //�������һ�ν����ݷ�װ��list����
                list.add(activity);
            }
            //����service������
            final int index = activityService.insertActivityList(list);
            //�����ɹ���Ϣ
            object.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            object.setRetData("�ɹ�����"+index+"������");
        } catch (IOException e) {
            e.printStackTrace();
            object.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            object.setMessage("ϵͳ��æ�����Ժ�����");
        }
        return object;
    }

    /**
     * ��ת���г����ע��
     * @return
     */
    @RequestMapping("/workbench/activity/detailActivity.do")
    public String detailActivity(String id,HttpServletRequest request){
        //��ѯ�г������
        final Activity activity = activityService.selectActivityAndDetailById(id);
        //��ѯ�г����ע
        final List<activityRemark> remarkList = activityRemark.selectActivityRemarkById(id);
        //�洢��request����
        request.setAttribute("activity",activity);
        request.setAttribute("remarkList",remarkList);
        //����ת��
        return "workbench/activity/detail";
    }
}
