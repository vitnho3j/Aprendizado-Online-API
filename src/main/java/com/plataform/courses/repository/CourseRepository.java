package com.plataform.courses.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.plataform.courses.model.entity.Course;
import com.plataform.courses.model.projections.CourseProjection;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>{

    List<CourseProjection> findByAuthor_Id(Long id);

    Long countByImmutableTrue();

    List<Course> findByActiveTrue();

    List<Course> findByActiveFalse();

    @Modifying
    @Query("DELETE FROM Course c WHERE c.immutable = false")
    void deleteByImmutableFalse();

}
