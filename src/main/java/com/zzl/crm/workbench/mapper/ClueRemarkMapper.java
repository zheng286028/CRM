package com.zzl.crm.workbench.mapper;

import com.zzl.crm.workbench.pojo.ClueRemark;

import java.util.List;

public interface ClueRemarkMapper {
    /**
     * 根据线索id查询
     * @return
     */
  List<ClueRemark> selectByClueId(String clueId);

    /**
     * 添加线索备注内容
     * @param clueRemark
     * @return
     */
  int insertClueRemark(ClueRemark clueRemark);

    /**
     * 根据id删除线索备注
     * @param id
     * @return
     */
  int deleteClueRemarkById(String id);

  /**
   * 根据id修改
   * @param clueRemark
   * @return
   */
  int updateClueRemarkById(ClueRemark clueRemark);
  /**
   * 根据clueId查询线索备注的详细信息
   * @param clueId
   * @return
   */
  List<ClueRemark> selectClueRemarkByClueId(String clueId);
}
