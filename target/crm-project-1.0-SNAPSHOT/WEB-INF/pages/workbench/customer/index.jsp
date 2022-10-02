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

<script type="text/javascript">

	$(function(){

		//定制字段
		$("#definedColumns > li").click(function(e) {
			//防止下拉菜单消失
	        e.stopPropagation();
	    });
		//页面刷新查询数据
		selectCustomerByPageAndCondition(1,3);
		//给查询按钮绑定单击事件
		$("#selectBtn").click(function () {
			selectCustomerByPageAndCondition(1,$("#currentPage")
					.bs_pagination("getOption","rowsPerPage"));
		});
		//给创建按钮绑定单击事件
		$("#insertBtn").click(function () {
			//重置表单
			$("#formCustomer").get(0).reset();
			//打开模态窗口
			$("#createCustomerModal").modal("show");
		});
		//给创建表单的保存按钮绑定单击事件
		$("#savaInsertBtn").click(function () {
			//收集参数
			var owner = $("#create-Owner").val();
			var name = $.trim($("#create-Name").val());
			var website = $.trim($("#create-website").val());
			var phone = $.trim($("#create-phone").val());
			var description = $.trim($("#create-describe").val());
			var contactSummary = $.trim($("#create-contactSummary").val());
			var nextContactTime = $.trim($("#create-nextContactTime").val());
			var address = $.trim($("#create-address").val());
			//表单验证
			if(name==""){
				alert("名称不可为空");
				return;
			}
			//发送请求
			$.ajax({
				url:'workbench/customer/insertCustomer.do',
				data:{
					owner:owner,
					name:name,
					website:website,
					phone:phone,
					description:description,
					contactSummary:contactSummary,
					nextContactTime:nextContactTime,
					address:address,
				},
				type:'post',
				dataType:'json',
				success:function (resp) {
					if(resp.code=="1"){
						//刷新页面
						selectCustomerByPageAndCondition(1,$("#currentPage").bs_pagination("getOption","rowsPerPage"));
						//关闭模态窗口
						$("#createCustomerModal").modal("hide");
					}else{
						alert(resp.message);
					}
				}
			});
		});
		//给复选框绑定单击事件
		$("#cheAll").click(function () {
			$("#Tbody input[type='checkbox']").prop('checked',this.checked);
		});
		//给全部的复选框绑定单击事件
		$("#Tbody").on('click','input[type="checkbox"]',function () {
			if($("#Tbody input[type='checkbox']").size()==$("#Tbody input[type='checkbox']:checked").size()){
				$("#cheAll").prop('checked',true);
			}else{
				$("#cheAll").prop('checked',false);
			}
		});
		//给修改按钮绑定单击事件
		$("#editSaveCustomerBtn").click(function () {
			//收集参数
			var customerId=$("#Tbody input[type='checkbox']:checked");
			if(customerId.size()==0){
				alert("请选择你要修改的数据");
				return;
			}
			if(customerId.size()>1){
				alert("一次只能修改一条数据");
				return;
			}
			//收集id
			var id ="id="+customerId.val();
			//发送请求
			$.ajax({
				url:'workbench/customer/selectCustomerById.do',
				data:id,
				type:'post',
				dataType:'json',
				success:function (resp) {
					//回显数据
					$("#edit-owner").val(resp.owner);
					$("#edit-Name").val(resp.name);
					$("#edit-website").val(resp.website);
					$("#edit-phone").val(resp.phone);
					$("#edit-describe").val(resp.description);
					$("#edit-contactSummary").val(resp.contactSummary);
					$("#edit-nextContactTime").val(resp.nextContactTime);
					$("#edit-address").val(resp.address);
					$("#editCustomerId").val(resp.id);
					//弹出模态窗口
					$("#editCustomerModal").modal("show");
				}
			});
		});
		//给修改保存按钮绑定单击事件
		$("#editSava").click(function () {
			//收集参数
			var owner=$("#edit-owner").val();
			var name=$("#edit-Name").val();
			var website=$("#edit-website").val();
			var phone=$("#edit-phone").val();
			var description=$("#edit-describe").val();
			var contactSummary=$("#edit-contactSummary").val();
			var nextContactTime=$("#edit-nextContactTime").val();
			var address=$("#edit-address").val();
			var id=$("#editCustomerId").val();
			//表单验证
			if(name==""){
				alert("名称不可为空");
				return;
			}
			//发送请求
			$.ajax({
				url:'workbench/customer/editSaveCustomerById.do',
				data:{
					owner:owner,
					name:name,
					website:website,
					phone:phone,
					description:description,
					contactSummary:contactSummary,
					nextContactTime:nextContactTime,
					address:address,
					id:id,
				},
				type:'post',
				dataType:'json',
				success:function (resp) {
					if(resp.code=="1"){
						//调用查询方法重新查询
						selectCustomerByPageAndCondition($("#currentPage").bs_pagination("getOption","currentPage"),$("#currentPage").bs_pagination("getOption","rowsPerPage"))
						//关闭模态窗口
						$("#editCustomerModal").modal("hide");
					}else{
						alert(resp.message);
					}
				}
			});
		});
		//给删除按钮绑定单击事件
		$("#deleteCustomerBtn").click(function () {
			//收集参数
			var ids=$("#Tbody input[type='checkbox']:checked");
			if(ids.size()==0){
				alert("请选择你要删除的数据");
				return;
			}
			if(window.confirm("确定删除吗？")){
				//遍历
				var id="";
				$.each(ids,function (index,obj) {
					id+=obj.value+",";
					//截取掉多余的字符
					id=id.substring(0,id.length-1);
				});
			}
			//发送请求
			$.ajax({
				url:'workbench/customer/deleteCustomerById.do',
				data:{id:id},
				type:'post',
				dataType:'json',
				success:function (resp) {
					if(resp.code=="1"){
						selectCustomerByPageAndCondition($("#currentPage").bs_pagination("getOption","currentPage"),$("#currentPage")
								.bs_pagination("getOption","rowsPerPage"));
					}else{
						alert(resp.message);
					}
				}
			});
		});
	});
	//分页条件查询
	function selectCustomerByPageAndCondition(currentPage,pageSize) {
		//收集参数
		var name=$("#name").val();
		var owner=$("#owner").val();
		var phone=$("#phone").val();
		var website=$("#website").val();
		//发送请求
		$.ajax({
			url:'workbench/customer/queryCustomerByPageAndCondition.do',
			data:{
				name:name,
				owner:owner,
				phone:phone,
				website:website,
				currentPage:currentPage,
				pageSize:pageSize
			},
			type:'post',
			dataType:'json',
			success:function (resp) {
				//遍历数据
				var strHtml="";
				$.each(resp.customers,function (index,obj) {
					strHtml+="<tr>";
					strHtml+="<td><input value='"+obj.id+"' type=\"checkbox\" /></td>";
					strHtml+="<td><a style=\"text-decoration: none; cursor: pointer;\" onclick='window.location.href=\"workbench/customer/customerDetail.do?id="+obj.id+"\"' \">"+obj.name+"</a></td>";
					strHtml+="<td>"+obj.owner+"</td>";
					strHtml+="<td>"+obj.phone+"</td>";
					strHtml+="<td>"+obj.website+"</td>";
					strHtml+="</tr>";
				});
				//覆盖数据
				$("#Tbody").html(strHtml);
				//关闭复选框
				$("#cheAll").prop('checked',false);
				//计算总页数
				var totalPage=1;
				if(resp.total%pageSize==0){
					totalPage=(resp.total/pageSize);
				}else{
					totalPage=parseInt(resp.total/pageSize)+1;
				}
				//分页工具
				//分页插件
				$("#currentPage").bs_pagination({
					currentPage:currentPage,//当前页
					rowsPerPage:pageSize,//当前页显示页数
					totalRows:resp.total,//总记录数
					totalPages:totalPage,//总页数
					visiblePageLinks:5,//最多显示的卡片数
					showGoToPage:true,//是否显示跳转页面
					showRowsPerPage:true, //是否显示每页显示条数
					showRowsInfo:true,//是否显示记录的信息
					//切换页号触发事件
					//每次返回切换页号之后的currentPage,pageSize
					onChangePage:function (event,pageObj) {
						selectCustomerByPageAndCondition(pageObj.currentPage,pageObj.rowsPerPage);
					},
				});
			}
		});
	}

</script>
</head>
<body>

	<!-- 创建客户的模态窗口 -->
	<div class="modal fade" id="createCustomerModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建客户</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="formCustomer">

						<div class="form-group">
							<label for="create-Owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-Owner">
								<c:forEach items="${requestScope.user}" var="u">
									<option value="${u.id}">${u.name}</option>
								</c:forEach>
								</select>
							</div>
							<label for="create-Name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-Name">
							</div>
						</div>

						<div class="form-group">
                            <label for="create-website" class="col-sm-2 control-label">公司网站</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-website">
                            </div>
							<label for="create-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-phone">
							</div>
						</div>
						<div class="form-group">
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
                                    <textarea class="form-control" rows="1" id="create-address"></textarea>
                                </div>
                            </div>
                        </div>
					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="savaInsertBtn">保存</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 修改客户的模态窗口 -->
	<div class="modal fade" id="editCustomerModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">修改客户</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
						<input type="hidden" id="editCustomerId">
						<div class="form-group">
							<label for="edit-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">
									<c:forEach items="${requestScope.user}" var="u">
										<option value="${u.id}">${u.name}</option>
									</c:forEach>
								</select>
							</div>
							<label for="edit-Name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-Name" value="动力节点">
							</div>
						</div>

						<div class="form-group">
                            <label for="edit-website" class="col-sm-2 control-label">公司网站</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-website" value="http://www.bjpowernode.com">
                            </div>
							<label for="edit-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-phone" value="010-84846003">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe"></textarea>
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
                                    <textarea class="form-control" rows="1" id="edit-address">北京大兴大族企业湾</textarea>
                                </div>
                            </div>
                        </div>
					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="editSava">更新</button>
				</div>
			</div>
		</div>
	</div>




	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>客户列表</h3>
			</div>
		</div>
	</div>

	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">

		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">

			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="name">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="owner">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司座机</div>
				      <input class="form-control" type="text" id="phone">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司网站</div>
				      <input class="form-control" type="text" id="website">
				    </div>
				  </div>

				  <button type="button" class="btn btn-default" id="selectBtn">查询</button>

				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="insertBtn" data-target="#createCustomerModal"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editSaveCustomerBtn" data-target="#editCustomerModal"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteCustomerBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>

			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="cheAll"/></td>
							<td>名称</td>
							<td>所有者</td>
							<td>公司座机</td>
							<td>公司网站</td>
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
