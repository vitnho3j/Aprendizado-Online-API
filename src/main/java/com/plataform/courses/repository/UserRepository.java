package com.plataform.courses.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.plataform.courses.model.entity.Course;
import com.plataform.courses.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    Long countByImmutableTrue();

    List<Course> findByCourses_Id(Long id);

    List<User> findByActiveTrue();

    List<User> findByActiveFalse();

    @Modifying
    @Query("DELETE FROM User s WHERE s.immutable = false")
    void deleteByImmutableFalse();

}
