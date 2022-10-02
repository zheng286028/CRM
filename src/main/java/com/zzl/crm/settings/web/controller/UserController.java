package com.zzl.crm.settings.web.controller;

import com.zzl.crm.commons.Constants.Constants;
import com.zzl.crm.commons.pojo.ReturnObject;
import com.zzl.crm.commons.utils.DateUtils;
import com.zzl.crm.settings.pojo.User;
import com.zzl.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/04/25  22:03
 */
@Controller
public class UserController {
    @Autowired
    UserService service;

    /**
     * 登录页面
     * @return
     */
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
        return "settings/qx/user/login";
    }

    /**
     * 登录验证
     * @param loginAct
     * @param loginPwd
     * @param isRemPwd
     * @return
     */
    @RequestMapping("/settings/qx/user/Login.do")
    @ResponseBody
    public Object Login(String loginAct, String loginPwd, String isRemPwd,
                        HttpServletRequest request,
                        HttpServletResponse response,
                        HttpSession session
                        ){
        //封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        //调用service
        final User user = service.queryUserByLoginActAndPwd(map);
        //调用公共类封装提示信息
        ReturnObject returnObject = new ReturnObject();
        //判断是否正确
        if(user == null){
            //登录失败，账号或密码错误
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("账号或密码错误");
        }else{
            //进一步判断账号是否合法
            /*user.getExpireTime()获取到期时间 ，因为是字符串，无法和当前时间做比较
            * 所以只能将当前时间转成指定格式的字符串，然后在比较
            * */
            String nowStr = DateUtils.formateDateTime(new Date());
            if(nowStr.compareTo(user.getExpireTime())>0){
                //登录失败，账户以过期
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账户以过期");
            }else if("0".equals(user.getLockState())){
                //账户已被锁定
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账户已被锁定");
            }else if(!user.getAllowIps().contains(request.getRemoteAddr())){
                //登录失败，IP受限
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("IP受限");
            }else{
                //登录成功
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                //将将登录成功的用户存储到session域中
                session.setAttribute(Constants.SESSION_USER,user);
                //判断用户是否勾选记住我
                if("true".equals(isRemPwd)){
                    //勾选，创建cookie
                    final Cookie _loginAct = new Cookie("loginAct", loginAct);
                    final Cookie _loginPwd = new Cookie("loginPwd", loginPwd);
                    //设置cookie时间
                    _loginAct.setMaxAge(60*60*24*7);
                    _loginPwd.setMaxAge(60*60*24*7);
                    //响应cookie
                    response.addCookie(_loginAct);
                    response.addCookie(_loginPwd);
                }else{
                    //用户不勾选记住我，把没有过期的cookie删掉
                    final Cookie _loginAct = new Cookie("loginAct", loginAct);
                    final Cookie _loginPwd = new Cookie("loginPwd", loginPwd);
                    //设置cookie时间
                    _loginAct.setMaxAge(0);
                    _loginPwd.setMaxAge(0);
                    //响应cookie
                    response.addCookie(_loginAct);
                    response.addCookie(_loginPwd);
                }
            }
        }
        return returnObject;
    }

    /**
     * 退出登录
      * @return
     */
    @RequestMapping("/settings/qx/user/logUto.do")
    public String logUto(HttpServletResponse response,HttpSession session){
        //清空cookie
        final Cookie _loginAct = new Cookie("loginAct", "0");
        final Cookie _loginPwd = new Cookie("loginPwd", "0");
        //设置cookie时间
        _loginAct.setMaxAge(0);
        _loginPwd.setMaxAge(0);
        //响应cookie
        response.addCookie(_loginAct);
        response.addCookie(_loginPwd);
        //销毁session
        session.invalidate();
        //跳转首页
        return "redirect:/"; //重定向是需要写项目名称的，这里不需要只是因为springmvc底层帮忙重定向了
    }
}
