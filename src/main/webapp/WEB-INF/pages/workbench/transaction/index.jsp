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
	<%--jquery框架--%>
	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
	<%--日历插件--%>
	<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
	<%--pagination分页插件--%>
	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>
	<%--自动补全插件--%>
	<script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>
<script type="text/javascript">

	$(function(){
		//给"创建按钮"绑定单击事件
		$("#createBtn").click(function () {
			//跳转到创建交易页面
			window.location.href="workbench/transaction/transactionSave.do";
		});
		//页面刷新查询
		inquireTransactionByPageAndCondition(1,3);
		//给"查询"按钮绑定单击事件
		$("#inquireBtn").click(function () {
			inquireTransactionByPageAndCondition(1,$("#currentPage").bs_pagination('getOption','rowsPerPage'));
		});
		//给"修改"按钮绑定单击事件
		$("#saveEditBtn").click(function () {
			//收集参数
			var ids=$("#Tbody input[type='checkbox']:checked");
			if(ids.size()==0 || ids.size()>1){
				alert("请选择你要修改的数据！且一次只能修改一条数据")
				return;
			}
			var id=ids.val();
			//跳转到修改页面
			$(window.location.href="workbench/tran/queryTransactionById.do?id="+id);
		});
	});
	function inquireTransactionByPageAndCondition(currentPage,pageSize) {
		//收集参数
		var owner=$("#owner").val();
		var name=$("#name").val();
		var customerId=$("#customerName").val();
		var stage=$("#stage").val();
		var type=$("#type").val();
		var source=$("#source").val();
		var contactsId=$("#contactsName").val();
		//发送请求
		$.ajax({
			url:'workbench/tran/queryTransactionByPageAndCondition.do',
			data:{
				owner:owner,
				name:name,
				customerId:customerId,
				stage:stage,
				type:type,
				source:source,
				contactsId:contactsId,
				currentPage:currentPage,
				pageSize:pageSize
			},
			type:'post',
			dataType:'json',
			success:function (resp) {
				var strHtml="";
				//遍历
				$.each(resp.trans,function (index,obj) {
					strHtml+="<tr class=\"active\">";
					strHtml+="<td><input type=\"checkbox\" value='"+obj.id+"'/></td>";
					strHtml+="<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench.transaction/TransactionRemarkByTransactionId.do?transactionId="+obj.id+"';\">"+obj.name+"</a></td>";
					strHtml+="<td>"+obj.customerId+"</td>";
					strHtml+="<td>"+obj.stage+"</td>";
					strHtml+="<td>"+obj.type+"</td>";
					strHtml+="<td>"+obj.owner+"</td>";
					strHtml+="<td>"+obj.source+"</td>";
					strHtml+="<td>"+obj.contactsId+"</td>";
					strHtml+="</tr>";
				});
				//覆盖
				$("#Tbody").html(strHtml);
				//计算总页数
				var totalPage=1;
				if(resp.totalCount%pageSize==0){
					totalPage=(resp.totalCount/pageSize)
				}else{
					totalPage=parseInt(resp.totalCount/pageSize)+1
				}
				//分页工具
				//分页插件
				$("#currentPage").bs_pagination({
					currentPage:currentPage,//当前页
					rowsPerPage:pageSize,//当前页显示页数
					totalRows:resp.totalCount,//总记录数
					totalPages:totalPage,//总页数
					visiblePageLinks:5,//最多显示的卡片数
					showGoToPage:true,//是否显示跳转页面
					showRowsPerPage:true, //是否显示每页显示条数
					showRowsInfo:true,//是否显示记录的信息
					//切换页号触发事件
					//每次返回切换页号之后的currentPage,pageSize
					onChangePage:function (event,pageObj) {
						inquireTransactionByPageAndCondition(pageObj.currentPage,pageObj.rowsPerPage);
					},
				});
			}
		});
	}

</script>
</head>
<body>



	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>交易列表</h3>
			</div>
		</div>
	</div>

	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">

		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">

			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="owner">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="name">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">客户名称</div>
				      <input class="form-control" type="text" id="customerName">
				    </div>
				  </div>

				  <br>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">阶段</div>
					  <select class="form-control" id="stage">
					  	<option></option>
					 <c:forEach items="${stages}" var="sta">
						 <option value="${sta.id}">${sta.value}</option>
					 </c:forEach>
					  </select>
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">类型</div>
					  <select class="form-control" id="type">
					  	<option></option>
					  <c:forEach items="${transactionTypes}" var="tran">
						  <option value="${tran.id}">${tran.value}</option>
					  </c:forEach>
					  </select>
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">来源</div>
				      <select class="form-control" id="source">
						  <option></option>
						<c:forEach items="${sources}" var="s">
							<option value="${s.id}">${s.value}</option>
						</c:forEach>
						</select>
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">联系人名称</div>
				      <input class="form-control" type="text" id="contactsName">
				    </div>
				  </div>

				  <button type="button" class="btn btn-default" id="inquireBtn">查询</button>

				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 10px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="saveEditBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>


			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" /></td>
							<td>名称</td>
							<td>客户名称</td>
							<td>阶段</td>
							<td>类型</td>
							<td>所有者</td>
							<td>来源</td>
							<td>联系人名称</td>
						</tr>
					</thead>
					<tbody id="Tbody">

					</tbody>
				</table>
				<%--分页--%>
				<div id="currentPage"></div>
			</div>

		</div>

	</div>
</body>
</html>
