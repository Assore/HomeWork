<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.nls.person.UserInfo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>个人资料</title>
</head>
<body>
<%UserInfo userinfo=(UserInfo) request.getAttribute("userinfo"); %>
<table>
<tr><td>昵称</td><td><%=userinfo.getName() %></td></tr>
<tr><td>年级</td><td><%=userinfo.getGrade() %></td></tr>
<tr><td>学院</td><td><%=userinfo.getAcad() %></td></tr>
<tr><td>专业</td><td><%=userinfo.getMajor() %></td></tr>
</table>
</body>
</html>