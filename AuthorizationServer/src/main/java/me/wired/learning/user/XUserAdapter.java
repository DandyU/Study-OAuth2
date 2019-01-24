package me.wired.learning.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class XUserAdapter extends User {

    private XUser xUser;

    public XUserAdapter(XUser xUser) {
        super(xUser.getVariableId(), xUser.getPassword(), authorities(xUser.getRoles()));
        this.xUser = xUser;
    }

    private static Collection<? extends GrantedAuthority> authorities(Set<XUserRole> roles) {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                .collect(Collectors.toSet());
    }

    public XUser getXUser() {
        return xUser;
    }

}
