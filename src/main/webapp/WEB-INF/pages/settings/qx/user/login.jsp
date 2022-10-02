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
	$(function () {
		//给页面绑定键盘按下事件
		$(window).keydown(function (e) {
			if(e.keyCode==13){
				$("#loginBtn").click()
			}
		});

		//给登录按钮绑定单击事件
		$("#loginBtn").click(function () {
			//获取登录参数
			var loginAct =$.trim($("#loginAct").val());
			var loginPwd =$.trim($("#loginPwd").val());
			var isRemPwd =$("#isRemPwd").prop("checked");
			//表单验证
			if(loginAct==""){
				alert("用户名不能为空")
				return;
			}
			if(loginPwd==""){
				alert("密码不能为空")
				return;
			}
			//发送请求
			$.ajax({
				url:'settings/qx/user/Login.do',
				data:{
					loginAct:loginAct,
					loginPwd:loginPwd,
					isRemPwd:isRemPwd,
				},
				type:'post',
				dataType:'json',
				success:function (resp) {
					if(resp.code=="1"){
						//登录成功
						window.location.href="workbench/index.do";
					}else if(resp.code=="0"){
						//登录失败
						$("#msg").html(resp.message)
					}
				},
				beforeSend:function (){
					$("#msg").text("正在验证,请稍后");
					return true;
				}
			});
		});
	});
</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2022 郑子浪</span></div>
	</div>

	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1><h5 style="color: red">${sessionScope.msg}</h5>
			</div>
			<form action="#" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" value="${cookie.loginAct.value}" id="loginAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="loginPwd" value="${cookie.loginPwd.value}">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						<label>
							<%--判断cookie是否为空，如果不为空自动勾上记住我--%>
							<c:if test="${not empty cookie.loginPwd and not empty cookie.loginAct}">
								<input type="checkbox" id="isRemPwd" checked> 记住我
							</c:if>
								<%--判断cookie是否为空，如果为空把记住我去掉--%>
							<c:if test="${empty cookie.loginPwd or empty cookie.loginAct}">
								<input type="checkbox" id="isRemPwd"> 记住我
							</c:if>
						</label>
						&nbsp;&nbsp;
						<span id="msg" style="color: red"></span>
					</div>
					<button type="button" id="loginBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
