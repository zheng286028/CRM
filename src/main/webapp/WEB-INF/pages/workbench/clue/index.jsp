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

<script type="text/javascript">

	$(function(){
		//给创建按钮绑定单击事件
		$("#insertBtn").click(function () {
			//重置表单
			$("#formClue").get(0).reset();
			//弹出模态创建
			$("#createClueModal").modal("show");
		});
		//给添加保存按钮绑定单击事件
		$("#savaEditBtn").click(function () {
			//收集参数
			var owner=$.trim($("#create-clueOwner").val());
			var company=$.trim($("#create-company").val());
			var appellation=$.trim($("#create-call").val());
			var fullname=$.trim($("#create-surname").val());
			var job=$.trim($("#create-job").val());
			var email=$.trim($("#create-email").val());
			var phone=$.trim($("#create-phone").val());//公司座机
			var website=$.trim($("#create-website").val());
			var mphone=$.trim($("#create-mphone").val());
			var state=$.trim($("#create-status").val());
			var source=$.trim($("#create-source").val());
			var description=$.trim($("#create-describe").val());
			var contactSummary=$.trim($("#create-contactSummary").val());
			var nextContactTime=$("#create-nextContactTime").val();
			var address=$.trim($("#create-address").val());
			//表单验证
			if(owner==""){
				alert("所有者不能为空");
				return;
			}
			if(company==""){
				alert("公司不能为空");
				return;
			}
			if(fullname==""){
				alert("姓名不能为空");
				return;
			}
			var regmon=/^(([1-9]\d*)|0)$/;
			var regpho=/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
			var regEma=/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
			if(!regpho.test(phone)){
				alert("请输入正确的座机号码");
				return;
			}
			if(!regmon.test(mphone)){
				alert("请输入正确的手机号码");
				return;
			}
			if(!regEma.test(email)){
				alert("请输入正确的邮箱格式");
				return;
			}
			//发送请求
			$.ajax(   {
				url:"workbench/clue/insertClue.do",
				data:{
					owner:owner,
					company:company,
					appellation:appellation,
					fullname:fullname,
					job:job,
					website:website,
					phone:phone,
					mphone:mphone,
					state:state,
					source:source,
					description:description,
					contactSummary:contactSummary,
					nextContactTime:nextContactTime,
					address:address,
					email:email
				},
				type:"post",
				dataType:"json",
				success:function (resp) {
					if(resp.code=="1"){
						//成功，查询数据
						selectClueByPageAndCondition(1,$("#currentPage").bs_pagination('getOption','rowsPerPage'));
						$("#createClueModal").modal("hide");
					}else{
						alert(resp.message);
					}
				}
			});
		});
		//页面刷新调用查询方法
		selectClueByPageAndCondition(1,3);
		//给查询按钮绑定单击事件
		$("#selectBtn").click(function () {
			selectClueByPageAndCondition(1,$("#currentPage").bs_pagination('getOption','rowsPerPage'));
		});
		//复选框选中事件
		$("#checkAll").click(function () {
			$("#Tbody input[type='checkbox']").prop('checked',this.checked);
		});
		//给所有生成的的复选框绑定单击事件
		$("Tbody").on('click',"input[type='checkbox']",function () {
			if($("#Tbody input[type='checkbox']").size()==$("#Tbody input[type='checkbox']:checked").size()){
				$("#checkAll").prop('checked',true);
			}else{
				$("#checkAll").prop('checked',false);
			}
		});
		//给删除按钮绑定单击事件
		$("#deleteClueBtn").click(function () {
			//收集参数
			var ids=$("#Tbody input[type='checkbox']:checked");
			if(ids.size()==0){
				alert("请选择你要删除的数据")
				return;
			}
			if(window.confirm("确定删除吗？")){
				//收集id
				var id="";
				$.each(ids,function (index,obj) {
					id+=obj.value+',';
				});
				//去掉多余字符
				id="ids="+id.substring(0,id.length-1);
				//发送请求
				$.ajax({
					url:"workbench/clue/deleteClueByIds.do",
					data:id,
					type:'post',
					dataType:'json',
					success:function (resp) {
						if(resp.code=='1'){
						selectClueByPageAndCondition($("#currentPage").bs_pagination('getOption','currentPage'),$("#currentPage").bs_pagination('getOption','rowsPerPage'));
						}else
							alert(resp.message);
					}
				});
			}
		});
		//给修改按钮绑定单击事件
		$("#updateBtn").click(function () {
			//验证
			var ids=$("#Tbody input[type='checkbox']:checked");
			if(ids.size()==0){
				alert("请选择你要修改的数据");
				return;
			}
			if(ids.size()>1){
				alert("一次只能修改一条数据");
				return;
			}
			var id=ids.val();
			//发送请求
			$.ajax({
				url:"workbench/clue/selectById.do",
				data:{id:id},
				type:"post",
				dataType:"json",
				success:function (resp) {
					//回显数据
					$("#edit-clueOwner").val(resp.owner);
					$("#edit-company").val(resp.company);
					$("#edit-call").val(resp.appellation);
					$("#edit-surname").val(resp.fullname);
					$("#edit-job").val(resp.job);
					$("#edit-email").val(resp.email);
					$("#edit-phone").val(resp.phone);
					$("#edit-website").val(resp.website);
					$("#edit-mphone").val(resp.mphone);
					$("#edit-status").val(resp.state);
					$("#edit-contactSummary").val(resp.contactSummary);
					$("#edit-nextContactTime").val(resp.nextContactTime);
					$("#edit-address").val(resp.address);
					$("#savaEditId").val(resp.id);
					$("#edit-source").val(resp.source);
					$("#edit-describe").val(resp.description)
					//打开模态窗口
					$("#editClueModal").modal("show");
				}
			});
		});
		//给修改按钮绑定单击事件
		$("#savaEditClueBtn").click(function () {
			//收集参数
			var owner=$("#edit-clueOwner").val();
			var company=$("#edit-company").val();
			var appellation=$("#edit-call").val();
			var fullname=$("#edit-surname").val();
			var job=$("#edit-job").val();
			var email=$("#edit-email").val();
			var phone=$("#edit-phone").val();
			var website=$("#edit-website").val();
			var mphone=$("#edit-mphone").val();
			var status=$("#edit-status").val();
			var contactSummary=$("#edit-contactSummary").val();
			var nextContactTime=$("#edit-nextContactTime").val();
			var address=$("#edit-address").val();
			var id=$("#savaEditId").val();
			var source=$("#edit-source").val();
			var description=$("#edit-describe").val()
			//表单验证
			if(owner==""){
				alert("所有者不能为空");
				return;
			}
			if(company==""){
				alert("公司不能为空");
				return;
			}
			if(fullname==""){
				alert("姓名不能为空");
				return;
			}
			var regmon=/^(([1-9]\d*)|0)$/;
			var regpho=/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
			var regEma=/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
			if(!regpho.test(phone)){
				alert("请输入正确的座机号码");
				return;
			}
			if(!regmon.test(mphone)){
				alert("请输入正确的手机号码");
				return;
			}
			if(!regEma.test(email)){
				alert("请输入正确的邮箱格式");
				return;
			}
			//发送请求
			$.ajax({
				url:'workbench/clue/savaEditClueById.do',
				data:{
					owner:owner,
					company:company,
					appellation:appellation,
					fullname:fullname,
					job:job,
					email:email,
					phone:phone,
					website:website,
					mphone:mphone,
					state:status,
					contactSummary:contactSummary,
					nextContactTime:nextContactTime,
					address:address,
					source:source,
					description:description,
					id:id
				},
				type:'post',
				dataType:'json',
				success:function (resp) {
					if(resp.code=="1"){
						$("#editClueModal").modal("hide");
						selectClueByPageAndCondition($("#currentPage").bs_pagination('getOption','currentPage'),$("#currentPage").bs_pagination('getOption','rowsPerPage'))
					}else{
						alert(resp.message);
					}
				}
			});
		});
	});

	//分页条件查询
	function selectClueByPageAndCondition(currentPage,pageSize) {
		//收集参数
		var fullname=$("#clue-clueName").val();
		var company=$("#clue-company").val();
		var phone=$("#clue-phone").val();
		var source=$("#clue-source").val();
		var owner=$("#clue-owner").val();
		var mphone=$("#clue-mphone").val();
		var state=$("#clue-state").val();
		//发送请求
		$.ajax({
			url:"workbench/clue/selectClueByPageAndCondition.do",
			data:{
				fullname:fullname,
				company:company,
				phone:phone,
				source:source,
				owner:owner,
				mphone:mphone,
				state:state,
				currentPage:currentPage,
				pageSize:pageSize
			},
			type:"post",
			dataType:"json",
			success:function (resp) {
				var htmlStr="";
				//遍历clueList拼接数据
				$.each(resp.clueList,function (index,obj) {
					htmlStr+="<tr>";
					htmlStr+="<td><input type=\"checkbox\" value="+obj.id+"></td>";
					htmlStr+="<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/clue/queryClueAndClueRemarkById.do?id="+obj.id+"';\">"+obj.fullname+obj.appellation+"</a></td>";
					htmlStr+="<td>"+obj.company+"</td>";
					htmlStr+="<td>"+obj.phone+"</td>";
					htmlStr+="<td>"+obj.mphone+"</td>";
					htmlStr+="<td>"+obj.source+"</td>";
					htmlStr+="<td>"+obj.owner+"</td>";
					htmlStr+="<td>"+obj.state+"</td>";
					htmlStr+="</tr>";
				});
				//覆盖数据
				$("#Tbody").html(htmlStr);
				//多选框去掉
				$("#checkAll").prop('checked',false);
				//计算总页数
				var totalPages=1;
				if(resp.totalCount%pageSize==0){
					totalPages=resp.totalCount/pageSize;
				}else{
					totalPages=parseInt(resp.totalCount/pageSize)+1;
				}
				//分页插件
				$("#currentPage").bs_pagination({
					currentPage:currentPage,//当前页
					rowsPerPage:pageSize,//当前页显示页数
					totalRows:resp.totalCount,//总记录数
					totalPages:totalPages,//总页数
					visiblePageLinks:5,//最多显示的卡片数
					showGoToPage:true,//是否显示跳转页面
					showRowsPerPage:true, //是否显示每页显示条数
					showRowsInfo:true,//是否显示记录的信息
					//切换页号触发事件
					//每次返回切换页号之后的currentPage,pageSize
					onChangePage:function (event,pageObj) {
						selectClueByPageAndCondition(pageObj.currentPage,pageObj.rowsPerPage);
					}
				});
			}
		});
	}

</script>
</head>
<body>

	<!-- 创建线索的模态窗口 -->
	<div class="modal fade" id="createClueModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">创建线索</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="formClue">

						<div class="form-group">
							<label for="create-clueOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-clueOwner">
									<c:forEach items="${requestScope.user}" var="u">
										<option value="${u.id}">${u.name}</option>
									</c:forEach>
								</select>
							</div>
							<label for="create-company" class="col-sm-2 control-label">公司<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-company">
							</div>
						</div>

						<div class="form-group">
							<label for="create-call" class="col-sm-2 control-label">称呼</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-call">
									<option></option>
								 <c:forEach items="${requestScope.appellation}" var="appe">
									 <option value="${appe.id}">${appe.value}</option>
								 </c:forEach>
								</select>
							</div>
							<label for="create-surname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-surname">
							</div>
						</div>

						<div class="form-group">
							<label for="create-job" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-job">
							</div>
							<label for="create-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-email">
							</div>
						</div>

						<div class="form-group">
							<label for="create-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-phone">
							</div>
							<label for="create-website" class="col-sm-2 control-label">公司网站</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-website">
							</div>
						</div>

						<div class="form-group">
							<label for="create-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-mphone">
							</div>
							<label for="create-status" class="col-sm-2 control-label">线索状态</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-status">
									<option></option>
								<c:forEach items="${requestScope.clueState}" var="state">
									<option value="${state.id}">${state.value}</option>
								</c:forEach>
								</select>
							</div>
						</div>

						<div class="form-group">
							<label for="create-source" class="col-sm-2 control-label">线索来源</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-source">
									<option></option>
									<c:forEach items="${requestScope.source}" var="source">
										<option value="${source.id}">${source.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>


						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">线索描述</label>
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
					<button type="button" class="btn btn-primary" id="savaEditBtn">保存</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 修改线索的模态窗口 -->
	<div class="modal fade" id="editClueModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">修改线索</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
						<%--修改表单id--%>
						<input type="hidden" id="savaEditId">
						<div class="form-group">
							<label for="edit-clueOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-clueOwner">
									<c:forEach items="${requestScope.user}" var="u">
										<option value="${u.id}">${u.name}</option>
									</c:forEach>
								</select>
							</div>
							<label for="edit-company" class="col-sm-2 control-label">公司<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-company" value="动力节点">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-call" class="col-sm-2 control-label">称呼</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-call">
								<c:forEach items="${requestScope.appellation}" var="al">
									<option value="${al.id}">${al.value}</option>
								</c:forEach>
								</select>
							</div>
							<label for="edit-surname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-surname" value="李四">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-job" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-job" value="CTO">
							</div>
							<label for="edit-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-email" value="lisi@bjpowernode.com">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-phone" value="010-84846003">
							</div>
							<label for="edit-website" class="col-sm-2 control-label">公司网站</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-website" value="http://www.bjpowernode.com">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-mphone" value="12345678901">
							</div>
							<label for="edit-status" class="col-sm-2 control-label">线索状态</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-status">
									<c:forEach items="${requestScope.clueState}" var="cs">
										<option value="${cs.id}">${cs.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>

							<div class="form-group">
								<label for="create-source" class="col-sm-2 control-label">线索来源</label>
								<div class="col-sm-10" style="width: 300px;">
									<select class="form-control" id="edit-source">
										<c:forEach items="${requestScope.source}" var="source">
											<option value="${source.id}">${source.value}</option>
										</c:forEach>
									</select>
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
									<textarea class="form-control" rows="3" id="edit-contactSummary">这个线索即将被转换</textarea>
								</div>
							</div>
							<div class="form-group">
								<label for="edit-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
								<div class="col-sm-10" style="width: 300px;">
									<input type="date" class="form-control" id="edit-nextContactTime" value="2017-05-01">
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
					<button type="button" class="btn btn-primary" id="savaEditClueBtn">更新</button>
				</div>
			</div>
		</div>
	</div>




	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>线索列表</h3>
			</div>
		</div>
	</div>

	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">

		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">

			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">姓名</div>
				      <input class="form-control" type="text" id="clue-clueName">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司</div>
				      <input class="form-control" type="text" id="clue-company">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司座机</div>
				      <input class="form-control" type="text" id="clue-phone">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">线索来源</div>
					  <select class="form-control" id="clue-source">
						  <option></option>
						  <c:forEach items="${requestScope.source}" var="se">
							  <option value="${se.id}">${se.value}</option>
						  </c:forEach>
					  </select>
				    </div>
				  </div>

				  <br>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="clue-owner">
				    </div>
				  </div>



				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">手机</div>
				      <input class="form-control" type="text" id="clue-mphone">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">线索状态</div>
					  <select class="form-control" id="clue-state">
						  <option></option>
					 	<c:forEach items="${requestScope.clueState}" var="state">
							<option value="${state.id}">${state.value}</option>
						</c:forEach>
					  </select>
				    </div>
				  </div>

				  <button type="button" class="btn btn-default" id="selectBtn">查询</button>

				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 40px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" data-target="#createClueModal" id="insertBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" data-target="#editClueModal" id="updateBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteClueBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>


			</div>
			<div style="position: relative;top: 50px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkAll"/></td>
							<td>姓名</td>
							<td>公司</td>
							<td>公司座机</td>
							<td>手机</td>
							<td>线索来源</td>
							<td>所有者</td>
							<td>线索状态</td>
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
