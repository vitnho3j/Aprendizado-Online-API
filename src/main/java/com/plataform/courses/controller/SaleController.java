package com.plataform.courses.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.plataform.courses.model.dto.SaleCreateDTO;
import com.plataform.courses.model.entity.Sale;
import com.plataform.courses.services.CourseService;
import com.plataform.courses.services.SaleService;
import com.plataform.courses.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sales")
@Validated
public class SaleController {

    @Autowired
    private SaleService saleService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<Void> create (@Valid @RequestBody SaleCreateDTO obj){
        Sale sale = this.saleService.fromDTO(obj);
        Sale newSale = this.saleService.create(sale);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}").buildAndExpand(newSale.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("")
    public ResponseEntity<List<Sale>> findAll(){
        List<Sale> sales = this.saleService.findAll();
        return ResponseEntity.ok().body(sales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> findById(@PathVariable Long id){
        Sale sale = this.saleService.findById(id);
        return ResponseEntity.ok().body(sale);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Sale>> findAllBySellerId(@PathVariable Long id){
        this.userService.findById(id);
        List<Sale> sales = this.saleService.findBySeller_Id(id);
        return ResponseEntity.ok().body(sales);
    }

    @GetMapping("/course/{id}")
    public ResponseEntity<List<Sale>> findAllByCourseId(@PathVariable Long id){
        this.courseService.findById(id);
        List<Sale> sales = this.saleService.findBySeller_Id(id);
        return ResponseEntity.ok().body(sales);
    }
    
}
