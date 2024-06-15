package com.plataform.courses.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plataform.courses.model.entity.Course;
import com.plataform.courses.repository.CourseRepository;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public Course findById(Long id){
        Optional<Course> course = this.courseRepository.findById(id);
        return course.orElseThrow(()-> new RuntimeException(
            "Curso não encontrado! Id: " + id + ", Tipo: " + Course.class.getName() 
        ));
    }

    @Transactional
    public Course create(Course obj){
        obj.setId(null);
        obj = this.courseRepository.save(obj);
        return obj;
    }

    @Transactional
    public Course update(Course obj){
        Course newObj = findById(obj.getId());
        newObj.setAvailable(obj.getAvailable());
        newObj.setDescription(obj.getDescription());
        newObj.setName(obj.getName());
        newObj.setPrice(obj.getPrice());
        return this.courseRepository.save(newObj);
    }

    public void delete(Long id){
        Course obj = findById(id);
        this.courseRepository.delete(obj);
    }
}
