package com.plataform.courses.model.projections;

public interface CourseProjection {

    public Long getId();

    public String getDescription();

    public String getCategory();

    public String getName();

    public Float getPrice();

    public Boolean getActive();
}
