<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
	<base href="<%=basePath%>"> <%--这样所有的图片资源啥的都会从根目录下找--%>
	<meta charset="UTF-8">

	<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>


<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<script type="text/javascript">
	$(function(){
		$("#isCreateTransaction").click(function(){
			if(this.checked){
				$("#create-transaction2").show(200);
			}else{
				$("#create-transaction2").hide(200);
			}
		});
		//市场活动模态窗口
		$("#activityModal").click(function () {
			//重置模态窗口
			$("#Tbody").html("");
			$("#fromClueConvert").val("");
			$("#searchActivityModal").modal("show");
		});
		$("#fromClueConvert").keyup(function () {
			var activityName=$("#fromClueConvert").val();
			var clueId='${clue.id}';
			//发送请求
			$.ajax({
				url:'workbench/clue/selectActivityByActivityNameAndClueId.do',
				data:{
					activityName:activityName,
					clueId:clueId
				},
				type:'post',
				dataType:'json',
				success:function (resp) {
					var strHtml="";
					$.each(resp,function (index,obj) {
						strHtml+="<tr>";
						strHtml+="<td><input type=\"radio\" value='"+obj.id+"' activityName=\""+obj.name+"\" name=\"activity\"/></td>";
						strHtml+="<td>"+obj.name+"</td>";
						strHtml+="<td>"+obj.startDate+"</td>";
						strHtml+="<td>"+obj.endDate+"</td>";
						strHtml+="<td>"+obj.owner+"</td>";
						strHtml+="</tr>";
					});
					$("#Tbody").html(strHtml);
				}
			});
		});
		$("#Tbody").on('click',"input[type='radio']:checked",function () {
			//收集参数
			var id=$(this).val();
			var name=$(this).attr("activityName");
			//回显数据
			$("#activity").val(name);
			$("#activityId").val(id);
			//关闭模态窗口
			$("#searchActivityModal").modal("hide");
		});
		//给”转换“按钮绑定单击事件
		$("#convertBtn").click(function () {
			//收集参数
			var clueId='${clue.id}';
			var activityId=$("activity").val();
			var money = $.trim($("#amountOfMoney").val());
			var name = $.trim($("#tradeName").val());
			var expectedDate=$.trim($("#expectedClosingDate").val());
			var stage = $.trim($("#stage").val());
			var isTransaction=$("#isCreateTransaction").prop('checked');
			//发送请求
			$.ajax({
				url:'workbench/customer/CustomerClue.do',
				data:{
					clueId:clueId,
					activityId:activityId,
					money:money,
					name:name,
					expectedDate:expectedDate,
					stage:stage,
					isTransaction:isTransaction
				},
				type:'post',
				dataType:'json',
				success:function (resp) {
					if(resp.code=="1"){
						//跳转到线索页面
						window.location.href="workbench/clue/clueIndex.do";//文.件[/WEB-INF/pages/workbench/customer/CustomerClue.jsp] 未找到
					}else{
						alert(resp.message);
					}
				}
			});
		});
	});
</script>

</head>
<body>

	<!-- 搜索市场活动的模态窗口 -->
	<div class="modal fade" id="searchActivityModal" role="dialog" >
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">搜索市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" id="fromClueConvert" class="form-control" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
								<td></td>
							</tr>
						</thead>
						<tbody id="Tbody">

						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<div id="title" class="page-header" style="position: relative; left: 20px;">
		<h4>转换线索 <small>${clue.fullname}-${clue.appellation}</small></h4>
	</div>
	<div id="create-customer" style="position: relative; left: 40px; height: 35px;">
		新建客户：${clue.company}
	</div>
	<div id="create-contact" style="position: relative; left: 40px; height: 35px;">
		新建联系人：${clue.fullname}${clue.appellation}
	</div>
	<div id="create-transaction1" style="position: relative; left: 40px; height: 35px; top: 25px;">
		<input type="checkbox" id="isCreateTransaction"/>
		为客户创建交易
	</div>
	<div id="create-transaction2" style="position: relative; left: 40px; top: 20px; width: 80%; background-color: #F7F7F7; display: none;" >

		<form>
		  <div class="form-group" style="width: 400px; position: relative; left: 20px;">
		    <label for="amountOfMoney">金额</label>
		    <input type="text" class="form-control" id="amountOfMoney">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="tradeName">交易名称</label>
		    <input type="text" class="form-control" id="tradeName" value="${clue.company}-">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="expectedClosingDate">预计成交日期</label>
		    <input type="text" class="form-control" id="expectedClosingDate">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="stage">阶段</label>
		    <select id="stage"  class="form-control">
		    <c:forEach items="${dicValueList}" var="dvl">
				<option value="${dvl.id}">${dvl.value}</option>
			</c:forEach>
		    </select>
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
			  <input type="hidden" value="1" id="activityId">
		    <label for="activity">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="activityModal" data-target="#searchActivityModal" style="text-decoration: none;"><span class="glyphicon glyphicon-search"></span></a></label>
		    <input type="text" class="form-control" id="activity" placeholder="点击上面搜索" readonly>
		  </div>
		</form>

	</div>

	<div id="owner" style="position: relative; left: 40px; height: 35px; top: 50px;">
		记录的所有者：<br>
		<b>${clue.owner}</b>
	</div>
	<div id="operation" style="position: relative; left: 40px; height: 35px; top: 100px;">
		<input class="btn btn-primary" type="button" value="转换" id="convertBtn">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input class="btn btn-default" type="button" onclick="window.history.back();" value="取消">
	</div>
</body>
</html>
