package carservice.controller;

import carservice.dao.WorkshopDAO;
import carservice.domain.*;
import carservice.service.StatisticsService;
import carservice.service.TaskExecutorExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс ответственный за отправку данных в модуль отображения данных
 */
@Controller
@RequestMapping("/")
public class ViewController {

    @Autowired
    public WorkshopDAO workshopDAO;

    @Autowired
    public TaskExecutorExample taskExecutorExample;

    @Autowired
    public StatisticsService statisticsService;

    @RequestMapping(method = RequestMethod.GET, value = "/getCurrentState", produces = "application/json")
    @ResponseBody
    public CarService getCurrentState() {
        Statistics statistics = statisticsService.getStatistics();
        CarService carService = new CarService();
        carService.setWorkshops(workshopDAO.getWorkshopList());
        for (Workshop workshop : carService.getWorkshops()) {
            List<IncomeTicket> completeTickets = new ArrayList<IncomeTicket>();

            for (IncomeTicket ticket : workshop.getQueue()) {
                if (ticket.getStatus().equals("Complete")) {
                    completeTickets.add(ticket);
                }
            }

            for (IncomeTicket ticket : completeTickets) {
                workshop.getQueue().remove(ticket);
            }


        }


        carService.setStatistics(statistics);
        return carService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/startQueueThread")
    @ResponseBody
    public String startQueueThread() {
        taskExecutorExample.startGeneratingTickets();
        return "";
    }

}
