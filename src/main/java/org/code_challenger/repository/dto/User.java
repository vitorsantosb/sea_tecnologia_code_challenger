package org.code_challenger.repository.dto;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Table(name = "users")
@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private UUID id;

    @Column(length = 100)
    private String username;

    private String cpf;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserEmail> emails;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserPhone> phones;

    private String password;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @Column(updatable = true, name = "updated_at")
    private Date updatedAt;

    private Address address;

    @Embeddable
    @Data
    public static class Address {
        private String street;
        private String number;
        private String neighborhood;
        private String city;
        private String state;
        private String zipCode;
    }

    // Inner class for emails
    @Entity
    @Table(name = "user_email")
    @Data
    public static class UserEmail {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        private User user;

        @Column(length = 100, nullable = false)
        private String email;
    }

    // Inner class for phones
    @Entity
    @Table(name = "user_phone")
    @Data
    public static class UserPhone {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        private User user;

        @Column(length = 15, nullable = false)
        private String phoneNumber;
        private String phoneType;
    }
}

