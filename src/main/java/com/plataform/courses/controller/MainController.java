package com.plataform.courses.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @GetMapping({"/users", "/", "home", "index"})
    public String main(){
        return "users";
    }

    @GetMapping("/courses")
    public String courses(){
        return "courses";
    }

    @GetMapping("/sales")
    public String sales(){
        return "sales";
    }

    @GetMapping("/purchases")
    public String purchases(){
        return "purchases";
    }

    @RequestMapping("*")
    public String not_found(){
        return "404";
    }

}
