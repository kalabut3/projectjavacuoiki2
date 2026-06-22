package dao;

import model.User;
import util.DatabaseConnection;
import java.util.ArrayList;
import java.sql.*;

public class UserDAO {

    public User login(String username, String password){
        try{
            Connection con = DatabaseConnection.getConnection();
            String sql = "select * from users where username=? and password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setFullname(rs.getString("fullname"));
                u.setRole(rs.getString("role"));
                return u;
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void add(User u){
        try{
            Connection con = DatabaseConnection.getConnection();
            String sql = "insert into users(username,password,fullname,role) values(?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,u.getUsername());
            ps.setString(2,u.getPassword());
            ps.setString(3,u.getFullname());
            ps.setString(4,u.getRole());
            ps.executeUpdate();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void update(User u){
        try{
            Connection con = DatabaseConnection.getConnection();
            String sql = "update users set username=?,password=?,fullname=?,role=? where id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,u.getUsername());
            ps.setString(2,u.getPassword());
            ps.setString(3,u.getFullname());
            ps.setString(4,u.getRole());
            ps.setInt(5,u.getId());
            ps.executeUpdate();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void delete(int id){
        try{
            Connection con = DatabaseConnection.getConnection();
            String sql = "delete from users where id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,id);
            ps.executeUpdate();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<User> getAll(){
        ArrayList<User> list = new ArrayList<>();
        try{
            Connection con = DatabaseConnection.getConnection();
            String sql = "select * from users";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setFullname(rs.getString("fullname"));
                u.setRole(rs.getString("role"));
                list.add(u);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
