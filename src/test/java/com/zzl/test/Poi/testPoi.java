package com.zzl.test.Poi;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/05/01  17:43
 * 测试poi创建，写入，导出excel文件
 */
public class testPoi {
    public static void main(String[] args) throws Exception {
        //创建excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        //往创建的workbook写完一页
        HSSFSheet sheet = workbook.createSheet("学生表");//可以给当前页起个名字
        //给sheet页创建一行
        HSSFRow row = sheet.createRow(0);//指定下表，也就是第几行
        //给row这一行里写入一列
        HSSFCell cell = row.createCell(0);//一样能指定下标，指定写到那一列
        //往cell写入数据
        cell.setCellValue("学号");
        //第一行第二列
        cell=row.createCell(1);
        cell.setCellValue("姓名");//即使没有变量引用也不会被垃圾回收机制回收，row的内部会有个变量指向它
        //第一行第三列
        cell=row.createCell(2);
        cell.setCellValue("年龄");

        //生成HSSFCellStyle对象
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        //每创建一行就写入三列数据
        for (int i = 1; i <=10 ; i++) {
            row=sheet.createRow(i);

            cell=row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue(1000+i);
            cell=row.createCell(1);
            cell.setCellValue("name="+i);
            cell=row.createCell(2);
            cell.setCellStyle(style);
            cell.setCellValue(20+i);
        }
        OutputStream os = new FileOutputStream("C:\\myProject\\crm-project\\poi\\student.xls");
        workbook.write(os);
        //释放资源
        os.close();
        workbook.close();
        System.out.println("ok...........");
    }
}
