package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ImportExportPanel extends JPanel {
    private JButton btnImport, btnExport;
    private JTextArea logArea;

    public ImportExportPanel() {
        setLayout(new BorderLayout(0, 16));
        setBackground(UITheme.BG_APP);
        setBorder(new EmptyBorder(20, 24, 20, 24));

        JLabel title = UITheme.label("IMPORT / EXPORT DỮ LIỆU", UITheme.FONT_SECTION, UITheme.TEXT_HI);
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 16));
        center.setOpaque(false);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        btnRow.setOpaque(false);
        btnImport = UITheme.pillBtn("↑ Import CSV", UITheme.ACCENT);
        btnImport.setPreferredSize(new Dimension(160, 46));
        btnExport = UITheme.pillBtn("↓ Export CSV", UITheme.GREEN);
        btnExport.setPreferredSize(new Dimension(160, 46));
        btnRow.add(btnImport);
        btnRow.add(btnExport);
        center.add(btnRow, BorderLayout.NORTH);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(UITheme.BG_CARD);
        logArea.setForeground(UITheme.TEXT_HI);
        logArea.setFont(UITheme.FONT_MONO);
        JScrollPane sp = new JScrollPane(logArea);
        sp.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
        center.add(sp, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);
    }
}
