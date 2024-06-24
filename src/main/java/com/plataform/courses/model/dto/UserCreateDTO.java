package com.plataform.courses.model.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserCreateDTO {

    @Size(min = 2, max = 100)
    @NotBlank()
    private String name;

    @Email
    @NotBlank()
    private String email;
    
}
