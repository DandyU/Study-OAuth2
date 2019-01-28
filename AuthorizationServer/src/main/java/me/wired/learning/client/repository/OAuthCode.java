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
@EqualsAndHashCode(of = {"id"})
public class OAuthCode {

    @Id
    @Column(nullable = false, updatable = false)
    private String id;

    private String code;

    private Byte[] authentication;

}
