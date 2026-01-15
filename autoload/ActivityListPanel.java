// src/com/campus/ui/ActivityListPanel.java
package com.campus.ui;
import java.awt.*;
import javax.swing.*;
import com.campus.entity.Activity;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Map;

public class ActivityListPanel extends JPanel {

    private JTable table = new JTable();
    private DefaultTableModel model;

    private String[] COL = {"ID", "名称", "地点", "开始", "结束", "状态", "上限", "已报", "已签到", "均分"};

    public ActivityListPanel() {
        setLayout(new BorderLayout());
        model = new DefaultTableModel(null, COL) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table.setModel(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    /* 外部调用：刷新数据 */
public void loadData(List<Map<String,Object>> acts) {
    model.setRowCount(0);
    for (Map<String,Object> a : acts) {
        model.addRow(new Object[]{
            a.get("id"), a.get("name"), a.get("location"),
            a.get("startTime"), a.get("endTime"),
            a.get("status"), a.get("maxNum"),
            a.get("applyNum"), a.get("checkNum"),
            a.get("avgScore")
        });
    }
}

    /* 获取当前选中行活动 ID */
    public int getSelectedActId() {
        int row = table.getSelectedRow();
        return row == -1 ? -1 : (int) model.getValueAt(row, 0);
    }
}
