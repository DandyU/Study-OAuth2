package me.wired.learning.client.repository;

import lombok.*;

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
@EqualsAndHashCode(of = {"id"})
public class OAuthApprovals {

    @Id
    @Column(nullable = false, updatable = false)
    private String id;

    private String userId;

    private String clientId;

    private String scope;

    @Column(length = 10)
    private String status;

    private LocalDateTime expiresAt;

    private LocalDateTime lastModifiedAt;

}
