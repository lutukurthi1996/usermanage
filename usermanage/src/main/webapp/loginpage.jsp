<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login Page</title>
</head>
<body>
<form action="http://localhost:8080/usermanage/webresources/user/verify">
username:<input type="username" name="name"><br>
password:<input type="password" name="passwd"><br>
<input type="submit"><br>
<a href="http://localhost:8080/usermanage/registration.jsp"><h5>Signup</h5></a>
</form>
</body>
</html>