package com.plataform.courses.controller;

import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @RequestMapping("/users")
    public String main(){
        return "users";
    }

    @RequestMapping("/courses")
    public String courses(){
        return "courses";
    }
}
