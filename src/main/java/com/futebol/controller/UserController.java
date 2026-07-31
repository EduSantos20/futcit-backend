package com.futebol.controller;

import com.futebol.dto.UsuarioDTO;
import com.futebol.entity.Usuario;
import com.futebol.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UserController {

    private final UsuarioService usuarioService;

    @GetMapping("/perfil")
    public ResponseEntity<UsuarioDTO> getPerfil(@AuthenticationPrincipal Usuario usuarioLogado) {
        return ResponseEntity.ok(usuarioService.toDTO(usuarioLogado));
    }

    @PutMapping("/atualizar-perfil")
    public ResponseEntity<UsuarioDTO> atualizarPerfil(
            @AuthenticationPrincipal Usuario usuarioLogado,
            @Valid @RequestBody UsuarioDTO req) {

        System.out.println(usuarioLogado);

        return ResponseEntity.ok(usuarioService.atualizarPerfil(usuarioLogado.getId(), req));
    }

//    @PutMapping("/promover-dono")
//    public ResponseEntity<Void> promoverParaDono(@AuthenticationPrincipal Usuario usuarioLogado) {
//        usuarioService.promoverParaDono(usuarioLogado.getId());
//        return ResponseEntity.noContent().build();
//    }

}
