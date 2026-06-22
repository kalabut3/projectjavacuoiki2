package service;

import dao.CategoryDAO;
import model.Category;
import java.util.ArrayList;

public class CategoryService {

    CategoryDAO dao = new CategoryDAO();

    public void addCategory(Category c){ dao.add(c); }
    public void updateCategory(Category c){ dao.update(c); }
    public void deleteCategory(int id){ dao.delete(id); }
    public ArrayList<Category> getAll(){ return dao.getAll(); }
}
