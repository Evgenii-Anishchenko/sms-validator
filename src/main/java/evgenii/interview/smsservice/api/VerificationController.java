package evgenii.interview.smsservice.api;

import evgenii.interview.smsservice.service.CodeGenerator;
import evgenii.interview.smsservice.service.CodeSender;
import evgenii.interview.smsservice.service.CodeValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/verify")
@RequiredArgsConstructor
public class VerificationController {

    private final CodeGenerator codeGenerator;
    private final CodeSender codeSender;
    private final CodeValidator codeValidator;

    @PostMapping("/send")
    @Operation(summary = "Send a verification code", description = "Sends a verification code to the provided phone number")
    @ApiResponse(responseCode = "200", description = "Verification code sent",
        content = @Content(mediaType = "text/plain"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Payload containing the phone number",
        required = true, content = @Content(schema = @Schema(implementation = Map.class),
        examples = {
            @ExampleObject(name = "Phone Verification", value = "{\"phone\": \"1234567890\"}")}))
    public ResponseEntity<String> sendCode(@RequestBody Map<String, String> payload) {
        String phoneNumber = payload.get("phone");

        // Check if it's allowed to send a new code
        if (!codeValidator.canSendCode(phoneNumber)) {
            return ResponseEntity.status(429).body("Rate limit exceeded. Please try again later.");
        }

        String code = codeGenerator.generateCode();
        codeValidator.saveCode(phoneNumber, code);
        codeSender.sendCode(phoneNumber, code);
        return ResponseEntity.ok("Verification code sent");
    }

    @PostMapping("/validate")
    @Operation(summary = "Validate a verification code", description = "Validates the provided verification code")
    @ApiResponse(responseCode = "200", description = "Phone number verified",
        content = @Content(mediaType = "text/plain"))
    @ApiResponse(responseCode = "400", description = "Invalid or expired code",
        content = @Content(mediaType = "text/plain"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Payload containing the phone number and verification code",
        required = true, content = @Content(schema = @Schema(implementation = Map.class),
        examples = {
            @ExampleObject(name = "Phone Verification", value = "{\"phone\": \"1234567890\", \"code\": \"1234\"}")}))
    public ResponseEntity<String> validateCode(
        @RequestBody Map<String, String> payload) {
        boolean isValid = codeValidator.isValid(payload);
        if (isValid) {
            return ResponseEntity.ok("Phone number verified");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired code");
        }
    }
}
