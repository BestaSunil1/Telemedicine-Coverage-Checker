
package training.iqgateway.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import training.iqgateway.dtos.ForgotPasswordRequest;
import training.iqgateway.dtos.ResetPasswordRequest;
import training.iqgateway.services.PasswordResetService;
/**
 * Unit tests for {@link PasswordResetController} using MockMvc.
 */
@WebMvcTest(PasswordResetController.class)
@AutoConfigureMockMvc(addFilters = false) // disable security filters for simplicity
class PasswordResetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordResetService passwordResetService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void forgotPassword_successAlwaysReturnsMessage() throws Exception {
        ForgotPasswordRequest req = new ForgotPasswordRequest();
        req.setEmail("user@example.com");

        Mockito.when(passwordResetService.initiatePasswordReset("user@example.com"))
                .thenReturn(true);

        mockMvc.perform(post("/api/forgotpassword/forgot-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message")
                        .value("If an account with that email exists, we've sent you a password reset link."));
    }

    @Test
    void forgotPassword_returns500OnException() throws Exception {
        ForgotPasswordRequest req = new ForgotPasswordRequest();
        req.setEmail("user@example.com");

        Mockito.when(passwordResetService.initiatePasswordReset(any()))
                .thenThrow(new RuntimeException("Error"));

        mockMvc.perform(post("/api/forgotpassword/forgot-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("An error occurred. Please try again later."));
    }

    @Test
    void resetPassword_success() throws Exception {
        ResetPasswordRequest req = new ResetPasswordRequest();
        req.setToken("valid-token");
        req.setNewPassword("NewPassword123");

        Mockito.when(passwordResetService.resetPassword("valid-token", "NewPassword123"))
                .thenReturn(true);

        mockMvc.perform(post("/api/forgotpassword/reset-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Password reset successfully."));
    }



    @Test
    void validateResetToken_valid() throws Exception {
        Mockito.when(passwordResetService.validateResetToken("good"))
                .thenReturn(true);

        mockMvc.perform(get("/api/forgotpassword/validate-reset-token")
                        .param("token", "good"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Token is valid."));
    }

    @Test
    void validateResetToken_invalid() throws Exception {
        Mockito.when(passwordResetService.validateResetToken("bad"))
                .thenReturn(false);

        mockMvc.perform(get("/api/forgotpassword/validate-reset-token")
                        .param("token", "bad"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid or expired token."));
    }

    @Test
    void validateResetToken_returns500OnException() throws Exception {
        Mockito.when(passwordResetService.validateResetToken(any()))
                .thenThrow(new RuntimeException("fail"));

        mockMvc.perform(get("/api/forgotpassword/validate-reset-token")
                        .param("token", "any"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("An error occurred. Please try again later."));
    }
}
