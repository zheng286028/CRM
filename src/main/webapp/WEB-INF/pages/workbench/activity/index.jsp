<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
	<base href="<%=basePath%>"> <%--这样所有的图片资源啥的都会从根目录下找--%>


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
	//入口函数
	$(function(){
		//给创建按钮添加单击事件
		$("#createActivityBtn").click(function () {
			//重置表单
			$("#ActivityFrom").get(0).reset();
			//弹出创建市场活动模态窗口
			$("#createActivityModal").modal("show");
		});
		//给保存添加单击事件
		$("#savaActivityBtn").click(function () {
			//收集参数
			var owner = $("#create-marketActivityOwner").val();
			var name = 	$.trim($("#create-marketActivityName").val());
			var startDate =$("#create-startTime").val();
			var endDate = $("#create-endTime").val();
			var cost = $.trim($("#create-cost").val());
			var description = $.trim($("#create-describe").val());
			//表单验证
			if(name == ""){
				alert("名称未填写！")
				return;
			}
			if(owner == ""){
				alert("所有者未填写！")
				return;
			}
			if(startDate>endDate){
				alert("结束日期不能小于开始日期")
				return;
			}
			var regExp=/^(([1-9]\d*)|0)$/;
			if(!regExp.test(cost)){
				alert("成本只能为非负整数");
				return;
			}
			$.ajax({
				url:"workbench/activity/saveActivity.do",
				data:{
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					cost:cost,
					description:description
				},
				type:"post",
				dataType:"json",
				success:function (resp) {
					if(resp.code=="1"){
						//关闭模态窗口
						$("#createActivityModal").modal("hide");
						//重新查询
						queryActivityByPageAndCondition(1,$("#currentPage").bs_pagination("getOption","rowsPerPage"));
					}else{
						//提示信息
						alert("服务器繁忙，请稍后尝试！");
					}
				}
			})
		});
		//设置日历
		$(".mydate").datetimepicker({
			language:'zh-CN',//语言
			format:'yyyy-mm-dd',//时间格式
			minView:'month', //可以选择最小视图
			initialDate:new Date(),
			autoclose:true,//选完日期关闭日历
			todayBtn:true,//设置今天日期按钮
			clearBtn:true,//表示是否显示清空按钮
		});
		//页面刷新查询数据，总记录数，默认查第一页，十条数据
		queryActivityByPageAndCondition(1,3);
		//给查询按钮绑定单击事件
		$("#queryBtn").click(function () {
			queryActivityByPageAndCondition(1,$("#currentPage").bs_pagination('getOption','rowsPerPage'));
		});
		//给选全按钮绑定单击事件
		$("#checkAll").click(function () {
			//如果全选按钮是选中状态，则列表中的所有checkbox都选中
			$("#Tbody input[type='checkbox']").prop('checked',this.checked);
		});
		//给所有的checkbox绑定单击事件
		$("#Tbody").on("click","input[type='checkbox']",function () {
			//拿没选中的和被选中的checkbox做比较,不相等就把“全选按钮”的checked设为false,反之为true
			if($("#Tbody input[type='checkbox']").size()==
					$("#Tbody input[type='checkbox']:checked").size()){
				$("#checkAll").prop("checked",true)
			}else{
				$("#checkAll").prop("checked",false)
			}
		});
		//给删除按钮绑定单击事件
		$("#deleteBtn").click(function () {
			//收集参数
			//获取被选中的checkbox
				var deleteById=$("#Tbody input[type='checkbox']:checked");
				//没选中checkbox不能点击删除
				if(deleteById.size()==0){
					alert("请选择要删除的数据！");
					return;
				}
				if(window.confirm("确定删除吗？")){
					var ids="";
					//遍历数组
					$.each(deleteById,function () {
						ids+=this.value+",";
					});
					//把多余的符号截取掉
					ids=ids.substring(0,ids.length-1);
					//发送请求
					$.ajax({
						url:"workbench/activity/deleteActivityById.do",
						data:{id:ids},
						type:"post",
						dataType:"json",
						success:function (resp) {
							if (resp.code=="1"){
								//删除成功,重新查询数据
							queryActivityByPageAndCondition(1,$("#currentPage").bs_pagination("getOption","rowsPerPage"));
							}else{
								//删除失败，给出信息
								alert(resp.message)
							}
						}
					});
				}
		});
		//给修改市场活动按钮绑定单击事件
		$("#updateActivityBtn").click(function () {
			//拿到被选中的checkbox
			var checkId=$("#Tbody input[type='checkbox']:checked");
			//判断是否选中一个checkbox
			if(checkId.size()==0){
				alert("请选择你要修改的数据！")
				return
			}else if (checkId.size()>1){
				alert("只能修改一条数据！")
				return;
			}
			//拿到id
			var id=checkId[0].value;
			//发送异步请求
			$.ajax({
				url:"workbench/activity/selectActivityById.do",
				data:{id:id},
				type:"post",
				dataType:"json",
				success:function (resp) {
					//将查询到数据拼接到修改的模态窗口上
					$("#updateId").val(resp.id);
					$("#edit-marketActivityOwner").val(resp.owner);
					$("#edit-marketActivityName").val(resp.name);
					$("#edit-startTime").val(resp.startDate);
					$("#edit-endTime").val(resp.endDate);
					$("#edit-cost").val(resp.cost);
					$("#edit-describe").val(resp.description);
					//打开修改市场活动的模态窗口
					$("#updateActivityModal").modal("show");

				}
			});
		});
		//给修改表单的更新按钮绑定单击事件
		$("#updateActivityById").click(function () {
			//收集参数
			var id=$("#updateId").val();
			var owner=$("#edit-marketActivityOwner").val();
			var name=$.trim($("#edit-marketActivityName").val());
			var startDate=$("#edit-startTime").val();
			var endDate=$("#edit-endTime").val();
			var cost=$.trim($("#edit-cost").val());
			var description=$.trim($("#edit-describe").val());
			//表单验证
			if(owner==""){
				alert("所有者不能为空");
				return;
			};
			if(name==""){
				alert("名称不能为空");
				return;
			};
			if(startDate>endDate){
				alert("开始日期不能大于结束日期");
				return;
			};
			var regExp=/^(([1-9]\d*)|0)$/;
			if(!regExp.test(cost)){
				alert("成本只能是非负整数");
				return;
			};
			//发送请求
			$.ajax({
				url:"workbench/activity/updateActivityById.do",
				data:{
					id:id,
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					cost:cost,
					description:description,
				},
				type:"post",
				dataType:"json",
				success:function (resp) {
					if(resp.code=="1"){
						//修改成功,关闭模态窗口，并查询数据
						$("#updateActivityModal").modal("hide");
						//当前页数和页码不变
						queryActivityByPageAndCondition($("#currentPage").bs_pagination("getOption","currentPage"),
														$("#currentPage").bs_pagination("getOption","rowsPerPage"));
					}else{
						//修改失败,给出信息，模态窗口不关闭
						alert(resp.message);
						$("#updateActivityModal").modal("show");
					}
				}
			});
		})
		//给批量导出按钮绑定单击事件
		$("#exportActivityAllBtn").click(function () {
			if(window.confirm("确定要全部导出吗？")){
				window.location.href="workbench/activity/selectAllActivityS.do"
			};
		});
		//给选择导出按钮绑定单击事件
		$("#exportActivityXzBtn").click(function () {
			//验证.只能选中一条数据
			var activityId=$("#Tbody input[type='checkbox']:checked");
			if(activityId.size()==0 || activityId.size()>1){
				alert("请选择你要导出的数据，且一次一条数据！");
				return;
			};
			//发送下载请求
			window.location.href="workbench/activity/queryActivityByIds.do?id="+activityId.val();
		});
		//给导入按钮绑定单击事件
		$("#importActivityBtn").click(function () {
			//收集参数
			var fileActivityName=$("#activityFile").val();
			var suffix=fileActivityName.substr(fileActivityName.lastIndexOf(".")).toLocaleLowerCase();
			if(suffix!=".xls"){
				alert("只支持xls的文件！");
				//重置表单
				var outerFile=document.getElementById("activityFile");
				outerFile.outerHTML=outerFile.outerHTML;
				return;
			}
			//获取文件
			var fileActivity=$("#activityFile").get(0).files[0];
			alert("111111");
			if(fileActivity.size>5*1024*1024){
				alert("文件大小不能超过5MB");
				return;
			}
			//发送请求
			var formData=new FormData();
			formData.append("activityFile",fileActivity);
			$.ajax({
				url:"workbench/activity/fileUploadActivity.do",
				data:formData,
				processData:false,//设置ajax向后台提交参数之前，是否把参数统一转换成字符串：true--是，false--不是，默认是true
				contentType:false,//设置ajax向后台提交参数之前，是否把所有的参数统一按urlencoded编码：true--是，false--不是，默认true
				type:"post",
				dataType:"json",
				success:function (resp) {
					if(resp.code=="1"){
						//关闭模态窗口，提示信息
						$("#importActivityModal").modal("hide");
						queryActivityByPageAndCondition($("#currentPage").bs_pagination("getOption","currentPage"),
														$("#currentPage").bs_pagination("getOption","rowsPerPage"));
						alert(resp.retData);
						//重置表单
						var outerFile=document.getElementById("activityFile");
						outerFile.outerHTML=outerFile.outerHTML;
					}else{
						$("#importActivityModal").modal("show");
						alert(resp.message);
					}
				}
			});
		});
	});
	//查询方法
	function queryActivityByPageAndCondition(currentPage,pageSize) {
		//收集数据
		var name=$("#query-name").val();
		var owner=$("#query-owner").val();
		var startDate=$("#startDate").val();
		var endDate=$("#endDate").val();
		/*var currentPage=1;
		var pageSize=5;*/
		//发送请求
		$.ajax({
			url:"workbench/activity/queryActivityByPageAndCondition.do",
			data:{
				name:name,
				owner:owner,
				startDate:startDate,
				endDate:endDate,
				currentPage:currentPage,
				pageSize:pageSize,
			},
			type:'post',
			dataType:'json',
			success:function (resp) {
				//显示总记录数
				//$("#totalCount").text(resp.totalCount);
				var htmlRows="";
				//显示列表
				$.each(resp.activityList,function (index,obj) {
					htmlRows+="<tr class=\"active\">";
					htmlRows+="<td><input type=\"checkbox\"value='"+obj.id+"'/></td>"
					htmlRows+="<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/activity/detailActivity.do?id="+obj.id+"'\">"+obj.name+"</a></td>";
					htmlRows+="<td>"+obj.owner+"</td>";
					htmlRows+="<td>"+obj.startDate+"</td>";
					htmlRows+="<td>"+obj.endDate+"</td>";
					htmlRows+="</tr>";
				});
				//当页面发送变化，被选中的”全选“去掉
				$("#checkAll").prop("checked",false);
				//拼接字符串
				$("#Tbody").html(htmlRows);
				//计算总记录数
				var totalPage=1;
				if(resp.totalCount%pageSize==0){
					totalPage=resp.totalCount/pageSize;
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
						queryActivityByPageAndCondition(pageObj.currentPage,pageObj.rowsPerPage);
					},
				});
			}
		});
	}

</script>
</head>
<body>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">

					<form class="form-horizontal" role="form" id="ActivityFrom">

						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
								 <c:forEach items="${requestScope.users}" var="u">
									 <option value="${u.id}">${u.name}</option>
								 </c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>

						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control mydate" id="create-startTime" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control mydate" id="create-endTime" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>

					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="savaActivityBtn">保存</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="updateActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">

					<form class="form-horizontal" role="form" id="fromActivity">
						<input type="hidden" id="updateId">
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
										<c:forEach items="${requestScope.users}" var="u">
											<option value="${u.id}">${u.name}</option>
										</c:forEach>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control mydate" id="edit-startTime" value="2020-10-10" readonly>
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control mydate" id="edit-endTime" value="2020-10-20" readonly>
							</div>
						</div>

						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>

					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateActivityById">更新</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div id="resetFile" style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>


	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
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
				      <input class="form-control" type="text" id="query-name">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="query-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="endDate">
				    </div>
				  </div>

				  <button type="button" class="btn btn-default" id="queryBtn">查询</button>

				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary"  data-target="#createActivityModal" id="createActivityBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" data-target="#editActivityModal" id="updateActivityBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-target="#importActivityModal" data-toggle="modal" id="uplocadFileActivity"><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover" >
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkAll"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
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
