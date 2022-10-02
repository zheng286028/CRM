package com.zzl.crm.commons.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * ��������
 * ����excel�ļ��Ĺ�����
 * @author ֣����
 * @date 2022/05/03  16:22
 */
public class HSSFUtils {
    //HSSFCellÿһ�е������ж�
    public static String getHSSFCellStr(HSSFCell cell){
        String str="";
        if(cell.getCellType()==HSSFCell.CELL_TYPE_STRING){
            str=cell.getStringCellValue();
        }else if(cell.getCellType()==HSSFCell.CELL_TYPE_BOOLEAN){
            str=cell.getBooleanCellValue()+"";
        }else if (cell.getCellType()==HSSFCell.CELL_TYPE_FORMULA){
            str=cell.getCellFormula();
        }else if (cell.getCellType()==HSSFCell.LAST_COLUMN_NUMBER){
            str=cell.getNumericCellValue()+"";
        }else{
            str="";
        }
        return str;
    }
}
