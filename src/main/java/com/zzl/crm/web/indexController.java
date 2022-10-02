package com.zzl.crm.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/04/25  21:41
 */
@Controller
public class indexController {
    /**
     * 访问首页
     * @return
     */
    @RequestMapping("/")
    public String index(){
        return "index";
    }

    /**
     * 访问系统设置
     * @return
     */
    @RequestMapping("/settings/settingsIndex.do")
    public String settingsIndex(){
        return "settings/index";
    }


}
