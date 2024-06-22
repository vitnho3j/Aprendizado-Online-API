package com.plataform.courses.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plataform.courses.model.entity.Course;
import com.plataform.courses.model.entity.Purchase;
import com.plataform.courses.repository.CourseRepository;
import com.plataform.courses.repository.PurchaseRepository;
import com.plataform.courses.services.exceptions.ObjectNotFoundException;
import com.plataform.courses.services.exceptions.SellerNotEqualsToAuthorException;

import java.util.Optional;

@Service
public class PurchaseService {
    
    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private CourseRepository courseRepository;

    public Purchase findById(Long id){
        Optional<Purchase> purchase = this.purchaseRepository.findById(id);
        return purchase.orElseThrow(()-> new ObjectNotFoundException(
            "Compra não encontrada! Id: " + id + ", Tipo: " + Purchase.class.getName()
        ));
    }

    @Transactional
    public Purchase create(Purchase obj){
        Course course = courseRepository.findById(obj.getCourse().getId())
        .orElseThrow(() -> new ObjectNotFoundException (
            "Curso não encontrado"
        ));
        if (course.getAuthor().getId().equals(obj.getBuyer().getId())) {
            throw new SellerNotEqualsToAuthorException("O autor do curso não pode comprar o próprio curso.");
        }
        obj.setValue(course.getPrice());
        obj.setId(null);
        obj = this.purchaseRepository.save(obj);
        return obj;
    }

}
