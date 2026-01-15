package com.campus.ui;

import com.campus.dao.UserDao;
import com.campus.entity.User;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class LoginDialog extends JDialog {
    private JTextField txtUser = new JTextField(); 
    // ========== 修改1：密码框改为JTextField（明文显示） ==========
    private JTextField txtPwd = new JTextField(); 
    private User loginUser = null;
    private Font simHeiFont;
    private BufferedImage bgImage;

    public LoginDialog(JFrame parent) {
        super(parent, "校园活动组织与报名系统", true);
        loadSimHeiFont();
        loadBgImage();

        setSize(600, 400); 
        setLocationRelativeTo(parent);
        setResizable(false);
        setContentPane(new BgPanel());
        getContentPane().setLayout(new BorderLayout(10, 20));

        // 顶部标题区
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        JLabel lblTitle = new JLabel("校园活动系统", JLabel.CENTER);
        lblTitle.setFont(simHeiFont.deriveFont(Font.BOLD, 18f));
        lblTitle.setForeground(new Color(30, 30, 30));
        topPanel.add(lblTitle);
        add(topPanel, BorderLayout.NORTH);

        // 中间输入区
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 40, 8, 40);
        gbc.anchor = GridBagConstraints.CENTER;

        // 账号标签
        JLabel lblUser = new JLabel("账  号：");
        lblUser.setFont(simHeiFont.deriveFont(Font.BOLD));
        lblUser.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(lblUser, gbc);

        // 账号输入框
        txtUser.setFont(simHeiFont);
        txtUser.setPreferredSize(new Dimension(200, 35));
        txtUser.setBorder(new LineBorder(new Color(150, 180, 220), 1, true));
        txtUser.setMargin(new Insets(5, 10, 5, 10));
        txtUser.setBackground(new Color(255, 255, 255, 200));
        gbc.gridx = 1;
        centerPanel.add(txtUser, gbc);

        // 密码标签
        JLabel lblPwd = new JLabel("密  码：");
        lblPwd.setFont(simHeiFont.deriveFont(Font.BOLD));
        lblPwd.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(lblPwd, gbc);

        // 密码输入框（明文+黑色字体）
        txtPwd.setFont(simHeiFont);
        txtPwd.setPreferredSize(new Dimension(200, 35));
        txtPwd.setBorder(new LineBorder(new Color(150, 180, 220), 1, true));
        txtPwd.setMargin(new Insets(5, 10, 5, 10));
        txtPwd.setBackground(new Color(255, 255, 255, 200));
        // ========== 修改2：设置密码框文字为黑色 ==========
        txtPwd.setForeground(Color.BLACK);
        gbc.gridx = 1;
        centerPanel.add(txtPwd, gbc);
        add(centerPanel, BorderLayout.CENTER);

        // 底部登录按钮区
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        JButton btnLogin = new JButton("登 录");
        btnLogin.setFont(simHeiFont);
        btnLogin.setPreferredSize(new Dimension(150, 40));
        btnLogin.setBackground(new Color(60, 120, 200));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);
        bottomPanel.add(btnLogin);
        add(bottomPanel, BorderLayout.SOUTH);

        // 登录逻辑
        btnLogin.addActionListener(e -> {
            String username = txtUser.getText();
            // ========== 修改3：获取密码改为getText()（适配JTextField） ==========
            String password = txtPwd.getText();
            
            // 账号密码为空弹窗 - 居中
            if (username.trim().isEmpty() || password.trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                    LoginDialog.this,
                    "账号和密码不能为空", 
                    "提示",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            btnLogin.setEnabled(false);
            try {
                User u = new UserDao().login(username, password);
                // 账号密码错误弹窗 - 居中
                if (u == null) {
                    JOptionPane.showMessageDialog(
                        LoginDialog.this, 
                        "账号或密码错误", 
                        "提示", 
                        JOptionPane.WARNING_MESSAGE
                    );
                } else {
                    loginUser = u;
                    dispose();
                }
            } catch (Exception ex) {
                // 登录异常弹窗 - 居中
                JOptionPane.showMessageDialog(
                    LoginDialog.this, 
                    "登录时发生错误，请稍后重试", 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE
                );
		 // === 关键：打印完整错误信息 ===
    System.err.println("===== 登录异常详情 =====");
    ex.printStackTrace();  // 这行会打印完整的错误堆栈
    System.err.println("========================");
                ex.printStackTrace();
            } finally {
                btnLogin.setEnabled(true);
            }
        });
    }

    // 加载SimHei字体
    private void loadSimHeiFont() {
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("SimHei.ttf"));
            simHeiFont = customFont.deriveFont(14f);
        } catch (Exception e) {
            simHeiFont = new Font("SimHei", Font.PLAIN, 14);
        }
    }

    // 加载背景图片
    private void loadBgImage() {
        try {
            bgImage = ImageIO.read(new File("img/bg.jpg"));
        } catch (IOException e) {
            System.out.println("背景图片加载失败，使用纯色背景：" + e.getMessage());
            bgImage = null;
        }
    }

    // 自定义背景面板
    private class BgPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (bgImage != null) {
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.setColor(new Color(230, 240, 250));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    public User getLoginUser() {
        return loginUser;
    }
}

