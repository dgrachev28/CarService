package carservice.controller;

import carservice.dao.WorkshopMasterDAO;
import carservice.domain.CarService;
import carservice.domain.Master;
import carservice.domain.Workshop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Класс ответственный за отправку данных в модуль отображения данных
 */
@Controller
@RequestMapping("/")
public class ViewController {

    @Autowired
    public WorkshopMasterDAO workshopMasterDAO;

    @RequestMapping(method = RequestMethod.GET, value = "/getCurrentState", produces = "application/json")
    @ResponseBody
    public CarService getCurrentState() {
        CarService carService = new CarService();
        carService.setWorkshops(workshopMasterDAO.getWorkshopList());
        return carService;
    }
}
