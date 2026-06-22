package service;
import java.util.ArrayList;
import dao.TaskDAO;
import model.Task;

public class TaskService {

    private TaskDAO dao = new TaskDAO();

    public void addTask(Task task){
        if(task == null){
            return;
        }
        dao.add(task);
    }

    public void updateTask(Task task){
        dao.update(task);
    }

    public void deleteTask(int id){
        if(id > 0){
            dao.delete(id);
        }
    }

    public ArrayList<Task> getAll(){
        return dao.getAll();
    }

    public ArrayList<Task> search(String key){
        if(key == null){
            key = "";
        }
        return dao.search(key);
    }
}
