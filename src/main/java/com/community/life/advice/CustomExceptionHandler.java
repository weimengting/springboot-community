package com.community.life.advice;

//一个专门用于处理异常与前端进行交互的类

import com.community.life.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)  //当出现异常的时候如何处理
    ModelAndView handleControllerException(Throwable ex, Model model) {
        if (ex instanceof CustomizeException){
            model.addAttribute("message", ex.getMessage());
        }
        else {
            model.addAttribute("message", "服务器已崩溃，请稍后再来试试~~~");
        }

        //类似于indexController的返回，即返回一个error.html页面
        return new ModelAndView("error");
    }
}
