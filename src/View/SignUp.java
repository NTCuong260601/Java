package View;

import Controller.BaseDAOImplement;
import Model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class SignUp extends javax.swing.JFrame {

    protected Login login;
    protected ArrayList<User> lstUsers;

    public SignUp() {
        initComponents();
        setLocationRelativeTo(null);
        setVisible(true);
        lstUsers = new ArrayList<>();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    btnSignUp.setEnabled(checkNull());
                    lstUsers = getAllUser();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SignUp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        t.start();
    }

    public ArrayList<User> getAllUser() {
        ArrayList<User> list = new ArrayList<>();
        String tableName = BaseDAOImplement.getIstance().getTableName("User");
        String sql = "SELECT * FROM " + tableName + "";
        try {
            Statement statement = BaseDAOImplement.getIstance().getConnection().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                list.add(new User(rs.getString(1), rs.getString(2), rs.getString(3)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public boolean checkNull() {
        if (txtUserName.getText().trim().equals("") || txtPassword.getText().equals("")) {
            return false;
        } else if (txtPassword.getText().contains(" ")) {
            return false;
        } else if (txtPassword.getText().length() < 8) {
            return false;
        } else if (!txtConfirm.getText().equals(txtPassword.getText())) {
            return false;
        } else {
            return true;
        }
    }

    public void clearForm() {
        txtUserName.setText("");
        txtPassword.setText("");
        txtConfirm.setText("");
    }

    public User readForm() {
        String name = txtUserName.getText();
        String pass = txtPassword.getText();
        String position = chkLecturer.isSelected() == true ? "Lecturer" : "Education";
        return new User(name, pass, position);
    }

    public void SignUp() {
        User user = readForm();
        if (lstUsers.size() > 0) {
            for (User us : lstUsers) {
                if (us.getUserName().equalsIgnoreCase(user.getUserName())) {
                    lblFaieldUS.setText("Tài khoản này đã tồn tại !!!");
                    return;
                }
            }
        }
        lblFaieldUS.setText("");
        String tableName = BaseDAOImplement.getIstance().getTableName("User");
        String[] columnName = BaseDAOImplement.getIstance().getColumnName(tableName);
        Object[] values = {user.getUserName(), user.getPassword(), user.getPosition()};
        int addUS = BaseDAOImplement.getIstance().addDB(tableName, columnName, "", values);
        if (addUS == 0) {
            JOptionPane.showMessageDialog(this, "Tạo tài khoản thất bại");
        } else {
            JOptionPane.showMessageDialog(this, "Tạo tài khoản thành công");
            clearForm();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        jpnSignup = new javax.swing.JPanel();
        lblUserName = new javax.swing.JLabel();
        lblPassWord = new javax.swing.JLabel();
        lblConfirm = new javax.swing.JLabel();
        txtUserName = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        txtConfirm = new javax.swing.JPasswordField();
        chkLecturer = new javax.swing.JCheckBox();
        btnCancel = new javax.swing.JButton();
        btnSignUp = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        lblFaieldUS = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SIGN UP");

        lblTitle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTitle.setForeground(java.awt.Color.blue);
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Sign Up");

        jpnSignup.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.red));

        lblUserName.setFont(new java.awt.Font("Gabriola", 1, 24)); // NOI18N
        lblUserName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblUserName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Icon/User_Icon.png"))); // NOI18N
        lblUserName.setText("User Name:");

        lblPassWord.setFont(new java.awt.Font("Gabriola", 1, 24)); // NOI18N
        lblPassWord.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblPassWord.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Icon/Password_Icon.png"))); // NOI18N
        lblPassWord.setText("Password:");

        lblConfirm.setFont(new java.awt.Font("Gabriola", 1, 24)); // NOI18N
        lblConfirm.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblConfirm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Icon/Password_Icon.png"))); // NOI18N
        lblConfirm.setText("Confirm:");

        chkLecturer.setFont(new java.awt.Font("Gabriola", 1, 24)); // NOI18N
        chkLecturer.setText("Lecturer?");

        btnCancel.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Icon/Cancel_Icon.png"))); // NOI18N
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnSignUp.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnSignUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Icon/SignUp_Icon.png"))); // NOI18N
        btnSignUp.setText("Sign Up");
        btnSignUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSignUpActionPerformed(evt);
            }
        });

        btnClear.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/View/Icon/Clear_Icon.png"))); // NOI18N
        btnClear.setText("CLear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        lblFaieldUS.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lblFaieldUS.setForeground(java.awt.Color.red);

        javax.swing.GroupLayout jpnSignupLayout = new javax.swing.GroupLayout(jpnSignup);
        jpnSignup.setLayout(jpnSignupLayout);
        jpnSignupLayout.setHorizontalGroup(
            jpnSignupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnSignupLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnSignupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpnSignupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jpnSignupLayout.createSequentialGroup()
                            .addComponent(lblConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jpnSignupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(chkLecturer, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jpnSignupLayout.createSequentialGroup()
                            .addComponent(lblUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jpnSignupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblFaieldUS)
                                .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jpnSignupLayout.createSequentialGroup()
                            .addComponent(btnSignUp, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jpnSignupLayout.createSequentialGroup()
                        .addComponent(lblPassWord, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpnSignupLayout.setVerticalGroup(
            jpnSignupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpnSignupLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpnSignupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblFaieldUS)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpnSignupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPassWord, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpnSignupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(chkLecturer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpnSignupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSignUp, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpnSignup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jpnSignup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSignUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignUpActionPerformed
        try {
            SignUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSignUpActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        try {
            int cancel = JOptionPane.showConfirmDialog(this, "Thoát đăng ký !", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.CANCEL_OPTION);
            if (cancel == 0) {
               this.setVisible(false);
               login = new Login();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        try {
            clearForm();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnClearActionPerformed

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
            java.util.logging.Logger.getLogger(SignUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SignUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SignUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SignUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SignUp();
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnSignUp;
    private javax.swing.JCheckBox chkLecturer;
    private javax.swing.JPanel jpnSignup;
    private javax.swing.JLabel lblConfirm;
    private javax.swing.JLabel lblFaieldUS;
    private javax.swing.JLabel lblPassWord;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JPasswordField txtConfirm;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables
}
