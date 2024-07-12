package sk.tuke.backend.gamestudio.server.service.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;
    @Column
    private String password;
}
