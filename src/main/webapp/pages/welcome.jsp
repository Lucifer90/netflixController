<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="it.fanciullini.model.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="it.fanciullini.model.PaymentsLog" %>
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
<table id="payments_log">
    <tr>
        <th>Id</th>
        <th>Pagante</th>
        <th>Spesa</th>
        <th>Data Pagamento</th>
        <th>Inizio Validità</th>
        <th>Fine Validità</th>
        <th>Stato Pagamento</th>
    </tr>
    <%
        ArrayList<PaymentsLog> paymentsLogList=(ArrayList<PaymentsLog>) request.getAttribute("paymentsLogList");
        for (PaymentsLog paymentsLog: paymentsLogList) {
    %>
        <tr>
            <td><%=paymentsLog.getId()%></td>
            <td><%=paymentsLog.getUsername()%></td>
            <td><%=paymentsLog.getQuantity()%></td>
            <td><%=paymentsLog.getPaymentDate()%></td>
            <td><%=paymentsLog.getStartServicePeriod()%></td>
            <td><%=paymentsLog.getEndServicePeriod()%></td>
            <td><%=paymentsLog.getPayed()%></td>
        </tr>
    <%}%>
</table>


</body>
</html>
