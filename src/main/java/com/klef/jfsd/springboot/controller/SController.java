 package com.klef.jfsd.springboot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.klef.jfsd.springboot.model.Courses;
import com.klef.jfsd.springboot.model.Register;
import com.klef.jfsd.springboot.springboot.service.CourseService;
import com.klef.jfsd.springboot.springboot.service.StudentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class SController {

    @Autowired
    private StudentService service;
    @Autowired
    private CourseService courseService;

    private HttpSession session;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String LoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String RegisterPage() {
        return "register";
    }
    @GetMapping("/about")
    public String AboutPage() {
        return "about";
    }
    @GetMapping("/contactus")
    public String ContactPage() {
        return "contactus";
    }
    @GetMapping("/success")
    public String showSuccessPage() {
        return "success";
    }
    @GetMapping("/feedback")
    public String feedbackPage() {
        return "feedback";
    }
    @GetMapping("/hand")
    public String handPage() {
        return "hand";
    }
    @GetMapping("/handout/{code}")
    public String getHandout(@PathVariable String code, Model model) {
        // Mock data for handouts
    	Map<String, String[]> handouts = new HashMap<>();
    	handouts.put("22CSB2103", new String[]{"Python Full Stack Development", "4 Credits", "Prof. John Doe"});
    	handouts.put("22CS2256", new String[]{"Engineering Mathematics", "3 Credits", "Prof. Jane Smith"});
    	handouts.put("22CS276A", new String[]{"Java Full Stack Development", "4 Credits", "Prof. Richard Roe"});
    	handouts.put("22CS341A", new String[]{"Machine Learning", "3 Credits", "Prof. Alice Johnson"});
    	handouts.put("22CS2871R", new String[]{"Database Management System", "3 Credits", "Prof. Emily Davis"});
    	handouts.put("22CS2871R_ATFL", new String[]{"ATFL", "3 Credits", "Prof. Grace Hopper"});



        if (handouts.containsKey(code)) {
            model.addAttribute("course", handouts.get(code)[0]);
            model.addAttribute("credits", handouts.get(code)[1]);
            model.addAttribute("coordinator", handouts.get(code)[2]);
        } else {
            model.addAttribute("error", "Course handout not found.");
        }

        return "handout-details";
    }

    @GetMapping("/generate-timetable")
    public String showTimetablePage() {
        return "generateTimetable"; 
    }

    @GetMapping("/studentdashboard")
    public String StudentDashboard(Model model, HttpServletRequest request) {
        String name = (String) request.getSession().getAttribute("name");
        model.addAttribute("name", name);
        return "studentdashboard"; 
    }

    @GetMapping("/academic-registration")
    public String showAcademicRegistrationPage() {
        return "academicregistrationpage";
    }
    @PostMapping("/submit-feedback")
    public String submitFeedback(@RequestParam("course") String course, 
                                 @RequestParam("feedback") String feedback, 
                                 Model model) {
        model.addAttribute("course", course);
        model.addAttribute("feedback", feedback);

        return "submit-feedback"; 
    }
    
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession();
        session.invalidate();
        redirectAttributes.addFlashAttribute("message", "User logged out successfully!");
        
        return "redirect:/login"; 
    }

    @GetMapping("/viewCourses")
    public String viewCourses(Model model) {
        List<Courses> courses = courseService.getAllCourses();
        System.out.println("Courses found: " + courses.size()); 
        model.addAttribute("courses", courses);
        return "viewcourses"; 
    }

    @PostMapping("/userdetails")
    public String registerDetails(@ModelAttribute Register register, 
                                @RequestParam("confirmpassword") String confirmPassword,
                                Model model) {
        if (!register.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }

        Register registeredUser = service.registrationPageDetails(register);
        System.out.println("User registered successfully with ID: " + registeredUser.getId());
        return "login";
    }
    
    
    @GetMapping("/searchCourses")
    public String searchCourses(@RequestParam("year") String year, 
                                 @RequestParam("semester") String semester, 
                                 Model model) {
        List<Courses> courses = null;
        try {
            courses = service.getCoursesByYearAndSemester(year, semester);
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while fetching the courses. Please try again.");
            e.printStackTrace();  
        }

        if (courses != null && !courses.isEmpty()) {
            model.addAttribute("courses", courses);
        } else {
            model.addAttribute("message", "No courses found for the selected year and semester.");
        }

        return "courses";
    }
    @GetMapping("/profile")
    public String showProfile(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        
        String name = (String) session.getAttribute("name");
        Long id = (Long) session.getAttribute("id");
        String email = (String) session.getAttribute("email");
        String department = (String) session.getAttribute("department");
        String type = (String) session.getAttribute("type");
        String password = (String) session.getAttribute("password");
        model.addAttribute("name", name);
        model.addAttribute("id", id);
        model.addAttribute("email", email);
        model.addAttribute("department", department);
        model.addAttribute("type", type);
        model.addAttribute("password", password);
        
        return "profile"; 
    }

    


    @PostMapping("/login")
    public String LoginDetails(@RequestParam String id,
                             @RequestParam String password,
                             Model model,
                             HttpServletRequest request) {
        Register register = service.loginPage(id, password);
        if (register == null) {
            model.addAttribute("message", "Invalid details");
            return "login";
        } else {
            session = request.getSession();
            session.setAttribute("name", register.getName());
            session.setAttribute("id", register.getId());
            session.setAttribute("password", register.getPassword());
            session.setAttribute("email", register.getEmailid());
            session.setAttribute("type", register.getType());
            session.setAttribute("department", register.getDepartment()); 
            session.setAttribute("funame", register.getId());

            model.addAttribute("name", register.getName());
            model.addAttribute("id", register.getId());

            if ("faculty".equalsIgnoreCase(register.getType())) {
                return "redirect:/facultydashboard";
            } else if ("student".equalsIgnoreCase(register.getType())) {
                return "redirect:/studentdashboard";
            } else {
                return "Dashboard";
            }
        }
    }
}