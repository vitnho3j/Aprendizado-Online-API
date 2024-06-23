package com.plataform.courses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.plataform.courses.model.entity.Purchase;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    Long countByImmutableTrue();

    @Modifying
    @Query("DELETE FROM Purchase p WHERE p.immutable = false")
    void deleteByImmutableFalse();
}
