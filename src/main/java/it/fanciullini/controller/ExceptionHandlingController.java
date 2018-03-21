package it.fanciullini.controller;

import org.hibernate.service.spi.ServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(ServiceException.class)
    public String conflict() {
        return "redirect:/";
    }

}