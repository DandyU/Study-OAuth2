package me.wired.learning.user;

import org.springframework.stereotype.Service;

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

}
