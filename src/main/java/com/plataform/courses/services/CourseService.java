package com.plataform.courses.services;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plataform.courses.model.dto.CourseCreateDTO;
import com.plataform.courses.model.dto.CourseUpdateDTO;
import com.plataform.courses.model.entity.Course;
import com.plataform.courses.model.projections.CourseProjection;
import com.plataform.courses.repository.CourseRepository;
import com.plataform.courses.services.exceptions.BadWordException;
import com.plataform.courses.services.exceptions.NotPermissionImmutableData;
import com.plataform.courses.services.exceptions.ObjectNotFoundException;

import jakarta.validation.Valid;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    private static String UNTANTED_CONTENT = "Conteudo indesejado detectado";

    private static String NOT_PERMISSION_DELETE = "Você não tem permissão para deletar este curso";

    private static String NOT_PERMISSION_UPDATE = "Você não tem permissão para alterar este curso";

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
        newObj.setActive(obj.getActive());
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
        obj.setActive(false);
        obj.setDeleted_at(LocalDateTime.now());
        this.courseRepository.save(obj);
    }

    public List<CourseProjection>findAllByAuthorId(Long authorId){
        List<CourseProjection> courses = this.courseRepository.findByAuthor_Id(authorId);
        return courses;
    }

    public List<Course>findAll(){
        List<Course> courses = this.courseRepository.findAll();
        return courses;
    }

    public void recoverCourse(Long id){
        Course course = findById(id);
        course.setActive(true);
        course.setDeleted_at(null);
        this.courseRepository.save(course);
    }

    public List<Course> findByActiveTrue(){
        List<Course> courses = this.courseRepository.findByActiveTrue();
        return courses;
    }

    public List<Course> findByActiveFalse(){
        List<Course> courses = this.courseRepository.findByActiveFalse();
        return courses;
    }

    public Course fromDTO(@Valid CourseCreateDTO obj){
        Course course = new Course();
        course.setName(obj.getName());
        course.setCategory(obj.getCategory());
        course.setDescription(obj.getDescription());
        course.setAuthor(obj.getAuthor());
        course.setPrice(obj.getPrice());
        return course;
    }

    public Course fromDTO(@Valid CourseUpdateDTO obj){
        Course course = new Course();
        course.setId(obj.getId());
        course.setName(obj.getName());
        course.setCategory(obj.getCategory());
        course.setDescription(obj.getDescription());
        course.setPrice(obj.getPrice());
        return course;
    }
}
