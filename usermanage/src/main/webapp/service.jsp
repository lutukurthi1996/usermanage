<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>services</title>
</head>
<body>
<form action="http://localhost:8080/usermanage/webresources/user/createservices">
services:<input type="services" name="services"><br>
accesslevel:<select name="accesslevel" name="accesslevel"><br>
		<option value="admin">Admin</option>
      	<option value="user">User</option>
      	<option value="manager">Manager</option></select>
 <input type="submit"><br>
</form>
</body>
</html>