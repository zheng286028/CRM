package com.zzl.crm.workbench.web.controller;

import com.zzl.crm.workbench.pojo.FunnelVO;
import com.zzl.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * ��������
 *
 * @author ֣����
 * @date 2022/05/16  1:08
 */
@Controller
public class ChartController {
    @Autowired
    private TranService tranService;

    @RequestMapping("/workbench/chart/transaction/index.do")
    public String index(){
        return "workbench/chart/transaction/index";
    }

    /**
     * ��ѯ����ͳ��ͼ��
     * @return
     */
    @RequestMapping("/workbench/chart/transaction/queryCountOfTranGroupByStage.do")
    @ResponseBody
    public Object queryCountOfTranGroupByStage(){
        //���÷�����ѯ
        List<FunnelVO> funnelVOS = tranService.selectCountOfTranGroupByStage();
        return funnelVOS;
    }
}
