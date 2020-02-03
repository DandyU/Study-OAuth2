package me.wired.learning.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuthClientDetailsRepository extends JpaRepository<OAuthClientDetails, String> {

    Optional<OAuthClientDetails> findByClientId(@Param("client_id") String clientId);

}
