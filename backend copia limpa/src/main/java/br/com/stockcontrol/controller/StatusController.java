
package br.com.stockcontrol.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class StatusController {

    @GetMapping("/api/status")
    public Map<String, String> verificarStatus() {
        return Map.of(
            "sistema", "StockControl",
            "status", "online",
            "mensagem", "Backend funcionando corretamente!"
        );
    }
}
