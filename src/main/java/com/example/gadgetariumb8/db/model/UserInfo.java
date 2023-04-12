package com.example.gadgetariumb8.db.model;

import com.example.gadgetariumb8.db.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.CascadeType.ALL;

@Getter
@Setter
@Entity
@Table(name = "users_info")
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    @Id
    @SequenceGenerator(name = "user_info_gen", sequenceName = "user_info_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_info_gen")
    private Long id;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(cascade = ALL, mappedBy = "userInfo", orphanRemoval = true)
    private User user;
}