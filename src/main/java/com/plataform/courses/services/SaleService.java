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
import com.plataform.courses.services.exceptions.DuplicateSaleException;
import com.plataform.courses.services.exceptions.ObjectNotFoundException;
import com.plataform.courses.services.exceptions.SellerNotEqualsToAuthorException;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class SaleService {

    private static final Integer MAX_IMMUTABLE_RECORDS = 5;

    private static String DUPLICATE_SELLER = "Este curso já foi vendido para este usuário";

    private static String SALE_INATIVE_USER = "Você não pode criar uma venda para um usuário inativo";

    private static String SALE_INATIVE_COURSE = "Você não pode criar uma venda para um curso inativo";
    
    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private CourseRepository courseRepository;

    
    @Autowired
    private UserRepository userRepository;

    public Sale findById(Long id){
        Optional<Sale> sale = this.saleRepository.findById(id);
        return sale.orElseThrow(()-> new ObjectNotFoundException(
            "Venda não encontrada! Id: " + id + ", Tipo: " + Sale.class.getName()
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


    public List<Sale>findAll(){
        List<Sale> sales = this.saleRepository.findAll();
        return sales;
    }

    @Transactional
    public Sale create(Sale obj){
        Course course = findByIdCourse(obj.getCourse().getId());
        User user = findByIdUser(obj.getSeller().getId());
        if (course.getActive().equals(false)){
            throw new CreateSaleWithCourseInactive(SALE_INATIVE_COURSE);
        }
        if (user.getActive().equals(false)){
            throw new CreateSaleWithSellerInactive(SALE_INATIVE_USER);
        }
        if (!course.getAuthor().getId().equals(obj.getSeller().getId())) {
            throw new SellerNotEqualsToAuthorException("O vendedor não é o autor do curso.");
        }
        Optional<Sale> existingSale = saleRepository.findBySellerIdAndCourseId(obj.getSeller().getId(), obj.getCourse().getId());
        if (existingSale.isPresent()){
            throw new DuplicateSaleException(DUPLICATE_SELLER);
        }
        Long immutableCount = saleRepository.countByImmutableTrue();
        if (immutableCount >= MAX_IMMUTABLE_RECORDS){
            obj.setImmutable(false);
        } else {
            obj.setImmutable(true);
        }
        obj.setValue(course.getPrice());
        obj.setId(null);
        obj = this.saleRepository.save(obj);
        return obj;
    }

    public Sale fromDTO(@Valid SaleCreateDTO obj){
        Sale sale = new Sale();
        sale.setSeller(obj.getSeller());
        sale.setCourse(obj.getCourse());
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
