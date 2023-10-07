package com.practise.demopractise.Controller;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")

public class reg {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/")
    public String homePage() {
        return "homepage";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/index")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/success")
    public String successPage() {
        return "success";
    }

    @GetMapping("/edit")
    public String editPage() {
        return "edit";
    }

    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "dashboard";
    }

    @PostMapping("/registrationSystem")
    public String registrationPage(
            @RequestParam String username,
            @RequestParam String password) {

        String sql = "INSERT INTO tuser (username,password) VALUES (?, ?)";
        jdbcTemplate.update(
                sql, username, password);
        return "redirect:/login";
    }

    @PostMapping("/loginSystem")
    public String loginPage(
            @RequestParam String username,
            @RequestParam String password,
            Model model) {
        {
            // Define a RowMapper to map rows to a User object
            RowMapper<User> userRowMapper = (rs, rowNum) -> {
                User user = new User();
                user.setName(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                return user;
            };
            // Query the database to check if the username and password match
            String sql = "SELECT * FROM tuser WHERE username = ? AND password = ?";
            User user = jdbcTemplate.queryForObject(sql, userRowMapper, username, password);

            if (user != null) {
                return "redirect:/dashboard";
            } else {
                model.addAttribute("error", "Invalid username or password");
                return "error";
            }
        }
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String studentName,
            @RequestParam String studentID,
            @RequestParam String studentPhone,
            @RequestParam String studentClass) {

        int studentIDValue = Integer.parseInt(studentID);
        int studentClassValue = Integer.parseInt(studentClass);

        String sql = "INSERT INTO student (STUDENT_NAME,STUDENT_ROLL,STUDENT_PHONE,STUDENT_CLASS) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(
                sql, studentName, studentIDValue, studentPhone, studentClassValue);
        return "redirect:/show";
    }

    @GetMapping("/show")
    public String showPage(Model model) {
        String sql = "SELECT * FROM student";
        List<Map<String, Object>> studentshow = jdbcTemplate.queryForList(sql);
        model.addAttribute("studentshow", studentshow);
        // model.addAttribute("totalCredit", formattedTotalCredit);
        return "show";
    }

    @GetMapping("/studentdelete")
    public String studentdelete(
        @RequestParam int studentID) {
        String sql = "DELETE FROM student WHERE id = ?";
        jdbcTemplate.update(sql, studentID);
    
        return "redirect:/show"; // Redirect to the show page after deletion
    }
    

}
