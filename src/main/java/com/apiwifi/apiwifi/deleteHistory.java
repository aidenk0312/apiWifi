package com.apiwifi.apiwifi;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/deleteHistoryServlet")
public class deleteHistory extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        try {
            // SQLite DB에 연결
            Class.forName("org.sqlite.JDBC");
            String url1 = "jdbc:sqlite:/Users/yongsuyongsu/Library/CloudStorage/OneDrive-개인/벡엔드/test/과제/wifi/wifiDb/dbSql.sqlite";
            Connection connection = DriverManager.getConnection(url1);

            // history 테이블에서 id를 이용해 해당 데이터 삭제하는 쿼리 실행
            PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM history WHERE ID = ?");
            deleteStatement.setInt(1, id);
            deleteStatement.executeUpdate();
            deleteStatement.close();

            // DB 연결 종료
            connection.close();

            // 삭제 후 목록 화면으로 이동
            response.sendRedirect("historyList.jsp");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
