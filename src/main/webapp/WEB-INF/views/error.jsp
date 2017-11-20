<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<title>500</title>
<style type="text/css" media="screen">
body {
	margin: 0;
	font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
}

.container {
	margin: 50px auto 40px auto;
	width: 600px;
	text-align: center;
}

h1 {
	width: 800px;
	position: relative;
	left: -100px;
	letter-spacing: -1px;
	line-height: 60px;
	font-weight: 100;
	margin: 0px 0 50px 0;
	text-shadow: 0 1px 0 #fff;
}

p {
	color: rgba(0, 0, 0, 0.5);
	margin: 20px 0;
	line-height: 1.6;
}
</style>
</head>
<body>
	<div class="container">
		<h1>错误</h1>
		<!-- request -->
		<p style="background: #F1FAFA">${errors}</p>
	</div>
</body>
</html>

