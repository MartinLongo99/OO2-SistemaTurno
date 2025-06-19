package com.oo2.grupo15.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.oo2.grupo15.exceptions.DNIExistenteException;
import com.oo2.grupo15.helpers.ViewRouteHelper;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DNIExistenteException.class)
    public ModelAndView handleDniAlreadyExistsException(DNIExistenteException ex) {
        ModelAndView mAV = new ModelAndView(ViewRouteHelper.ERROR_VIEW);
        mAV.addObject("errorMessage", ex.getMessage());
        mAV.addObject("returnUrl", ViewRouteHelper.REDIRECT_USUARIOS_ROOT);
        return mAV;
    }
}
