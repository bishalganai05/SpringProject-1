package com.spring.main.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.spring.main.service.ExcelService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class EmployeeController {

    @Autowired
    private ExcelService excelService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/register-employee")
    public String registerEmployee() {
        return "register_employee";
    }

    @PostMapping("/save-employee")
    public String saveEmployee(@RequestParam String name,
                               @RequestParam String address,
                               @RequestParam String phone) throws IOException {
        excelService.saveEmployee(name, address, phone);
        return "redirect:/";
    }

    @GetMapping("/download-csv")
    public StreamingResponseBody downloadCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=employees.csv");
        return excelService.downloadAsCsv();
    }
}