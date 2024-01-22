package evgenii.interview.smsservice;

import evgenii.interview.smsservice.service.SmsCodeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static evgenii.interview.smsservice.utils.ConstantPool.CODE_KEY;
import static evgenii.interview.smsservice.utils.ConstantPool.PHONE_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SmsCodeValidatorTest {
    private SmsCodeValidator smsCodeValidator;

    @BeforeEach
    void setUp() {
        smsCodeValidator = new SmsCodeValidator();
    }

    @Test
    void whenCodeIsValid_thenShouldReturnTrue() {
        String phoneNumber = "123456789";
        String code = "1234";
        smsCodeValidator.saveCode(phoneNumber, code);

        Map<String, String> payload = new HashMap<>();
        payload.put(PHONE_KEY, phoneNumber);
        payload.put(CODE_KEY, code);

        assertTrue(smsCodeValidator.isValid(payload));
    }

    @Test
    void whenCodeIsInvalid_thenShouldReturnFalse() {
        String phoneNumber = "123456789";
        String invalidCode = "4321";
        smsCodeValidator.saveCode(phoneNumber, "1234");

        Map<String, String> payload = new HashMap<>();
        payload.put(PHONE_KEY, phoneNumber);
        payload.put(CODE_KEY, invalidCode);

        assertFalse(smsCodeValidator.isValid(payload));
    }

    @Test
    void whenCodeIsSaved_thenShouldBeValidForThatPhone() {
        String phoneNumber = "123456789";
        String code = "1234";
        smsCodeValidator.saveCode(phoneNumber, code);

        assertEquals(code, smsCodeValidator.getVerificationData().get(phoneNumber));
    }

    @Test
    void whenCheckingSendability_thenShouldRespectRateLimit() {
        String phoneNumber = "123456789";
        assertTrue(smsCodeValidator.canSendCode(phoneNumber)); // First time should be true

        smsCodeValidator.saveCode(phoneNumber, "1234");
        assertFalse(smsCodeValidator.canSendCode(phoneNumber)); // Should be false within 1 minute
    }

}
