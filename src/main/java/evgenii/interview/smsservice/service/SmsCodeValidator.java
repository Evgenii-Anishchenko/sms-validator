package evgenii.interview.smsservice.service;

import lombok.Getter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import static evgenii.interview.smsservice.utils.ConstantPool.CODE_KEY;
import static evgenii.interview.smsservice.utils.ConstantPool.PHONE_KEY;

@Service
@Getter
public class SmsCodeValidator implements CodeValidator {

    private final Map<String, String> verificationData = new HashMap<>();
    private final Map<String, LocalDateTime> requestTimestamps = new HashMap<>();

    @Override
    public boolean isValid(Map<String, String> payload) {
        String phoneNumber = payload.get(PHONE_KEY);
        String code = payload.get(CODE_KEY);
        String storedCode = verificationData.get(phoneNumber);
        return storedCode != null && storedCode.equals(code);
    }

    @Override
    public void saveCode(String phoneNumber, String code) {
        verificationData.put(phoneNumber, code);
        requestTimestamps.put(phoneNumber, LocalDateTime.now());
    }

    public boolean canSendCode(String phoneNumber) {
        LocalDateTime lastRequestTime = requestTimestamps.get(phoneNumber);
        return lastRequestTime == null || lastRequestTime.isBefore(LocalDateTime.now().minusMinutes(1));
    }

    // Scheduled task to purge expired codes
    @Scheduled(fixedRate = 60000) // Runs every minute
    public void purgeExpiredCodes() {
        Iterator<Entry<String, LocalDateTime>> iterator = requestTimestamps.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, LocalDateTime> entry = iterator.next();
            if (entry.getValue().isBefore(LocalDateTime.now().minusMinutes(5))) { // Assuming 5 minutes as code expiry
                iterator.remove();
                verificationData.remove(entry.getKey());
            }
        }
    }
}
