package com.plataform.courses.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plataform.courses.model.dto.PurchaseCreateDTO;
import com.plataform.courses.model.entity.Course;
import com.plataform.courses.model.entity.Purchase;
import com.plataform.courses.model.entity.User;
import com.plataform.courses.model.projections.PurchaseGetByIdCourseProjection;
import com.plataform.courses.model.projections.PurchaseGetByIdUserProjection;
import com.plataform.courses.repository.CourseRepository;
import com.plataform.courses.repository.PurchaseRepository;
import com.plataform.courses.repository.UserRepository;
import com.plataform.courses.services.exceptions.CreatePurchaseWithBuyerInactive;
import com.plataform.courses.services.exceptions.CreatePurchaseWithCourseInactive;
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

    private static String PURCHASE_INATIVE_USER = "Você não pode criar uma compra para um usuário inativo";

    private static String PURCHASE_INATIVE_COURSE = "Você não pode criar uma compra para um usuário inativo";
    
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

    public User findByIdUser(Long id){
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(()-> new ObjectNotFoundException(
            "Usuário não encontrado! Id: " + id + ", Tipo: " + User.class.getName()
        ));
    }

    public Course findByIdCourse(Long id){
        Optional<Course> course = this.courseRepository.findById(id);
        return course.orElseThrow(()-> new ObjectNotFoundException(
            "Curso não encontrado! Id: " + id + ", Tipo: " + Course.class.getName()
        ));
    }

    public List<Purchase> findAll(){
        List<Purchase> purchases = this.purchaseRepository.findAll();
        return purchases;
    }

    @Transactional
    public Purchase create(Purchase obj){
        Course course = findByIdCourse(obj.getCourse().getId());
        User user = findByIdUser(obj.getBuyer().getId());
        if (course.getActive().equals(false)){
            throw new CreatePurchaseWithCourseInactive(PURCHASE_INATIVE_COURSE);
        }
        if (user.getActive().equals(false)){
            throw new CreatePurchaseWithBuyerInactive(PURCHASE_INATIVE_USER);
        }
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

    public List<PurchaseGetByIdUserProjection>findAllByBuyerId(Long buyerId){
        this.userRepository.findById(buyerId);
        List<PurchaseGetByIdUserProjection> purchases = this.purchaseRepository.findByBuyer_Id(buyerId);
        return purchases;
    }

    public List<PurchaseGetByIdCourseProjection>findAllByCourseId(Long courseId){
        this.courseRepository.findById(courseId);
        List<PurchaseGetByIdCourseProjection> purchases = this.purchaseRepository.findByCourse_Id(courseId);
        return purchases;
    }


}
