package system.gc.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import system.gc.configuration.GenerateTXIDImpl;
import system.gc.services.PixService;
import java.util.HashMap;

@RestController
@RequestMapping(value = "v1/pix", produces = MediaType.APPLICATION_JSON_VALUE)
public class PixController {

    @Autowired
    private PixService pixService;

    @PostMapping("new")
    public ResponseEntity<String> createChargePix(@RequestBody String debt) throws Exception {
        JSONObject chargePix = pixService.createChargePix(new JSONObject(debt), new GenerateTXIDImpl());
        return ResponseEntity.ok(chargePix.toString());
    }

    @GetMapping("list")
    public ResponseEntity<String> listChargesPix(@RequestParam(name = "start") String start,
                                                 @RequestParam(name = "end") String end,
                                                 @RequestParam(name = "currentPage", defaultValue = "0") String currentPage,
                                                 @RequestParam(name = "itemsPerPage", defaultValue = "5") String itemsPerPage) throws Exception {
        HashMap<String, String> params = new HashMap<>();
        params.put("inicio", start);
        params.put("fim", end);
        params.put("paginacao.paginaAtual", currentPage);
        params.put("paginacao.itensPorPagina", itemsPerPage);
        JSONObject result = pixService.pixListCharges(params, new JSONObject());
        return ResponseEntity.ok(result.toString());
    }

    @GetMapping("details")
    public ResponseEntity<String> details(@RequestParam(name = "txid") String txid) throws Exception {
        HashMap<String, String> params = new HashMap<>();
        params.put("txid", txid);
        JSONObject result = pixService.pixDetailsCharge(params, new JSONObject());
        return ResponseEntity.ok(result.toString());
    }

    @GetMapping("qrcode")
    public ResponseEntity<String> generateQRCode(@RequestParam(name = "id") String id) throws Exception {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        JSONObject result = pixService.generateQRCode(params, new HashMap<>());
        return ResponseEntity.ok(result.toString());
    }

    @PostMapping("update")
    public ResponseEntity<String> updateCharge(@RequestParam String txid, @RequestBody String charge) throws Exception {
        HashMap<String, String> params = new HashMap<>();
        params.put("txid", txid);
        JSONObject chargePix = pixService.updateChargePix(params, new JSONObject(charge));
        return ResponseEntity.ok(chargePix.toString());
    }
}