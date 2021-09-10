package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/exam")
public class cont {

    @RequestMapping("question")
    public String question(@SessionAttribute(name = "user") Object user) {
//        String x= String.valueOf(session.getAttribute("user"));
        System.out.println(user);
        return "q";
    }

    @RequestMapping(value = {"/search"}, method = RequestMethod.GET)
    public String search(
            @RequestParam String query,
            Model model
    ) {
        System.out.println(query);
        return "q";
    }

}

