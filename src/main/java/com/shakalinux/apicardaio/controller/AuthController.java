package com.shakalinux.apicardaio.controller;

import com.shakalinux.apicardaio.component.JwtUtil;
import com.shakalinux.apicardaio.exception.BadRequestException;
import com.shakalinux.apicardaio.exception.UnauthorizedException;
import com.shakalinux.apicardaio.model.Usuario;
import com.shakalinux.apicardaio.model.enums.StatusPedido;
import com.shakalinux.apicardaio.service.PedidoService;
import com.shakalinux.apicardaio.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation; // Importação importante
import io.swagger.v3.oas.annotations.responses.ApiResponse; // Importação importante
import io.swagger.v3.oas.annotations.responses.ApiResponses; // Importação importante
import io.swagger.v3.oas.annotations.tags.Tag; // Importação importante
import io.swagger.v3.oas.annotations.security.SecurityRequirement; // Importação importante (para rotas futuras, se for o caso)
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação (JWT)", description = "Gerenciamento de acesso e geração de tokens JWT para usuários e administradores.")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Operation(
            summary = "Realizar Login e obter Token JWT",
            description = "Autentica um usuário existente (Cliente ou Admin) com nome de usuário e senha, retornando um token de acesso JWT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login bem-sucedido. Retorna o token JWT."),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas (usuário não encontrado ou senha incorreta).")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String senha = body.get("password");

        Usuario user = usuarioService.findByNome(username)
                .orElseThrow(() -> new UnauthorizedException("Usuário não encontrado"));

        if (!passwordEncoder.matches(senha, user.getSenha())) {
            throw new UnauthorizedException("Senha inválida");
        }

        String token = jwtUtil.generateToken(username, user.getRole());

        return ResponseEntity.ok(Map.of("token", token));
    }



    @Operation(
            summary = "Registrar novo usuário (Cliente)",
            description = "Cria uma nova conta de usuário com a role padrão (CLIENTE)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados de registro inválidos (ex: nome de usuário já existe).")
    })
    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody Map<String, String> body) {

        String username = body.get("username");
        String senha = body.get("password");


        usuarioService.registrar(username, senha);

        return ResponseEntity.ok(
                Map.of("mensagem", "Usuário criado com sucesso")
        );
    }


    @Operation(
            summary = "Registrar novo administrador",
            description = "Cria uma nova conta com a role **ADMIN**. **Este endpoint deve ser protegido em produção (ex: por chave mestra ou apenas em ambiente DEV/configuração inicial).**"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Administrador criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados de registro inválidos.")

    })

    @PostMapping("/register-admin")
    public ResponseEntity<?> registrarAdmin(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String senha = body.get("password");
        usuarioService.registrarAdmin(username, senha);
        return ResponseEntity.ok(Map.of("mensagem", "Admin criado com sucesso"));
    }
}