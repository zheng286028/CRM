package com.zzl.crm.settings.web.Interceptor;

import com.zzl.crm.commons.Constants.Constants;
import com.zzl.crm.settings.pojo.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 功能描述
 *  拦截尚未登录的用户
 * @author 郑子浪
 * @date 2022/04/27  13:34
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取session
        HttpSession session = request.getSession();
        User user =(User) session.getAttribute(Constants.SESSION_USER);
        //判断该用户是否登录
        if(user==null){
            //尚未登录，跳转到登录方法
            session.setAttribute("msg","你尚未登录！");
            response.sendRedirect(request.getContextPath());//因为有个Controller方法的路径就是”/“ 只要访问该项目自动访问该方法
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
