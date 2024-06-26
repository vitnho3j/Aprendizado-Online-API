package com.plataform.courses.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plataform.courses.model.dto.PurchaseCreateDTO;
import com.plataform.courses.model.entity.Course;
import com.plataform.courses.model.entity.Purchase;
import com.plataform.courses.repository.CourseRepository;
import com.plataform.courses.repository.PurchaseRepository;
import com.plataform.courses.repository.UserRepository;
import com.plataform.courses.services.exceptions.DuplicatePurchaseException;
import com.plataform.courses.services.exceptions.ObjectNotFoundException;
import com.plataform.courses.services.exceptions.SellerNotEqualsToAuthorException;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@Service
public class PurchaseService {

    private static final Integer MAX_IMMUTABLE_RECORDS = 5;

    private static String DUPLICATE_PURCHASE = "Este usuário já possui este curso";
    
    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    public Purchase findById(Long id){
        Optional<Purchase> purchase = this.purchaseRepository.findById(id);
        return purchase.orElseThrow(()-> new ObjectNotFoundException(
            "Compra não encontrada! Id: " + id + ", Tipo: " + Purchase.class.getName()
        ));
    }

    public List<Purchase> findAll(){
        List<Purchase> purchases = this.purchaseRepository.findAll();
        return purchases;
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
        Optional<Purchase> existingPurchase = purchaseRepository.findByBuyerIdAndCourseId(obj.getBuyer().getId(), obj.getCourse().getId());
        if (existingPurchase.isPresent()){
            throw new DuplicatePurchaseException(DUPLICATE_PURCHASE);
        }
        Long immutableCount = purchaseRepository.countByImmutableTrue();
        if (immutableCount >= MAX_IMMUTABLE_RECORDS){
            obj.setImmutable(false);
        } else {
            obj.setImmutable(true);
        }
        obj.setValue(course.getPrice());
        obj.setId(null);
        obj = this.purchaseRepository.save(obj);
        return obj;
    }

    public Purchase fromDTO(@Valid PurchaseCreateDTO obj){
        Purchase purchase = new Purchase();
        purchase.setBuyer(obj.getBuyer());
        purchase.setCourse(obj.getCourse());
        return purchase;
    }

    public List<Purchase>findAllByBuyerId(Long buyerId){
        this.userRepository.findById(buyerId);
        List<Purchase> purchases = this.purchaseRepository.findByBuyer_Id(buyerId);
        return purchases;
    }

    public List<Purchase>findAllByCourseId(Long courseId){
        this.courseRepository.findById(courseId);
        List<Purchase> purchases = this.purchaseRepository.findByCourse_Id(courseId);
        return purchases;
    }


}
