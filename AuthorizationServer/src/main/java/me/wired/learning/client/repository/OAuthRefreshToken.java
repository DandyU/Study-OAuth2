package me.wired.learning.client.repository;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

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

    private Byte[] token;

    private Byte[] authentication;

}
