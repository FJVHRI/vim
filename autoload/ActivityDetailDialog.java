// src/com/campus/ui/ActivityDetailDialog.java
package com.campus.ui;

import com.campus.dao.ActivityDao;
import com.campus.dao.ApplyDao;
import com.campus.entity.Activity;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ActivityDetailDialog extends JDialog {

    public ActivityDetailDialog(JFrame parent, Activity act) {
        super(parent, "活动详情 - " + act.getName(), true);
        setSize(700, 500);
        setLocationRelativeTo(parent);

        JTabbedPane tabs = new JTabbedPane();

        /* ① 基本信息：根据系统时间实时计算状态 */
        String realStatus = new ActivityDao()
                .calcStatus(act.getStartTime(), act.getEndTime(), act.getStatus());

        JTextArea info = new JTextArea(
                "名称：" + act.getName() + "\n" +
                "地点：" + act.getLocation() + "\n" +
                "时间：" + act.getStartTime() + " ~ " + act.getEndTime() + "\n" +
                "上限：" + act.getMaxNum() + "\n" +
                "状态：" + realStatus);
        info.setEditable(false);
        tabs.add("基本信息", new JScrollPane(info));

        /* ② 签到名单 */
        try {
            List<Map<String, Object>> list = new ApplyDao().getCheckInList(act.getId());
            String[] col = {"姓名", "签到时间"};
            Object[][] data = new Object[list.size()][2];
            for (int i = 0; i < list.size(); i++) {
                data[i][0] = list.get(i).get("stuName");
                data[i][1] = list.get(i).get("checkTime");
            }
            JTable checkTable = new JTable(data, col);
            tabs.add("签到名单", new JScrollPane(checkTable));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /* ③ 评价列表 */
        try {
            List<Map<String, Object>> eval = new ApplyDao().getEvaluationList(act.getId());
            String[] col2 = {"姓名", "评分", "评论"};
            Object[][] data2 = new Object[eval.size()][3];
            for (int i = 0; i < eval.size(); i++) {
                data2[i][0] = eval.get(i).get("stuName");
                data2[i][1] = eval.get(i).get("score");
                data2[i][2] = eval.get(i).get("comment");
            }
            JTable evalTable = new JTable(data2, col2);
            tabs.add("评价列表", new JScrollPane(evalTable));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        add(tabs);
    }
}
