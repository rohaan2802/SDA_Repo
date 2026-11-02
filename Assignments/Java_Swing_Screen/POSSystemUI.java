import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class POSSystemUI extends JFrame {

    // Color schemes
    private static Color HEADER_COLOR = new Color(70, 130, 180);
    private static Color PANEL_BG = new Color(245, 245, 245);
    private static Color BUTTON_COLOR = new Color(100, 149, 237);
    private static Color ACCENT_COLOR = new Color(255, 215, 0);
    private static Color TEXT_COLOR = Color.BLACK;
    private static Color FIELD_BG = Color.WHITE;
    private static Color TABLE_BG = Color.WHITE;
    private static Color TABLE_HEADER_BG = new Color(230, 230, 230);

    // Dark mode colors
    private static final Color DARK_BG = new Color(45, 45, 45);
    private static final Color DARK_PANEL = new Color(60, 60, 60);
    private static final Color DARK_FIELD = new Color(70, 70, 70);
    private static final Color DARK_TEXT = new Color(220, 220, 220);
    private static final Color DARK_HEADER = new Color(35, 35, 35);
    private static final Color DARK_BUTTON = new Color(80, 80, 80);

    private boolean isDarkMode = false;

    // Store components for theme switching
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JMenuBar menuBar;
    private JToggleButton darkModeButton;

    // Store references to interactive components
    private JTextField customerIdField;
    private JTextField customerNameField;
    private JTextField medicineNameField;
    private JTextField medicineIdField;
    private JTextField medicineTypeField;
    private JTextField qtyField;
    private JTextField listField;
    private JTextField priceField;
    private JTextField dosageField;
    private JTextField mfgField;
    private JTextField userIdField;
    private JTextField discountPercentField;
    private JTextField discountAmountField;
    private JTextField cashField;
    private JTextField totalField;
    private JTextField netTotalField;
    private JTextField balanceField;
    private DefaultTableModel tableModel;
    private DefaultTableModel invoiceTableModel;
    private JTable mainTable;
    private JTable invoiceTable;
    private int billNumber = 2210;

    public POSSystemUI() {
        setTitle("MedPharm POS System");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Menu Bar
        createMenuBar();

        // Main Content
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(PANEL_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left Panel - Medicine Entry and Table
        leftPanel = createLeftPanel();

        // Right Panel - Invoice Preview
        rightPanel = createRightPanel();

        // Split main panel
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(900);
        splitPane.setResizeWeight(0.65);

        mainPanel.add(splitPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);

        // Add keyboard shortcuts
        addKeyboardShortcuts();
    }

    private void addKeyboardShortcuts() {
        // F4 for print
        KeyStroke f4 = KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(f4, "printInvoice");
        getRootPane().getActionMap().put("printInvoice", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printInvoice();
            }
        });

        // Delete key for removing selected item
        KeyStroke delete = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(delete, "deleteItem");
        getRootPane().getActionMap().put("deleteItem", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedItem();
            }
        });
    }

    private void createMenuBar() {
        menuBar = new JMenuBar();
        menuBar.setBackground(HEADER_COLOR);

        String[] menuItems = {"Medicine", "Administration", "Special Medicines",
                "Stocking", "Daily Sale", "Lock", "About", "Exit"};

        for (String item : menuItems) {
            JMenu menu = new JMenu(item);
            menu.setForeground(Color.WHITE);
            menu.setFont(new Font("Segoe UI", Font.PLAIN, 12));

            // Add action listeners for menus
            if (item.equals("Exit")) {
                JMenuItem exitItem = new JMenuItem("Exit Application");
                exitItem.addActionListener(e -> exitApplication());
                menu.add(exitItem);
            } else if (item.equals("About")) {
                JMenuItem aboutItem = new JMenuItem("About");
                aboutItem.addActionListener(e -> showAbout());
                menu.add(aboutItem);
            } else {
                JMenuItem placeholder = new JMenuItem(item + " Module");
                placeholder.addActionListener(e -> JOptionPane.showMessageDialog(this,
                        item + "not implemented", "Info", JOptionPane.INFORMATION_MESSAGE));
                menu.add(placeholder);
            }

            menuBar.add(menu);
        }

        // Add spacing
        menuBar.add(Box.createHorizontalGlue());

        // Dark Mode Toggle Button
        darkModeButton = new JToggleButton("🌙 Dark Mode");
        darkModeButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        darkModeButton.setFocusPainted(false);
        darkModeButton.setBackground(new Color(50, 50, 50));
        darkModeButton.setForeground(Color.WHITE);
        darkModeButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        darkModeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        darkModeButton.addActionListener(e -> toggleDarkMode());

        menuBar.add(darkModeButton);

        setJMenuBar(menuBar);
    }

    private void toggleDarkMode() {
        isDarkMode = !isDarkMode;

        if (isDarkMode) {
            // Switch to Dark Mode
            darkModeButton.setText("☀️ Light Mode");
            darkModeButton.setBackground(new Color(240, 240, 240));
            darkModeButton.setForeground(Color.BLACK);

            HEADER_COLOR = DARK_HEADER;
            PANEL_BG = DARK_BG;
            BUTTON_COLOR = DARK_BUTTON;
            TEXT_COLOR = DARK_TEXT;
            FIELD_BG = DARK_FIELD;
            TABLE_BG = DARK_PANEL;
            TABLE_HEADER_BG = DARK_HEADER;

            menuBar.setBackground(DARK_HEADER);
            updateMenuBarColors();

        } else {
            // Switch to Light Mode
            darkModeButton.setText("🌙 Dark Mode");
            darkModeButton.setBackground(new Color(50, 50, 50));
            darkModeButton.setForeground(Color.WHITE);

            HEADER_COLOR = new Color(70, 130, 180);
            PANEL_BG = new Color(245, 245, 245);
            BUTTON_COLOR = new Color(100, 149, 237);
            TEXT_COLOR = Color.BLACK;
            FIELD_BG = Color.WHITE;
            TABLE_BG = Color.WHITE;
            TABLE_HEADER_BG = new Color(230, 230, 230);

            menuBar.setBackground(HEADER_COLOR);
            updateMenuBarColors();
        }

        // Refresh the UI
        refreshUI();
    }

    private void updateMenuBarColors() {
        for (Component comp : menuBar.getComponents()) {
            if (comp instanceof JMenu) {
                JMenu menu = (JMenu) comp;
                menu.setForeground(isDarkMode ? DARK_TEXT : Color.WHITE);
            }
        }
    }

    private void refreshUI() {
        // Remove old panels
        mainPanel.removeAll();

        // Recreate panels with new colors
        leftPanel = createLeftPanel();
        rightPanel = createRightPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(900);
        splitPane.setResizeWeight(0.65);

        mainPanel.setBackground(PANEL_BG);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Repaint everything
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(PANEL_BG);

        // Top Section - Customer and Medicine Entry
        JPanel topSection = new JPanel(new BorderLayout(10, 10));
        topSection.setBackground(FIELD_BG);
        topSection.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(isDarkMode ? DARK_HEADER : new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Customer Info Panel
        JPanel customerPanel = createCustomerPanel();

        // Medicine Entry Panel
        JPanel medicineEntryPanel = createMedicineEntryPanel();

        topSection.add(customerPanel, BorderLayout.NORTH);
        topSection.add(medicineEntryPanel, BorderLayout.CENTER);

        // Middle Section - Medicine Table
        JPanel tablePanel = createTablePanel();

        // Bottom Section - Discount and Payment
        JPanel bottomSection = createBottomPanel();

        leftPanel.add(topSection, BorderLayout.NORTH);
        leftPanel.add(tablePanel, BorderLayout.CENTER);
        leftPanel.add(bottomSection, BorderLayout.SOUTH);

        return leftPanel;
    }

    private JPanel createCustomerPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBackground(FIELD_BG);

        JLabel idLabel = new JLabel("ID:");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        idLabel.setForeground(TEXT_COLOR);

        customerIdField = new JTextField(String.valueOf(billNumber), 8);
        customerIdField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        styleTextField(customerIdField);

        JLabel nameLabel = new JLabel("Customer Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nameLabel.setForeground(TEXT_COLOR);

        customerNameField = new JTextField(15);
        customerNameField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        styleTextField(customerNameField);

        JButton registerBtn = new JButton("Register First");
        registerBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        styleButton(registerBtn, new Color(220, 220, 220));
        registerBtn.addActionListener(e -> registerCustomer());

        panel.add(idLabel);
        panel.add(customerIdField);
        panel.add(nameLabel);
        panel.add(customerNameField);
        panel.add(registerBtn);

        return panel;
    }

    private void registerCustomer() {
        String name = customerNameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter customer name!",
                    "Input Required", JOptionPane.WARNING_MESSAGE);
            customerNameField.requestFocus();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Customer '" + name + "' registered successfully with ID: " + customerIdField.getText(),
                    "Registration Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private JPanel createMedicineEntryPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(FIELD_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 1 - Medicine Name
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Medicine Name (Brand):");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nameLabel.setForeground(TEXT_COLOR);
        panel.add(nameLabel, gbc);

        gbc.gridx = 1; gbc.gridwidth = 2;
        medicineNameField = new JTextField(20);
        medicineNameField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        styleTextField(medicineNameField);
        panel.add(medicineNameField, gbc);

        gbc.gridx = 3; gbc.gridwidth = 1;
        JButton searchBtn = new JButton("Search Medicine");
        styleButton(searchBtn, BUTTON_COLOR);
        searchBtn.setForeground(Color.WHITE);
        searchBtn.addActionListener(e -> searchMedicine());
        panel.add(searchBtn, gbc);

        // Row 2 - Entry Fields
        gbc.gridy = 1;
        String[] labels = {"Medicine ID", "Medicine Type", "Quantity", "List", "Price", "Dosage Detail", "Mfg. ML"};
        int x = 0;

        for (String label : labels) {
            gbc.gridx = x++;
            JLabel lbl = new JLabel(label);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lbl.setForeground(TEXT_COLOR);
            panel.add(lbl, gbc);
        }

        // Row 3 - Input Fields
        gbc.gridy = 2;
        gbc.gridx = 0;
        medicineIdField = new JTextField(8);
        styleTextField(medicineIdField);
        panel.add(medicineIdField, gbc);

        gbc.gridx = 1;
        medicineTypeField = new JTextField(10);
        styleTextField(medicineTypeField);
        panel.add(medicineTypeField, gbc);

        gbc.gridx = 2;
        qtyField = new JTextField(5);
        styleTextField(qtyField);
        panel.add(qtyField, gbc);

        gbc.gridx = 3;
        listField = new JTextField(5);
        styleTextField(listField);
        panel.add(listField, gbc);

        gbc.gridx = 4;
        priceField = new JTextField(8);
        styleTextField(priceField);
        panel.add(priceField, gbc);

        gbc.gridx = 5;
        dosageField = new JTextField(10);
        styleTextField(dosageField);
        panel.add(dosageField, gbc);

        gbc.gridx = 6;
        mfgField = new JTextField(8);
        styleTextField(mfgField);
        panel.add(mfgField, gbc);

        gbc.gridx = 7;
        JButton addBtn = new JButton("Add Item");
        addBtn.setBackground(new Color(34, 139, 34));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        addBtn.setFocusPainted(false);
        addBtn.addActionListener(e -> addItemToTable());
        panel.add(addBtn, gbc);

        return panel;
    }

    private void searchMedicine() {
        String medicineName = medicineNameField.getText().trim();
        if (medicineName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter medicine name to search!",
                    "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Simulate search - populate fields with dummy data
        medicineIdField.setText("380" + (int)(Math.random() * 10000));
        medicineTypeField.setText("Tablet");
        priceField.setText(String.format("%.2f", Math.random() * 100 + 10));
        dosageField.setText("500mg");
        mfgField.setText("ABC Pharma");
        qtyField.setText("1");
        listField.setText("In Stock");

        JOptionPane.showMessageDialog(this, "Medicine found: " + medicineName,
                "Search Result", JOptionPane.INFORMATION_MESSAGE);
        qtyField.requestFocus();
    }

    private void addItemToTable() {
        // Validate inputs
        if (medicineNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter medicine name!",
                    "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int qty = Integer.parseInt(qtyField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());
            double amount = qty * price;

            // Add to main table
            tableModel.addRow(new Object[]{
                    medicineIdField.getText(),
                    medicineNameField.getText(),
                    medicineTypeField.getText(),
                    qty,
                    String.format("%.2f", price),
                    String.format("%.2f", amount),
                    listField.getText(),
                    "N/A",
                    dosageField.getText()
            });

            // Add to invoice table
            int rowNum = invoiceTableModel.getRowCount() + 1;
            invoiceTableModel.addRow(new Object[]{
                    rowNum,
                    medicineNameField.getText(),
                    qty,
                    String.format("%.2f", price),
                    String.format("%.2f", amount)
            });

            // Update totals
            calculateTotals();

            // Clear input fields
            clearMedicineFields();

            JOptionPane.showMessageDialog(this, "Item added successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid quantity and price!",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearMedicineFields() {
        medicineNameField.setText("");
        medicineIdField.setText("");
        medicineTypeField.setText("");
        qtyField.setText("");
        listField.setText("");
        priceField.setText("");
        dosageField.setText("");
        mfgField.setText("");
        medicineNameField.requestFocus();
    }

    private void calculateTotals() {
        double total = 0.0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String amountStr = tableModel.getValueAt(i, 5).toString();
            total += Double.parseDouble(amountStr);
        }

        totalField.setText(String.format("%.2f", total));

        // Calculate discount
        try {
            double discountPercent = Double.parseDouble(discountPercentField.getText().trim());
            double discountAmount = total * (discountPercent / 100);
            double netTotal = total - discountAmount;

            discountAmountField.setText(String.format("%.2f", discountAmount));
            netTotalField.setText(String.format("%.2f", netTotal));

            // Calculate balance
            if (!cashField.getText().trim().isEmpty()) {
                double cash = Double.parseDouble(cashField.getText().trim());
                double balance = cash - netTotal;
                balanceField.setText(String.format("%.2f", balance));
            }
        } catch (NumberFormatException ex) {
            discountAmountField.setText("0.00");
            netTotalField.setText(String.format("%.2f", total));
        }
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(TABLE_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(isDarkMode ? DARK_HEADER : new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Column headers
        String[] columns = {"Stock No", "Brand_name", "Medicine_Type", "Quantity",
                "Price_Per Item", "Amount", "Stock", "Exp_Date", "Type"};

        // Sample data
        Object[][] data = {
                {"3806850", "CEDROX 1G 16S", "", "4", "26.34", "105.36", "3952276", "PANADOL SYRUP 100ML 12  16-1", ""},
                {"3806840", "ASIX 10MG 30S", "", "8", "8.92", "71.36", "3952275", "PANADOL DROP 15ML 48  3-3", ""},
                {"3806110", "TEROXIA TAB 300MG 30S", "", "10", "9.27", "92.70", "3952431", "PANADOL CF 50MG 10CL  2-5", ""},
                {"3806940", "ADVIL 200MG 200MG TAB", "", "1", "56.10", "56.10", "3952521", "PANADOL EXTRA 24CL  2-4", ""},
                {"3806874", "PANADOL SYRUP 100ML 1S", "", "1", "54.64", "54.64", "", "", ""}
        };

        tableModel = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        mainTable = new JTable(tableModel);
        mainTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        mainTable.setRowHeight(25);
        mainTable.setGridColor(isDarkMode ? DARK_HEADER : new Color(220, 220, 220));
        mainTable.setSelectionBackground(isDarkMode ? new Color(80, 80, 80) : new Color(184, 207, 229));
        mainTable.setBackground(TABLE_BG);
        mainTable.setForeground(TEXT_COLOR);

        JTableHeader header = mainTable.getTableHeader();
        header.setBackground(TABLE_HEADER_BG);
        header.setForeground(TEXT_COLOR);
        header.setFont(new Font("Segoe UI", Font.BOLD, 11));

        JScrollPane scrollPane = new JScrollPane(mainTable);
        scrollPane.getViewport().setBackground(TABLE_BG);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(PANEL_BG);

        // Left side - User and Barcode
        JPanel leftBottom = new JPanel(new GridLayout(3, 1, 5, 5));
        leftBottom.setBackground(FIELD_BG);
        leftBottom.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(isDarkMode ? DARK_HEADER : new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userPanel.setBackground(FIELD_BG);
        JLabel userLabel = new JLabel("User ID:");
        userLabel.setForeground(TEXT_COLOR);
        userPanel.add(userLabel);
        userIdField = new JTextField("M12", 10);
        styleTextField(userIdField);
        userPanel.add(userIdField);

        JPanel barcodePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barcodePanel.setBackground(FIELD_BG);
        JLabel barcodeLabel = new JLabel("Barcode: 2211215 Exp-Dat-21-Apr");
        barcodeLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        barcodeLabel.setForeground(Color.RED);
        barcodePanel.add(barcodeLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttonPanel.setBackground(FIELD_BG);

        JButton editBtn = new JButton("Edit Data");
        JButton delBtn = new JButton("Del/Delete");
        JButton removeAllBtn = new JButton("Remove All");
        JButton cancelBtn = new JButton("Cancel");

        editBtn.addActionListener(e -> editSelectedItem());
        delBtn.addActionListener(e -> deleteSelectedItem());
        removeAllBtn.addActionListener(e -> removeAllItems());
        cancelBtn.addActionListener(e -> cancelTransaction());

        for (JButton btn : new JButton[]{editBtn, delBtn, removeAllBtn, cancelBtn}) {
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            styleButton(btn, isDarkMode ? DARK_BUTTON : new Color(220, 220, 220));
            btn.setForeground(TEXT_COLOR);
            buttonPanel.add(btn);
        }

        leftBottom.add(userPanel);
        leftBottom.add(barcodePanel);
        leftBottom.add(buttonPanel);

        // Right side - Payment
        JPanel rightBottom = new JPanel(new GridBagLayout());
        rightBottom.setBackground(FIELD_BG);
        rightBottom.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(isDarkMode ? DARK_HEADER : new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Total Section
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel totalLabel = new JLabel("Total");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalLabel.setForeground(TEXT_COLOR);
        rightBottom.add(totalLabel, gbc);

        gbc.gridx = 1;
        totalField = new JTextField("380.16", 10);
        totalField.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalField.setHorizontalAlignment(JTextField.RIGHT);
        totalField.setEditable(false);
        styleTextField(totalField);
        rightBottom.add(totalField, gbc);

        // Discount Section
        gbc.gridx = 0; gbc.gridy = 1;
        JPanel discountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        discountPanel.setBackground(FIELD_BG);
        JLabel discLabel = new JLabel("Discount %age");
        discLabel.setForeground(TEXT_COLOR);
        discountPanel.add(discLabel);
        discountPercentField = new JTextField("5", 5);
        styleTextField(discountPercentField);
        discountPercentField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                calculateTotals();
            }
        });
        discountPanel.add(discountPercentField);
        rightBottom.add(discountPanel, gbc);

        gbc.gridx = 1;
        JLabel netTotalLabel = new JLabel("Net Total");
        netTotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        netTotalLabel.setForeground(TEXT_COLOR);
        rightBottom.add(netTotalLabel, gbc);

        gbc.gridx = 2;
        netTotalField = new JTextField("361.15", 10);
        netTotalField.setFont(new Font("Segoe UI", Font.BOLD, 16));
        netTotalField.setHorizontalAlignment(JTextField.RIGHT);
        netTotalField.setEditable(false);
        styleTextField(netTotalField);
        rightBottom.add(netTotalField, gbc);

        // Discount Amount
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel discAmtLabel = new JLabel("Discount AMT");
        discAmtLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        discAmtLabel.setForeground(TEXT_COLOR);
        rightBottom.add(discAmtLabel, gbc);

        gbc.gridx = 1;
        discountAmountField = new JTextField("19.01", 10);
        discountAmountField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        discountAmountField.setHorizontalAlignment(JTextField.RIGHT);
        discountAmountField.setEditable(false);
        styleTextField(discountAmountField);
        rightBottom.add(discountAmountField, gbc);

        // Cash
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel cashLabel = new JLabel("Cash");
        cashLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cashLabel.setForeground(TEXT_COLOR);
        rightBottom.add(cashLabel, gbc);

        gbc.gridx = 1;
        cashField = new JTextField("1000", 10);
        cashField.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cashField.setBackground(ACCENT_COLOR);
        cashField.setForeground(Color.BLACK);
        cashField.setHorizontalAlignment(JTextField.RIGHT);
        cashField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                calculateTotals();
            }
        });
        rightBottom.add(cashField, gbc);

        // Balance
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel balanceLabel = new JLabel("Balance");
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        balanceLabel.setForeground(TEXT_COLOR);
        rightBottom.add(balanceLabel, gbc);

        gbc.gridx = 1;
        balanceField = new JTextField("638.85", 10);
        balanceField.setFont(new Font("Segoe UI", Font.BOLD, 16));
        balanceField.setHorizontalAlignment(JTextField.RIGHT);
        balanceField.setEditable(false);
        styleTextField(balanceField);
        rightBottom.add(balanceField, gbc);

        // Print Button
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 3;
        JButton printBtn = new JButton("Press F4 to Print and Generate Invoice Copy");
        printBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        printBtn.setBackground(new Color(220, 20, 60));
        printBtn.setForeground(Color.WHITE);
        printBtn.setPreferredSize(new Dimension(300, 40));
        printBtn.setFocusPainted(false);
        printBtn.addActionListener(e -> printInvoice());
        rightBottom.add(printBtn, gbc);

        // Payment options
        gbc.gridy = 6; gbc.gridwidth = 1;
        JPanel paymentOptions = new JPanel(new FlowLayout(FlowLayout.CENTER));
        paymentOptions.setBackground(FIELD_BG);

        ButtonGroup paymentGroup = new ButtonGroup();
        JRadioButton printRadio = new JRadioButton("Print", true);
        JRadioButton displayRadio = new JRadioButton("Display");
        JRadioButton noneRadio = new JRadioButton("None");

        for (JRadioButton rb : new JRadioButton[]{printRadio, displayRadio, noneRadio}) {
            rb.setBackground(FIELD_BG);
            rb.setForeground(TEXT_COLOR);
            paymentGroup.add(rb);
            paymentOptions.add(rb);
        }

        gbc.gridx = 0; gbc.gridwidth = 3;
        rightBottom.add(paymentOptions, gbc);

        // Remove Button
        gbc.gridy = 7;
        JButton removeBtn = new JButton("Remove");
        removeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        styleButton(removeBtn, isDarkMode ? DARK_BUTTON : new Color(220, 220, 220));
        removeBtn.setForeground(TEXT_COLOR);
        removeBtn.addActionListener(e -> deleteSelectedItem());
        rightBottom.add(removeBtn, gbc);

        panel.add(leftBottom, BorderLayout.WEST);
        panel.add(rightBottom, BorderLayout.CENTER);

        return panel;
    }

    private void editSelectedItem() {
        int selectedRow = mainTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to edit!",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Populate fields with selected row data
        medicineIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
        medicineNameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
        medicineTypeField.setText(tableModel.getValueAt(selectedRow, 2).toString());
        qtyField.setText(tableModel.getValueAt(selectedRow, 3).toString());
        priceField.setText(tableModel.getValueAt(selectedRow, 4).toString());
        listField.setText(tableModel.getValueAt(selectedRow, 6).toString());
        dosageField.setText(tableModel.getValueAt(selectedRow, 8).toString());

        // Remove the row so it can be re-added with updated values
        tableModel.removeRow(selectedRow);
        if (selectedRow < invoiceTableModel.getRowCount()) {
            invoiceTableModel.removeRow(selectedRow);
            // Renumber invoice items
            for (int i = 0; i < invoiceTableModel.getRowCount(); i++) {
                invoiceTableModel.setValueAt(i + 1, i, 0);
            }
        }

        calculateTotals();
        JOptionPane.showMessageDialog(this, "Item loaded for editing. Modify and click 'Add Item'.",
                "Edit Mode", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteSelectedItem() {
        int selectedRow = mainTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to delete!",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this item?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
            if (selectedRow < invoiceTableModel.getRowCount()) {
                invoiceTableModel.removeRow(selectedRow);
                // Renumber invoice items
                for (int i = 0; i < invoiceTableModel.getRowCount(); i++) {
                    invoiceTableModel.setValueAt(i + 1, i, 0);
                }
            }
            calculateTotals();
            JOptionPane.showMessageDialog(this, "Item deleted successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void removeAllItems() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No items to remove!",
                    "Empty Cart", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove all items?",
                "Confirm Remove All", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.setRowCount(0);
            invoiceTableModel.setRowCount(0);
            totalField.setText("0.00");
            netTotalField.setText("0.00");
            discountAmountField.setText("0.00");
            balanceField.setText("0.00");
            JOptionPane.showMessageDialog(this, "All items removed!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cancelTransaction() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel this transaction?",
                "Confirm Cancel", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Clear all fields
            customerNameField.setText("");
            clearMedicineFields();
            removeAllItems();
            cashField.setText("");
            discountPercentField.setText("0");
            JOptionPane.showMessageDialog(this, "Transaction cancelled!",
                    "Cancelled", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void printInvoice() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No items to print! Please add items first.",
                    "Empty Cart", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (customerNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter customer name!",
                    "Customer Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Simulate printing
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());

        StringBuilder invoice = new StringBuilder();
        invoice.append("========================================\n");
        invoice.append("       LLOYD'S PHARMACY\n");
        invoice.append("     Islamabad 5, 44-W-Block\n");
        invoice.append("         UMT LAHORE\n");
        invoice.append("========================================\n");
        invoice.append("Bill #: ").append(customerIdField.getText()).append("\n");
        invoice.append("Customer: ").append(customerNameField.getText()).append("\n");
        invoice.append("Date/Time: ").append(currentDateTime).append("\n");
        invoice.append("User: ").append(userIdField.getText()).append("\n");
        invoice.append("========================================\n");
        invoice.append("Items:\n");
        invoice.append("========================================\n");

        for (int i = 0; i < invoiceTableModel.getRowCount(); i++) {
            invoice.append(String.format("%d. %s\n",
                    invoiceTableModel.getValueAt(i, 0),
                    invoiceTableModel.getValueAt(i, 1)));
            invoice.append(String.format("   Qty: %s x %s = %s\n",
                    invoiceTableModel.getValueAt(i, 2),
                    invoiceTableModel.getValueAt(i, 3),
                    invoiceTableModel.getValueAt(i, 4)));
        }

        invoice.append("========================================\n");
        invoice.append(String.format("Total:        Rs. %s\n", totalField.getText()));
        invoice.append(String.format("Discount:     Rs. %s\n", discountAmountField.getText()));
        invoice.append(String.format("Net Total:    Rs. %s\n", netTotalField.getText()));
        invoice.append(String.format("Cash:         Rs. %s\n", cashField.getText()));
        invoice.append(String.format("Balance:      Rs. %s\n", balanceField.getText()));
        invoice.append("========================================\n");
        invoice.append("    Thank you for your business!\n");
        invoice.append("Medicines once sold are not taken back\n");
        invoice.append("     Software by MedPharmaware\n");
        invoice.append("========================================\n");

        JTextArea textArea = new JTextArea(invoice.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 500));

        JOptionPane.showMessageDialog(this, scrollPane,
                "Invoice Preview", JOptionPane.INFORMATION_MESSAGE);

        // Increment bill number and reset for next transaction
        billNumber++;
        customerIdField.setText(String.valueOf(billNumber));

        int nextTransaction = JOptionPane.showConfirmDialog(this,
                "Invoice printed successfully!\nStart new transaction?",
                "Success", JOptionPane.YES_NO_OPTION);

        if (nextTransaction == JOptionPane.YES_OPTION) {
            customerNameField.setText("");
            clearMedicineFields();
            removeAllItems();
            cashField.setText("");
        }
    }

    private void showAbout() {
        String aboutText = "MedPharm POS System\n\n" +
                "Version: 1.0\n" +
                "Developed by: MedPharmaware\n\n" +
                "A comprehensive Point of Sale system\n" +
                "for pharmacy management.\n\n" +
                "Features:\n" +
                "• Medicine inventory management\n" +
                "• Invoice generation\n" +
                "• Customer registration\n" +
                "• Sales tracking\n" +
                "• Dark mode support\n\n" +
                "© 2025 MedPharmaware. All rights reserved.";

        JOptionPane.showMessageDialog(this, aboutText,
                "About MedPharm POS", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit?",
                "Exit Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(FIELD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(isDarkMode ? DARK_HEADER : new Color(180, 180, 180), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(FIELD_BG);

        JLabel logoLabel = new JLabel("LLOYD'S PHARMACY");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setForeground(TEXT_COLOR);

        JLabel addressLabel = new JLabel("<html><center>Islamabad 5, 44-W-Block<br>UMT LAHORE<br>[F]2634728 [C]212 0333 6867</center></html>");
        addressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        addressLabel.setHorizontalAlignment(SwingConstants.CENTER);
        addressLabel.setForeground(TEXT_COLOR);

        headerPanel.add(logoLabel, BorderLayout.NORTH);
        headerPanel.add(addressLabel, BorderLayout.CENTER);

        // Invoice Details
        JPanel detailsPanel = new JPanel(new GridLayout(4, 2, 5, 3));
        detailsPanel.setBackground(FIELD_BG);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        detailsPanel.add(createDetailLabel("Bill#: 2210"));
        detailsPanel.add(createDetailLabel(""));
        detailsPanel.add(createDetailLabel("Name: [F]2634728 [C]212 0333 6867 154"));
        detailsPanel.add(createDetailLabel(""));
        detailsPanel.add(createDetailLabel("Date: Next Liquidation"));
        detailsPanel.add(createDetailLabel("Qty: 0"));
        detailsPanel.add(createDetailLabel("Time: 03:28:09"));
        detailsPanel.add(createDetailLabel("Price: Amount"));

        // Items Table
        String[] columns = {"S#", "Item/Description", "Qty", "Price", "Amount"};
        Object[][] invoiceData = {
                {"1", "CEDROX 1G 16S", "4", "26.34", "105.36"},
                {"2", "ASIX 10MG 30S", "8", "8.92", "71.36"},
                {"3", "GLUCOPHAGE 1000MG", "10", "9.27", "92.70"},
                {"4", "TEROXIA TAB 30MG", "10", "0.27", "2.70"},
                {"5", "PADS MAXI 30S", "1", "3.92", "31.92"},
                {"6", "ABD LMVG 3R5", "1", "0.00", "0.00"}
        };

        invoiceTableModel = new DefaultTableModel(invoiceData, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        invoiceTable = new JTable(invoiceTableModel);
        invoiceTable.setFont(new Font("Monospaced", Font.PLAIN, 10));
        invoiceTable.setRowHeight(20);
        invoiceTable.setShowGrid(true);
        invoiceTable.setGridColor(isDarkMode ? DARK_HEADER : new Color(220, 220, 220));
        invoiceTable.setBackground(TABLE_BG);
        invoiceTable.setForeground(TEXT_COLOR);

        JTableHeader invHeader = invoiceTable.getTableHeader();
        invHeader.setBackground(TABLE_HEADER_BG);
        invHeader.setForeground(TEXT_COLOR);

        JScrollPane scrollPane = new JScrollPane(invoiceTable);
        scrollPane.setPreferredSize(new Dimension(300, 250));
        scrollPane.getViewport().setBackground(TABLE_BG);

        // Totals Panel
        JPanel totalsPanel = new JPanel(new GridLayout(5, 2, 5, 3));
        totalsPanel.setBackground(FIELD_BG);
        totalsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        totalsPanel.add(createDetailLabel("Total:"));
        totalsPanel.add(createDetailLabel("380.16", SwingConstants.RIGHT));
        totalsPanel.add(createDetailLabel("Subtotal:"));
        totalsPanel.add(createDetailLabel("NTS", SwingConstants.RIGHT));
        totalsPanel.add(createDetailLabel("Paid:"));
        totalsPanel.add(createDetailLabel("1000", SwingConstants.RIGHT));
        totalsPanel.add(createDetailLabel("Discount:"));
        totalsPanel.add(createDetailLabel("19.01", SwingConstants.RIGHT));
        totalsPanel.add(createDetailLabel("Net Total:"));
        totalsPanel.add(createDetailLabel("361.15", SwingConstants.RIGHT));

        // Footer
        JLabel footerLabel = new JLabel("<html><center>All Pharmacy items & coffee shop are non refundable.<br>" +
                "Medicines once sold are not taken back & neither<br>" +
                "exchangeable as per drug act.<br>" +
                "Software by MedPharmaware</center></html>");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerLabel.setForeground(TEXT_COLOR);

        // Media controls
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        controlsPanel.setBackground(FIELD_BG);

        JButton prevBtn = new JButton("◄◄");
        JButton playBtn = new JButton("▶");
        JButton stopBtn = new JButton("■");
        JButton nextBtn = new JButton("►►");

        prevBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Previous invoice feature not implemented!", "Info", JOptionPane.INFORMATION_MESSAGE));
        playBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Play/Preview feature not implemented!", "Info", JOptionPane.INFORMATION_MESSAGE));
        stopBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Stop feature not implemented!", "Info", JOptionPane.INFORMATION_MESSAGE));
        nextBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Next invoice feature not implemented!", "Info", JOptionPane.INFORMATION_MESSAGE));

        for (JButton btn : new JButton[]{prevBtn, playBtn, stopBtn, nextBtn}) {
            btn.setPreferredSize(new Dimension(50, 25));
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            styleButton(btn, isDarkMode ? DARK_BUTTON : new Color(220, 220, 220));
            btn.setForeground(TEXT_COLOR);
            controlsPanel.add(btn);
        }

        panel.add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(FIELD_BG);
        centerPanel.add(detailsPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(totalsPanel, BorderLayout.SOUTH);

        panel.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(FIELD_BG);
        bottomPanel.add(footerLabel, BorderLayout.CENTER);
        bottomPanel.add(controlsPanel, BorderLayout.SOUTH);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JLabel createDetailLabel(String text) {
        return createDetailLabel(text, SwingConstants.LEFT);
    }

    private JLabel createDetailLabel(String text, int alignment) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        label.setHorizontalAlignment(alignment);
        label.setForeground(TEXT_COLOR);
        return label;
    }

    // Helper method to style text fields
    private void styleTextField(JTextField field) {
        field.setBackground(FIELD_BG);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(isDarkMode ? DARK_HEADER : new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
    }

    // Helper method to style buttons
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            POSSystemUI pos = new POSSystemUI();
            pos.setVisible(true);
        });
    }
}