// src/com/campus/ui/QRDialog.java
package com.campus.ui;

import com.campus.util.QRUtil;
import javax.swing.*;
import java.awt.*;

public class QRDialog extends JDialog {

    public QRDialog(JFrame parent, String text) {
        super(parent, "签到二维码", true);
        setSize(300, 320);
        setLocationRelativeTo(parent);
        try {
            byte[] png = QRUtil.generate(text, 250);
            ImageIcon icon = new ImageIcon(png);
            JLabel lbl = new JLabel(icon);
            add(lbl, BorderLayout.CENTER);
        } catch (Exception ex) {
            add(new JLabel("生成失败：" + ex.getMessage()), BorderLayout.CENTER);
        }
        JButton btn = new JButton("关闭");
        btn.addActionListener(e -> dispose());
        add(btn, BorderLayout.SOUTH);
    }
}
