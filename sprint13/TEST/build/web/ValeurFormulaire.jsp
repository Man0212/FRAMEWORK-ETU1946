<%@ page import="etu1946.framework.models.Emp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP Page</title>
</head>
<body>
    <% Emp obj = (Emp) request.getAttribute("test"); %>
    Nom: <%= obj.getNom() %><br>
    Age: <%= obj.getAge() %><br>
</body>
</html>
