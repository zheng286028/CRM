package com.zzl.crm.workbench.service.Impl;

import com.zzl.crm.workbench.mapper.ClueMapper;
import com.zzl.crm.workbench.pojo.Clue;
import com.zzl.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * ��������
 *
 * @author ֣����
 * @date 2022/05/05  19:36
 */
@Service
public class ClueServiceImpl implements ClueService {
    @Autowired
    ClueMapper clueMapper;
    /**
     * �������
     * @param clue
     * @return
     */
    @Override
    public int insertClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }
    /**
     * ��ҳ������ѯ
     * @param map
     * @return
     */
    @Override
    public List<Clue> queryClueByPageAndCondition(Map<String, Object> map) {
        return clueMapper.queryClueByPageAndCondition(map);
    }
    /**
     * ������ѯ�ܼ�¼��
     * @param map
     * @return
     */
    @Override
    public int queryClueByConditionTotal(Map<String, Object> map) {
        return clueMapper.queryClueByConditionTotal(map);
    }
    /**
     * ����id����ɾ��
     * @param ids
     * @return
     */
    @Override
    public int deleteClueByIds(String[] ids) {
        return clueMapper.deleteClueByIds(ids);
    }
    /**
     * ����id��ѯ
     * @param id
     * @return
     */
    @Override
    public Clue selectById(String id) {
        return clueMapper.selectById(id);
    }
    /**
     * ����id�޸�
     * @param clue
     * @return
     */
    @Override
    public int updateClueById(Clue clue) {
        return clueMapper.updateClueById(clue);
    }
    /**
     * ����id��ѯ��������ϸ��Ϣ���Ͳ��id��
     * @param id
     * @return
     */
    @Override
    public Clue selectClueById(String id) {
        return clueMapper.selectById(id);
    }
    /**
     * ����idɾ����ǰ����
     * @param id
     * @return
     */
    @Override
    public int deleteClueById(String id) {
        return clueMapper.deleteClueById(id);
    }
}
