package com.example.demo.repository;

import com.example.demo.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    // 💡 HINT:
    // You are selecting 's' (Student).
    // You need to JOIN 's.courses' to get access to the Course fields.
    // Alias the joined course as 'c'.

    @Query("SELECT s FROM Student s JOIN s.courses c WHERE c.title = :title")
    List<Student> findStudentsByCourseTitle(@Param("title") String title);
}
