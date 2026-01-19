package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Course;
import com.example.demo.model.Instructor;
import com.example.demo.model.Lesson;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.InstructorRepository;
import com.example.demo.request.CreateCourseRequest;
import com.example.demo.request.LessonRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;

    @Transactional // 1. Keeps the Hibernate Session open
    public void createCourse(Long instructorId, CreateCourseRequest request) {

        // 2. Load the Parent (Instructor)
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));

        // 3. Create the Child (Course)
        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setPrice(request.getPrice());

        // 4. Create Grandchildren (Lessons) if provided
        if (request.getLessons() != null) {
            for (Lesson lr : request.getLessons()) {
                Lesson lesson = new Lesson();
                lesson.setTitle(lr.getTitle());
                lesson.setVideoUrl(lr.getVideoUrl());

                // Helper method syncs relationship!
                course.addLesson(lesson);
            }
        }

        // 5. Link Parent and Child (CRITICAL STEP)
        // This uses your helper method to sync both sides
        instructor.addCourses(course);

        // 6. Save
        // Because of CascadeType.ALL on Instructor, simply saving the instructor
        // will cascade down and save the Course AND the Lessons.
        instructorRepository.save(instructor);
    }
}
