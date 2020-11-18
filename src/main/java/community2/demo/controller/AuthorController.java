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

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorController {
    @Autowired
    private GithubProvider githubProvider;

    @Autowired(required=false)
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
                           HttpServletRequest request) throws Exception {
        AccessTakenDto assesstakendto = new AccessTakenDto();
        assesstakendto.setCode(code);
        assesstakendto.setClient_id(clientId);
        assesstakendto.setClient_secret(clientSecret);
        assesstakendto.setState(state);
        assesstakendto.setRedirect_uri(redirectUri);

        String token = githubProvider.getaccesstaken(assesstakendto);
        GithubUser githubuser = githubProvider.getUser(token);
        if (githubuser != null){
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setAccount_id(String.valueOf(githubuser.getId()));
            user.setName(githubuser.getName());
            user.setGmt_create(System.currentTimeMillis());
            user.setGmt_modified(user.getGmt_modified());
            userMapper.insert(user);

            //登录成功，可以写入cookie和session
            request.getSession().setAttribute("user",githubuser);
            return "redirect:/";
        }else{
            //登录失败，应该要给予反馈
            return "redirect:/";
        }
    }
}
