package ui;

import model.Log;
import service.LogService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;

public class LogPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public LogPanel() { init(); }

    private void init() {
        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel card = UITheme.card();
        add(card, BorderLayout.CENTER);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(14, 16, 10, 16));
        header.add(UITheme.label("NHẬT KÝ HOẠT ĐỘNG HỆ THỐNG", UITheme.FONT_SECTION, UITheme.TEXT_HI), BorderLayout.WEST);

        JButton btnRefresh = UITheme.pillBtn("↺ Làm mới", UITheme.ACCENT);
        btnRefresh.setPreferredSize(new Dimension(120, 36));
        header.add(btnRefresh, BorderLayout.EAST);

        JPanel headerWrap = new JPanel(new BorderLayout());
        headerWrap.setOpaque(false);
        headerWrap.add(header, BorderLayout.NORTH);
        headerWrap.add(sepWithMargin(), BorderLayout.SOUTH);
        card.add(headerWrap, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Username", "Hành động", "Thời gian"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        CategoryPanel.applyTableStyle(table);
        table.setRowHeight(42);

        table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable tbl, Object v, boolean s, boolean f, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(tbl, v, s, f, r, c);
                if (!s) l.setBackground(r % 2 == 0 ? UITheme.BG_CARD : UITheme.BG_ROW_ALT);
                l.setForeground(UITheme.GREEN);
                l.setFont(new Font("Arial", Font.BOLD, 12));
                l.setBorder(new EmptyBorder(0, 10, 0, 0));
                return l;
            }
        });
        table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable tbl, Object v, boolean s, boolean f, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(tbl, v, s, f, r, c);
                if (!s) l.setBackground(r % 2 == 0 ? UITheme.BG_CARD : UITheme.BG_ROW_ALT);
                l.setForeground(new Color(180, 188, 230));
                l.setFont(UITheme.FONT_BODY);
                l.setBorder(new EmptyBorder(0, 10, 0, 0));
                return l;
            }
        });

        JScrollPane sc = new JScrollPane(table);
        sc.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(0, 16, 16, 16),
                BorderFactory.createLineBorder(UITheme.BORDER, 1)));
        sc.getViewport().setBackground(UITheme.BG_CARD);
        card.add(sc, BorderLayout.CENTER);

        loadTable();
        btnRefresh.addActionListener(e -> loadTable());
    }

    private JSeparator sepWithMargin() {
        JSeparator s = UITheme.hSep();
        s.setBorder(new EmptyBorder(8, 16, 0, 16));
        return s;
    }

    private void loadTable() {
        model.setRowCount(0);
        for (Log l : new LogService().getAll())
            model.addRow(new Object[]{l.getId(), l.getUsername(), l.getAction(), l.getTime()});
    }
}
