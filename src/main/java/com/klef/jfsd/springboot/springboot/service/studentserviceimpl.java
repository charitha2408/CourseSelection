package com.klef.jfsd.springboot.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klef.jfsd.springboot.model.Courses;
import com.klef.jfsd.springboot.model.Register;
import com.klef.jfsd.springboot.repo.CoursesRepo;
import com.klef.jfsd.springboot.repo.StudentRepo;

import java.util.List;

@Service
public class studentserviceimpl implements StudentService {

    @Autowired
    private StudentRepo studentrepository;

    @Autowired
    private CoursesRepo coursesRepo;

    @Override
    public Register registrationPageDetails(Register register) {
        return studentrepository.save(register); 
    }

    @Override
    public Register loginPage(String id, String password) {
        return studentrepository.IdOrNameAndPassword(id, id, password); 
    }

    @Override
    public List<Courses> getAllCoursesData() {
        return coursesRepo.findAll(); 
    }
    @Override
    public void saveCourse(Courses course) {
        coursesRepo.save(course); 
    }

    @Override
    public void deleteCourse(Long courseId) {
        coursesRepo.deleteById(courseId);
    }

    @Override
    public List<Courses> getCoursesByYearAndSemester(String year, String semester) {
        return coursesRepo.findByYearAndSemester(year, semester); 
    }
    
    
}
