package com.apiwifi.apiwifi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/publicWifiServlet")
public class javaApi extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int MAX_RESULTS_PER_REQUEST = 1000;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PreparedStatement statement = null;
        int startIndex = 1;
        int endIndex = startIndex + MAX_RESULTS_PER_REQUEST - 1;

        List<PublicWifiData> allData = new ArrayList<>();

        try {
            while (true) {
                String urlBuilder = "http://openapi.seoul.go.kr:8088/" + URLEncoder.encode("43414f53506b7973383562584a5755", "UTF-8") +
                        "/" + URLEncoder.encode("json", "UTF-8") +
                        "/" + URLEncoder.encode("TbPublicWifiInfo", "UTF-8") +
                        "/" + URLEncoder.encode(String.valueOf(startIndex), "UTF-8") +
                        "/" + URLEncoder.encode(String.valueOf(endIndex), "UTF-8") +
                        "/" + URLEncoder.encode("", "UTF-8") + // 자치구명을 지정하지 않음
                        "/" + URLEncoder.encode("", "UTF-8"); // 도로명 주소를 지정하지 않음

                URL url = new URL(urlBuilder);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/xml");
                System.out.println("Response code: " + conn.getResponseCode());
                BufferedReader rd;

                // 서비스코드가 정상이면 200~300사이의 숫자가 나옵니다.
                if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
                conn.disconnect();
                System.out.println(sb.toString());

                // JSON 데이터를 Java 객체로 변환
                Gson gson = new GsonBuilder().create();
                PublicWifiResponse responseData = gson.fromJson(sb.toString(), PublicWifiResponse.class);

                if (responseData == null) {
                    throw new RuntimeException("데이터를 가져오는 데 실패했습니다.");
                }

                List<PublicWifiData> data = responseData.getPublicWifiInfo().getRow();

                allData.addAll(data);

                if (data.size() < MAX_RESULTS_PER_REQUEST) {
                    break;
                }

                startIndex += MAX_RESULTS_PER_REQUEST;
                endIndex += MAX_RESULTS_PER_REQUEST;

                if (endIndex - startIndex >= MAX_RESULTS_PER_REQUEST) {
                    endIndex = startIndex + MAX_RESULTS_PER_REQUEST - 1;
                }
            }

            // SQLite DB에 연결
            Class.forName("org.sqlite.JDBC");
            String url1 = "jdbc:sqlite:/Users/yongsuyongsu/Library/CloudStorage/OneDrive-개인/벡엔드/test/과제/wifi/wifiDb/dbSql.sqlite";
            Connection connection = DriverManager.getConnection(url1);

            // 삭제 쿼리 실행
            Statement deleteStatement = connection.createStatement();
            deleteStatement.execute("DELETE FROM info_wifi");
            deleteStatement.close();
            statement = connection.prepareStatement("INSERT INTO info_wifi (mgr_no, wrdofc, main_nm, adres1, adres2, instl_floor, instl_ty, instl_mby, svc_se, cmcwr, cnstc_year, inout_door, remars3, lat, lnt, work_dttm) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement selectStatement = connection.prepareStatement("SELECT COUNT(*) FROM info_wifi");
            ResultSet rs = selectStatement.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            selectStatement.close();
            if (allData != null) {
                for (PublicWifiData data : allData) {
                    statement.setString(1, data.getX_SWIFI_MGR_NO());
                    statement.setString(2, data.getX_SWIFI_WRDOFC());
                    statement.setString(3, data.getX_SWIFI_MAIN_NM());
                    statement.setString(4, data.getX_SWIFI_ADRES1());
                    statement.setString(5, data.getX_SWIFI_ADRES2());
                    String instl_floor = data.getX_SWIFI_INSTL_FLOOR() != null ? data.getX_SWIFI_INSTL_FLOOR() : "";
                    statement.setString(6, instl_floor);
                    statement.setString(7, data.getX_SWIFI_INSTL_TY());
                    statement.setString(8, data.getX_SWIFI_INSTL_MBY());
                    statement.setString(9, data.getX_SWIFI_SVC_SE());
                    statement.setString(10, data.getX_SWIFI_CMCWR());
                    statement.setString(11, data.getX_SWIFI_CNSTC_YEAR());
                    statement.setString(12, data.getX_SWIFI_INOUT_DOOR());
                    statement.setString(13, data.getX_SWIFI_REMARS3());
                    statement.setDouble(15, Double.parseDouble(data.getLAT()));
                    statement.setDouble(14, Double.parseDouble(data.getLNT()));
                    statement.setString(16, data.getWORK_DTTM());

                    statement.executeUpdate();
                }

            } else {
                throw new RuntimeException("데이터를 가져오는 데 실패했습니다.");
            }
            // 출력할 문자열을 생성
            String result = allData.size() + "개의 WIFI 정보를 정상적으로 저장하였습니다.";
            request.setAttribute("result", result);
            request.getRequestDispatcher("result.jsp").forward(request, response);
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
        }
    }
}
