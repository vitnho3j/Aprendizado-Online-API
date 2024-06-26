package com.plataform.courses.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.plataform.courses.model.entity.Sale;
import com.plataform.courses.model.projections.SaleGetByIdCourseProjection;
import com.plataform.courses.model.projections.SaleGetByIdUserProjection;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long>{

    List<SaleGetByIdUserProjection> findBySeller_Id(Long id);

    List<SaleGetByIdCourseProjection> findByCourse_Id(Long id);

    Long countByImmutableTrue();

    Optional<Sale> findBySellerIdAndCourseId(Long sellerId, Long courseId);

    @Modifying
    @Query("DELETE FROM Sale s WHERE s.immutable = false")
    void deleteByImmutableFalse();
}
