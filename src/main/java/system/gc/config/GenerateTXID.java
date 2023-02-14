package system.gc.config;

import java.util.UUID;

public interface GenerateTXID {

    default String generateTXID()
    {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
