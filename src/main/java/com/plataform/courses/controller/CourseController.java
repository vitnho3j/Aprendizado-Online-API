package com.plataform.courses.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.plataform.courses.model.entity.Course;
import com.plataform.courses.model.projections.CourseProjection;
import com.plataform.courses.services.CourseService;
import com.plataform.courses.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/courses")
@Validated
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<List<Course>> findAll(){
        List<Course> courses = this.courseService.findAll();
        return ResponseEntity.ok().body(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> findById(@PathVariable Long id){
        Course obj = this.courseService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody Course obj){
        this.courseService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody Course obj, @PathVariable Long id){
        obj.setId(id);
        this.courseService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        this.courseService.soft_delete(id);
    }

    @GetMapping("/user/{authorId}")
    public ResponseEntity<List<CourseProjection>> findAllByAuthorId(@PathVariable Long authorId){
        this.userService.findById(authorId);
        List<CourseProjection> courses = this.courseService.findAllByAuthorId(authorId);
        return ResponseEntity.ok().body(courses);
    }
}
