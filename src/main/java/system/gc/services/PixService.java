package system.gc.services;

import br.com.gerencianet.gnsdk.Gerencianet;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import system.gc.config.GenerateTXID;
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
    /**
     * Gerar novas cobranças pix
     * @param gerencianet api do gerencianet
     * @param body corpo da requisição exigidos pela API do gerencianet
     * @param information Informações adionais da cobrança
     * @return Objeto JSON da cobrança
     */
    public JSONObject createChargePix(Gerencianet gerencianet, JSONObject body, JSONArray information, GenerateTXID generateTXID) throws Exception {
        body.put("infoAdicionais", information);
        return createChargePix(gerencianet, body, generateTXID);
    }

    /**
     * Gerar novas cobranças pix
     * @param gerencianet api do gerencianet
     * @param body corpo da requisição exigidos pela API do gerencianet
     * @return Objeto JSON da cobrança
     */
    public JSONObject createChargePix(Gerencianet gerencianet, JSONObject body, GenerateTXID generateTXID) throws Exception {
        body.put("chave", environment.getProperty("CHAVE"));
        return gerencianet.call("pixCreateCharge", prepareParams(generateTXID), body);
    }
    /**
     * Listar cobranças pix em um determinado intervalo de tempo
     * @param gerencianet api do gerencianet
     * @param params parametros exigidos pela API do gerencianet
     * @return Objetos JSON das cobranças pix
     */
    public JSONObject pixListCharges(Gerencianet gerencianet, HashMap<String, String> params, JSONObject jsonObject) throws Exception {
        return gerencianet.call("pixListCharges", params, jsonObject);
    }

    /**
     * Consultar detalhes de uma cobrança por id
     * @param gerencianet api do gerencianet
     * @param params parametros exigidos pela API do gerencianet
     * @return Objeto JSON com os detalhes da cobrança
     */
    public JSONObject pixDetailsCharge(Gerencianet gerencianet, HashMap<String, String> params, JSONObject jsonObject) throws Exception {
        return gerencianet.call("pixDetailCharge", params, jsonObject);
    }

    /**
     * Gerar QRCODE Para pagamento
     * @param gerencianet api do gerencianet
     * @param params parametros exigidos pela API do gerencianet. ID do location
     * @return pix copia/cola e QRCODE com codificação base64
     */
    public JSONObject generateQRCode(Gerencianet gerencianet, HashMap<String, String> params, HashMap<String, Object> hashMap) throws Exception {
        return mapToJSON(gerencianet.call("pixGenerateQRCode", params, hashMap));
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

}
