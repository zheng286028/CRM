package com.zzl.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/04/26  15:31
 */
@Controller
public class workbenchIndexController {

    /**
     * 登录成功跳转系统首页
     * @return
     */
    @RequestMapping("/workbench/index.do")
    public String loginSuccess(){
        return "workbench/index";
    }
    /**
     *  工作台首页
     * @return
     */
    @RequestMapping("/workbench/main/index.do")
    public String index(){
        return "workbench/main/index";
    }

}
