package com.plataform.courses.model.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Course.TABLE_NAME)
public class Course {
    public static final String TABLE_NAME = "course";
    public interface CreateCourse {}
    public interface UpdateCourse {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "category", length = 100, nullable = false)
    @NotBlank(groups = {CreateCourse.class, UpdateCourse.class})
    @Size(min = 2, max = 100, groups = {CreateCourse.class, UpdateCourse.class})
    private String category;

    @Column(name = "name", length = 100, nullable = false)
    @NotBlank(groups = {CreateCourse.class, UpdateCourse.class})
    @Size(min = 2, max = 100, groups = {CreateCourse.class, UpdateCourse.class})
    private String name;

    @Positive
    @Column(name = "price", nullable = false)
    @NotNull(groups = {CreateCourse.class, UpdateCourse.class})
    private Float price;

    @Column(name = "description", length = 255, nullable = false)
    @NotBlank(groups = {CreateCourse.class, UpdateCourse.class})
    @Size(min = 10, max = 255, groups = {CreateCourse.class, UpdateCourse.class})
    private String description;

    @NotNull(groups = CreateCourse.class)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @ManyToOne
    private User author;  

    @Column(name = "available", nullable = false)
    @NotNull
    private Boolean available = true;

    @JsonIgnore
    @OneToMany(mappedBy = "course", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Purchase> purchases = new ArrayList<Purchase>();

    @JsonIgnore
    @OneToMany(mappedBy = "course", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Sale> sales = new ArrayList<Sale>();
    
}

 