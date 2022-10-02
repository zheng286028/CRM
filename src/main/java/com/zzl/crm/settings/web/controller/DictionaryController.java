package com.zzl.crm.settings.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/05/07  20:10
 */
@Controller
public class DictionaryController {
    /**
     * 数据字典表
     * @return
     */
    @RequestMapping("/settings/dictionary/settingsDictionaryIndex.do")
    public String settingsDictionaryIndex(){
        return "settings/dictionary/index";
    }
    /**
     * 数据字典表类型
     * @return
     */
    @RequestMapping("/settings/dictionary/settingsDictionaryTypeIndex.do")
    public String settingsDictionaryTypeIndex(){
        return "settings/dictionary/type/index";
    }
    /**
     * 数据字典表值
     * @return
     */
    @RequestMapping("/settings/dictionary/settingsDictionaryValueIndex.do")
    public String settingsDictionaryValueIndex(){
        return "settings/dictionary/value/index";
    }

    /**
     * 修改字典表类型
     * @return
     */
    @RequestMapping("/settings/dictionary/settingsDictionaryTypeEdit.do")
    public String settingsDictionaryTypeEdit(){
        return "settings/dictionary/type/edit";
    }
    /**
     * 修改字典表数据
     * @return
     */
    @RequestMapping("/settings/dictionary/settingsDictionaryTypeSava.do")
    public String settingsDictionaryTypeSava(){
        return "settings/dictionary/type/save";
    }
    /**
     * 保存字典表类型
     * @return
     */
    @RequestMapping("/settings/dictionary/settingsDictionaryValueEdit.do")
    public String settingsDictionaryValueEdit(){
        return "settings/dictionary/value/edit";
    }
    /**
     * 保存字典表数据
     * @return
     */
    @RequestMapping("/settings/dictionary/settingsDictionaryValueSava.do")
    public String settingsDictionaryValueSava(){
        return "settings/dictionary/value/save";
    }
}
