package training.iqgateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import training.iqgateway.dtos.ApiResponse;
import training.iqgateway.dtos.ForgotPasswordRequest;
import training.iqgateway.dtos.ResetPasswordRequest;
import training.iqgateway.services.PasswordResetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/forgotpassword")

public class PasswordResetController {
    
    private static final Logger logger = LoggerFactory.getLogger(PasswordResetController.class);
    
    @Autowired
    private PasswordResetService passwordResetService;
    
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        try {
            boolean result = passwordResetService.initiatePasswordReset(request.getEmail());
            
            // Always return success to prevent email enumeration attacks
            return ResponseEntity.ok(new ApiResponse(true, 
                "If an account with that email exists, we've sent you a password reset link."));
                
        } catch (Exception e) {
            logger.error("Error in forgot password endpoint", e);
            return ResponseEntity.status(500)
                .body(new ApiResponse(false, "An error occurred. Please try again later."));
        }
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            boolean result = passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
            
            if (result) {
                return ResponseEntity.ok(new ApiResponse(true, "Password reset successfully."));
            } else {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Invalid or expired reset token."));
            }
            
        } catch (Exception e) {
            logger.error("Error in reset password endpoint", e);
            return ResponseEntity.status(500)
                .body(new ApiResponse(false, "An error occurred. Please try again later."));
        }
    }
    
    @GetMapping("/validate-reset-token")
    public ResponseEntity<ApiResponse> validateResetToken(@RequestParam String token) {
        try {
            boolean isValid = passwordResetService.validateResetToken(token);
            
            if (isValid) {
                return ResponseEntity.ok(new ApiResponse(true, "Token is valid."));
            } else {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Invalid or expired token."));
            }
            
        } catch (Exception e) {
            logger.error("Error validating reset token", e);
            return ResponseEntity.status(500)
                .body(new ApiResponse(false, "An error occurred. Please try again later."));
        }
    }
}
