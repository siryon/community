package community2.demo.controller;


import community2.demo.mapper.UserMapper;
import community2.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class HelloController {

    @Autowired(required = false)
    private UserMapper userMapper;
    @GetMapping("/")
    public String login(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            if (cookie.getName().equals("token")){
                String token= cookie.getValue();
                User user = userMapper.findByToken(token);
                if (user != null){
                    request.getSession().setAttribute("user",user);
                }
                break;
            }

        }

        return "index";
    }

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
