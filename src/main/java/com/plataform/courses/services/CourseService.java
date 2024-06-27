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
import com.plataform.courses.repository.UserRepository;
import com.plataform.courses.services.exceptions.BadWordException;
import com.plataform.courses.services.exceptions.CourseInactiveUpdateException;
import com.plataform.courses.services.exceptions.CreateCourseWithAuthorInativeException;
import com.plataform.courses.services.exceptions.NotPermissionImmutableData;
import com.plataform.courses.services.exceptions.ObjectNotFoundException;
import com.plataform.courses.model.entity.User;

import jakarta.validation.Valid;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    private static String UNTANTED_CONTENT = "Conteudo indesejado detectado";

    private static String NOT_PERMISSION_DELETE = "Você não tem permissão para deletar este curso";

    private static String NOT_PERMISSION_UPDATE = "Você não tem permissão para alterar este curso";

    private static String AUTHOR_INATIVE_COURSE = "Você não pode criar um curso para um usuário inativo";   

    
    private static String INATIVE_COURSE = "Você não pode atualizar as informações de um curso inativo";  

    private static final Integer MAX_IMMUTABLE_RECORDS = 3;

    private ContentFilterService filterService = new ContentFilterService();

    public String generateCourseNotFoundMessage(Long id){
        return "Curso não encontrado! Id: " + id + ", Tipo: " + Course.class.getName();
    }

    public String generateUserNotFoundMessage(Long id){
        return "Usuário não encontrado! Id: " + id + ", Tipo: " + User.class.getName();
    }

    public Course findById(Long id){
        Optional<Course> course = this.courseRepository.findById(id);
        return course.orElseThrow(()-> new ObjectNotFoundException(
            generateCourseNotFoundMessage(id)
        ));
    }

    public User findByIdUser(Long id){
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(()-> new ObjectNotFoundException(
            generateUserNotFoundMessage(id)
        ));
    }

    public void checkBadWord(List<String> fields){
        if (filterService.containsBadWord(fields)) {
            throw new BadWordException(UNTANTED_CONTENT);
        }
    }

    public void checkAuthorInative(User user){
        if (user.getActive().equals(false)){
            throw new CreateCourseWithAuthorInativeException(AUTHOR_INATIVE_COURSE);
        }
    }

    public Course checkIfIsImmutable(Course course, String str){
        if (course.getImmutable().equals(true)){
            throw new NotPermissionImmutableData(str);
        }
        return course;
    }

    public void checkCourseInative(Course course){
        if (course.getActive().equals(false)){
            throw new CourseInactiveUpdateException(INATIVE_COURSE);
        }
    }

    public Course generateSets(Course obj){
        obj.setActive(obj.getActive());
        obj.setDescription(obj.getDescription());
        obj.setName(obj.getName());
        obj.setPrice(obj.getPrice());
        obj.setImmutable(false);
        obj.setAuthor(obj.getAuthor());
        return obj;
    }

    public Course makeCourseInactive(Course course){
        course.setActive(false);
        course.setDeleted_at(LocalDateTime.now());
        return course;
    }

    public Course makeCourseActive(Course course){
        course.setActive(true);
        course.setDeleted_at(null);
        return course;
    }

    public Course countImmutableRecords(Course obj){
        Long immutableCount = courseRepository.countByImmutableTrue();
        if (immutableCount >= MAX_IMMUTABLE_RECORDS){
            obj.setImmutable(false);
        } else {
            obj.setImmutable(true);
        }
        return obj;
    }

    public Course generateSetsCreateDTO(CourseCreateDTO obj){
        Course course = new Course();
        course.setName(obj.getName());
        course.setCategory(obj.getCategory());
        course.setDescription(obj.getDescription());
        course.setAuthor(obj.getAuthor());
        course.setPrice(obj.getPrice());
        return course;
    }

    public Course generateSetsUpdateDTO(CourseUpdateDTO obj){
        Course course = new Course();
        course.setId(obj.getId());
        course.setName(obj.getName());
        course.setCategory(obj.getCategory());
        course.setDescription(obj.getDescription());
        course.setPrice(obj.getPrice());
        return course;
    }

    public Course setIdNull(Course course){
        course.setId(null);
        return course;
    }

    public Course mantainAuthor(Course newObj, Course obj){
        obj.setAuthor(newObj.getAuthor());
        return obj;
    }

    @Transactional
    public Course create(Course obj){
        List<String> fieldsToCheck = Arrays.asList(obj.getName(), obj.getCategory(), obj.getDescription());
        checkBadWord(fieldsToCheck);
        User user = findByIdUser(obj.getAuthor().getId());
        checkAuthorInative(user);
        Course newObj = countImmutableRecords(obj);
        newObj = setIdNull(newObj);
        return this.courseRepository.save(newObj);
  
    }

    @Transactional
    public Course update(Course obj){
        List<String> fieldsToCheck = Arrays.asList(obj.getName(), obj.getCategory(), obj.getDescription());
        checkBadWord(fieldsToCheck);
        Course newObj = findById(obj.getId());
        checkIfIsImmutable(newObj, NOT_PERMISSION_UPDATE);
        checkCourseInative(newObj);
        obj = mantainAuthor(newObj, obj);
        newObj = generateSets(obj);
        return this.courseRepository.save(newObj);
    }

    public void soft_delete(Long id){
        Course obj = findById(id);
        obj = checkIfIsImmutable(obj, NOT_PERMISSION_DELETE);
        obj = makeCourseInactive(obj);
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
        course = makeCourseActive(course);
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
        Course course = generateSetsCreateDTO(obj);
        return course;
    }

    public Course fromDTO(@Valid CourseUpdateDTO obj){
        Course course = generateSetsUpdateDTO(obj);
        return course;
    }
}
