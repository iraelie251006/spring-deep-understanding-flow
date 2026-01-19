package com.example.demo.request;

import com.example.demo.model.Lesson;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class CreateCourseRequest {
    private Long id;
    private String title;
    private BigDecimal price;
    private List<Lesson> lessons = new ArrayList<>();
}
