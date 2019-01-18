package me.wired.learning.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface XUserRepository extends JpaRepository<XUser, String> {

    Optional<XUser> findBySubId(String subId);

}
