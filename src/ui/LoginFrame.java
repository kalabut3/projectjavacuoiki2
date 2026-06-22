package ui;

import service.AuthService;
import model.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {

    private JTextField     txtUser;
    private JPasswordField txtPass;
    private JLabel          lbError;

    public LoginFrame() { init(); }

    void init() {
        setTitle("Task Master");
        setSize(480, 580);
        setMinimumSize(new Dimension(420, 520));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        // Root – gradient background, rounded, BorderLayout: [topbar][center form]
        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(10, 12, 30), 0, getHeight(), new Color(18, 22, 52));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                GradientPaint bar = new GradientPaint(0, 0, UITheme.ACCENT, getWidth(), 0, UITheme.ACCENT2);
                g2.setPaint(bar);
                g2.fillRoundRect(0, 0, getWidth(), 4, 4, 4);
            }
        };
        root.setOpaque(false);
        setContentPane(root);

        // drag-to-move
        Point[] origin = {null};
        root.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { origin[0] = e.getPoint(); }
        });
        root.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (origin[0] == null) return;
                Point l = getLocation();
                setLocation(l.x + e.getX() - origin[0].x, l.y + e.getY() - origin[0].y);
            }
        });

        root.add(buildTopBar(), BorderLayout.NORTH);
        root.add(buildCenterForm(), BorderLayout.CENTER);
    }

    /* ── Close button row ── */
    private JComponent buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(10, 0, 0, 10));

        JButton btnX = new JButton("✕");
        btnX.setForeground(UITheme.TEXT_LOW);
        btnX.setBackground(new Color(0, 0, 0, 0));
        btnX.setBorderPainted(false);
        btnX.setFocusPainted(false);
        btnX.setContentAreaFilled(false);
        btnX.setFont(new Font("Arial", Font.BOLD, 15));
        btnX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnX.setPreferredSize(new Dimension(34, 34));
        btnX.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnX.setForeground(UITheme.RED); }
            public void mouseExited(MouseEvent e) { btnX.setForeground(UITheme.TEXT_LOW); }
        });
        btnX.addActionListener(e -> System.exit(0));

        bar.add(btnX, BorderLayout.EAST);
        return bar;
    }

    /* ── Center form, vertically & horizontally centered via GridBagLayout ── */
    private JComponent buildCenterForm() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);
        GridBagConstraints outer = new GridBagConstraints();
        outer.gridx = 0; outer.gridy = 0;
        outer.weightx = 1; outer.weighty = 1;
        outer.anchor = GridBagConstraints.CENTER;

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(new EmptyBorder(0, 40, 0, 40));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = new Insets(4, 0, 4, 0);
        int row = 0;

        // logo
        JPanel logo = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, UITheme.ACCENT, 60, 60, UITheme.ACCENT2);
                g2.setPaint(gp);
                g2.fillOval(0, 0, 60, 60);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 28));
                g2.drawString("T", 18, 42);
            }
        };
        logo.setOpaque(false);
        logo.setPreferredSize(new Dimension(60, 60));
        JPanel logoHolder = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        logoHolder.setOpaque(false);
        logoHolder.add(logo);
        c.gridy = row++; c.insets = new Insets(10, 0, 10, 0);
        form.add(logoHolder, c);

        // title
        JLabel lbT = UITheme.label("TASK MASTER", UITheme.FONT_TITLE, UITheme.TEXT_HI);
        lbT.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridy = row++; c.insets = new Insets(4, 0, 2, 0);
        form.add(lbT, c);

        JLabel lbSub = UITheme.label("Đăng nhập để tiếp tục", UITheme.FONT_SMALL, UITheme.TEXT_LOW);
        lbSub.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridy = row++; c.insets = new Insets(0, 0, 14, 0);
        form.add(lbSub, c);

        c.gridy = row++; c.insets = new Insets(0, 0, 16, 0);
        form.add(UITheme.hSep(), c);

        // username
        JLabel lu = UITheme.label("TÊN ĐĂNG NHẬP", UITheme.FONT_LABEL, UITheme.TEXT_MID);
        c.gridy = row++; c.insets = new Insets(0, 0, 6, 0);
        form.add(lu, c);

        txtUser = UITheme.field();
        c.gridy = row++; c.insets = new Insets(0, 0, 16, 0);
        form.add(txtUser, c);

        // password
        JLabel lp = UITheme.label("MẬT KHẨU", UITheme.FONT_LABEL, UITheme.TEXT_MID);
        c.gridy = row++; c.insets = new Insets(0, 0, 6, 0);
        form.add(lp, c);

        txtPass = UITheme.passField();
        c.gridy = row++; c.insets = new Insets(0, 0, 8, 0);
        form.add(txtPass, c);

        // error
        lbError = UITheme.label(" ", UITheme.FONT_SMALL, UITheme.RED);
        lbError.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridy = row++; c.insets = new Insets(0, 0, 10, 0);
        form.add(lbError, c);

        // login button
        JButton btnLogin = UITheme.pillBtn("ĐĂNG NHẬP", UITheme.ACCENT);
        btnLogin.setPreferredSize(new Dimension(100, 50));
        c.gridy = row++; c.insets = new Insets(0, 0, 18, 0);
        form.add(btnLogin, c);

        // footer
        JLabel foot = UITheme.label("© 2025 Task Master System", UITheme.FONT_SMALL, UITheme.TEXT_LOW);
        foot.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridy = row++; c.insets = new Insets(0, 0, 6, 0);
        form.add(foot, c);

        // events
        KeyAdapter enter = new KeyAdapter() {
            public void keyPressed(KeyEvent e) { if (e.getKeyCode() == KeyEvent.VK_ENTER) doLogin(); }
        };
        txtUser.addKeyListener(enter);
        txtPass.addKeyListener(enter);
        btnLogin.addActionListener(e -> doLogin());

        wrap.add(form, outer);
        return wrap;
    }

    private void doLogin() {
        String u = txtUser.getText().trim();
        String p = String.valueOf(txtPass.getPassword());
        if (u.isEmpty() || p.isEmpty()) {
            lbError.setText("Vui lòng điền đầy đủ thông tin!");
            return;
        }
        User user = new AuthService().login(u, p);
        if (user != null) {
            lbError.setText(" ");
            new UserDashboardFrame(user).setVisible(true);
            dispose();
        } else {
            lbError.setText("Sai tài khoản hoặc mật khẩu!");
            txtPass.setText("");
        }
    }
}
