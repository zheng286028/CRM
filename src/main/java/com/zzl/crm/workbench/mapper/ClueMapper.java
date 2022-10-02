package com.zzl.crm.workbench.mapper;

import com.zzl.crm.workbench.pojo.Clue;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ClueMapper {

    /**
     * 添加线索
     * @param clue
     * @return
     */
    int insertClue(Clue clue);

    /**
     * 分页条件查询
     * @param map
     * @return
     */
    List<Clue> queryClueByPageAndCondition(Map<String,Object> map);

    /**
     * 条件查询总记录数
     * @param map
     * @return
     */
    int queryClueByConditionTotal(Map<String,Object> map);

    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    int deleteClueByIds(@Param("ids") String[] ids);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Clue selectById(String id);

    /**
     * 根据id修改
     * @param clue
     * @return
     */
    int updateClueById(Clue clue);

    /**
     * 根据id查询，不查明细信息，就查带id的
     * @param id
     * @return
     */
    Clue selectClueById(String id);

    /**
     * 根据id删除当前线索
     * @param id
     * @return
     */
    int deleteClueById(String id);
}
