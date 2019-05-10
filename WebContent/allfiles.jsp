<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8"> 
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>mongo网盘</title>
	<link rel="stylesheet" href="bootstrap-3.3.7-dist/css/bootstrap.min.css">  
	<script src="bootstrap-3.3.7-dist/js/jquery-3.2.1.min.js"></script>
	<script src="bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>

<style>
#header {
    background-color:#708090;
    color:black;
    text-align:center;
    padding:5px;
   
}
#nav {
    line-height:25px;
    background-color:#00BFFF;
    float:left;
    width:10%;
   
    padding:5px;	      
}
#section {
	background-color:#eFAFFF;
    float:right;
    width:90%;
   
    padding:2px;	 
}
#footer {
    background-color:green;
    color:black;
    clear:both;
	float:bottom;
    text-align:center;
    padding:10px;	
    position:absolute;
    bottom:0px;
    left:0px;
    right:0px;
}
</style>
<script type="text/javascript">
//点击导航条添加active
$(document).ready(function () {
	$('ul.nav > li').click(function (e) {
	//e.preventDefault();
	$('ul.nav > li').removeClass('active');
	$(this).addClass('active');
	});
});
</script>
<script>
	//上传文件总长度， 已经上传的字节数， 要上传的文件数， 已经上传的文件数
	var totalFileLength, totalUploaded, fileCount, filesUploaded;

	//一个文件上传完成
	function onUploadComplete(e){
		totalUploaded += document.getElementById('files').files[filesUploaded].size;
		filesUploaded++;
		if (filesUploaded < fileCount){
			uploadNext(); //上传下一个文件
		} else {
			alert('上传成功！');
		}
	}
	
	
	function onFileSelect(e){
		var pro = document.getElementById('pro');
		pro.value = "0"; //进度条重置
		
		var files = e.target.files;
		fileCount = files.length; //选择的文件数
		totalFileLength = 0;
		for (var i=0; i<fileCount; i++){
			var file = files[i];
			totalFileLength += file.size; //计算总文件长度
		}
		document.getElementById('selectedFiles').innerHTML = output.join('');
	}
	
	//上传进度
	function onUploadProgress(e){
		if (e.lengthComputable){
			var percentComplete = parseInt((e.loaded + totalUploaded)*100/totalFileLength);//计算上传字节数的百分比
			var pro = document.getElementById('pro');//进度条
			pro.innerHTML = percentComplete + '%';
			pro.value = percentComplete;//更新进度条
		} else {
			debug('unable to compute');
		}
	}
	
	//上传失败
	function onUploadFailed(e){
		alert("Error uploading file");
	}
	
	//上传一个文件
	function uploadNext(){
		var xhr = new XMLHttpRequest();
		var fd = new FormData();
		var file = document.getElementById('files').files[filesUploaded];//获取input的某个file
		fd.append("fileToUpload", file);
		xhr.upload.addEventListener("progress", onUploadProgress, false);//对上传对象增加监听，更新进度条
		xhr.addEventListener("load", onUploadComplete, false);//添加监听器，监听该文件上传是否完成
		xhr.open("POST", "FileUploadServlet?uploadType=multipleUploads");//打开连接，每一个文件都有一个请求
		xhr.send(fd);//发送该文件
	}
	
	//开始上传
	function startUpload(){
		totalUploaded = filesUploaded = 0;
		uploadNext();
	}
	
	//页面加载
	window.onload = function(){
		document.getElementById('files').addEventListener('change', onFileSelect, false);
		document.getElementById('uploadButton').addEventListener('click', startUpload, false);
	}
	
</script>
</head>

<body>
<div id="header">
<h1>mongo文件管理</h1>
</div>

<div id="nav">

<!-- 导航 -->
<ul class="nav nav-pills nav-stacked" >
	<li></li><!-- 有这行导航条才随点击添加active，又会触发a连接 -->
	<li><a href="${pageContext.request.contextPath}/ListFilesServlet?listType=all" target="listframe">全部文件</a></li>
	<li><a href="${pageContext.request.contextPath}/ListFilesServlet?listType=video" target="listframe">视频文件</a></li>
	<li><a href="${pageContext.request.contextPath}/ListFilesServlet?listType=image" target="listframe">图片文件</a></li>
	<li><a href="${pageContext.request.contextPath}/ListFilesServlet?listType=doc" target="listframe">文档文件</a></li>

</ul>
------------------------
<!-- 查询 -->
<form action="ListFilesServlet?listType=accurate" method="post" target='listframe' role="form">
<div class="form-group">
	<input type="text" name="filename" style="width:100%" class="form-control" >
	<button type="submit" class="btn btn-default">精确查询</button>
</div>
</form>
<form action="ListFilesServlet?listType=match" method="post" target='listframe' role="form">
<div class="form-group">
	<input type="text" name="filename" style="width:100%" class="form-control" >
	<button type="submit" class="btn btn-default">模糊查询</button>
</div>
</form>

<!-- 上传 -->
<form id='form1' action="FileUploadServlet?uploadType=multipleUploads" enctype="multipart/form-data" method="post" target='listframe'>
		<input type="file" id="files" name="filename" multiple/>
		<input id="uploadButton" type="button" value="上传文件">
</form>
</div>



<div id="section">
<iframe name="listframe" frameborder="0" width="100%" height="450px"></iframe>
</div>

<div>
	<progress id="pro" value="0" max="100" style="width:100%;"></progress>
</div>
<div id="footer">
Copyright ? jinpeng.com
</div>

</body>
</html>