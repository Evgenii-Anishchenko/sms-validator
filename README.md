# SMS Verification Service

## Description

This service provides an API for phone number verification via SMS. It sends a verification code to a specified phone number and validates the entered code. This service is designed with flexibility to potentially support other verification methods like email in the future.

## Features

- **Send Verification Code**: Generates and sends a verification code to the provided phone number.
- **Validate Verification Code**: Validates the code entered by the user.
- **Rate Limiting**: Prevents abuse by limiting the frequency of verification code requests.
- **In-Memory Storage**: Temporarily stores verification data for validation purposes.