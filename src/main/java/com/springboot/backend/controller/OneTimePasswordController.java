package com.springboot.backend.controller;


import com.springboot.backend.service.impl.OneTimePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/otp")
public class OneTimePasswordController {

    @Autowired
    private OneTimePasswordService oneTimePAsswordService;

    @Autowired
    public OneTimePasswordController(OneTimePasswordService oneTimePAsswordService) {
        super();
        this.oneTimePAsswordService = oneTimePAsswordService;
    }

    @GetMapping("/create")
    @ResponseBody
    private Object getOneTimePassword() {
        try {
            return ResponseEntity.ok(oneTimePAsswordService.returnOneTimePassword());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST);
        }
    }
}
