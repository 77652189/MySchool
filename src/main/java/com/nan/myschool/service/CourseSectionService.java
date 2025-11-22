package com.nan.myschool.service;

import com.nan.myschool.entity.CourseSection;
import com.nan.myschool.repository.CourseSectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseSectionService {
    private final CourseSectionRepository courseSectionRepository;

    public CourseSectionService(CourseSectionRepository courseSectionRepository) {
        this.courseSectionRepository = courseSectionRepository;
    }

    public List<CourseSection> getAllSections() {
        return courseSectionRepository.findAll();
    }

    public CourseSection getSectionById(Integer id) {
        return courseSectionRepository.findById(id).orElse(null);
    }

    public CourseSection saveSection(CourseSection section) {
        return courseSectionRepository.save(section);
    }

    public void deleteSection(Integer id) {
        courseSectionRepository.deleteById(id);
    }
}