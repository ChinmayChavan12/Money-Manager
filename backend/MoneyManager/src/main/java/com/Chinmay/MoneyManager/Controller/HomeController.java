package com.Chinmay.MoneyManager.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping({"/status","/health"})
    public String healthCheck(){
        return "Application is running";
    }

}
