package com.apiwifi.apiwifi;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

@WebServlet("/selectApiServlet")
public class selectApi extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        double lat = Double.parseDouble(request.getParameter("LAT"));
        double lnt = Double.parseDouble(request.getParameter("LNT"));

        try {
            // SQLite DB에 연결
            Class.forName("org.sqlite.JDBC");
            String url1 = "jdbc:sqlite:/Users/yongsuyongsu/Library/CloudStorage/OneDrive-개인/벡엔드/test/과제/wifi/wifiDb/dbSql.sqlite";
            Connection connection = DriverManager.getConnection(url1);

            // history 테이블에 데이터를 삽입하는 쿼리 실행
            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO history (LAT, LNT, 조회일자) VALUES (?, ?, datetime('now'))");
            insertStatement.setDouble(1, lat);
            insertStatement.setDouble(2, lnt);
            insertStatement.executeUpdate();
            insertStatement.close();

            PreparedStatement statement = connection.prepareStatement("SELECT *, " +
                    "(6371 * acos(cos(radians(?)) * cos(radians(lat)) * cos(radians(lnt) - radians(?)) + sin(radians(?)) * sin(radians(lat)))) AS distance " +
                    "FROM info_wifi " +
                    "ORDER BY distance " +
                    "LIMIT 20");

            statement.setDouble(1, lat);
            statement.setDouble(2, lnt);
            statement.setDouble(3, lat);

            ResultSet resultSet = statement.executeQuery();

            List<PublicWifiData> nearestData = new ArrayList<>();

            while (resultSet.next()) {
                PublicWifiData data = new PublicWifiData();

                data.setX_SWIFI_MGR_NO(resultSet.getString("mgr_no"));
                data.setX_SWIFI_WRDOFC(resultSet.getString("wrdofc"));
                data.setX_SWIFI_MAIN_NM(resultSet.getString("main_nm"));
                data.setX_SWIFI_ADRES1(resultSet.getString("adres1"));
                data.setX_SWIFI_ADRES2(resultSet.getString("adres2"));
                data.setX_SWIFI_INSTL_FLOOR(resultSet.getString("instl_floor"));
                data.setX_SWIFI_INSTL_TY(resultSet.getString("instl_ty"));
                data.setX_SWIFI_INSTL_MBY(resultSet.getString("instl_mby"));
                data.setX_SWIFI_SVC_SE(resultSet.getString("svc_se"));
                data.setX_SWIFI_CMCWR(resultSet.getString("cmcwr"));
                data.setX_SWIFI_CNSTC_YEAR(resultSet.getString("cnstc_year"));
                data.setX_SWIFI_INOUT_DOOR(resultSet.getString("inout_door"));
                data.setX_SWIFI_REMARS3(resultSet.getString("remars3"));
                data.setLAT(String.valueOf(resultSet.getDouble("lat")));
                data.setLNT(String.valueOf(resultSet.getDouble("lnt")));
                data.setWORK_DTTM(resultSet.getString("work_dttm"));

                nearestData.add(data);
            }
            // 검색 결과를 JSON 형태로 변환하여 응답으로 전달
            String json = new Gson().toJson(nearestData);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
            // DB 연결 종료
            statement.close();
            connection.close();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}