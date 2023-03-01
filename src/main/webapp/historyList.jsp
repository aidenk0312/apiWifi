<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<!DOCTYPE html>
<html>
<head>
    <title>위치 히스토리 목록</title>
</head>
<body>
<h1>위치 히스토리 목록</h1>

<a href="index.jsp">홈</a>
|
<a href="historyList.jsp">위치 히스토리 목록</a>
|
<a href="publicWifiServlet">Open API 와이파이 정보 가져오기</a>
<% String result = (String) request.getAttribute("result");
    if (result != null) { %>
<p><%= result %></p>
<% } %>

<style>
    #wifiApi {
        font-family: Arial, Helvetica, sans-serif;
        border-collapse: collapse;
        width: 100%;
    }
    #wifiApi td, #wifiApi th {
        border: 1px solid #ddd;
        padding: 8px;
    }
    #wifiApi tr:nth-child(even){background-color: #f2f2f2;}
    #wifiApi tr:hover {background-color: #ddd;}
    #wifiApi th {
        padding-top: 12px;
        padding-bottom: 12px;
        text-align: center; /* 가운데 정렬 */
        background-color: #04AA6D;
        color: white;
        font-size: 16px; /* 글자 크기 조정 */
    }
    .deleteBtn {
        display: flex;
        justify-content: center;
    }
</style>
<table id="wifiApi">
    <thead>
    <tr>
        <th>ID</th>
        <th>X좌표</th>
        <th>Y좌표</th>
        <th>조회일자</th>
        <th class="deleteBtn">비고</th>
    </tr>
    </thead>
    <script>
        function confirmDelete(id) {
            if (confirm("해당 데이터를 삭제 하시겠습니까?")) {
                document.getElementById("deleteBtn_" + id).submit();
            }
        }
    </script>
    <tbody>
    <%
        try {
            Class.forName("org.sqlite.JDBC");
            String url1 = "jdbc:sqlite:/Users/yongsuyongsu/Library/CloudStorage/OneDrive-개인/벡엔드/test/과제/wifi/wifiDb/dbSql.sqlite";
            Connection connection = DriverManager.getConnection(url1);

            // 최근 조회한 데이터를 조회하는 쿼리 실행
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM history ORDER BY 조회일자 DESC");
            ResultSet resultSet = statement.executeQuery();

            // 조회 결과를 List 객체에 저장
            List<String[]> dataList = new ArrayList<>();
            while (resultSet.next()) {
                String[] data = new String[4];
                data[0] = String.valueOf(resultSet.getInt("ID"));
                data[1] = String.valueOf(resultSet.getDouble("LAT"));
                data[2] = String.valueOf(resultSet.getDouble("LNT"));
                data[3] = resultSet.getString("조회일자");
                dataList.add(data);
            }

            // DB 연결 종료
            resultSet.close();
            statement.close();
            connection.close();

            // 조회 결과를 테이블에 출력
            for (String[] data : dataList) {
    %>
    <tr>
        <td><%= data[0] %></td>
        <td><%= data[1] %></td>
        <td><%= data[2] %></td>
        <td><%= data[3] %></td>
        <td class="deleteBtn">
            <form id="deleteBtn_<%= data[0] %>" action="deleteHistoryServlet" method="post" onclick="confirmDelete('<%= data[0] %>')">
                <input type="hidden" name="id" value="<%= data[0] %>">
                <button type="button">삭제</button>
            </form>
        </td>
    </tr>
    <%
        }
    } catch (Exception e) {
    %>
    <p>에러 발생: <%= e %></p>
    <%
        }
    %>
    </tbody>
</table>
</body>
</html>