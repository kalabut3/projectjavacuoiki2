package service;

import dao.LogDAO;
import model.Log;
import java.util.ArrayList;

public class LogService {

    public void add(Log log){
        LogDAO dao = new LogDAO();
        dao.add(log);
    }

    public ArrayList<Log> getAll(){
        LogDAO dao = new LogDAO();
        return dao.getAll();
    }
}
