package me.wired.learning.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface XUserService {

    XUser save(XUser user);

    Optional<XUser> findByVariableId(String variableId);

    Optional<XUser> findById(String id);

    Page<XUser> findAll(Pageable pageable);

    void deleteById(String id);

}
