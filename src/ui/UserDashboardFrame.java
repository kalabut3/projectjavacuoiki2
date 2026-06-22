package ui;

import model.User;
import model.Task;
import service.TaskService;
import service.StatisticsService;
import util.ImportCSV;
import util.ExportCSV;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class UserDashboardFrame extends JFrame {

    private User    currentUser;
    private JPanel  contentArea;
    private JButton activeNavBtn;

    // nav buttons kept as fields so we can toggle active state
    private JButton btnDash, btnTask, btnCat, btnUser, btnImport, btnExport, btnLog;

    public UserDashboardFrame(User user) {
        this.currentUser = user;
        setTitle("Task Master");
        setSize(1440, 840);
        setMinimumSize(new Dimension(1100, 680));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(UITheme.BG_APP);
        setLayout(new BorderLayout());

        add(buildSidebar(), BorderLayout.WEST);

        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(UITheme.BG_APP);
        right.add(buildTopbar(), BorderLayout.NORTH);
        right.add(buildContentArea(), BorderLayout.CENTER);
        add(right, BorderLayout.CENTER);

        showDashboard();
    }

    /* ════════════════════ SIDEBAR ════════════════════ */
    private JComponent buildSidebar() {
        JPanel sb = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(UITheme.BG_SIDEBAR);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(UITheme.BORDER);
                g.fillRect(getWidth() - 1, 0, 1, getHeight());
            }
        };
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS));
        sb.setOpaque(false);
        sb.setPreferredSize(new Dimension(230, 0));

        // logo
        JPanel logoBox = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.BG_SIDEBAR);
                g2.fillRect(0, 0, getWidth(), getHeight());
                GradientPaint gp = new GradientPaint(14, 14, UITheme.ACCENT, 50, 50, UITheme.ACCENT2);
                g2.setPaint(gp);
                g2.fillOval(14, 10, 46, 46);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 22));
                g2.drawString("T", 28, 40);
            }
        };
        logoBox.setOpaque(false);
        logoBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
        logoBox.setPreferredSize(new Dimension(230, 68));
        logoBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoBox.setBorder(new EmptyBorder(0, 68, 0, 0));

        JLabel lbApp = UITheme.label("TASK MASTER", new Font("Arial", Font.BOLD, 15), UITheme.TEXT_HI);
        logoBox.add(lbApp, BorderLayout.CENTER);
        sb.add(logoBox);

        JSeparator s1 = UITheme.hSep();
        s1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        s1.setAlignmentX(Component.LEFT_ALIGNMENT);
        sb.add(s1);

        sb.add(Box.createVerticalStrut(14));

        // section label
        JLabel secMain = sectionLabel("MENU CHÍNH");
        sb.add(secMain);
        sb.add(Box.createVerticalStrut(6));

        btnDash = navEntry(sb, "⊞  Dashboard");
        btnTask = navEntry(sb, "✔  Công việc");
        btnCat  = navEntry(sb, "◈  Danh mục");

        if (currentUser.getRole().equals("ADMIN")) {
            sb.add(Box.createVerticalStrut(8));
            JSeparator s2 = UITheme.hSep();
            s2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            s2.setAlignmentX(Component.LEFT_ALIGNMENT);
            sb.add(s2);
            sb.add(Box.createVerticalStrut(14));

            JLabel secAdmin = sectionLabel("QUẢN TRỊ");
            sb.add(secAdmin);
            sb.add(Box.createVerticalStrut(6));

            btnUser   = navEntry(sb, "👤  Người dùng");
            btnImport = navEntry(sb, "↑  Import CSV");
            btnExport = navEntry(sb, "↓  Export CSV");
            btnLog    = navEntry(sb, "☰  Nhật ký");

            btnUser.addActionListener(e -> show(new UserPanel(), btnUser));
            btnImport.addActionListener(e -> {
                ArrayList<Task> list = ImportCSV.importFile();
                TaskService svc = new TaskService();
                for (Task t : list) svc.addTask(t);
                JOptionPane.showMessageDialog(this, "Import thành công!");
                showDashboard();
            });
            btnExport.addActionListener(e -> {
                ExportCSV.export(new TaskService().getAll());
                JOptionPane.showMessageDialog(this, "Xuất CSV thành công!");
            });
            btnLog.addActionListener(e -> show(new LogPanel(), btnLog));
        }

        btnDash.addActionListener(e -> { showDashboard(); setActive(btnDash); });
        btnTask.addActionListener(e -> show(new TaskPanel(), btnTask));
        btnCat.addActionListener(e -> show(new CategoryPanel(), btnCat));

        // spacer pushes logout button to the bottom
        sb.add(Box.createVerticalGlue());

        JButton btnOut = UITheme.navBtn("⟵  Đăng xuất");
        btnOut.setForeground(UITheme.RED);
        btnOut.setPreferredSize(new Dimension(230, 46));
        btnOut.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnOut.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnOut.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION) == 0) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });
        sb.add(btnOut);
        sb.add(Box.createVerticalStrut(12));

        setActive(btnDash);
        return sb;
    }

    private JLabel sectionLabel(String text) {
        JLabel l = UITheme.label(text, UITheme.FONT_LABEL, UITheme.TEXT_LOW);
        l.setBorder(new EmptyBorder(0, 20, 0, 0));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setMaximumSize(new Dimension(Integer.MAX_VALUE, 16));
        return l;
    }

    private JButton navEntry(JPanel parent, String text) {
        JButton b = UITheme.navBtn(text);
        parent.add(b);
        return b;
    }

    private void setActive(JButton btn) {
        if (activeNavBtn != null) {
            activeNavBtn.setForeground(UITheme.TEXT_MID);
            activeNavBtn.setOpaque(false);
            activeNavBtn.setBackground(new Color(0, 0, 0, 0));
        }
        activeNavBtn = btn;
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setBackground(new Color(UITheme.ACCENT.getRed(), UITheme.ACCENT.getGreen(), UITheme.ACCENT.getBlue(), 45));
    }

    /* ════════════════════ TOPBAR ════════════════════ */
    private JComponent buildTopbar() {
        JPanel top = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(UITheme.BG_TOPBAR);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(UITheme.BORDER);
                g.fillRect(0, getHeight() - 1, getWidth(), 1);
            }
        };
        top.setOpaque(false);
        top.setPreferredSize(new Dimension(0, 64));
        top.setBorder(new EmptyBorder(0, 24, 0, 16));

        JLabel pgTitle = UITheme.label("Bảng điều khiển", new Font("Arial", Font.BOLD, 17), UITheme.TEXT_HI);
        top.add(pgTitle, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        right.setOpaque(false);

        JLabel greet = UITheme.label("Xin chào, " + currentUser.getFullname() + " (" + currentUser.getRole() + ")",
                UITheme.FONT_BODY, UITheme.TEXT_MID);
        right.add(greet);

        JPanel av = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, UITheme.ACCENT, 38, 38, UITheme.ACCENT2);
                g2.setPaint(gp);
                g2.fillOval(0, 0, 38, 38);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 17));
                String init = currentUser.getFullname().length() > 0 ? currentUser.getFullname().substring(0, 1).toUpperCase() : "U";
                g2.drawString(init, 12, 26);
            }
        };
        av.setOpaque(false);
        av.setPreferredSize(new Dimension(38, 38));
        right.add(av);

        top.add(right, BorderLayout.EAST);
        return top;
    }

    /* ════════════════════ CONTENT ════════════════════ */
    private JComponent buildContentArea() {
        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(UITheme.BG_APP);
        contentArea.setBorder(new EmptyBorder(20, 20, 20, 20));
        return contentArea;
    }

    /* ════════════════════ DASHBOARD ════════════════════ */
    private void showDashboard() {
        contentArea.removeAll();

        StatisticsService s = new StatisticsService();
        int total = s.getTotal(), done = s.getDone(), doing = s.getDoing(), todo = s.getTodo();

        JPanel page = new JPanel(new BorderLayout(0, 16));
        page.setOpaque(false);

        // stat cards row
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 16, 0));
        statsRow.setOpaque(false);
        statsRow.add(statCard("Tổng", String.valueOf(total), UITheme.ACCENT));
        statsRow.add(statCard("Hoàn thành", String.valueOf(done), UITheme.GREEN));
        statsRow.add(statCard("Đang làm", String.valueOf(doing), UITheme.ORANGE));
        statsRow.add(statCard("Chưa làm", String.valueOf(todo), UITheme.RED));
        page.add(statsRow, BorderLayout.NORTH);

        // chart + recent tasks row
        JPanel midRow = new JPanel(new GridLayout(1, 2, 16, 0));
        midRow.setOpaque(false);

        JPanel chartCard = UITheme.card();
        chartCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                new EmptyBorder(12, 18, 12, 18)));
        JLabel ct = UITheme.label("THỐNG KÊ THEO TRẠNG THÁI", UITheme.FONT_LABEL, UITheme.TEXT_MID);
        chartCard.add(ct, BorderLayout.NORTH);
        ChartPanel chart = new ChartPanel();
        chartCard.add(chart, BorderLayout.CENTER);
        midRow.add(chartCard);

        JPanel recentCard = UITheme.card();
        recentCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                new EmptyBorder(12, 18, 12, 18)));
        JLabel rt = UITheme.label("CÔNG VIỆC GẦN ĐÂY", UITheme.FONT_LABEL, UITheme.TEXT_MID);
        recentCard.add(rt, BorderLayout.NORTH);

        JPanel recentList = new JPanel();
        recentList.setLayout(new BoxLayout(recentList, BoxLayout.Y_AXIS));
        recentList.setOpaque(false);
        recentList.setBorder(new EmptyBorder(8, 0, 0, 0));

        TaskService ts = new TaskService();
        ArrayList<Task> list = ts.getAll();
        int maxRows = Math.min(list.size(), 7);
        for (int i = 0; i < maxRows; i++) {
            Task t = list.get(i);
            JPanel row = new JPanel(new BorderLayout());
            row.setBackground(i % 2 == 0 ? UITheme.BG_CARD : UITheme.BG_ROW_ALT);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            row.setPreferredSize(new Dimension(10, 40));
            row.setBorder(new EmptyBorder(0, 14, 0, 14));

            JLabel tl = UITheme.label(t.getTitle(), UITheme.FONT_BODY, UITheme.TEXT_HI);
            row.add(tl, BorderLayout.WEST);

            JPanel rightSide = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
            rightSide.setOpaque(false);
            JLabel sl = UITheme.label("● " + t.getStatus(), UITheme.FONT_LABEL, UITheme.statusColor(t.getStatus()));
            rightSide.add(sl);
            JLabel dl = UITheme.label(t.getEndDate() != null ? t.getEndDate() : "", UITheme.FONT_SMALL, UITheme.TEXT_LOW);
            rightSide.add(dl);
            row.add(rightSide, BorderLayout.EAST);

            recentList.add(row);
        }
        recentCard.add(recentList, BorderLayout.CENTER);
        midRow.add(recentCard);

        page.add(midRow, BorderLayout.CENTER);

        contentArea.add(page, BorderLayout.CENTER);
        contentArea.repaint();
        contentArea.revalidate();
    }

    private JPanel statCard(String label, String value, Color accent) {
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

    private void show(JPanel panel, JButton btn) {
        setActive(btn);
        contentArea.removeAll();
        contentArea.add(panel, BorderLayout.CENTER);
        contentArea.repaint();
        contentArea.revalidate();
    }
}
