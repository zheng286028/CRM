package com.zzl.crm.workbench.service.Impl;

import com.zzl.crm.workbench.mapper.ClueRemarkMapper;
import com.zzl.crm.workbench.pojo.ClueRemark;
import com.zzl.crm.workbench.service.ClueRemarkService;
import com.zzl.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ��������
 *
 * @author ֣����
 * @date 2022/05/06  22:05
 */
@Service
public class ClueRemarkServiceImpl implements ClueRemarkService {
    @Autowired
    ClueRemarkMapper clueRemarkMapper;
    /**
     * ��������id��ѯ
     * @return
     */
    @Override
    public List<ClueRemark> selectByClueId(String clueId) {
        return clueRemarkMapper.selectByClueId(clueId);
    }
    /**
     * ���������ע����
     * @param clueRemark
     * @return
     */
    @Override
    public int insertClueRemark(ClueRemark clueRemark) {
        return clueRemarkMapper.insertClueRemark(clueRemark);
    }
    /**
     * ����idɾ��������ע
     * @param id
     * @return
     */
    @Override
    public int deleteClueRemarkById(String id) {
        return clueRemarkMapper.deleteClueRemarkById(id);
    }
    /**
     * ����id�޸�
     * @param clueRemark
     * @return
     */
    @Override
    public int updateClueRemarkById(ClueRemark clueRemark) {
        return clueRemarkMapper.updateClueRemarkById(clueRemark);
    }
    /**
     * ����clueId��ѯ������ע����ϸ��Ϣ
     * @param clueId
     * @return
     */
    @Override
    public List<ClueRemark> selectClueRemarkByClueId(String clueId) {
        return clueRemarkMapper.selectClueRemarkByClueId(clueId);
    }
}
