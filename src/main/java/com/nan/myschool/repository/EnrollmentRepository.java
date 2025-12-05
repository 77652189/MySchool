package com.nan.myschool.repository;

import com.nan.myschool.entity.CourseSection;
import com.nan.myschool.entity.Enrollment;
import com.nan.myschool.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    List<Enrollment> findByPet(Pet pet);
    List<Enrollment> findByCourseSection(CourseSection courseSection);
}