<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%@ page import = "java.sql.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<h1>hello（此页登录您的数据库用户名和密码）</h1>
<form id="from1" action="MyServlet" method="post">
<table>
<tr><td>用户名</td><td><input type="text"  name="ID"></td></tr>
<tr><td>密码</td><td><input type="password"  name="PW"></td></tr>

<tr><td colspan = "3" align = "center"><input type = "submit" value = "login_mysql" id = "btnde1" onclick="del(id)"/></td></tr>


</table>
</form>

</body>
</html>

