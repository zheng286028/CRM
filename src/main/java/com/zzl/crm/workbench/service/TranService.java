package com.zzl.crm.workbench.service;

import com.zzl.crm.workbench.pojo.FunnelVO;
import com.zzl.crm.workbench.pojo.Tran;

import java.util.List;
import java.util.Map;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/05/10  13:32
 */
public interface TranService {
    /**
     * 添加交易
     * @param tran
     * @return
     */
    int insertTran(Tran tran);
    /**
     * 根据客户id查询当前客户下的交易
     * @return
     */
    List<Tran> selectTranDetailed();

    /**
     * 添加交易
     * @param map
     * @return
     */
    void enterTransaction(Map<String,Object> map);
    /**
     * 分页条件查询
     * @param map
     * @return
     */
    List<Tran> inquireTransactionByPageAndCondition(Map<String,Object>map);

    /**
     * 查询条件总记录数
     * @param map
     * @return
     */
    int inquireTransactionTotalCountByCondition(Map<String,Object>map);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    Tran inquireTransactionById(String id);
    /**
     * 根据id修改
     * @param map
     * @return
     */
    void reviseTransactionById(Map<String,Object>map);

    /**
     * 查询交易统计图表
     * @return
     */
    List<FunnelVO> selectCountOfTranGroupByStage();
}
