package com.plataform.courses.controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @Value("${baseURL.api}")
    String baseURL;

    @Value("${baseURL}")
    String baseURL_schedule;
    
    private static final long FIXED_RATE = 2 * 60 * 60 * 1000; // 2 hours in milliseconds
    private static final long START_TIME = System.currentTimeMillis();

    @GetMapping({"/users", "/", "home", "index"})
    public String main(Model model){
        model.addAttribute("baseURL", baseURL);
        model.addAttribute("baseURL_schedule", baseURL_schedule);
        return "users";
    }

    @GetMapping("/courses")
    public String courses(Model model){
        model.addAttribute("baseURL", baseURL);
        model.addAttribute("baseURL_schedule", baseURL_schedule);
        return "courses";
    }

    @GetMapping("/sales")
    public String sales(Model model){
        model.addAttribute("baseURL", baseURL);
        model.addAttribute("baseURL_schedule", baseURL_schedule);
        return "sales";
    }

    @GetMapping("/purchases")
    public String purchases(Model model){
        model.addAttribute("baseURL", baseURL);
        model.addAttribute("baseURL_schedule", baseURL_schedule);
        return "purchases";
    }

    @RequestMapping("*")
    public String not_found(){
        return "404";
    }

    @GetMapping("/schedule")
    public ResponseEntity<Long> getNextExecutionTime() {
        long currentTime = System.currentTimeMillis();
        long timeElapsed = currentTime - START_TIME;
        long timeToNextExecution = FIXED_RATE - (timeElapsed % FIXED_RATE);
        return ResponseEntity.ok(timeToNextExecution);
    }

}
