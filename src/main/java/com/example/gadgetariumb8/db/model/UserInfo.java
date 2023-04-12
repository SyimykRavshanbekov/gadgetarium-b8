package com.example.gadgetariumb8.db.model;

import com.example.gadgetariumb8.db.model.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.CascadeType.*;

@Getter
@Setter
@Entity
@Table(name = "users_info")
@NoArgsConstructor
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_info_seq")
    @SequenceGenerator(name = "user_info_seq")
    @Column(name = "id", nullable = false)
    private Long id;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(cascade = ALL, mappedBy = "userInfo", orphanRemoval = true)
    private User user;

    public UserInfo(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}