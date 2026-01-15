// src/com/campus/ui/ScanTicketDialog.java
package com.campus.ui;
import com.campus.dao.ApplyDao;
import javax.swing.*;
import java.awt.*;

public class ScanTicketDialog extends JDialog {
    private final int actId;
    private JTextField txtQr = new JTextField(25);

    public ScanTicketDialog(JFrame parent, int actId) {
        super(parent, "活动签到", true);
        this.actId = actId;
        setSize(400, 150);
        setLocationRelativeTo(parent);

        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.add(new JLabel("粘贴学生票二维码内容："), BorderLayout.NORTH);
        p.add(txtQr, BorderLayout.CENTER);

        JButton btnOk = new JButton("完成签到");
        p.add(btnOk, BorderLayout.SOUTH);
        add(p);

        btnOk.addActionListener(e -> {
            String qr = txtQr.getText().trim();
            // 正则：STU:数字,ACT:数字
            if (!qr.matches("STU:(\\d+),ACT:(\\d+)")) {
                JOptionPane.showMessageDialog(this, "格式错误！应为：STU:学号,ACT:活动ID");
                return;
            }
            int stuId = Integer.parseInt(qr.replaceAll(".*STU:(\\d+).*", "$1"));
            int qrActId = Integer.parseInt(qr.replaceAll(".*ACT:(\\d+).*", "$1"));
            if (qrActId != actId) {
                JOptionPane.showMessageDialog(this, "二维码不属于本活动！");
                return;
            }
            try {
                new ApplyDao().checkIn(stuId, actId);
                JOptionPane.showMessageDialog(this, "签到成功！");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "签到失败：" + ex.getMessage());
            }
        });
    }
}
