package com.zzl.crm.workbench.mapper;

import com.zzl.crm.workbench.pojo.ClueRemark;

import java.util.List;

public interface ClueRemarkMapper {
    /**
     * ��������id��ѯ
     * @return
     */
  List<ClueRemark> selectByClueId(String clueId);

    /**
     * ���������ע����
     * @param clueRemark
     * @return
     */
  int insertClueRemark(ClueRemark clueRemark);

    /**
     * ����idɾ��������ע
     * @param id
     * @return
     */
  int deleteClueRemarkById(String id);

  /**
   * ����id�޸�
   * @param clueRemark
   * @return
   */
  int updateClueRemarkById(ClueRemark clueRemark);
  /**
   * ����clueId��ѯ������ע����ϸ��Ϣ
   * @param clueId
   * @return
   */
  List<ClueRemark> selectClueRemarkByClueId(String clueId);
}
