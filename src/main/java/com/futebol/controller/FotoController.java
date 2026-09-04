package com.futebol.controller;

import com.futebol.dto.FotoDTO;
import com.futebol.entity.Usuario;
import com.futebol.service.FotoJogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/fotos")
@RequiredArgsConstructor
public class FotoController {

    private final FotoJogoService service;

    // Postar uma foto de jogo (jogador ou dono de time). O time é opcional:
    // se informado, a foto passa a aparecer também no perfil do time.
    @PostMapping
    public ResponseEntity<FotoDTO.Response> postar(
            @RequestParam("arquivo") MultipartFile arquivo,
            @RequestParam(value = "comentario", required = false) String comentario,
            @RequestParam(value = "timeId", required = false) String timeId,
            @AuthenticationPrincipal Usuario u) {
        return ResponseEntity.ok(service.postar(arquivo, comentario, timeId, u));
    }

    // Galeria de fotos do perfil de um jogador/dono (público)
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<FotoDTO.Response>> porUsuario(@PathVariable String usuarioId) {
        return ResponseEntity.ok(service.listarPorAutor(usuarioId));
    }

    // Galeria de fotos do perfil de um time (público)
    @GetMapping("/time/{timeId}")
    public ResponseEntity<List<FotoDTO.Response>> porTime(@PathVariable String timeId) {
        return ResponseEntity.ok(service.listarPorTime(timeId));
    }

    // Bytes da imagem (público, usado como src="" das <img>)
    @GetMapping("/{id}/imagem")
    public ResponseEntity<byte[]> imagem(@PathVariable String id) {
        return ResponseEntity.ok()
                .header("Content-Type", "image/jpeg")
                .body(service.getImagemBytes(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id, @AuthenticationPrincipal Usuario u) {
        service.deletar(id, u);
        return ResponseEntity.noContent().build();
    }
}
