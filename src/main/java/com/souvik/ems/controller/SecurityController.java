package com.souvik.ems.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SecurityController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("error", "Forbidden");
        return "redirect:" + request.getHeader("Referer");
    }

    @RequestMapping("/login")
    public String handleLogin() {
        return "login";
    }

}
