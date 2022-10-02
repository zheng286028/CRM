<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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

	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;

	$(function(){
		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});

		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
		});

		$("#remarkForm").on("mouseover",".remarkDiv",function () {
			$(this).children("div").children("div").show();
		});

		$("#remarkForm").on("mouseout",".remarkDiv",function () {
			$(this).children("div").children("div").hide();
		});

		$("#remarkForm").on("mouseover",".myHref",function () {
			$(this).children("span").css("color","red");
		});

		$("#remarkForm").on("mouseout",".myHref",function () {
			$(this).children("span").css("color","#E6E6E6");
		});

		//给“保存”按钮添加单击事件
		$("#SaveBtn").click(function () {
			//收集参数
			var noteContent=$("#remark").val();
			var customerId='${customer.id}'
			//表单验证
			if(noteContent==""){
				alert("内容不可为空");
				return;
			}
			//发送请求
			$.ajax({
				url:'workbench/customer/insertCustomerRemark.do',
				data:{
					noteContent:noteContent,
					customerId:customerId
				},
				type:'post',
				dataType:'json',
				success:function (resp) {
					var strHtml="";
					if(resp.code=="1"){
						//清空内容
						$("#remark").val("");
						//拼接
						strHtml+="<div id='div_"+resp.retData.id+"' class=\"remarkDiv\" style=\"height: 60px;\">";
						strHtml+="<img title=${customer.createBy} src=\"image/user-thumbnail.png\" style=\"width: 30px; height:30px;\">";
						strHtml+="<div style=\"position: relative; top: -40px; left: 40px;\" >";
						strHtml+="<h5>"+resp.retData.noteContent+"</h5>";
						strHtml+="<font color=\"gray\">客户</font> <font color=\"gray\">-</font> <b>${customer.name}</b> <small style=\"color: gray;\"> "+resp.retData.createTime+" 由${sessionScope.sessionUser.name}创建</small>";
						strHtml+="<div style=\"position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;\">";
						strHtml+="<a class=\"myHref\" remarkId=\""+resp.retData.id+"\" name='updateA' href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-edit\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
						strHtml+="&nbsp;&nbsp;&nbsp;&nbsp;";
						strHtml+="<a class=\"myHref\" remarkId=\""+resp.retData.id+"\" name='deleteA' href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-remove\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
						strHtml+="</div>";
						strHtml+="</div>";
						strHtml+="</div>";
						//追加
						$("#remarkDiv").before(strHtml);
					}
				}
			});
		});
		//给“删除图标”绑定单击事件
		$("#remarkForm").on("click","a[name='deleteA']",function () {
			var id=$(this).attr("remarkId");
			//确认删除
			if(window.confirm("确定删除吗？")){
				//发送请求
				$.ajax({
					url:'workbench/customer/deleteCustomerRemarkById.do',
					data:{id:id},
					type:'post',
					dataType:'json',
					success:function (resp) {
						if(resp.code=="1"){
							//删除指定备注
							$("#div_"+id).remove();
						}
					}
				});
			}
		});
		//给“修改”图标绑定单击事件
		$("#remarkForm").on("click","a[name='updateA']",function () {
			//重置数据
			$("#noteContent").val("");
			//回显内容
			var id=$(this).attr("remarkId");
			var noteContent=$("#div_"+id+" h5").text()
			$("#noteContent").val(noteContent);
			$("#remarkId").val(id)
			//弹出模态窗口
			$("#editRemarkModal").modal("show");
		});
		//给“保存按钮绑定单击事件”
		$("#updateRemarkBtn").click(function () {
			//收集参数
			var noteContent=$.trim($("#noteContent").val());
			var id=$("#remarkId").val();
			//表单验证
			if(noteContent==""){
				alert("内容不可为空！");
				return;
			}
			//发送请求
			$.ajax({
				url:'workbench/customer/updateCustomerRemarkById.do',
				data:{noteContent:noteContent,id:id},
				type:'post',
				dataType:'json',
				success:function (resp) {
					if(resp.code=="1"){
						//覆盖数据
						$("#div_"+id+" h5").html(noteContent);
						//关闭模态窗口
						$("#editRemarkModal").modal("hide");
					}else{
						alert(resp.message);
					}
				}
			});
		});
		//给联系人的客户名称绑定自动补全
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
		//给新建联系人绑定单击事件
		$("#addContacts").click(function () {
			//弹出模态窗口
			$("#createContactsModal").modal("show");
		});
		//给保存按钮添加单击事件
		$("#saveCreateContactsBtn").click(function () {
			//收集参数
			var owner=$("#create-contactsOwner").val();
			var source=$("#create-clueSource").val();
			var fullName=$("#create-surname").val();
			var appellation=$("#create-call").val();
			var job=$("#create-job").val();
			var mphone=$("#create-mphone").val();
			var email=$("#create-email").val();
			var createTime=$("#create-birth").val();
			var customerId=$("#create-customerName").val();
			var description=$("#create-describe").val();
			var contactSummary=$("#create-contactSummary").val();
			var nextContactTime=$("#create-nextContactTime").val();
			var address=$("#create-address").val();
			//表单验证
			if(owner==""){
				alert("用户名不可为空");
				return;
			}
			//发送请求
			$.ajax({
				url:'workbench/customer/insertContacts.do',
				data:{
					owner:owner,
					source:source,
					fullName:fullName,
					appellation:appellation,
					job:job,
					mphone:mphone,
					email:email,
					createTime:createTime,
					CustomerName:customerId,
					description:description,
					contactSummary:contactSummary,
					nextContactTime:nextContactTime,
					address:address
				},
				type:'post',
				dataType:'json',
				success:function (resp) {
					if(resp.code=="1"){
						//拼接
						var strHtml="";
						strHtml+="<tr id=\"tr_"+resp.retData.id+"\">";
						strHtml+="<td><a href=\"contacts/detail.jsp\" style=\"text-decoration: none;\"></a>"+customerId+"</td>";
						strHtml+="<td>"+resp.retData.email+"</td>";
						strHtml+="<td>"+resp.retData.mphone+"</td>";
						strHtml+="<td><a href=\"javascript:void(0);\" name=\"deleteA\" contactsId='"+resp.retData.id+"' data-target=\"#removeContactsModal\" style=\"text-decoration: none;\"><span class=\"glyphicon glyphicon-remove\"></span>删除</a></td>";
						strHtml+="</tr>";
						//覆盖
						$("#Tbody").append(strHtml);
						//关闭模态窗口
						$("#createContactsModal").modal("hide");
					}else{
						alert(resp.message);
					}
				}
			});
		});
		//给删除联系人绑定单击事件
		$("#Tbody").on('click',"a[name='deleteA']",function () {
			//收集参数
			var id=$(this).attr("contactsId");
			//弹出模态窗口
			$("#removeContactsModal").modal("show");
			//给删除按钮绑定单击事件
			$("#deleteContactsBtn").click(function () {
				//发送请求
				$.ajax({
					url:'workbench/customer/deleteContactsById.do',
					data:{id:id},
					type:'post',
					dataType:'json',
					success:function (resp) {
						if(resp.code=="1"){
							//删除指定的tr
							$("#tr_"+id).remove();
						}else{
							alert(resp.message);
						}
					}
				});
			});
		});
	});

</script>

</head>
<body>

	<!-- 删除联系人的模态窗口 -->
	<div class="modal fade" id="removeContactsModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 30%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">删除联系人</h4>
				</div>
				<div class="modal-body">
					<p>您确定要删除该联系人吗？</p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-danger" id="deleteContactsBtn" data-dismiss="modal">删除</button>
				</div>
			</div>
		</div>
	</div>

    <!-- 删除交易的模态窗口 -->
    <div class="modal fade" id="removeTransactionModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 30%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title">删除交易</h4>
                </div>
                <div class="modal-body">
                    <p>您确定要删除该交易吗？</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-danger" data-dismiss="modal">删除</button>
                </div>
            </div>
        </div>
    </div>

	<!-- 创建联系人的模态窗口 -->
	<div class="modal fade" id="createContactsModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" onclick="$('#createContactsModal').modal('hide');">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建联系人</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">

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
								<c:forEach items="${sources}" var="sours">
									<option value="${sours.id}">${sours.value}</option>
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
                                    <textarea class="form-control" rows="3" id="create-contactSummary">这个线索即将被转换</textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
                                <div class="col-sm-10" style="width: 300px;">
                                    <input type="text" class="form-control" id="create-nextContactTime" value="2017-05-01">
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
					<button type="button" class="btn btn-primary" id="saveCreateContactsBtn">保存</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 修改备注的模态窗口 -->
	<div class="modal fade" id="editRemarkModal" role="dialog">
		<%-- 备注的id --%>
		<input type="hidden" id="remarkId">
		<div class="modal-dialog" role="document" style="width: 40%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">修改备注</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
						<div class="form-group">
							<label for="noteContent" class="col-sm-2 control-label">内容</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="noteContent"></textarea>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
				</div>
			</div>
		</div>
	</div>


	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>

	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>${customer.name} <small><a href="${customer.website}" target="_blank">${customer.website}</a></small></h3>
		</div>
		<div style="position: relative; height: 50px; width: 500px;  top: -72px; left: 700px;">
			<button type="button" class="btn btn-default" data-toggle="modal" data-target="#editCustomerModal"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
			<button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>

	<br/>
	<br/>
	<br/>

	<!-- 详细信息 -->
	<div style="position: relative; top: -70px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${customer.owner}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${customer.name}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">公司网站</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${customer.website}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">公司座机</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${customer.phone}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${customer.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${customer.createTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${customer.editBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${customer.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
        <div style="position: relative; left: 40px; height: 30px; top: 40px;">
            <div style="width: 300px; color: gray;">联系纪要</div>
            <div style="width: 630px;position: relative; left: 200px; top: -20px;">
                <b>
					${customer.contactSummary}
                </b>
            </div>
            <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
        </div>
        <div style="position: relative; left: 40px; height: 30px; top: 50px;">
            <div style="width: 300px; color: gray;">下次联系时间</div>
            <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${customer.nextContactTime}</b></div>
            <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px; "></div>
        </div>
		<div style="position: relative; left: 40px; height: 30px; top: 60px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${customer.description}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
        <div style="position: relative; left: 40px; height: 30px; top: 70px;">
            <div style="width: 300px; color: gray;">详细地址</div>
            <div style="width: 630px;position: relative; left: 200px; top: -20px;">
                <b>
					${customer.address}
                </b>
            </div>
            <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
        </div>
	</div>

	<!-- 备注 -->
	<div id="remarkForm" style="position: relative; top: 10px; left: 40px;">
		<div class="page-header">
			<h4>备注</h4>
		</div>

		<!-- 备注1 -->
		<c:forEach items="${crList}" var="crlist">
			<div id="div_${crlist.id}" class="remarkDiv" style="height: 60px;">
				<img title="${crlist.createBy}" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
				<div style="position: relative; top: -40px; left: 40px;" >
					<h5>${crlist.noteContent}</h5>
					<font color="gray">市场活动</font> <font color="gray">-</font> <b>${customer.name}</b> <small style="color: gray;"> ${crlist.editFlag=='1'?crlist.editTime:crlist.createTime} 由${crlist.editFlag=='1'?crlist.editBy:crlist.createBy}${obj.editFlag=='1'?'修改':'创建'}</small>
					<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
						<a class="myHref" href="javascript:void(0);" name="updateA" remarkId="${crlist.id}" name="updateA"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<a class="myHref" href="javascript:void(0);" name="deleteA" remarkId="${crlist.id}" name="deleteA"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
					</div>
				</div>
			</div>
		</c:forEach>

		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button type="button" class="btn btn-primary" id="SaveBtn">保存</button>
				</p>
			</form>
		</div>
	</div>

	<!-- 交易 -->
	<div>
		<div style="position: relative; top: 20px; left: 40px;">
			<div class="page-header">
				<h4>交易</h4>
			</div>
			<div style="position: relative;top: 0px;">
				<table id="activityTable2" class="table table-hover" style="width: 900px;">
					<thead>
						<tr style="color: #B3B3B3;">
							<td>名称</td>
							<td>金额</td>
							<td>阶段</td>
							<td>可能性</td>
							<td>预计成交日期</td>
							<td>类型</td>
							<td></td>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${trans}" var="tr">
						<tr id="tr_${tr.id}">
							<td><a href="transaction/detail.html" style="text-decoration: none;">${tr.name}</a></td>
							<td>${tr.money}</td>
							<td>${tr.stage}</td>
							<td>90</td>
							<td>${tr.nextContactTime}</td>
							<td>${tr.type}</td>
							<td><a TranId="${tr.id}" name="deleteA" href="javascript:void(0);" data-toggle="modal" data-target="#removeTransactionModal" style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>删除</a></td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>

			<div>
				<a href="workbench/transaction/transactionSave.do" style="text-decoration: none;"><span class="glyphicon glyphicon-plus"></span>新建交易</a>
			</div>
		</div>
	</div>

	<!-- 联系人 -->
	<div>
		<div style="position: relative; top: 20px; left: 40px;">
			<div class="page-header">
				<h4>联系人</h4>
			</div>
			<div style="position: relative;top: 0px;">
				<table id="activityTable" class="table table-hover" style="width: 900px;">
					<thead>
						<tr style="color: #B3B3B3;">
							<td>名称</td>
							<td>邮箱</td>
							<td>手机</td>
							<td></td>
						</tr>
					</thead>
					<tbody id="Tbody">
					<c:forEach items="${contacts}" var="con">
						<tr id="tr_${con.id}">
							<td><a href="contacts/detail.html" style="text-decoration: none;">${con.customerId}</a></td>
							<td>${con.email}</td>
							<td>${con.mphone}</td>
							<td><a href="javascript:void(0);" name="deleteA" contactsId="${con.id}" data-target="#removeContactsModal" style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>删除</a></td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>

			<div>
				<a href="javascript:void(0);" id="addContacts" data-target="#createContactsModal" style="text-decoration: none;"><span class="glyphicon glyphicon-plus"></span>新建联系人</a>
			</div>
		</div>
	</div>

	<div style="height: 200px;"></div>
</body>
</html>
