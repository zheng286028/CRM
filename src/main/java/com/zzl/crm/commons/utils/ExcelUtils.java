package com.zzl.crm.commons.utils;

import org.apache.poi.hssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：Excel的工具类
 *
 * @author 郑子浪
 * @date 2022/10/04  0:42
 */
public class ExcelUtils {

    /**
     * 使用反射导出Excel文件
     *
     * @param dataList 导出的数据
     * @param out      输出流
     * @param <T>      任意类型
     */
    public static <T> void exportExcel(List<T> dataList, ServletOutputStream out) throws IllegalAccessException, IOException {
        //为空直接返回
        if (dataList.isEmpty()) {
            return;
        }
        //1、获取文件列名
        List<String> fields = ClassUtils.getFields(dataList.get(0).getClass());
        //2、建立一个Excel文件,并创建表单
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        //3、创建表头
        HSSFRow row = sheet.createRow(0);
        //4、创建单元格,并设置列名
        HSSFCell cell;
        for (int i = 0; i < fields.size(); i++) {
            cell = row.createCell(i);
            cell.setCellValue(fields.get(i));
        }
        //5、为每一行添加数据
        Map<String, Object> dataMap;
        for (int i = 0; i < dataList.size(); i++) {
            //5.1、添加新一行
            row = sheet.createRow(i + 1);
            //5.2、获取每个对象的值
            dataMap = ClassUtils.getFieldValues(dataList.get(i));
            //5.3、将该对象写入该行
            for (int j = 0; j < fields.size(); j++) {
                row.createCell(j).setCellValue(dataMap.get(fields.get(j))+"");
            }
        }
        workbook.write(out);
        out.flush();
        out.close();
    }
}
