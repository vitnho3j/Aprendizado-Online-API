package com.plataform.courses.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plataform.courses.model.entity.Course;
import com.plataform.courses.repository.CourseRepository;
import com.plataform.courses.services.exceptions.BadWordException;
import com.plataform.courses.services.exceptions.NotPermissionImmutableData;
import com.plataform.courses.services.exceptions.ObjectNotFoundException;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    private static String UNTANTED_CONTENT = "Conteudo indesejado detectado";

    private static String NOT_PERMISSION_DELETE = "Você não tem permissão para deletar este usuário";

    private static String NOT_PERMISSION_UPDATE = "Você não tem permissão para alterar este usuário";

    private static final Integer MAX_IMMUTABLE_RECORDS = 3;

    public Course findById(Long id){
        Optional<Course> course = this.courseRepository.findById(id);
        return course.orElseThrow(()-> new ObjectNotFoundException(
            "Curso não encontrado! Id: " + id + ", Tipo: " + Course.class.getName() 
        ));
    }

    @Transactional
    public Course create(Course obj){
        ContentFilterService filterService = new ContentFilterService();
        List<String> fieldsToCheck = Arrays.asList(obj.getName(), obj.getCategory(), obj.getDescription());
        if (filterService.containsBadWord(fieldsToCheck)) {
            throw new BadWordException(UNTANTED_CONTENT);
        }
        Long immutableCount = courseRepository.countByImmutableTrue();
        if (immutableCount >= MAX_IMMUTABLE_RECORDS){
            obj.setImmutable(false);
        } else {
            obj.setImmutable(true);
        }
        obj.setId(null);
        obj = this.courseRepository.save(obj);
        return obj;
    }

    @Transactional
    public Course update(Course obj){
        ContentFilterService filterService = new ContentFilterService();
        List<String> fieldsToCheck = Arrays.asList(obj.getName(), obj.getCategory(), obj.getDescription());
        if (filterService.containsBadWord(fieldsToCheck)) {
            throw new BadWordException(UNTANTED_CONTENT);
        }
        Course newObj = findById(obj.getId());
        if (newObj.getImmutable().equals(true)){
            throw new NotPermissionImmutableData(NOT_PERMISSION_UPDATE);
        }
        newObj.setAvailable(obj.getAvailable());
        newObj.setDescription(obj.getDescription());
        newObj.setName(obj.getName());
        newObj.setPrice(obj.getPrice());
        newObj.setImmutable(false);
        return this.courseRepository.save(newObj);
    }

    public void soft_delete(Long id){
        Course obj = findById(id);
        if (obj.getImmutable().equals(true)){
            throw new NotPermissionImmutableData(NOT_PERMISSION_DELETE);
        }
        obj.setAvailable(false);
        this.courseRepository.save(obj);
    }

    public List<Course>findAllByAuthorId(Long authorId){
        List<Course> courses = this.courseRepository.findByAuthor_Id(authorId);
        return courses;
    }

    public List<Course>findAll(){
        List<Course> courses = this.courseRepository.findAll();
        return courses;
    }
}
