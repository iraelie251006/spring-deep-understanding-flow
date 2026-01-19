package com.example.demo;

import com.example.demo.model.Instructor;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.InstructorRepository;
import com.example.demo.request.CreateCourseRequest;
import com.example.demo.service.CourseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // 1. Initializes the Mocks
class CourseServiceTest {

    // 2. Create the Fakes
    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private CourseRepository courseRepository; // (Might not be needed if cascading, but good to have)

    // 3. Create the Real Service with Fakes inside
    @InjectMocks
    private CourseService courseService;

    @Test
    void shouldCreateCourseSuccessfully() {
        // --- GIVEN (Prepare the data) ---
        Long instructorId = 1L;
        CreateCourseRequest request = new CreateCourseRequest();
        request.setTitle("Advanced Java");
        request.setPrice(BigDecimal.TEN);

        Instructor mockInstructor = new Instructor();
        mockInstructor.setId(instructorId);

        // TEACH THE MOCK: "When someone asks for ID 1, return this instructor"
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(mockInstructor));

        // --- WHEN (Execute the logic) ---
        // CALL THE METHOD YOU WROTE
        courseService.createCourse(instructorId, request);

        // --- THEN (Verify the result) ---

        // YOUR TASK:
        // 1. Verify that 'instructorRepository.save(mockInstructor)' was called exactly once.
        // Hint: use verify(mock, times(1))...
        verify(instructorRepository, times(1)).save(mockInstructor);

        // 2. (Bonus) Verify that the mockInstructor now has 1 course in its list.
        // Hint: assertions like assertEquals(...)
        assertNotNull(mockInstructor);
        assertEquals(1, mockInstructor.getCourses().size());
        assertEquals(request.getTitle(), mockInstructor.getCourses().getFirst().getTitle());
    }
}