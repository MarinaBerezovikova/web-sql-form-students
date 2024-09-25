<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Student Form</title>
</head>
<body>
    <h2>Введите данные студента</h2>
    <form action="saveStudent" method="post">
        Имя: <input type="text" name="firstName" required /><br />
        Фамилия: <input type="text" name="lastName" required /><br />
        Номер курса: <input type="number" name="courseNumber" min="1" max="4" required /><br />
        <input type="submit" value="Сохранить" />
    </form>
</body>
</html>