package com.futebol.controller;

import com.futebol.dto.PageResponse;
import com.futebol.dto.TimeDTO;
import com.futebol.entity.Usuario;
import com.futebol.enums.StatusDesafio;
import com.futebol.service.TimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/times")
@RequiredArgsConstructor
public class TimeController {
    private final TimeService service;

    // GET /api/times/publicos?cidade=&bairro=&busca=&page=0&size=12&sort=nome,asc
    @GetMapping("/publicos")
    public ResponseEntity<PageResponse<TimeDTO.Response>> publicos(
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String bairro,
            @RequestParam(required = false) String busca,
            @PageableDefault(size = 12, sort = "criadoEm", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal Usuario u) {
        Page<TimeDTO.Response> pagina = service.buscar(null, cidade, bairro, busca, pageable, u);
        return ResponseEntity.ok(PageResponse.of(pagina));
    }

    // GET /api/times/disponiveis?cidade=&bairro=&busca=&page=0&size=12
    @GetMapping("/disponiveis")
    public ResponseEntity<PageResponse<TimeDTO.Response>> disponiveis(
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String bairro,
            @RequestParam(required = false) String busca,
            @PageableDefault(size = 12, sort = "criadoEm", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal Usuario u) {
        Page<TimeDTO.Response> pagina = service.buscar(StatusDesafio.DISPONIVEL, cidade, bairro, busca, pageable, u);
        return ResponseEntity.ok(PageResponse.of(pagina));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeDTO.Response> buscar(@PathVariable String id, @AuthenticationPrincipal Usuario u) {
        return ResponseEntity.ok(service.buscarPorId(id, u));
    }

    @GetMapping("/meus")
    public ResponseEntity<List<TimeDTO.Response>> meus(@AuthenticationPrincipal Usuario u) {
        return ResponseEntity.ok(service.meusTimes(u));
    }

    @PostMapping("/registrar")
    public ResponseEntity<TimeDTO.Response> criar(@Valid @RequestBody TimeDTO.Request req, @AuthenticationPrincipal Usuario u) {
        return ResponseEntity.ok(service.criar(req, u));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimeDTO.Response> atualizar(@PathVariable String id, @Valid @RequestBody TimeDTO.Request req, @AuthenticationPrincipal Usuario u) {
        return ResponseEntity.ok(service.atualizar(id, req, u));
    }

    @PostMapping("/{id}/escudo")
    public ResponseEntity<TimeDTO.Response> escudo(@PathVariable String id, @RequestParam("arquivo") MultipartFile arquivo, @AuthenticationPrincipal Usuario u) {
        return ResponseEntity.ok(service.uploadEscudo(id, arquivo, u));
    }

    @GetMapping("/{id}/escudo")
    public ResponseEntity<byte[]> getEscudo(@PathVariable String id) {
        return ResponseEntity.ok()
                .header("Content-Type", "image/png")
                .body(service.getEscudoBytes(id));
    }

    @PatchMapping("/{id}/disponibilidade")
    public ResponseEntity<TimeDTO.Response> disponib(@PathVariable String id, @AuthenticationPrincipal Usuario u) {
        return ResponseEntity.ok(service.alternarDisponibilidade(id, u));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id, @AuthenticationPrincipal Usuario u) {
        service.deletar(id, u);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}/membros/{membroId}")
    public ResponseEntity<Void> removerMembro(@PathVariable String id,
                                           @PathVariable String membroId,
                                           @AuthenticationPrincipal Usuario u) {
        service.removerMembro(id, membroId, u);
    return ResponseEntity.noContent().build();
    }
}
