package com.springapp.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Класс ответственный за отправку данных в модуль отображения данных
 */
@Controller
@ResponseBody
@RequestMapping("/")
public class ViewController {
    @RequestMapping(method = RequestMethod.POST, value="/getCurrentState")
    public String getCurrentState() {
        return "hello Masha";
    }
}
