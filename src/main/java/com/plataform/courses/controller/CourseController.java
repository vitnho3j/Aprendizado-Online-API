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
import com.plataform.courses.model.entity.Course.CreateCourse;
import com.plataform.courses.services.CourseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/course")
@Validated
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping("/{id}")
    public ResponseEntity<Course> findById(@PathVariable Long id){
        Course obj = this.courseService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    @Validated(CreateCourse.class)
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
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.courseService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{authorId}")
    public ResponseEntity<List<Course>> findAllByAuthorId(@PathVariable Long authorId){
        List<Course> courses = this.courseService.findAllByAuthorId(authorId);
        return ResponseEntity.ok().body(courses);
    }
}