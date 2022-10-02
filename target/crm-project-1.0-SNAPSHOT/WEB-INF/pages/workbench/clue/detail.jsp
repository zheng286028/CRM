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

		$("#remarkFor").on("mouseover",".remarkDiv",function () {
			$(this).children("div").children("div").show();
		});

		$("#remarkFor").on("mouseout",".remarkDiv",function () {
			$(this).children("div").children("div").hide();
		});

		$("#remarkFor").on("mouseover",".myHref",function () {
			$(this).children("span").css("color","red");
		});

		$("#remarkFor").on("mouseout",".myHref",function () {
			$(this).children("span").css("color","#E6E6E6");
		});
		//给保存按钮绑定键盘按下事件
		$(window).keydown(function (e) {
			if(cancelAndSaveBtnDefault==false && e.keyCode==13){
					$("#saveClueRemark").click();
			}
		});
		//给保存按钮添加单击事件
		$("#saveClueRemark").click(function () {
			//收集参数
			var clueId="${clue.id}";
			var noteContent=$.trim($("#remark").val());
			//表单验证
			if(noteContent==''){
				alert("内容不可为空");
				return;
			};
			//发送请求
			$.ajax({
				url:"workbench/clue/insertClueRemark.do",
				data:{clueId:clueId,noteContent:noteContent},
				type:"post",
				dataType:"json",
				success:function (resp) {
					if(resp.code=="1"){
						//清空内容
						$("#remark").val("");
						//刷新列表
						var strHtml="";
						strHtml+="<div class=\"remarkDiv\" id=\"div_"+resp.retData.id+"\" style=\"height: 60px;\">";
						strHtml+="<img title=\""+resp.retData.createBy+"\" src=\"image/user-thumbnail.png\" style=\"width: 30px; height:30px;\">";
						strHtml+="<div style=\"position: relative; top: -40px; left: 40px;\" >";
						strHtml+="<h5>"+resp.retData.noteContent+"</h5>";
						strHtml+="<font color=\"gray\">线索</font> <font color=\"gray\">-</font> <b>${clue.fullname}-${clue.company}</b> <small style=\"color: gray;\"> "+resp.retData.createTime+" 由${sessionScope.sessionUser.name}创建</small>";
						strHtml+="<div style=\"position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;\">";
						strHtml+="<a class=\"myHref\" name=\"updateA\" remarkId="+resp.retData.id+" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-edit\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
						strHtml+="&nbsp;&nbsp;&nbsp;&nbsp;";
						strHtml+="<a class=\"myHref\" name=\"deleteA\" remarkId="+resp.retData.id+" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-remove\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
						strHtml+="</div>";
						strHtml+="</div>";
						strHtml+="</div>";
						//追加
						$("#remarkDiv").before(strHtml);
					}else{
						alert(resp.message);
					}
				}
			});
		});
		//给删除按钮绑定单击事件
		$("#remarkFor").on("click","a[name=\"deleteA\"]",function () {
			var id = $(this).attr("remarkId");
			//发送请求
			$.ajax({
				url:'workbench/clue/deleteClueRemarkById.do',
				data:{id:id},
				type:'post',
				dataType:'json',
				success:function (resp) {
					if(resp.code=="1"){
						//删除指定备注区域
						$("#div_"+id).remove();
					}else{
						alert(resp.message);
					}
				}
			});
		});
		//给修改按钮绑定单击事件
		$("#remarkFor").on("click","a[name='updateA']",function () {
			//收集参数
			var id=$(this).attr("remarkId");
			var noteContent=$("#div_"+id+" h5").text();
			//回显数据
			$("#remarkId").val(id);
			$("#noteContent").val(noteContent);
			//显示模态窗口
			$("#editRemarkModal").modal("show");
		});
		//给修改备注更新按钮绑定单击事件
		$("#updateRemarkBtn").click(function () {
			//获取参数
			var id=$("#remarkId").val();
			var noteContent=$.trim($("#noteContent").val().replace(/\s/g,""));
			//表单验证
			if(noteContent==""){
				alert("内容不可为空！");
				return;
			}
			//发送请求
			$.ajax({
				url:'workbench/clue/savaEditClueRemark.do',
				data:{id:id,noteContent:noteContent},
				type:'post',
				dataType:'json',
				success:function (resp) {
					if(resp.code=="1"){
						//覆盖数据
						$("#div_"+id+" h5").html(resp.retData.noteContent);
						$("#div_"+id+" small").html(resp.retData.editTime+"由${sessionScope.sessionUser.name}修改");
						//关闭模态窗口
						$("#editRemarkModal").modal("hide");
					}else{
						alert(resp.message);
					}
				}
			});
		});
		//给关联市场活动按钮绑定单击事件
		$("#associationActivityBtn").click(function () {
			//重置表单
			$("#searchActivityNameText").val("");
			$("#activityBody").html("");
			//弹出模态窗口
			$("#bundModal").modal("show");
		});
		//给搜索框绑定键盘弹起事件
		$("#searchActivityNameText").keyup(function () {
			//收集参数
			var activityName=this.value;
			var clueId='${clue.id}';
			//表单验证
			if(activityName==""){
				return;
			}
			//发送请求
			$.ajax({
				url:"workbench/clue/queryActivityByActivityNameAndClueId.do",
				data:{activityName:activityName,clueId:clueId},
				type:'post',
				dataType:'json',
				success:function (resp) {
					//拼接市场活动
					var strHTMl="";
					$.each(resp,function (index,obj) {
						strHTMl+="<tr>;"
						strHTMl+="<td><input value=\""+obj.id+"\" type=\"checkbox\"/></td>;"
						strHTMl+="<td>"+obj.name+"</td>;"
						strHTMl+="<td>"+obj.startDate+"</td>;"
						strHTMl+="<td>"+obj.endDate+"</td>;"
						strHTMl+="<td>"+obj.owner+"</td>;"
						strHTMl+="</tr>;"
					});
					//覆盖数据
					$("#activityBody").html(strHTMl);
				}
			});
		});
		//给市场活动表单里的复选框绑定单击事件
		$("#checkAll").click(function () {
			$("#activityBody input[type='checkbox']").prop('checked',this.checked);
		});
		//给全部全选框绑定单击事件
		$("#activityBody").on("click","input[type='checkbox']",function () {
			if($("#activityBody input[type='checkbox']").size()==$("#activityBody input[type='checkbox']:checked").size()){
				$("#checkAll").prop('checked',true);
			}else{
				$("#checkAll").prop('checked',false);
			}
		});
		//给关联市场活动关联按钮绑定单击事件
		$("#relationActivityBtn").click(function () {
			//收集参数
			var relation=$("#activityBody input[type='checkbox']:checked");
			if(relation.size()==0){
				alert("请选择你要关联的市场活动！")
				return;
			}
			var ids="";
			$.each(relation,function () {
				ids+=this.value+",";
			});
			ids=ids.substring(0,ids.length-1);
			var clueId="${clue.id}";
			//发送请求
			$.ajax({
				url:'workbench/clue/insertActivityClueRelation.do',
				data:{
					activityId:ids,
					clueId:clueId
				},
				type:'post',
				dataType:'json',
				success:function (resp) {
					if(resp.code=="1"){
						//追加数据
						var strHtml="";
						$.each(resp.retData,function (index,obj) {
							strHtml+="<tr id=\"tr_"+obj.id+"\">";
							strHtml+="<td>"+obj.name+"</td>";
							strHtml+="<td>"+obj.startDate+"</td>";
							strHtml+="<td>"+obj.endDate+"</td>";
							strHtml+="<td>"+obj.owner+"</td>";
							strHtml+="<td><a href=\"javascript:void(0);\" value=\""+obj.id+"\" style=\"text-decoration: none;\"><span class=\"glyphicon glyphicon-remove\"></span>解除关联</a></td>";
							strHtml+="</tr>";
						});
						$("#Tbody").append(strHtml);
						//关闭模态窗口
						$("#bundModal").modal("hide");
						//清空复选框
						$("#checkAll").prop('checked',false);
					}else{
						alert(resp.message);
					}
				}
			});
		});
		//给解除关联按钮绑定单击事件
		$("#Tbody").on("click","a",function () {
			//确定弹框
			if(window.confirm("确定解除吗？")){
				//收集参数
				var clueId='${clue.id}';
				var activityId=$(this).attr("value");
				//发送请求
				$.ajax({
					url:'workbench/clue/removeClueActivityByAndClueIdAndActivityId.do',
					data:{
						clueId:clueId,
						activityId:activityId
					},
					type:'post',
					dataType:'json',
					success:function (resp) {
						if(resp.code=="1"){
							//删除指定的市场活动
							$("#tr_"+activityId).remove();
						}else{
							alert(resp.message);
						}
					}
				});
			}
		});
		//给”转换按钮“绑定单击事件
		$("#clueConvertBtn").click(function () {
			window.location.href="workbench/clue/clueConvert.do?id=${clue.id}";
		});
	});

</script>

</head>
<body>

	<!-- 关联市场活动的模态窗口 -->
	<div class="modal fade" id="bundModal" role="dialog">
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
						    <input type="text" id="searchActivityNameText" class="form-control" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td><input type="checkbox" id="checkAll"/></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
								<td></td>
							</tr>
						</thead>
						<tbody id="activityBody">

						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="relationActivityBtn">关联</button>
				</div>
			</div>
		</div>
	</div>

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
			<h3>${clue.fullname}${clue.appellation} <small>${clue.company}</small></h3>
		</div>
		<div style="position: relative; height: 50px; width: 500px;  top: -72px; left: 700px;">
			<button id="clueConvertBtn" type="button" class="btn btn-default";><span class="glyphicon glyphicon-retweet"></span> 转换</button>

		</div>
	</div>

	<br/>
	<br/>
	<br/>

	<!-- 详细信息 -->
	<div style="position: relative; top: -70px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.fullname}${clue.appellation}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.owner}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">公司</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.company}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">职位</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.job}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">邮箱</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.email}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">公司座机</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.phone}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">公司网站</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.website}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">手机</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.mphone}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">线索状态</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.state}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">线索来源</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.source}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${clue.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${clue.createTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 60px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${clue.editBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${clue.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 70px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${clue.description}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 80px;">
			<div style="width: 300px; color: gray;">联系纪要</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${clue.contactSummary}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 90px;">
			<div style="width: 300px; color: gray;">下次联系时间</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.nextContactTime}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px; "></div>
		</div>
        <div style="position: relative; left: 40px; height: 30px; top: 100px;">
            <div style="width: 300px; color: gray;">详细地址</div>
            <div style="width: 630px;position: relative; left: 200px; top: -20px;">
                <b>
                    ${clue.address}
                </b>
            </div>
            <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
        </div>
	</div>

	<!-- 备注 -->
	<div id="remarkFor" style="position: relative; top: 40px; left: 40px;">
		<div class="page-header">
			<h4>备注</h4>
		</div>
		<!-- 备注1 -->
		<c:forEach items="${clueRemarkList}" var="clueRemark">
			<div class="remarkDiv" id="div_${clueRemark.id}" style="height: 60px;">
				<img title="${clueRemark.createBy}" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
				<div style="position: relative; top: -40px; left: 40px;" >
					<h5>${clueRemark.noteContent}</h5>
					<font color="gray">线索</font> <font color="gray">-</font> <b>${clue.fullname}${clue.appellation}-${clue.company}</b> <small style="color: gray;"> ${clueRemark.editFlag=="1"?clueRemark.editTime:clueRemark.createTime} 由${clue.createBy}${clueRemark.editFlag=="1"?'修改':'创建'}</small>
					<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
						<a class="myHref" name="updateA" remarkId="${clueRemark.id}" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<a class="myHref" name="deleteA" remarkId="${clueRemark.id}" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
					</div>
				</div>
			</div>
		</c:forEach>
		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button type="button" class="btn btn-primary" id="saveClueRemark">保存</button>
				</p>
			</form>
		</div>
	</div>

	<!-- 市场活动 -->
	<div>
		<div style="position: relative; top: 60px; left: 40px;">
			<div class="page-header">
				<h4>市场活动</h4>
			</div>
			<div style="position: relative;top: 0px;">
				<table class="table table-hover" style="width: 900px;">
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
							<td><a href="javascript:void(0);" value="${al.id}" style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>解除关联</a></td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>

			<div>
				<a href="javascript:void(0);" id="associationActivityBtn" data-target="#bundModal" style="text-decoration: none;"><span class="glyphicon glyphicon-plus"></span>关联市场活动</a>
			</div>
		</div>
	</div>


	<div style="height: 200px;"></div>
</body>
</html>
