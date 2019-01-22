package me.wired.learning.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class XUserServiceImpl implements XUserService {

    private final XUserRepository xUserRepository;

    public XUserServiceImpl(XUserRepository xUserRepository) {
        this.xUserRepository = xUserRepository;
    }

    @Override
    public XUser save(XUser user) {
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
