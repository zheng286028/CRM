<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
    <base href="<%=basePath%>"> <%--这样所有的图片资源啥的都会从根目录下找--%>
    <meta charset="UTF-8">
    <%--jquery--%>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <%--报表插件--%>
    <script type="text/javascript" src="jquery/echars/echarts.min.js"></script>
    <script>
        $(function () {
          $.ajax({
              url:'workbench/chart/transaction/queryCountOfTranGroupByStage.do',
              type:'post',
              dataType:'json',
              success:function (resp) {
                  // 基于准备好的dom，初始化echarts实例
                  var myChart = echarts.init(document.getElementById('main'));

                  // 指定图表的配置项和数据
                  var option = {
                      title: {
                          text: '交易统计图表',
                          subtext: '交易表中各个阶段的数量'
                      },
                      tooltip: {
                          trigger: 'item',
                          formatter: '{a} <br/>{b} : {c}'
                      },
                      toolbox: {
                          feature: {
                              dataView: { readOnly: false },
                              restore: {},
                              saveAsImage: {}
                          }
                      },
                      series: [
                          {
                              name: '预期',
                              type: 'funnel',
                              left: '10%',
                              width: '80%',
                              label: {
                                  formatter: '{b}预期'
                              },
                              labelLine: {
                                  show: false
                              },
                              itemStyle: {
                                  opacity: 0.8
                              },
                              emphasis: {
                                  label: {
                                      position: 'inside',
                                      formatter: '{b}: {c}'
                                  }
                              },
                              data: resp
                          }
                      ]
                  };

                  // 使用刚指定的配置项和数据显示图表。
                  myChart.setOption(option);
              }
          });
        });
    </script>
</head>
<body>
<!-- 为 ECharts 准备一个定义了宽高的 DOM -->
<div id="main" style="width: 600px;height:400px;"></div>

</div>
</body>
</html>
