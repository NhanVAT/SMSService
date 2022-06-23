package com.cfm.SMS.SMSService.service;

import com.cfm.SMS.SMSService.component.SOAPClientSAAJ;
import com.cfm.SMS.SMSService.model.Response;
import com.cfm.SMS.SMSService.model.SMSInputModel;
import com.cfm.SMS.SMSService.util.Constants;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class SMSService {
    private final ModelMapper modelMapper;
    private final Environment environment;
    private final SOAPClientSAAJ soapClientSAAJ;

    public static final Logger LOGGER = LoggerFactory.getLogger(SMSService.class);

    public SMSService(ModelMapper modelMapper, Environment environment, SOAPClientSAAJ soapClientSAAJ) {
        this.modelMapper = modelMapper;
        this.environment = environment;
        this.soapClientSAAJ = soapClientSAAJ;
    }

    public Response testSMS() {
        Response result = new Response();

        //Dùng trong môi trường DEV
        String phone = "0386909357";

        SMSInputModel dataSMS = new SMSInputModel();
        dataSMS.setUser("evn");
        dataSMS.setPassword("EVNICT@123");
        dataSMS.setCPCode("EVN");
        dataSMS.setRequestID("1");
        dataSMS.setUserID("84" + phone.substring(1));
        dataSMS.setReceiverID("84" + phone.substring(1));
        dataSMS.setServiceID("EVN");
        dataSMS.setCommandCode("bulksms");
        dataSMS.setContent("Test SMS service");
        dataSMS.setContentType("1");

        if (soapClientSAAJ.SOAPClientSAAJ(dataSMS)) {
            LOGGER.info("SEND OTP TO GETWAY SMS SUCCESS");
        } else {
            LOGGER.info("SEND OTP TO GETWAY SMS ERROR");
        }

        result.setErrorCode("0");
        result.setMessage(Constants.RESPONSE_MESSAGE.SUCCESS_CODE_TOTP_CREATE);

        return result;
    }

}
