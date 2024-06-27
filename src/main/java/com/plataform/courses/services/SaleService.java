package com.plataform.courses.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plataform.courses.model.dto.SaleCreateDTO;
import com.plataform.courses.model.entity.Course;
import com.plataform.courses.model.entity.Sale;
import com.plataform.courses.model.entity.User;
import com.plataform.courses.model.projections.SaleGetByIdCourseProjection;
import com.plataform.courses.model.projections.SaleGetByIdUserProjection;
import com.plataform.courses.repository.CourseRepository;
import com.plataform.courses.repository.SaleRepository;
import com.plataform.courses.repository.UserRepository;
import com.plataform.courses.services.exceptions.CreateSaleWithCourseInactive;
import com.plataform.courses.services.exceptions.CreateSaleWithSellerInactive;
import com.plataform.courses.services.exceptions.ObjectNotFoundException;
import com.plataform.courses.services.exceptions.SellerNotEqualsToAuthorException;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class SaleService {

    private static final Integer MAX_IMMUTABLE_RECORDS = 5;

    private static String SALE_INATIVE_USER = "Você não pode criar uma venda para um usuário inativo";

    private static String SALE_INATIVE_COURSE = "Você não pode criar uma venda para um curso inativo";
    
    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private CourseRepository courseRepository;

    
    @Autowired
    private UserRepository userRepository;

    public String generateSaleNotFoundMessage(Long id){
        return "Venda não encontrada! Id: " + id + ", Tipo: " + Sale.class.getName();
    }

    public String generateUserNotFoundMessage(Long id){
        return "Usuário não encontrado! Id: " + id + ", Tipo: " + User.class.getName();
    }

    public String generateCourseNotFoundMessage(Long id){
        return "Curso não encontrado! Id: " + id + ", Tipo: " + Course.class.getName();
    }
    

    public Sale findById(Long id){
        Optional<Sale> sale = this.saleRepository.findById(id);
        return sale.orElseThrow(()-> new ObjectNotFoundException(
            generateSaleNotFoundMessage(id)
        ));
    }

        public User findByIdUser(Long id){
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(()-> new ObjectNotFoundException(
           generateUserNotFoundMessage(id)
        ));
    }

    public Course findByIdCourse(Long id){
        Optional<Course> course = this.courseRepository.findById(id);
        return course.orElseThrow(()-> new ObjectNotFoundException(
            generateCourseNotFoundMessage(id)
        ));
    }


    public List<Sale>findAll(){
        List<Sale> sales = this.saleRepository.findAll();
        return sales;
    }

    public Sale generateSetsCreateDTO(SaleCreateDTO obj){
        Sale sale = new Sale();
        sale.setSeller(obj.getSeller());
        sale.setCourse(obj.getCourse());
        return sale;
    }

    public void checkCourseInactive(Course course){
        if (course.getActive().equals(false)){
            throw new CreateSaleWithCourseInactive(SALE_INATIVE_COURSE);
        }
    }

    public void checkSellerInactive(User user){
        if (user.getActive().equals(false)){
            throw new CreateSaleWithSellerInactive(SALE_INATIVE_USER);
        }
    }

    public void checkSellerEqualsToAuthor(Course course, Sale obj){
        if (!course.getAuthor().getId().equals(obj.getSeller().getId())) {
            throw new SellerNotEqualsToAuthorException("O vendedor não é o autor do curso.");
        }
    }

    public Sale countImmutableRecords(Sale obj){
        Long immutableCount = saleRepository.countByImmutableTrue();
        if (immutableCount >= MAX_IMMUTABLE_RECORDS){
            obj.setImmutable(false);
        } else {
            obj.setImmutable(true);
        }
        return obj;
    }

    public Sale generateSets(Sale obj, Course course){
        obj.setValue(course.getPrice());
        obj.setId(null);
        return obj;
    }

    @Transactional
    public Sale create(Sale obj){
        Course course = findByIdCourse(obj.getCourse().getId());
        User user = findByIdUser(obj.getSeller().getId());
        checkCourseInactive(course);
        checkSellerInactive(user);
        checkSellerEqualsToAuthor(course, obj);
        obj = countImmutableRecords(obj);
        obj = generateSets(obj, course);
        return this.saleRepository.save(obj);

    }

    public Sale fromDTO(@Valid SaleCreateDTO obj){
        Sale sale = generateSetsCreateDTO(obj);
        return sale;
    }

    public List<SaleGetByIdUserProjection> findBySeller_Id(Long sellerId){
        this.userRepository.findById(sellerId);
        List<SaleGetByIdUserProjection> sales = this.saleRepository.findBySeller_Id(sellerId);
        return sales;
    }

    public List<SaleGetByIdCourseProjection> findByCourse_Id(Long courseId){
        this.courseRepository.findById(courseId);
        List<SaleGetByIdCourseProjection> sales = this.saleRepository.findByCourse_Id(courseId);
        return sales;
    }

}
