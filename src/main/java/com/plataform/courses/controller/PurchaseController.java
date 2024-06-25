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

import com.plataform.courses.model.dto.PurchaseCreateDTO;
import com.plataform.courses.model.entity.Purchase;
import com.plataform.courses.services.PurchaseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/purchases")
@Validated
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<Void> create (@Valid @RequestBody PurchaseCreateDTO obj){
        Purchase purchase = this.purchaseService.fromDTO(obj);
        Purchase newPurchase = this.purchaseService.create(purchase);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}").buildAndExpand(newPurchase.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("")
    public ResponseEntity<List<Purchase>> findAll(){
        List<Purchase> purchases = this.purchaseService.findAll();
        return ResponseEntity.ok().body(purchases);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Purchase> findById(@PathVariable Long id){
        Purchase purchase = this.purchaseService.findById(id);
        return ResponseEntity.ok().body(purchase);
    }
    
}
