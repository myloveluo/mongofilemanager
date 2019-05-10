<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Insert title here</title>
	<link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">  
	<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
	<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<style type="text/css">
	.col1{
		width:10%;
	}
	.col2{
		width:30%;
	}
	.col3{
		width:10%;
	}
	.col4{
		width:10%;
	}
	.col5{
		width:20%;
	}
	.col6{
		width:20%;
	}
	</style>

	<script type="text/javascript">
	function selectAll(obj){
		var checkObj=document.getElementsByName("deletecheck");
	    for(var i=0;i<checkObj.length;i++)
	    {
	        if(obj.checked==true)
	        	checkObj[i].checked=true;
	        else
	        	checkObj[i].checked=false;
	    }
	}
	</script>
</head>
<body>
<div>
<table class="table table-hover">
<!-- 	<caption>悬停表格布局</caption> -->
	<thead >
		<tr>
			<th><input type="checkbox" name="allcheck" value="1" onclick="selectAll(this)"></th>
			<th class="col1">序号</th>
			<th class="col2">文件名</th>
			<th class="col3">缩略图</th>
			<th class="col4">大小</th>
			<th class="col5">上传日期</th>
			<th class="col6">操作</th>
		</tr>
	</thead>
</table>

<form action="FileOperateServlet?operate=multipleDelete" method="post" target="_self">
<div style="overflow:auto;height:350px">
<table class="table table-hover">
	<tbody>
	<c:forEach var="fileInfo" varStatus="status" items="${fileInfoList}">
		
		<tr>
			<td><input type="checkbox" name="deletecheck" value="${fileInfo.fileId}"></td>
			<td class="col1">${status.index+1}</td>
			<td class="col2">${fileInfo.filename}</td>
			<td class="col3"><img src="${fileInfo.thumbnailURL}" height="50" width="50" alt="显示为50*50"/></td>
			<td class="col4"><fmt:formatNumber value="${fileInfo.size/(1024*1024)}" maxFractionDigits="2"></fmt:formatNumber> MB</td>
			<td class="col5"><fmt:formatDate value="${fileInfo.uploadDate}" pattern="yyyy-MM-dd "/></td>
			<td class="col6">
			<a href="${pageContext.request.contextPath}/FileDownloadServlet?fileId=${fileInfo.fileId}&filename=${fileInfo.filename}">下载</a>
			<a href="${pageContext.request.contextPath}/FileOperateServlet?operate=singleDelete&fileId=${fileInfo.fileId}">删除</a>
			</td>
		</tr>
	</c:forEach>
	</tbody>
</table>
</div>
<input type="submit" value="删除选中">
</form>
</div>


</body>

</html>