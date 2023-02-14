package system.gc.gerencianet;

import br.com.gerencianet.gnsdk.Gerencianet;
import org.json.JSONObject;
import system.gc.models.Credentials;
public class GerenciaNETInitialize {

    public Gerencianet createDefaultChargeSystemGerenciaNetWithOptions(Credentials credentials) throws Exception {
        JSONObject options = new JSONObject();
        options.put("client_id", credentials.getClientId());
        options.put("client_secret", credentials.getClientSecret());
        options.put("sandbox", credentials.isSandbox());
        return new Gerencianet(options);
    }

    public Gerencianet createDefaultChargePixSystemGerenciaNetWithOptions(Credentials credentials) throws Exception {
        JSONObject options = new JSONObject();
        options.put("client_id", credentials.getClientId());
        options.put("client_secret", credentials.getClientSecret());
        options.put("certificate", credentials.getCertificate());
        options.put("sandbox", credentials.isSandbox());
        return new Gerencianet(options);
    }
}
