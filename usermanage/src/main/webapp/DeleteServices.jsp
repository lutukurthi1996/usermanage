<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Delete Services</title>
</head>
<body>
<form action="http://localhost:8080/usermanage/webresources/user/deleteservices">
Delete services:<select name="services" value="services"><br>
		<option value="msoffice">Ms Office</option>
      	<option value="teams">Teams</option>
      	<option value="salesforce">Salesforce</option>
      	<option value="sailpoint">Sailpoint</option>
      	<option  value="pantry">Pantry</option>
      	<option value="outlook">Outlook</option></select>
<input type="submit"><br>
</form>
</body>
</html>