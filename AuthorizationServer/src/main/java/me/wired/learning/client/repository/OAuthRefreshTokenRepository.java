package me.wired.learning.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthRefreshTokenRepository extends JpaRepository<OAuthRefreshToken, String> {

}
