package system.gc.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import system.gc.models.Credentials;

import java.util.Objects;

@Component
public class CredentialsInitialize {

    @Autowired
    private Environment environment;
    @Bean
    public Credentials init()
    {
        return Credentials.create(
                Objects.requireNonNull(environment.getProperty("client_id")),
                Objects.requireNonNull(environment.getProperty("client_secret")),
                Objects.requireNonNull(environment.getProperty("certificate")),
                Boolean.parseBoolean(Objects.requireNonNull(environment.getProperty("sandbox"))),
                Boolean.parseBoolean(Objects.requireNonNull(environment.getProperty("debug")))
        );
    }

}
