package com.futebol.service;

import com.futebol.config.BusinessException;
import com.futebol.dto.FotoDTO;
import com.futebol.entity.FotoJogo;
import com.futebol.entity.Time;
import com.futebol.entity.Usuario;
import com.futebol.repository.FotoJogoRepository;
import com.futebol.repository.MembroTimeRepository;
import com.futebol.repository.TimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FotoJogoService {

    private static final long TAMANHO_MAXIMO_BYTES = 5 * 1024 * 1024; // 5MB

    private final FotoJogoRepository repo;
    private final TimeRepository timeRepo;
    private final MembroTimeRepository membroRepo;

    @Transactional
    public FotoDTO.Response postar(MultipartFile arquivo, String comentario, String timeId, Usuario autor) {
        if (autor == null)
            throw new BusinessException("Usuário não autenticado");

        if (arquivo == null || arquivo.isEmpty())
            throw new BusinessException("Selecione uma foto para enviar");

        String tipo = arquivo.getContentType();
        if (tipo == null || !tipo.startsWith("image/"))
            throw new BusinessException("Arquivo enviado não é uma imagem");

        if (arquivo.getSize() > TAMANHO_MAXIMO_BYTES)
            throw new BusinessException("Imagem muito grande. Máximo 5MB");

        if (comentario != null && comentario.length() > 500)
            throw new BusinessException("Comentário muito longo (máximo 500 caracteres)");

        Time time = null;
        if (timeId != null && !timeId.isBlank()) {
            time = timeRepo.findById(timeId)
                    .orElseThrow(() -> new BusinessException("Time não encontrado"));

            boolean ehDono = time.getUsuario().getId().equals(autor.getId());
            boolean ehMembro = membroRepo.existsByTimeIdAndUsuarioId(timeId, autor.getId());

            if (!ehDono && !ehMembro)
                throw new BusinessException("Você precisa ser dono ou membro do time para postar fotos dele");
        }

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            net.coobird.thumbnailator.Thumbnails
                    .of(arquivo.getInputStream())
                    .size(1280, 1280)
                    .outputQuality(0.85)
                    .outputFormat("jpg")
                    .toOutputStream(out);

            FotoJogo foto = FotoJogo.builder()
                    .autor(autor)
                    .time(time)
                    .imagem(out.toByteArray())
                    .comentario(comentario != null && !comentario.isBlank() ? comentario.trim() : null)
                    .build();

            return toResponse(repo.save(foto));
        } catch (IOException e) {
            throw new BusinessException("Erro ao processar a imagem: " + e.getMessage());
        }
    }

    public List<FotoDTO.Response> listarPorTime(String timeId) {
        return repo.findByTimeIdOrderByCriadoEmDesc(timeId).stream().map(this::toResponse).toList();
    }

    public List<FotoDTO.Response> listarPorAutor(String autorId) {
        return repo.findByAutorIdOrderByCriadoEmDesc(autorId).stream().map(this::toResponse).toList();
    }

    public byte[] getImagemBytes(String fotoId) {
        return repo.findById(fotoId)
                .orElseThrow(() -> new BusinessException("Foto não encontrada"))
                .getImagem();
    }

    @Transactional
    public void deletar(String fotoId, Usuario usuario) {
        FotoJogo foto = repo.findById(fotoId)
                .orElseThrow(() -> new BusinessException("Foto não encontrada"));

        boolean ehAutor = foto.getAutor().getId().equals(usuario.getId());
        boolean ehDonoDoTime = foto.getTime() != null
                && foto.getTime().getUsuario().getId().equals(usuario.getId());

        if (!ehAutor && !ehDonoDoTime)
            throw new BusinessException("Sem permissão para remover esta foto");

        repo.delete(foto);
    }

    private FotoDTO.Response toResponse(FotoJogo f) {
        return FotoDTO.Response.builder()
                .id(f.getId())
                .autorId(f.getAutor().getId())
                .autorNome(f.getAutor().getNome())
                .autorFoto(f.getAutor().getFotoPerfil())
                .timeId(f.getTime() != null ? f.getTime().getId() : null)
                .timeNome(f.getTime() != null ? f.getTime().getNome() : null)
                .comentario(f.getComentario())
                .imagemUrl("/api/fotos/" + f.getId() + "/imagem")
                .criadoEm(f.getCriadoEm())
                .build();
    }
}
