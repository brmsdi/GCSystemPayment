package system.gc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import system.gc.models.UserModel;
import system.gc.services.AuthenticationService;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class UserModelDetailsService implements UserDetailsService {

    @Autowired
    private AuthenticationService authenticationService;

    private final String ORGANIZATION = "gcsystem";
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = authenticationService.findForOrganization(ORGANIZATION, username);
        return new User(userModel.getUsername(), userModel.getPassword(), authorities());
    }

    private Collection<? extends GrantedAuthority> authorities()
    {
        Collection<GrantedAuthority> auths = new ArrayList<>();
        auths.add(new SimpleGrantedAuthority("ROLE_USER"));
        return auths;

    }
}
