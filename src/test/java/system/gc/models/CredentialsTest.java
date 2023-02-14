package system.gc.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CredentialsTest {
    @Test
    public void credentials_not_fail()
    {
        Assertions.assertDoesNotThrow(Credentials::createCredentialsHomo);
    }
}
