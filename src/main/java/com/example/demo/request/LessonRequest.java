package com.example.demo.request;

import com.example.demo.model.Course;
import lombok.Data;

@Data
public class LessonRequest {
    private Long id;
    private String title;
    private String videoUrl;
    private Course course;
}
