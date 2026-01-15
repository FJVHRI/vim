// src/com/campus/dao/ActivityDao.java
package com.campus.dao;
import com.campus.db.DBUtil;
import com.campus.entity.Activity;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class ActivityDao {

    /* -------------- 原有方法 -------------- */
    public List<Activity> listAll() throws SQLException {
        List<Activity> list = new ArrayList<>();
        String sql = "SELECT * FROM activity ORDER BY start_time";
        try (Connection c = DBUtil.getConn(); Statement s = c.createStatement()) {
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                Activity a = new Activity();
                a.setId(rs.getInt("id"));
                a.setName(rs.getString("name"));
                a.setInfo(rs.getString("info"));
                a.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                a.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
                a.setLocation(rs.getString("location"));
                a.setMaxNum(rs.getInt("max_num"));
                a.setStatus(rs.getString("status"));
                a.setAdminId(rs.getInt("admin_id"));
                list.add(a);
            }
        }
        return list;
    }

    public int insert(Activity a) throws SQLException {
        String sql = "INSERT INTO activity(name,info,start_time,end_time,location,max_num,status,admin_id) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection c = DBUtil.getConn(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getName());
            ps.setString(2, a.getInfo());
            ps.setTimestamp(3, Timestamp.valueOf(a.getStartTime()));
            ps.setTimestamp(4, Timestamp.valueOf(a.getEndTime()));
            ps.setString(5, a.getLocation());
            ps.setInt(6, a.getMaxNum());
            ps.setString(7, a.getStatus());
            ps.setInt(8, a.getAdminId());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            keys.next();
            return keys.getInt(1);
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "UPDATE activity SET status='已取消' WHERE id=?";
        try (Connection c = DBUtil.getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /* -------------- 新增：用于编辑回显 -------------- */
    public Activity findById(int id) throws SQLException {
        String sql = "SELECT * FROM activity WHERE id=?";
        try (Connection c = DBUtil.getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Activity a = new Activity();
                a.setId(rs.getInt("id"));
                a.setName(rs.getString("name"));
                a.setInfo(rs.getString("info"));
                a.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                a.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
                a.setLocation(rs.getString("location"));
                a.setMaxNum(rs.getInt("max_num"));
                a.setStatus(rs.getString("status"));
                a.setAdminId(rs.getInt("admin_id"));
                return a;
            }
            return null;
        }
    }

 
    /* 根据起止时间计算状态 */
public String calcStatus(LocalDateTime start, LocalDateTime end, String dbStatus) {
    if ("已取消".equals(dbStatus)) return "已取消";
    LocalDateTime now = LocalDateTime.now();
    if (now.isBefore(start))          return "未开始";
    if (now.isAfter(end))             return "已结束";
    return "报名中";
}
 
/* 带统计信息的活动列表 */
public List<Map<String,Object>> listWithStats() throws SQLException {
    String sql = "SELECT * FROM v_act_stats ORDER BY start_time";
    try (Connection c = DBUtil.getConn(); Statement s = c.createStatement()) {
        ResultSet rs = s.executeQuery(sql);
        List<Map<String,Object>> list = new ArrayList<>();
        while (rs.next()) {
            Map<String,Object> m = new HashMap<>();
            m.put("id", rs.getInt("id"));
            m.put("name", rs.getString("name"));
            m.put("location", rs.getString("location"));

            LocalDateTime st = rs.getTimestamp("start_time").toLocalDateTime();
            LocalDateTime et = rs.getTimestamp("end_time").toLocalDateTime();
            m.put("startTime", st);
            m.put("endTime", et);

            // 用库里的原始状态参与计算，但界面显示实时计算后的中文
            String rawStatus = rs.getString("status");
            m.put("status", calcStatus(st, et, rawStatus));

            m.put("maxNum", rs.getInt("max_num"));
            m.put("applyNum", rs.getInt("apply_num"));
            m.put("checkNum", rs.getInt("check_num"));
            m.put("avgScore", rs.getDouble("avg_score"));
            list.add(m);
        }
        return list;
    }
}


    /* -------------- 新增：用于电子票 -------------- */
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
