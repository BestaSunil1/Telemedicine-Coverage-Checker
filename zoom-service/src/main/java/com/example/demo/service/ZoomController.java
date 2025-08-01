package com.example.demo.service;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/zoom")

public class ZoomController {

    @Value("${zoom.accountId}")
    private String accountId;
    @Value("${zoom.clientId}")
    private String clientId;
    @Value("${zoom.clientSecret}")
    private String clientSecret;
    @Value("${zoom.hostEmail}")
    private String hostEmail;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/meeting")
    public ResponseEntity<?> createMeeting(@RequestBody Map<String, String> req) throws IOException {
        String topic = req.getOrDefault("topic", "Virtual Care Meeting");
        String accessToken = fetchAccessToken();

        HttpPost post = new HttpPost("https://api.zoom.us/v2/users/" + hostEmail + "/meetings");
        post.setHeader("Authorization", "Bearer " + accessToken);
        post.setHeader("Content-Type", "application/json");

        Map<String, Object> body = Map.of(
            "topic", topic,
            "type", 1 // Instant Meeting
        );
        post.setEntity(new StringEntity(objectMapper.writeValueAsString(body)));

        try (var httpClient = HttpClients.createDefault();
             var response = httpClient.execute(post)) {
            String json = null;
			try {
				json = EntityUtils.toString(response.getEntity());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return ResponseEntity.ok(json);
        }
    }

    private String fetchAccessToken() throws IOException {
        HttpPost post = new HttpPost("https://zoom.us/oauth/token");
        String authValue = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
        post.setHeader("Authorization", "Basic " + authValue);
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        String body = "grant_type=account_credentials&account_id=" + accountId;
        post.setEntity(new StringEntity(body));

        try (var httpClient = HttpClients.createDefault();
             var response = httpClient.execute(post)) {
            String json = null;
			try {
				json = EntityUtils.toString(response.getEntity());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return objectMapper.readTree(json).path("access_token").asText();
        }
    }
}

