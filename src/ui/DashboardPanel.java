package ui;

import service.StatisticsService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** Legacy standalone dashboard panel, kept for compatibility.
 *  UserDashboardFrame builds its own dashboard view; this class is no
 *  longer wired into the main navigation but is kept usable on its own. */
public class DashboardPanel extends JPanel {

    public DashboardPanel() {
        setLayout(new BorderLayout(0, 16));
        setBackground(UITheme.BG_APP);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        StatisticsService s = new StatisticsService();

        JPanel statsRow = new JPanel(new GridLayout(1, 4, 16, 0));
        statsRow.setOpaque(false);
        statsRow.add(card("Tổng công việc", String.valueOf(s.getTotal()), UITheme.ACCENT));
        statsRow.add(card("Hoàn thành", String.valueOf(s.getDone()), UITheme.GREEN));
        statsRow.add(card("Đang làm", String.valueOf(s.getDoing()), UITheme.ORANGE));
        statsRow.add(card("Chưa làm", String.valueOf(s.getTodo()), UITheme.RED));
        add(statsRow, BorderLayout.NORTH);

        ChartPanel chart = new ChartPanel();
        JPanel chartWrap = new JPanel(new BorderLayout());
        chartWrap.setOpaque(false);
        chartWrap.setPreferredSize(new Dimension(560, 300));
        chartWrap.add(chart, BorderLayout.CENTER);
        add(chartWrap, BorderLayout.CENTER);
    }

    private JPanel card(String label, String value, Color accent) {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(accent);
                g2.fillRoundRect(0, 0, 4, getHeight(), 3, 3);
            }
        };
        p.setOpaque(false);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                new EmptyBorder(8, 18, 8, 18)));
        p.setPreferredSize(new Dimension(0, 110));
        JLabel lv = UITheme.label(value, new Font("Arial", Font.BOLD, 38), accent);
        p.add(lv, BorderLayout.CENTER);
        JLabel ll = UITheme.label(label, UITheme.FONT_BODY, UITheme.TEXT_MID);
        p.add(ll, BorderLayout.SOUTH);
        return p;
    }
}
