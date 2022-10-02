package com.zzl.crm.workbench.service.Impl;

import com.zzl.crm.workbench.mapper.ClueMapper;
import com.zzl.crm.workbench.pojo.Clue;
import com.zzl.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/05/05  19:36
 */
@Service
public class ClueServiceImpl implements ClueService {
    @Autowired
    ClueMapper clueMapper;
    /**
     * 添加线索
     * @param clue
     * @return
     */
    @Override
    public int insertClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }
    /**
     * 分页条件查询
     * @param map
     * @return
     */
    @Override
    public List<Clue> queryClueByPageAndCondition(Map<String, Object> map) {
        return clueMapper.queryClueByPageAndCondition(map);
    }
    /**
     * 条件查询总记录数
     * @param map
     * @return
     */
    @Override
    public int queryClueByConditionTotal(Map<String, Object> map) {
        return clueMapper.queryClueByConditionTotal(map);
    }
    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    @Override
    public int deleteClueByIds(String[] ids) {
        return clueMapper.deleteClueByIds(ids);
    }
    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public Clue selectById(String id) {
        return clueMapper.selectById(id);
    }
    /**
     * 根据id修改
     * @param clue
     * @return
     */
    @Override
    public int updateClueById(Clue clue) {
        return clueMapper.updateClueById(clue);
    }
    /**
     * 根据id查询，不查明细信息，就查带id的
     * @param id
     * @return
     */
    @Override
    public Clue selectClueById(String id) {
        return clueMapper.selectById(id);
    }
    /**
     * 根据id删除当前线索
     * @param id
     * @return
     */
    @Override
    public int deleteClueById(String id) {
        return clueMapper.deleteClueById(id);
    }
}
