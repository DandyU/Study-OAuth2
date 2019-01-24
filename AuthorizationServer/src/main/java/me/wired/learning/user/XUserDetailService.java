package me.wired.learning.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class XUserDetailService implements UserDetailsService {

    @Autowired
    XUserRepository xUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<XUser> optionalUser = xUserRepository.findByVariableId(username);
        XUser xUser = optionalUser.orElseThrow(() -> new UsernameNotFoundException(username));
        return new XUserAdapter(xUser);
    }

}
