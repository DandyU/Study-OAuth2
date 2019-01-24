package me.wired.learning.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class XUserServiceImpl implements XUserService {

    private final XUserRepository xUserRepository;
    private final PasswordEncoder passwordEncoder;

    public XUserServiceImpl(XUserRepository xUserRepository, PasswordEncoder passwordEncoder) {
        this.xUserRepository = xUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public XUser save(XUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return xUserRepository.save(user);
    }

    @Override
    public Optional<XUser> findByVariableId(String variableId) {
        return xUserRepository.findByVariableId(variableId);
    }

    @Override
    public Optional<XUser> findById(String id) {
        return xUserRepository.findById(id);
    }

    @Override
    public Page<XUser> findAll(Pageable pageable) {
        return xUserRepository.findAll(pageable);
    }

    @Override
    public void deleteById(String id) {
        xUserRepository.deleteById(id);
    }

}
