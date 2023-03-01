<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>와이파이 정보 구하기</title>
</head>
<body>
<style>
    h1, a {
        text-align: center;
        display: flex;
        justify-content: center;
    }
</style>

<h1><%= request.getAttribute("result") %></h1>
<a href="index.jsp">홈 으로 가기</a>
</body>
</html>