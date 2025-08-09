package com.springboot.backend.service.impl;

import com.springboot.backend.config.OneTimePasswordHelpService;
import com.springboot.backend.model.OneTimePassword;
import com.springboot.backend.repository.OneTimePasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
public class OneTimePasswordService {
    private final Long expiryInterval = 5L * 60 * 1000;//otp expires in 5minutes

    OneTimePasswordRepository oneTimePasswordRepository;

    OneTimePasswordHelpService oneTimePasswordHelpService;

    @Autowired
    public OneTimePasswordService(OneTimePasswordRepository oneTimePasswordRepository) {
        this.oneTimePasswordRepository = oneTimePasswordRepository;
    }

    public OneTimePassword returnOneTimePassword() {
        OneTimePassword oneTimePassword = new OneTimePassword();

        oneTimePassword.setOneTimePasswordCode(oneTimePasswordHelpService.createRandomOneTimePassword().get());
        oneTimePassword.setExpires(new Date(System.currentTimeMillis() + expiryInterval));

        oneTimePasswordRepository.save(oneTimePassword);

        return oneTimePassword;

    }
}
