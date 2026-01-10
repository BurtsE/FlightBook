package booking.service;

import booking.dto.JwtValidationRequest;
import booking.dto.JwtValidationResponse;
import booking.dto.UserDetailsWithRole;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.io.IOException;

@Service
public class UserService {

    private final RestClient userClient;

    private final EurekaClient eurekaClient;


    public UserService(@Value("${app.user-service.name}") String userServiceName, EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
        InstanceInfo service = eurekaClient
                .getApplication(userServiceName)
                .getInstances()
                .getFirst();
        String baseUrl = "http://" + service.getHostName() + ":" + service.getPort();

        System.out.println(baseUrl);
        this.userClient = RestClient.create(baseUrl);
    }

    @Retryable(
            value = {RestClientException.class, IOException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2) // 1s, 2s, 4s
    )
    public JwtValidationResponse validateAndExtractRole(String jwt) {
        JwtValidationResponse response = userClient
                .post()
                .uri("users/validate-role")
                .header("Authorization", "Bearer " + jwt)
                .body(new JwtValidationRequest(jwt))
                .retrieve()
                .body(JwtValidationResponse.class);
        if (response == null) {
            throw new RuntimeException("Failed to validate JWT and extract role");
        }

        return response;
    }
}
