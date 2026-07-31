package com.futebol.security;

import com.futebol.entity.Usuario;
import com.futebol.repository.UsuarioRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        String nome = oauth2User.getAttribute("name");
        String foto = oauth2User.getAttribute("picture");

        // Busca ou cria o usuário no seu banco de dados
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseGet(() -> {
                    Usuario novoUsuario = new Usuario();
                    novoUsuario.setEmail(email);
                    // Preencha campos obrigatórios da entidade Usuario
                    novoUsuario.setNome(nome);
                    novoUsuario.setFotoPerfil(foto);
                    novoUsuario.setSenha("google_auth"); // Campo necessário pela entidade
                    novoUsuario.setTipoUsuario(com.futebol.enums.TipoUsuario.JOGADOR); // Defina um padrão
                    return usuarioRepository.save(novoUsuario);
                });

        // Gera o token JWT para o usuário
        String token = jwtService.gerar(usuario.getId());

        // Redireciona para o frontend passando o token na URL (ou configurando um cookie)
        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:5173/oauth2/redirect")
                .queryParam("token", token)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
