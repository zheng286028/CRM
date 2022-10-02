package com.zzl.crm.settings.service;

import com.zzl.crm.settings.pojo.DicValue;

import java.util.List;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/05/07  20:57
 */
public interface DicValueService {
    /**
     * 根据TypeCode查询数据字典值
     * @param typeCode
     * @return
     */
    List<DicValue> queryDicValueByTypeCode(String typeCode);
}
