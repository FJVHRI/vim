// src/com/campus/db/DBUtil.java
package com.campus.db;

import java.sql.*;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/campus?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai";
    private static final String USER = "campus";
    private static final String PASSWORD = "123";

    static {
        try { Class.forName("com.mysql.cj.jdbc.Driver"); }
        catch (ClassNotFoundException e) { throw new RuntimeException(e); }
    }

    public static Connection getConn() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /* ===== 测试数据库连通性 ===== */
    public static void main(String[] args) {
        try (Connection c = getConn()) {
            System.out.println("✅ 数据库连接成功，版本：" + c.getMetaData().getDatabaseProductVersion());
        } catch (Exception e) {
            System.out.println("❌ 数据库连接失败");
            e.printStackTrace();
        }
    }
}
