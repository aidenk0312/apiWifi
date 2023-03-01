<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>와이파이 정보 구하기</title>
</head>
<body>
<h1>
    <%= "와이파이 정보 구하기" %>
</h1>

<br/>

<a href="index.jsp">홈</a>
|
<a href="historyList.jsp">위치 히스토리 목록</a>
|
<a href="publicWifiServlet">Open API 와이파이 정보 가져오기</a>
<% String result = (String) request.getAttribute("result");
    if (result != null) { %>
<p><%= result %></p>
<% } %>


<br/><br/>

LAT:
<input type="text" name="LAT" value="<%=request.getParameter("LAT")!=null ? request.getParameter("LAT") : "0.0" %>" id="latInput">
,
LNT:
<input type="text" name="LNT" value="<%=request.getParameter("LNT")!=null ? request.getParameter("LNT") : "0.0" %>" id="lntInput">

<button onclick="getLocation()">내 위치 가져오기</button>
<script>
    function getLocation() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(showPosition);
        } else {
            alert("지원하지 않는 브라우져 입니다.");
        }
    }
    function showPosition(position) {
        document.getElementById("latInput").value = position.coords.latitude;
        document.getElementById("lntInput").value = position.coords.longitude;
    }
</script>
<button onclick="getNear()">근처 WIPI 정보 보기</button>
<script>
    function getNear() {
        var lat = document.getElementById("latInput").value;
        var lnt = document.getElementById("lntInput").value;
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                var wifiData = JSON.parse(this.responseText);
                var table = document.getElementById("wifiApi").getElementsByTagName('tbody')[0];
                table.innerHTML = "";
                for (var i = 0; i < wifiData.length; i++) {
                    var distance = Math.round(wifiData[i].distance * 1000) / 1000;
                    var row = table.insertRow(i);
                    var cell1 = row.insertCell(0);
                    var cell2 = row.insertCell(1);
                    var cell3 = row.insertCell(2);
                    var cell4 = row.insertCell(3);
                    var cell5 = row.insertCell(4);
                    var cell6 = row.insertCell(5);
                    var cell7 = row.insertCell(6);
                    var cell8 = row.insertCell(7);
                    var cell9 = row.insertCell(8);
                    var cell10 = row.insertCell(9);
                    var cell11 = row.insertCell(10);
                    var cell12 = row.insertCell(11);
                    var cell13 = row.insertCell(12);
                    var cell14 = row.insertCell(13);
                    var cell15 = row.insertCell(14);
                    var cell16 = row.insertCell(15);
                    var cell17 = row.insertCell(16);

                    cell1.innerHTML = distance;
                    cell2.innerHTML = wifiData[i].X_SWIFI_MGR_NO;
                    cell3.innerHTML = wifiData[i].X_SWIFI_WRDOFC;
                    cell4.innerHTML = wifiData[i].X_SWIFI_MAIN_NM;
                    cell5.innerHTML = wifiData[i].X_SWIFI_ADRES1;
                    cell6.innerHTML = wifiData[i].X_SWIFI_ADRES2;
                    cell7.innerHTML = wifiData[i].X_SWIFI_INSTL_FLOOR;
                    cell8.innerHTML = wifiData[i].X_SWIFI_INSTL_TY;
                    cell9.innerHTML = wifiData[i].X_SWIFI_INSTL_MBY;
                    cell10.innerHTML = wifiData[i].X_SWIFI_SVC_SE;
                    cell11.innerHTML = wifiData[i].X_SWIFI_CMCWR;
                    cell12.innerHTML = wifiData[i].X_SWIFI_CNSTC_YEAR;
                    cell13.innerHTML = wifiData[i].X_SWIFI_INOUT_DOOR;
                    cell14.innerHTML = wifiData[i].X_SWIFI_REMARS3;
                    cell15.innerHTML = wifiData[i].LNT;
                    cell16.innerHTML = wifiData[i].LAT;
                    cell17.innerHTML = wifiData[i].WORK_DTTM;
                }
            }
        };
        xhttp.open("GET", "selectApiServlet?LAT=" + lat + "&LNT=" + lnt, true);
        xhttp.send();
    }
</script>

<br/><br/>

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
</style>
<table id="wifiApi">
    <thead>
    <tr>
        <th>거리(Km)</th>
        <th>관리번호</th>
        <th>자치구</th>
        <th>와이파이명</th>
        <th>도로명주소</th>
        <th>상세주소</th>
        <th>설치위치(층)</th>
        <th>설치유형</th>
        <th>설치기관</th>
        <th>서비스구분</th>
        <th>망종류</th>
        <th>설치년도</th>
        <th>실내외구분</th>
        <th>WIFI접속환경</th>
        <th>X좌표</th>
        <th>Y좌표</th>
        <th>작업일자</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td colspan="17" style="text-align:center;">위치 정보를 입력한 후에 조회해 주세요.</td>
    </tr>
    </tbody>
</table>

</body>
</html>