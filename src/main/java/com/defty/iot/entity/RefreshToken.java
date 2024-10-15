package com.defty.iot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshToken extends BaseEntity {
    @Column(unique = true, nullable = false)
    String refreshToken;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private Boolean isRevoked = false;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    Account account;
}
