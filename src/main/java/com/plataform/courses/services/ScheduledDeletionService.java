// package com.plataform.courses.services;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Service;

// import com.plataform.courses.repository.CourseRepository;
// import com.plataform.courses.repository.PurchaseRepository;
// import com.plataform.courses.repository.SaleRepository;
// import com.plataform.courses.repository.UserRepository;

// import jakarta.persistence.EntityManager;
// import jakarta.persistence.PersistenceContext;
// import jakarta.transaction.Transactional;

// @Service
// public class ScheduledDeletionService {

//     @PersistenceContext
//     private EntityManager entityManager;

//     @Autowired
//     private SaleRepository saleRepository;

//     @Autowired
//     private UserRepository userRepository;

//     @Autowired
//     private CourseRepository courseRepository;

//     @Autowired
//     private PurchaseRepository purchaseRepository;

//     @Transactional
//     @Scheduled(fixedRate = 30000) // 2 * 60 * 60 * 1000
//     public void deleteNonImmutableSales(){
//         entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
//         this.saleRepository.deleteByImmutableFalse();
//         entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
//     }

//     @Transactional
//     @Scheduled(fixedRate = 30000) // 2 * 60 * 60 * 1000
//     public void deleteNonImmutableUsers(){
//         entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
//         this.userRepository.deleteByImmutableFalse();
//         entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
//     }

//     @Transactional
//     @Scheduled(fixedRate = 30000) // 2 * 60 * 60 * 1000
//     public void deleteNonImmutableCourses(){
//         entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
//         this.courseRepository.deleteByImmutableFalse();
//         entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
//     }

//     @Transactional
//     @Scheduled(fixedRate = 30000) // 2 * 60 * 60 * 1000
//     public void deleteNonImmutablePurchases(){
//         entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
//         this.purchaseRepository.deleteByImmutableFalse();
//         entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
//     }
// }
