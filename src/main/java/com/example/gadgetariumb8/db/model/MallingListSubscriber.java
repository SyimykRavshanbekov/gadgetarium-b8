package com.example.gadgetariumb8.db.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "malling_list_subscribers")
@NoArgsConstructor
public class MallingListSubscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "malling_list_subscriber_seq")
    @SequenceGenerator(name = "malling_list_subscriber_seq")
    @Column(name = "id", nullable = false)
    private Long id;
    @ElementCollection
    private List<String>usersEmails;


}