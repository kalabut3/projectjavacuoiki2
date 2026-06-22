package service;

import model.User;

public class AuthService {

    UserService service = new UserService();

    public User login(String username, String password){
        return service.login(username, password);
    }
}
