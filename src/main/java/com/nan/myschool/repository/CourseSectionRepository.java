package com.nan.myschool.repository;

import com.nan.myschool.entity.Course;
import com.nan.myschool.entity.CourseSection;
import com.nan.myschool.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseSectionRepository extends JpaRepository<CourseSection, Integer> {
    List<CourseSection> findByCourse(Course course);
    List<CourseSection> findByInstructor(Instructor instructor);

}
