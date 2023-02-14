package system.gc.models;

import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Log4j2
public class Credentials {

    private String clientId;
    private String clientSecret;
    private String certificate;
    private boolean sandbox;
    private boolean debug;

    private Credentials() {}

    public static Credentials createCredentialsHomo()
    {
        return create(Objects.requireNonNull(Credentials.class.getClassLoader().getResourceAsStream("credentials-homo.json")));
    }

    public static Credentials createCredentialsProd()
    {
        return create(Objects.requireNonNull(Credentials.class.getClassLoader().getResourceAsStream("credentials-prod.json")));
    }

    private static Credentials create(InputStream credentialsFile)
    {
        JSONTokener tokener = new JSONTokener(credentialsFile);
        JSONObject credentialsJSONObject = new JSONObject(tokener);
        try {
            credentialsFile.close();
        } catch (IOException e) {
            log.error("Impossible to close file credentials.json");
        }
        Credentials credentials = new Credentials();
        credentials.clientId = credentialsJSONObject.getString("client_id");
        credentials.clientSecret = credentialsJSONObject.getString("client_secret");
        credentials.certificate = credentialsJSONObject.getString("certificate");
        credentials.sandbox = credentialsJSONObject.getBoolean("sandbox");
        credentials.debug = credentialsJSONObject.getBoolean("debug");
        return credentials;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getCertificate() {
        return certificate;
    }

    public boolean isSandbox() {
        return sandbox;
    }

    public boolean isDebug() {
        return debug;
    }
}
