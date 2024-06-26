package com.plataform.courses.model.projections;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.plataform.courses.model.entity.Course;

public interface PurchaseGetByIdUserProjection {
    
    public Long getId();

    public LocalDateTime getTimestamp();

    public Float getValue();

    @JsonIncludeProperties("id")
    public Course getCourse();

}
