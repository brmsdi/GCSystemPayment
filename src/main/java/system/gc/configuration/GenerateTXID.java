package system.gc.configuration;

import java.util.UUID;

public interface GenerateTXID {

    default String generateTXID()
    {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
