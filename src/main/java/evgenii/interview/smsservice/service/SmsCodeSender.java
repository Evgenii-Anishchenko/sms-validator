package evgenii.interview.smsservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsCodeSender implements CodeSender {

    @Override
    public void sendCode(String phoneNumber, String code) {
        log.info("Sending SMS to " + phoneNumber + ": Your code is " + code);
    }
}
