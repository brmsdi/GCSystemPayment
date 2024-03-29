package system.gc.services;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import system.gc.configuration.GenerateTXIDImpl;
import system.gc.models.Credentials;
import java.io.IOException;
import java.io.InputStream;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static system.gc.utils.TextUtils.generateTXID;

@ExtendWith(SpringExtension.class)
public class PixServiceTest {
    @Mock
    private Environment environment;

    @Mock
    private Credentials credentialsMock;

    private static JSONObject chargePix;

    @Mock
    private GenerateTXIDImpl generateTXID;
    @Spy
    @InjectMocks
    private PixService pixService;

    @BeforeEach
    public void setup() throws Exception {
        Mockito.when(environment.getActiveProfiles()).thenReturn(new String[]{"test"});
        InputStream credentialsFile = Objects.requireNonNull(Credentials.class.getClassLoader().getResourceAsStream("credentials-homo.json"));
        JSONTokener tokener = new JSONTokener(credentialsFile);
        JSONObject credentialsJSONObject = new JSONObject(tokener);
        credentialsFile.close();

        Mockito.when(environment.getProperty("CLIENT_ID"))
                .thenReturn(credentialsJSONObject.getString("CLIENT_ID"));
        Mockito.when(environment.getProperty("CLIENT_SECRET"))
                .thenReturn(credentialsJSONObject.getString("CLIENT_SECRET"));
        Mockito.when(environment.getProperty("CERTIFICATE"))
                .thenReturn(credentialsJSONObject.getString("CERTIFICATE"));
        Mockito.when(environment.getProperty("SANDBOX"))
                .thenReturn(String.valueOf(credentialsJSONObject.getBoolean("SANDBOX")));
        Mockito.when(environment.getProperty("DEBUG"))
                .thenReturn(String.valueOf(credentialsJSONObject.getBoolean("DEBUG")));

        Mockito.when(credentialsMock.getClientId())
                .thenReturn(credentialsJSONObject.getString("CLIENT_ID"));
        Mockito.when(credentialsMock.getClientSecret())
                .thenReturn(credentialsJSONObject.getString("CLIENT_SECRET"));
        Mockito.when(credentialsMock.getCertificate())
                .thenReturn(credentialsJSONObject.getString("CERTIFICATE"));
        Mockito.when(credentialsMock.isSandbox())
                .thenReturn(credentialsJSONObject.getBoolean("SANDBOX"));
        Mockito.when(credentialsMock.isDebug())
                .thenReturn(credentialsJSONObject.getBoolean("DEBUG"));

        if (chargePix == null)
        {
            //Cria registro para uso nos testes
            JSONObject body = new JSONObject();
            body.put("calendario", new JSONObject().put("expiracao", 3600));
            body.put("devedor", new JSONObject().put("cpf", "94271564656").put("nome", "Gorbadoc Oldbuck"));
            body.put("valor", new JSONObject().put("original", "500.00"));
            body.put("solicitacaoPagador", "Aluguel referente a data: ".concat(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
            String txid = generateTXID();
            HashMap<String, String> params = new HashMap<>();
            params.put("txid", txid);
            Mockito.when(environment.getProperty("CHAVE"))
                    .thenReturn("811701dc-1257-4bf5-abb7-1968c2611d8b");
            Mockito.when(pixService.prepareParams(generateTXID))
                    .thenReturn(params);
            body.put("chave", "811701dc-1257-4bf5-abb7-1968c2611d8b");
            chargePix = pixService.createChargePix(body, generateTXID);
        }
    }
    @Test
    public void create_charge_pix_success() throws Exception {
        JSONObject body = new JSONObject();
        body.put("calendario", new JSONObject().put("expiracao", 3600));
        body.put("devedor", new JSONObject().put("cpf", "94271564656").put("nome", "Gorbadoc Oldbuck"));
        body.put("valor", new JSONObject().put("original", "500.00"));
        body.put("solicitacaoPagador", "Aluguel referente a data: ".concat(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
        String txid = generateTXID();
        HashMap<String, String> params = new HashMap<>();
        params.put("txid", txid);
        Mockito.when(environment.getProperty("CHAVE"))
                .thenReturn("811701dc-1257-4bf5-abb7-1968c2611d8b");
        Mockito.when(pixService.prepareParams(generateTXID))
                .thenReturn(params);
        body.put("chave", "811701dc-1257-4bf5-abb7-1968c2611d8b");
        JSONObject result = pixService.createChargePix(body, generateTXID);
        assertEquals(txid, result.get("txid"));
    }

    @Test
    public void list_charges_pix_success() throws Exception {
        OffsetDateTime date = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC);
        HashMap<String, String> params = new HashMap<>();
        params.put("inicio", date.minusDays(5).toString());
        params.put("fim", date.toString());
        JSONObject jsonObject = new JSONObject();
        JSONObject result = pixService.pixListCharges(params, jsonObject);
        assertNotNull(result.get("cobs"));
    }

    @Test
    public void list_charges_pix_with_pagination_success() throws Exception {
        OffsetDateTime date = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC);
        HashMap<String, String> params = new HashMap<>();
        params.put("inicio", date.minusDays(5).toString());
        params.put("fim", date.toString());
        params.put("paginacao.paginaAtual", "10");
        params.put("paginacao.itensPorPagina", "1");
        JSONObject jsonObject = new JSONObject();
        JSONObject result = pixService.pixListCharges(params, jsonObject);
        assertNotNull(result.get("cobs"));
    }

    @Test
    public void details_charge_pix_test() throws Exception {
        // Cobrança detalhada
        JSONObject jsonObject = new JSONObject();
        HashMap<String, String> params = new HashMap<>();
        params.put("txid", Objects.requireNonNull(chargePix).get("txid").toString());
        String txid = params.get("txid");
        JSONObject result = pixService.pixDetailsCharge(params, jsonObject);
        assertEquals(txid, result.get("txid").toString());
    }
    @Test
    public void generate_QRCode_success_test() throws Exception {
        HashMap<String, Object> hashMap = new HashMap<>();
        HashMap<String, String> params = new HashMap<>();
        JSONObject loc = (JSONObject) chargePix.get("loc");
        params.put("id", loc.get("id").toString());
        JSONObject result = pixService.generateQRCode(params, hashMap);
        assertNotNull(result);
    }

    @Test
    public void update_charge_test() throws Exception {
        final String newValue = "100.00";
        HashMap<String, String> params = new HashMap<>();
        params.put("txid", chargePix.getString("txid"));
        JSONObject body = new JSONObject();
        body.put("valor", new JSONObject().put("original", newValue));
        JSONObject result = pixService.updateChargePix(params, body);
        assertEquals(newValue, ((JSONObject) result.get("valor")).get("original"));
    }
}