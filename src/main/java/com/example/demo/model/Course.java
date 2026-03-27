package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructorId")
//    I have learnt the owning side is always the one that has ManyToOne
//    because It will be impossible to put all courseIDs on row of an instructor
//    without violating database normalization
    private Instructor instructor;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();

    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    private Set<Student> students = new HashSet<>();

    public void addLesson(Lesson lesson) {
//        Pro edition(Principal engineer approach) 👌

        this.lessons.add(lesson);
        lesson.setCourse(this);
//        second implementation after understanding it
//        this was what was happening under the hood.
//        if (lessons == null) {
//            lessons = new ArrayList<>();
//        }
//
//        lessons.add(lesson);
//
//        if (lesson.getCourse() == null) {
//            lesson.setCourse(this);
//        }


//        first implementation before understanding
//        if (lessons == null) lessons = new ArrayList<>();
//        lessons.add(lesson);
//        lesson.setCourse(this);
    }

    public void addStudent(Student student) {

        this.students.add(student);
        student.getCourses().add(this);

        // 1. Update this side
//        if (students == null) students = new HashSet<>();
//        students.add(student);

        // 2. Update the other side (The Owner) safely
//        if (student.getCourses() == null) {
//            student.setCourses(new HashSet<>());
//        }
//        student.getCourses().add(this);
    }
}
