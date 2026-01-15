// src/com/campus/ui/AdminMainFrame.java
package com.campus.ui;

import com.campus.service.ActivityService;
import javax.swing.*;
import java.awt.*;
import java.util.List; 
import com.campus.dao.ActivityDao;
import com.campus.entity.Activity; 
import java.sql.SQLException; 
import java.util.Map;
public class AdminMainFrame extends JFrame {

    private ActivityListPanel listPanel = new ActivityListPanel();
    private ActivityService service = new ActivityService();
private ActivityDao actDao = new ActivityDao();
    public AdminMainFrame() {
        super("管理员端");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton btnAdd = new JButton("新增活动");
        JButton btnDel = new JButton("删除活动");
        JButton btnEdit = new JButton("编辑活动");
        JButton btnDetail = new JButton("详情");
JButton btnCalendar = new JButton("日历视图");
JButton btnScan = new JButton("扫学生票签到");
        JButton btnExport = new JButton("导出 CSV");

        JPanel north = new JPanel();
        north.add(btnAdd); 
        north.add(btnDel); 
        north.add(btnEdit);
        north.add(btnDetail);
        north.add(btnCalendar);
        north.add(btnScan);
        north.add(btnExport);
        add(north, BorderLayout.NORTH);
        add(listPanel, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> {
            ActivityFormDialog dlg = new ActivityFormDialog(this);
            dlg.setVisible(true);
            if (dlg.isSaved()) reload();
        });

        btnDel.addActionListener(e -> {
            int id = listPanel.getSelectedActId();
            if (id == -1) { JOptionPane.showMessageDialog(this, "请先选择要删除的活动"); return; }
            int ok = JOptionPane.showConfirmDialog(this, "确认删除？");
            if (ok != JOptionPane.YES_OPTION) return;
            String res = service.removeActivity(id);
            JOptionPane.showMessageDialog(this, "ok".equals(res) ? "已删除" : res);
            reload();
        });
btnEdit.addActionListener(e->{
  int id = listPanel.getSelectedActId();
  if(id == -1){JOptionPane.showMessageDialog(this,"请先选择要编辑的活动");
return;
  }
try {
        Activity a = actDao.findById(id);          // 见下方 DAO 追加方法
        ActivityFormDialog dlg = new ActivityFormDialog(this, a); // 构造重载，见下方 4
        dlg.setVisible(true);
        if (dlg.isSaved()) reload();               // 保存成功再刷新列表
    } catch (SQLException ex) { ex.printStackTrace(); }
});

btnDetail.addActionListener(e -> {
    int id = listPanel.getSelectedActId();
    if (id == -1) { JOptionPane.showMessageDialog(this, "请先选择活动"); return; }
    try {
        Activity a = actDao.findById(id);
        new ActivityDetailDialog(this, a).setVisible(true);
    } catch (SQLException ex) { ex.printStackTrace(); }
});

btnCalendar.addActionListener(e -> {
    JDialog dlg = new JDialog(this, "活动日历", true);
    dlg.setSize(600, 400);
    dlg.setLocationRelativeTo(this);
    dlg.add(new CalendarPanel());
    dlg.setVisible(true);
});

btnScan.addActionListener(e -> {
    int id = listPanel.getSelectedActId();
    if (id == -1) { JOptionPane.showMessageDialog(this, "请先选择活动"); return; }
    new ScanTicketDialog(this, id).setVisible(true);
}); 
btnExport.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new java.io.File("activity.csv"));
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    List<com.campus.entity.Activity> acts = service.listAll();
                    com.campus.util.CsvUtil.exportActivity(acts, chooser.getSelectedFile().getAbsolutePath());
                    JOptionPane.showMessageDialog(this, "导出完成");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "导出失败：" + ex.getMessage());
                }
            }
        });

        reload();
        // =====每10秒自动刷新一次 =====
        Timer autoRefreshTimer = new Timer(10000, e -> reload());
        autoRefreshTimer.start();
    }
private void reload() {
    try {
        List<Map<String,Object>> data = actDao.listWithStats();
        System.out.println(">>> 返回条数 = " + data.size());
        if (!data.isEmpty()) {
            System.out.println(">>> 首条 key 集合 = " + data.get(0).keySet());
        }
        listPanel.loadData(data);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

}
