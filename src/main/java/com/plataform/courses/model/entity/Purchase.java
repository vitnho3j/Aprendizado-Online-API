package com.plataform.courses.model.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Purchase.TABLE_NAME)
public class Purchase {
    public static final String TABLE_NAME = "purchases";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "timestamp", updatable = false, nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timestamp;

    @Positive
    @Column(name = "value", updatable = false, nullable = false)
    private Float value;

    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @ManyToOne
    @NotNull()
    @JsonIncludeProperties({"id"})
    private User buyer;

    @JoinColumn(name = "course_id", nullable = false, updatable = false)
    @ManyToOne
    @NotNull()
    @JsonIncludeProperties("id")
    private Course course;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "immutable", nullable = false)
    private Boolean immutable = false;

}
