package system.gc.models;

public class Credentials {
    private String clientId;
    private String clientSecret;
    private String certificate;
    private boolean sandbox;
    private boolean debug;

    private Credentials(){}

    public static Credentials create(String clientId, String clientSecret, String certificate, boolean sandbox, boolean debug) {
        Credentials credentials = new Credentials();
        credentials.clientId = clientId;
        credentials.clientSecret = clientSecret;
        credentials.certificate = certificate;
        credentials.sandbox = sandbox;
        credentials.debug = debug;
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
