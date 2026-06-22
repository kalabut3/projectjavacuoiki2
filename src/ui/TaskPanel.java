package ui;

import model.Log;
import model.Task;
import service.LogService;
import service.TaskService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;

public class TaskPanel extends JPanel {

    private JTextField  txtTitle, txtStart, txtEnd, txtSearch;
    private JTextArea   txtDesc;
    private JComboBox<String> cbCat, cbPri, cbStatus;
    private JButton     btnAdd, btnUpdate, btnDelete, btnClear, btnSearch;
    private JTable      table;
    private DefaultTableModel model;
    private int selectedId = -1;

    public TaskPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildForm(), buildTable());
        split.setResizeWeight(0); // table side grows preferentially
        split.setDividerSize(10);
        split.setBorder(null);
        split.setOpaque(false);
        split.setContinuousLayout(true);

        add(split, BorderLayout.CENTER);
        loadTable();
    }

    /* ── LEFT: Form card ── */
    private JComponent buildForm() {
        JPanel card = UITheme.card();
        card.setPreferredSize(new Dimension(310, 0));
        card.setMinimumSize(new Dimension(280, 0));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(14, 16, 10, 16));
        JLabel sectionTitle = UITheme.label("THÊM / SỬA CÔNG VIỆC", UITheme.FONT_SECTION, UITheme.TEXT_HI);
        header.add(sectionTitle, BorderLayout.NORTH);
        JSeparator sep = UITheme.hSep();
        header.add(sep, BorderLayout.SOUTH);
        card.add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(new EmptyBorder(8, 16, 16, 16));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
        c.insets = new Insets(4, 0, 4, 0);
        int row = 0;

        c.gridy = row++; form.add(formLabel("Tiêu đề"), c);
        txtTitle = UITheme.field();
        c.gridy = row++; c.insets = new Insets(0, 0, 10, 0); form.add(txtTitle, c);

        c.insets = new Insets(4, 0, 4, 0);
        c.gridy = row++; form.add(formLabel("Danh mục"), c);
        cbCat = UITheme.combo("Học tập", "Công việc", "Gia đình");
        c.gridy = row++; c.insets = new Insets(0, 0, 10, 0); form.add(cbCat, c);

        c.insets = new Insets(4, 0, 4, 0);
        c.gridy = row++; form.add(formLabel("Ưu tiên"), c);
        cbPri = UITheme.combo("HIGH", "MEDIUM", "LOW");
        c.gridy = row++; c.insets = new Insets(0, 0, 10, 0); form.add(cbPri, c);

        c.insets = new Insets(4, 0, 4, 0);
        c.gridy = row++; form.add(formLabel("Trạng thái"), c);
        cbStatus = UITheme.combo("TODO", "DOING", "DONE");
        c.gridy = row++; c.insets = new Insets(0, 0, 10, 0); form.add(cbStatus, c);

        c.insets = new Insets(4, 0, 4, 0);
        c.gridy = row++; form.add(formLabel("Bắt đầu"), c);
        txtStart = UITheme.field();
        c.gridy = row++; c.insets = new Insets(0, 0, 10, 0); form.add(txtStart, c);

        c.insets = new Insets(4, 0, 4, 0);
        c.gridy = row++; form.add(formLabel("Hạn cuối"), c);
        txtEnd = UITheme.field();
        c.gridy = row++; c.insets = new Insets(0, 0, 10, 0); form.add(txtEnd, c);

        c.insets = new Insets(4, 0, 4, 0);
        c.gridy = row++; form.add(formLabel("Mô tả"), c);

        txtDesc = new JTextArea();
        txtDesc.setBackground(UITheme.BG_FIELD);
        txtDesc.setForeground(UITheme.TEXT_HI);
        txtDesc.setCaretColor(UITheme.ACCENT);
        txtDesc.setFont(UITheme.FONT_BODY);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        JScrollPane ds = new JScrollPane(txtDesc);
        ds.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_LIGHT, 1));
        ds.setPreferredSize(new Dimension(10, 90));
        c.gridy = row++; c.insets = new Insets(0, 0, 12, 0);
        c.weighty = 1; c.fill = GridBagConstraints.BOTH;
        form.add(ds, c);
        c.weighty = 0; c.fill = GridBagConstraints.HORIZONTAL;

        // button row 1
        btnAdd    = UITheme.pillBtn("+ Thêm", UITheme.ACCENT);
        btnDelete = UITheme.pillBtn("✕ Xóa", UITheme.RED);
        JPanel row1 = new JPanel(new GridLayout(1, 2, 8, 0));
        row1.setOpaque(false);
        row1.add(btnAdd);
        row1.add(btnDelete);
        c.gridy = row++; c.insets = new Insets(0, 0, 8, 0);
        form.add(row1, c);

        // button row 2
        btnUpdate = UITheme.pillBtn("✎ Cập nhật", UITheme.GREEN);
        btnClear  = UITheme.pillBtn("↺ Mới", new Color(45, 52, 90));
        JPanel row2 = new JPanel(new GridLayout(1, 2, 8, 0));
        row2.setOpaque(false);
        row2.add(btnUpdate);
        row2.add(btnClear);
        c.gridy = row++; c.insets = new Insets(0, 0, 0, 0);
        form.add(row2, c);

        btnAdd.addActionListener(e -> doAdd());
        btnDelete.addActionListener(e -> doDelete());
        btnUpdate.addActionListener(e -> doUpdate());
        btnClear.addActionListener(e -> clearForm());

        JScrollPane formScroll = new JScrollPane(form);
        formScroll.setBorder(null);
        formScroll.getViewport().setOpaque(false);
        formScroll.setOpaque(false);
        formScroll.getVerticalScrollBar().setUnitIncrement(16);
        card.add(formScroll, BorderLayout.CENTER);

        return card;
    }

    /* ── RIGHT: Table card ── */
    private JComponent buildTable() {
        JPanel card = UITheme.card();
        card.setMinimumSize(new Dimension(420, 0));

        JPanel header = new JPanel(new BorderLayout(0, 10));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(14, 16, 10, 16));

        JLabel sectionTitle = UITheme.label("DANH SÁCH CÔNG VIỆC", UITheme.FONT_SECTION, UITheme.TEXT_HI);
        header.add(sectionTitle, BorderLayout.NORTH);

        JPanel searchRow = new JPanel(new BorderLayout(8, 0));
        searchRow.setOpaque(false);
        txtSearch = UITheme.field();
        searchRow.add(txtSearch, BorderLayout.CENTER);
        btnSearch = UITheme.pillBtn("🔍 Tìm", UITheme.ACCENT);
        btnSearch.setPreferredSize(new Dimension(110, 36));
        searchRow.add(btnSearch, BorderLayout.EAST);
        header.add(searchRow, BorderLayout.CENTER);
        btnSearch.addActionListener(e -> doSearch());

        card.add(header, BorderLayout.NORTH);

        // table
        model = new DefaultTableModel(new String[]{"ID", "Tiêu đề", "Danh mục", "Ưu tiên", "Trạng thái", "Bắt đầu", "Hạn cuối"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        styleTable(table);

        table.getColumnModel().getColumn(4).setCellRenderer(badgeRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(badgeRenderer());

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) return;
            selectedId = Integer.parseInt(model.getValueAt(row, 0).toString());
            txtTitle.setText(model.getValueAt(row, 1).toString());
            cbCat.setSelectedItem(model.getValueAt(row, 2).toString());
            cbPri.setSelectedItem(model.getValueAt(row, 3).toString());
            cbStatus.setSelectedItem(model.getValueAt(row, 4).toString());
            txtStart.setText(model.getValueAt(row, 5).toString());
            txtEnd.setText(model.getValueAt(row, 6).toString());
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
        scroll.getViewport().setBackground(UITheme.BG_CARD);
        scroll.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(0, 16, 16, 16),
                BorderFactory.createLineBorder(UITheme.BORDER, 1)));

        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    /* ── helpers ── */
    private void doAdd() {
        if (txtTitle.getText().trim().isEmpty()) { msg("Nhập tiêu đề!"); return; }
        if (txtStart.getText().trim().isEmpty()) { msg("Nhập ngày bắt đầu!"); return; }
        if (txtEnd.getText().trim().isEmpty()) { msg("Nhập hạn cuối!"); return; }
        Task t = new Task();
        t.setTitle(txtTitle.getText());
        t.setCategoryId(cbCat.getSelectedIndex() + 1);
        t.setPriority(cbPri.getSelectedItem().toString());
        t.setStatus(cbStatus.getSelectedItem().toString());
        t.setStartDate(txtStart.getText());
        t.setEndDate(txtEnd.getText());
        t.setDescription(txtDesc.getText());
        t.setUserId(1);
        new TaskService().addTask(t);
        Log log = new Log();
        log.setUsername("admin");
        log.setAction("Thêm công việc: " + t.getTitle());
        new LogService().add(log);
        loadTable();
        clearForm();
        msg("Thêm thành công!");
    }

    private void doDelete() {
        if (table.getSelectedRow() < 0) { msg("Chọn công việc!"); return; }
        if (JOptionPane.showConfirmDialog(this, "Xóa công việc này?", "Xác nhận", JOptionPane.YES_NO_OPTION) == 0) {
            new TaskService().deleteTask(selectedId);
            loadTable();
            clearForm();
        }
    }

    private void doUpdate() {
        if (selectedId < 0) { msg("Chọn công việc để sửa!"); return; }
        Task t = new Task();
        t.setId(selectedId);
        t.setTitle(txtTitle.getText());
        t.setCategoryId(cbCat.getSelectedIndex() + 1);
        t.setPriority(cbPri.getSelectedItem().toString());
        t.setStatus(cbStatus.getSelectedItem().toString());
        t.setStartDate(txtStart.getText());
        t.setEndDate(txtEnd.getText());
        t.setDescription(txtDesc.getText());
        t.setUserId(1);
        new TaskService().updateTask(t);
        loadTable();
        clearForm();
        msg("Cập nhật thành công!");
    }

    private void doSearch() {
        String key = txtSearch.getText().toLowerCase();
        for (int i = 0; i < table.getRowCount(); i++) {
            if (table.getValueAt(i, 1).toString().toLowerCase().contains(key)) {
                table.setRowSelectionInterval(i, i);
                table.scrollRectToVisible(table.getCellRect(i, 0, true));
                return;
            }
        }
        msg("Không tìm thấy!");
    }

    private void loadTable() {
        model.setRowCount(0);
        for (Task t : new TaskService().getAll())
            model.addRow(new Object[]{t.getId(), t.getTitle(), t.getCategoryId(), t.getPriority(), t.getStatus(), t.getStartDate(), t.getEndDate()});
    }

    private void clearForm() {
        txtTitle.setText(""); txtStart.setText(""); txtEnd.setText(""); txtDesc.setText("");
        cbCat.setSelectedIndex(0); cbPri.setSelectedIndex(0); cbStatus.setSelectedIndex(0);
        selectedId = -1;
        table.clearSelection();
    }

    private void msg(String m) { JOptionPane.showMessageDialog(this, m); }

    private JLabel formLabel(String t) {
        return UITheme.label(t, UITheme.FONT_LABEL, UITheme.TEXT_MID);
    }

    private static void styleTable(JTable t) {
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
        t.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER));
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

    private static TableCellRenderer badgeRenderer() {
        return new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable tbl, Object v, boolean s, boolean f, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(tbl, v, s, f, r, c);
                String str = v != null ? v.toString() : "";
                if (!s) l.setBackground(r % 2 == 0 ? UITheme.BG_CARD : UITheme.BG_ROW_ALT);
                l.setForeground(UITheme.statusColor(str));
                l.setFont(new Font("Arial", Font.BOLD, 12));
                l.setBorder(new EmptyBorder(0, 10, 0, 0));
                return l;
            }
        };
    }
}
