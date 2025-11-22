package com.nan.myschool.service;

import com.nan.myschool.entity.CourseSection;
import com.nan.myschool.entity.Enrollment;
import com.nan.myschool.entity.Student;
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

    /**
     * 获取所有选课记录
     */
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    /**
     * 根据ID获取选课记录
     */
    public Enrollment getEnrollmentById(Integer id) {
        Optional<Enrollment> enrollment = enrollmentRepository.findById(id);
        return enrollment.orElse(null);
    }

    /**
     * 根据学生获取选课记录列表
     */
    public List<Enrollment> getEnrollmentsByStudent(Student student) {
        return enrollmentRepository.findByStudent(student);
    }

    /**
     * 根据课程章节获取选课记录列表
     * 注意：方法名修正为 getEnrollmentsBySection
     */
    public List<Enrollment> getEnrollmentsBySection(CourseSection section) {
        return enrollmentRepository.findByCourseSection(section);
    }

    /**
     * 保存或更新选课记录
     */
    public Enrollment saveEnrollment(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    /**
     * 删除选课记录
     */
    public void deleteEnrollment(Integer id) {
        enrollmentRepository.deleteById(id);
    }

    /**
     * 检查学生是否已选某课程章节
     */
    public boolean isStudentEnrolled(Student student, CourseSection section) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);
        return enrollments.stream()
                .anyMatch(e -> e.getCourseSection().getSectionId().equals(section.getSectionId()));
    }

    /**
     * 获取课程章节的选课人数
     */
    public long getEnrollmentCount(CourseSection section) {
        return enrollmentRepository.findByCourseSection(section).size();
    }
}