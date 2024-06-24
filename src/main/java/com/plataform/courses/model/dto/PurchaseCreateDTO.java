package com.plataform.courses.model.dto;

import com.plataform.courses.model.entity.Course;
import com.plataform.courses.model.entity.User;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PurchaseCreateDTO {

    @NotNull()
    private User buyer;

    @NotNull()
    private Course course;
}
