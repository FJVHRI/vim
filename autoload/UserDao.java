// src/com/campus/dao/UserDao.java
package com.campus.dao;
import com.campus.db.DBUtil;
import com.campus.entity.User;
import java.sql.*;

public class UserDao {
    public User login(String account, String pwd) throws SQLException {
        String sql = "SELECT id,account,name,role FROM user WHERE account=? AND pwd=?";
        try (Connection c = DBUtil.getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, account);
            ps.setString(2, pwd);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("account"), rs.getString("name"), rs.getString("role"));
            }
            return null;
        }
    }
}
