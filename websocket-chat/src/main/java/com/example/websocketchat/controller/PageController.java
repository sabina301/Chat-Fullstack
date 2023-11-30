package com.example.websocketchat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;


//It doesn't work :(
@Controller
@CrossOrigin("*")
public class PageController {
    @GetMapping("/a")
    public String showIndexPage() {
        return "index";
    }
}
