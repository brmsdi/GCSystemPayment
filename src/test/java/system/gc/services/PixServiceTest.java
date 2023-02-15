package system.gc.services;

import br.com.gerencianet.gnsdk.Gerencianet;
import org.json.JSONObject;
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
import system.gc.gerencianet.GerenciaNETInitialize;
import system.gc.models.Credentials;
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

    private static JSONObject chargePix;

    @Mock
    private GenerateTXIDImpl generateTXID;
    @Spy
    @InjectMocks
    private PixService pixService;

    private Gerencianet gerenciaNETTest;

    @BeforeEach
    public void setup() {
        try {
            Mockito.when(environment.getActiveProfiles()).thenReturn(new String[]{"test"});
            Credentials testCredentials = Credentials.createCredentialsHomo();
            JSONObject options = new JSONObject();
            options.put("client_id", testCredentials.getClientId());
            options.put("client_secret", testCredentials.getClientSecret());
            options.put("certificate", testCredentials.getCertificate());
            options.put("sandbox", testCredentials.isSandbox());
            gerenciaNETTest = new GerenciaNETInitialize().createDefaultChargePixSystemGerenciaNetWithOptions(testCredentials);

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
                chargePix = pixService.createChargePix(gerenciaNETTest, body, generateTXID);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        JSONObject result = pixService.createChargePix(gerenciaNETTest, body, generateTXID);
        assertEquals(txid, result.get("txid"));
    }

    @Test
    public void list_charges_pix_success() throws Exception {
        OffsetDateTime date = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC);
        HashMap<String, String> params = new HashMap<>();
        params.put("inicio", date.minusDays(5).toString());
        params.put("fim", date.toString());
        JSONObject jsonObject = new JSONObject();
        JSONObject result = pixService.pixListCharges(gerenciaNETTest, params, jsonObject);
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
        JSONObject result = pixService.pixListCharges(gerenciaNETTest, params, jsonObject);
        assertNotNull(result.get("cobs"));
    }

    @Test
    public void details_charge_pix_test() throws Exception {
        // Cobrança detalhada
        JSONObject jsonObject = new JSONObject();
        HashMap<String, String> params = new HashMap<>();
        params.put("txid", Objects.requireNonNull(chargePix).get("txid").toString());
        String txid = params.get("txid");
        JSONObject result = pixService.pixDetailsCharge(gerenciaNETTest, params, jsonObject);
        assertEquals(txid, result.get("txid").toString());
    }
    @Test
    public void generate_QRCode_success_test() throws Exception {
        HashMap<String, Object> hashMap = new HashMap<>();
        HashMap<String, String> params = new HashMap<>();
        JSONObject loc = (JSONObject) chargePix.get("loc");
        params.put("id", loc.get("id").toString());
        JSONObject result = pixService.generateQRCode(gerenciaNETTest, params, hashMap);
        assertNotNull(result);
    }
}