# 项目介绍
一款存储客户信息的后台系统，使用Java的：HSSFWorkbook完成将数据转成Excel文件，在发送给浏览器
# 技术架构
java8 ssm mybatis mysql jsp 
# 技术难点
问题一：在使用HSSFWorkbook时会有大量的代码冗余
解决：使用反射技术实现文件的导出
