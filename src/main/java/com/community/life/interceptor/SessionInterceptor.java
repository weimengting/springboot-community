package com.community.life.interceptor;

import com.community.life.bean.User;
import com.community.life.bean.UserExample;
import com.community.life.mapper.UserMapper;
import com.community.life.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

//拦截器，每次访问页面之前（或之后）都会执行的操作
@Service
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationService notificationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        //考虑到cookie可能不存在的情况
        if (cookies != null && cookies.length != 0){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")){
                    String value = cookie.getValue();
                    UserExample userExample = new UserExample();
                    //集成mybatis之后，通过创建criteria来实现想要执行的crud操作
                    userExample.createCriteria().andTokenEqualTo(value);
                    List<User> users = userMapper.selectByExample(userExample); //看看能不能找到该token对应的user，找不到就返回首页，找到则显示登录态
                    if (!users.isEmpty()){
                        request.getSession().setAttribute("user", users.get(0));
                        Long unreadCount = notificationService.unreadCount(users.get(0).getId());
                        request.getSession().setAttribute("unreadCount", unreadCount);
                    }
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
