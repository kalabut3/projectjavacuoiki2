package service;

import dao.StatisticsDAO;

public class StatisticsService {

    StatisticsDAO dao = new StatisticsDAO();

    public int getTotal(){ return dao.countAll(); }
    public int getTodo(){ return dao.countStatus("TODO"); }
    public int getDoing(){ return dao.countStatus("DOING"); }
    public int getDone(){ return dao.countStatus("DONE"); }
}
