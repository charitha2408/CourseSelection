package com.klef.jfsd.springboot.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klef.jfsd.springboot.model.Courses;
import com.klef.jfsd.springboot.repo.CoursesRepo;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CoursesRepo coursesRepo;
    
    @Override
    public List<Courses> getAllCourses() {
        return coursesRepo.findAll();
    }
    
    @Override
    public Courses saveCourse(Courses course) {
        return coursesRepo.save(course);
    }
    
    @Override
    public void deleteCourse(Long id) {
        coursesRepo.deleteById(id);
    }
    
    @Override
    public Courses getCourseById(Long id) {
        return coursesRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }
}
