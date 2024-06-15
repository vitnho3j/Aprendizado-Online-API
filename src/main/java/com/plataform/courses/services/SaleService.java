package com.plataform.courses.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plataform.courses.model.entity.Sale;
import com.plataform.courses.repository.SaleRepository;

import jakarta.transaction.Transactional;

@Service
public class SaleService {
    
    @Autowired
    private SaleRepository saleRepository;

    public Sale findById(Long id){
        Optional<Sale> sale = this.saleRepository.findById(id);
        return sale.orElseThrow(()-> new RuntimeException(
            "Venda não encontrada! Id: " + id + ", Tipo: " + Sale.class.getName()
        ));
    }

    @Transactional
    public Sale create(Sale obj){
        obj.setId(null);
        obj = this.saleRepository.save(obj);
        return obj;
    }

}
