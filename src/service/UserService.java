package service;

import dao.UserDAO;
import model.User;
import java.util.ArrayList;

public class UserService {

    UserDAO dao = new UserDAO();

    public User login(String username, String password){
        return dao.login(username, password);
    }

    public void addUser(User u){ dao.add(u); }
    public void updateUser(User u){ dao.update(u); }
    public void deleteUser(int id){ dao.delete(id); }
    public ArrayList<User> getAll(){ return dao.getAll(); }
}
