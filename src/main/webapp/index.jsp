<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Student Form</title>
</head>
<body>
    <h2>Введите данные студента</h2>

        <c:if test="${not empty errorMessage}">
            <div style="color: red;">${errorMessage}</div>
        </c:if>

        <c:if test="${not empty validationMessage}">
            <div style="color: red;">${validationMessage}</div>
        </c:if>

        <c:if test="${not empty successMessage}">
                <div style="color: green;">${successMessage}</div>
            </c:if>

    <form action="saveStudent" method="post">
        Имя: <input type="text" name="firstName" required /><br />
        Фамилия: <input type="text" name="lastName" required /><br />
        Номер курса: <input type="number" name="courseNumber" min="1" max="4" required /><br />
        <input type="submit" value="Сохранить" />
    </form>
</body>
</html>