package carservice.controller;

import carservice.dao.WorkshopDAO;
import carservice.domain.*;
import carservice.service.TaskExecutorExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping(method = RequestMethod.GET, value = "/getCurrentState", produces = "application/json")
    @ResponseBody
    public CarService getCurrentState() {

        CarService carService = new CarService();
        carService.setWorkshops(workshopDAO.getWorkshopList());
        return carService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/startQueueThread")
    @ResponseBody
    public String startQueueThread() {
        taskExecutorExample.startGeneratingTickets();
        return "";
    }

}
