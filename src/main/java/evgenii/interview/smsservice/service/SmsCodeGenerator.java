package evgenii.interview.smsservice.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SmsCodeGenerator implements CodeGenerator {
    private final Random random = new Random();
    @Override
    public String generateCode() {
        return String.format("%04d", random.nextInt(10000));
    }
}
