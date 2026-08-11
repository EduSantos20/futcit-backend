package com.futebol.service;

import com.futebol.dto.UsuarioDTO;
import com.futebol.entity.Usuario;
import com.futebol.enums.TipoUsuario;
import com.futebol.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final AvaliacaoService avaliacaoService;

    public UsuarioDTO buscarPorId(String id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return toDTO(usuario);
    }

    @Transactional
    public UsuarioDTO atualizarPerfil(String usuarioId, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Campos comuns a ambos
        usuario.setNome(dto.getNome());
        usuario.setTelefone(dto.getTelefone());
        usuario.setCidade(dto.getCidade());
        usuario.setFotoPerfil(dto.getFotoPerfil());

        // Lógica específica por papel
        if (usuario.getTipoUsuario() == TipoUsuario.JOGADOR) {
            usuario.setPosicao(dto.getPosicao());
        }

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return toDTO(usuarioSalvo);
    }

//    @Transactional
//    public void promoverParaDono(String usuarioId) {
//        Usuario usuario = usuarioRepository.findById(usuarioId)
//                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
//
//        usuario.setTipoUsuario(com.futebol.enums.TipoUsuario.DONO);
//        usuarioRepository.save(usuario);
//    }

    public UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setTelefone(usuario.getTelefone());
        dto.setCidade(usuario.getCidade());
        dto.setPosicao(usuario.getPosicao());
        dto.setFotoPerfil(usuario.getFotoPerfil());
        dto.setTipoUsuario(usuario.getTipoUsuario());
        dto.setCriadoEm(usuario.getCriadoEm());
        dto.setMediaEstrelas(avaliacaoService.calcularMedia(usuario.getId()));
        dto.setComentarios(avaliacaoService.listar(usuario.getId()));
        return dto;
    }
}
