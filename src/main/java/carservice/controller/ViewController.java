package carservice.controller;

import carservice.domain.*;
import carservice.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ViewController {

    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private CurrentStateService currentStateService;
    @Autowired
    private SystemStateService systemStateService;

    @RequestMapping(method = RequestMethod.GET, value = "/getCurrentState", produces = "application/json")
    public CarService getCurrentState() {
        CarService carService = new CarService();
        carService.setStatistics(statisticsService.getStatistics());
        carService.setWorkshops(currentStateService.getWorkshopList());
        carService.setCurrentDateTime(currentStateService.getCurrentDateTime());
        return carService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/startQueueThread")
    public String startQueueThread() {
        systemStateService.startApplication();
        return "";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/stopApplication")
    public String stopApplication() {
        systemStateService.stopApplication();
        return "";
    }

}
