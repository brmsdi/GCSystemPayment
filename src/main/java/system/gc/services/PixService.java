package system.gc.services;

import br.com.gerencianet.gnsdk.Gerencianet;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import system.gc.models.Credentials;
import system.gc.configuration.GenerateTXID;
import system.gc.gerencianet.GerenciaNETInitialize;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Wisley Bruno Marques França
 */
@Service
@Log4j2
public class PixService {
    @Autowired
    private Environment environment;

    @Autowired
    private Credentials credentials;

    private GerenciaNETInitialize gerenciaNETInitialize;

    /**
     * Gerar novas cobranças pix
     * @param body corpo da requisição exigidos pela API do gerencianet
     * @param information Informações adionais da cobrança
     * @return Objeto JSON da cobrança
     */
    public JSONObject createChargePix(JSONObject body, JSONArray information, GenerateTXID generateTXID) throws Exception {
        body.put("infoAdicionais", information);
        return createChargePix(body, generateTXID);
    }

    /**
     * Gerar novas cobranças pix
     * @param body corpo da requisição exigidos pela API do gerencianet
     * @return Objeto JSON da cobrança
     */
    public JSONObject createChargePix(JSONObject body, GenerateTXID generateTXID) throws Exception {
        Gerencianet gerencianet = getGerenciaNET(new GerenciaNETInitialize(), credentials);
        body.put("chave", environment.getProperty("CHAVE"));
        return gerencianet.call("pixCreateCharge", prepareParams(generateTXID), body);
    }

    /**
     * Listar cobranças pix em um determinado intervalo de tempo
     * @param params parametros exigidos pela API do gerencianet
     * @return Objetos JSON das cobranças pix
     */
    public JSONObject pixListCharges(HashMap<String, String> params, JSONObject jsonObject) throws Exception {
        Gerencianet gerencianet = getGerenciaNET(new GerenciaNETInitialize(), credentials);
        return gerencianet.call("pixListCharges", params, jsonObject);
    }

    /**
     * Consultar detalhes de uma cobrança por id
     * @param params parametros exigidos pela API do gerencianet
     * @return Objeto JSON com os detalhes da cobrança
     */
    public JSONObject pixDetailsCharge(HashMap<String, String> params, JSONObject jsonObject) throws Exception {
        Gerencianet gerencianet = getGerenciaNET(new GerenciaNETInitialize(), credentials);
        return gerencianet.call("pixDetailCharge", params, jsonObject);
    }

    /**
     * Gerar QRCODE Para pagamento
     * @param params parametros exigidos pela API do gerencianet. ID do location
     * @return pix copia/cola e QRCODE com codificação base64
     */
    public JSONObject generateQRCode(HashMap<String, String> params, HashMap<String, Object> hashMap) throws Exception {
        Gerencianet gerencianet = getGerenciaNET(new GerenciaNETInitialize(), credentials);
        return mapToJSON(gerencianet.call("pixGenerateQRCode", params, hashMap));
    }

    /**
     * Atualizar informações da divida
     * @param params parametros exigidos pela API do gerencianet. ID do location
     * @return informações da divida atualizadas
     */

    public JSONObject updateChargePix(HashMap<String, String> params, JSONObject body) throws Exception {
        Gerencianet gerencianet = getGerenciaNET(new GerenciaNETInitialize(), credentials);
        return gerencianet.call("pixUpdateCharge", params, body);
    }

    public HashMap<String, String> prepareParams(GenerateTXID generateTXID)
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("txid", generateTXID.generateTXID());
        return params;
    }

    public JSONObject mapToJSON(Map<?, ?> map)
    {
        return new JSONObject(map);
    }

    public Gerencianet getGerenciaNET(GerenciaNETInitialize gerenciaNETInitialize, Credentials credentials) throws Exception {
        return gerenciaNETInitialize.createDefaultChargePixSystemGerenciaNetWithOptions(credentials);
    }
}
