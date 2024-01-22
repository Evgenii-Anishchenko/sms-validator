SMS Verification Service
Description
This service provides an API for phone number verification via SMS. It sends a verification code to a specified phone number and validates the entered code. This service is designed with flexibility to potentially support other verification methods like email in the future.

Features
Send Verification Code: Generates and sends a verification code to the provided phone number.
Validate Verification Code: Validates the code entered by the user.
Rate Limiting: Prevents abuse by limiting the frequency of verification code requests.
In-Memory Storage: Temporarily stores verification data for validation purposes.
Getting Started
Prerequisites
Java 17
Maven or Gradle (for building the project)
Any IDE that supports Java (e.g., IntelliJ IDEA, Eclipse)
Installation
Clone the repository:
bash
Copy code
git clone https://your-repository-url.git
Navigate to the project directory:
bash
Copy code
cd sms-verification-service
Build the project:
With Maven:
bash
Copy code
mvn clean install
With Gradle:
bash
Copy code
gradle build
Running the Service
Run the application using your IDE or through the command line:
With Maven:
bash
Copy code
mvn spring-boot:run
With Gradle:
bash
Copy code
gradle bootRun
Usage
The service exposes two main REST endpoints:

POST /verify/send: Send a verification code.
Request body:
json
Copy code
{
"phone": "1234567890"
}
POST /verify/validate: Validate a received code.
Request body:
json
Copy code
{
"phone": "1234567890",
"code": "1234"
}
Documentation
Swagger UI is available at http://localhost:8080/swagger-ui/ for interactive API documentation and testing.
Testing
Run the unit and integration tests in the project using:
With Maven:
bash
Copy code
mvn test
With Gradle:
bash
Copy code
gradle test