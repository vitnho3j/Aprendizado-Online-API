package com.plataform.courses.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plataform.courses.model.entity.Purchase;
import com.plataform.courses.repository.PurchaseRepository;
import java.util.Optional;

@Service
public class PurchaseService {
    
    @Autowired
    private PurchaseRepository purchaseRepository;

    public Purchase findById(Long id){
        Optional<Purchase> purchase = this.purchaseRepository.findById(id);
        return purchase.orElseThrow(()-> new RuntimeException(
            "Compra não encontrada! Id: " + id + ", Tipo: " + Purchase.class.getName()
        ));
    }

    @Transactional
    public Purchase create(Purchase obj){
        obj.setId(null);
        obj = this.purchaseRepository.save(obj);
        return obj;
    }

}
