package community2.demo.controller;


import community2.demo.dto.AccessTakenDto;
import community2.demo.dto.GithubUser;
import community2.demo.provier.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class AuthorController {
    @Autowired
    private GithubProvider githubProvider;
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state) throws Exception {
        AccessTakenDto assesstakendto = new AccessTakenDto();
        assesstakendto.setCode(code);
        assesstakendto.setClient_id("4e6a5e29ce81350605df");
        assesstakendto.setClient_secret("7bb124d7f9111a50df864343cb66d28cffafc134");
        assesstakendto.setState(state);
        assesstakendto.setRedirect_uri("http://localhost:8090/callback");

        String token = githubProvider.getaccesstaken(assesstakendto);
        GithubUser user = githubProvider.getUser(token);
        System.out.println(user.getName());
        return "index";
    }
}
