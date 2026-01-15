// src/com/campus/ui/CalendarPanel.java
package com.campus.ui;
import com.campus.dao.ActivityDao;
import com.campus.entity.Activity;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class CalendarPanel extends JPanel {
    private final LocalDate today = LocalDate.now();
    private YearMonth currentMonth;
    private final ActivityDao dao = new ActivityDao();

    public CalendarPanel() {
        currentMonth = YearMonth.of(today.getYear(), today.getMonth());
        setLayout(new BorderLayout());
        reload();
    }

    private void reload() {
        removeAll();
        JLabel lblMonth = new JLabel(currentMonth.toString(), SwingConstants.CENTER);
        lblMonth.setFont(lblMonth.getFont().deriveFont(16f));
        add(lblMonth, BorderLayout.NORTH);

        JPanel days = new JPanel(new GridLayout(0, 7)); // 7 列
        String[] week = {"日","一","二","三","四","五","六"};
        for (String w : week) days.add(new JLabel(w, SwingConstants.CENTER));

        int daysInMonth = currentMonth.lengthOfMonth();
        int firstDayWeek = currentMonth.atDay(1).getDayOfWeek().getValue() % 7;
        for (int i = 0; i < firstDayWeek; i++) days.add(new JLabel(""));
        for (int day = 1; day <= daysInMonth; day++) {
            JButton btnDay = new JButton(String.valueOf(day));
            btnDay.setMargin(new Insets(0, 0, 0, 0));
            LocalDate date = currentMonth.atDay(day);
            try {
                List<Activity> acts = dao.listAll(); // 简单：当天有活动就标红
                boolean hasAct = acts.stream()
                        .anyMatch(a -> a.getStartTime().toLocalDate().equals(date));
                if (hasAct) btnDay.setBackground(Color.PINK);
            } catch (Exception ignored) {}
            btnDay.addActionListener(e -> JOptionPane.showMessageDialog(this, "点击了 " + date));
            days.add(btnDay);
        }
        add(days, BorderLayout.CENTER);

        JPanel ctrl = new JPanel();
        JButton prev = new JButton("<");
        JButton next = new JButton(">");
        prev.addActionListener(e -> { currentMonth = currentMonth.minusMonths(1); reload(); });
        next.addActionListener(e -> { currentMonth = currentMonth.plusMonths(1); reload(); });
        ctrl.add(prev); ctrl.add(next);
        add(ctrl, BorderLayout.SOUTH);
    }
}
