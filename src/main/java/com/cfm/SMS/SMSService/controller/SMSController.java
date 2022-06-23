package com.cfm.SMS.SMSService.controller;

import com.cfm.SMS.SMSService.model.Response;
import com.cfm.SMS.SMSService.service.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/com/cfm/sms")
public class SMSController {
    @Autowired
    private SMSService smsService;

    @GetMapping("/testAPI")
    public String testAPI() {
        return "Test API success";
    }

    @GetMapping("/testSMS")
    public ResponseEntity<Object> testSMS() {
        return this.createResponse(HttpStatus.OK, smsService.testSMS());
    }

    ResponseEntity<Object> createResponse(HttpStatus httpStatus, String errorCode, String jwt, String message) {
        Response result = new Response();
        result.setErrorCode(errorCode);
        result.setMessage(message);
        result.setJwt(jwt);

        return new ResponseEntity<>(result, httpStatus);
    }

    ResponseEntity<Object> createResponse(HttpStatus httpStatus, Response response) {
        return new ResponseEntity<>(response, httpStatus);
    }
}
