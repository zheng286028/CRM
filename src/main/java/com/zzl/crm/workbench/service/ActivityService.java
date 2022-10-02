package com.zzl.crm.workbench.service;

import com.zzl.crm.workbench.pojo.Activity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/04/27  23:36
 */
public interface ActivityService {
    /**
     * 添加
     * @param activity
     * @return
     */
    int insertActivity(Activity activity);

    /**
     * 分页条件查询
     * @param map
     * @return
     */
    List<Activity> queryActivityByPageAndCondition(Map<String,Object> map);

    /**
     * 查询条件总记录数
     * @param map
     * @return
     */
    int queryActivityByConditionAndTotal(Map<String,Object> map);

    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    int deleteActivityByIds(String[] ids);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Activity selectActivityById(String id);

    /**
     * 根据id修改市场活动
     * @param activity
     * @return
     */
    int updateActivityById(Activity activity);

    /**
     * 查询所有
     * @return
     */
    List<Activity> selectAllActivityS();

    /**
     * 根据id查询，选择导出
     * @param id
     * @return
     */
    Activity selectActivityByIds(String id);

    /**
     * 批量添加
     * @param activityList
     * @return
     */
    int insertActivityList(List<Activity> activityList);

    /**
     * 根据Id查询市场活动全部信息
     * @param id
     * @return
     */
    Activity selectActivityAndDetailById(String id);
    /**
     * 根据clueId查询市场活动信息
     * @param clueId
     * @return
     */
    List<Activity> queryActivityByClueId(String clueId);
    /**
     * 根据市场活动名称跟线索id查询市场活动对象
     * @return
     */
    List<Activity>selectActivityByActivityNameAndClueId(Map<String,Object> map);

    /**
     * 根据ids查询市场活动详细信息
     * @param ids
     * @return
     */
    List<Activity> selectActivityDetailedByIds(String[] ids);
    /**
     * 根据市场活动名称跟线索id查询市场活动对象动
     * @return
     */
    List<Activity>selectActivityByActivityByClueId(Map<String,Object> map);
    /**
     * 根据市场活动名称查询
     * @param name
     * @return
     */
    List<Activity>selectActivityByName(String name);
    /**
     * 根据联系人id查询市场活动
     * @param contactsId
     * @return
     */
    List<Activity>selectActivityByContactsId(String contactsId);
    /**
     * 根据市场活动名称跟联系人id查询市场活动对象动
     * @return
     */
    List<Activity>selectActivityByActivityByContactsId(Map<String,Object> map);
    /**
     * 根据contactsId查询被关联的市场活动
     * @param ids
     * @return
     */
    List<Activity> inquireContactsActivityRelationByContactsId(String[] ids);
}
