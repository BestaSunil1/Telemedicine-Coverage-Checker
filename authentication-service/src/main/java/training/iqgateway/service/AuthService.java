package training.iqgateway.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import training.iqgateway.entities.JWTToken;
import training.iqgateway.entities.User;
import training.iqgateway.repository.JWTTokenRepository;
import training.iqgateway.repository.UserRepository;

@Service
public class AuthService {

    private final Key SIGNING_KEY;
    private final UserRepository userRepository;
    private final JWTTokenRepository jwtTokenRepository;

    public AuthService(
            UserRepository userRepository,
            JWTTokenRepository jwtTokenRepository,
            @Value("${jwt.secret}") String jwtSecret
    ) {
        this.userRepository = userRepository;
        this.jwtTokenRepository = jwtTokenRepository;

        if (jwtSecret.getBytes(StandardCharsets.UTF_8).length < 64) {
            throw new IllegalArgumentException("JWT secret must be at least 64 characters (bytes) long for HS512.");
        }

        this.SIGNING_KEY = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public User authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password."));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid email or password.");
        }

        return user;
    }

    public String generateToken(User user) {
        String token;
        LocalDateTime now = LocalDateTime.now();
        Optional<JWTToken> optionalToken = jwtTokenRepository.findByUser(user);

        if (optionalToken.isPresent() && now.isBefore(optionalToken.get().getExpiresAt())) {
            token = optionalToken.get().getToken();
        } else {
            token = generateNewToken(user);
            optionalToken.ifPresent(jwtTokenRepository::delete);
            saveToken(user, token);
        }

        return token;
    }

    private String generateNewToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("email", user.getEmail())
                .claim("username", user.getUsername())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    public void saveToken(User user, String token) {
        JWTToken jwtToken = new JWTToken(user, token, LocalDateTime.now().plusHours(1));
        jwtTokenRepository.save(jwtToken);
    }

    public void logout(User user) {
        jwtTokenRepository.deleteByUser(user);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token);

            Optional<JWTToken> storedToken = jwtTokenRepository.findByToken(token);
            return storedToken.isPresent() && storedToken.get().getExpiresAt().isAfter(LocalDateTime.now());
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
