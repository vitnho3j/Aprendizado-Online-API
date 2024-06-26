package com.plataform.courses.model.projections;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.plataform.courses.model.entity.User;

public interface SaleGetByIdCourseProjection {
    
    public Long getId();

    public LocalDateTime getTimestamp();

    public Float getValue();

    @JsonIncludeProperties("id")
    public User getSeller();

}
