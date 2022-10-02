package com.zzl.crm.workbench.service.Impl;

import com.zzl.crm.workbench.mapper.ActivityMapper;
import com.zzl.crm.workbench.pojo.Activity;
import com.zzl.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/04/27  23:54
 */
@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    ActivityMapper mapper;

    /**
     * 保存添加的市场活动
     * @param activity
     * @return
     */
    @Override
    public int insertActivity(Activity activity) {
        return mapper.insertActivity(activity);
    }

    /**
     * 分页条件查询
     * @param map
     * @return
     */
    @Override
    public List<Activity> queryActivityByPageAndCondition(Map<String, Object> map) {
        return mapper.selectActivityByPageAndCondition(map);
    }

    /**
     * 查询条件总记录数
     * @param map
     * @return
     */
    @Override
    public int queryActivityByConditionAndTotal(Map<String, Object> map) {
        return mapper.selectActivityByConditionTotal(map);
    }

    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    @Override
    public int deleteActivityByIds(String[] ids) {
        return mapper.deleteActivityByIds(ids);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public Activity selectActivityById(String id) {
        return mapper.selectActivityById(id);
    }

    /**
     * 根据id修改市场活动
     * @param activity
     * @return
     */
    @Override
    public int updateActivityById(Activity activity) {
        return mapper.updateActivityById(activity);
    }
    /**
     * 查询所有
     * @return
     */
    @Override
    public List<Activity> selectAllActivityS() {
        return mapper.selectAllActivityS();
    }
    /**
     * 根据id查询，选择导出
     * @param id
     * @return
     */
    @Override
    public Activity selectActivityByIds(String id) {
        return mapper.selectActivityByIds(id);
    }
    /**
     * 批量添加
     * @param activityList
     * @return
     */
    @Override
    public int insertActivityList(List<Activity> activityList) {
        return mapper.insertActivityList(activityList);
    }
    /**
     * 根据Id查询市场活动全部信息
     * @param id
     * @return
     */
    @Override
    public Activity selectActivityAndDetailById(String id) {
        return mapper.selectActivityAndDetailById(id);
    }
    /**
     * 根据clueId查询市场活动信息
     * @param clueId
     * @return
     */
    @Override
    public List<Activity> queryActivityByClueId(String clueId) {
        return mapper.queryActivityByClueId(clueId);
    }
    /**
     * 根据市场活动名称跟线索id查询市场活动对象
     * @return
     */
    @Override
    public List<Activity> selectActivityByActivityNameAndClueId(Map<String, Object> map) {
        return mapper.selectActivityByActivityNameAndClueId(map);
    }
    /**
     * 根据ids查询市场活动详细信息
     * @param ids
     * @return
     */
    @Override
    public List<Activity> selectActivityDetailedByIds(String[] ids) {
        return mapper.selectActivityDetailedByIds(ids);
    }
    /**
     * 根据市场活动名称跟线索id查询市场活动对象动
     * @return
     */
    @Override
    public List<Activity> selectActivityByActivityByClueId(Map<String, Object> map) {
        return mapper.selectActivityByActivityByClueId(map);
    }
    /**
     * 根据市场活动名称查询
     * @param name
     * @return
     */
    @Override
    public List<Activity> selectActivityByName(String name) {
        return mapper.selectActivityByName(name);
    }
    /**
     * 根据联系人id查询市场活动
     * @param contactsId
     * @return
     */
    @Override
    public List<Activity> selectActivityByContactsId(String contactsId) {
        return mapper.selectActivityByContactsId(contactsId);
    }
    /**
     * 根据市场活动名称跟联系人id查询市场活动对象动
     * @return
     */
    @Override
    public List<Activity> selectActivityByActivityByContactsId(Map<String, Object> map) {
        return mapper.selectActivityByActivityByContactsId(map);
    }
    /**
     * 根据contactsId查询被关联的市场活动
     * @param dis
     * @return
     */
    @Override
    public List<Activity> inquireContactsActivityRelationByContactsId(String[] dis) {
        return mapper.inquireContactsActivityRelationByContactsId(dis);
    }
}
