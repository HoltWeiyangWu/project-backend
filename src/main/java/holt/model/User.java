package holt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "user", schema = "project")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 256)
    @NotNull
    @Column(name = "username", nullable = false, length = 256)
    private String username;

    @Size(max = 256)
    @NotNull
    @Column(name = "password", nullable = false, length = 256)
    private String password;

    @Size(max = 256)
    @Column(name = "name", length = 256)
    private String name;

    @Size(max = 1024)
    @Column(name = "avatar", length = 1024)
    private String avatar;

    @Size(max = 256)
    @Column(name = "profile", length = 256)
    private String profile;

    @Size(max = 256)
    @NotNull
    @Column(name = "role", nullable = false, length = 256)
    private String role;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "update_time", nullable = false)
    private Date updateTime;

    @Size(max = 512)
    @Column(name = "email", length = 512)
    private String email;

}