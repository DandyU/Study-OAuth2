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
@EqualsAndHashCode(of = {"authenticationId"})
public class OAuthClientToken {

    @Id
    @Column(nullable = false, updatable = false)
    private String authenticationId;

    private String tokenId;

    private Byte[] token;

    private String userName;

    private String clientId;

}
