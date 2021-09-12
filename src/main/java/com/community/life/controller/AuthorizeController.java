package com.community.life.controller;


import com.community.life.bean.User;
import com.community.life.dto.AccessTokenDto;
import com.community.life.dto.GiteeUser;
import com.community.life.mapper.UserMapper;
import com.community.life.provider.GiteeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GiteeProvider giteeProvider;

    @Autowired
    private UserMapper userMapper;

    @Value("${gitee.client.id}") //将配置文件中的属性值注入
    private String clientId;

    @Value("${gitee.client.secret}")
    private String clientSecret;

    @Value("${gitee.redirect.url}")
    private String redirectUrl;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           HttpServletResponse response) throws IOException {
        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setCode(code);
        accessTokenDto.setClient_id(clientId);
        accessTokenDto.setClient_secret(clientSecret);
        accessTokenDto.setRedirect_uri(redirectUrl);
        String accessToken = giteeProvider.getAccessToken(accessTokenDto);
        GiteeUser giteeUser = giteeProvider.getUser(accessToken);
        if (giteeUser != null && giteeUser.getName() != null) {
            //登陆成功，写入cookie和session
            System.out.println("success");
            System.out.println(giteeUser.getName());
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setName(giteeUser.getName());
            user.setAccountId(String.valueOf(giteeUser.getId()));
            user.setToken(token);
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(System.currentTimeMillis());
            user.setAvatarUrl(giteeUser.getAvatar_url());
            userMapper.insert(user); //此时，用数据库的实际存储代替了session会话
            //以token为信号绑定后端与前端登陆的状态
            response.addCookie(new Cookie("token", token));  //向浏览器中写入cookie

            //request.getSession().setAttribute("user", giteeUser);
        }
        return "redirect:/"; //重定向到index页面（首页）
    }
}
