package com.futebol.controller;

import com.futebol.dto.AvaliacaoDTO;
import com.futebol.entity.Usuario;
import com.futebol.service.AvaliacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {
    private final AvaliacaoService service;

    @PostMapping
    public ResponseEntity<AvaliacaoDTO.Response> avaliar(@RequestBody AvaliacaoDTO.Request req, @AuthenticationPrincipal Usuario u) {
        return ResponseEntity.ok(service.avaliar(req, u));
    }

    @GetMapping("/{avaliadoId}")
    public ResponseEntity<List<AvaliacaoDTO.Response>> listar(@PathVariable String avaliadoId) {
        return ResponseEntity.ok(service.listar(avaliadoId));
    }

    @GetMapping("/{avaliadoId}/media")
    public ResponseEntity<Map<String, Double>> media(@PathVariable String avaliadoId) {
        return ResponseEntity.ok(Map.of("media", service.calcularMedia(avaliadoId)));
    }
}
