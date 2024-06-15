package com.plataform.courses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.plataform.courses.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{}
