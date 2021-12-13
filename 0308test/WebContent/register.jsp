<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>用户注册</title>
</head>
<body>
<td colspan="3" align ="center">
<h1>user register(用户注册)</h1></td>
<form id="register_form" action="RegisterServlet" method="post">
<table>
<tr><td>账号</td><td><input type="text" name="username"></td></tr>
<tr><td>密码</td><td><input type="password" name="password"></td></tr>
<tr><td>昵称</td><td><input type="text" name="name"></td></tr>
<tr><td>年级</td><td><input type= "text"name=grade></td></tr>
<tr><td>学号</td><td><input type="text" name="number"></td></tr>
<tr><td>学院</td><td><input type="text" name="academy"></td></tr>
<tr><td>专业</td><td><input type="text" name="major"></td></tr>
<tr><td>班级</td><td><input type="text" name="class"></td></tr>
<tr><td>联系方式</td><td><input type="text" name="telephone"></td></tr>
<tr><td colspan="3" align="center"><input type="submit" value="register" id="register_btn" >
</table>
</form>
</body>
</html>