package dao;

import model.Task;
import util.DatabaseConnection;
import java.util.ArrayList;
import java.sql.*;

public class TaskDAO {

    public void add(Task t){
        try{
            Connection con = DatabaseConnection.getConnection();
            String sql = "insert into tasks(title,description,start_date,end_date,priority,status,category_id,user_id)"
                    + " values(?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,t.getTitle());
            ps.setString(2,t.getDescription());
            ps.setString(3,t.getStartDate());
            ps.setString(4,t.getEndDate());
            ps.setString(5,t.getPriority());
            ps.setString(6,t.getStatus());
            ps.setInt(7,t.getCategoryId());
            ps.setInt(8,t.getUserId());
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void update(Task task){
        try{
            Connection con = DatabaseConnection.getConnection();
            String sql = "update tasks set title=?,description=?,start_date=?,end_date=?,priority=?,status=?,category_id=?,user_id=? where id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,task.getTitle());
            ps.setString(2,task.getDescription());
            ps.setString(3,task.getStartDate());
            ps.setString(4,task.getEndDate());
            ps.setString(5,task.getPriority());
            ps.setString(6,task.getStatus());
            ps.setInt(7,task.getCategoryId());
            ps.setInt(8,task.getUserId());
            ps.setInt(9,task.getId());
            ps.executeUpdate();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void delete(int id){
        try{
            Connection con = DatabaseConnection.getConnection();
            String sql = "delete from tasks where id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,id);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Task> getAll(){
        ArrayList<Task> list = new ArrayList<>();
        try{
            Connection con = DatabaseConnection.getConnection();
            String sql = "select * from tasks order by id desc ";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Task t = mapRow(rs);
                list.add(t);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Task> search(String keyword){
        ArrayList<Task> list = new ArrayList<>();
        try{
            Connection con = DatabaseConnection.getConnection();
            String sql = "select * from tasks where title like ? or description like ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%"+keyword+"%");
            ps.setString(2, "%"+keyword+"%");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                list.add(mapRow(rs));
            }
            rs.close();
            ps.close();
            con.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public Task getById(int id){
        Task t = null;
        try{
            Connection con = DatabaseConnection.getConnection();
            String sql = "select * from tasks where id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                t = mapRow(rs);
            }
            rs.close();
            ps.close();
            con.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        return t;
    }

    private Task mapRow(ResultSet rs) throws SQLException {
        Task t = new Task();
        t.setId(rs.getInt("id"));
        t.setTitle(rs.getString("title"));
        t.setDescription(rs.getString("description"));
        t.setStartDate(rs.getString("start_date"));
        t.setEndDate(rs.getString("end_date"));
        t.setPriority(rs.getString("priority"));
        t.setStatus(rs.getString("status"));
        t.setCategoryId(rs.getInt("category_id"));
        t.setUserId(rs.getInt("user_id"));
        return t;
    }
}
