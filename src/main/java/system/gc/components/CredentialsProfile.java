package system.gc.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import system.gc.models.Credentials;
import java.util.Arrays;
import java.util.Objects;

@Component
public class CredentialsProfile {
    @Autowired
    private Environment environment;

    @Bean
    public Credentials create()
    {
        String[] profile = Objects.requireNonNull(environment.getActiveProfiles());
        if (Arrays.stream(profile).toList().contains("production"))
        {
            return Credentials.createCredentialsProd();
        }
        return Credentials.createCredentialsHomo();
    }
}
