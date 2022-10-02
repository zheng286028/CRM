package com.zzl.test;

import com.zzl.crm.commons.utils.HSSFUtils;
import com.zzl.crm.settings.pojo.User;
import com.zzl.crm.settings.service.UserService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 功能描述
 *
 * @author 郑子浪
 * @date 2022/04/29  22:02
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext_mapper.xml","classpath:applicationContext_service.xml"})
public class MyTest {
    @Autowired
    UserService service;
    @Test
    public void test01(){
        String id = UUID.randomUUID().toString().replaceAll("-","");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH.mm");
        final String format = sdf.format(new Date());
        final int i = service.insertSelective(new User(
                id, "2822", "邱媛", "286028", "2860285053@qq.com", "2028-12-1",
                "1", "A001", "192.168.0.10", format,
                "", format, ""
        ));
        System.out.println(i);
    }
    @Test
    public void test02(){
        System.out.println("你好");
    }
    @Test
    public void Test03() throws IOException {
        //拿到文件
        FileInputStream fin = new FileInputStream("C:\\myProject\\crm-project\\poi\\activity.xls");
        //创建POI对象
        HSSFWorkbook wb = new HSSFWorkbook(fin);
        HSSFSheet sheet = wb.getSheetAt(0);//页的下标
        //获取行
        HSSFRow row=null;
        HSSFCell cell=null;
        for (int i = 0; i <=sheet.getLastRowNum(); i++) {//getLastRowNumdan当前页的最后一个下标
            row=sheet.getRow(i);//每遍历一次创建一行
            for (int j = 0; j <row.getLastCellNum() ; j++) {//row.getLastCellNum()下标在加1所以不能<=
                cell = row.getCell(j);//每遍历一次创建一列
                System.out.print(HSSFUtils.getHSSFCellStr(cell)+" ");
            }
            System.out.println();
        }
    }
}
