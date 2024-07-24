package com.plataform.courses.controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @Value("${baseURL.api}")
    String baseURL;

    @GetMapping({"/users", "/", "home", "index"})
    public String main(Model model){
        model.addAttribute("baseURL", baseURL);
        return "users";
    }

    @GetMapping("/courses")
    public String courses(Model model){
        model.addAttribute("baseURL", baseURL);
        return "courses";
    }

    @GetMapping("/sales")
    public String sales(Model model){
        model.addAttribute("baseURL", baseURL);
        return "sales";
    }

    @GetMapping("/purchases")
    public String purchases(Model model){
        model.addAttribute("baseURL", baseURL);
        return "purchases";
    }

    @RequestMapping("*")
    public String not_found(){
        return "404";
    }

}
