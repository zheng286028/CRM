package com.zzl.crm.settings.service;

import com.zzl.crm.settings.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/04/26  13:08
 */
public interface UserService {
    /**
     * 登录查询
     * @param map
     * @return
     */
    User queryUserByLoginActAndPwd(Map<String,Object> map);

    /**
     * 查询所有
     * @return
     */
    List<User> selectAdd();

    /**
     * 添加
     * @param user
     * @return
     */
    int insertSelective(User user);
}
