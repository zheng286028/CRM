package com.zzl.crm.settings.service;

import com.zzl.crm.settings.pojo.DicValue;

import java.util.List;

/**
 * ��������
 *
 * @author ֣����
 * @date 2022/05/07  20:57
 */
public interface DicValueService {
    /**
     * ����TypeCode��ѯ�����ֵ�ֵ
     * @param typeCode
     * @return
     */
    List<DicValue> queryDicValueByTypeCode(String typeCode);
}
