package ui;

import model.Category;
import service.CategoryService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;

public class CategoryPanel extends JPanel {

    private JTextField txtName, txtDesc;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private JTable table;
    private DefaultTableModel model;
    private int selectedId = -1;

    public CategoryPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildForm(), buildTable());
        split.setResizeWeight(0);
        split.setDividerSize(10);
        split.setBorder(null);
        split.setOpaque(false);
        split.setContinuousLayout(true);

        add(split, BorderLayout.CENTER);
        loadTable();
    }

    private JComponent buildForm() {
        JPanel card = UITheme.card();
        card.setPreferredSize(new Dimension(310, 0));
        card.setMinimumSize(new Dimension(260, 0));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(14, 16, 10, 16));
        header.add(UITheme.label("DANH MỤC", UITheme.FONT_SECTION, UITheme.TEXT_HI), BorderLayout.NORTH);
        header.add(UITheme.hSep(), BorderLayout.SOUTH);
        card.add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(new EmptyBorder(8, 16, 16, 16));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
        int row = 0;

        c.gridy = row++; c.insets = new Insets(4, 0, 4, 0);
        form.add(UITheme.label("Tên danh mục", UITheme.FONT_LABEL, UITheme.TEXT_MID), c);
        txtName = UITheme.field();
        c.gridy = row++; c.insets = new Insets(0, 0, 10, 0);
        form.add(txtName, c);

        c.gridy = row++; c.insets = new Insets(4, 0, 4, 0);
        form.add(UITheme.label("Mô tả", UITheme.FONT_LABEL, UITheme.TEXT_MID), c);
        txtDesc = UITheme.field();
        c.gridy = row++; c.insets = new Insets(0, 0, 14, 0);
        form.add(txtDesc, c);

        btnAdd    = UITheme.pillBtn("+ Thêm", UITheme.ACCENT);
        btnUpdate = UITheme.pillBtn("✎ Sửa", UITheme.GREEN);
        btnDelete = UITheme.pillBtn("✕ Xóa", UITheme.RED);
        btnClear  = UITheme.pillBtn("↺ Mới", new Color(45, 52, 90));

        JPanel row1 = new JPanel(new GridLayout(1, 2, 8, 0));
        row1.setOpaque(false);
        row1.add(btnAdd); row1.add(btnUpdate);
        c.gridy = row++; c.insets = new Insets(0, 0, 8, 0);
        form.add(row1, c);

        JPanel row2 = new JPanel(new GridLayout(1, 2, 8, 0));
        row2.setOpaque(false);
        row2.add(btnDelete); row2.add(btnClear);
        c.gridy = row++; c.insets = new Insets(0, 0, 0, 0);
        form.add(row2, c);

        // pushes content to top
        c.gridy = row; c.weighty = 1; c.fill = GridBagConstraints.VERTICAL;
        form.add(Box.createVerticalGlue(), c);

        btnAdd.addActionListener(e -> {
            Category cat = new Category();
            cat.setName(txtName.getText());
            cat.setDescription(txtDesc.getText());
            new CategoryService().addCategory(cat);
            loadTable(); clearForm(); ok("Thêm thành công!");
        });
        btnUpdate.addActionListener(e -> {
            if (selectedId < 0) return;
            Category cat = new Category();
            cat.setId(selectedId);
            cat.setName(txtName.getText());
            cat.setDescription(txtDesc.getText());
            new CategoryService().updateCategory(cat);
            loadTable(); clearForm(); ok("Cập nhật thành công!");
        });
        btnDelete.addActionListener(e -> {
            if (selectedId < 0) return;
            if (JOptionPane.showConfirmDialog(this, "Xóa danh mục?", "Xác nhận", JOptionPane.YES_NO_OPTION) == 0) {
                new CategoryService().deleteCategory(selectedId);
                loadTable(); clearForm();
            }
        });
        btnClear.addActionListener(e -> clearForm());

        card.add(form, BorderLayout.CENTER);
        return card;
    }

    private JComponent buildTable() {
        JPanel card = UITheme.card();
        card.setMinimumSize(new Dimension(380, 0));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(14, 16, 10, 16));
        header.add(UITheme.label("DANH SÁCH DANH MỤC", UITheme.FONT_SECTION, UITheme.TEXT_HI), BorderLayout.WEST);
        card.add(header, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Tên danh mục", "Mô tả"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        applyTableStyle(table);

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) return;
            selectedId = Integer.parseInt(model.getValueAt(row, 0).toString());
            txtName.setText(model.getValueAt(row, 1).toString());
            txtDesc.setText(model.getValueAt(row, 2).toString());
        });

        JScrollPane sc = new JScrollPane(table);
        sc.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(0, 16, 16, 16),
                BorderFactory.createLineBorder(UITheme.BORDER, 1)));
        sc.getViewport().setBackground(UITheme.BG_CARD);
        card.add(sc, BorderLayout.CENTER);

        return card;
    }

    private void loadTable() {
        model.setRowCount(0);
        for (Category c : new CategoryService().getAll())
            model.addRow(new Object[]{c.getId(), c.getName(), c.getDescription()});
    }

    private void clearForm() { txtName.setText(""); txtDesc.setText(""); selectedId = -1; table.clearSelection(); }
    private void ok(String m) { JOptionPane.showMessageDialog(this, m); }

    static void applyTableStyle(JTable t) {
        t.setBackground(UITheme.BG_CARD);
        t.setForeground(UITheme.TEXT_HI);
        t.setGridColor(UITheme.BORDER);
        t.setRowHeight(40);
        t.setFont(UITheme.FONT_BODY);
        t.setSelectionBackground(UITheme.BG_SEL);
        t.setSelectionForeground(Color.WHITE);
        t.setShowVerticalLines(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        t.getTableHeader().setBackground(new Color(20, 26, 56));
        t.getTableHeader().setForeground(UITheme.TEXT_MID);
        t.getTableHeader().setFont(UITheme.FONT_LABEL);
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable tbl, Object v, boolean s, boolean f, int r, int c) {
                Component comp = super.getTableCellRendererComponent(tbl, v, s, f, r, c);
                if (!s) comp.setBackground(r % 2 == 0 ? UITheme.BG_CARD : UITheme.BG_ROW_ALT);
                comp.setForeground(UITheme.TEXT_HI);
                ((JLabel) comp).setBorder(new EmptyBorder(0, 10, 0, 0));
                return comp;
            }
        });
    }
}
