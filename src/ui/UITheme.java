package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Shared design tokens & helper factory for all UI panels.
 * Dark indigo theme — consistent palette, rounded controls, hover states.
 *
 * Layout note: panels in this app now use LayoutManagers (BorderLayout,
 * GridBagLayout, BoxLayout) instead of absolute positioning, so the UI
 * resizes correctly when the window is resized. The gbc(...) helper below
 * is a small convenience builder for GridBagConstraints used throughout
 * the form panels.
 */
public class UITheme {

    /* ── Palette ── */
    public static final Color BG_APP       = new Color(8,  11, 26);
    public static final Color BG_SIDEBAR   = new Color(12, 15, 36);
    public static final Color BG_TOPBAR    = new Color(14, 18, 42);
    public static final Color BG_CARD      = new Color(20, 25, 56);
    public static final Color BG_FIELD     = new Color(24, 29, 62);
    public static final Color BG_ROW_ALT   = new Color(17, 22, 48);
    public static final Color BG_SEL       = new Color(99, 102, 241, 55);

    public static final Color ACCENT       = new Color(99,  102, 241);
    public static final Color ACCENT2      = new Color(139,  92, 246);
    public static final Color GREEN        = new Color(16,  185, 129);
    public static final Color ORANGE       = new Color(245, 158,  11);
    public static final Color RED          = new Color(239,  68,  68);
    public static final Color CYAN         = new Color(6,   182, 212);

    public static final Color TEXT_HI      = new Color(240, 242, 255);
    public static final Color TEXT_MID     = new Color(160, 168, 210);
    public static final Color TEXT_LOW     = new Color(90,  100, 150);

    public static final Color BORDER       = new Color(35,  42,  80);
    public static final Color BORDER_LIGHT = new Color(55,  65, 110);

    /* ── Typography ── */
    public static final Font FONT_TITLE   = new Font("Arial", Font.BOLD,  22);
    public static final Font FONT_SECTION = new Font("Arial", Font.BOLD,  13);
    public static final Font FONT_LABEL   = new Font("Arial", Font.BOLD,  11);
    public static final Font FONT_BODY    = new Font("Arial", Font.PLAIN, 13);
    public static final Font FONT_MONO    = new Font("Monospaced", Font.PLAIN, 13);
    public static final Font FONT_SMALL   = new Font("Arial", Font.PLAIN, 11);

    /* ── Spacing tokens (used by GridBagLayout insets) ── */
    public static final int GAP_SM = 6;
    public static final int GAP_MD = 12;
    public static final int GAP_LG = 20;

    /* ── Factory: Label ── */
    public static JLabel label(String text, Font f, Color c) {
        JLabel l = new JLabel(text);
        l.setFont(f); l.setForeground(c);
        return l;
    }

    /* ── Factory: Field ── */
    public static JTextField field() {
        JTextField f = new JTextField();
        styleField(f); return f;
    }

    public static JPasswordField passField() {
        JPasswordField f = new JPasswordField();
        styleField(f); return f;
    }

    public static void styleField(JTextField f) {
        f.setBackground(BG_FIELD);
        f.setForeground(TEXT_HI);
        f.setCaretColor(ACCENT);
        f.setFont(FONT_BODY);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_LIGHT, 1, true),
            new EmptyBorder(6, 14, 6, 14)
        ));
        // Reasonable minimum so fields don't collapse to nothing when the
        // container shrinks, while still allowing the layout to grow them.
        f.setPreferredSize(new Dimension(f.getPreferredSize().width, 36));
    }

    /* ── Factory: ComboBox ── */
    public static JComboBox<String> combo(String... items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setBackground(BG_FIELD);
        cb.setForeground(TEXT_HI);
        cb.setFont(FONT_BODY);
        cb.setBorder(BorderFactory.createLineBorder(BORDER_LIGHT, 1));
        cb.setPreferredSize(new Dimension(cb.getPreferredSize().width, 36));
        return cb;
    }

    /* ── Factory: Pill Button ── */
    public static JButton pillBtn(String text, Color bg) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isPressed()  ? bg.darker()
                        : getModel().isRollover() ? bg.brighter() : bg;
                g2.setColor(c);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth()  - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
            }
        };
        b.setFont(FONT_SECTION);
        b.setForeground(Color.WHITE);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(120, 38));
        return b;
    }

    /* ── Factory: Ghost (nav) Button ── */
    public static JButton navBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(FONT_BODY);
        b.setForeground(TEXT_MID);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorder(new EmptyBorder(0, 16, 0, 0));
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        b.setPreferredSize(new Dimension(230, 44));
        return b;
    }

    /* ── Factory: Section panel (card). Uses BorderLayout by default so
       callers can drop a header at NORTH and content at CENTER. ── */
    public static JPanel card() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createLineBorder(BORDER, 1, true));
        return p;
    }

    /** Card with no layout manager pre-assigned (caller sets its own). */
    public static JPanel cardRaw() {
        JPanel p = new JPanel();
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createLineBorder(BORDER, 1, true));
        return p;
    }

    /* ── Separator ── */
    public static JSeparator hSep() {
        JSeparator s = new JSeparator();
        s.setForeground(BORDER);
        return s;
    }

    /* ── GridBagConstraints helper ──
       gbc(gridx, gridy, gridwidth, weightx) — fills horizontally, anchors
       NORTHWEST, with standard padding. Used by all form-style panels. */
    public static GridBagConstraints gbc(int x, int y, int w, double weightx) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x; c.gridy = y; c.gridwidth = w;
        c.weightx = weightx;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new Insets(GAP_SM, GAP_MD, GAP_SM, GAP_MD);
        return c;
    }

    /** Standard empty border padding for cards/content panels. */
    public static EmptyBorder padding(int amount) {
        return new EmptyBorder(amount, amount, amount, amount);
    }

    /* ── Status badge colour ── */
    public static Color statusColor(String s) {
        if (s == null) return TEXT_MID;
        return switch (s.toUpperCase()) {
            case "DONE"   -> GREEN;
            case "DOING"  -> ORANGE;
            case "TODO"   -> RED;
            case "HIGH"   -> RED;
            case "MEDIUM" -> ORANGE;
            case "LOW"    -> GREEN;
            case "ADMIN"  -> ACCENT2;
            default       -> TEXT_MID;
        };
    }
}
