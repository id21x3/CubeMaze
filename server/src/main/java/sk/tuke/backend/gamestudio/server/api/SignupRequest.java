package sk.tuke.backend.gamestudio.server.api;

import lombok.Data;

@Data
public class SignupRequest {
    private String username;
    private String password;
}
