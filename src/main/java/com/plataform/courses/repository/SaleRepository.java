package com.plataform.courses.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.plataform.courses.model.entity.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long>{
    Long countByImmutableTrue();

    Optional<Sale> findBySellerIdAndCourseId(Long sellerId, Long courseId);

    @Modifying
    @Query("DELETE FROM Sale s WHERE s.immutable = false")
    void deleteByImmutableFalse();
}
