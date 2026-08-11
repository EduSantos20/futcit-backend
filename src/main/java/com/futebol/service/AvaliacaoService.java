package com.futebol.service;

import com.futebol.dto.AvaliacaoDTO;
import com.futebol.entity.Avaliacao;
import com.futebol.entity.Usuario;
import com.futebol.repository.AvaliacaoRepository;
import com.futebol.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {
    private final AvaliacaoRepository repo;
    private final UsuarioRepository usuarioRepo;
    @Transactional
    public AvaliacaoDTO.Response avaliar(AvaliacaoDTO.Request req, Usuario avaliador) {
        Usuario avaliado = usuarioRepo.findById(req.getAvaliadoId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Avaliacao a = Avaliacao.builder()
                .avaliador(avaliador)
                .avaliado(avaliado)
                .nota(req.getNota())
                .comentarios(req.getComentarios())
                .build();

        return toResponse(repo.save(a));
    }

    public List<AvaliacaoDTO.Response> listar(String avaliadoId) {
        return repo.findByAvaliadoIdOrderByCriadoEmDesc(avaliadoId).stream().map(this::toResponse).toList();
    }

    public Double calcularMedia(String avaliadoId) {
        Double media = repo.mediaEstrelas(avaliadoId);
        return media != null ? media : 0.0;
    }

    private AvaliacaoDTO.Response toResponse(Avaliacao a) {
        AvaliacaoDTO.Response r = new AvaliacaoDTO.Response();
        r.setId(a.getId());
        r.setAvaliadorNome(a.getAvaliador().getNome());
        r.setNota(a.getNota());
        r.setComentarios(a.getComentarios());
        r.setCriadoEm(a.getCriadoEm());
        return r;
    }
}
