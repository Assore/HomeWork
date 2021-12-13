<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>用户登录</title>
<body>

<td colspan = "3" align = "center">
<h1>user login（此页是您数据库中数据表中存的内容）</h1></td>
<form id="from1" action="MyServlet1" method="post">
<table>
<tr><td>username</td><td><input type="text"  name="username"></td></tr>
<tr><td>password</td><td><input type="password"  name="password"></td></tr>

<tr><td colspan = "3" align = "center"><input type = "submit" value = "login" id = "btnde2" /></td>
    <td clospan="3" align="center"><input type = "button" value = "register" onclick = "window.location.href = 'register.jsp'"></td>
</tr>

</table>
</form>

</body>
</html>
