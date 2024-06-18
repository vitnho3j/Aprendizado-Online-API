package com.plataform.courses.model.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = User.TABLE_NAME)
public class User {
    public static final String TABLE_NAME = "user";
    public interface CreateUser {}
    public interface UpdateUser {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    @Size(min = 2, max = 100, groups = {CreateUser.class, UpdateUser.class})
    @NotBlank(groups = {CreateUser.class, UpdateUser.class})
    private String name;

    @Column(name = "cpf", length = 11, nullable = false)
    @CPF
    @Size(min = 11, max = 11)
    @NotBlank(groups = {CreateUser.class, UpdateUser.class})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String cpf;

    @Column(name = "email", length = 256, nullable = false)
    @Email
    private String email;

    @JsonIgnore
    @OneToMany(mappedBy = "author", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Course> courses = new ArrayList<Course>();

    @JsonIgnore
    @OneToMany(mappedBy = "buyer", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private List<Purchase> purchases = new ArrayList<Purchase>();

    @JsonIgnore
    @OneToMany(mappedBy = "seller", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Sale> sales = new ArrayList<Sale>();
}
