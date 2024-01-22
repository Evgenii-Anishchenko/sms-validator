package evgenii.interview.smsservice.service;

import java.util.Map;

public interface CodeValidator {
    boolean isValid(Map<String, String> payload);
    void saveCode(String phoneNumber, String code);
    boolean canSendCode(String phoneNumber);
}
