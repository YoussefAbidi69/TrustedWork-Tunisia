package tn.esprit.mscontractservicee.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.mscontractservicee.dto.UserDTO;
import tn.esprit.mscontractservicee.feign.UserServiceClient;

@Component
@Slf4j
public class UserServiceFallback implements UserServiceClient {

    @Override
    public UserDTO getUserById(Long id, String token) {
        log.error("Fallback: Unable to get user with id: {}", id);
        throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "User service unavailable");
    }

    @Override
    public UserDTO getCurrentUser(String token) {
        log.error("Fallback: Unable to get current user");
        throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "User service unavailable");
    }
}
