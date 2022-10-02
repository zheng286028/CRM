package com.zzl.crm.settings.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ��������
 *
 * @author ֣����
 * @date 2022/05/07  20:10
 */
@Controller
public class DictionaryController {
    /**
     * �����ֵ��
     * @return
     */
    @RequestMapping("/settings/dictionary/settingsDictionaryIndex.do")
    public String settingsDictionaryIndex(){
        return "settings/dictionary/index";
    }
    /**
     * �����ֵ������
     * @return
     */
    @RequestMapping("/settings/dictionary/settingsDictionaryTypeIndex.do")
    public String settingsDictionaryTypeIndex(){
        return "settings/dictionary/type/index";
    }
    /**
     * �����ֵ��ֵ
     * @return
     */
    @RequestMapping("/settings/dictionary/settingsDictionaryValueIndex.do")
    public String settingsDictionaryValueIndex(){
        return "settings/dictionary/value/index";
    }

    /**
     * �޸��ֵ������
     * @return
     */
    @RequestMapping("/settings/dictionary/settingsDictionaryTypeEdit.do")
    public String settingsDictionaryTypeEdit(){
        return "settings/dictionary/type/edit";
    }
    /**
     * �޸��ֵ������
     * @return
     */
    @RequestMapping("/settings/dictionary/settingsDictionaryTypeSava.do")
    public String settingsDictionaryTypeSava(){
        return "settings/dictionary/type/save";
    }
    /**
     * �����ֵ������
     * @return
     */
    @RequestMapping("/settings/dictionary/settingsDictionaryValueEdit.do")
    public String settingsDictionaryValueEdit(){
        return "settings/dictionary/value/edit";
    }
    /**
     * �����ֵ������
     * @return
     */
    @RequestMapping("/settings/dictionary/settingsDictionaryValueSava.do")
    public String settingsDictionaryValueSava(){
        return "settings/dictionary/value/save";
    }
}
