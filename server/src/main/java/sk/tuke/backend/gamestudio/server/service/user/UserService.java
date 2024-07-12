package sk.tuke.backend.gamestudio.server.service.user;

import java.util.Optional;

public interface UserService {
    Optional<User> loadUserByUsernameOptional(String username);
}
