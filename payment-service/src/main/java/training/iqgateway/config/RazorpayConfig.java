package training.iqgateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Configuration
public class RazorpayConfig {
    
    @Value("${razorpay.key.id:}")
    private String razorpayKeyId;
    
    @Value("${razorpay.key.secret:}")
    private String razorpayKeySecret;
    
    private static final Logger logger = LoggerFactory.getLogger(RazorpayConfig.class);
    
    @Bean
    public RazorpayClient razorpayClient() throws RazorpayException {
        // Check if properties are set
        if (razorpayKeyId == null || razorpayKeyId.trim().isEmpty()) {
            logger.error("Razorpay Key ID is not configured. Please set 'razorpay.key.id' in application.properties");
            throw new IllegalStateException("Razorpay Key ID is missing. Please configure razorpay.key.id in application.properties");
        }
        
        if (razorpayKeySecret == null || razorpayKeySecret.trim().isEmpty()) {
            logger.error("Razorpay Key Secret is not configured. Please set 'razorpay.key.secret' in application.properties");
            throw new IllegalStateException("Razorpay Key Secret is missing. Please configure razorpay.key.secret in application.properties");
        }
        
        logger.info("Initializing Razorpay client with Key ID: {}", razorpayKeyId);
        return new RazorpayClient(razorpayKeyId, razorpayKeySecret);
    }
    
    // Getter methods for accessing the properties
    public String getRazorpayKeyId() {
        return razorpayKeyId;
    }
    
    public String getRazorpayKeySecret() {
        return razorpayKeySecret;
    }
}