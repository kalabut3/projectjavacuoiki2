package util;
import javax.swing.JFileChooser;
import java.io.File;
import model.Task;
import java.io.*;
import java.util.ArrayList;

public class ImportCSV {
    public static ArrayList<Task> importFile(){
        ArrayList<Task> list = new ArrayList<>();
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(null);
        if(result == JFileChooser.APPROVE_OPTION){
            File file = chooser.getSelectedFile();
            try{
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                br.readLine();
                while((line = br.readLine()) != null){
                    String[] data = line.split(",");
                    Task task = new Task();
                    task.setTitle(data[0]);
                    task.setDescription(data[1]);
                    task.setStartDate(data[2]);
                    task.setEndDate(data[3]);
                    task.setPriority(data[4]);
                    task.setStatus(data[5]);
                    task.setCategoryId(Integer.parseInt(data[6]));
                    task.setUserId(Integer.parseInt(data[7]));
                    list.add(task);
                }
                br.close();
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        return list;
    }
}
