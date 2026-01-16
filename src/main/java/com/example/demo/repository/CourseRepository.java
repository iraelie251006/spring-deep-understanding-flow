package com.example.demo.repository;

import com.example.demo.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    // 💡 HINT:
    // You are selecting from 'Course c'.
    // You need to check if 'c.students' is empty.

    @Query("SELECT c FROM Course c WHERE c.students IS EMPTY")
    List<Course> findCoursesWithNoStudents();

}
