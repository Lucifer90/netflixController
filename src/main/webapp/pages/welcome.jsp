<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="it.fanciullini.model.User" %>
<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: Luca
  Date: 30/11/2017
  Time: 22:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/styles.css' />"/>
    <title>Benvenuto!</title>
</head>
<body>
<h1>Lista Utenti</h1>
<table id="users">
    <tr>
        <th>Nome</th>
        <th>Cognome</th>
        <th>Mail</th>
        <th>Data Registrazione</th>
    </tr>
    <%
        ArrayList<User> usersList=(ArrayList<User>) request.getAttribute("usersList");
        for (User user: usersList) {
    %>
        <tr>
            <td><%=user.getName()%></td>
            <td><%=user.getSurname()%></td>
            <td><%=user.getMail()%></td>
            <td><%=user.getRegisterDate()%></td>
        </tr>
    <%}%>
</table>
<h1>Lista Pagamenti</h1>
</body>
</html>
