package ru.ptkom.provisor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ErrorsController implements ErrorController {

    @Autowired
    private TemplateEngine templateEngine;

    private static final String[] ERRORS_DESCRIPTIONS_LIST = {"Стнаница не найдена, поищите получше...",
    "Ошибка сервера, что-то явно пошло не так...",
    "Что-то не так..."};

    @GetMapping("/error")
    public String httpErrorHandler(HttpServletRequest request, HttpServletResponse response){
        Context context = new Context();
        int code = response.getStatus();
        String[] errorData= new String[2];
        if (code == 404) {
            errorData[0] = String.valueOf(code);
            errorData[1] = ERRORS_DESCRIPTIONS_LIST[0];
            context.setVariable("error", errorData);
        } else if (code == 500) {
            errorData[0] = String.valueOf(code);
            errorData[1] = ERRORS_DESCRIPTIONS_LIST[1];
            context.setVariable("error", errorData);
        } else {
            errorData[0] = String.valueOf(code);
            errorData[1] = ERRORS_DESCRIPTIONS_LIST[2];
            context.setVariable("error", errorData);
        }


        String errorPage = templateEngine.process("error", context);
        return errorPage;
    }
}