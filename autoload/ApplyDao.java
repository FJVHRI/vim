// src/com/campus/dao/ApplyDao.java
package com.campus.dao;

import com.campus.db.DBUtil;
import com.campus.entity.Apply;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class ApplyDao {

    /* 报名 */
    public void insert(Apply a) throws SQLException {
        String sql = "INSERT INTO apply(stu_id,act_id) VALUES (?,?)";
        try (Connection c = DBUtil.getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, a.getStuId());
            ps.setInt(2, a.getActId());
            ps.executeUpdate();
        }
    }

    /* 取消报名 */
    public void delete(int stuId, int actId) throws SQLException {
        String sql = "DELETE FROM apply WHERE stu_id=? AND act_id=?";
        try (Connection c = DBUtil.getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, stuId);
            ps.setInt(2, actId);
            ps.executeUpdate();
        }
    }

    /* 是否已报 */
    public boolean exists(int stuId, int actId) throws SQLException {
        String sql = "SELECT 1 FROM apply WHERE stu_id=? AND act_id=? LIMIT 1";
        try (Connection c = DBUtil.getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, stuId);
            ps.setInt(2, actId);
            return ps.executeQuery().next();
        }
    }

    /* 签到 */
    public void checkIn(int stuId, int actId) throws SQLException {
        String sql = "UPDATE apply SET check_time=NOW() WHERE stu_id=? AND act_id=?";
        try (Connection c = DBUtil.getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, stuId);
            ps.setInt(2, actId);
            ps.executeUpdate();
        }
    }

    /* 评分 + 评论 */
    public void score(int stuId, int actId, int score, String comment) throws SQLException {
        String sql = "UPDATE apply SET score=?,comment=? WHERE stu_id=? AND act_id=?";
        try (Connection c = DBUtil.getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, score);
            ps.setString(2, comment);
            ps.setInt(3, stuId);
            ps.setInt(4, actId);
            ps.executeUpdate();
        }
    }

    /* 是否已评价 */
    public boolean hasScored(int stuId, int actId) throws SQLException {
        String sql = "SELECT 1 FROM apply WHERE stu_id=? AND act_id=? AND score IS NOT NULL";
        try (Connection c = DBUtil.getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, stuId);
            ps.setInt(2, actId);
            return ps.executeQuery().next();
        }
    }

    /* 保存评分（业务包装） */
    public void saveScore(int stuId, int actId, int score, String comment) throws SQLException {
        score(stuId, actId, score, comment);
    }

    /* 管理员：签到名单 */
    public List<Map<String, Object>> getCheckInList(int actId) throws SQLException {
        String sql = """
                SELECT u.name stu_name, ap.check_time
                FROM apply ap
                JOIN user u ON u.id = ap.stu_id
                WHERE ap.act_id = ? AND ap.check_time IS NOT NULL
                """;
        try (Connection c = DBUtil.getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, actId);
            ResultSet rs = ps.executeQuery();
            List<Map<String, Object>> list = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> m = new HashMap<>();
                m.put("stuName", rs.getString("stu_name"));
                m.put("checkTime", rs.getTimestamp("check_time").toLocalDateTime());
                list.add(m);
            }
            return list;
        }
    }

    /* 管理员：评价列表 */
    public List<Map<String, Object>> getEvaluationList(int actId) throws SQLException {
        String sql = """
                SELECT u.name stu_name, ap.score, ap.comment
                FROM apply ap
                JOIN user u ON u.id = ap.stu_id
                WHERE ap.act_id = ? AND ap.score IS NOT NULL
                """;
        try (Connection c = DBUtil.getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, actId);
            ResultSet rs = ps.executeQuery();
            List<Map<String, Object>> list = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> m = new HashMap<>();
                m.put("stuName", rs.getString("stu_name"));
                m.put("score", rs.getInt("score"));
                m.put("comment", rs.getString("comment"));
                list.add(m);
            }
            return list;
        }
    }

    /* 电子票信息 */
    public Map<String, Object> getTicketInfo(int stuId, int actId) throws SQLException {
        String sql = """
                SELECT u.name stu_name, a.name act_name, a.start_time, a.location
                FROM apply ap
                JOIN user u ON u.id = ap.stu_id
                JOIN activity a ON a.id = ap.act_id
                WHERE ap.stu_id = ? AND ap.act_id = ?
                """;
        try (Connection c = DBUtil.getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, stuId);
            ps.setInt(2, actId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Map<String, Object> m = new HashMap<>();
                m.put("stuName", rs.getString("stu_name"));
                m.put("actName", rs.getString("act_name"));
                m.put("startTime", rs.getTimestamp("start_time").toLocalDateTime());
                m.put("location", rs.getString("location"));
                return m;
            }
            return null;
        }
    }
}
