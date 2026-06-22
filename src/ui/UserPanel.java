package ui;

import model.User;
import service.UserService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;

public class UserPanel extends JPanel {

    private JTextField txtUser, txtPass, txtName;
    private JComboBox<String> cbRole;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private JTable table;
    private DefaultTableModel model;
    private int selectedId = -1;

    public UserPanel() {
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
        header.add(UITheme.label("QUẢN LÝ NGƯỜI DÙNG", UITheme.FONT_SECTION, UITheme.TEXT_HI), BorderLayout.NORTH);
        header.add(UITheme.hSep(), BorderLayout.SOUTH);
        card.add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(new EmptyBorder(8, 16, 16, 16));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
        int row = 0;

        c.gridy = row++; c.insets = new Insets(4, 0, 4, 0);
        form.add(UITheme.label("Username", UITheme.FONT_LABEL, UITheme.TEXT_MID), c);
        txtUser = UITheme.field();
        c.gridy = row++; c.insets = new Insets(0, 0, 10, 0);
        form.add(txtUser, c);

        c.gridy = row++; c.insets = new Insets(4, 0, 4, 0);
        form.add(UITheme.label("Mật khẩu", UITheme.FONT_LABEL, UITheme.TEXT_MID), c);
        txtPass = UITheme.field();
        c.gridy = row++; c.insets = new Insets(0, 0, 10, 0);
        form.add(txtPass, c);

        c.gridy = row++; c.insets = new Insets(4, 0, 4, 0);
        form.add(UITheme.label("Họ tên", UITheme.FONT_LABEL, UITheme.TEXT_MID), c);
        txtName = UITheme.field();
        c.gridy = row++; c.insets = new Insets(0, 0, 10, 0);
        form.add(txtName, c);

        c.gridy = row++; c.insets = new Insets(4, 0, 4, 0);
        form.add(UITheme.label("Vai trò", UITheme.FONT_LABEL, UITheme.TEXT_MID), c);
        cbRole = UITheme.combo("ADMIN", "USER");
        c.gridy = row++; c.insets = new Insets(0, 0, 14, 0);
        form.add(cbRole, c);

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

        c.gridy = row; c.weighty = 1; c.fill = GridBagConstraints.VERTICAL;
        form.add(Box.createVerticalGlue(), c);

        btnAdd.addActionListener(e -> {
            User u = new User();
            u.setUsername(txtUser.getText());
            u.setPassword(txtPass.getText());
            u.setFullname(txtName.getText());
            u.setRole(cbRole.getSelectedItem().toString());
            new UserService().addUser(u);
            loadTable(); clearForm(); ok("Thêm thành công!");
        });
        btnUpdate.addActionListener(e -> {
            if (selectedId < 0) return;
            User u = new User();
            u.setId(selectedId);
            u.setUsername(txtUser.getText());
            u.setPassword(txtPass.getText());
            u.setFullname(txtName.getText());
            u.setRole(cbRole.getSelectedItem().toString());
            new UserService().updateUser(u);
            loadTable(); clearForm();
        });
        btnDelete.addActionListener(e -> {
            if (selectedId < 0) return;
            new UserService().deleteUser(selectedId);
            loadTable(); clearForm();
        });
        btnClear.addActionListener(e -> clearForm());

        card.add(form, BorderLayout.CENTER);
        return card;
    }

    private JComponent buildTable() {
        JPanel card = UITheme.card();
        card.setMinimumSize(new Dimension(420, 0));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(14, 16, 10, 16));
        header.add(UITheme.label("DANH SÁCH NGƯỜI DÙNG", UITheme.FONT_SECTION, UITheme.TEXT_HI), BorderLayout.WEST);
        card.add(header, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Username", "Password", "Họ tên", "Role"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        CategoryPanel.applyTableStyle(table);

        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable tbl, Object v, boolean s, boolean f, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(tbl, v, s, f, r, c);
                if (!s) l.setBackground(r % 2 == 0 ? UITheme.BG_CARD : UITheme.BG_ROW_ALT);
                l.setForeground(UITheme.statusColor(v != null ? v.toString() : ""));
                l.setFont(new Font("Arial", Font.BOLD, 12));
                l.setBorder(new EmptyBorder(0, 10, 0, 0));
                return l;
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) return;
            selectedId = Integer.parseInt(model.getValueAt(row, 0).toString());
            txtUser.setText(model.getValueAt(row, 1).toString());
            txtPass.setText(model.getValueAt(row, 2).toString());
            txtName.setText(model.getValueAt(row, 3).toString());
            cbRole.setSelectedItem(model.getValueAt(row, 4).toString());
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
        for (User u : new UserService().getAll())
            model.addRow(new Object[]{u.getId(), u.getUsername(), u.getPassword(), u.getFullname(), u.getRole()});
    }

    private void clearForm() {
        txtUser.setText(""); txtPass.setText(""); txtName.setText("");
        cbRole.setSelectedIndex(0); selectedId = -1; table.clearSelection();
    }

    private void ok(String m) { JOptionPane.showMessageDialog(this, m); }
}
