// src/com/campus/service/ApplyService.java
package com.campus.service;

import com.campus.dao.*;
import com.campus.entity.Apply;
import com.campus.entity.Activity;
import java.sql.SQLException;

public class ApplyService {
    private ApplyDao dao = new ApplyDao();
    private ActivityDao actDao = new ActivityDao();

    public String addApply(int stuId, int actId) {
        try {
            // 1. 检查是否已报名
            if (dao.exists(stuId, actId))
                return "您已报名";

            // 2. 关键：检查活动状态是否已结束
            Activity act = actDao.findById(actId);
            if (act == null) return "活动不存在";
            
            // 实时计算状态（不依赖数据库里的status字段）
            String realStatus = actDao.calcStatus(act.getStartTime(), act.getEndTime(), act.getStatus());
            if ("已结束".equals(realStatus)) {
                return "活动已结束，无法报名";
            }
            if ("已取消".equals(realStatus)) {
                return "活动已取消，无法报名";
            }

            // 3. 执行报名
            dao.insert(new Apply() {
                {
                    setStuId(stuId);
                    setActId(actId);
                }
            });
            return "ok";
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("报名人数已满"))
                return "报名人数已满";
            e.printStackTrace();
            return "db error";
        }
    }
}
