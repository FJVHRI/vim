// src/com/campus/ui/ActivityFormDialog.java
package com.campus.ui;

import com.campus.entity.Activity;
import com.campus.service.ActivityService;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class ActivityFormDialog extends JDialog {

    private JTextField txtName   = new JTextField(20);
    private JTextArea  txtInfo   = new JTextArea(4, 30);
    private JTextField txtStart  = new JTextField("2026-03-25T14:00", 16);
    private JTextField txtEnd    = new JTextField("2026-03-25T17:00", 16);
    private JTextField txtLoc    = new JTextField(20);
    private JTextField txtMax    = new JTextField("50", 5);
    private JButton    btnSave   = new JButton("保存");
    private JButton    btnCancel = new JButton("取消");

    private ActivityService service = new ActivityService();
    private boolean saved = false;
    private int actId = 0;   // 0=新增 非0=编辑

    /* ---------------- 新增：空表单构造 ---------------- */
    public ActivityFormDialog(JFrame parent) {
        this(parent, null);
    }

    /* ---------------- 编辑：回显构造 ---------------- */
    public ActivityFormDialog(JFrame parent, Activity a) {
        super(parent, a == null ? "新增活动" : "编辑活动", true);
        setSize(400, 350);
        setLocationRelativeTo(parent);

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; p.add(new JLabel("名称"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; p.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 1; p.add(new JLabel("简介"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH;
        p.add(new JScrollPane(txtInfo), gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        p.add(new JLabel("开始时间"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(txtStart, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        p.add(new JLabel("结束时间"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(txtEnd, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        p.add(new JLabel("地点"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(txtLoc, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE;
        p.add(new JLabel("人数上限"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(txtMax, gbc);

        JPanel south = new JPanel();
        south.add(btnSave); south.add(btnCancel);
        add(p, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);

        /* ===== 回显数据（仅编辑模式）===== */
        if (a != null) {
            txtName.setText(a.getName());
            txtInfo.setText(a.getInfo());
            txtStart.setText(a.getStartTime().toString());
            txtEnd.setText(a.getEndTime().toString());
            txtLoc.setText(a.getLocation());
            txtMax.setText(String.valueOf(a.getMaxNum()));
            this.actId = a.getId();   // 记住 id，保存时走 update
        }

        /* ===== 保存事件：构造末尾统一绑定 ===== */
        btnSave.addActionListener(e -> {
            try {
                Activity act = new Activity();
                act.setName(txtName.getText().trim());
                act.setInfo(txtInfo.getText().trim());
                act.setStartTime(LocalDateTime.parse(txtStart.getText().trim()));
                act.setEndTime(LocalDateTime.parse(txtEnd.getText().trim()));
                act.setLocation(txtLoc.getText().trim());
                act.setMaxNum(Integer.parseInt(txtMax.getText().trim()));
                act.setStatus("报名中");
                act.setAdminId(1);   // 当前管理员硬编码

                if (actId == 0) {               // 新增
                    String res = service.addActivity(act);
                    if (!"ok".equals(res)) {
                        JOptionPane.showMessageDialog(this, res);
                        return;
                    }
                } else {                        // 编辑
                    act.setId(actId);
                    String res = service.updateActivity(act);
                    if (!"ok".equals(res)) {
                        JOptionPane.showMessageDialog(this, res);
                        return;
                    }
                }
                saved = true;
                dispose();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "时间格式错误，请用 yyyy-MM-ddTHH:mm");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "保存失败：" + ex.getMessage());
            }
        });

        btnCancel.addActionListener(e -> dispose());
    }

    public boolean isSaved() { return saved; }
}
