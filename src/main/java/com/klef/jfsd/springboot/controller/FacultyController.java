package com.klef.jfsd.springboot.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.klef.jfsd.springboot.model.Courses;
import com.klef.jfsd.springboot.repo.CoursesRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class FacultyController {

    @Autowired
    private CoursesRepo coursesRepo;

    private static final String UPLOAD_DIR = "uploads/";
    @GetMapping("/facultydashboard")
    public ModelAndView facultyDashboard(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        HttpSession session = request.getSession();

        String funame = session.getAttribute("funame") != null ? session.getAttribute("funame").toString() : null;
        String name = session.getAttribute("name") != null ? session.getAttribute("name").toString() : null;
        if (funame != null) {
            List<Courses> coursesList = coursesRepo.findCoursesByFaculty(funame);
            mv.setViewName("facultydashboard");
            mv.addObject("coursesList", coursesList);
            mv.addObject("name", name);
            mv.addObject("funame", funame);
        } else {
            mv.setViewName("login");
        }
        return mv;
    }

    @GetMapping("/addcourse")
    public ModelAndView showAddCoursePage() {
        return new ModelAndView("addcourse");
    }
    @GetMapping("/editcourse")
    public ModelAndView editCoursePage() {
        return new ModelAndView("editcourse");
    }
    @GetMapping("/deletecourse")
    public ModelAndView deleteCoursePage() {
        return new ModelAndView("deletecourse");
    }
    @GetMapping("/handouts")
    public ModelAndView handoutsPage() {
        return new ModelAndView("handouts");
    }
    @GetMapping("/profilef")
    public String showProfile(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
                String name = (String) session.getAttribute("name");
        Long id = (Long) session.getAttribute("id");
        String email = (String) session.getAttribute("email");
        String department = (String) session.getAttribute("department");
        String type = (String) session.getAttribute("type");
        String password = (String) session.getAttribute("password"); // Add this line
                model.addAttribute("name", name);
        model.addAttribute("id", id);
        model.addAttribute("email", email);
        model.addAttribute("department", department);
        model.addAttribute("type", type);
        model.addAttribute("password", password); // Add this line
        return "profilef";
    }

    @GetMapping("/viewcourses")
    public ModelAndView viewCourses(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        HttpSession session = request.getSession();

        String funame = session.getAttribute("funame") != null ? session.getAttribute("funame").toString() : null;
        if (funame != null) {
            List<Courses> coursesList = coursesRepo.findCoursesByFaculty(funame);
            mv.setViewName("viewcourses");
            mv.addObject("coursesList", coursesList);
        } else {
            mv.setViewName("login");
        }
        return mv;
    }

    @PostMapping("/insertcourse")
    public ModelAndView insertCourse(HttpServletRequest request, @ModelAttribute Courses course) {
        ModelAndView mv = new ModelAndView();
        HttpSession session = request.getSession();

        String funame = session.getAttribute("funame") != null ? session.getAttribute("funame").toString() : null;
        if (funame != null) {
            course.setFaculty(funame);
            coursesRepo.save(course);
            mv.setViewName("redirect:/facultydashboard");
        } else {
            mv.setViewName("login");
        }
        return mv;
    }
    @GetMapping("/deletecourse/{id}")
    public ModelAndView deleteCourse(@PathVariable Long id, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        HttpSession session = request.getSession();

        String funame = session.getAttribute("funame") != null ? session.getAttribute("funame").toString() : null;
        if (funame != null) {
            Courses course = coursesRepo.findById(id).orElse(null);
            if (course != null && course.getFaculty().equals(funame)) {
                coursesRepo.deleteById(id);
            }
            mv.setViewName("redirect:/facultydashboard");
        } else {
            mv.setViewName("login");
        }
        return mv;
    }

    @GetMapping("/uploadhandout")
    public ModelAndView showUploadHandoutPage() {
        return new ModelAndView("uploadhandout");
    }

    @PostMapping("/uploadhandout/{subject}")
    public ModelAndView uploadHandout(HttpServletRequest request, 
                                      @PathVariable String subject, 
                                      @RequestParam("file") MultipartFile file) {
        ModelAndView mv = new ModelAndView();
        HttpSession session = request.getSession();

        String funame = session.getAttribute("funame") != null ? session.getAttribute("funame").toString() : null;
        if (funame != null) {
            if (file.isEmpty()) {
                mv.setViewName("uploadhandout");
                mv.addObject("message", "No file uploaded for subject: " + subject);
                return mv;
            }

            try {
                File subjectDir = new File(UPLOAD_DIR + subject);
                if (!subjectDir.exists()) {
                    subjectDir.mkdirs();
                }

                String filePath = subjectDir.getAbsolutePath() + File.separator + file.getOriginalFilename();
                file.transferTo(new File(filePath));

                mv.setViewName("uploadhandout");
                mv.addObject("message", "File uploaded successfully for subject: " + subject);
            } catch (IOException e) {
                mv.setViewName("uploadhandout");
                mv.addObject("message", "Error uploading file for subject: " + subject);
            }
        } else {
            mv.setViewName("login");
        }
        return mv;

    }
}
