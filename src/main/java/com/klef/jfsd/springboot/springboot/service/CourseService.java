package com.klef.jfsd.springboot.springboot.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.klef.jfsd.springboot.model.Courses;

@Service
public interface CourseService {
    List<Courses> getAllCourses();
    Courses saveCourse(Courses course);
    void deleteCourse(Long id);
    Courses getCourseById(Long id);
}