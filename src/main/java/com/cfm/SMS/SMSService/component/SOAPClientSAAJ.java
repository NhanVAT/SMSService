package com.cfm.SMS.SMSService.component;

import com.cfm.SMS.SMSService.model.SMSInputModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.soap.*;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

@Component
public class SOAPClientSAAJ {
    public static final Logger LOGGER = LoggerFactory.getLogger(SOAPClientSAAJ.class);

    private static void createSoapEnvelope(SOAPMessage soapMessage, SMSInputModel inputdata) throws SOAPException {
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String myNamespace = "impl";
        String myNamespaceURI = "http://impl.bulkSms.ws/";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);

            /*
            Constructed SOAP Request Message:
            <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"
            xmlns:myNamespace="https://www.w3schools.com/xml/">
                <SOAP-ENV:Header/>
                <SOAP-ENV:Body>
                    <myNamespace:CelsiusToFahrenheit>
                        <myNamespace:Celsius>100</myNamespace:Celsius>
                    </myNamespace:CelsiusToFahrenheit>
                </SOAP-ENV:Body>
            </SOAP-ENV:Envelope>
            */

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("wsCpMt", myNamespace);

        SOAPElement wsCpMt_User = soapBodyElem.addChildElement("User");
        wsCpMt_User.addTextNode(inputdata.getUser());
        SOAPElement wsCpMt_Password = soapBodyElem.addChildElement("Password");
        wsCpMt_Password.addTextNode(inputdata.getPassword());
        SOAPElement wsCpMt_CPCode = soapBodyElem.addChildElement("CPCode");
        wsCpMt_CPCode.addTextNode(inputdata.getCPCode());
        SOAPElement wsCpMt_RequestID = soapBodyElem.addChildElement("RequestID");
        wsCpMt_RequestID.addTextNode(inputdata.getRequestID());
        SOAPElement wsCpMt_UserID = soapBodyElem.addChildElement("UserID");
        wsCpMt_UserID.addTextNode(inputdata.getUserID());
        SOAPElement wsCpMt_ReceiverID = soapBodyElem.addChildElement("ReceiverID");
        wsCpMt_ReceiverID.addTextNode(inputdata.getReceiverID());
        SOAPElement wsCpMt_ServiceID = soapBodyElem.addChildElement("ServiceID");
        wsCpMt_ServiceID.addTextNode(inputdata.getServiceID());
        SOAPElement wsCpMt_CommandCode = soapBodyElem.addChildElement("CommandCode");
        wsCpMt_CommandCode.addTextNode(inputdata.getCommandCode());
        SOAPElement wsCpMt_Content = soapBodyElem.addChildElement("Content");
        wsCpMt_Content.addTextNode(inputdata.getContent());
        SOAPElement wsCpMt_ContentType = soapBodyElem.addChildElement("ContentType");
        wsCpMt_ContentType.addTextNode(inputdata.getContentType());

    }

    private static SOAPMessage createSOAPRequest(String soapAction, SMSInputModel datasms) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        createSoapEnvelope(soapMessage, datasms);

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", soapAction);

        soapMessage.saveChanges();

        /* Print the request message, just for debugging purposes */
        System.out.println("Request SOAP Message:");
        soapMessage.writeTo(System.out);
        System.out.println("\n");

        return soapMessage;
    }

    // SAAJ - SOAP Client Testing
    public boolean SOAPClientSAAJ(SMSInputModel datasms) {
        String soapEndpointUrl = "http://apismsbrand.viettel.vn:8998/bulkapi";
        String soapAction = "http://apismsbrand.viettel.vn:8998/bulkapi";

        return callSoapWebService(soapEndpointUrl, soapAction, datasms);
    }

    private boolean callSoapWebService(String soapEndpointUrl, String soapAction, SMSInputModel datasms) {
        try {
            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Send SOAP Message to SOAP Server
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(soapAction, datasms), soapEndpointUrl);

            // Print the SOAP Response
            System.out.println("Response SOAP Message:");
            soapResponse.writeTo(System.out);
            System.out.println();

            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            soapResponse.writeTo(os);
            String strMsg = new String(os.toByteArray(), Charset.forName("UTF-8"));
            soapConnection.close();

            //Message SMS
            LOGGER.info("RESULT OTP TO GETWAY SMS " + Thread.currentThread().getName() + datasms.getUserID() + " " + strMsg);

            return strMsg.contains("<result>1</result>");
        } catch (Exception e) {
            System.err.println("\nError occurred while sending SOAP Request to Server!\nMake sure you have the " +
                    "correct endpoint URL and SOAPAction!\n");
            e.printStackTrace();
            return false;
        }
    }

}
