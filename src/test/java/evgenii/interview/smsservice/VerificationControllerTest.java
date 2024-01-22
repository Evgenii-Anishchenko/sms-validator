package evgenii.interview.smsservice;

import evgenii.interview.smsservice.service.CodeGenerator;
import evgenii.interview.smsservice.service.CodeSender;
import evgenii.interview.smsservice.service.CodeValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VerificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CodeGenerator codeGenerator;

    @MockBean
    private CodeSender codeSender;

    @MockBean
    private CodeValidator codeValidator;

    @Test
    void whenSendCode_thenReturns200() throws Exception {
        String phoneNumber = "1234567890";
        String code = "1234";
        when(codeValidator.canSendCode(phoneNumber)).thenReturn(true);
        when(codeGenerator.generateCode()).thenReturn(code);

        mockMvc.perform(post("/verify/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"phone\":\"" + phoneNumber + "\"}"))
            .andExpect(status().isOk())
            .andExpect(content().string("Verification code sent"));

        verify(codeValidator).saveCode(phoneNumber, code);
        verify(codeSender).sendCode(phoneNumber, code);
    }

    @Test
    void whenValidateCode_thenReturns200() throws Exception {
        String phoneNumber = "1234567890";
        String code = "1234";
        when(codeValidator.isValid(any(Map.class))).thenReturn(true);

        mockMvc.perform(post("/verify/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"phone\":\"" + phoneNumber + "\", \"code\":\"" + code + "\"}"))
            .andExpect(status().isOk())
            .andExpect(content().string("Phone number verified"));
    }

    @Test
    void whenValidateCode_thenReturns400() throws Exception {
        String phoneNumber = "1234567890";
        String code = "1234";
        when(codeValidator.isValid(any(Map.class))).thenReturn(false);

        mockMvc.perform(post("/verify/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"phone\":\"" + phoneNumber + "\", \"code\":\"" + code + "\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Invalid or expired code"));
    }

}
