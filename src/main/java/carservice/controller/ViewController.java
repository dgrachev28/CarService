package carservice.controller;

import carservice.domain.*;
import carservice.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping(method = RequestMethod.GET, value = "/startApplication")
    public String startApplication(@RequestParam Integer masterCount1,
                                   @RequestParam Integer masterCount2,
                                   @RequestParam Integer masterCount3,
                                   @RequestParam Integer masterCount4,
                                   @RequestParam Integer minIntervalMinutes,
                                   @RequestParam Integer maxIntervalMinutes,
                                   @RequestParam Integer timeCoefficient) {
        systemStateService.startApplication(new Integer[] {masterCount1, masterCount2, masterCount3, masterCount4},
                minIntervalMinutes, maxIntervalMinutes, timeCoefficient);

        return "";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/stopApplication")
    public String stopApplication() {
        systemStateService.stopApplication();
        return "";
    }

}
