package com.dairy.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    // Root → index.html (files are inside static/html/ folder)
    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> home() {
        return serve("static/html/index.html");
    }

    @GetMapping(value = "/login.html", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> loginHtml() {
        return serve("static/html/login.html");
    }

    @GetMapping(value = "/register.html", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> registerHtml() {
        return serve("static/html/register.html");
    }

    // user_dashboard.html — note underscore matches actual filename
    @GetMapping(value = "/user-dashboard.html", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> userDashboardHtml() {
        return serve("static/html/user_dashboard.html");
    }

    @GetMapping(value = "/admin-dashboard.html", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> adminDashboardHtml() {
        return serve("static/html/admin-dashboard.html");
    }

    // Clean URLs without .html extension
    @GetMapping(value = "/login", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> login() {
        return serve("static/html/login.html");
    }

    @GetMapping(value = "/register", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> register() {
        return serve("static/html/register.html");
    }

    @GetMapping(value = "/user-dashboard", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> userDashboard() {
        return serve("static/html/user_dashboard.html");
    }

    @GetMapping(value = "/admin-dashboard", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> adminDashboard() {
        return serve("static/html/admin-dashboard.html");
    }

    private ResponseEntity<Resource> serve(String path) {
        Resource resource = new ClassPathResource(path);
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(resource);
    }
}
