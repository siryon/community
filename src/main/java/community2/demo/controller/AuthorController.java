package community2.demo.controller;


import community2.demo.dto.AccessTakenDto;
import community2.demo.dto.GithubUser;
import community2.demo.mapper.UserMapper;
import community2.demo.model.User;
import community2.demo.provier.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
@Controller
public class AuthorController {
    @Autowired
    private GithubProvider githubProvider;

    @Autowired(required = false)
    private UserMapper userMapper;
    
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletResponse response) throws Exception {
        //将数据传输到github中，获取github的值
        AccessTakenDto assesstakendto = new AccessTakenDto();
        assesstakendto.setCode(code);
        assesstakendto.setClient_id(clientId);
        assesstakendto.setClient_secret(clientSecret);
        assesstakendto.setState(state);
        assesstakendto.setRedirect_uri(redirectUri);

        //获取从github上返回的值
        String accesstoken = githubProvider.getaccesstaken(assesstakendto);
        GithubUser githubuser = githubProvider.getUser(accesstoken);
        if (githubuser != null && githubuser.getId() != 0){
            //登陆成功
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setAccount_id(String.valueOf(githubuser.getId()));
            user.setName(githubuser.getName());
            user.setGmt_create(System.currentTimeMillis());
            user.setGmt_modified(user.getGmt_modified());
            user.setBio(githubuser.getBio());
            user.setAvatar_url(githubuser.getAvatar_url());
            userMapper.insert(user);

            //登录成功，可以写入cookie和session
            //写入Cookie

            response.addCookie(new Cookie("token", token));
            return "redirect:/";
        }else{
            //登录失败，应该要给予反馈
            return "redirect:/";
        }
    }
}
