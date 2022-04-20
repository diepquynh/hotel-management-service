package vn.utc.hotelmanager.jwt.config;

import com.google.common.net.HttpHeaders;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {

    private String secretKey;
    private String tokenPrefix;
    private Integer tokenExpirationAfterDays;
    private String filterUrl;

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }
}
