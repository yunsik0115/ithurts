package com.sidepj.ithurts.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping("/signup")
    public String signupForm(){
        return "signupForm.html";
    }

    @PostMapping("/signup")
    public String signUp(){
        return "login.html"; //redirect to login page
    }

    @GetMapping("/signup/OAuth")
    public String signupFormOAuth(){
        return "/";
    }

    @PostMapping("/signup/OAuth")
    public String signupOAuth(){
        return "/";
    }

    // == TO DO == OAUTH API SPECIFICATION == //

    @GetMapping("/login")
    public String loginForm(){
        return "loginForm.html";
    }

    @PostMapping("/login")
    public String login(){
        return "/";
    }




}
