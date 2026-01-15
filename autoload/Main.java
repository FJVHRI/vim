package com.campus;
import com.campus.entity.User;
import com.campus.ui.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginDialog dlg = new LoginDialog(null);
            dlg.setVisible(true);
            User u = dlg.getLoginUser();
            if (u == null) System.exit(0);
            if ("student".equals(u.getRole())) {
                new StudentMainFrame(u).setVisible(true);
            } else {
                new AdminMainFrame().setVisible(true);
            }
        });
    }
}
