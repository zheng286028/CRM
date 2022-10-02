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
		//定制字段
		$("#definedColumns > li").click(function(e) {
			//防止下拉菜单消失
	        e.stopPropagation();
	    });
		//页面刷新调用方法查询
		selectContactsPageAndCondition(1,3);
		//给创建客户绑定自动补全
		$("#create-customerName").typeahead({
			source:function (jquery,process) {
				//发送请求
				$.ajax({
					url:'workbench/customer/selectCustomerByName.do',
					data:{customerName:jquery},
					type:'post',
					dataType:'json',
					success:function (resp) {
						process(resp);
					}
				});
			}
		});
		//给创建按钮绑定单击事件
		$("#createContactsBtn").click(function () {
			//重置表单
			$("#Tfrom").val("");
			//弹出模态窗口
			$("#createContactsModal").modal("show");
		});
		//给保存按钮绑定单击事件
		$("#saveContactsBtn").click(function () {
			//收集参数
			var owner=$("#create-contactsOwner").val();
			var source=$("#create-clueSource").val();
			var fullnName=$("#create-surname").val();
			var appellation=$("#create-call").val();
			var job=$("#create-job").val();
			var mphone=$("#create-mphone").val();
			var email=$("#create-email").val();
			var createTime=$("#create-birth").val();
			var customerName=$("#create-customerName").val();
			var description=$("#create-describe").val();
			var contactSummary=$("#create-contactSummary").val();
			var nextContactTime=$("#create-nextContactTime").val();
			var address=$("#create-address").val();
			//表单验证
			//发送请求
			$.ajax({
				url:'workbench/contacts/createContacts.do',
				data:{
					owner:owner,
					source:source,
					fullname:fullnName,
					appellation:appellation,
					job:job,
					mphone:mphone,
					email:email,
					createTime:createTime,
					CustomerName:customerName,
					description:description,
					contactSummary:contactSummary,
					nextContactTime:nextContactTime,
					address:address
				},
				type:'post',
				dataType:'json',
				success:function (resp) {
					if(resp.code=="1"){
						//关闭模态窗口
						$("#createContactsModal").modal("hide");
						//调用方法查询
						selectContactsPageAndCondition(1,$("#currentPage").bs_pagination("getOption","rowsPerPage"));
					}else{
						alert(resp.message);
					}
				}
			});
		});
		//给查询按钮绑定单击事件
		$("#queryContactsByConditionBtn").click(function () {
			selectContactsPageAndCondition(1,$("#currentPage").bs_pagination('getOption','rowsPerPage'));
		});
		//给复选框绑定单击事件
		$("#cheAll").click(function () {
			//给所有复选框绑定
			$("#Tbody input[type='checkbox']").prop('checked',this.checked);
		});
		//给全部复选框绑定单击事件
		$("#Tbody").on('click',"input[type='checkbox']",function () {
			if($("#Tbody input[type='checkbox']:checked").size()==$("#Tbody input[type='checkbox']").size()){
				$("#cheAll").prop('checked',true);
			}else{
				$("#cheAll").prop('checked',false);
			}
		});
		//给”删除“按钮绑定单击事件
		$("#deleteBtn").click(function () {
			//收集参数
			var conIds=$("#Tbody input[type='checkbox']:checked");
			//判断
			if(conIds.size()==0){
				alert("请选择你要删除的数据");
				return;
			}
			//遍历
			var id="";
			$.each(conIds,function (index,obj) {
				id+=obj.value+",";
			});
			if(window.confirm("确定删除吗?")){
			//截断最后一个字符
			var ids ="ids="+id.substring(0,id.length-1);
			//发送请求
				$.ajax({
					url:'workbench/contacts/deleteContactsByIds.do',
					data:ids,
					type:'post',
					dataType:'json',
					success:function (resp) {
						if(resp.code=="1"){
							//刷新页面
							selectContactsPageAndCondition($("#currentPage").bs_pagination("getOption","currentPage"),
									$("#currentPage").bs_pagination("getOption","rowsPerPage"));
						}else{
							alert(resp.message);
						}
					}
				});
			}
		});
		//给”修改“按钮绑定单击事件
		$("#editContactsBtn").click(function () {
			//收集参数
			var ids=$("#Tbody input[type='checkbox']:checked");
			if(ids.size()==0 || ids.size()>1){
				alert("请选择你要修改的数据，且一次只能修改一条");
				return;
			}
			//获取id
			var id ="id="+ids.val();
			//发送请求
			$.ajax({
				url:'workbench/contacts/queryContactsById.do',
				data:id,
				type:'post',
				dataType:'json',
				success:function (resp) {
					$("#edit-contactsOwner").val(resp.owner);
					$("#edit-clueSource").val(resp.source);
					$("#edit-surname").val(resp.fullname);
					$("#edit-call").val(resp.appellation);
					$("#edit-job").val(resp.job);
					$("#edit-mphone").val(resp.mphone);
					$("#edit-email").val(resp.email);
					$("#edit-birth").val(resp.createTime);
					$("#edit-customerName").val(resp.customerId);
					$("#edit-describe").val(resp.description);
					$("#edit-contactSummary").val(resp.contactSummary);
					$("#edit-nextContactTime").val(resp.nextContactTime);
					$("#editContactsId").val(resp.id);
					//弹出模态窗口
					$("#editContactsModal").modal("show");
				}
			});
		});
		//给创建客户绑定自动补全
		$("#edit-customerName").typeahead({
			source:function (jquery,process) {
				//发送请求
				$.ajax({
					url:'workbench/customer/selectCustomerByName.do',
					data:{customerName:jquery},
					type:'post',
					dataType:'json',
					success:function (resp) {
						process(resp);
					}
				});
			}
		});
		//给保存按钮添加单击事件
		$("#saveEditContactsBtn").click(function () {
			//收集参数
			var owner=$("#edit-contactsOwner").val();
			var source=$("#edit-clueSource").val();
			var fullname=$("#edit-surname").val();
			var appellation=$("#edit-call").val();
			var job=$("#edit-job").val();
			var mphone=$("#edit-mphone").val();
			var email=$("#edit-email").val();
			var createTime=$("#edit-birth").val();
			var customerId=$("#edit-customerName").val();
			var description=$("#edit-describe").val();
			var contactSummary=$("#edit-contactSummary").val();
			var nextContactTime=$("#edit-nextContactTime").val();
			var address=$("#edit-address").val();
			var id=$("#editContactsId").val();
			//表单验证
			if(owner==""){
				alert("所有者不可为空");
				return;
			}
			if(fullname==""){
				alert("名称不可为空");
				return;
			}
			//发送请求
			$.ajax({
				url:'workbench/contacts/ReviseContactsById.do',
				data:{
					owner:owner,
					source:source,
					fullname:fullname,
					appellation:appellation,
					job:job,
					mphone:mphone,
					email:email,
					createTime:createTime,
					CustomerId:customerId,
					description:description,
					contactSummary:contactSummary,
					nextContactTime:nextContactTime,
					address:address,
					id:id
				},
				type:'post',
				dataType:'json',
				success:function (resp) {
					if(resp.code=="1"){
						selectContactsPageAndCondition($("#currentPage").bs_pagination('getOption','currentPage'),
														$("#currentPage").bs_pagination('getOption','rowsPerPage'));
						//关闭模态窗口
						$("#editContactsModal").modal("hide");
					}else{
						alert(resp.message);
					}
				}
			});
		});
	});
	//分页条件查询
	function selectContactsPageAndCondition(currentPage,pageSize) {
		//收集参数
		var owner=$("#owner").val();
		var fullName=$("#fullName").val();
		var CustomerId=$("#CustomerId").val();
		var source=$("#source").val();
		var createTime=$("#createTime").val();
		//发送请求
		$.ajax({
			url:'workbench/contacts/queryContactsByPageAnCondition.do',
			data:{
				owner:owner,
				fullname:fullName,
				customerId:CustomerId,
				source:source,
				createTime:createTime,
				current:currentPage,
				pageSize:pageSize
			},
			type:'post',
			dataType:'json',
			success:function (resp) {
				//遍历
				var strHtml="";
				$.each(resp.contacts,function (index,obj) {
					strHtml+="<tr>";
					strHtml+="<td><input type=\"checkbox\" value=\""+obj.id+"\" /></td>";
					strHtml+="<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/contacts/forwardDetail.do?contactsId="+obj.id+"';\">"+obj.fullname+"</a></td>";
					strHtml+="<td>"+obj.customerId+"</td>";
					strHtml+="<td>"+obj.owner+"</td>";
					strHtml+="<td>"+obj.source+"</td>";
					strHtml+="<td>"+obj.createTime+"</td>";
					strHtml+="</tr>";
				});
				//覆盖
				$("#Tbody").html(strHtml);
				//清除复选框
				$("#cheAll").prop("checked",false);
				//计算总页数
				var totalPage=1;
				if(resp.totalCount%pageSize==0){
					totalPage=(resp.totalCount/pageSize);
				}else{
					totalPage=parseInt(resp.totalCount/pageSize)+1;
				}
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
						selectContactsPageAndCondition(pageObj.currentPage,pageObj.rowsPerPage);
					},
				});
			}
		});
	}

</script>
</head>
<body>


	<!-- 创建联系人的模态窗口 -->
	<div class="modal fade" id="createContactsModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" onclick="$('#createContactsModal').modal('hide');">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabelx">创建联系人</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" id="Tfrom" role="form">

						<div class="form-group">
							<label for="create-contactsOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-contactsOwner">
								<c:forEach items="${users}" var="u">
									<option value="${u.id}">${u.name}</option>
								</c:forEach>
								</select>
							</div>
							<label for="create-clueSource" class="col-sm-2 control-label">来源</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-clueSource">
								  <option></option>
								 <c:forEach items="${sources}" var="sou">
									 <option value="${sou.id}">${sou.value}</option>
								 </c:forEach>
								</select>
							</div>
						</div>

						<div class="form-group">
							<label for="create-surname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-surname">
							</div>
							<label for="create-call" class="col-sm-2 control-label">称呼</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-call">
								  <option></option>
								<c:forEach items="${appellations}" var="app">
									<option value="${app.id}">${app.value}</option>
								</c:forEach>
								</select>
							</div>

						</div>

						<div class="form-group">
							<label for="create-job" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-job">
							</div>
							<label for="create-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-mphone">
							</div>
						</div>

						<div class="form-group" style="position: relative;">
							<label for="create-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-email">
							</div>
							<label for="create-birth" class="col-sm-2 control-label">生日</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-birth">
							</div>
						</div>

						<div class="form-group" style="position: relative;">
							<label for="create-customerName" class="col-sm-2 control-label">客户名称</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-customerName" placeholder="支持自动补全，输入客户不存在则新建">
							</div>
						</div>

						<div class="form-group" style="position: relative;">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>

						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

						<div style="position: relative;top: 15px;">
							<div class="form-group">
								<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
								<div class="col-sm-10" style="width: 81%;">
									<textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
								<div class="col-sm-10" style="width: 300px;">
									<input type="date" class="form-control" id="create-nextContactTime">
								</div>
							</div>
						</div>

                        <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="create-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="create-address">北京大兴区大族企业湾</textarea>
                                </div>
                            </div>
                        </div>
					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveContactsBtn" >保存</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 修改联系人的模态窗口 -->
	<div class="modal fade" id="editContactsModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">修改联系人</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
						<input type="hidden" id="editContactsId">
						<div class="form-group">
							<label for="edit-contactsOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-contactsOwner">
										<c:forEach items="${requestScope.users}" var="u">
											<option value="${u.id}">${u.name}</option>
										</c:forEach>
								</select>
							</div>
							<label for="edit-clueSource" class="col-sm-2 control-label">来源</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-clueSource">
								  <option></option>
									<c:forEach items="${requestScope.sources}" var="sou">
										<option value="${sou.id}">${sou.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>

						<div class="form-group">
							<label for="edit-surname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-surname" value="李四">
							</div>
							<label for="edit-call" class="col-sm-2 control-label">称呼</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-call">
								  <option></option>
									<c:forEach items="${appellations}" var="app">
										<option value="${app.id}">${app.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>

						<div class="form-group">
							<label for="edit-job" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-job" value="CTO">
							</div>
							<label for="edit-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-mphone" value="12345678901">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-email" value="lisi@bjpowernode.com">
							</div>
							<label for="edit-birth" class="col-sm-2 control-label">生日</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-birth">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-customerName" class="col-sm-2 control-label">客户名称</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-customerName" placeholder="支持自动补全，输入客户不存在则新建" value="动力节点">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">这是一条线索的描述信息</textarea>
							</div>
						</div>

						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

						<div style="position: relative;top: 15px;">
							<div class="form-group">
								<label for="edit-contactSummary" class="col-sm-2 control-label">联系纪要</label>
								<div class="col-sm-10" style="width: 81%;">
									<textarea class="form-control" rows="3" id="edit-contactSummary"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label for="edit-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
								<div class="col-sm-10" style="width: 300px;">
									<input type="date" class="form-control" id="edit-nextContactTime">
								</div>
							</div>
						</div>

						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="edit-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="edit-address">北京大兴区大族企业湾</textarea>
                                </div>
                            </div>
                        </div>
					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveEditContactsBtn">更新</button>
				</div>
			</div>
		</div>
	</div>





	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>联系人列表</h3>
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
				      <div class="input-group-addon">姓名</div>
				      <input class="form-control" type="text" id="fullName">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">客户名称</div>
				      <input class="form-control" type="text" id="CustomerId">
				    </div>
				  </div>

				  <br>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">来源</div>
				      <select class="form-control" id="source">
						  <option></option>
						  <c:forEach items="${sources}" var="sou">
							  <option value="${sou.id}">${sou.value}</option>
						  </c:forEach>
						</select>
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">生日</div>
				      <input class="form-control" type="text" id="createTime">
				    </div>
				  </div>

				  <button type="button" class="btn btn-default" id="queryContactsByConditionBtn">查询</button>

				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 10px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#createContactsModal"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editContactsBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>


			</div>
			<div style="position: relative;top: 20px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="cheAll"/></td>
							<td>姓名</td>
							<td>客户名称</td>
							<td>所有者</td>
							<td>来源</td>
							<td>生日</td>
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
