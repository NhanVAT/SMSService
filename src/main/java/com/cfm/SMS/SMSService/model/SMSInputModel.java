package com.cfm.SMS.SMSService.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SMSInputModel {
    private String User;
    private String Password;
    private String CPCode;
    private String RequestID;
    private String UserID;
    private String ReceiverID;
    private String ServiceID;
    private String CommandCode;
    private String Content;
    private String ContentType;
}
