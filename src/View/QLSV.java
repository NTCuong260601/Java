package View;

import Controller.BaseDAOImplement;
import Model.Students;
import Model.User;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class QLSV extends javax.swing.JFrame {

    protected ArrayList<Students> lstStudents;
    protected DefaultTableModel model;
    protected int index;
    protected int page;
    protected String imgFilePath;
    protected Login login;

    public QLSV() {
        initComponents();
        setLocationRelativeTo(null);
        setVisible(true);
        lstStudents = new ArrayList<>();
        model = (DefaultTableModel) tblQLSV.getModel();
        lstStudents = getAllStudent();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    btnUpdate.setEnabled(checkNull());
                    btnSave.setEnabled(checkNull());
                    lstStudents = getAllStudent();
                    btnDelete.setEnabled(!txtMaSV.getText().trim().equals(""));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(QLSV.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        t.start();
        fillToTable(lstStudents);
    }

    public ArrayList<Students> getAllStudent() {
        ArrayList<Students> list = new ArrayList<>();
        String tableName = BaseDAOImplement.getIstance().getTableName("Student");
        String sql = "SELECT * FROM " + tableName + "";
        try {
            Statement statement = BaseDAOImplement.getIstance().getConnection().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                list.add(new Students(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5) == 1 ? true : false,
                        rs.getString(6),
                        rs.getString(7)
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public void fillToTable(ArrayList<Students> list) {
        model.setRowCount(0);
        for (Students st : list) {
            model.addRow(new Object[]{st.getIdST(), st.getNameST(), st.getEmailST(), st.getPhoneNumberST(), st.isSexST() == true ? "Nam" : "Nu", st.getAddressST(), st.getImageST()});
        }
    }

    public void showDetail() {
        txtMaSV.setText(model.getValueAt(index, 0) + "");
        txtHoTen.setText(model.getValueAt(index, 1) + "");
        txtEmail.setText(model.getValueAt(index, 2) + "");
        txtSDT.setText(model.getValueAt(index, 3) + "");
        if (model.getValueAt(index, 4).equals("Nam")) {
            rdoNam.setSelected(true);
        } else {
            rdoNu.setSelected(true);
        }
        txtDiaChi.setText(model.getValueAt(index, 5) + "");
        imgFilePath = (String) model.getValueAt(index, 6);
        if (imgFilePath.trim().equals("")) {
            lblImg.setIcon(null);
        } else {
            lblImg.setIcon(icon(imgFilePath));
        }
        txtMaSV.setEditable(false);
        txtMaSV.setEnabled(false);
    }

    public boolean checkNull() {
        if (txtMaSV.getText().trim().equals("")
                || txtHoTen.getText().trim().equals("")
                || txtEmail.getText().trim().equals("")
                || txtSDT.getText().trim().equals("")
                || txtDiaChi.getText().trim().equals("")) {
            return false;
        }
        return true;
    }

    public boolean checkEmailAndPhoneNumber() {
        String emailRegex = "\\w+@\\w+(\\.\\w+){1,2}";
        String pNRegex = "0[0-9\\s.-]{9,11}";
        if (txtEmail.getText().trim().matches(emailRegex)) {
            if (txtSDT.getText().trim().matches(pNRegex)) {
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Sai định dạng số điện thoại");
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(this, "Sai định dạng email");
            return false;
        }
    }

    public boolean checkExist() {
        String idSV = txtMaSV.getText();
        for (Students st : lstStudents) {
            if (st.getIdST().equalsIgnoreCase(idSV)) {
                return true;
            }
        }
        return false;
    }

    public int getIndex(String idST) {
        int sumRow = model.getRowCount();
        for (int i = 0; i < sumRow; i++) {
            if (idST.equalsIgnoreCase(model.getValueAt(i, 0) + "")) {
                return i;
            }
        }
        return -1;
    }

    public void clearForm() {
        txtMaSV.setText("");
        txtHoTen.setText("");
        txtEmail.setText("");
        txtSDT.setText("");
        rdoNam.setSelected(true);
        txtDiaChi.setText("");
        imgFilePath = "";
        lblImg.setIcon(null);
        txtMaSV.setEditable(true);
        txtMaSV.setEnabled(true);
    }

    public Students readForm() {
        String idST = txtMaSV.getText();
        String nameST = txtHoTen.getText();
        String emailST = txtEmail.getText();
        String pNST = txtSDT.getText();
        boolean sexST = rdoNam.isSelected() == true ? true : false;
        String addressST = txtDiaChi.getText();
        String imgFP = imgFilePath;
        return new Students(idST, nameST, emailST, pNST, sexST, addressST, imgFP);
    }

    public String imageFilePath() {
        JFileChooser jf = new JFileChooser();
        int file = jf.showOpenDialog(this);
        if (file == JFileChooser.APPROVE_OPTION) {
            return jf.getSelectedFile().getAbsolutePath();
        }
        return "";
    }

    public ImageIcon icon(String file) {
        try {
            BufferedImage bi = ImageIO.read(new File(file));
            return new ImageIcon(bi.getScaledInstance(lblImg.getWidth(), lblImg.getHeight(), bi.SCALE_SMOOTH));
        } catch (IOException ex) {
            Logger.getLogger(QLSV.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void addST() {
        Students st = readForm();
        String tableName = BaseDAOImplement.getIstance().getTableName("Student");
        String[] columnName = BaseDAOImplement.getIstance().getColumnName(tableName);
        Object[] values = {st.getIdST(), st.getNameST(), st.getEmailST(), st.getPhoneNumberST(), st.isSexST() == true ? "1" : "0", st.getAddressST(), st.getImageST()};
        int addST = BaseDAOImplement.getIstance().addDB(tableName, columnName, "", values);
        if (addST != 0) {
            model.addRow(new Object[]{
                st.getIdST(),
                st.getNameST(),
                st.getEmailST(),
                st.getPhoneNumberST(),
                st.isSexST() == true ? "Nam" : "Nu",
                st.getAddressST(),
                st.getImageST()});
            JOptionPane.showMessageDialog(this, "Thêm mới thành công");
        } else {
            JOptionPane.showMessageDialog(this, "Thêm mới thất bại");
        }
    }

    public void updateST() {
        Students st = readForm();
        String tableName = BaseDAOImplement.getIstance().getTableName("Student");
        String[] columnName = new String[BaseDAOImplement.getIstance().getColumnName(tableName).length - 1];
        for (int i = 0; i < columnName.length; i++) {
            columnName[i] = BaseDAOImplement.getIstance().getColumnName(tableName)[i + 1];
        }
        Object[] values = {st.getNameST(), st.getEmailST(), st.getPhoneNumberST(), st.isSexST() == true ? "1" : "0", st.getAddressST(), st.getImageST()};
        String condition = BaseDAOImplement.getIstance().getCondition(BaseDAOImplement.getIstance().getColumnName(tableName)[0], "LIKE", st.getIdST());
        int updateST = BaseDAOImplement.getIstance().updateDB(tableName, columnName, values, condition);
        if (updateST != 0) {
            index = getIndex(st.getIdST());
            model.setValueAt(st.getIdST(), index, 0);
            model.setValueAt(st.getNameST(), index, 1);
            model.setValueAt(st.getEmailST(), index, 2);
            model.setValueAt(st.getPhoneNumberST(), index, 3);
            model.setValueAt(st.isSexST() == true ? "Nam" : "Nu", index, 4);
            model.setValueAt(st.getAddressST(), index, 5);
            model.setValueAt(st.getImageST(), index, 6);
            JOptionPane.showMessageDialog(this, "Cập nhật thành công");
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại");
        }
    }

    public void deleteST() {
        Students st = readForm();
        String tableNameGR = BaseDAOImplement.getIstance().getTableName("GRADE");
        String tableNameST = BaseDAOImplement.getIstance().getTableName("STUDENT");
        String condition = BaseDAOImplement.getIstance().getCondition("IDST", "LIKE", st.getIdST());
        int deleteGR = BaseDAOImplement.getIstance().deleteDB(tableNameGR, condition);
        int deleteST = BaseDAOImplement.getIstance().deleteDB(tableNameST, condition);
        if (deleteST > 0) {
            JOptionPane.showMessageDialog(this, "Xóa thành công");
            index = getIndex(st.getIdST());
            model.removeRow(index);
        } else {
            JOptionPane.showMessageDialog(this, "Xóa thất bại");
        }
    }

    public ArrayList<Students> getTopList(int page, int row) {
        ArrayList<Students> list = new ArrayList<>();
        String tableName = BaseDAOImplement.getIstance().getTableName("Student");
        String sql = "SELECT * FROM " + tableName + " ORDER BY idST OFFSET " + page + " ROWS FETCH NEXT " + row + " ROWS ONLY";
        try {
            Statement statement = BaseDAOImplement.getIstance().getConnection().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                list.add(new Students(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5) == 1 ? true : false,
                        rs.getString(6),
                        rs.getString(7)
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public void first(int row) {
        page = 0;
        fillToTable(getTopList(page, row));
    }

    public void last(int row) {
        if (lstStudents.size() % row == 0) {
            page = lstStudents.size() - row;
        }else{
            int lastRow = Math.round(lstStudents.size() / row);
            page = lastRow*row;
        }
        fillToTable(getTopList(page, row));
    }

    public void prev(int row) {
        if (lstStudents.size() % row == 0) {
            if (page <= 0) {
                page = lstStudents.size() - row;
            } else {
                page -= row;
            }
        } else {
            int lastRow = Math.round(lstStudents.size() / row);
            if (page <= 0) {
                page = lastRow * row;
            } else {
                page -= row;
            }
        }
        fillToTable(getTopList(page, row));
    }

    public void next(int row) {
        if (page >= lstStudents.size() - row) {
            page = 0;
        } else {
            page += row;
        }
        fillToTable(getTopList(page, row));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroupGT = new javax.swing.ButtonGroup();
        jpnTitle = new javax.swing.JLabel();
        jpnQLSV = new javax.swing.JPanel();
        lblMaSV = new javax.swing.JLabel();
        lblHoTen = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblSDT = new javax.swing.JLabel();
        lblGioiTinh = new javax.swing.JLabel();
        lblDiaChi = new javax.swing.JLabel();
        txtMaSV = new javax.swing.JTextField();
        txtHoTen = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtSDT = new javax.swing.JTextField();
        rdoNam = new javax.swing.JRadioButton();
        rdoNu = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDiaChi = new javax.swing.JTextArea();
        jpnImg = new javax.swing.JPanel();
        btnImg = new javax.swing.JButton();
        lblImg = new javax.swing.JLabel();
        btnNew = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnLogOut = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblQLSV = new javax.swing.JTable();
        btnFirst = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("QLSV");

        jpnTitle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jpnTitle.setForeground(java.awt.Color.blue);
        jpnTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jpnTitle.setText("Quản lí sinh viên");

        jpnQLSV.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, java.awt.Color.blue));

        lblMaSV.setFont(new java.awt.Font("Courier New", 1, 17)); // NOI18N
        lblMaSV.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMaSV.setText("Mã sinh viên:");

        lblHoTen.setFont(new java.awt.Font("Courier New", 1, 17)); // NOI18N
        lblHoTen.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblHoTen.setText("Họ và tên:");

        lblEmail.setFont(new java.awt.Font("Courier New", 1, 17)); // NOI18N
        lblEmail.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEmail.setText("Email:");

        lblSDT.setFont(new java.awt.Font("Courier New", 1, 17)); // NOI18N
        lblSDT.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSDT.setText("Số điện thoại:");

        lblGioiTinh.setFont(new java.awt.Font("Courier New", 1, 17)); // NOI18N
        lblGioiTinh.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblGioiTinh.setText("Giới tính:");

        lblDiaChi.setFont(new java.awt.Font("Courier New", 1, 17)); // NOI18N
        lblDiaChi.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDiaChi.setText("Địa chỉ:");

        btnGroupGT.add(rdoNam);
        rdoNam.setFont(new java.awt.Font("Courier New", 1, 17)); // NOI18N
        rdoNam.setSelected(true);
        rdoNam.setText("Nam");

        btnGroupGT.add(rdoNu);
        rdoNu.setFont(new java.awt.Font("Courier New", 1, 17)); // NOI18N
        rdoNu.setText("Nữ");

        txtDiaChi.setColumns(20);
        txtDiaChi.setRows(5);
        jScrollPane1.setViewportView(txtDiaChi);

        jpnImg.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.red));

        btnImg.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnImg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Icon/Image_Icon.png"))); // NOI18N
        btnImg.setText("Image");
        btnImg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImgActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnImgLayout = new javax.swing.GroupLayout(jpnImg);
        jpnImg.setLayout(jpnImgLayout);
        jpnImgLayout.setHorizontalGroup(
            jpnImgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnImgLayout.createSequentialGroup()
                .addGroup(jpnImgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblImg, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                    .addComponent(btnImg, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jpnImgLayout.setVerticalGroup(
            jpnImgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnImgLayout.createSequentialGroup()
                .addComponent(lblImg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnImg, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Icon/New_Icon.png"))); // NOI18N
        btnNew.setText("New");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        btnUpdate.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Icon/Update_Icon.png"))); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnSave.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Icon/Save_Icon.png"))); // NOI18N
        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnDelete.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Icon/Delete_Icon.png"))); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnLogOut.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnLogOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Icon/LogOut_Icon.png"))); // NOI18N
        btnLogOut.setText("LogOut");
        btnLogOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogOutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnQLSVLayout = new javax.swing.GroupLayout(jpnQLSV);
        jpnQLSV.setLayout(jpnQLSVLayout);
        jpnQLSVLayout.setHorizontalGroup(
            jpnQLSVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnQLSVLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnQLSVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jpnQLSVLayout.createSequentialGroup()
                        .addComponent(lblMaSV, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtMaSV, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnQLSVLayout.createSequentialGroup()
                        .addComponent(lblHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnQLSVLayout.createSequentialGroup()
                        .addComponent(lblEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnQLSVLayout.createSequentialGroup()
                        .addComponent(lblSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnQLSVLayout.createSequentialGroup()
                        .addComponent(lblGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rdoNam, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rdoNu, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnQLSVLayout.createSequentialGroup()
                        .addComponent(lblDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1)))
                .addGap(18, 18, 18)
                .addGroup(jpnQLSVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnQLSVLayout.createSequentialGroup()
                        .addComponent(jpnImg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnLogOut, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnQLSVLayout.createSequentialGroup()
                        .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnQLSVLayout.createSequentialGroup()
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpnQLSVLayout.setVerticalGroup(
            jpnQLSVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnQLSVLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnQLSVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpnQLSVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jpnQLSVLayout.createSequentialGroup()
                            .addGroup(jpnQLSVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblMaSV, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtMaSV, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jpnQLSVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jpnQLSVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jpnQLSVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jpnQLSVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(rdoNam, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(rdoNu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jpnImg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnLogOut, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpnQLSVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jpnQLSVLayout.createSequentialGroup()
                        .addGroup(jpnQLSVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jpnQLSVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblQLSV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã SV", "Họ và tên", "Email", "Số điện thoại", "Giới tính", "Địa chỉ", "Hình ảnh"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblQLSV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblQLSVMouseClicked(evt);
            }
        });
        tblQLSV.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblQLSVKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tblQLSV);

        btnFirst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Icon/Firt_Icon.png"))); // NOI18N
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        btnPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Icon/Prev_Icon.png"))); // NOI18N
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Icon/Next_Icon.png"))); // NOI18N
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnLast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Icon/Last_Icon.png"))); // NOI18N
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jpnTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
                        .addComponent(jpnQLSV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(248, 248, 248)
                        .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpnTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jpnQLSV, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblQLSVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblQLSVMouseClicked
        try {
            index = tblQLSV.getSelectedRow();
            showDetail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_tblQLSVMouseClicked

    private void tblQLSVKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblQLSVKeyReleased
        try {
            if (evt.getKeyCode() == evt.VK_UP || evt.getKeyCode() == evt.VK_DOWN) {
                index = tblQLSV.getSelectedRow();
                showDetail();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_tblQLSVKeyReleased

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        try {
            clearForm();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnImgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImgActionPerformed
        try {
            imgFilePath = imageFilePath();
            lblImg.setIcon(icon(imgFilePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnImgActionPerformed

    private void btnLogOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogOutActionPerformed
        try {
            int cancel = JOptionPane.showConfirmDialog(this, "Thoát !!!", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.CANCEL_OPTION);
            if (cancel == 0) {
                this.setVisible(false);
                login = new Login();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnLogOutActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        try {
            if (checkNull()) {
                if (checkEmailAndPhoneNumber()) {
                    if (!checkExist()) {
                        addST();
                        clearForm();
                    } else {
                        int update = JOptionPane.showConfirmDialog(this, "Mã SV đã tồn tại thực hiện update", "Update", JOptionPane.YES_NO_OPTION, JOptionPane.CANCEL_OPTION);
                        if (update == 0) {
                            updateST();
                            clearForm();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        try {
            if (checkNull()) {
                if (checkEmailAndPhoneNumber()) {
                    if (checkExist()) {
                        updateST();
                        clearForm();
                    } else {
                        int add = JOptionPane.showConfirmDialog(this, "Mã SV chưa tồn tại", "Add", JOptionPane.YES_NO_OPTION, JOptionPane.DEFAULT_OPTION);
                        if (add == 0) {
                            addST();
                            clearForm();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        try {
            int delete = JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa SV này", "Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (delete == 0) {
                deleteST();
                clearForm();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        try {
            first(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        try {
            last(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnLastActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        try {
            prev(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        try {
            next(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnNextActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(QLSV.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QLSV.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QLSV.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QLSV.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QLSV();
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFirst;
    private javax.swing.ButtonGroup btnGroupGT;
    private javax.swing.JButton btnImg;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnLogOut;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel jpnImg;
    private javax.swing.JPanel jpnQLSV;
    private javax.swing.JLabel jpnTitle;
    private javax.swing.JLabel lblDiaChi;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblGioiTinh;
    private javax.swing.JLabel lblHoTen;
    private javax.swing.JLabel lblImg;
    private javax.swing.JLabel lblMaSV;
    private javax.swing.JLabel lblSDT;
    private javax.swing.JRadioButton rdoNam;
    private javax.swing.JRadioButton rdoNu;
    private javax.swing.JTable tblQLSV;
    private javax.swing.JTextArea txtDiaChi;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtMaSV;
    private javax.swing.JTextField txtSDT;
    // End of variables declaration//GEN-END:variables
}
