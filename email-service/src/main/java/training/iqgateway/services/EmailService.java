package training.iqgateway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${app.mail.from}")
    private String fromEmail;
    
    @Value("${app.frontend.url}")
    private String frontendUrl;

    public void sendPasswordResetEmail(String toEmail, String resetToken, String username) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Password Reset Request - Healthcare System");
            
            String resetUrl = frontendUrl + "/reset-password?token=" + resetToken;
            
            String htmlContent = buildPasswordResetEmailTemplate(username, resetUrl);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            logger.info("Password reset email sent successfully to: {}", toEmail);
            
        } catch (MessagingException e) {
            logger.error("Failed to send password reset email to: {}", toEmail, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
    
    public void sendPasswordResetConfirmationEmail(String toEmail, String username) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Password Reset Successful - Healthcare System");
            
            String htmlContent = buildPasswordResetConfirmationTemplate(username);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            logger.info("Password reset confirmation email sent to: {}", toEmail);
            
        } catch (MessagingException e) {
            logger.error("Failed to send confirmation email to: {}", toEmail, e);
        }
    }
    
    private String buildPasswordResetEmailTemplate(String username, String resetUrl) {
        return "<!DOCTYPE html>" +
               "<html>" +
               "<head>" +
               "<style>" +
               "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
               ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
               ".header { background-color: #007bff; color: white; padding: 20px; text-align: center; }" +
               ".content { padding: 30px; background-color: #f9f9f9; }" +
               ".button { display: inline-block; padding: 12px 24px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }" +
               ".footer { padding: 20px; text-align: center; color: #666; font-size: 12px; }" +
               "</style>" +
               "</head>" +
               "<body>" +
               "<div class='container'>" +
               "<div class='header'>" +
               "<h1>Healthcare System</h1>" +
               "</div>" +
               "<div class='content'>" +
               "<h2>Password Reset Request</h2>" +
               "<p>Hello " + username + ",</p>" +
               "<p>We received a request to reset your password for your Healthcare System account.</p>" +
               "<p>Click the button below to reset your password:</p>" +
               "<a href='" + resetUrl + "' class='button'>Reset Password</a>" +
               "<p>If the button doesn't work, copy and paste this link into your browser:</p>" +
               "<p>" + resetUrl + "</p>" +
               "<p><strong>This link will expire in 1 hour.</strong></p>" +
               "<p>If you didn't request this password reset, please ignore this email. Your password will remain unchanged.</p>" +
               "</div>" +
               "<div class='footer'>" +
               "<p>This is an automated message, please do not reply to this email.</p>" +
               "<p>&copy; 2025 Healthcare System. All rights reserved.</p>" +
               "</div>" +
               "</div>" +
               "</body>" +
               "</html>";
    }
    
    private String buildPasswordResetConfirmationTemplate(String username) {
        return "<!DOCTYPE html>" +
               "<html>" +
               "<head>" +
               "<style>" +
               "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
               ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
               ".header { background-color: #28a745; color: white; padding: 20px; text-align: center; }" +
               ".content { padding: 30px; background-color: #f9f9f9; }" +
               ".footer { padding: 20px; text-align: center; color: #666; font-size: 12px; }" +
               "</style>" +
               "</head>" +
               "<body>" +
               "<div class='container'>" +
               "<div class='header'>" +
               "<h1>Healthcare System</h1>" +
               "</div>" +
               "<div class='content'>" +
               "<h2>Password Reset Successful</h2>" +
               "<p>Hello " + username + ",</p>" +
               "<p>Your password has been successfully reset for your Healthcare System account.</p>" +
               "<p>If you didn't make this change, please contact our support team immediately.</p>" +
               "</div>" +
               "<div class='footer'>" +
               "<p>This is an automated message, please do not reply to this email.</p>" +
               "<p>&copy; 2025 Healthcare System. All rights reserved.</p>" +
               "</div>" +
               "</div>" +
               "</body>" +
               "</html>";
    }
}
