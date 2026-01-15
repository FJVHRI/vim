// src/com/campus/ui/ScoreDialog.java
package com.campus.ui;

import com.campus.dao.ApplyDao;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class ScoreDialog extends JDialog {
    private JTextArea commentArea;
    private JSlider scoreSlider;

    public ScoreDialog(Frame parent, int stuId, int actId) {
        super(parent, "活动评价", true);
        setLocationRelativeTo(parent);
        setResizable(false);

        // 评分滑块
        JLabel scoreLabel = new JLabel("活动评分（1-5分）：");
        scoreSlider = new JSlider(1, 5, 3);
        scoreSlider.setMajorTickSpacing(1);
        scoreSlider.setPaintTicks(true);
        scoreSlider.setPaintLabels(true);

        // 评论输入框（关键：设置首选大小）
        JLabel commentLabel = new JLabel("评论内容：");
        commentArea = new JTextArea(4, 25);  // ← 增加行数和列数
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        JScrollPane commentScroll = new JScrollPane(commentArea);
        commentScroll.setPreferredSize(new Dimension(300, 80));  // ← 强制设置首选大小

        // 按钮组
        JButton submitBtn = new JButton("提交评价");
        submitBtn.addActionListener(e -> {
            int score = scoreSlider.getValue();
            String comment = commentArea.getText().trim();

            if (comment.isEmpty()) {
                JOptionPane.showMessageDialog(this, "评论内容不能为空！");
                return;
            }

            try {
                new ApplyDao().saveScore(stuId, actId, score, comment);
                JOptionPane.showMessageDialog(this, "评价提交成功！");
                dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "提交失败：" + ex.getMessage());
                ex.printStackTrace();
            }
        });

        JButton cancelBtn = new JButton("取消");
        cancelBtn.addActionListener(e -> dispose());

        JPanel btnPanel = new JPanel();
        btnPanel.add(submitBtn);
        btnPanel.add(cancelBtn);

        // 使用垂直BoxLayout代替BorderLayout（更稳定的跨平台布局）
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        mainPanel.add(scoreLabel);
        mainPanel.add(Box.createVerticalStrut(5));  // 增加间距
        mainPanel.add(scoreSlider);
        mainPanel.add(Box.createVerticalStrut(15)); // 增加间距
        mainPanel.add(commentLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(commentScroll);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(btnPanel);

        add(mainPanel);

        // 关键：使用pack()自动调整大小，替代setSize()
        pack();
        setMinimumSize(new Dimension(400, 300));  // 设置最小尺寸
    }
}
