package com.shakalinux.apicardaio.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.*;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Cardápio Delivery – Documentação Oficial",
                version = "1.0.0",
                description = """
                         **API Completa para Sistemas de Delivery/Cardápio Digital.**

                        Esta API robusta e escalável foi desenvolvida para ser o *backend* de um sistema de pedidos e cardápio online,
                        facilitando a gestão completa de um negócio de alimentação.

                        **Funcionalidades Chave:**
                        * **Autenticação JWT:** Sistema seguro para usuários e administradores.
                        * **Gestão de Estoque:** Controle de Produtos e Categorias.
                        * **Processamento de Pedidos:** Fluxo completo de criação e acompanhamento de pedidos.
                        * **Segurança:** Painel **ADMIN** com controle de acesso baseado em *roles* (RBAC).

                        **Atenção:** Todas as rotas administrativas/de escrita exigem autenticação com **Bearer Token (JWT).**
                        """,
                contact = @Contact(
                        name = "Henrique – Shakalinux",
                        email = "hrprocha3@outlook.com",
                        url = "https://Shakalinux.github.io"

                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Ambiente de Desenvolvimento Local"),
                @Server(url = "https://api-cardapio.shaka.com", description = "Ambiente de Produção (Live)")
        }
)
@SecurityScheme(
        name = "BearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Informe o **JWT Bearer Token** obtido no endpoint de autenticação para acessar rotas protegidas."
)
public class OpenApiConfig {}