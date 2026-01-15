// src/com/campus/ui/StudentMainFrame.java
package com.campus.ui;
import com.campus.dao.ActivityDao;
import com.campus.dao.ApplyDao;
import com.campus.entity.Activity;
import com.campus.entity.User;
import com.campus.service.ApplyService;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;
import java.awt.Toolkit;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class StudentMainFrame extends JFrame {
    private final User user;
    private final JTable table = new JTable();
    private final ActivityDao actDao = new ActivityDao();
    private final ApplyService applySvc = new ApplyService();

    public StudentMainFrame(User user) {
        super("学生端");
        this.user = user;
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton btnRefresh = new JButton("刷新活动");
        JButton btnApply   = new JButton("报名");
        JButton btnCancel  = new JButton("取消报名");
        JButton btnScore = new JButton("评价");
        JPanel north = new JPanel();
        north.add(btnRefresh);
        north.add(btnApply);
        north.add(btnCancel);
        north.add(btnScore);
        add(north, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> loadData());

        btnApply.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "请先选择活动"); return; }
            int actId = (int) table.getValueAt(row, 0);
            String res = applySvc.addApply(user.getId(), actId);
            if ("ok".equals(res)) {
                // 生成电子票
                new PdfTicket().generate(user.getId(), actId, user.getAccount());
                
                // 二维码内容
                String qrContent = "STU:" + user.getId() + ",ACT:" + actId;
                
                // 显示新样式的弹窗
                new SuccessDialog(this, actId, user.getAccount(), qrContent).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, res);
            }
        });

        btnCancel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "请先选择活动"); return; }
            int actId = (int) table.getValueAt(row, 0);
            String status = (String) table.getValueAt(row, 5);
            if ("已结束".equals(status)) { JOptionPane.showMessageDialog(this, "活动已结束，无法取消"); return; }
            try {
                new ApplyDao().delete(user.getId(), actId);
                JOptionPane.showMessageDialog(this, "已取消报名");
                loadData();
            } catch (SQLException ex) { ex.printStackTrace(); }
        });

        btnScore.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "请先选择活动"); return; }
            int actId = (int) table.getValueAt(row, 0);
            new ScoreDialog(this, user.getId(), actId).setVisible(true);
        });

        loadData();
    }

    // ========== 新样式报名成功弹窗 ==========
    private class SuccessDialog extends JDialog {
        public SuccessDialog(Frame parent, int actId, String account, String qrContent) {
            super(parent, "报名成功", true);
            setSize(450, 180);
            setLocationRelativeTo(parent);
            setResizable(false);
            
            // 主信息标签
            String infoHtml = String.format(
                "<html>报名成功！<br>电子票已生成：ticket/act%d_%s.pdf</html>", 
                actId, account
            );
            JLabel infoLabel = new JLabel(infoHtml);
            infoLabel.setFont(new Font("SimHei", Font.PLAIN, 14));
            infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            // 二维码内容面板
            JPanel qrPanel = new JPanel(new BorderLayout());
            qrPanel.setBorder(BorderFactory.createTitledBorder("二维码内容（可复制给管理员）"));
            
            // 显示二维码内容的文本框
            JTextField qrField = new JTextField(qrContent);
            qrField.setEditable(false);
            qrField.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            // 复制按钮
            JButton copyBtn = new JButton("复制");
            copyBtn.setPreferredSize(new Dimension(60, 25));
            copyBtn.addActionListener(e -> {
                // 复制到剪贴板
                copyToClipboard(qrContent);
                // 显示自动消失的提示
                showAutoHidePopup();
            });
            
            qrPanel.add(qrField, BorderLayout.CENTER);
            qrPanel.add(copyBtn, BorderLayout.EAST);
            
            // 确定按钮
            JButton okBtn = new JButton("确定");
            okBtn.setFont(new Font("SimHei", Font.PLAIN, 14));
            okBtn.addActionListener(e -> dispose());
            JPanel btnPanel = new JPanel();
            btnPanel.add(okBtn);
            
            // 组装布局
            setLayout(new BorderLayout());
            add(infoLabel, BorderLayout.NORTH);
            add(qrPanel, BorderLayout.CENTER);
            add(btnPanel, BorderLayout.SOUTH);
        }
        
        private void copyToClipboard(String text) {
            StringSelection selection = new StringSelection(text);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        }
        
        private void showAutoHidePopup() {
            JWindow popup = new JWindow();
            popup.setSize(120, 40);
            popup.setLocationRelativeTo(this);
            popup.setLayout(new BorderLayout());
            
            JLabel label = new JLabel("已复制", SwingConstants.CENTER);
            label.setFont(new Font("SimHei", Font.BOLD, 14));
            label.setForeground(Color.WHITE);
            label.setBackground(new Color(60, 120, 200));
            label.setOpaque(true);
            popup.add(label);
            
            popup.setVisible(true);
            
            // 2秒后自动消失
            Timer timer = new Timer(2000, e -> popup.dispose());
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void loadData() {
        try {
            List<Map<String, Object>> list = actDao.listWithStats();
            String[] col = {"ID", "名称", "地点", "开始时间", "结束时间", "状态", "上限"};
            Object[][] data = new Object[list.size()][7];
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> a = list.get(i);
                data[i][0] = a.get("id");
                data[i][1] = a.get("name");
                data[i][2] = a.get("location");
                data[i][3] = a.get("startTime").toString();
                data[i][4] = a.get("endTime").toString();
                data[i][5] = a.get("status");
                data[i][6] = a.get("maxNum");
            }
            table.setModel(new javax.swing.table.DefaultTableModel(data, col));
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}
