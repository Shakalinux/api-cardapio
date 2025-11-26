package com.shakalinux.apicardaio.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Estrutura dos dados necessários para registrar um novo usuário ou administrador.")
public class UsuarioRegisterDTO {

    @Schema(
            description = "Nome de usuário (único) a ser utilizado para login.",
            example = "cliente_shaka"
    )
    private String username;

    @Schema(
            description = "Senha do usuário. Deve atender aos requisitos de segurança.",
            example = "SenhaSegura123!"
    )
    private String password;
}