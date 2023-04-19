package com.example.gadgetariumb8.db.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "malling_list_subscribers")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MallingListSubscriber {
    @Id
    @SequenceGenerator(name = "malling_list_subscriber_gen", sequenceName = "malling_list_subscriber_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "malling_list_subscriber_gen")
    private Long id;
    private String userEmail;
}