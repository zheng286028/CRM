<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
	<base href="<%=basePath%>"> <%--这样所有的图片资源啥的都会从根目录下找--%>
	<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<%--自动补全插件--%>
<script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>
	<script type="text/javascript">
		//入口函数
		$(function () {
			//给阶段绑定改变事件
			$("#create-transactionStage").change(function () {
				//收集参数
				var stageValue=$(this).find("option:selected").text();
				//验证
				if(stageValue==""){
					//将可能性的输入框置为空
					$("#create-possibility").val("");
					return;
				}
				//发送请求
				$.ajax({
					url:'workbench/transaction/getPossibilityValue.do',
					data:{
						stageValue:stageValue
					},
					type:'post',
					dataType:'json',
					success:function (resp) {
						//将值赋给可能性
						$("#create-possibility").val(resp);
					}
				});
			});
			//给查询市场活动源绑定单击事件
			$("#selectActivityBtn").click(function () {
				//重置表单,内容
				$("#Tbody").html("");
				$("#selectActivityName").val("");
				//弹出模态窗口
				$("#findMarketActivity").modal("show");
			});
			//给查询输入框绑定键盘弹起事件
			$("#selectActivityName").keyup(function () {
				//收集参数
				var name=$("#selectActivityName").val();
				if (name==""){
					$("#Tbody").html("");
					//内容为空不显示内容
					return;
				}
				//发送请求
				$.ajax({
					url:'workbench/transaction/selectActivityNames.do',
					data:{
						name:name
					},
					type:'post',
					dataType:'json',
					success:function (resp) {
						//拼接字符串
						var strHtml="";
						//遍历
						$.each(resp,function (index,obj) {
							strHtml+="<tr id='tr_"+obj.id+"'>";
							strHtml+="<td><input type=\"radio\" activityName='"+obj.name+"' value='"+obj.id+"' name=\"activity\"/></td>";
							strHtml+="<td>"+obj.name+"</td>";
							strHtml+="<td>"+obj.startDate+"</td>";
							strHtml+="<td>"+obj.endDate+"</td>";
							strHtml+="<td>"+obj.owner+"</td>";
							strHtml+="</tr>";
						});
						//覆盖
						$("#Tbody").html(strHtml);
					}
				});
			});
			//给市场活动复选框绑定单击事件
			$("#Tbody").on("click","input[type='radio']:checked",function () {
				//收集参数
				var id=$(this).val();
				var name=$(this).attr("activityName");
				//将市场活动的name回显到输入框
				$("#create-activitySrc").val(name);
				$("#activityId").val(id);
				//关闭模态窗口
				$("#findMarketActivity").modal("hide");
			});
			//给联系人绑定单击事件
			$("#contactsName").click(function () {
				//重置表单，内容
				$("#ConBody").html("");
				$("#selectContacts").val("");
				//模态窗口
				$("#findContacts").modal("show");
			});
			//给查询输入框绑定弹起事件
			$("#selectContacts").keyup(function () {
				//收集参数
				var name=$("#selectContacts").val();
				if(name==""){
					$("#ConBody").html("");
					return;
				}
				//发送请求
				$.ajax({
					url:'workbench/transaction/selectContactsByNames.do',
					data:{name:name},
					type:'post',
					dataType:'json',
					success:function (resp) {
						//拼接字符串
						var strHtml="";
						//遍历
						$.each(resp,function (index,obj) {
							strHtml+="<tr id='tr_"+obj.id+"'>";
							strHtml+="<td><input type=\"radio\" contactsName='"+obj.fullname+"' value='"+obj.id+"' name=\"activity\"/></td>";
							strHtml+="<td>"+obj.fullname+"</td>";
							strHtml+="<td>"+obj.email+"</td>";
							strHtml+="<td>"+obj.mphone+"</td>";
							strHtml+="</tr>";
						});
						//覆盖
						$("#ConBody").html(strHtml);
					}
				});
			});
			//给联系复选框绑定单击事件
			$("#ConBody").on('click',"input[type='radio']:checked",function () {
				//收集参数
				var id=$(this).val();
				var name=$(this).attr("contactsName");
				//回显数据
				$("#contactsId").val(id);
				$("#create-contactsName").val(name);
				//关闭模态窗口
				$("#findContacts").modal("hide");
			});
			//给交易的客户名称绑定自动补全
			$("#create-accountName").typeahead({
				//重置内容
				source:function (jquery,process) {
					$.ajax({
						url:'workbench/transaction/selectAllCustomerName.do',
						data:{
							name:jquery
						},
						type:'post',
						dataType:'json',
						success:function (resp) {
							process(resp);
						}
					});
				}
			});
			//给"保存"按钮添加单击事件
			$("#saveTransactionBtn").click(function () {
				//收集参数
				var owner          =$("#create-transactionOwner").val();
				var money          =$("#create-amountOfMoney").val();
				var name           =$("#create-transactionName").val();
				var expectedDate   =$("#create-expectedClosingDate").val();
				var customerId     =$("#create-accountName").val();
				var stage          =$("#create-transactionStage").val();
				var type           =$("#create-transactionType").val();
				var source         =$("#create-clueSource").val();
				var activityId     =$("#activityId").val();
				var contactsId     =$("#contactsId").val();
				var description    =$("#create-describe").val();
				var contactSummary =$("#create-contactSummary").val();
				var nextContactTime=$("#create-nextContactTime").val();
				//表单验证
				if(owner=="" || owner.length<0){
					alert("所有者不可为空");
					return;
				}
				//发送请求
				$.ajax({
					url:'workbench/transaction/saveCreateTransaction.do',
					data:{
						 owner          :owner          ,
						 money          :money          ,
						 name           :name           ,
						 expectedDate   :expectedDate   ,
						 customerId     :customerId     ,
						 stage          :stage          ,
						 type           :type           ,
						 source         :source         ,
						 activityId     :activityId     ,
						 contactsId     :contactsId     ,
						 description    :description    ,
						 contactSummary :contactSummary ,
						 nextContactTime:nextContactTime
					},
					type:'post',
					dataType:'json',
					success:function (resp) {
						if(resp.code=="1"){
							//跳转到交易页面
							window.location.href='workbench/transaction/forwardTransactionIndex.do';
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

	<!-- 查找市场活动 -->
	<div class="modal fade" id="findMarketActivity" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" id="selectActivityName" class="form-control" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable3" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
							</tr>
						</thead>
						<tbody id="Tbody">

						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- 查找联系人 -->
	<div class="modal fade" id="findContacts" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找联系人</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" id="selectContacts" style="width: 300px;" placeholder="请输入联系人名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>邮箱</td>
								<td>手机</td>
							</tr>
						</thead>
						<tbody id="ConBody">

						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>


	<div style="position:  relative; left: 30px;">
		<h3>创建交易</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button type="button" class="btn btn-primary" id="saveTransactionBtn">保存</button>
			<button type="button" class="btn btn-default" onclick="window.history.back()">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" role="form" style="position: relative; top: -30px;">
		<div class="form-group">
			<label for="create-transactionOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-transactionOwner">
				<c:forEach items="${users}" var="u">
					<option value="${u.id}">${u.name}</option>
				</c:forEach>
				</select>
			</div>
			<label for="create-amountOfMoney" class="col-sm-2 control-label">金额</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-amountOfMoney">
			</div>
		</div>

		<div class="form-group">
			<label for="create-transactionName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-transactionName">
			</div>
			<label for="create-expectedClosingDate" class="col-sm-2 control-label">预计成交日期<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="date" class="form-control" id="create-expectedClosingDate">
			</div>
		</div>

		<div class="form-group">
			<label for="create-accountName" class="col-sm-2 control-label">客户名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-accountName" placeholder="支持自动补全，输入客户不存在则新建">
			</div>
			<label for="create-transactionStage" class="col-sm-2 control-label">阶段<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
			  <select class="form-control" id="create-transactionStage">
			  	<option></option>
			  <c:forEach items="${stages}" var="sta">
				  <option value="${sta.id}">${sta.value}</option>
			  </c:forEach>
			  </select>
			</div>
		</div>

		<div class="form-group">
			<label for="create-transactionType" class="col-sm-2 control-label">类型</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-transactionType">
				  <option></option>
				 <c:forEach items="${transactionTypes}" var="tran">
					 <option value="${tran.id}">${tran.value}</option>
				 </c:forEach>
				</select>
			</div>
			<label for="create-possibility" class="col-sm-2 control-label">可能性</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-possibility" readonly>
			</div>
		</div>

		<div class="form-group">
			<label for="create-clueSource" class="col-sm-2 control-label">来源</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-clueSource">
				  <option></option>
				 <c:forEach items="${sources}" var="sou">
					 <option value="${sou.id}">${sou.value}</option>
				 </c:forEach>
				</select>
			</div>
			<label for="create-activitySrc" class="col-sm-2 control-label">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="selectActivityBtn" data-target="#findMarketActivity"><span class="glyphicon glyphicon-search"></span></a></label>
			<input type="hidden" id="activityId">
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-activitySrc">
			</div>
		</div>

		<div class="form-group">
			<label for="create-contactsName" class="col-sm-2 control-label">联系人名称&nbsp;&nbsp;<a href="javascript:void(0);" id="contactsName" data-target="#findContacts"><span class="glyphicon glyphicon-search"></span></a></label>
			<input type="hidden" id="contactsId">
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-contactsName">
			</div>
		</div>

		<div class="form-group">
			<label for="create-describe" class="col-sm-2 control-label">描述</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-describe"></textarea>
			</div>
		</div>

		<div class="form-group">
			<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
			</div>
		</div>

		<div class="form-group">
			<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="date" class="form-control" id="create-nextContactTime">
			</div>
		</div>

	</form>
</body>
</html>
