package com.zzl.crm.settings.service.Impl;

import com.zzl.crm.settings.mapper.UserMapper;
import com.zzl.crm.settings.pojo.User;
import com.zzl.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/04/26  13:11
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper mapper;

    /**
     * 登录查询
     * @param map
     * @return
     */
    @Override
    public User queryUserByLoginActAndPwd(Map<String, Object> map) {
        return mapper.selectUserByLoginActAndPwd(map);
    }

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<User> selectAdd() {
        return mapper.selectAdd();
    }

    @Override
    public int insertSelective(User user) {
        return mapper.insertSelective(user);
    }
}
