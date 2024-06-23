package com.plataform.courses.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.plataform.courses.model.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>{

    List<Course> findByAuthor_Id(Long id);

    Long countByImmutableTrue();

    @Modifying
    @Query("DELETE FROM Course c WHERE c.immutable = false")
    void deleteByImmutableFalse();

}
