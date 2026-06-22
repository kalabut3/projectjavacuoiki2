package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SearchPanel extends JPanel {
    private JTextField txtSearch;
    private JButton btnSearch;

    public SearchPanel() {
        setLayout(new BorderLayout(0, 6));
        setOpaque(false);
        setBorder(new EmptyBorder(0, 16, 16, 16));

        JLabel lb = UITheme.label("TÌM KIẾM", UITheme.FONT_LABEL, UITheme.TEXT_MID);
        add(lb, BorderLayout.NORTH);

        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        txtSearch = UITheme.field();
        row.add(txtSearch, BorderLayout.CENTER);
        btnSearch = UITheme.pillBtn("🔍 Tìm", UITheme.ACCENT);
        btnSearch.setPreferredSize(new Dimension(110, 42));
        row.add(btnSearch, BorderLayout.EAST);
        add(row, BorderLayout.CENTER);
    }

    public String getKeyword() { return txtSearch.getText(); }
}
