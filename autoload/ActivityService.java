// src/com/campus/service/ActivityService.java
package com.campus.service;

import com.campus.dao.ActivityDao;
import com.campus.db.DBUtil;
import com.campus.entity.Activity;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class ActivityService {

    private ActivityDao dao = new ActivityDao();

    /* 新建活动：简单校验时间 */
    public String addActivity(Activity a) {
        if (a.getEndTime().isBefore(a.getStartTime())) {
            return "结束时间不能早于开始时间";
        }
        try {
            dao.insert(a);
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return "数据库错误";
        }
    }

    /* 删除活动（逻辑） */
    public String removeActivity(int actId) {
        try {
            dao.delete(actId);
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return "删除失败";
        }
    }

    /* 列表 */
    public List<Activity> listAll() throws Exception {
        return dao.listAll();
    }

    /* 修改活动 + 时间冲突检测 */
    public String updateActivity(Activity a) {
        if (a.getEndTime().isBefore(a.getStartTime())) return "结束时间不能早于开始时间";
        try (Connection c = DBUtil.getConn()) {
            // 冲突检测：排除自己，看时间段是否重叠
            String sql = """
                SELECT COUNT(*) FROM activity
                WHERE id!=? AND status!='已取消'
                  AND (start_time < ? AND end_time > ?)
                """;
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, a.getId());
                ps.setTimestamp(2, Timestamp.valueOf(a.getEndTime()));
                ps.setTimestamp(3, Timestamp.valueOf(a.getStartTime()));
                ResultSet rs = ps.executeQuery();
                rs.next();
                if (rs.getInt(1) > 0) return "时间段与现有活动冲突";
            }
            // 真正更新
            String up = """
                UPDATE activity
                SET name=?,info=?,start_time=?,end_time=?,location=?,max_num=?
                WHERE id=?
                """;
            try (PreparedStatement ps = c.prepareStatement(up)) {
                ps.setString(1, a.getName());
                ps.setString(2, a.getInfo());
                ps.setTimestamp(3, Timestamp.valueOf(a.getStartTime()));
                ps.setTimestamp(4, Timestamp.valueOf(a.getEndTime()));
                ps.setString(5, a.getLocation());
                ps.setInt(6, a.getMaxNum());
                ps.setInt(7, a.getId());
                ps.executeUpdate();
            }
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return "数据库错误";
        }
    }
}
