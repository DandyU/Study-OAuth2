package me.wired.learning.client.repository;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(of = {"tokenId"})
public class OAuthRefreshToken {

    @Id
    @Column(nullable = false, updatable = false)
    private String tokenId;

    @Column(length = 128000)
    private byte[] token;

    @Column(length = 128000)
    private byte[] authentication;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime insertedDateTime;

    @UpdateTimestamp
    private LocalDateTime updatedDateTime;

}
