package community2.demo.controller;

import community2.demo.mapper.QuestionMapper;
import community2.demo.mapper.UserMapper;
import community2.demo.model.Question;
import community2.demo.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.HttpConstraintElement;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import lombok.Data;

@Controller
public class PublishController {

    @Autowired(required = false)
    private QuestionMapper questionMapper;
    @Autowired(required = false)
    private UserMapper userMapper;

    @GetMapping("/publish")
    public String publish(){
        return "publish";

    }

    @PostMapping("/publish")
    public String topublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            HttpServletRequest request,
            //使用该方式向前端传送页面
            Model model
            ){

        //将传输过来的页面，送到html中， 使得写入的部分可以回显
        model.addAttribute("title", title);
        model.addAttribute("description",description );
        model.addAttribute("tag", tag);
        //对三个内容做简单的校验
        if(title == null || title=="") {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (description==null || description==""){
            model.addAttribute("error", "内容不能为空");
            return "publish";
        }
        if(tag == null || tag ==""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }

        Cookie[] cookies = request.getCookies();
        User user = null;
        for(Cookie cookie: cookies){
            if(cookie.getName().equals("token")){
                String token = cookie.getValue();
                user = userMapper.findByToken(token);
                if (user != null){
                    request.getSession().setAttribute("user",user);
                }
                else
                    break;
            }
        }
        //使用model，向前端传回页面
        if (user == null){
            model.addAttribute("error","please login in");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmt_create(System.currentTimeMillis());
        question.setGmt_modified(question.getGmt_create());

        questionMapper.create(question);
        return "redirect:/";
    }
}
