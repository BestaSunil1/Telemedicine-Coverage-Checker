package training.iqgateway.controller;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import training.iqgateway.controllers.AuthController;
import training.iqgateway.dto.LoginRequest;
import training.iqgateway.entities.User;
import training.iqgateway.entities.Role;
import training.iqgateway.service.AuthService;

import jakarta.servlet.http.Cookie;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testLogin_Success() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        User user = new User();
        user.setId("u123");
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setRole(Role.PATIENT);

        Mockito.when(authService.authenticate(eq("test@example.com"), eq("password123"))).thenReturn(user);
        Mockito.when(authService.generateToken(any(User.class))).thenReturn("mocktoken");

        // Act & Assert
        MockHttpServletResponse response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.role").value("PATIENT"))
                .andExpect(cookie().exists("authToken"))
                .andExpect(cookie().value("authToken", "mocktoken"))
                .andReturn().getResponse();

        // Also check the Set-Cookie header
        String setCookie = response.getHeader("Set-Cookie");
        assert setCookie != null && setCookie.contains("authToken=mocktoken") : "Set-Cookie header not set correctly";
    }

    @Test
    void testLogin_Failure_Unauthorized() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("wrong@example.com");
        loginRequest.setPassword("wrongpass");

        Mockito.when(authService.authenticate(any(), any()))
                .thenThrow(new RuntimeException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid credentials"));
    }

    @Test
    void testLogout_Success() throws Exception {
        User user = new User();
        user.setId("u123");
        // If logout is a no-op, you can omit mocking, else:
        Mockito.doNothing().when(authService).logout(Mockito.any(User.class));

        // Simulate user in request attribute using a custom handler (or do not check user for stateless JWT)
        mockMvc.perform(post("/api/auth/logout")
                        .requestAttr("authenticatedUser", user))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logout successful"))
                .andExpect(cookie().maxAge("authToken", 0));  // Cookie deleted (Max-Age=0)
    }

    @Test
    void testLogout_Failure() throws Exception {
        // Simulate error (e.g., some logout exception)
        Mockito.doThrow(new RuntimeException("Logout error")).when(authService).logout(Mockito.any());

        mockMvc.perform(post("/api/auth/logout")
                        .requestAttr("authenticatedUser", new User()))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Logout failed"));
    }
}

