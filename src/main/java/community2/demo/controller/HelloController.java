package community2.demo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HelloController {

    @GetMapping("/")
    public String login(){
        return "index";
    }

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
