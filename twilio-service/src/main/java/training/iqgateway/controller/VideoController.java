package training.iqgateway.controller;

import com.twilio.Twilio;
import com.twilio.jwt.accesstoken.AccessToken;
import com.twilio.jwt.accesstoken.VideoGrant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/video")
@CrossOrigin(origins = "*")
public class VideoController {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.api.key}")
    private String apiKeySid;

    @Value("${twilio.api.secret}")
    private String apiKeySecret;

    /**
     * Generates an access token for Twilio Video Room.
     *
     * @param identity - the username of the person
     * @param room     - the room name to join
     * @return JSON containing the JWT token
     */
    @GetMapping("/token")
    public ResponseEntity<Map<String, String>> generateToken(@RequestParam String identity,
                                                             @RequestParam String room) {
        try {
            // Initialize Twilio (needed for internal SDK use)
            Twilio.init(apiKeySid, apiKeySecret, accountSid);

            // Create a grant for video room
            VideoGrant videoGrant = new VideoGrant().setRoom(room);

            // Generate access token (NOTE: use byte[] for secret)
            byte[] secretBytes = apiKeySecret.getBytes(StandardCharsets.UTF_8);

            AccessToken accessToken = new AccessToken.Builder(accountSid, apiKeySid, secretBytes)
                    .identity(identity)
                    .grant(videoGrant)
                    .ttl(3600) // Token valid for 1 hour
                    .build();

            Map<String, String> response = new HashMap<>();
            response.put("token", accessToken.toJwt());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = Collections.singletonMap("error", "Token generation failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}