package ui;

import model.User;

/** Legacy entry-point kept for compatibility; delegates to UserDashboardFrame */
public class DashboardFrame extends javax.swing.JFrame {

    private User currentUser;

    public DashboardFrame(User user){
        currentUser = user;
        init();
    }

    private void init(){
        // Redirect to new frame
        new UserDashboardFrame(currentUser).setVisible(true);
        dispose();
    }
}
