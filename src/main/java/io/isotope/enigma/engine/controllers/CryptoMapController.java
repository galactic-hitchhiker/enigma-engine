package io.isotope.enigma.engine.controllers;

import io.isotope.enigma.engine.services.CryptoService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping(
        path = "/crypto/map",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RestController
public class CryptoMapController {

    private final CryptoService cryptoService;

    public CryptoMapController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @PostMapping(path = "/encrypt/{key}")
    public ResponseEntity<Map<String, String>> encrypt(
            @PathVariable("key") String key,
            @RequestBody Map<String, String> body) {

        return ResponseEntity.ok()
                .body(cryptoService.encrypt(body, key));
    }

    @PostMapping(path = "/decrypt/{key}")
    public ResponseEntity<Map<String, String>> decrypt(
            @PathVariable("key") String key,
            @RequestBody Map<String, String> body) {

        return ResponseEntity.ok()
                .body(cryptoService.decrypt(body, key));
    }
}
