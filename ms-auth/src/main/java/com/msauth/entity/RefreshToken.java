package com.msauth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            unique = true,
            length = 1000
    )
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiration;

    @Builder.Default
    @Column(nullable = false)
    private Boolean revoked = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "usuario_id",
            nullable = false
    )
    private Usuario usuario;
}