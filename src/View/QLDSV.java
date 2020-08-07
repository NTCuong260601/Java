package View;

import Controller.BaseDAOImplement;
import Model.Grades;
import Model.Students;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class QLDSV extends javax.swing.JFrame {

    protected ArrayList<Students> lstStudents;
    protected ArrayList<Grades> lsGrades;
    protected int index;
    protected DefaultTableModel model;
    protected Login login;

    public QLDSV() {
        initComponents();
        setLocationRelativeTo(null);
        setVisible(true);
        lstStudents = new ArrayList<>();
        lsGrades = new ArrayList<>();
        model = (DefaultTableModel) tblDSV.getModel();
        lsGrades = getAllGrade();
        lstStudents = getAllStudent();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    btnSave.setEnabled(checkNull());
                    btnUpdate.setEnabled(checkNull());
                    lstStudents = getAllStudent();
                     lsGrades = getAllGrade();
                    showTB();
                    lblHTHienThi.setText(getNameST(txtMaSV.getText()));
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(QLDSV.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        t.start();
         fillToTable(getTop3Grade());
    }

    public void showTB() {
        try {
            float ta = Float.parseFloat(txtTiengAnh.getText());
            float th = Float.parseFloat(txtTinHoc.getText());
            float gdtc = Float.parseFloat(txtGDTC.getText());
            if (ta >= 0 && ta <= 10 && th >= 0 && th <= 10 && gdtc >= 0 && gdtc <= 10) {
                Grades gr = new Grades();
                lblDiemTB.setText(gr.getDTB(ta, th, gdtc));
            } else {
                lblDiemTB.setText("0");
            }
        } catch (Exception e) {
            lblDiemTB.setText("0");
        }
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

    public ArrayList<Grades> getAllGrade() {
        ArrayList<Grades> list = new ArrayList<>();
        String tableName = BaseDAOImplement.getIstance().getTableName("Grade");
        String sql = "SELECT * FROM " + tableName + " ";
        try {
            Statement statement = BaseDAOImplement.getIstance().getConnection().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                list.add(new Grades(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getFloat(3),
                        rs.getFloat(4),
                        rs.getFloat(5))
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public ArrayList<Grades> getTop3Grade() {
        ArrayList<Grades> list = new ArrayList<>();
        String tableName = BaseDAOImplement.getIstance().getTableName("Grade");
        String sql = "SELECT TOP 3 * FROM " + tableName + "   ORDER BY (gradeE+gradeIM+gradeP)/3 DESC ";
        try {
            Statement statement = BaseDAOImplement.getIstance().getConnection().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                list.add(new Grades(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getFloat(3),
                        rs.getFloat(4),
                        rs.getFloat(5))
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public String getNameST(String idST) {
        for (Students st : lstStudents) {
            if (st.getIdST().equalsIgnoreCase(idST)) {
                return st.getNameST();
            }
        }
        return "";
    }

    public void fillToTable(ArrayList<Grades> list) {
        model.setRowCount(0);
        for (Grades gr : list) {
            model.addRow(new Object[]{
                gr.getIdST(),
                getNameST(gr.getIdST()),
                gr.getGradeE(),
                gr.getGradeIM(),
                gr.getGradeP(),
                gr.getDTB(gr.getGradeE(),
                gr.getGradeIM(),
                gr.getGradeP())});
        }
    }

    public int getIndex(String idSVGR) {
        for (int i = 0; i < lsGrades.size(); i++) {
            if (lsGrades.get(i).getIdST().equalsIgnoreCase(idSVGR)) {
                return i;
            }
        }
        return -1;
    }

    public void showDetail() {
        index = getIndex(model.getValueAt(index, 0) + "");
        System.out.println(model.getValueAt(index, 0));
        txtMaSV.setText(lsGrades.get(index).getIdST());
        txtTiengAnh.setText(lsGrades.get(index).getGradeE() + "");
        txtTinHoc.setText(lsGrades.get(index).getGradeIM() + "");
        txtGDTC.setText(lsGrades.get(index).getGradeP() + "");

        txtMaSV.setEditable(false);
        txtMaSV.setEnabled(false);
    }

    public boolean checkNull() {
        if (txtMaSV.getText().trim().equals("")
                || txtTiengAnh.getText().trim().equals("")
                || txtTinHoc.getText().trim().equals("")
                || txtGDTC.getText().trim().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkGrade() {
        try {
            float diemTA = Float.parseFloat(txtTiengAnh.getText());
            if (diemTA < 0 || diemTA > 10) {
                JOptionPane.showMessageDialog(this, "Điểm nằm trong khoảng từ 0-10");
                txtTiengAnh.requestFocus();
                return false;
            } else {
                try {
                    float diemTH = Float.parseFloat(txtTinHoc.getText());
                    if (diemTH < 0 || diemTH > 10) {
                        JOptionPane.showMessageDialog(this, "Điểm nằm trong khoảng từ 0-10");
                        txtTinHoc.requestFocus();
                        return false;
                    } else {
                        try {
                            float diemGDTC = Float.parseFloat(txtGDTC.getText());
                            if (diemGDTC < 0 || diemGDTC > 10) {
                                JOptionPane.showMessageDialog(this, "Điểm nằm trong khoảng từ 0-10");
                                txtGDTC.requestFocus();
                                return false;
                            } else {
                                return true;
                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(this, "Sai định dạng điểm");
                            txtGDTC.requestFocus();
                            return false;
                        }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Sai định dạng điểm");
                    txtTinHoc.requestFocus();
                    return false;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Sai định dạng điểm");
            txtTiengAnh.requestFocus();
            return false;
        }
    }

    public boolean checkExixtST(String idST) {
        for (Students st : lstStudents) {
            if (st.getIdST().equalsIgnoreCase(idST)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkExixstGR(String idST) {
        for (Grades gr : lsGrades) {
            if (gr.getIdST().equalsIgnoreCase(idST)) {
                return true;
            }
        }
        return false;
    }

    public HashMap<String, Object> readForm() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("masv", txtMaSV.getText());
        map.put("ta", txtTiengAnh.getText());
        map.put("th", txtTinHoc.getText());
        map.put("gdtc", txtGDTC.getText());
        return map;
    }

    public void clearForm() {
        txtMaSV.setText("");
        txtMaSVSearch.setText("");
        txtTiengAnh.setText("");
        txtTinHoc.setText("");
        txtGDTC.setText("");
        txtMaSV.setEditable(true);
        txtMaSV.setEnabled(true);
    }

    public void findST() {
        String idTK = txtMaSVSearch.getText();
        if (checkExixtST(idTK) == true && checkExixstGR(idTK) == false) {
            txtMaSV.setText(idTK);
            txtTiengAnh.setText("");
            txtTinHoc.setText("");
            txtGDTC.setText("");
            txtMaSV.setEditable(false);
            txtMaSV.setEnabled(false);
        } else if (checkExixstGR(idTK) == true && checkExixtST(idTK) == true) {
            for (Grades gr : lsGrades) {
                if (gr.getIdST().equalsIgnoreCase(idTK)) {
                    lblHTHienThi.setText(getNameST(idTK));
                    txtMaSV.setText(idTK);
                    txtTiengAnh.setText(gr.getGradeE() + "");
                    txtTinHoc.setText(gr.getGradeIM() + "");
                    txtGDTC.setText(gr.getGradeP() + "");
                    return;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Không tồn tại mã sv này");
        }
    }

    public void addGR() {
        HashMap<String, Object> map = readForm();
        String tableName = BaseDAOImplement.getIstance().getTableName("Grade");
        String[] columnName = new String[BaseDAOImplement.getIstance().getColumnName(tableName).length - 1];
        for (int i = 0; i < columnName.length; i++) {
            columnName[i] = BaseDAOImplement.getIstance().getColumnName(tableName)[i + 1];
        }
        Object[] values = {map.get("masv"), map.get("ta"), map.get("th"), map.get("gdtc")};
        int addGR = BaseDAOImplement.getIstance().addDB(tableName, columnName, "", values);
        if (addGR != 0) {
            fillToTable(getTop3Grade());
            lsGrades = getAllGrade();
            JOptionPane.showMessageDialog(this, "Thêm điểm cho SV thành công");
        } else {
            JOptionPane.showMessageDialog(this, "Thêm điểm cho SV thất bại");
        }
    }

    public void updateGR() {
        HashMap<String, Object> map = readForm();
        String tableName = BaseDAOImplement.getIstance().getTableName("Grade");
        String[] columnName = BaseDAOImplement.getIstance().getColumnName(tableName);
        String[] columnName1 = {columnName[2], columnName[3], columnName[4]};
        Object[] values = {map.get("ta"), map.get("th"), map.get("gdtc")};
        String condition = BaseDAOImplement.getIstance().getCondition(columnName[1], "LIKE", (String) map.get("masv"));

        int updateGR = BaseDAOImplement.getIstance().updateDB(tableName, columnName1, values, condition);
        if (updateGR != 0) {
            fillToTable(getTop3Grade());
            lsGrades = getAllGrade();
            JOptionPane.showMessageDialog(this, "Cập nhật điểm cho SV thành công");
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật cho SV thất bại");
        }
    }

    public void deleteGR() {
        HashMap<String, Object> map = readForm();
        String tableName = BaseDAOImplement.getIstance().getTableName("Grade");
        String[] columnName = BaseDAOImplement.getIstance().getColumnName(tableName);
        String condition = BaseDAOImplement.getIstance().getCondition(columnName[1], "LIKE", (String) map.get("masv"));
        int delete = BaseDAOImplement.getIstance().deleteDB(tableName, condition);
        if (delete > 0) {
            fillToTable(getTop3Grade());
            lsGrades = getAllGrade();
            JOptionPane.showMessageDialog(this, "Xóa thành công");
        } else {
            JOptionPane.showMessageDialog(this, "Xóa thất bại");
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        jpnSearch = new javax.swing.JPanel();
        lblMaSVSearch = new javax.swing.JLabel();
        txtMaSVSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jpnQLDSV = new javax.swing.JPanel();
        lblHoTen = new javax.swing.JLabel();
        lblMaSV = new javax.swing.JLabel();
        lblTiengAnh = new javax.swing.JLabel();
        lblTinHoc = new javax.swing.JLabel();
        lblGDTC = new javax.swing.JLabel();
        lblHTHienThi = new javax.swing.JLabel();
        txtMaSV = new javax.swing.JTextField();
        jpnDTB = new javax.swing.JPanel();
        lblDTB = new javax.swing.JLabel();
        lblDiemTB = new javax.swing.JLabel();
        btnNew = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnLogOut = new javax.swing.JButton();
        txtTiengAnh = new javax.swing.JTextField();
        txtTinHoc = new javax.swing.JTextField();
        txtGDTC = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDSV = new javax.swing.JTable();
        lblSVDC = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("QLDSV");

        lblTitle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTitle.setForeground(java.awt.Color.blue);
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Quản lí điểm sinh viên");

        jpnSearch.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.red), "Tìm kiếm", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Courier New", 1, 18), java.awt.Color.red)); // NOI18N

        lblMaSVSearch.setFont(new java.awt.Font("Segoe Print", 1, 18)); // NOI18N
        lblMaSVSearch.setText("Mã sinh viên:");

        btnSearch.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Icon/Search_Icon.png"))); // NOI18N
        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpnSearchLayout = new javax.swing.GroupLayout(jpnSearch);
        jpnSearch.setLayout(jpnSearchLayout);
        jpnSearchLayout.setHorizontalGroup(
            jpnSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnSearchLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMaSVSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtMaSVSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpnSearchLayout.setVerticalGroup(
            jpnSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnSearchLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaSVSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(txtMaSVSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jpnQLDSV.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.blue));

        lblHoTen.setFont(new java.awt.Font("Courier New", 1, 17)); // NOI18N
        lblHoTen.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblHoTen.setText("Họ và tên:");

        lblMaSV.setFont(new java.awt.Font("Courier New", 1, 17)); // NOI18N
        lblMaSV.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMaSV.setText("Mã sinh viên:");

        lblTiengAnh.setFont(new java.awt.Font("Courier New", 1, 17)); // NOI18N
        lblTiengAnh.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTiengAnh.setText("Tiếng anh:");

        lblTinHoc.setFont(new java.awt.Font("Courier New", 1, 17)); // NOI18N
        lblTinHoc.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTinHoc.setText("Tin học:");

        lblGDTC.setFont(new java.awt.Font("Courier New", 1, 17)); // NOI18N
        lblGDTC.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblGDTC.setText("GDTC:");

        lblHTHienThi.setFont(new java.awt.Font("Segoe Print", 1, 14)); // NOI18N
        lblHTHienThi.setForeground(java.awt.Color.blue);
        lblHTHienThi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jpnDTB.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.red));

        lblDTB.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblDTB.setForeground(java.awt.Color.blue);
        lblDTB.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDTB.setText("Điểm TB:");

        lblDiemTB.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblDiemTB.setForeground(java.awt.Color.blue);
        lblDiemTB.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDiemTB.setText("0");

        javax.swing.GroupLayout jpnDTBLayout = new javax.swing.GroupLayout(jpnDTB);
        jpnDTB.setLayout(jpnDTBLayout);
        jpnDTBLayout.setHorizontalGroup(
            jpnDTBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnDTBLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnDTBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDTB, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                    .addComponent(lblDiemTB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpnDTBLayout.setVerticalGroup(
            jpnDTBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnDTBLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDTB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblDiemTB, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btnNew.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Icon/New_Icon.png"))); // NOI18N
        btnNew.setText("New");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
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

        btnUpdate.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Icon/Update_Icon.png"))); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
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

        javax.swing.GroupLayout jpnQLDSVLayout = new javax.swing.GroupLayout(jpnQLDSV);
        jpnQLDSV.setLayout(jpnQLDSVLayout);
        jpnQLDSVLayout.setHorizontalGroup(
            jpnQLDSVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnQLDSVLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnQLDSVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnQLDSVLayout.createSequentialGroup()
                        .addComponent(lblTinHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTinHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnQLDSVLayout.createSequentialGroup()
                        .addComponent(lblGDTC, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtGDTC, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnQLDSVLayout.createSequentialGroup()
                        .addComponent(lblHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblHTHienThi, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnQLDSVLayout.createSequentialGroup()
                        .addComponent(lblMaSV, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtMaSV, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnQLDSVLayout.createSequentialGroup()
                        .addComponent(lblTiengAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTiengAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jpnDTB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpnQLDSVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLogOut, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jpnQLDSVLayout.setVerticalGroup(
            jpnQLDSVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnQLDSVLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnQLDSVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jpnQLDSVLayout.createSequentialGroup()
                        .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLogOut, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpnQLDSVLayout.createSequentialGroup()
                        .addGroup(jpnQLDSVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblHTHienThi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jpnQLDSVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMaSV, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaSV, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jpnQLDSVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTiengAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTiengAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jpnQLDSVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTinHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTinHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jpnQLDSVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblGDTC, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGDTC, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jpnDTB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblDSV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã SV", "Họ và tên", "Tiếng anh", "Tin học", "GDTC", "ĐTB"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDSV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDSVMouseClicked(evt);
            }
        });
        tblDSV.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblDSVKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblDSV);

        lblSVDC.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        lblSVDC.setForeground(java.awt.Color.blue);
        lblSVDC.setText("3 Sinh viên có điểm cao nhất:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 725, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 727, Short.MAX_VALUE)
                                .addComponent(jpnSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jpnQLDSV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSVDC))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jpnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jpnQLDSV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblSVDC)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        try {
            findST();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        try {
            clearForm();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        try {
            if (checkNull()) {
                if (checkGrade()) {
                    if (checkExixtST(txtMaSV.getText())) {
                        if (checkExixstGR(txtMaSV.getText())) {
                            int update = JOptionPane.showConfirmDialog(this, "Mã SV này đã có điểm bạn có muốn cập nhật !", "Update", JOptionPane.YES_NO_OPTION, JOptionPane.DEFAULT_OPTION);
                            if (update == 0) {
                                updateGR();
                                clearForm();
                            }
                        } else {
                            addGR();
                            clearForm();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Chưa có SV này trong danh sách sinh viên");
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
                if (checkGrade()) {
                    if (checkExixtST(txtMaSV.getText())) {
                        if (!checkExixstGR(txtMaSV.getText())) {
                            int add = JOptionPane.showConfirmDialog(this, "Mã SV này chưa có điểm bạn có muốn thêm moiws !", "Add", JOptionPane.YES_NO_OPTION, JOptionPane.DEFAULT_OPTION);
                            if (add == 0) {
                                addGR();
                                clearForm();
                            }
                        } else {
                            updateGR();
                            clearForm();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Chưa có SV này trong danh sách sinh viên");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        try {
            if (checkExixstGR(txtMaSV.getText())) {
                int delete = JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa điểm SV này không", "Delete", JOptionPane.YES_NO_OPTION, JOptionPane.DEFAULT_OPTION);
                if (delete == 0) {
                    deleteGR();
                    clearForm();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Chưa có SV này trong danh sách điểm");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

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

    private void tblDSVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDSVMouseClicked
        try {
            index = tblDSV.getSelectedRow();
            showDetail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_tblDSVMouseClicked

    private void tblDSVKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblDSVKeyReleased
        try {
            if (evt.getKeyCode() == evt.VK_UP || evt.getKeyCode() == evt.VK_DOWN) {
                index = tblDSV.getSelectedRow();
                showDetail();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_tblDSVKeyReleased

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
            java.util.logging.Logger.getLogger(QLDSV.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QLDSV.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QLDSV.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QLDSV.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QLDSV();
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnLogOut;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel jpnDTB;
    private javax.swing.JPanel jpnQLDSV;
    private javax.swing.JPanel jpnSearch;
    private javax.swing.JLabel lblDTB;
    private javax.swing.JLabel lblDiemTB;
    private javax.swing.JLabel lblGDTC;
    private javax.swing.JLabel lblHTHienThi;
    private javax.swing.JLabel lblHoTen;
    private javax.swing.JLabel lblMaSV;
    private javax.swing.JLabel lblMaSVSearch;
    private javax.swing.JLabel lblSVDC;
    private javax.swing.JLabel lblTiengAnh;
    private javax.swing.JLabel lblTinHoc;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblDSV;
    private javax.swing.JTextField txtGDTC;
    private javax.swing.JTextField txtMaSV;
    private javax.swing.JTextField txtMaSVSearch;
    private javax.swing.JTextField txtTiengAnh;
    private javax.swing.JTextField txtTinHoc;
    // End of variables declaration//GEN-END:variables
}
