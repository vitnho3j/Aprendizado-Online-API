package com.plataform.courses.model.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = Sale.TABLE_NAME)
public class Sale {
    public static final String TABLE_NAME = "sale";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "timestamp", updatable = false)
    private LocalDateTime timestamp;

    @Positive
    @NotBlank
    @Column(name = "value", nullable = false, updatable = false)
    private Float value;

    @NotBlank
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @ManyToOne
    private User seller;

    @NotBlank
    @JoinColumn(name = "course_id", nullable = false, updatable = false)
    @ManyToOne
    private Course course;
}
