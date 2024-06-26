package com.plataform.courses.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.plataform.courses.model.entity.Purchase;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findByBuyer_Id(Long id);

    List<Purchase> findByCourse_Id(Long id);

    Long countByImmutableTrue();

    Optional<Purchase> findByBuyerIdAndCourseId(Long buyerId, Long courseId);

    @Modifying
    @Query("DELETE FROM Purchase p WHERE p.immutable = false")
    void deleteByImmutableFalse();
}
