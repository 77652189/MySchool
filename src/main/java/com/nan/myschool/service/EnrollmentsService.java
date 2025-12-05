package com.nan.myschool.service;

import com.nan.myschool.entity.CourseSection;
import com.nan.myschool.entity.Enrollment;
import com.nan.myschool.entity.Pet;
import com.nan.myschool.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentsService {
    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentsService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    public Enrollment getEnrollmentById(Integer id) {
        Optional<Enrollment> enrollment = enrollmentRepository.findById(id);
        return enrollment.orElse(null);
    }

    public List<Enrollment> getEnrollmentsByPet(Pet pet) {
        return enrollmentRepository.findByPet(pet);
    }

    public List<Enrollment> getEnrollmentsBySection(CourseSection section) {
        return enrollmentRepository.findByCourseSection(section);
    }

    public Enrollment saveEnrollment(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    public void deleteEnrollment(Integer id) {
        enrollmentRepository.deleteById(id);
    }

    public boolean isPetEnrolled(Pet pet, CourseSection section) {
        List<Enrollment> enrollments = enrollmentRepository.findByPet(pet);
        return enrollments.stream()
                .anyMatch(e -> e.getCourseSection().getSectionId().equals(section.getSectionId()));
    }

    public long getEnrollmentCount(CourseSection section) {
        return enrollmentRepository.findByCourseSection(section).size();
    }
}