package com.nan.myschool.repository;

import com.nan.myschool.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.beans.JavaBean;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
