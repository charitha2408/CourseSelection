package com.klef.jfsd.springboot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.klef.jfsd.springboot.model.Courses;

import java.util.List;

@Repository
public interface CoursesRepo extends JpaRepository<Courses, Long> {

    List<Courses> findCoursesByDepartment(String department);
    List<Courses> findCoursesByFaculty(String faculty);
    List<Courses> findByYearAndSemester(String year, String semester);

}
