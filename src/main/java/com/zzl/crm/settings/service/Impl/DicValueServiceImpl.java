package com.zzl.crm.settings.service.Impl;

import com.zzl.crm.settings.mapper.DicValueMapper;
import com.zzl.crm.settings.pojo.DicValue;
import com.zzl.crm.settings.service.DicValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ��������
 *
 * @author ֣����
 * @date 2022/05/07  20:57
 */
@Service("DicValueService")
public class DicValueServiceImpl implements DicValueService {
    @Autowired
    private DicValueMapper dicValueMapper;
    /**
     * ����TypeCode��ѯ�����ֵ�ֵ
     * @param typeCode
     * @return
     */
    @Override
    public List<DicValue> queryDicValueByTypeCode(String typeCode) {
        return dicValueMapper.queryDicValueByTypeCode(typeCode);
    }
}
