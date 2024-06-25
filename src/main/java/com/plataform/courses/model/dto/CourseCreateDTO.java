package com.plataform.courses.model.dto;

import com.plataform.courses.model.entity.User;

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
public class CourseCreateDTO {

    @NotBlank()
    @Size(min = 2, max = 100)
    private String category;

    @NotBlank()
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank()
    @Size(min = 10, max = 255)
    private String description;
    
    @NotNull()
    private User author;

    @Positive
    @NotNull()
    private Float price;

    
}
