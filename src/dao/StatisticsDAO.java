package dao;

import util.DatabaseConnection;
import java.sql.*;

public class StatisticsDAO {

    public int countAll(){
        int total = 0;
        try{
            Connection con = DatabaseConnection.getConnection();
            String sql = "select count(*) from tasks";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                total = rs.getInt(1);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return total;
    }

    public int countStatus(String status){
        int total = 0;
        try{
            Connection con = DatabaseConnection.getConnection();
            String sql = "select count(*) from tasks where status=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                total = rs.getInt(1);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return total;
    }
}
