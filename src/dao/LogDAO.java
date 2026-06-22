package dao;

import model.Log;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;

public class LogDAO {

    public void add(Log log){
        try{
            Connection con = DatabaseConnection.getConnection();
            String sql = "INSERT INTO logs(username,action) VALUES(?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, log.getUsername());
            ps.setString(2, log.getAction());
            ps.executeUpdate();
            ps.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Log> getAll(){
        ArrayList<Log> list = new ArrayList<>();
        try{
            Connection con = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM logs ORDER BY id DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Log log = new Log();
                log.setId(rs.getInt("id"));
                log.setUsername(rs.getString("username"));
                log.setAction(rs.getString("action"));
                log.setTime(rs.getString("time"));
                list.add(log);
            }
            rs.close();
            ps.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
