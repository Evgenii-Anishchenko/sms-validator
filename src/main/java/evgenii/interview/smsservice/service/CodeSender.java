package evgenii.interview.smsservice.service;

public interface CodeSender {
    void sendCode(String phoneNumber, String code);
}
