package com.vegetables.vegetable;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/api")
public class SmsController {

    private static final Logger logger = LoggerFactory.getLogger(SmsController.class);

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.fromNumber}")
    private String fromNumber;

    @Value("${twilio.toNumber}")
    private String toNumber;

    @PostConstruct
    public void initTwilio() {
        Twilio.init(accountSid, authToken);
        logger.info("Twilio initialized with account SID {}", accountSid);
    }

    @PostMapping("/sendSms")
    public String sendSms(@RequestBody SmsRequest request) {
        logger.debug("Received SMS request with text: {}", request.getText());

        try {
            Message message = Message.creator(
                    new com.twilio.type.PhoneNumber(toNumber),
                    new com.twilio.type.PhoneNumber(fromNumber),
                    request.getText()
            ).create();

            logger.info("SMS sent successfully. SID: {}", message.getSid());
            return "Message SID: " + message.getSid();
        } catch (Exception e) {
            logger.error("Failed to send SMS", e);
            return "Error sending SMS: " + e.getMessage();
        }
    }
}

class SmsRequest {
    private String text;
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
