package com.community.life.advice;

//一个专门用于处理异常与前端进行交互的类

import com.alibaba.fastjson.JSON;
import com.community.life.dto.ResultDto;
import com.community.life.exception.CustomizeErrorCode;
import com.community.life.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//ControllerAdvice用于处理所有的Controller执行中出现的异常

@ControllerAdvice
public class CustomExceptionHandler {

    //通用的异常处理器,所有预料之外的Controller抛出的Exception异常都由这里处理
    @ExceptionHandler(Exception.class)
    public ModelAndView handleControllerException(Throwable ex, Model model,
                                     HttpServletRequest request, HttpServletResponse response) {
        String contentType = request.getContentType();
        ex.printStackTrace();
        //字符串内容放在前面，不然会报空指针异常
        if (("application/json").equals(contentType)){
            //返回json
            ResultDto resultDto;
            if (ex instanceof CustomizeException){
                resultDto = ResultDto.errorOf((CustomizeException) ex);
            }
            else {
                resultDto = ResultDto.errorOf(CustomizeErrorCode.SYSTEM_ERROR);
            }
            try {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json");
                response.setStatus(200);
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDto));
                writer.close();
            } catch (IOException e) {
            }
            return null;
        }
        else {
            //返回html的页面
            if (ex instanceof CustomizeException){
                model.addAttribute("message", ex.getMessage());
            }
            else {
                model.addAttribute("message", CustomizeErrorCode.SYSTEM_ERROR.getMessage());
            }
            //类似于indexController的返回，即返回一个error.html页面
            return new ModelAndView("error");
        }
    }
}
