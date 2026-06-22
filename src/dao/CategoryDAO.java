package dao;
import model.Category;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;

public class CategoryDAO {

    public void add(Category c){
        try{
            Connection con = DatabaseConnection.getConnection();
            String sql = "insert into categories(name,description) values(?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,c.getName());
            ps.setString(2,c.getDescription());
            ps.executeUpdate();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void update(Category c){
        try{
            Connection con = DatabaseConnection.getConnection();
            String sql = "update categories set name=?,description=? where id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,c.getName());
            ps.setString(2,c.getDescription());
            ps.setInt(3,c.getId());
            ps.executeUpdate();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void delete(int id){
        try{
            Connection con = DatabaseConnection.getConnection();
            String sql = "delete from categories where id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,id);
            ps.executeUpdate();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Category> getAll(){
        ArrayList<Category> list = new ArrayList<>();
        try{
            Connection con = DatabaseConnection.getConnection();
            String sql = "select * from categories";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Category c = new Category();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setDescription(rs.getString("description"));
                list.add(c);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
