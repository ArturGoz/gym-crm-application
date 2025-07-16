package com.gca.controller;

import com.gca.exception.DaoException;
import com.gca.exception.ServiceException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestExceptionController {

    @GetMapping("/test/service")
    public void throwServiceException() {
        throw new ServiceException("This is a test ServiceException");
    }

    @GetMapping("/test/dao")
    public void throwDaoException() {
        throw new DaoException("This is a test DaoException");
    }
}
