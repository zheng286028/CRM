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

		$("#divForm").on("mouseover",".remarkDiv",function () {
			$(this).children("div").children("div").show();
		});

		$("#divForm").on("mouseout",".remarkDiv",function () {
			$(this).children("div").children("div").hide();
		});

		$("#divForm").on("mouseover",".myHref",function () {
			$(this).children("span").css("color","red");
		});

		$("#divForm").on("mouseout",".myHref",function () {
			$(this).children("span").css("color","#E6E6E6");
		});

		//给”保存“按钮添加单击事件
		$("#addContactsRemarkBtn").click(function () {
			//收集参数
			var noteContent=$("#remark").val();
			var contactsId="${contacts.id}"
			//表单验证
			if(noteContent==""){
				alert("请输入内容！");
				return;
			}
			//发送请求
			$.ajax({
				url:'workbench/contacts/addContactsRemark.do',
				data:{
					noteContent:noteContent,
					contactsId:contactsId
				},
				type:'post',
				dataType:'json',
				success:function (resp) {
					var strHtml="";
					if(resp.code=="1"){
						//拼接
						strHtml+="<div class=\"remarkDiv\" id=\"div_"+resp.retData.id+"\" style=\"height: 60px;\">";
						strHtml+="<img title=\"${sessionScope.sessionUser.name}\" src=\"image/user-thumbnail.png\" style=\"width: 30px; height:30px;\">";
						strHtml+="<div style=\"position: relative; top: -40px; left: 40px;\" >";
						strHtml+="<h5>"+resp.retData.noteContent+"</h5>";
						strHtml+="<font color=\"gray\">联系人</font> <font color=\"gray\">-</font> <b>${contacts.fullname}${contacts.appellation}-${contacts.address}${contacts.customerId}</b> <small style=\"color: gray;\"> "+resp.retData.createTime+" 由${sessionScope.sessionUser.name}创建</small>";
						strHtml+="<div style=\"position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;\">";
						strHtml+="<a class=\"myHref\" contactsReId=\""+resp.retData.id+"\" name=\"updateA\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-edit\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
						strHtml+="&nbsp;&nbsp;&nbsp;&nbsp;";
						strHtml+="<a class=\"myHref\" contactsReId=\""+resp.retData.id+"\" name=\"deleteA\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-remove\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
						strHtml+="</div>";
						strHtml+="</div>";
						strHtml+="</div>";
						//追加
						$("#remarkDiv").before(strHtml);
						//清空内容
						$("#remark").val("");
					}else{
						alert(resp.message);
					}
				}
			});
		});
		//给“删除”标记绑定单击事件
		$("#divForm").on("click","a[name='deleteA']",function () {
			//收集参数
			var id=$(this).attr("contactsReId");
			//发送请求
			$.ajax({
				url:'workbench/contacts/removeContactsRemark.do',
				data:{id:id},
				type:'post',
				dataType:'json',
				success:function (resp) {
					if(resp.code=="1"){
						$("#div_"+id).remove();
					}else{
						alert(resp.message);
					}
				}
			});
		});
		//给“修改”按钮绑定单击事件
		$("#divForm").on("click","a[name='updateA']",function () {
			//清空内容
			$("#noteContent").val("");
			//收集数据
			var id=$(this).attr("contactsReId");
			var noteContent=$("#div_"+id+" h5").text();
			//回显数据
			$("#noteContent").val(noteContent);
			$("#remarkId").val(id);
			//弹出模态窗口
			$("#editRemarkModal").modal("show");
		});
		//给修改备注的“更新”按钮绑定单击事件
		$("#updateRemarkBtn").click(function () {
			//收集参数
			var noteContent=$("#noteContent").val();
			var id=$("#remarkId").val();
			//发送请求
			$.ajax({
				url:'workbench/contacts/saveEditContactsRemarkById.do',
				data:{
					noteContent:noteContent,
					id:id
				},
				type:'post',
				dataType:'json',
				success:function (resp) {
					if(resp.code=="1"){
						//内容覆盖
						$("#div_"+id+" h5").html(noteContent);
						//关闭模态窗口
						$("#editRemarkModal").modal("hide");
					}
				}
			});
		});
		//给关联市场活动绑定单击事件
		$("#relationActivity").click(function () {
			//复选框
			$("#cheAll").prop("checked",false);
			//清空表单，内容
			$("#Tbidy").html("");
			$("#searchActivity").val("");
			//弹出模态窗口
			$("#bundActivityModal").modal("show");
		});
		//给关联市场活动搜索表单绑定单击事件
		$("#searchActivity").keyup(function () {
			//收集参数
			var contactsId="${contacts.id}"
			var name = $("#searchActivity").val();
			//发送请求
			$.ajax({
				url:'workbench/contacts/inquireActivityByContactsId.do',
				data:{
					contactsId:contactsId,
					name:name
				},
				type:'post',
				dataType:'json',
				success:function (resp) {
					var strHtml="";
					//遍历
					$.each(resp,function (index,obj) {
						strHtml+="<tr id='tr_"+obj.id+"'>";
						strHtml+="<td><input type=\"checkbox\" value='"+obj.id+"'/></td>";
						strHtml+="<td>"+obj.name+"</td>";
						strHtml+="<td>"+obj.startDate+"</td>";
						strHtml+="<td>"+obj.endDate+"</td>";
						strHtml+="<td>"+obj.owner+"</td>";
						strHtml+="</tr>";
					});
					//覆盖
					$("#Tbidy").html(strHtml);
				}
			});
		});
		//给复选框绑定单击事件
		$("#cheAll").click(function () {
			//给全部复选绑定checked
			$("#Tbidy input[type='checkbox']").prop('checked',this.checked);
		});
		//给全部复选框绑定单击事件
		$("#Tbidy").on("click","input[type='checkbox']",function () {
			if($("#Tbidy input[type='checkbox']:checked").size()==$("#Tbidy input[type='checkbox']").size()){
				$("#cheAll").prop("checked",true);
			}else{
				$("#cheAll").prop("checked",false);
			}
		});
		//给"关联"按钮绑定单击事件
		$("#relationBtn").click(function () {
			//收集参数
			var contactsId="${contacts.id}";
			var idss=$("#Tbidy input[type='checkbox']:checked");
			if(idss.size()==0){
				alert("请选择你要关联的数据！");
				return;
			}
			var ids = "";
			//遍历，获取id
			$.each(idss,function (index,obj) {
				ids+=obj.value+",";
			});
			//截断多余字符
			ids=ids.substring(0,ids.length-1);
			//发送请求
			$.ajax({
				url:'workbench/contacts/inquireActivityContactsRelationByContactsId.do',
				data:{
					ids:ids,
					contactsId:contactsId
				},
				type:'post',
				dataType:'json',
				success:function (resp) {
					if(resp.code=="1"){
						var strHtml="";
						//遍历数据
						$.each(resp.retData,function (index,obj) {
							strHtml+="<tr id=\"tr_"+obj.id+"\">";
							strHtml+="<td>"+obj.name+"</td>";
							strHtml+="<td>"+obj.owner+"</td>";
							strHtml+="<td>"+obj.startDate+"</td>";
							strHtml+="<td>"+obj.endDate+"</td>";
							strHtml+="<td><a href=\"javascript:void(0);\" name='liftRelationBtn' value=\""+obj.id+"\" style=\"text-decoration: none;\"><span class=\"glyphicon glyphicon-remove\"></span>解除关联</a></td>";
							strHtml+="</tr>";
						});

						//追加
						$("#Tbody").append(strHtml);
						//关闭模态窗口
						$("#bundActivityModal").modal("hide");
					}else{
						alert(resp.message);
					}
				}
			});
		});
		//给“解除关联”绑定单击事件
		$("#Tbody").on("click","a",function () {
			if(window.confirm("确定解除吗？")){
				//收集参数
				var activityId=$(this).attr("value");
				var contactsId="${contacts.id}";
				//发送请求
				$.ajax({
					url:'workbench/contacts/liftContactsActivityRelationByActivityId.do',
					data:{
						activityId:activityId,
						contactsId:contactsId
					},
					type:'post',
					dataType:'json',
					success:function (resp) {
						if(resp.code=="1"){
							//删除指定市场活动
							$("#tr_"+activityId).remove();
						}else{
							alert(resp.message);
						}
					}
				});
			}
		});
	});

</script>

</head>
<body>

<!-- 修改市场活动备注的模态窗口 -->
<div class="modal fade" id="editRemarkModal" role="dialog">
	<%-- 备注的id --%>
	<input type="hidden" id="remarkId">
	<div class="modal-dialog" role="document" style="width: 40%;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">×</span>
				</button>
				<h4 class="modal-title" id="saveEditRemark">修改备注</h4>
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
	<!-- 解除联系人和市场活动关联的模态窗口 -->
	<div class="modal fade" id="unbundActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 30%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">解除关联</h4>
				</div>
				<div class="modal-body">
					<p>您确定要解除该关联关系吗？</p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-danger" data-dismiss="modal">解除</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 联系人和市场活动关联的模态窗口 -->
	<div class="modal fade" id="bundActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">关联市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" id="searchActivity" class="form-control" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable2" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td><input type="checkbox" id="cheAll"/></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
								<td></td>
							</tr>
						</thead>
						<tbody id="Tbidy">

						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="relationBtn">关联</button>
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
					<h4 class="modal-title" id="myModalLabel">修改联系人</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">

						<div class="form-group">
							<label for="edit-contactsOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-contactsOwner">
								  <option selected>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>
								</select>
							</div>
							<label for="edit-clueSource" class="col-sm-2 control-label">来源</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-clueSource">
								  <option></option>
								  <option selected>广告</option>
								  <option>推销电话</option>
								  <option>员工介绍</option>
								  <option>外部介绍</option>
								  <option>在线商场</option>
								  <option>合作伙伴</option>
								  <option>公开媒介</option>
								  <option>销售邮件</option>
								  <option>合作伙伴研讨会</option>
								  <option>内部研讨会</option>
								  <option>交易会</option>
								  <option>web下载</option>
								  <option>web调研</option>
								  <option>聊天</option>
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
								  <option selected>先生</option>
								  <option>夫人</option>
								  <option>女士</option>
								  <option>博士</option>
								  <option>教授</option>
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
								<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
								<div class="col-sm-10" style="width: 81%;">
									<textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
								<div class="col-sm-10" style="width: 300px;">
									<input type="text" class="form-control" id="create-nextContactTime">
								</div>
							</div>
						</div>

						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="edit-address1" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="edit-address1">北京大兴区大族企业湾</textarea>
                                </div>
                            </div>
                        </div>
					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal">更新</button>
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
			<h3>${contacts.fullname}${contacts.appellation} <small> - ${contacts.customerId}</small></h3>
		</div>
		<div style="position: relative; height: 50px; width: 500px;  top: -72px; left: 700px;">
			<button type="button" class="btn btn-default" data-toggle="modal" data-target="#editContactsModal"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
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
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${contacts.owner}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">来源</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${contacts.source}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">客户名称</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${contacts.customerId}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">姓名</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${contacts.fullname}${contacts.appellation}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">邮箱</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${contacts.email}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">手机</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${contacts.mphone}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">职位</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${contacts.job}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">生日</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>&nbsp;${contacts.createTime}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${contacts.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${contacts.createTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${contacts.editBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${contacts.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 60px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${contacts.description}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 70px;">
			<div style="width: 300px; color: gray;">联系纪要</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
				${contacts.contactSummary}	&nbsp;
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 80px;">
			<div style="width: 300px; color: gray;">下次联系时间</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>&nbsp;${contacts.nextContactTime}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
        <div style="position: relative; left: 40px; height: 30px; top: 90px;">
            <div style="width: 300px; color: gray;">详细地址</div>
            <div style="width: 630px;position: relative; left: 200px; top: -20px;">
                <b>
					${contacts.address}
                </b>
            </div>
            <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
        </div>
	</div>
	<!-- 备注 -->
	<div id="divForm" style="position: relative; top: 20px; left: 40px;">
		<div class="page-header">
			<h4>备注</h4>
		</div>

		<!-- 备注1 -->
		<c:forEach items="${requestScope.conReList}" var="con">
			<div class="remarkDiv" id="div_${con.id}" style="height: 60px;">
				<img title="${sessionScope.sessionUser.name}" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
				<div style="position: relative; top: -40px; left: 40px;" >
					<h5>${con.noteContent}</h5>
					<font color="gray">联系人</font> <font color="gray">-</font> <b>${contacts.fullname}${contacts.appellation}-${contacts.address}${contacts.customerId}</b> <small style="color: gray;"> ${con.editFlag=="1"?con.editTime:con.createTime} 由${con.createBy}${con.editFlag=="1"?'修改':'创建'}</small>
					<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
						<a class="myHref" contactsReId="${con.id}" name="updateA" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<a class="myHref" contactsReId="${con.id}" name="deleteA" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
					</div>
				</div>
			</div>
		</c:forEach>

		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button type="button" class="btn btn-primary" id="addContactsRemarkBtn">保存</button>
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
				<table id="activityTable3" class="table table-hover" style="width: 900px;">
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
								<td>${tr.possibility}</td>
								<td>${tr.nextContactTime}</td>
								<td>${tr.type}</td>
								<td><a TranId="${tr.id}" name="deleteA" href="javascript:void(0);" data-toggle="modal" data-target="#removeTransactionModal" style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>删除</a></td>
							</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>

			<div>
				<a href="transaction/save.jsp" style="text-decoration: none;"><span class="glyphicon glyphicon-plus"></span>新建交易</a>
			</div>
		</div>
	</div>

	<!-- 市场活动 -->
	<div>
		<div style="position: relative; top: 60px; left: 40px;">
			<div class="page-header">
				<h4>市场活动</h4>
			</div>
			<div style="position: relative;top: 0px;">
				<table id="activityTable" class="table table-hover" style="width: 900px;">
					<thead>
						<tr style="color: #B3B3B3;">
							<td>名称</td>
							<td>开始日期</td>
							<td>结束日期</td>
							<td>所有者</td>
							<td></td>
						</tr>
					</thead>
					<tbody id="Tbody">
					<c:forEach items="${activityList}" var="al">
						<tr id="tr_${al.id}">
							<td>${al.name}</td>
							<td>${al.startDate}</td>
							<td>${al.endDate}</td>
							<td>${al.owner}</td>
							<td><a href="javascript:void(0);" name="liftRelationBtn" value="${al.id}" style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>解除关联</a></td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>

			<div>
				<a href="javascript:void(0);" id="relationActivity" data-target="#bundActivityModal" style="text-decoration: none;"><span class="glyphicon glyphicon-plus"></span>关联市场活动</a>
			</div>
		</div>
	</div>


	<div style="height: 200px;"></div>
</body>
</html>
