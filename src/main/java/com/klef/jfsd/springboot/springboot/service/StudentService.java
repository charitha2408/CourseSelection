package com.klef.jfsd.springboot.springboot.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.klef.jfsd.springboot.model.Courses;
import com.klef.jfsd.springboot.model.Register;

@Service
public interface StudentService {
    Register registrationPageDetails(Register register);
    
    Register loginPage(String id, String password);
    
    List<Courses> getAllCoursesData();
    
    void saveCourse(Courses course);
    
    void deleteCourse(Long courseId);
    
    List<Courses> getCoursesByYearAndSemester(String year, String semester);

}
