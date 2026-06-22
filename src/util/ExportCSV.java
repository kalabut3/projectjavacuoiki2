package util;

import model.Task;
import java.io.FileWriter;
import java.util.ArrayList;

public class ExportCSV {
    public static void export(ArrayList<Task> list){
        try {
            FileWriter writer = new FileWriter("tasks.csv");
            writer.append("title,description,start,end,priority,status,category,user\n");
            for (Task t : list) {
                writer.append(t.getTitle()).append(",");
                writer.append(t.getDescription()).append(",");
                writer.append(t.getStartDate()).append(",");
                writer.append(t.getEndDate()).append(",");
                writer.append(t.getPriority()).append(",");
                writer.append(t.getStatus()).append(",");
                writer.append(String.valueOf(t.getCategoryId())).append(",");
                writer.append(String.valueOf(t.getUserId()));
                writer.append("\n");
            }
            writer.flush();
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
