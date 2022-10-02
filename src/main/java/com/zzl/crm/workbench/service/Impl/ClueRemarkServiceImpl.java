package com.zzl.crm.workbench.service.Impl;

import com.zzl.crm.workbench.mapper.ClueRemarkMapper;
import com.zzl.crm.workbench.pojo.ClueRemark;
import com.zzl.crm.workbench.service.ClueRemarkService;
import com.zzl.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/05/06  22:05
 */
@Service
public class ClueRemarkServiceImpl implements ClueRemarkService {
    @Autowired
    ClueRemarkMapper clueRemarkMapper;
    /**
     * 根据线索id查询
     * @return
     */
    @Override
    public List<ClueRemark> selectByClueId(String clueId) {
        return clueRemarkMapper.selectByClueId(clueId);
    }
    /**
     * 添加线索备注内容
     * @param clueRemark
     * @return
     */
    @Override
    public int insertClueRemark(ClueRemark clueRemark) {
        return clueRemarkMapper.insertClueRemark(clueRemark);
    }
    /**
     * 根据id删除线索备注
     * @param id
     * @return
     */
    @Override
    public int deleteClueRemarkById(String id) {
        return clueRemarkMapper.deleteClueRemarkById(id);
    }
    /**
     * 根据id修改
     * @param clueRemark
     * @return
     */
    @Override
    public int updateClueRemarkById(ClueRemark clueRemark) {
        return clueRemarkMapper.updateClueRemarkById(clueRemark);
    }
    /**
     * 根据clueId查询线索备注的详细信息
     * @param clueId
     * @return
     */
    @Override
    public List<ClueRemark> selectClueRemarkByClueId(String clueId) {
        return clueRemarkMapper.selectClueRemarkByClueId(clueId);
    }
}
