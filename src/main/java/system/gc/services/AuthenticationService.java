package system.gc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import system.gc.models.UserModel;

@Service
public class AuthenticationService {

    @Autowired
    private Environment environment;

    public UserModel findForOrganization(String organizationTag, String username) throws UsernameNotFoundException
    {
        String usernameProp = environment.getProperty(organizationTag.concat(".USERNAME"));
        String password = environment.getProperty(organizationTag.concat(".PASSWORD"));
        if (usernameProp == null || usernameProp.isEmpty() || password == null || password.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }

        if (!username.equals(usernameProp))
        {
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }
        return new UserModel(usernameProp, password);
    }
}
