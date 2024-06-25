package com.plataform.courses.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plataform.courses.model.dto.SaleCreateDTO;
import com.plataform.courses.model.entity.Course;
import com.plataform.courses.model.entity.Sale;
import com.plataform.courses.repository.CourseRepository;
import com.plataform.courses.repository.SaleRepository;
import com.plataform.courses.services.exceptions.DuplicateSaleException;
import com.plataform.courses.services.exceptions.ObjectNotFoundException;
import com.plataform.courses.services.exceptions.SellerNotEqualsToAuthorException;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class SaleService {

    private static final Integer MAX_IMMUTABLE_RECORDS = 5;

    private static String DUPLICATE_SELLER = "Este curso já foi vendido para este usuário";
    
    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private CourseRepository courseRepository;

    public Sale findById(Long id){
        Optional<Sale> sale = this.saleRepository.findById(id);
        return sale.orElseThrow(()-> new ObjectNotFoundException(
            "Venda não encontrada! Id: " + id + ", Tipo: " + Sale.class.getName()
        ));
    }

    public List<Sale>findAll(){
        List<Sale> sales = this.saleRepository.findAll();
        return sales;
    }

    @Transactional
    public Sale create(Sale obj){
        Course course = courseRepository.findById(obj.getCourse().getId())
                .orElseThrow(() -> new ObjectNotFoundException("Curso não encontrado"));
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

}
