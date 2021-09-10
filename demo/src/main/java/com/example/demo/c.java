package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class c {

    @RequestMapping("/")
    public String question(HttpServletRequest request) {
        request.getSession().setAttribute("user","damjan");
        return "redirect:/exam/question";
    }
}